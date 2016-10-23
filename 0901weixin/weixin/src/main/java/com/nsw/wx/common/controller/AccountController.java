package com.nsw.wx.common.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import com.nsw.wx.common.service.*;
import net.sf.json.JSONObject;

import org.bson.types.ObjectId;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nsw.wx.api.util.AccessTokenHelper;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.docmodel.SysLog;
import com.nsw.wx.common.job.DeletetAccountDataJob;
import com.nsw.wx.common.job.LoadFansInfo;
import com.nsw.wx.common.model.Site;
import com.nsw.wx.common.security.CustomSecurityUser;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;
import com.nsw.wx.common.views.Message;


/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月6日 下午2:49:17
* @Description: 系统用户管理
 */
@Controller
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private WxAccountService wxAccountService ;
	
	@Autowired
	private LogService logService;
	
	@Value("${wxurl}")
	private String wxurl;  //本地访问路径
	
	@Value("${7Niu.domain}")
	private String fastDfsServerUrl;
	
	
	@Autowired
	private FTPService ftpService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private FancService fancService ;
	
	
	/**
	 * 
	* @Description: 获取账户所在微信账号的列表        开通服务初始化默认是两个账户，多的需要收费
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "getWxAccountList", method = RequestMethod.GET)
	@ResponseBody
	public String getWxAccountList(String filter) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CustomSecurityUser securityUser = ContextUtil.getLoginUser();
			boolean flag =  userService.checkUserPermission(securityUser.getId());
			List<Map<String,Object>> accountList = 	 new ArrayList<Map<String,Object>>();
			if(flag){
				if(filter==null){
					 accountList = 	 wxAccountService.getWxAccountList(securityUser.getSite());
				}else{
					 accountList = 	 wxAccountService.getWxAccountListByFilter(securityUser.getSite(), filter);
				}
				if(accountList == null ){
					accountList = new ArrayList<Map<String,Object>>();
				}
				//新账号加入newAccount字段
				for(Map<String,Object> map:accountList){
					String create_time = map.get("create_time")+"";
					Date date = df.parse(create_time);
					long timediff = (new Date().getTime() - date.getTime())/(1000 * 60 * 60 * 24);
					map.put("newAccount", timediff <= 3 ? true :false);
					if(map.get("head_img")!=null && !"".equals(map.get("head_img")) && map.get("head_img").toString().indexOf("http") == -1){
						map.put("head_img",fastDfsServerUrl+map.get("head_img"));
					}
					map.put("id", map.get("_id")+"");
				}
			}
			return AjaxUtil.renderSuccessMsg(accountList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxUtil.renderFailMsg("获取微信账户列表出错！");
		}
	}
	
	/**
	 * 
	* @Description:获取微信账号信息
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "wxAccountInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getWxAccountInfo(String id) {
		try {
			Map<String,Object> account = 	 wxAccountService.getAccount(id);
			if(account == null ){
				return AjaxUtil.renderFailMsg("没有找到账号信息!");
			}
			account.put("id", account.get("_id")+"");
			
			return AjaxUtil.renderSuccessMsg(account);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("获取微信账户信息出错！");
	}
	
	
	@RequestMapping(value = "wxAccountInfo", method = RequestMethod.PUT)
	@ResponseBody
	public String UpdateWxAccount(@RequestBody Map<String,Object> entity) {
		try {
			
			Update update = new Update();
			update.set("head_img", entity.get("head_img")+"".replace(fastDfsServerUrl, ""));
			update.set("verify_type_info", entity.get("verify_type_info"));
			update.set("bindType", entity.get("bindType"));
			update.set("name", entity.get("name"));
			update.set("appSecret", entity.get("appSecret"));
			update.set("nick_name", entity.get("nick_name"));
			Map<String,Object> account =  wxAccountService.getAccountByAppId(entity.get("appId")+"");
			
			if(account!=null){
				if(!account.get("appSecret").toString().equals(entity.get("appSecret").toString())){
					//检测是否修改过appSecret  修改过要校验
					Message msg = checkoutAccount(entity.get("appId")+"",entity.get("appSecret")+"");
					if(!msg.getIsSuc()){
						return AjaxUtil.renderFailMsg("请填写正确的appSecret！");
					}
				}
				//如果认证类型发生更改，拉取粉丝信息
				if(Integer.parseInt(entity.get("verify_type_info")+"")== 0 && Integer.parseInt(account.get("verify_type_info")+"")== -1){
					//拉取粉丝信息
					fancService.syncJobDo(entity.get("appId")+"");
				}
				
			}
			boolean result =  wxAccountService.updateAccount(entity, update);
			if(result){
				SysLog log = new SysLog("acount_edit",entity.get("appId")+"","user","修改公众号");
				logService.saveLog(log);
				entity.put("id", entity.get("_id")+"");
				return AjaxUtil.renderSuccessMsg(entity);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("修改失败！");
	}
	
	
	@RequestMapping(value = "wxAccountInfo", method = RequestMethod.DELETE)
	@ResponseBody
	public String delWxAccount( String id) {
		try {
			if(id != null){
				Map<String,Object> account = wxAccountService.getAccount(id);
				boolean result = 	 wxAccountService.deleteAccount(account);
				if(result ){
					//启动job删除用户不必要数据
					
					SchedulerFactory sf = new StdSchedulerFactory();
					Scheduler sched = sf.getScheduler();
					
					Date startTime = nextGivenSecondDate(null, 1);
					JobDataMap jobMap = new JobDataMap();
					jobMap.put("appId", account.get("appId")+"");
					String taskId = UUID.randomUUID().toString().replace("-", "");
					JobDetail job = newJob(DeletetAccountDataJob.class)
							.withIdentity(Constants.TASK_BINDGACCOUNTROUP + taskId,
									Constants.TASKTIME_GROUP + taskId)
							.setJobData(jobMap).build();
					SimpleTrigger trigger = newTrigger()
							.withIdentity(Constants.TASKTIME_TRIGGER + taskId,
									Constants.TASKTIME_GROUP + taskId)
							.startAt(startTime).withSchedule(simpleSchedule()).build();
					sched.scheduleJob(job, trigger);
					sched.start();
					
					SysLog log = new SysLog("account_del",account.get("appId")+"","user","删除公众号");
					logService.saveLog(log);
					
					
					return AjaxUtil.renderSuccessMsg("删除成功！");
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("删除失败！");
		
	}
	
	
	
	
	@RequestMapping(value = "wxAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public String addWxAccount(@RequestBody Account entity) {
		try {
			CustomSecurityUser securityUser = ContextUtil.getLoginUser();
			if(securityUser!=null){
				boolean flag =  userService.checkUserPermission(securityUser.getId());
				if(!flag){
					return AjaxUtil.renderFailMsg("没有权限进行此操作！");
				}
				Map<String,Object> entitys = 	wxAccountService.getAccountByAppId(entity.getAppId());
				Site site =  ftpService.findSite(Long.parseLong(securityUser.getSite()), 7);
				if(site!=null){
					//能够开通的微信号数
					long accountNum =  site.getWechatPublicAccountNum();
					//已开通的微信号数
					long nowAccountNum =  wxAccountService.getWxAccountList(securityUser.getSite()).size();
					if(nowAccountNum+1>accountNum){
						return AjaxUtil.renderFailMsg("非常抱歉，您的公众号数量超过限制，请联系牛商网客服为您开通！");
					}
				}else{
					return AjaxUtil.renderFailMsg("请先开通微信业务!");
				}
				
				if(entitys != null){
					return AjaxUtil.renderFailMsg("AppID为  "+entity.getAppId()+" 的账户已被添加!");
				}
				Map<String,Object> en = new HashMap<String, Object>();
			    ObjectId id = new ObjectId();
				en.put("_id", id);
				en.put("type", 3);
				en.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				en.put("update_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				en.put("token", getRandomString(6));
				en.put("url", wxurl +"msg/"+entity.getAppId());
				en.put("site", securityUser.getSite() );
				en.put("nick_name", entity.getNick_name() );
				en.put("head_img", entity.getHead_img() );
				en.put("verify_type_info", entity.getVerify_type_info());
				en.put("bindType", entity.getBindType());
				en.put("appId", entity.getAppId());
				en.put("appSecret", entity.getAppSecret());
				en.put("user_name", entity.getUser_name());
				en.put("name", entity.getName());
				
				//判断用户填写的appId和AppSecret是否合法
				Message msg = checkoutAccount(entity.getAppId(),entity.getAppSecret());
				if(msg.getIsSuc()){
					JSONObject  access_token = (JSONObject) msg.getData();
						en.put("authorizer_access_token", access_token.getString("access_token"));
						en.put("refresh_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						if(wxAccountService.getAccountByAppId(entity.getAppId())==null){
							wxAccountService.addAccount(en);
						    en.put("id", id.toString());
							SysLog log = new SysLog("account_add",entity.getAppId(),"user","添加公众号");
							logService.saveLog(log);
							return AjaxUtil.renderSuccessMsg(en);
						}
				}else{
					return AjaxUtil.renderFailMsg("添加失败,请填写正确的AppID和AppSecret!");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("添加失败！");
	}
	
	
	
	
	
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }     
	
    /**
     * 
    * @Description: 判断填写的账户信息是否合法
    * @param @param appId
    * @param @param appSecret
    * @param @return   
    * @return boolean  
    * @throws
     */
	public Message checkoutAccount(String appId,String appSecret){
		Message msg = new Message();
		AccessTokenHelper service = new AccessTokenHelper();
		JSONObject   result =  service.getAccessToken(appId, appSecret);	
		if(result.containsKey("access_token")){
			msg.setIsSuc(true);
			msg.setData(result);
		}else{
			msg.setIsSuc(false);
		}
		return msg;
	}
    
    
	
	

}

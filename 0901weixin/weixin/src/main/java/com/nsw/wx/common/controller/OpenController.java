package com.nsw.wx.common.controller;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;

import com.nsw.wx.common.service.*;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.WriteResult;
import com.nsw.wx.api.service.OpenWxService;
import com.nsw.wx.api.service.imp.OpenWxServiceImp;
import com.nsw.wx.api.util.WXBizMsgCrypt;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.docmodel.SysLog;
import com.nsw.wx.common.job.AttentionJob;
import com.nsw.wx.common.job.DeletetAccountDataJob;
import com.nsw.wx.common.job.LoadFansInfo;
import com.nsw.wx.common.job.RefreshBindAcountJob;
import com.nsw.wx.common.model.Site;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.security.CustomSecurityUser;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;
import com.nsw.wx.common.util.DateUtils;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月14日 下午3:07:43
* @Description: TODO
 */
@Controller
@RequestMapping("/oauth")
public class OpenController {

	private Logger logger = Logger.getLogger(OpenController.class);
	
	@Value("${wxurl}")
	private String wxurl;  //本地访问路径
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private OpenCommonService openCommonService;
	@Autowired
	private WxAccountService wxAccountService;
	
	OpenWxService service = new OpenWxServiceImp();
	
	@Autowired
	private FTPService ftpService;
	
	@Autowired
	private LogService logService;

	@Autowired
	private FancService fancService ;

	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private NewsService  newsService ;
	
	@Value("${appid}")
	private  String TOKENPREFIX ;
	
	 /**
     * 授权事件接收
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "/handle")
    public void acceptAuthorizeEvent(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//ticket缓存和处理   
    	 processAuthorizeEvent(request);
         output(response, "success"); // 输出响应的内容。
    }
    
    @RequestMapping(value = "/authorCallback")
    public void authorCallback(HttpServletRequest request, HttpServletResponse response) throws IOException,  DocumentException {
    	
    	String auth_code = request.getParameter("auth_code");
    	String state = "success" ;
    	String site = request.getParameter("site");
    	if(site==null){
    		return;
    	}
    	Map<String,Object> componAccount =  openCommonService.getComponentAccessToken("1");
    	//1.使用授权码换取公众号的接口调用凭据和授权信息
    	OpenWxService service = new OpenWxServiceImp();
    	JSONObject query = new JSONObject();
    	
    	query.put("component_appid", componAccount.get("appId"));
    	query.put("authorization_code", auth_code);
    	JSONObject entity =  service.getAuthorizerAccessTokenAndRefreshToken(componAccount.get("component_access_token")+"", query,null);
    	logger.info("授权成功过程  1.使用授权码换取公众号的接口调用凭据和授权信息->"+entity);
    	if(entity.get("errcode") != null && "40001" .equals(entity.get("errcode"))){
    		logger.error("@@@@@@@@@@@@@@@@@@@@ 授权成功后调用获取授权方令牌（authorizer_access_token）出错--->@@@@@@@@@@@@@@，错误返回信息为:"+entity);
    	}
    	
    	
    	
    	
    	
    	Map<String,Object>  newAccountEntity = new HashMap<String, Object>();
    	newAccountEntity.put("create_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    	newAccountEntity.put("type", 2);//2为授权绑定
    	//2.入库操作，缓存票据
    	if(entity != null && entity.get("authorization_info") != null){
    		entity = entity.getJSONObject("authorization_info");
    		String authorizer_appid = entity.get("authorizer_appid")+"";
    		//缓存到redis
    		if(redisTemplate.opsForValue().get(TOKENPREFIX+authorizer_appid) !=null){
    			redisTemplate.delete(TOKENPREFIX+authorizer_appid);
    		}
    		redisTemplate.opsForValue().set(TOKENPREFIX+authorizer_appid, entity.get("authorizer_access_token")+"", 100, TimeUnit.MINUTES);
    		
    		newAccountEntity.put("appId",authorizer_appid);
    		newAccountEntity.put("authorizer_access_token",entity.get("authorizer_access_token")+"");
    		newAccountEntity.put("authorizer_refresh_token",entity.get("authorizer_refresh_token")+"");
    		newAccountEntity.put("update_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    		newAccountEntity.put("refresh_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    		
    		//3.获取授权方的公众号帐号基本信息并入库
        	JSONObject jquery = new JSONObject();
        	jquery.put("component_appid", componAccount.get("appId"));
        	jquery.put("authorizer_appid", entity.get("authorizer_appid"));
        	JSONObject authorInfo =  service.getAuthorizerInfo(componAccount.get("component_access_token")+"", jquery,null);
        	if(authorInfo != null && authorInfo.get("authorizer_info") != null){
        		logger.info("授权成功过程  2.获取授权方的公众号帐号基本信息->"+authorInfo);
        		authorInfo = authorInfo.getJSONObject("authorizer_info");
        		newAccountEntity.put("nick_name",authorInfo.get("nick_name")+"");
        		newAccountEntity.put("head_img",authorInfo.get("head_img")+"");
        		int bindType = Integer.parseInt(authorInfo.getJSONObject("service_type_info").get("id")+"");
        		bindType=bindType==1?0:bindType;
        		newAccountEntity.put("bindType",bindType);
        		newAccountEntity.put("verify_type_info",Integer.parseInt(authorInfo.getJSONObject("verify_type_info").get("id")+""));
        		newAccountEntity.put("user_name",authorInfo.get("user_name")+"");
        		newAccountEntity.put("qrcode_url",authorInfo.get("qrcode_url")+"");
        		Map<String,Object> authorInfos = wxAccountService.getAccountByAppId(authorizer_appid);
        		if(authorInfos != null){//重新授权的
        			wxAccountService.deleteAccount(authorInfos);
        			newAccountEntity.put("site",authorInfos.get("site")+"");
        		}else{
        			newAccountEntity.put("site",site);
        			Site sites =  ftpService.findSite(Long.parseLong(site) , 7);
        			if(site!=null){
        				//能够开通的微信号数
        				long accountNum =  sites.getWechatPublicAccountNum();
        				//已开通的微信号数
        				long nowAccountNum =  wxAccountService.getWxAccountList(site).size();
        				if(nowAccountNum+1>accountNum){
        					state = "fail";
        				}
        			}
        		}
        		if("success".equals(state)){
            		Map<String,Object> mentity =  wxAccountService.addAccount(newAccountEntity);
			         //同步图文信息
			         newsService.syncNews(entity.get("authorizer_appid")+"");
            		if(mentity != null &&  Integer.parseInt(mentity.get("verify_type_info")+"") != -1){
						//同步粉丝信息
            			fancService.syncJobDo(entity.get("authorizer_appid")+"");
            		}
        			response.sendRedirect(request.getContextPath() +"/app/index.html#/wechat/account/authorizeMain/success?state=success&id="+mentity.get("_id")+"");
        		}else{//授权失败
        			response.sendRedirect(request.getContextPath() +"/app/index.html#/wechat/account/authorizeMain/success?state=fail");
        		}
        		
        	
        	}
        	
    	}
    	
    	
    	
    }
    
    @RequestMapping(value = "/authorCheck", method = RequestMethod.GET)
    @ResponseBody
    public String authorCheck(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
		CustomSecurityUser user =  ContextUtil.getLoginUser();
		
		if(user!=null){
			Site  site = 	ftpService.findSite(Long.parseLong(user.getSite()) , 7);
			    if(site==null){
			    	return AjaxUtil.renderFailMsg("请开通服务后再操作!");
			    }else{
			    	boolean flag =  userService.checkUserPermission(user.getId());
			    	if(flag){
				    	//能够开通的微信号数
						long accountNum =  site.getWechatPublicAccountNum();
						//已开通的微信号数
						long nowAccountNum =  wxAccountService.getWxAccountList(user.getSite()).size();
						if(nowAccountNum+1>accountNum){
							return AjaxUtil.renderFailMsg("非常抱歉，您的公众号数量超过限制，请联系牛商网客服为您开通！");
						}
			    	}else{
			    		 return AjaxUtil.renderFailMsg("角色权限不够");
			    	}
					 return AjaxUtil.renderSuccessMsg("");
			    }
		}
		 return AjaxUtil.renderFailMsg("获取账号信息出错");
	   
		
	    
    }
    
    
    
    /**
     * 一键授权功能
     * @param request
     * @param response
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "/goAuthor")
    public void goAuthor(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
    	
    	try {
    		CustomSecurityUser user =  ContextUtil.getLoginUser();
    		if(user!=null){
    			boolean flag =  userService.checkUserPermission(user.getId());
    			if(flag){
    				//从数据库根据类型查询出第三方平台的componentAccessToken
    	    		Map<String,Object> componentAccessToken =  openCommonService.getComponentAccessToken("1");
    	    		//componentAccessToken = openCommonService.isExpireAndRefresh(componentAccessToken);
    	    		OpenWxService service = new OpenWxServiceImp();
    	    		JSONObject component_appid = new JSONObject();
    	    		component_appid.put("component_appid", componentAccessToken.get("appId")+"");
    	    		//得到component_access_token的值
    	    		String component_access_tokenStr = componentAccessToken.get("component_access_token")+"";
    	    		//从第三方平台获取预授权码
    	    		JSONObject preAuthResult  =  service.getPreAuthCode(component_access_tokenStr, component_appid,null);
    				//预授权码值
    				String preAuthCode= preAuthResult.getString("pre_auth_code");
    				String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="+componentAccessToken.get("appId")+""+"&pre_auth_code="+preAuthCode+"&redirect_uri="+wxurl+"oauth/authorCallback?site="+user.getSite();
    				response.sendRedirect(url);
    			}else{
    				logger.error("该角色没有一件授权权限");
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    
    	
    }
    
    
    
    /**
     * 处理授权事件的推送
     * 
     * @param request
     * @throws IOException
     * @throws DocumentException
     */
    public void processAuthorizeEvent(HttpServletRequest request) throws Exception{
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String signature = request.getParameter("signature");
        String msgSignature = request.getParameter("msg_signature");
        
        Map<String,Object> commonAccount =  openCommonService.getComponentAccessToken("1");
        if (!StringUtils.isNotBlank(msgSignature))
            return;// 微信推送给第三方开放平台的消息一定是加过密的，无消息加密无法解密消息
        boolean isValid = checkSignature(commonAccount.get("token")+"", signature, timestamp, nonce);
        if (isValid) {
            StringBuilder sb = new StringBuilder();
            BufferedReader in = request.getReader();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String xml = sb.toString();
             String encodingAesKey =  commonAccount.get("encodingAesKey")+"";// 第三方平台组件加密密钥
            WXBizMsgCrypt pc = new WXBizMsgCrypt(commonAccount.get("token")+"", encodingAesKey, commonAccount.get("appId")+"");
            xml = pc.decryptMsgTs(msgSignature, timestamp, nonce, xml);
           // 对ComponentVerifyTicket进行缓存或更新
            saveOrUpdateTicket(xml);
            //对 componentAccessToken进行检查
            componentAccessTokenCheck(xml);
        }
    }
    
    /**
     * 对 componentAccessToken进行检查或缓存
     * @param xml
     */
    void  componentAccessTokenCheck(String xml){
    	Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			String appId = rootElt.elementText("AppId");
			
			if(!"".equals(appId) && appId != null){	
				 Map<String,Object> commonAccount =  openCommonService.getComponentAccessToken("1");
				
		    		Criteria criteria=Criteria.where("appId").is(appId).and("type").is(1);
			    	Query que = new Query();
			    	que.addCriteria(criteria);
			    	Update update =  new Update();
			    	if(commonAccount.get("component_access_token") == null ){
			    		JSONObject ob = new JSONObject();
			    		ob.put("component_appid", commonAccount.get("appId")+"");
			    		ob.put("component_appsecret",  commonAccount.get("appSecret")+"");
			    		ob.put("component_verify_ticket", commonAccount.get("component_verify_ticket"));
			    		JSONObject  componentAccessTokens =     service.getComponentAccessToken(ob,null);
			    		if(componentAccessTokens.get("component_access_token") != null){
			    			update.set("component_access_token",  componentAccessTokens.getString("component_access_token"));
			    			update.set("refresh_time", DateUtils.getCurrentTime());
			    			if(baseMongoTemplate.updateOne(que, update, Constants.WXACCOUNT_T)){
			    				logger.info("更新 componentAccessTokenCheck 成功!");
			    			}
			    			
			    		}
			    	
			    		
			    		
			    	}
			    	
			}
			//消息类型
			String InfoType = rootElt.elementText("InfoType");
			
			if(!"".equals(InfoType) && InfoType != null){
				String authorizerAppid =  rootElt.elementText("AuthorizerAppid");
				//取消授权
				if("unauthorized".equals(InfoType)){
					SysLog log = new SysLog("unauthorized",appId,"user","公众号取消授权");
					logService.saveLog(log);
					logger.info("#######################授权事件:公众号"+authorizerAppid+"正在取消授权############################");
				//授权	
				}else if("authorized".equals(InfoType)){
					SysLog log = new SysLog("authorized",appId,"user","公众号授权");
					logService.saveLog(log);
					logger.info("#######################授权事件:公众号"+authorizerAppid+"正在进行授权############################");
				//更新授权
				}else if("updateauthorized".equals(InfoType)){
					SysLog log = new SysLog("updateauthorized",appId,"user","公众号更新授权");
					logService.saveLog(log);
					logger.info("#######################授权事件:公众号"+authorizerAppid+"正在更新授权############################");
					
				}
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 对ComponentVerifyTicket进行缓存或更新
     * @param xml
     */
    void saveOrUpdateTicket(String xml){
    	Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			String ticket = rootElt.elementText("ComponentVerifyTicket");
			String appId = rootElt.elementText("AppId");
			
			if(!"".equals(ticket)&&ticket!=null){
			    	Criteria criteria=Criteria.where("appId").is(appId).and("type").is(1);
			    	Query que = new Query();
			    	que.addCriteria(criteria);
			    	Update update = new Update(); 
			    	update.set("component_verify_ticket", ticket);
			    	update.set("update_time",DateUtils.getCurrentTime());
			    	boolean status =  baseMongoTemplate.updateOne(que, update, Constants.WXACCOUNT_T);
			    	
//			    	WriteResult wr =  mongoTemplate.updateFirst(que, update,Constants.WXACCOUNT_T);
//			    	boolean  status =wr.getN()==1?true:false;
			    	if(status){
			    		logger.info(("##############推送component_verify_ticket成功-->ticket = "+ticket+"###################,更新到数据库状态? "+status));
			    	}
			 }
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 获取授权的Appid
     * @param xml
     * @return
     */
	String getAuthorizerAppidFromXml(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			String toUserName = rootElt.elementText("ToUserName");
			return toUserName;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
 
    /**
     * 工具类：回复微信服务器"文本消息"
     * @param response
     * @param returnvaleue
     */
    public void output(HttpServletResponse response,String returnvaleue){
		try {
			PrintWriter pw = response.getWriter();
			pw.write(returnvaleue);
//			System.out.println("****************returnvaleue***************="+returnvaleue);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 判断是否加密
     * @param token
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String token,String signature,String timestamp,String nonce){
      //  System.out.println("###token:"+token+";signature:"+signature+";timestamp:"+timestamp+"nonce:"+nonce);
    	   boolean flag = false;
    	   if(signature!=null && !signature.equals("") && timestamp!=null && !timestamp.equals("") && nonce!=null && !nonce.equals("")){
    	      String sha1 = "";
    	      String[] ss = new String[] { token, timestamp, nonce }; 
              Arrays.sort(ss);  
              for (String s : ss) {  
               sha1 += s;  
              }  
     
              sha1 = AddSHA1.SHA1(sha1);  
     
              if (sha1.equals(signature)){
        	   flag = true;
              }
    	   }
    	   return flag;
       }
}


class AddSHA1 {
    public static String SHA1(String inStr) {
        MessageDigest md = null;
        String outStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");     //选择SHA-1，也可以选择MD5
            byte[] digest = md.digest(inStr.getBytes());       //返回的是byet[]，要转化为String存储比较方便
            outStr = bytetoString(digest);
        }
        catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return outStr;
    }
    
    
    public static String bytetoString(byte[] digest) {
        String str = "";
        String tempStr = "";
        
        for (int i = 0; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            }
            else {
                str = str + tempStr;
            }
        }
        return str.toLowerCase();
    }
}

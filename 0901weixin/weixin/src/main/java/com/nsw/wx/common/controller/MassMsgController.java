package com.nsw.wx.common.controller;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nsw.wx.api.util.JSONHelper;
import com.nsw.wx.common.service.*;
import com.nsw.wx.common.util.*;
import com.nsw.wx.common.views.Message;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.nsw.wx.api.model.Article;
import com.nsw.wx.api.model.Media;
import com.nsw.wx.api.model.News;
import com.nsw.wx.api.model.mass.ImageMass;
import com.nsw.wx.api.model.mass.ImageText;
import com.nsw.wx.api.model.mass.MassFilter;
import com.nsw.wx.api.model.mass.Text;
import com.nsw.wx.api.model.mass.TextMass;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.MessageService;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.api.service.imp.MessageServiceImp;
import com.nsw.wx.common.docmodel.MassMessage;
import com.nsw.wx.common.job.MassMsgJob;
import com.nsw.wx.common.repository.BaseMongoTemplate;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月26日 上午10:45:05
* @Description: 群发管理
 */
@Controller
@RequestMapping("/massmsg")
public class MassMsgController {
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoFileOperationService mongoFileOperationService;

	
	@Autowired
	private FancService fancService;
	
	@Autowired
	private WxAccountService wxAccountService ;

	@Autowired
	private FansTagService fansTagService;
	
	MessageService service  =  new MessageServiceImp();
	
	MaterialService materService = new MaterialServiceImp();
	
	private Logger logger = Logger.getLogger(MassMsgController.class);
	
	@Autowired
	private  FileService fileService;
	
	@Autowired
	private AccessTokenService tokenService;

	@Autowired
	private SendMassMsgService sendMassMsgService;

	/**
	 * 发送到手机预览
	 * @param request
	 * @param response
	 * @param wxNews
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/preViewNews", method = RequestMethod.POST)
	@ResponseBody
	public String preViewNews(HttpServletRequest request,
								   HttpServletResponse response,@RequestBody Map<String,Object> wxNews) {
		try {
			String paraError = null;
			String jsonStr = null;
			if(!StringUtils.isEmpty(wxNews.get("appId")+"")  && !StringUtils.isEmpty(wxNews.get("openId")) && !StringUtils.isEmpty(wxNews.get("articles"))){
				//将前端的信息进行校验  然后上传图文调用预览群发
				JSONObject news = JSONObject.fromObject(wxNews);
				JSONArray arr = news.getJSONArray("articles");
				Map<String,Object> sendJson = new HashMap<>();
				String accessToken =  tokenService.getAccessTokenByRedis(wxNews.get("appId")+"");
				JSONObject js =  sendMassMsgService.UploadNews(arr,wxNews.get("appId")+"",accessToken);
				if (js != null && js.containsKey("media_id")) {
					String media_id = js.getString("media_id");
					Map<String,Object> mpnews = new HashMap<>();
					mpnews.put("media_id",media_id);
					sendJson.put("mpnews",mpnews);
					sendJson.put("msgtype","mpnews");
					jsonStr = JSONObject.fromObject(sendJson).toString();
				}else{
					paraError = js!=null?js.toString():"预览失败";
				}
				if(jsonStr != null ){
					JSONObject  jsonResult = service.massMsgByGroup(jsonStr, accessToken,wxNews.get("appId")+"",wxNews.get("openId")+"");
					if(jsonResult!=null && jsonResult.containsKey("errcode") && jsonResult.getInt("errcode") == 0){
						return AjaxUtil.renderSuccessMsg("发送到手机预览成功");
					}
				}else{
					return AjaxUtil.renderFailMsg("预览失败"+jsonStr);
				}

			}else{
				paraError = "参数错误";
			}
		} catch (Exception e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("预览失败！");
		}
		return AjaxUtil.renderFailMsg("预览失败");
	}
	
	
	@RequestMapping(value = "sendMassMsgByJob", method = RequestMethod.POST)
	@ResponseBody
	public String addJob(@RequestBody MassMessage msg){

		if( !StringUtils.isEmpty(msg.getAppId())  && !StringUtils.isEmpty(msg.getMassType()) && !StringUtils.isEmpty( msg.getJobTime())){
			String uuid = UUID.randomUUID().toString().replace("-","");
			msg.setJobId(uuid);
			//从调度管理器中获取调度对象
			try {
				//检测时间的有效性
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d = df.parse(msg.getJobTime());
				if(d.getTime() < new Date().getTime()){
					return AjaxUtil.renderFailMsg("时间不能小于当前时间!");
				}
				//Message message = new Message();
				String jsonStr = null;//
				String errorMsg = null;
				if(msg.getAppId()!=null && msg.getMassType()!=null) {
					String accessToken = tokenService.getAccessTokenByRedis(msg.getAppId());
					Map<String,Object> filter = new HashMap<>();
					if ("-100".equals(msg.getGroupid())) {//全部用户
						filter.put("is_to_all",true);
						filter.put("tag_id",0);
					} else {//分组
						filter.put("is_to_all",false);
						filter.put("tag_id",Integer.parseInt(msg.getGroupid()));
					}
					Map<String,Object> sendJson = new HashMap<>();
					sendJson.put("filter",filter);
					if ("txt".equals(msg.getMassType()) && msg.getContent() != null) {//群发文字
						Map<String,Object> text = new HashMap<>();
						text.put("content",msg.getContent());
						sendJson.put("text",text);
						sendJson.put("msgtype","text");
						jsonStr = JSONObject.fromObject(sendJson).toString();
					} else if ("pic".equals(msg.getMassType()) && msg.getFileId() != null) {//群发图片
						JSONObject js = sendMassMsgService.UploadTempImage(msg.getAppId(), msg.getFileId(), accessToken, "image");
						if (js != null && js.containsKey("media_id")) {
							String media_id = js.getString("media_id");
							Map<String,Object> image = new HashMap<>();
							image.put("media_id",media_id);
							sendJson.put("msgtype","image");
							sendJson.put("image",image);
							jsonStr = JSONObject.fromObject(sendJson).toString();
						}else{
							errorMsg = js.toString();
						}
					} else if ("news".equals(msg.getMassType()) && msg.getMediaId() != null) {//群发图文
						//素材库拿到图文信息
						JSONObject js = sendMassMsgService.UploadNews(msg, accessToken);
						if (js != null && js.containsKey("media_id")) {
							String media_id = js.getString("media_id");
							Map<String,Object> mpnews = new HashMap<>();
							mpnews.put("media_id",media_id);
							sendJson.put("mpnews",mpnews);
							sendJson.put("msgtype","mpnews");
							jsonStr = JSONObject.fromObject(sendJson).toString();
						}else{
							errorMsg = js.toString();
						}
					}
					if(jsonStr == null){
						return  AjaxUtil.renderFailMsg("定时群发失败!错误信息:"+errorMsg==null?"":errorMsg);
					}
					String str =  JSONHelper.bean2json(msg);
					JSONObject obj = JSONObject.fromObject(str);
					obj.put("msg_id", uuid);
					int totalCount = 0;
					if ("-100".equals(msg.getGroupid())) {
						Map<String,Object> m =  fancService.getFansCount(msg.getAppId());
						totalCount = Integer.parseInt(m.get("allNum")+"");
					}else{
						totalCount =(int) fansTagService.getFansCountByTag(msg.getAppId(),msg.getGroupid());
					}
					//向表masslog追加记录
					Map<String, Object> masslog = new HashMap<String, Object>();
					masslog.put("appId", msg.getAppId());
					masslog.put("ToUserName", msg.getAppId());
					masslog.put("CreateTime", new Date().getTime() / 1000 + "");
					masslog.put("MsgID", uuid);
					masslog.put("Status", "ready");
					masslog.put("TotalCount", totalCount);
					masslog.put("FilterCount", totalCount);
					masslog.put("SentCount", totalCount);
					masslog.put("ErrorCount", "0");
					masslog.put("massType", msg.getMassType());
					masslog.put("jobTime", msg.getJobTime());
					masslog.put("jobId", msg.getJobId());
					masslog.put("jobContent", jsonStr);
					if(baseMongoTemplate.count(Constants.WXMASSMSG_T, obj) < 1){
						baseMongoTemplate.save(Constants.WXMASSMSG_T, obj);
					}
					if(baseMongoTemplate.count(Constants.WXMASSLOG_T,masslog) < 1){
						baseMongoTemplate.save(Constants.WXMASSLOG_T, masslog);
					}
				}

				Scheduler sched = QuartzScheduleMgr.getInstanceScheduler();
				Date runTime = df.parse(msg.getJobTime());
				JobDataMap jobMap = new JobDataMap();
				jobMap.put("massMsg", msg);
				JobDetail job = newJob(MassMsgJob.class).withIdentity("job"+uuid, "massMsg"+uuid).setJobData(jobMap).build();
				//创建一个触发器的名称
				Trigger trigger = newTrigger().withIdentity("trigger", "massMsg"+uuid).startAt(runTime) .build();
				//设置调度相关的Job
				sched.scheduleJob(job, trigger);
				logger.info(job.getKey() + " will run at: " + runTime);
				//启动调度任务
				sched.start();
				logger.info("-------群发定时任务启动成功 ---------"+job.getKey());
				return AjaxUtil.renderSuccessMsg("定时任务启动成功");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("定时任务日期格式错误!");
				return AjaxUtil.renderFailMsg("定时任务日期格式错误");
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("定时任务启动失败!");
				return AjaxUtil.renderFailMsg("定时任务启动失败");
			}
		}else{
			return  AjaxUtil.renderFailMsg("参数错误!");
		}



	}
	
	@RequestMapping(value = "cancelJob", method = RequestMethod.GET)
	@ResponseBody
	public String stopJob( String jobId){
		try {
			JobKey jobKey = new JobKey( "job"+jobId ,"massMsg"+jobId);
			Scheduler sched =  QuartzScheduleMgr.getInstanceScheduler();
			boolean flag =  sched.checkExists(jobKey);
	        logger.info(jobKey + " job是否存在 " + flag);
	        if(flag){
	        	boolean isDelete =  sched.deleteJob(jobKey);
	        	logger.info("-------job删除结果 -----------------"+isDelete);
				if(isDelete){
					Query querys = new Query(Criteria.where("MsgID").is(jobId));
					Update update = new Update();
					update.set("Status","send pause");
					baseMongoTemplate.updateFirst(querys,update,Constants.WXMASSLOG_T);
					return AjaxUtil.renderSuccessMsg("取消成功!");
				}
	        }
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxUtil.renderFailMsg("取消失败!");
		}
		return AjaxUtil.renderFailMsg("取消失败!");
	}
	
	
	
	/**
	 * 
	* @Description: 获取全部用户的省份集合
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "provinceList", method = RequestMethod.GET)
	@ResponseBody
	public String get( String appId){
		if(appId != null){
	    	BasicDBObject queryObject = new BasicDBObject();
	        queryObject.put("appId", appId);
	        String reduce = "function(key,value){ }";
	        DBObject result = mongoTemplate.getCollection(Constants.WXFANS_T).group(new BasicDBObject("province", ""), queryObject,
	                new BasicDBObject(), reduce);
	        Map map = result.toMap();
	        List<String> list = new ArrayList<String>();
	        for (Object value : map.values()) {  
	            JSONObject j = JSONObject.fromObject(value);
	            if(!"".equals(j.getString("province"))){
	            	 list.add(j.getString("province"));
	            }
	        }  
	        return AjaxUtil.renderSuccessMsg(list);
		}
		return AjaxUtil.renderFailMsg("操作失败");
	}
	
	/**
	 * 
	* @Description: 删除图文 和发送失败的诗句
	* @param @param id
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/sendMassMsg", method = RequestMethod.DELETE)
	@ResponseBody
	public String delMassMsg(String id,String appId) {
		if(id != null && appId != null){
		//	Map<String,Object> account = wxAccountService.getAccountByAppId(appId);
			Map<String,Object> map  = new HashMap<String, Object>();
			map.put("_id", new ObjectId(id));
			Map<String,Object> massLog =  baseMongoTemplate.findOne(Constants.WXMASSLOG_T, map);
			if(massLog!=null){
				String msg_id = massLog.get("MsgID")+"";
				//如果是已经发送失败的，只需要删除本库就行了，否则要先删除微信的再删除本地的
				//删除 库 表
				Query query = new Query();
				Criteria criteria=Criteria.where("msg_id").is(msg_id);
				query.addCriteria(criteria);
				Query query1 = new Query();
				Criteria criteria1=Criteria.where("MsgID").is(msg_id);
				query1.addCriteria(criteria1);
				if(baseMongoTemplate.removeByqyery(query, Constants.WXMASSMSG_T)&&baseMongoTemplate.removeByqyery(query1, Constants.WXMASSLOG_T)){
					return AjaxUtil.renderSuccessMsg("删除成功");
				}
			}
		}
		return AjaxUtil.renderFailMsg("删除失败");
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/sendMassMsg", method = RequestMethod.POST)
	@ResponseBody
	public String sendMassMsg(HttpServletRequest request,
			HttpServletResponse response,@RequestBody MassMessage msg) {
		
		JSONObject jsonResult = null;
		if(msg.getAppId()!=null && msg.getMassType()!=null){
				String accessToken = tokenService.getAccessTokenByRedis(msg.getAppId());
				String  jsonStr = null;
				String  errorMsg = null;
				Map<String,Object> filter = new HashMap<>();
				if ("-100".equals(msg.getGroupid())) {//全部用户
					filter.put("is_to_all",true);
					filter.put("tag_id",0);
				} else {//分组
					filter.put("is_to_all",false);
					if(!StringUtils.isEmpty(msg.getGroupid())){
						filter.put("tag_id",Integer.parseInt(msg.getGroupid()));
					}
				}
				Map<String,Object> sendJson = new HashMap<>();
				if(!StringUtils.isEmpty( msg.getGroupid() )){
					sendJson.put("filter",filter);
				}
				if( "txt".equals(msg.getMassType())&& msg.getContent()!=null){//群发文字
					Map<String,Object> text = new HashMap<>();
					text.put("content",msg.getContent());
					sendJson.put("text",text);
					sendJson.put("msgtype","text");
					jsonStr = JSONObject.fromObject(sendJson).toString();
				}else if("pic".equals(msg.getMassType())&& msg.getFileId()!=null){//群发图片
					JSONObject js =  sendMassMsgService.UploadTempImage(msg.getAppId(),msg.getFileId(),accessToken,"image");
					if (js != null && js.containsKey("media_id")) {
						String media_id = js.getString("media_id");
						Map<String,Object> image = new HashMap<>();
						image.put("media_id",media_id);
						sendJson.put("msgtype","image");
						sendJson.put("image",image);
						jsonStr = JSONObject.fromObject(sendJson).toString();
					}else{
						errorMsg = js.toString();
					}
				}else if("news".equals(msg.getMassType())&& msg.getMediaId()!=null){//群发图文
					   //素材库拿到图文信息
					JSONObject js = sendMassMsgService.UploadNews(msg, accessToken);
					if (js != null && js.containsKey("media_id")) {
						String media_id = js.getString("media_id");
						Map<String,Object> mpnews = new HashMap<>();
						mpnews.put("media_id",media_id);
						sendJson.put("mpnews",mpnews);
						sendJson.put("msgtype","mpnews");
						jsonStr = JSONObject.fromObject(sendJson).toString();
					}else{
						errorMsg = js.toString();
					}
				}
				if(errorMsg != null){
					return AjaxUtil.renderFailMsg("发送失败，失败代码:"+errorMsg);
				}
				if(jsonStr == null){
					return AjaxUtil.renderFailMsg("发送失败");
				}
				//调用群发接口
				if(msg.getOpenId()==null ){
					jsonResult = service.massMsgByGroup(jsonStr, accessToken,msg.getAppId(),null);
				}else{
					//发送预览
					jsonResult = service.massMsgByGroup(jsonStr, accessToken,msg.getAppId(),msg.getOpenId());
				}
				logger.info("群发"+jsonResult);
				int totalCount = 0;
				if ("-100".equals(msg.getGroupid())) {
					Map<String,Object> m =  fancService.getFansCount(msg.getAppId());
					totalCount = Integer.parseInt(m.get("allNum")+"");
				}else{
					totalCount =(int) fansTagService.getFansCountByTag(msg.getAppId(),msg.getGroupid());
				}

				if(jsonResult!=null && jsonResult.containsKey("errcode") && jsonResult.getInt("errcode") == 0){
					if(msg.getOpenId()==null ){
						JSONObject obj = JSONObject.fromObject(msg);
						obj.put("msg_id", jsonResult.get("msg_id")+"");

						//向表masslog追加记录
						Map<String,Object> masslog = new HashMap<String, Object>();
						masslog.put("appId", msg.getAppId());
						masslog.put("ToUserName",msg.getAppId());
						masslog.put("CreateTime",new Date().getTime()/1000+"");
						masslog.put("MsgID",jsonResult.get("msg_id")+"");
						masslog.put("Status","send success");
						masslog.put("TotalCount",totalCount);
						masslog.put("FilterCount",totalCount);
						masslog.put("SentCount",totalCount);
						masslog.put("ErrorCount","0");
						masslog.put("massType",msg.getMassType());
						if(baseMongoTemplate.count(Constants.WXMASSMSG_T, obj) < 1){
							baseMongoTemplate.save(Constants.WXMASSMSG_T, obj);
						}
						if(baseMongoTemplate.count(Constants.WXMASSLOG_T, masslog) < 1){
							baseMongoTemplate.save(Constants.WXMASSLOG_T, masslog);
						}
					}
					//发送成功
					return AjaxUtil.renderSuccessMsg("群发成功");
				}else{//发送失败，保存失败信息
					if(msg.getOpenId()==null ){
						JSONObject obj = JSONObject.fromObject(msg);
						String msg_id = UUID.randomUUID().toString().replaceAll("-", "");
						obj.put("msg_id", msg_id);
						//向表masslog追加记录
						Map<String,Object> masslog = new HashMap<String, Object>();
						masslog.put("appId", msg.getAppId());
						masslog.put("ToUserName","failUser");
						masslog.put("CreateTime",new Date().getTime()/1000+"");
						masslog.put("MsgID",msg_id);
						masslog.put("Status","send fail");
						masslog.put("TotalCount",totalCount);
						masslog.put("FilterCount","0");
						masslog.put("SentCount","0");
						masslog.put("ErrorCount","0");
						masslog.put("massType",msg.getMassType());
						if(baseMongoTemplate.count(Constants.WXMASSMSG_T, obj) < 1){
							baseMongoTemplate.save(Constants.WXMASSMSG_T, obj);
						}
						if(baseMongoTemplate.count(Constants.WXMASSLOG_T, masslog) < 1){
							baseMongoTemplate.save(Constants.WXMASSLOG_T, masslog);
						}
					}
				}
		}
		if(jsonResult!=null &&jsonResult .containsKey("errcode") && jsonResult.getInt("errcode")==45028){
			return AjaxUtil.renderFailMsg("群发失败,调用次数超过限制!");
		}
		return AjaxUtil.renderFailMsg("群发失败!"+(jsonResult==null?"":jsonResult));
	}
	
	
	@RequestMapping(value = "/massMsgList", method = RequestMethod.GET)
	@ResponseBody
	public String massMsgList( String appId,String pageNum,String pageSize) {
		 int pageNum_ = pageNum==null?1:Integer.parseInt(pageNum);
	     int pageSize_  = pageSize==null?20:Integer.parseInt(pageSize);
	     long totalPages=0;//总页数
		if(appId!=null){
			 Query querys = new Query();
		     querys.addCriteria(Criteria.where("appId").is(appId));
		     long totalRows= mongoTemplate.count( querys,Constants.WXMASSLOG_T);//总记录数
		     if ((totalRows % pageSize_) == 0) {
		            totalPages = totalRows / pageSize_;
		        } else {
		            totalPages = totalRows / pageSize_ + 1;
		     }
		     try {
					Query query = new Query();
					Criteria criteria=Criteria.where("appId").is(appId);
			    	query.addCriteria(criteria);
					query.skip((pageNum_ - 1) * pageSize_).limit(pageSize_);
					query.with(new Sort(Direction.DESC , "CreateTime"));
					List<HashMap> list =     baseMongoTemplate.findByQuery(query, Constants.WXMASSLOG_T);
					if(list != null){
						for(Map<String,Object> w: list){
							//根据msgID去找数据
							String msgID = w.get("MsgID")+"";
							if("ready".equals(w.get("Status")+"")){
								//如果是定时发送的job任务，需要检测定时任务在不在，不在就显示失败
								JobKey jobKey = new JobKey( "job"+msgID ,"massMsg"+msgID);
								Scheduler sched =  QuartzScheduleMgr.getInstanceScheduler();
								boolean flag =  sched.checkExists(jobKey);
								logger.info(jobKey + " job是否存在 " + flag);
								if(!flag){
									Query updateQuery = new Query(Criteria.where("MsgID").is(msgID));
									Update update = new Update();
									update.set("Status","send fail");
									baseMongoTemplate.updateFirst(querys,update,Constants.WXMASSLOG_T);
									w.put("Status","send fail");
								}
							}

							Map<String,Object> m = new HashMap<String, Object>();
							m.put("msg_id", msgID);
							m.put("appId", appId);
							String time =  w.get("CreateTime")+"";
							w.put("CreateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(Long.parseLong(time)*1000)));
							Map<String,Object> massMsg =  baseMongoTemplate.findOne(Constants.WXMASSMSG_T, m);
							if("txt".equals( massMsg.get("massType")+"" )){
								w.put("content", massMsg.get("content"+""));
							}else if("pic".equals( massMsg.get("massType")+"" )){
								w.put("content", massMsg.get("fileId"+""));
							}else if("news".equals( massMsg.get("massType")+"" )){
								//去图文表查询记录
								String id = massMsg.get("mediaId")+"" ;
								Map<String,Object> m1 = new HashMap<String, Object>();
								m1.put("_id", new ObjectId(id));
								Map<String,Object> news = baseMongoTemplate.findOne(Constants.WXNEWS_T , m1);
								if(news==null){
									//list.remove(w);
									continue;
								}
								
								w.put("content", news.get("articles"));
							}
							
							w.put("id",w.get("_id")+"");
						}
					}else{
						list = new ArrayList<HashMap>();
					}
					JSONObject result = new JSONObject();
					result.put("dataList", list);
					result.put("totalPages", totalPages);
					result.put("totalRows", totalRows);
					return AjaxUtil.renderSuccessMsg(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return AjaxUtil.renderFailMsg("获取列表失败");
	}
	

	
	
}

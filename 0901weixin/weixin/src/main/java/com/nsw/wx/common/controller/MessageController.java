package com.nsw.wx.common.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nsw.wx.api.service.KfService;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.imp.KfServiceImp;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.common.docmodel.SysLog;
import com.nsw.wx.common.docmodel.WxMessage;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.LogService;
import com.nsw.wx.common.service.MessageService;
import com.nsw.wx.common.service.MongoFileOperationService;
import com.nsw.wx.common.service.AccessTokenService;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.DateUtils;
import com.nsw.wx.common.util.ImageFactory;

@Controller
@RequestMapping("/message")
public class MessageController {
	
	private Logger logger = Logger.getLogger(MessageController.class);
	
	@Autowired
	private LogService logService;
	
	
	@Value("${7Niu.domain}")
	private String fastDfsServerUrl;
	
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	@Autowired
	private WxAccountService wxAccountService ;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoFileOperationService mongoFileOperationService;
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Value("${wxurl}")
	private String wxurl;
	
	MaterialService materService = new MaterialServiceImp();
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private AccessTokenService tokenService;
	
	
	
	
	/**
	 * 
	* @Description: 查找所有的聊天记录，查找与当前用户的聊天记录（需要传openId）
	* @param @param appId
	* @param @param openId
	* @param @param filter
	* @param @param pageNum
	* @param @param pageSize
	* @param @param dayNum  天数 0 全部  1今天  2昨天 3前天 4最近5天
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "msgList", method = RequestMethod.GET)
	@ResponseBody
	public String msgList(String appId,String openId, String filter,String dayNum, String pageNum,String pageSize) {
	
		try {
			if(appId != null){
				int pageSize_ = pageSize==null?20:Integer.parseInt(pageSize);
				int pageNum_  =  pageNum==null?1:Integer.parseInt(pageNum);
				filter = filter==null?"":filter;
				Query query = new Query();
				Criteria cr = new Criteria();
				if(filter!=null && filter.trim().length()>0){
					query.addCriteria(cr.orOperator(
						    Criteria.where("content").regex(filter)));
				}
				
				dayNum = dayNum == null?"0":dayNum;
				
				if(openId==null){
					query.addCriteria(Criteria.where("appId").is(appId).and("GetOrPut").is("get").and("msgType").ne("event"));//查询全部的不需要查看公众号发出的消息
				}else{
					query.addCriteria(Criteria.where("appId").is(appId).and("fromUserName").is(openId).and("msgType").ne("event"));//查询与公众号的对话 需要查看公众号发出的消息
				}
				//取出今天的日期
				String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				long toDayTime = new SimpleDateFormat("yyyy-MM-dd").parse(toDay).getTime()/1000;
				
				//gt  大于 　　lt　　小于
				if("1".equals(dayNum)){//今天
					query.addCriteria(Criteria.where("createTime").gt(toDayTime+""));
				}else if("2".equals(dayNum)){//昨天
					long temp = toDayTime - 24 * 60 * 60;
					query.addCriteria(Criteria.where("createTime").lt(toDayTime+"").gt(temp+""));
				}else if("3".equals(dayNum)){//前天
					long temp = toDayTime - 24 * 60 * 60*2;
					long temp1 = toDayTime - 24 * 60 * 60;
					query.addCriteria(Criteria.where("createTime").lt(temp1+"").gt(temp+""));
				}else if("4".equals(dayNum)){//5天内
					long temp = toDayTime - 24 * 60 * 60 * 4;
					query.addCriteria(Criteria.where("createTime").gt(temp+""));
				}
				
				
				long totalRows =  mongoTemplate.count(query, Constants.WXMESSAGE_T);
				long totalPages = 0;
			    if ((totalRows % pageSize_) == 0) {
			        totalPages = totalRows / pageSize_;
			    } else {
			        totalPages = totalRows / pageSize_ + 1;
			    }
			    query.skip((pageNum_ - 1) * pageSize_).limit(pageSize_);
			    query.with(new Sort(Direction.DESC , "createTime"));
			    Map<String,Object>  account =  wxAccountService.getAccountByAppId(appId);
				List<HashMap> lists =   baseMongoTemplate.findByQuery(query, Constants.WXMESSAGE_T);
				if(lists != null){
					
					String gzhHeadImgUrl = account.get("head_img")+"";
					String gzhNickName = account.get("nick_name")+"";
					if(!gzhHeadImgUrl.startsWith("http")){
						gzhHeadImgUrl = fastDfsServerUrl+gzhHeadImgUrl;
					}
					SimpleDateFormat df1 =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat df2 =  new SimpleDateFormat("HH:mm:ss");
					for(Map<String,Object> w: lists){
						w.put("id",w.get("_id")+"");
						//显示今天，昨天，前天，5天内
						String dayRemark = "";
						
						long beginTime = new SimpleDateFormat("yyyy-MM-dd").parse(toDay).getTime() ;
						
						
						String createTime = df1.format( new Date(Long.parseLong(w.get("createTime")+"") *1000 ));
						long createTimes =Long.parseLong(w.get("createTime")+"") *1000 ;
						
						
						if(createTimes > beginTime){
							dayRemark = "今天 "+df2.format(new Date(Long.parseLong(w.get("createTime")+"") *1000 ));
						}else if(createTimes>toDayTime * 1000 - 24 * 60 * 60 * 1000 &&  createTimes<beginTime){
							dayRemark = "昨天 "+df2.format(new Date(Long.parseLong(w.get("createTime")+"") *1000 ));
						}else if(createTimes>toDayTime * 1000 - 2 * 24 * 60 * 60 * 1000 &&  createTimes<toDayTime * 1000 - 24 * 60 * 60 * 1000){
							dayRemark = "前天 "+df2.format(new Date(Long.parseLong(w.get("createTime")+"") *1000 ));
//						}else if(createTimes>toDayTime * 1000 - 24 * 60 * 60 * 1000 * 4){
//							dayRemark = "5天内 "+createTime;
						}else{
							dayRemark = createTime;
						}
						//对时间进行转换
						w.put("createTime", dayRemark);
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("openid", w.get("fromUserName")+"");
						Map<String,Object> fansInfo =   baseMongoTemplate.findOne(Constants.WXFANS_T, map);
						if(fansInfo!=null){
							w.put("fansHeadImgUrl", fansInfo.get("headimgurl")+"");
							w.put("nickName", fansInfo.get("nickname"));
							w.put("gzhHeadImgUrl", gzhHeadImgUrl);
							w.put("gzhNickName", gzhNickName);
						}
					}
				}else{
					lists = new ArrayList<HashMap>();
				}
				JSONObject result = new JSONObject();
				result.put("dataList", lists);
				result.put("totalPages", totalPages);
				result.put("totalRows", totalRows);
				return AjaxUtil.renderSuccessMsg(result);
			}
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return AjaxUtil.renderFailMsg("获取信息出错！");
	}
	
	
	@RequestMapping(value = "sendMsg", method = RequestMethod.POST)
	@ResponseBody
	public String sendMsg(@RequestBody WxMessage msg,String msgId) {
		
		try {
			if(msg.getAppId()!=null && msg.getOpenId() != null){
				KfService service = new KfServiceImp();
				Map<String,Object> msgEntity = new HashMap<String, Object>();
				msgEntity.put("fromUserName", msg.getOpenId());
				msgEntity.put("appId", msg.getAppId());
				msgEntity.put("GetOrPut", "put");
				
			//	Map<String,Object>  account =  wxAccountService.getAccountByAppId(msg.getAppId());
				
				String accessToken =  tokenService.getAccessTokenByRedis(msg.getAppId());
				
				JSONObject returnJson = null;
				if("txt".equals(msg.getMsgType())){//发送文本消息
					String content = msg.getContent();
					msgEntity.put("msgType", "text");
					msgEntity.put("content", msg.getContent());
					if(content==null || "".equals(content.trim())){
						return AjaxUtil.renderFailMsg("请输入内容后再发送!");
					}
					JSONObject j = new JSONObject();
					j.put("touser", msg.getOpenId());
					j.put("msgtype", "text");
					JSONObject c = new JSONObject();
					c.put("content",msg.getContent());
					j.put("text",c);
					returnJson=  service.sendMsg(accessToken, j,msg.getAppId());
					logger.info("客服消息"+returnJson);
				}else if("pic".equals(msg.getMsgType())){//图片消息
					msgEntity.put("msgType", "image");
					String fileId = msg.getFileId();
					JSONObject js =  uploadTempImage(msg.getAppId(), fileId);
					//上传图片得到的media_id
					if (js != null &&js.containsKey("media_id")) {
						String media_id  = js.getString("media_id");
						JSONObject j = new JSONObject();
						j.put("touser", msg.getOpenId());
						j.put("msgtype", "image");
						JSONObject c = new JSONObject();
						c.put("media_id",media_id);
						j.put("image",c);
						
						msgEntity.put("mediaId", media_id);
						msgEntity.put("picUrl", fileId);
						returnJson=  service.sendMsg(accessToken, j,msg.getAppId());
						logger.info("客服消息"+returnJson);
					}
				}else if("news".equals(msg.getMsgType())){//图文消息
					msgEntity.put("msgType", "news");
					String newsId = msg.getMediaId();
					Map<String,Object> mapQuery = new HashMap<String, Object>();
					mapQuery.put("_id", new ObjectId(newsId));
					Map<String,Object> newsEntity =  baseMongoTemplate.findOne(Constants.WXNEWS_T, mapQuery);
					if(newsEntity!=null){
						JSONArray jsonArr = JSONArray.fromObject( newsEntity.get("articles"));
						JSONArray  articlesArr = new JSONArray();
						for(int i=0;i<jsonArr.size();i++){
							JSONObject obj = jsonArr.getJSONObject(i);
							JSONObject o = new JSONObject();
							o.put("title", obj.get("title")==null?"":obj.get("title")+"");
							o.put("description", obj.get("digest")==null?"":obj.get("digest")+"");
							if(newsEntity.get("media_id") != null){//微信 图文
								//o.put("picurl", obj.getString("thumb_url"));
								o.put("picurl", obj.get("fileId")==null?"":fastDfsServerUrl+obj.get("fileId")+"");
								o.put("url",obj.getString("url"));
							}else{//本地图文
								o.put("picurl", obj.get("fileId")==null?"":fastDfsServerUrl+obj.get("fileId")+"");
								String updateTimes =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newsEntity.get("update_time")+"").getTime()+"" ;
								o.put("url", (obj.get("otherUrl")==null||obj.getString("otherUrl").length()==0)  ?wxurl+"/newsPage/detail?appId="+msg.getAppId()+"&newsId="+(String)newsEntity.get("_id")+"&articleNum="+i+"&sendTime ="+DateUtils.getCurrentMillis()+"&update_time="+updateTimes:obj.getString("otherUrl"));
							}
							articlesArr.add(o);
						}
						JSONObject j = new JSONObject();
						j.put("touser", msg.getOpenId());
						j.put("msgtype", "news");
						JSONObject a =  new JSONObject();
						a.put("articles", articlesArr);
						j.put("news",a);
						msgEntity.put("dataContent", articlesArr);//图文的数据保存
						returnJson=  service.sendMsg(accessToken, j,msg.getAppId());
						logger.info("客服消息"+returnJson);
					}
					
					
				}
				if(returnJson!=null && returnJson.getInt("errcode")==0){
					msgEntity.put("createTime", new Date().getTime()/1000 +"");
					if(baseMongoTemplate.count(Constants.WXMESSAGE_T, msgEntity) < 1){
						if(baseMongoTemplate.save(Constants.WXMESSAGE_T, msgEntity)!=null){
							if(msgId!=null){
								//更新回复状态
								Query query = new Query();
								query.addCriteria(Criteria.where("appId").is(msg.getAppId()).and("msgId").is(msgId));
								Update updates = Update.update("isReply", true);
								baseMongoTemplate.updateOne(query, updates, Constants.WXMESSAGE_T);
							}
							return AjaxUtil.renderSuccessMsg("发送成功!");
						}
					}
				}else if(returnJson!=null && returnJson.getInt("errcode")==45015){
					return AjaxUtil.renderFailMsg("由于该用户48小时未与你互动，你不能主动发送消息给他，直到用户下次主动发送消息给你才可以对其进行回复!");
				}else if(returnJson!=null && returnJson.getInt("errcode")==43004){
					return AjaxUtil.renderFailMsg("用户未关注或已加入黑名单,无法向其发送消息!");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("发送失败!");
	}
	
	
	
	/**
	 * 
	* @Description: 上传图片到临时素材
	* @param @param msg
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject uploadTempImage(String appId,String fileId){
		String mediaId =  messageService.getImageMedia(fileId, appId);
		if(mediaId != null && ! "".equals(mediaId)){
			JSONObject j = new JSONObject();
			j.put("media_id", mediaId);
			return j;
		}
		return null;
	} 
	
	
}

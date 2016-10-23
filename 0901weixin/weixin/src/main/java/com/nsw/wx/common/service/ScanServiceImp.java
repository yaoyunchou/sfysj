package com.nsw.wx.common.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.nsw.wx.api.service.KfService;
import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.UserTagService;
import com.nsw.wx.api.service.imp.KfServiceImp;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.api.service.imp.UserTagServiceImp;
import com.nsw.wx.api.util.AesException;
import com.nsw.wx.api.util.WXBizMsgCrypt;
import com.nsw.wx.api.util.WxUtil;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.docmodel.QrCode;
import com.nsw.wx.common.docmodel.ScanQrCodeLog;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.DateUtils;
/**
 * 
 * @author liuzp
 *
 */
@Service("scanService")
public class ScanServiceImp implements ScanService{
	
	private Logger logger = Logger.getLogger(ScanServiceImp.class);
	
	@Autowired
	private WxQrCodeService wxQrCodeService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private OpenCommonService openCommonService;
	
	@Autowired
	private AccessTokenService tokenService;
	
	@Value("${7Niu.domain}")
	private String fastDfsServerUrl;
	
	@Value("${wxurl}")
	private String wxurl;
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired
	private ScanQrCodeLogService scanQrCodeLogService;
	
	@Autowired
	private FancService fancService ;
	

	@Override
	public void scanEvent(HttpServletResponse response , Map<String, String> map, String appId, int type) {
		// TODO Auto-generated method stub
		String eventType = map.get("Event");
		String eventKey = map.get("EventKey");
		String fromUserName = map.get("FromUserName");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String accessToken =  tokenService.getAccessTokenByRedis(appId);
		//定义二维码的scene_id 
		String scene_id = "";
		if (WxUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)) {
			fancService.addFans(appId, fromUserName);
			if(eventKey.indexOf("qrscene") != -1){
				//通过扫描二维码关注的
				scene_id = eventKey.split("_")[1];
			}
		}else if("SCAN".equals(eventType)){
			// 用户已关注时的事件推送 
			scene_id = eventKey;
		}
		if(!"".equals(scene_id)){
			Query query =  new Query();
			Criteria criteria = Criteria.where("appId").is(appId).and("scene_id").is(scene_id);
			query.addCriteria(criteria);
			QrCode qrCode = wxQrCodeService.getQrCodeByQuery(query);
			if(qrCode != null){
				//如果是关注事件，则添加数据库记录，如果有扫描后添加到哪个组的事件，并处理
				if(qrCode.getGroupId() != null && !("-100".equals(qrCode.getGroupId() )) && WxUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)){
					//添加用户到标签
					UserTagService userTagService = new UserTagServiceImp();
					JSONObject jsonRst =  userTagService.batchAddTagToFans(accessToken, new String[]{fromUserName}, Integer.parseInt(qrCode.getGroupId()) , appId);
					if(jsonRst.containsKey("errcode") && jsonRst.getInt("errcode") == 0){
						//更新数据库用户标签
						Query query1 = new Query();
						Criteria criteria1 =Criteria.where("appId").is(appId).and("openid").is(fromUserName);
				    	query1.addCriteria(criteria1);
						Update u = new Update();
						u.set("tagid_list", new int[]{Integer.parseInt(qrCode.getGroupId())});
						baseMongoTemplate.updateOne(query1, u,  Constants.WXFANS_T);
					}
				}
				Update update = new Update();
				update.set("scanDate", df.format(new Date()));
				update.set("scanNum", qrCode.getScanNum() + 1);
				//添加当天扫描日志或更新扫描日志
				String nowDate = df.format(new Date());
				ScanQrCodeLog scanLog =  scanQrCodeLogService.getScanLog(appId, nowDate, qrCode.get_id().toString());
				if(scanLog != null){//更新
					Update updates = new Update();
					if(WxUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)){
						updates.set("followNum", scanLog.getFollowNum()+1);
					}
					updates.set("scanNum", scanLog.getScanNum()+1);
					scanQrCodeLogService.updateScanLog(scanLog.get_id().toString(), updates);
				}else{//加入扫描日志
					scanLog = new ScanQrCodeLog();
					scanLog.setId(qrCode.get_id().toString());
					scanLog.setAppId(appId);
					scanLog.setScanDate(nowDate);
					scanLog.setScene_id(scene_id);
					scanLog.setFollowNum("SCAN".equals(eventType)?0:1);
					scanLog.setScanNum(1);
					scanQrCodeLogService.addScanLog(scanLog);//添加扫描日志
				}
				if("SCAN".equals(eventType)){
					update.set("scanFollowNum", qrCode.getScanFollowNum() + 1);
				}else{
					update.set("addFollowNum", qrCode.getAddFollowNum() + 1);
				}
				wxQrCodeService.updateQrCode(qrCode.get_id().toString(), update);
				if(qrCode.isReply()){//触发自动回复
						KfService service = new KfServiceImp();
						if("txt".equals(qrCode.getReplyType()) && qrCode.getReplyContent() != null ){
							JSONObject j = new JSONObject();
							j.put("touser", fromUserName);
							j.put("msgtype", "text");
							JSONObject c = new JSONObject();
							c.put("content",qrCode.getReplyContent());
							j.put("text",c);
							JSONObject result =  service.sendMsg(accessToken, j,appId);
							logger.info("二维码触发客服消息"+j.toString() +"/n 返回结果"+result.toString());
						}else if ("pic".equals(qrCode.getReplyType()) && qrCode.getReplyContent() != null ) {
							String media_id  = qrCode.getReplyContent();
							String mediaId =  messageService.getImageMedia(media_id, appId);
							if(mediaId != null && ! "".equals(mediaId)){
								JSONObject j = new JSONObject();
								j.put("touser",fromUserName);
								j.put("msgtype", "image");
								JSONObject c = new JSONObject();
								c.put("media_id",mediaId);
								j.put("image",c);
								JSONObject result =  service.sendMsg(accessToken, j,appId);
								logger.info("二维码触发客服消息"+j.toString() +"/n 返回结果"+result.toString());
							}
						} else if ("news".equals(qrCode.getReplyType()) && qrCode.getReplyContent() != null ) {
							try {    
								String newsId = qrCode.getReplyContent();
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
											o.put("picurl", obj.getString("thumb_url"));
											o.put("url",obj.getString("url"));
										}else{//本地图文
											o.put("picurl", obj.get("fileId")==null?"":fastDfsServerUrl+obj.get("fileId")+"");
											String updateTimes =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newsEntity.get("update_time")+"").getTime()+"" ;
											o.put("url", (obj.get("otherUrl")==null||obj.getString("otherUrl").length()==0)  ?wxurl+"/newsPage/detail?appId="+appId+"&newsId="+(String)newsEntity.get("_id")+"&articleNum="+i+"&sendTime ="+DateUtils.getCurrentMillis()+"&update_time="+updateTimes:obj.getString("otherUrl"));
										}
										articlesArr.add(o);
									}
									JSONObject j = new JSONObject();
									j.put("touser", fromUserName);
									j.put("msgtype", "news");
									JSONObject a =  new JSONObject();
									a.put("articles", articlesArr);
									j.put("news",a);
									JSONObject result =  service.sendMsg(accessToken, j,appId);
									logger.info("二维码触发客服消息"+j.toString() +"/n 返回结果"+result.toString());
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					
				}
				
			}
		}
		
	}

}

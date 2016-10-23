package com.nsw.wx.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.nsw.wx.api.common.MessageUtils;
import com.nsw.wx.api.model.message.res.Article;
import com.nsw.wx.api.model.message.res.Image;
import com.nsw.wx.api.model.message.res.ImageMessageRes;
import com.nsw.wx.api.model.message.res.NewsMessageRes;
import com.nsw.wx.api.model.message.res.TextMessageRes;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.api.util.WXBizMsgCrypt;
import com.nsw.wx.api.util.WxUtil;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.DateUtils;
import com.nsw.wx.common.util.ImageFactory;


@Service("keyWordService")
public class KeyWordServiceImp implements  KeyWordService{
	
	@Autowired
	BaseMongoTemplate basemongoTemplate;
	
	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private OpenCommonService openCommonService;
	
	@Autowired
	private MongoFileOperationService mongoFileOperationService;
	
	@Autowired
	private ScanService scanService;
	
	
	@Autowired
	private MessageService messageService;
	
	
	MaterialService materService = new MaterialServiceImp();
	
	@Override
	public void handTxtEvent(HttpServletResponse response,
			Map<String, String> wxMsg, String appId, int type) {
		try {
			// TODO Auto-generated method stub
			// 文本消息内容
			String content = wxMsg.get("Content").trim();

			// 找出用户所设的关键字回复 列表，匹配
			Query query = new Query();
			Criteria criteria = Criteria.where("appId").is(appId).and("type").is(1).and("enable").is(false);//.and("keyWordList").regex(content)
			query.addCriteria(criteria);
			List<Map<String, Object>> lists = basemongoTemplate.queryMulti(query,
					Constants.WXKEYWORD_T);
			Map<String,Object> replyMap = null;
			
			if (lists != null && lists.size() > 0) {
					for(Map<String, Object> m :lists){
						JSONObject o = JSONObject.fromObject(m);
						JSONArray  arr = o.getJSONArray("keyWordList");
						int matchType = o.getInt("matchType");
						for(int i=0;i<arr.size();i++){
							String key = arr.getString(i);
							if(matchType==0 && content.indexOf(key) != -1){//模糊匹配
								replyMap = m;
								break;
							}else if(matchType==1 && content.equalsIgnoreCase(key)){
								replyMap = m;
								break;
							}
						}
						if(replyMap!=null){
							break;
						}
					}
			} 
			if(replyMap == null){
				// 查找是否设置无匹配回复
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("appId", appId);
				map.put("type", 2);
				Map en = basemongoTemplate.findOne(Constants.WXKEYWORD_T, map);
				if (en != null) {
					replyMap = en;
				}
			}
			if(replyMap != null){
				handleMapMsg(response, wxMsg, replyMap, appId, type);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  try {
				PrintWriter out = response.getWriter();  
				out.print("success");  
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		 
		
	}

	@Override
	public void attentionEvent(HttpServletResponse response,
			Map<String, String> wxMsg, String appId, int type) {
			Query query = new Query();
			Criteria criteria = Criteria.where("appId").is(appId).and("type").is(1);
			query.addCriteria(criteria);
			// 查找是否设置关注时回复
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appId", appId);
			map.put("type", 0);
			Map<String,Object> en = basemongoTemplate.findOne(Constants.WXKEYWORD_T, map);
			if(en!= null){//设置了关注时回复
				handleMapMsg(response, wxMsg, en, appId, type);
			}
			scanService.scanEvent(response ,wxMsg , appId, type);
	}
	
	void handleMapMsg(HttpServletResponse response,
			Map<String, String> wxMsg,Map<String,Object> replyMap, String appId, int type){
		
		try {
			//更新点击数
			Query query = new Query();
			Criteria criteria = Criteria.where("appId").is(appId).and("_id").is(new ObjectId(replyMap.get("_id")+""));
			query.addCriteria(criteria);
			Update update = new Update();
			update.set("replyNum",  Integer.parseInt(replyMap.get("replyNum")+"")+1);
			basemongoTemplate.updateOne(query, update, Constants.WXKEYWORD_T);
			String respMessage = null;
			// 发送方帐号（open_id）
			String fromUserName = wxMsg.get("FromUserName");
			// 公众帐号
			String toUserName = wxMsg.get("ToUserName");
			
			String replyType = replyMap.get("replyType") + "";
			if ("txt".equals(replyType)) {
				 respMessage = messageService.resText(fromUserName, toUserName, replyMap.get("content")+"");
			} else if ("pic".equals(replyType)) {
				String fileId = replyMap.get("fileId") + "";
				String mediaId =  messageService.getImageMedia(fileId, appId);
				if(mediaId !=null && !"".equals(mediaId)){
					respMessage = messageService.resImage(mediaId, toUserName, fromUserName);
				}
			} else if ("news".equals(replyType)) {
				String id = replyMap.get("mediaId") + "";
				respMessage = messageService.resNews(appId, id, fromUserName, toUserName);
			}
			 respMessage = respMessage==null?"success":respMessage;
			   if(type==3){//第三方平台的需要加密
				   Account account = 	openCommonService.getAccountBasic();
				   WXBizMsgCrypt pc = new WXBizMsgCrypt(account.getToken(), account.getEncodingAesKey(), account.getAppId());
				   respMessage = pc.encryptMsg(respMessage, new Date().getTime()+"",UUID.randomUUID().toString());
			   }
			   // 响应消息  
			    PrintWriter out = response.getWriter();  
			    out.print(respMessage);  
			    out.close();
		} catch (Exception e) {
			    try {
					PrintWriter out = response.getWriter();  
					out.print("success");  
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		} 
		
		
	}
	
	

}

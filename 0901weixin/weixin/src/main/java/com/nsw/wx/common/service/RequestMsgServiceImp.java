package com.nsw.wx.common.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;


@Service("requestMsgService")
public class RequestMsgServiceImp implements RequestMsgService{
	
	@Autowired
	BaseMongoTemplate basemongoTemplate;

	@Override
	public void saveRequestMsg(String appId,
			Map<String, String> contentMap) {
		// TODO Auto-generated method stub
		//开发者微信号
		String toUserName =  contentMap.get("ToUserName")+"";
		String fromUserName =  contentMap.get("FromUserName")+"";
		String createTime =  contentMap.get("CreateTime")+"";
		String msgType =  contentMap.get("MsgType")+"";
		String msgId =  contentMap.get("MsgId")+"";
		
		Map<String,Object> entity = new HashMap<String, Object>();
		entity.put("toUserName",toUserName );
		entity.put("fromUserName", fromUserName);
		entity.put("createTime", createTime);
		entity.put("msgType",msgType );
		entity.put("appId", appId);
		entity.put("GetOrPut", "get");
		entity.put("isReply", false);//回复状态
		
		//接收文本消息
		if("text".equals(msgType)){
			entity.put("msgId", msgId);
			//内容
			String content  = contentMap.get("Content")+"";
			entity.put("content", content);
		//接收图片消息
		}else if("image".equals(msgType)){
			entity.put("msgId", msgId);
			String picUrl   = contentMap.get("PicUrl")+"";
			String mediaId    = contentMap.get("MediaId")+"";
			entity.put("picUrl", picUrl);
			entity.put("mediaId", mediaId);
		//接收链接消息
		}else if("link".equals(msgType)){
			entity.put("msgId", msgId);
			String title    = contentMap.get("Title")+"";
			String description    = contentMap.get("Description")+"";
			String url    = contentMap.get("Url")+"";
			entity.put("title", title);
			entity.put("description", description);
			entity.put("url", url);
		}
		basemongoTemplate.save(Constants.WXMESSAGE_T, entity);
	}

	@Override
	public boolean isRepeat(String appId, Map<String, String> contentMap) {
		String toUserName =  contentMap.get("ToUserName")+"";
		String fromUserName =  contentMap.get("FromUserName")+"";
		String createTime =  contentMap.get("CreateTime")+"";
		String msgType =  contentMap.get("MsgType")+"";
		String msgId =  contentMap.get("MsgId")+"";
		Map<String,Object> query = new HashMap<String, Object>();
		query.put("toUserName",toUserName );
		query.put("fromUserName", fromUserName);
		query.put("createTime", createTime);
		query.put("msgType",msgType );
		if(contentMap.get("MsgId")!=null){
			query.put("msgId", msgId);
		}
		query.put("appId", appId);
		query.put("GetOrPut", "get");
		if(basemongoTemplate.count(Constants.WXMESSAGE_T, query) < 1){
			return false;
		}
		return true;
	}

}

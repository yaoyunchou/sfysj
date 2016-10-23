package com.nsw.wx.common.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.api.util.AesException;
import com.nsw.wx.api.util.WXBizMsgCrypt;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.MapUtil;


@Service("menuService")
public class MenuServiceImp implements MenuService{
	
	
	@Autowired
	BaseMongoTemplate basemongoTemplate;
	
	@Autowired
	private OpenCommonService openCommonService;
	
	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	private MongoFileOperationService mongoFileOperationService;
	
	MaterialService materService = new MaterialServiceImp();
	
	
	
	@Autowired
	private MessageService messageService;
	
	
	
	
	
	private Logger logger = Logger.getLogger(MenuServiceImp.class);

	@Override
	public Map<String, Object> getMenu(String appId) {
		// TODO Auto-generated method stub
    	try {
			Map<String,Object> query = new HashMap<String, Object>();
			query.put("appId", appId);
			Map<String,Object> entity =  basemongoTemplate.findOne(Constants.WXMENU_T, query);
			return entity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取微信菜单出错");
		}
		
		return null;
	}

	@Override
	public boolean delMainMenu(String appId) {
		// TODO Auto-generated method stub
		try {
			Map<String,Object> query = new HashMap<String, Object>();
			query.put("appId", appId);
			return basemongoTemplate.deleteData(Constants.WXMENU_T, query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除微信菜单出错");
		}
		
		return false;
	}

	@Override
	public boolean updateMenu(Map<String, Object> menu) {
		// TODO Auto-generated method stub
		
		try {
			return basemongoTemplate.update(Constants.WXMENU_T, MapUtil.putMap("appId", menu.get("appId")), menu);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("更新微信菜单出错");
		}
		
		return false;
	}

	@Override
	public Map<String, Object> addMenu(Map<String, Object> menu) {
		// TODO Auto-generated method stub
		try {
			return basemongoTemplate.save(Constants.WXMENU_T, menu);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("添加微信菜单出错");
		}
		return null;
	}
	
	/**
	 * 将菜单信息同步到微信服务器
	 */
	@Override
	public boolean syncWxServer(JSONObject entity,String accessToken,String appId) {
		// TODO Auto-generated method stub
		com.nsw.wx.api.service.MenuService service = new com.nsw.wx.api.service.imp.MenuServiceImp();
		JSONObject obj =  service.createMenu(entity, accessToken,appId);
		if(obj.containsKey("errcode")&&obj.getInt("errcode")==0){
			return true;
		}
		return false;
	}

	/**
	 * 处理微信用户点击菜单触发事件
	 */
	@Override
	public void handMenuEvent(HttpServletResponse response,
			Map<String, String> wxMsg, String appId,int type) {
		// TODO Auto-generated method stub
			String respMessage = null;
		
		   try {
			   // 发送方帐号（open_id）  
	          String fromUserName = wxMsg.get("FromUserName");  
	          // 公众帐号  
	          String toUserName = wxMsg.get("ToUserName");  
			  String EventKey = wxMsg.get("EventKey");
			   Map<String,Object> map = new HashMap<String, Object>();
			   if(EventKey.indexOf(".")!= -1){//说明推送的是图片
				  String mediaId =  messageService.getImageMedia(EventKey, appId);
				   if(mediaId!=null && ! "".equals(mediaId)){
					   respMessage = messageService.resImage(mediaId, toUserName, fromUserName);
				   }
			   }else{//推送的是图文
				   //去关键字查找，如果没找到，则触发图文
				   Map<String,Object> queryKeyWold = new HashMap<String, Object>();
				   queryKeyWold.put("_id", new ObjectId(EventKey));
				   queryKeyWold.put("enable", false);
				   Map<String,Object> keyWold =  basemongoTemplate.findOne(Constants.WXKEYWORD_T, queryKeyWold);
				   if(keyWold != null){
					   if("txt".equals( keyWold.get("replyType")+"") && keyWold.get("content") != null){
						   respMessage = messageService.resText(fromUserName, toUserName,  keyWold.get("content")+"");
					   }else if("pic".equals( keyWold.get("replyType")+"") && keyWold.get("fileId") != null){
						   String mediaId =  messageService.getImageMedia(keyWold.get("fileId")+"", appId);
						   if(mediaId!=null && ! "".equals(mediaId)){
							   respMessage = messageService.resImage(mediaId, toUserName, fromUserName);
						   }
					   }else if("news".equals( keyWold.get("replyType")+"") && keyWold.get("mediaId") != null){
						   respMessage =  messageService.resNews(appId, keyWold.get("mediaId")+"", fromUserName, toUserName);
					   }
				   }else{
					   respMessage =  messageService.resNews(appId, EventKey, fromUserName, toUserName);
				   }
				   logger.info("菜单事件:公众号回复的消息->"+respMessage);
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}

}

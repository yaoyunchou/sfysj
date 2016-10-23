package com.nsw.wx.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.nsw.wx.common.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.nsw.wx.api.util.WxUtil;
import com.nsw.wx.common.repository.BaseMongoTemplate;

@Service("messageService")
public class MessageServiceImp  implements MessageService{
	
	@Autowired
	private  MongoFileOperationService mongoFileOperationService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Value("${7Niu.domain}")
    private String fastDfsServerUrl;  //本地访问路径
	
	@Autowired
	private AccessTokenService tokenService;
	
	
	@Value("${wxurl}")
	private String wxurl;
	
	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	private BaseMongoTemplate basemongoTemplate;
 	
	
	MaterialService materService = new MaterialServiceImp();
	
	
	 
	public String  getImageMedia(String fileId,String appId ){
		
		String mediaId ="";
		QiNiuFileUtils qiniuUtils = new QiNiuFileUtils();


		if (redisTemplate.opsForValue().get(appId + fileId) == null) {
			// 图片消息需要先将图片上传，得到MediaId
			InputStream stream = null;
			try {
				//stream = mongoFileOperationService.getBufferedInputStream(fileId);
				//如果是其他图片域的话需要已httpConnection获取流
				stream = (fileId+"").startsWith("http")? HttpConnectionUtils.getImageFromNetByUrl(fileId):qiniuUtils.getDownload(fileId);
				if(stream!=null){
					long fileSize = (fileId+"").startsWith("http")? HttpConnectionUtils.getImageSizeFromNetByUrl(fileId):qiniuUtils.getDownloadFileSize(fileId);
					if(fileSize==0){
						return null;
					}
					ImageFactory imageFactory = new ImageFactory();
					//不得超过 1048576  
					while(fileSize>1048576){
						stream = imageFactory.imgCompressToScale(stream, 0.5f, "jpg");
						 fileSize = stream.available();
					} 
					String fileName = fileId.substring(fileId.lastIndexOf("/") + 1,fileId.length());
//					Map<String, Object> account_Map = wxAccountService.getAccountByAppId(appId);
					String accessToken = tokenService.getAccessTokenByRedis(appId);
				    JSONObject mediaJson = materService.addTempMatter(accessToken, "image", stream, fileName,appId);
					stream.close();
					if (mediaJson.containsKey("media_id")) {
						mediaId =  mediaJson.getString("media_id");
						redisTemplate.opsForValue().set(appId + fileId,mediaId,60,TimeUnit.MINUTES);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			mediaId =  redisTemplate.opsForValue().get(appId + fileId)+ "";
		}
		
		return mediaId;
	}
	
	

	@Override
	public String resImage(String mediaId,String toUserName, String fromUserName) {
			Image images = new Image();
			images.setMediaId(mediaId);
			ImageMessageRes imageRes = new ImageMessageRes();
			imageRes.setImage(images);
			imageRes.setCreateTime(new Date().getTime());
			imageRes.setFromUserName(toUserName);
			imageRes.setMsgType("image");
			imageRes.setToUserName(fromUserName);
			return  imageRes.getXmlStr();
	}

	@Override
	public String resText(String fromUserName,String toUserName ,String content) {
		MessageUtils utils = new MessageUtils();
		TextMessageRes textMessage = new TextMessageRes();
		textMessage.setContent(content);
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(WxUtil.RESP_MESSAGE_TYPE_TEXT);
		return  utils.getXmlForBean(textMessage);
	}

	@Override
	public String resMusic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String resNews(String appId,String newsId,String fromUserName,String toUserName ) {
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		mapQuery.put("_id", new ObjectId(newsId));
		Map<String, Object> news = basemongoTemplate.findOne(
				Constants.WXNEWS_T, mapQuery);
		if (news != null) {
			try {
				NewsMessageRes newsMsg = new NewsMessageRes();
				JSONObject newsJson = JSONObject.fromObject(news);
				JSONArray arr = newsJson.getJSONArray("articles");
				List<Article> list = new ArrayList<Article>();
				for (int i = 0; i < arr.size(); i++) {
					JSONObject child = arr.getJSONObject(i);
					Article article = new Article();
					article.setDescription(child.get("digest")==null?"": child.getString("digest"));
					if(news.get("media_id") != null){//微信 图文
						article.setUrl(child.getString("url"));
						article.setPicUrl(child.getString("thumb_url"));
					}else{//本地图文
						String updateTimes =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newsJson.getString("update_time")).getTime()+"" ;
						//点击原文跳转链接
						article.setUrl((child.get("otherUrl")==null||child.getString("otherUrl").length()==0) ?wxurl+"/newsPage/detail?appId="+appId+"&newsId="+(String)news.get("_id")+"&articleNum="+i+"&sendTime ="+DateUtils.getCurrentMillis()+"&update_time="+updateTimes:child.getString("otherUrl"));
						article.setPicUrl(child.getString("fileId").startsWith("http")?child.getString("fileId"): fastDfsServerUrl+ child.getString("fileId"));
					}
					article.setTitle(child.get("title")==null ? "" : child.getString("title"));
					list.add(article);
				}
				newsMsg.setArticleCount(list.size());
				newsMsg.setCreateTime(new Date().getTime());
				newsMsg.setFromUserName(toUserName);
				newsMsg.setMsgType("news");
				newsMsg.setToUserName(fromUserName);
				newsMsg.setArticles(list);
				return newsMsg.getXmlStr();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}

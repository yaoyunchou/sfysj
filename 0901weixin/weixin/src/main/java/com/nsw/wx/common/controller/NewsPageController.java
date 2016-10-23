package com.nsw.wx.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.Constants;

@Controller
@RequestMapping("/newsPage")
public class NewsPageController {

	@Autowired
	BaseMongoTemplate basemongoTemplate;
	
	@Value("${7Niu.domain}")
	private String fastDfsServerUrl;
	
	@Autowired
	private WxAccountService wxAccountService;
	
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 
	* @Description: TODO 
	* @param @param appId
	* @param @param newsId
	* @param @param articleNum  第几个图文   0开始计数
	* @param @return   
	* @return String  
	* @throws
	 */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public String detail(String appId,String newsId,String articleNum,String sendTime,String update_time)  {
		
    	
		
		StringBuffer bf = new StringBuffer();
		bf.append("<!DOCTYPE html>");
		bf.append("<html>");
		bf.append(" <head>");
		bf.append(" <meta charset=\"utf-8\" />");
		bf.append("  <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" />");
		bf.append("<meta content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\" name=\"viewport\">");
		bf.append("<meta content=\"application/xhtml+xml;charset=UTF-8\" http-equiv=\"Content-Type\">");
		bf.append(" <meta content=\"telephone=no, address=no\" name=\"format-detection\">");
		bf.append(" <meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");
		bf.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black-translucent\" />");
		bf.append(" <link rel=\"stylesheet\"  href=\"../app/newsPage/newsDetail.css\">");
		
		bf.append("<style>");
		bf.append(" img{max-width:100%!important;}");
		bf.append(" </style>");
		//正文内容开始
		bf.append(" <body onselectstart=\"return true;\" ondragstart=\"return false;\">");
		
		//判断有无缓存，无缓存自己查询，有缓存读取缓存内容
		Object newsDetail = redisTemplate.opsForValue().get(appId+newsId+articleNum+update_time);//查询这条图文信息是否被缓存
	
		Map<String, Object> newsDetailMap = null;
		if(newsDetail!=null){
			newsDetailMap = (Map<String, Object>) newsDetail;
		}else{
			Map<String,Object> account =  wxAccountService.getAccountByAppId(appId);
			String appIdNickName = account.get("nick_name")==null?"":account.get("nick_name")+"";
			Map<String, Object> mapQuery = new HashMap<String, Object>();
			mapQuery.put("_id", new ObjectId(newsId));
			Map<String, Object> newsMap = basemongoTemplate.findOne(Constants.WXNEWS_T, mapQuery);
			if(newsMap!=null){
				JSONArray articles = JSONArray.fromObject( newsMap.get("articles"));
				JSONObject article = null;
				for(int i=0; i < articles.size(); i++){
					JSONObject j = articles.getJSONObject(i);
					if((i+"").equals(articleNum)){
						article = j;
					}
				}
				if(article==null){
					bf.append("<h3>该内容已被发布者删除</h3>");
				}else{
					newsDetailMap = new HashMap<String, Object>();
					newsDetailMap.put("appIdNickName", appIdNickName);
					newsDetailMap.put("article", article);
					redisTemplate.opsForValue().set(appId+newsId+articleNum+update_time, newsDetailMap, 50, TimeUnit.MINUTES);
				}
			}else{
				bf.append("<h3>该内容已被发布者删除</h3>");
			}
		}
		if(newsDetailMap!=null){
			   JSONObject article =JSONObject.fromObject(newsDetailMap.get("article")) ;
			   String appIdNickName = (String) newsDetailMap.get("appIdNickName");
				//填充图文数据
				String content = article.get("content")==null?"":article.getString("content");//内容
				String title = article.get("title")==null?"":article.getString("title");//标题
				String content_source_url =  (article.get("content_source_url")!=null && article.get("content_source_url").toString().length() > 0) ?article.get("content_source_url")+"":null;//原文链接
				
				
				boolean show_cover_pic = false;	//是否在里面显示图片
				if(article.get("show_cover_pic")==null || "".equals(article.get("show_cover_pic")+"")){
					show_cover_pic = false;
				}else{
					show_cover_pic = (boolean)article.get("show_cover_pic");
				}
				String dateYmd = new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(sendTime==null?new Date().getTime()+"":sendTime)));
				String fileId = article.get("fileId")==null?"":article.getString("fileId");
				bf.append("<h2>"+title+"</h2>");
				bf.append(" <div style=\"width:100%;\">");
				bf.append(" 	<label>"+ dateYmd +"</label>");
				bf.append(" 	<label style='color:#f08300'>"+appIdNickName+"</label>");
				//bf.append(" 	<div style=\"color:#ccc;\">"+account.get("user_name")+"</div>");
				bf.append("</div></div>");
				bf.append(" <article class = \"article\">");
				if(show_cover_pic){
					bf.append("<img src=\""+ (fileId.startsWith("http")?fileId:fastDfsServerUrl+fileId)+"\"/>");
				}
				bf.append(content);
				bf.append("</article>");
				if(content_source_url!=null){
					bf.append("<p><a href='"+content_source_url+"' >阅读原文</a><p>");
				}
				bf.append("<div style=\"padding-bottom:5px!important;\">");
				bf.append(" <a style=\"font-size:12px;margin:5px auto;display:block;color:#fff;text-align:center;line-height:35px;background:#333;margin-bottom:-10px;\" href=\"javascript:window.scrollTo(0,0);\">返回顶部</a>");
				bf.append("</div>");
				
		}
		bf.append("</body></html>");
		 return bf.toString();
		
    }
}

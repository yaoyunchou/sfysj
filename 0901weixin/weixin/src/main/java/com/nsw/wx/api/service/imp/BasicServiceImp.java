package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.nsw.wx.api.common.HttpClientConnectionManager;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.BasicServices;
import com.nsw.wx.common.service.AccessTokenService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月13日 上午10:10:31
* @Description: 获得accessToken 以及处理get或者post请求
 */

@Service("basicService")
public class BasicServiceImp implements BasicServices{
	public static DefaultHttpClient httpclient;
	
	
	private Logger logger = Logger.getLogger(BasicServiceImp.class);
	static {
		ClientConnectionManager cm = new ThreadSafeClientConnManager(); 
		httpclient = new DefaultHttpClient(cm);
		httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient); 
	}
	
	
	public JSONObject getMethodResult(String url,String appId){
	
		
		AccessTokenService tokenService = (AccessTokenService) ContextUtil.getBean("accessTokenService");
		JSONObject jsonObject = null;
		try {
			HttpGet get = HttpClientConnectionManager.getGetMethod(url);
			HttpResponse response = httpclient.execute(get);
			String jsonStr = EntityUtils.toString(response.getEntity(), Constants.UTFCODE);
			 jsonObject = JSONObject.fromObject(jsonStr);
			 //针对系统繁忙作出处理
			 if(jsonObject.containsKey(Constants.ERRCODE)){
				 int cs=0;//如果是系统繁忙，重试3次
				 while(jsonObject.containsKey(Constants.ERRCODE)&&jsonObject.getInt(Constants.ERRCODE) == -1 && cs < 3){
					 cs++;
					 response=httpclient.execute(get);
					 jsonStr = EntityUtils.toString(response.getEntity(), Constants.UTFCODE);
					 jsonObject = JSONObject.fromObject(jsonStr);
				 }
				 //token失效或超时
				 if(jsonObject.getInt(Constants.ERRCODE)==40001 ||jsonObject.getInt(Constants.ERRCODE)== 40014 ||
						 jsonObject.getInt(Constants.ERRCODE)== 42001 
						 //||jsonObject.getInt(Constants.ERRCODE)== 42002
						 ){
					 logger.error("调用AccessToken超时,url="+url+",appId="+appId);
					 if(appId!=null && (appId+"").length()>4){
						 //被动刷新
						 if(tokenService!=null){
							 //accessToken超时后重新获取并重新请求
							String newToken =  tokenService.checkRuslt(appId);

							if(newToken != null){
								if(url.indexOf("access_token=") != -1){
									url = url.substring(0,url.indexOf("access_token=")) +"access_token="+newToken;
								}
								logger.info("token缓存成功，新的accessToken="+newToken+",继续发请求"+url);
								 get = HttpClientConnectionManager.getGetMethod(url.replace(Constants.ACCESSTOKEN, newToken));
								 response = httpclient.execute(get);
								 jsonStr = EntityUtils.toString(response.getEntity(), Constants.UTFCODE);
								 jsonObject = JSONObject.fromObject(jsonStr);
							}
						 }
						 
					 }
				 	}
			 } 
			 
		return jsonObject; 
		} catch (Exception e) {
			e.printStackTrace();
			return jsonObject;
		}
	}
	
	public JSONObject postMethodResult(String url, String params,String appId){
		
		AccessTokenService tokenService = (AccessTokenService) ContextUtil.getBean("accessTokenService");
		String jsonStr="";
		JSONObject object =null;
		try {
			HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
			httpost.setEntity(new StringEntity(params, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpost);
			jsonStr = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
			object = JSONObject.fromObject(jsonStr);
			 //针对系统繁忙作出处理
			 if(object.containsKey(Constants.ERRCODE)){
				 int cs = 0;//如果是系统繁忙，重试3次
				 while(object.containsKey(Constants.ERRCODE)&&object.getInt(Constants.ERRCODE) ==-1 && cs < 3){
					 cs++;
					 response = httpclient.execute(httpost);
					 jsonStr = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
					 object = JSONObject.fromObject(jsonStr);
				 }
				 //token失效或超时
				 if(object.getInt(Constants.ERRCODE)==40001 ||object.getInt(Constants.ERRCODE)== 40014 ||
						 object.getInt(Constants.ERRCODE)== 42001
						 //||object.getInt(Constants.ERRCODE)== 42002
						 ){
					 logger.error("调用AccessToken超时,url="+url+",appId="+appId);
					 if(appId!=null && (appId+"").length()>4){
						 //被动刷新
						 if(tokenService!=null){
							 //accessToken超时后重新获取并重新请求
							String newToken =  tokenService.checkRuslt(appId);
							if(newToken != null){
								if(url.indexOf("access_token=") != -1){
									url = url.substring(0,url.indexOf("access_token=")) +"access_token="+newToken;
								}
								logger.info("token缓存成功，新的accessToken="+newToken+",继续发请求"+url);
								httpost = HttpClientConnectionManager.getPostMethod(url.replace(Constants.ACCESSTOKEN, newToken));
								httpost.setEntity(new StringEntity(params, HTTP.UTF_8));
								response = httpclient.execute(httpost);
								jsonStr = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
								object = JSONObject.fromObject(jsonStr);
							}
						 }
					 }
					 
				 	}
			 }
		return object;
			
		} catch (Exception e) {
			e.printStackTrace();
			return object;
		}
	}

	
	public JSONObject getWxIp(String accessToken){
		String tokenUrl=WeiXinApiUrl.Getcallbackip_Get.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject jsonObject = getMethodResult(tokenUrl,"");
		return jsonObject;
		
	}
	
}

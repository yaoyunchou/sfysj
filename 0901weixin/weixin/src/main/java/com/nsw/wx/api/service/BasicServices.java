package com.nsw.wx.api.service;

import net.sf.json.JSONObject;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月14日 上午9:53:58
* @Description: 微信基础接口
 */
public interface BasicServices {
	
	/**
	 * 
	* @Description: 调用微信的get方法，通过url解析并返回结果 
	* @param @param url
	* @param @return
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getMethodResult(String url,String appId);
	/**
	 * 
	* @Description: 调用post方法，并返回对应的结果 
	* @param @param url
	* @param @param params
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject postMethodResult(String url,String  params,String appId);

	/**
	 * 
	* @Description: 获取所有微信服务器IP
	* @param @param accessToken
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getWxIp(String accessToken);
}

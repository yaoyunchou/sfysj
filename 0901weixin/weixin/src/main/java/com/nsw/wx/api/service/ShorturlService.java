package com.nsw.wx.api.service;

import net.sf.json.JSONObject;


/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月28日 下午5:44:13
* @Description: 长链接转短链接
 */
public interface ShorturlService {

	/**
	 * 
	* @Description: 长链接转短链接
	* @param @param longUrl
	* @param @param accessToken
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject longUrlToShort(String longUrl,String accessToken,String appId);
}

package com.nsw.wx.common.service;

import net.sf.json.JSONObject;

public interface AccessTokenService {
	
	/**
	 * 
	* @Description:检查调用后的结果，如果结果是超时的，则被动刷新
	* @param @param appId
	* @param @param result
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public  String checkRuslt(String appId)  throws Exception;
	
	/**
	 * 根据appId获取redis缓存的accessToken
	 * @param appId
	 * @return
	 */
	public String getAccessTokenByRedis(String appId);

}

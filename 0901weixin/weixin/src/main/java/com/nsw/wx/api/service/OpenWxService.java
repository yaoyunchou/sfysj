package com.nsw.wx.api.service;

import net.sf.json.JSONObject;

public interface OpenWxService  extends BasicServices{
	
	
	/**
	 * 获取第三方平台component_access_token
	 * @param postPara
	 * @return
	 */
	public JSONObject getComponentAccessToken(JSONObject postPara,String appId);
	
	
	/**
	 * 获取预授权码pre_auth_code
	 * 该API用于获取预授权码。预授权码用于公众号授权时的第三方平台方安全验证。
	 * @param component_access_token
	 * @param component_appid
	 * @return
	 */
	public JSONObject getPreAuthCode(String component_access_token,JSONObject component_appid,String appId);

	
	
	/**
	 * 使用授权码换取公众号的接口调用凭据和授权信息
	 * @param component_access_token
	 * @param entity
	 * @return
	 */
	public JSONObject getAuthorizerAccessTokenAndRefreshToken(String component_access_token,JSONObject entity,String appId);


	/**
	 * 获取授权方的公众号帐号基本信息
	 * @param component_access_token
	 * @param entity
	 * @return
	 */
	public JSONObject getAuthorizerInfo(String component_access_token,JSONObject entity,String appId);
}

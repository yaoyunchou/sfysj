package com.nsw.wx.api.service;

import com.nsw.wx.api.model.OAuthAccessToken;
import com.nsw.wx.api.model.UserEntity;


/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月28日 下午5:09:34
* @Description: 微信授权接口
 */
public interface OauthService {
	
	/**
	 * 微信OAuth2.0授权（目前微信只支持在微信客户端发送连接，实现授权）
	 * */
	public String getCodeUrl(String appid, String redirect_uri,String scope,String state,String appId) throws Exception;
	/**
	 * 微信OAuth2.0授权-获取accessToken(这里通过code换取的网页授权access_token,与基础支持中的access_token不同）
	 */
	public OAuthAccessToken getOAuthAccessToken(String appid, String secret,String code,String appId) throws Exception;
	/**
	 * 微信OAuth2.0授权-刷新access_token（如果需要）
	 */
	public OAuthAccessToken refershOAuthAccessToken(String appid, String refresh_token,String appId) throws Exception;
	/**
	 * 微信OAuth2.0授权-检验授权凭证（access_token）是否有效
	 */
	public boolean isAccessTokenValid(String accessToken, String openId,String appId) throws Exception;
	/**
	 * 微信OAuth2.0授权-获取用户信息（网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息）
	 */
	public UserEntity acceptOAuthUserNews(String accessToken, String openId,String appId) throws Exception;
	
}

package com.nsw.wx.api.service.imp;

import java.net.URLEncoder;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.OAuthAccessToken;
import com.nsw.wx.api.model.UserEntity;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.OauthService;
import com.nsw.wx.common.util.Constants;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月28日 下午5:25:36
* @Description: 授权实现类
 */
public class OauthServiceImp  extends BasicServiceImp implements OauthService{

	/**
	 * 微信OAuth2.0授权（目前微信只支持在微信客户端发送连接，实现授权）
	 * */
	public String getCodeUrl(String appid, String redirect_uri, String scope,
			String state,String appId) throws Exception {
		redirect_uri = URLEncoder.encode(redirect_uri, Constants.UTFCODE);
		String getCodeUrl=WeiXinApiUrl.getCodeUrl.replace("APPID", appid).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", scope).replace("STATE", state);
		return getCodeUrl;
	}

	/**
	 * 微信OAuth2.0授权-获取accessToken(这里通过code换取的网页授权access_token,与基础支持中的access_token不同）
	 */
	public OAuthAccessToken getOAuthAccessToken(String appid, String secret,
			String code,String appId) throws Exception {
		String getOAuthAccessToken=WeiXinApiUrl.getOAuthAccessToken.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
		JSONObject jsonObject = getMethodResult(getOAuthAccessToken,appId);
		OAuthAccessToken accessToken=new OAuthAccessToken();
		accessToken.setAccessToken(jsonObject.getString("access_token"));
		accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
		accessToken.setRefreshToken(jsonObject.getString("refresh_token"));
		accessToken.setOpenid(jsonObject.getString("openid"));
		accessToken.setScope(jsonObject.getString("scope"));
		return accessToken;
	}

	/**
	 * 微信OAuth2.0授权-刷新access_token（如果需要）
	 */
	public OAuthAccessToken refershOAuthAccessToken(String appid,
			String refresh_token,String appId) throws Exception {
		String getreferAccessUrl=WeiXinApiUrl.getreferAccessUrl.replace("APPID", appid).replace("REFRESH_TOKEN", refresh_token);
		JSONObject jsonObject =getMethodResult(getreferAccessUrl,appId);
		OAuthAccessToken accessToken=new OAuthAccessToken();
		accessToken.setAccessToken(jsonObject.getString("access_token"));
		accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
		accessToken.setRefreshToken(jsonObject.getString("refresh_token"));
		accessToken.setOpenid(jsonObject.getString("openid"));
		accessToken.setScope(jsonObject.getString("scope"));
		return accessToken;
		
	}

	/**
	 * 微信OAuth2.0授权-检验授权凭证（access_token）是否有效
	 */
	public boolean isAccessTokenValid(String accessToken, String openId,String appId)
			throws Exception {
		String isOAuthAccessToken=WeiXinApiUrl.isOAuthAccessToken.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		JSONObject jsonObject = getMethodResult(isOAuthAccessToken,appId);
		int returnNum=jsonObject.getInt(Constants.ERRCODE);
		if(returnNum == 0){
			return true;
		}
		return false;
	}

	/**
	 * 微信OAuth2.0授权-获取用户信息（网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息）
	 */
	public UserEntity acceptOAuthUserNews(String accessToken, String openId,String appId)
			throws Exception {
		String getOAuthUserNews=WeiXinApiUrl.getOAuthUserNews.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		JSONObject jsonObject = getMethodResult(getOAuthUserNews,appId);
		
		UserEntity entity=new UserEntity();
		entity.setOpenid(jsonObject.getString("openid"));
		entity.setNickname(jsonObject.getString("nickname"));
		entity.setSex(jsonObject.getInt("sex"));
		entity.setProvince(jsonObject.getString("province"));
		entity.setCity(jsonObject.getString("city"));
		entity.setCountry(jsonObject.getString("country"));
		entity.setHeadimgurl(jsonObject.getString("headimgurl"));
		return entity;
	}

}

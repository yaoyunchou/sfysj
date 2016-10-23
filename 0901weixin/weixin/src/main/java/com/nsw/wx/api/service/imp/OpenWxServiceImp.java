package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.OpenWxService;

public class OpenWxServiceImp  extends BasicServiceImp implements  OpenWxService{

	@Override
	public JSONObject getComponentAccessToken(JSONObject postPara,String appId) {
		
		 String url = WeiXinApiUrl.component_access_token_Post;
		 return postMethodResult(url, postPara.toString(),appId);
	}
	

	@Override
	public JSONObject getPreAuthCode(String component_access_token,JSONObject component_appid,String appId) {
		// TODO Auto-generated method stub
		 String url = WeiXinApiUrl.pre_auth_code_Post.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		 return postMethodResult(url, component_appid.toString(),appId);
	}


	@Override
	public JSONObject getAuthorizerAccessTokenAndRefreshToken(
			String component_access_token, JSONObject entity,String appId) {
		 String url = WeiXinApiUrl.api_query_auth_Post.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		 return postMethodResult(url, entity.toString(),appId);
	}


	@Override
	public JSONObject getAuthorizerInfo(String component_access_token,
			JSONObject entity,String appId) {
		 String url = WeiXinApiUrl.api_get_authorizer_info_Post.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		 return postMethodResult(url, entity.toString(),appId);
	}
}

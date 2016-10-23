package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.ShorturlService;
import com.nsw.wx.api.util.AccessTokenHelper;
import com.nsw.wx.common.util.Constants;

public class ShorturlServiceImp extends BasicServiceImp implements ShorturlService{

	@Override
	public JSONObject longUrlToShort(String longUrl, String accessToken,String appId) {
		String url = WeiXinApiUrl.ShorturlCreate_Post.replace(Constants.ACCESSTOKEN, accessToken);
		
		String paras ="{\"action\":\"long2short\",\"long_url\":\""+longUrl+"\"}";

		
		return postMethodResult(url, paras,appId);
	}
	

}

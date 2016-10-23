package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.DateModel;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.DataNewsService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.api.util.JSONHelper;

public class DataNewsServiceImp extends BasicServiceImp implements DataNewsService {

	@Override
	public JSONObject getarticlesummary(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getarticlesummary_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getarticletotal(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getarticletotal_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getuserread(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getuserread_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getuserreadhour(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getuserreadhour_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getusershare(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getusershare_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getusersharehour(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getusersharehour_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

}

package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.DateModel;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.DataUserService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.api.util.JSONHelper;

public class DataUserServiceImp extends BasicServiceImp implements DataUserService{

	@Override
	public JSONObject getusersummary(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getusersummary_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getusercumulate(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getusercumulate_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

}

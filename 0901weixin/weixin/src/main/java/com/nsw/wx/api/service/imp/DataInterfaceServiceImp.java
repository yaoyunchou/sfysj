package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.DateModel;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.DataInterfaceService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.api.util.JSONHelper;

public class DataInterfaceServiceImp extends BasicServiceImp implements DataInterfaceService{

	@Override
	public JSONObject getinterfacesummary(String accesstoken,
			DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getinterfacesummary_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getinterfacesummaryhour(String accesstoken,
			DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getinterfacesummaryhour_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

}

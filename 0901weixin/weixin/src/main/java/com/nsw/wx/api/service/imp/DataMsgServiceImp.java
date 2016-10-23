package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.DateModel;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.DataMsgService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.api.util.JSONHelper;

public class DataMsgServiceImp  extends BasicServiceImp implements DataMsgService{

	@Override
	public JSONObject getupstreammsg(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getupstreammsg_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getupstreammsghour(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getupstreammsghour_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getupstreammsgweek(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getupstreammsgweek_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getupstreammsgmonth(String accesstoken,
			DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getupstreammsgmonth_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getupstreammsgdist(String accesstoken, DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getupstreammsgdist_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getupstreammsgdistweek(String accesstoken,
			DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getupstreammsgdistweek_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getupstreammsgdistmonth(String accesstoken,
			DateModel dateModel,String appId) {
		String url = WeiXinApiUrl.Getupstreammsgdistmonth_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		String jsonStr = JSONHelper.bean2json(dateModel);
		return postMethodResult(url, jsonStr,appId);
	}

}

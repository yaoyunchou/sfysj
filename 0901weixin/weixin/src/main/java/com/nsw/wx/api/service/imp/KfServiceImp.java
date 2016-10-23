package com.nsw.wx.api.service.imp;



import net.sf.json.JSONObject;

import com.nsw.wx.api.model.Custorm;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.KfService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.api.util.JSONHelper;

public class KfServiceImp extends BasicServiceImp implements KfService{

	@Override
	public JSONObject addCustorm(String accessToken, Custorm custorm,String appId) {
		String url = WeiXinApiUrl.Addkf_Post.replace(Constants.ACCESSTOKEN, accessToken);
		String jsonStr = JSONHelper.bean2json(custorm);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject editCustorm(String accessToken, Custorm custorm,String appId) {
		String url = WeiXinApiUrl.Editkf_Post.replace(Constants.ACCESSTOKEN, accessToken);
		String jsonStr = JSONHelper.bean2json(custorm);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject delCustorm(String accessToken, Custorm custorm,String appId) {
		String url = WeiXinApiUrl.Delkf_Post.replace(Constants.ACCESSTOKEN, accessToken);
		String jsonStr = JSONHelper.bean2json(custorm);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getCustormList(String accessToken,String appId) {
		String url = WeiXinApiUrl.GetKfList_Get.replace(Constants.ACCESSTOKEN, accessToken);
		return getMethodResult(url,appId);
	}

	@Override
	public JSONObject sendMsg(String accessToken, JSONObject msgJson,String appId) {
		// TODO Auto-generated method stub
		String url = WeiXinApiUrl.KfSend_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, msgJson.toString(),appId);
	}

	

}

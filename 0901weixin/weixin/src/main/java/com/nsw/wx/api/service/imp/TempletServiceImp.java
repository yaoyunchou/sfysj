package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.TempletService;
import com.nsw.wx.common.util.Constants;

/**
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月13日 下午3:06:11
* @Description: TODO
 */
public class TempletServiceImp extends BasicServiceImp implements TempletService{

	@Override
	public JSONObject getTempletId(String accessToken, String para,String appId) {
		String url = WeiXinApiUrl.GetTempletID_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, para,appId);
	}

	@Override
	public JSONObject sendTempletMsg(String accessToken, String para,String appId) {
		String url = WeiXinApiUrl.SendTempletMsg_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, para,appId);
	}

	@Override
	public JSONObject setIndustry(String accessToken, String para,String appId) {
		String url = WeiXinApiUrl.SetIndustry_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, para,appId);
	}

	@Override
	public JSONObject getIndustry(String accessToken, String appId) {
		String url = WeiXinApiUrl.GetIndustry_Get.replace(Constants.ACCESSTOKEN, accessToken);
		return getMethodResult(url,appId);
	}

	@Override
	public JSONObject getAllTemplate(String accessToken, String appId) {
		String url = WeiXinApiUrl.GetAllTemplate_Get.replace(Constants.ACCESSTOKEN, accessToken);
		return getMethodResult(url,appId);
	}

	@Override
	public JSONObject delTemplate(String accessToken,String template_id, String appId) {
		JSONObject templateData = new JSONObject();
		templateData.put("template_id",template_id);
		String url = WeiXinApiUrl.GetAllTemplate_Get.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, templateData.toString(),appId);
	}

}

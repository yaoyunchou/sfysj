package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.UserTagService;
import com.nsw.wx.common.util.Constants;

public class UserTagServiceImp  extends BasicServiceImp  implements  UserTagService{

	@Override
	public JSONObject createTag(String accessToken,  String tagName  , String appId) {
		String url = WeiXinApiUrl.createTag_Post.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject data = new JSONObject();
		JSONObject name = new JSONObject();
		name.put("name", tagName);
		data.put("tag",name);
		return postMethodResult(url, data.toString(),appId);
	}

	@Override
	public JSONObject getTagList(String accessToken, String appId) {
		// TODO Auto-generated method stub
		String url = WeiXinApiUrl.getTagList_Get.replace(Constants.ACCESSTOKEN, accessToken);
		return getMethodResult(url,appId);
	}

	@Override
	public JSONObject editTag(String accessToken, JSONObject data, String appId) {
		String url = WeiXinApiUrl.editTag_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, data.toString(),appId);
	}

	@Override
	public JSONObject delTag(String accessToken, int tagId, String appId) {
		// TODO Auto-generated method stub
		String url = WeiXinApiUrl.delTag_Post.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject data = new JSONObject();
		JSONObject id = new JSONObject();
		id.put("id", tagId);
		data.put("tag", id);
		return postMethodResult(url, data.toString(),appId);
	}

	@Override
	public JSONObject getFansByTag(String accessToken, int tagId,String next_openid, String appId) {
		// TODO Auto-generated method stub
		String url = WeiXinApiUrl.getFansByTag_Post.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject data = new JSONObject();
		data.put("tagid", tagId);
		data.put("next_openid", next_openid);
		return postMethodResult(url, data.toString(),appId);
	}

	@Override
	public JSONObject batchAddTagToFans(String accessToken,
			String[] openIdList, int tagId, String appId) {
		// TODO Auto-generated method stub
		String url = WeiXinApiUrl.batchAddTagToFans_Post.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject data = new JSONObject();
		data.put("openid_list", openIdList);
		data.put("tagid", tagId);
		return postMethodResult(url, data.toString(),appId);
	}

	@Override
	public JSONObject batchDelTagToFans(String accessToken,
			String[] openIdList, int tagId, String appId) {
		// TODO Auto-generated method stub
		String url = WeiXinApiUrl.batchDelTagToFans_Post.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject data = new JSONObject();
		data.put("openid_list", openIdList);
		data.put("tagid", tagId);
		return postMethodResult(url, data.toString(),appId);
	}

	@Override
	public JSONObject getUserTags(String accessToken, String openId,
			String appId) {
		// TODO Auto-generated method stub
		String url = WeiXinApiUrl.getUserTags_Post.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject data = new JSONObject();
		data.put("openid", openId);
		return postMethodResult(url, data.toString(),appId);
	}

}

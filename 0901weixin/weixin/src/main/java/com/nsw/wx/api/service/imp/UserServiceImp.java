package com.nsw.wx.api.service.imp;

import java.util.List;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.UserService;
import com.nsw.wx.common.util.Constants;

/**
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月13日 下午3:06:21
* @Description: TODO
 */
public class UserServiceImp extends BasicServiceImp implements UserService {
	
	
	@Override
	public JSONObject getUserInfo(String accessToken, String openId,String appId) {
		String getUserNews=WeiXinApiUrl.GetUserBaseInfo_Get.replace(Constants.ACCESSTOKEN, accessToken).replace("OPENID", openId);
		JSONObject object =getMethodResult(getUserNews,appId);
		return object;
	}

	@Override
	public JSONObject setUserRemark(String accessToken, String openid , String remark,String appId) {
		JSONObject obj = new JSONObject();
		obj.put("openid", openid);
		obj.put("remark", remark);
		String url = WeiXinApiUrl.Remark_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, obj.toString(),appId);
	}

	@Override
	public JSONObject getUserList(String accessToken, String next_openid,String appId) {
		next_openid = next_openid==null?"":next_openid;
		String getUserNews=WeiXinApiUrl.GetUserInfoList_Get.replace(Constants.ACCESSTOKEN, accessToken).replace("NEXT_OPENID", next_openid);
		JSONObject object = getMethodResult(getUserNews,appId);
		return object;
	}

	@Override
	public JSONObject createGroup(String accessToken, String name,String appId) {
		
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		JSONObject obj1 = new JSONObject();
		obj1.put("group", obj);
		String url = WeiXinApiUrl.CreateGroup_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, obj1.toString(),appId);
	}

	@Override
	public JSONObject getAllGroup(String accessToken,String appId) {
		String url=WeiXinApiUrl.GetGroup_Get.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject object = getMethodResult(url,appId);
		return object;
	}

	@Override
	public JSONObject getUsersGroup(String accessToken, String openid,String appId) {
		
		JSONObject obj = new JSONObject();
		obj.put("openid", openid);
		String url = WeiXinApiUrl.GetGroupByUser_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, obj.toString(),appId);
	}

	@Override
	public JSONObject editGroup(String accessToken, int id, String name,String appId) {
		JSONObject obj = new JSONObject();
		obj.put("id", id);
		obj.put("name", name);
		JSONObject obj1 = new JSONObject();
		obj1.put("group", obj);
		String url = WeiXinApiUrl.EditGroupName_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, obj1.toString(),appId);
	}

	@Override
	public JSONObject changeUserGroup(String accessToken, String openid ,int to_groupid,String appId) {
		JSONObject obj = new JSONObject();
		obj.put("to_groupid", to_groupid);
		obj.put("openid", openid);
		String url = WeiXinApiUrl.MoveGroupforUser_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, obj.toString(),appId);
	}

	@Override
	public JSONObject batchChangeUsersGroup(String accessToken, List<String> openid_list,int to_groupid ,String appId) {
		
		JSONObject obj = new JSONObject();
		obj.put("openid_list", openid_list);
		obj.put("to_groupid", to_groupid);
		String url = WeiXinApiUrl.MoveGroupforManyUser_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, obj.toString(),appId);
	}

	@Override
	public JSONObject deleteGroup(String accessToken, int id,String appId) {
		JSONObject obj = new JSONObject();
		obj.put("id", id);
		JSONObject data = new JSONObject();
		data.put("group", obj);
		String url = WeiXinApiUrl.DeleteGroup_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, data.toString(),appId);
	}
}

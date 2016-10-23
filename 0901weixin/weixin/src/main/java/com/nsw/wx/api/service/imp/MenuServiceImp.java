package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.MenuCreate;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.MenuService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.api.util.JSONHelper;
/**
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月13日 下午3:05:55
* @Description: TODO
 */
public class MenuServiceImp extends BasicServiceImp implements MenuService{

	@Override
	public JSONObject createMenu(JSONObject jsonStr, String accessToken,String appId) {
		String url = WeiXinApiUrl.MenuCreate_Post.replace(Constants.ACCESSTOKEN, accessToken);
		//String jsonStr = JSONHelper.bean2json(button);
		return postMethodResult(url, jsonStr.toString(),appId);
	}

	@Override
	public JSONObject getMenu(String accessToken,String appId) {
		String url = WeiXinApiUrl.Getmenu_Get.replace(Constants.ACCESSTOKEN, accessToken);
		return getMethodResult(url,appId);
	}

	@Override
	public JSONObject deleteMenu(String accessToken,String appId) {
		String url = WeiXinApiUrl.DeleteMenu_Get.replace(Constants.ACCESSTOKEN, accessToken);
		return getMethodResult(url,appId);
	}

}

package com.nsw.wx.api.service.imp;

import net.sf.json.JSONObject;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.QrCodeService;
import com.nsw.wx.common.util.Constants;

public class QrCodeServiceImp  extends BasicServiceImp  implements QrCodeService{

	@Override
	public JSONObject createGrcode(String accesstoken, JSONObject jsonData,
			String appId) {
		String url = WeiXinApiUrl.CreateGrcode_Post.replace(Constants.ACCESSTOKEN, accesstoken);
		return postMethodResult(url, jsonData.toString(),appId);
	}

}

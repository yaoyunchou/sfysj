package com.nsw.wx.api.service;

import net.sf.json.JSONObject;

/**
 * 微信二维码接口
 * @author liuzp
 *
 */
public interface QrCodeService extends BasicServices{
	
	/**
	 * 创建微信二维码
	 * @param accesstoken
	 * @param jsonData
	 * @param appId
	 * @return
	 */
	public JSONObject createGrcode(String accesstoken,JSONObject jsonData,String appId);
}

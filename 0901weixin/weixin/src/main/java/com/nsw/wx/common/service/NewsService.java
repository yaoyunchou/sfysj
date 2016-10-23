package com.nsw.wx.common.service;

import java.util.Map;

import net.sf.json.JSONObject;

public interface NewsService {
	
	JSONObject newsUpload(String appId,Map<String,Object> news);

	/**
	 * 同步图文job
	 * @param appId
	 */
	public void syncNews(String appId);
	
}

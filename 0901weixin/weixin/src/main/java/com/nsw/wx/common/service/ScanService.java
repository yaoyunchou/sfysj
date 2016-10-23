package com.nsw.wx.common.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;


/**
 * 
 * @author liuzp
 *二维码扫描处理
 */
public interface ScanService {
	/**
	 * 
	 * @param map
	 * @param appId
	 * @param type
	 */
	void scanEvent(HttpServletResponse response ,Map<String, String> map,String appId,int type);
	
	
}

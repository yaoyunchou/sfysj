package com.nsw.wx.api.service;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.DateModel;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月21日 下午7:55:49
* @Description: 接口分析数据接口指的是用于获得公众平台官网数据统计模块中接口分析数据的接口，具体接口列表如无用户属性数据接口
 */
public interface DataInterfaceService  extends BasicServices{

	/**
	 * 
	* @Description: 获取接口分析数据
	* @param @param accesstoken
	* @param @param dateModel
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getinterfacesummary(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取接口分析分时数据
	* @param @param accesstoken
	* @param @param dateModel
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getinterfacesummaryhour(String accesstoken,DateModel dateModel,String appId);
}

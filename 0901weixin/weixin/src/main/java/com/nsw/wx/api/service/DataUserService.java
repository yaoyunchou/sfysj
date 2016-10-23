package com.nsw.wx.api.service;

import com.nsw.wx.api.model.DateModel;

import net.sf.json.JSONObject;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月21日 下午7:18:23
* @Description: 用户分析数据接口
 */
public interface  DataUserService  extends BasicServices{
	/**
	 * 
	* @Description: 获取用户增减数据
	* @param @param accesstoken
	* @param @param dateModel 时间范围参数 包含起始和结束日期 时间跨度 7
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getusersummary(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取累计用户数据 
	* @param @param accesstoken
	* @param @param dateModel 时间范围参数 包含起始和结束日期 时间跨度 7
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getusercumulate(String accesstoken,DateModel dateModel,String appId);
	

}

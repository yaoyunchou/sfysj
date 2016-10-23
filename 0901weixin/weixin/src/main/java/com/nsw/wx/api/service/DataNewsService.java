package com.nsw.wx.api.service;

import com.nsw.wx.api.model.DateModel;

import net.sf.json.JSONObject;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月21日 下午7:18:23
* @Description: 图文分析数据接口
 */
public interface  DataNewsService  extends BasicServices{
	
	/**
	 * 
	* @Description: 获取图文群发每日数据
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:1
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getarticlesummary(String accesstoken,DateModel dateModel,String appId);
	
	/**
	 * 
	* @Description: 获取图文群发总数据 
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:1
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getarticletotal(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取图文统计数据
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:3
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getuserread(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取图文统计分时数据
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:1
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getuserreadhour(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取图文分享转发数据
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:7
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getusershare(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取图文分享转发分时数据
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:1
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getusersharehour(String accesstoken,DateModel dateModel,String appId);
	

}

package com.nsw.wx.api.service;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.DateModel;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月21日 下午8:03:59
* @Description: 消息分析数据接口指的是用于获得公众平台官网数据统计模块中消息分析数据的接口
 */
public interface DataMsgService  extends BasicServices {

	/**
	 * 
	* @Description: 获取消息发送概况数据
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:7
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getupstreammsg(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取消息分送分时数据 
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:1
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getupstreammsghour(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取消息发送周数据
	* @param @param accesstoken
	* @param @param dateModel dateModel 日期模型 最大时间跨度:30
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getupstreammsgweek(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取消息发送月数据
	* @param @param accesstoken
	* @param @param dateModel dateModel 日期模型 最大时间跨度:30
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getupstreammsgmonth(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取消息发送分布数据
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:15
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getupstreammsgdist(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取消息发送分布周数据 
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:30
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getupstreammsgdistweek(String accesstoken,DateModel dateModel,String appId);
	/**
	 * 
	* @Description: 获取消息发送分布月数据
	* @param @param accesstoken
	* @param @param dateModel 日期模型 最大时间跨度:30
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getupstreammsgdistmonth(String accesstoken,DateModel dateModel,String appId);

}

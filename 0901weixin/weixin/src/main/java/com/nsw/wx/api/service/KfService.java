package com.nsw.wx.api.service;

import com.nsw.wx.api.model.Custorm;

import net.sf.json.JSONObject;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月23日 上午9:51:34
* @Description: 客服接口
 */
public interface KfService extends BasicServices {
	
	
	/**
	 * 
	* @Description: 添加客服账号
	* @param @param accessToken
	* @param @param custorm
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject addCustorm(String accessToken,Custorm custorm,String appId);
	/**
	 * 
	* @Description: 修改客服账号
	* @param @param accessToken
	* @param @param custorm
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject editCustorm(String accessToken,Custorm custorm,String appId);
	/**
	 * 
	* @Description: 删除客服账号
	* @param @param accessToken
	* @param @param custorm
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject delCustorm(String accessToken,Custorm custorm,String appId);
	
	/**
	 * 
	* @Description: 获取客服列表
	* @param @param accessToken
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getCustormList(String accessToken,String appId);
	
	
	
	public  JSONObject sendMsg(String accessToken,JSONObject msgJson,String appId);

}

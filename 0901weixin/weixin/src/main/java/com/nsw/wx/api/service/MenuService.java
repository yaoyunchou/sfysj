package com.nsw.wx.api.service;

import com.nsw.wx.api.model.MenuCreate;

import net.sf.json.JSONObject;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月13日 上午10:09:20
* @Description: 自定义菜单管理接口
 */
public interface MenuService extends BasicServices{
	
	/**
	 * 
	* @Description: 创建菜单
	* @param @param button 菜单实体
	* @return JSONObject  
	* 		正确返回参数：  {"errcode":0,"errmsg":"ok"} 
	* 		错误示例：          {"errcode":40018,"errmsg":"invalid button name size"}
	* @throws
	 */
	public JSONObject createMenu(JSONObject button,String accessToken,String appId);
	/**
	* @Description: 获取自定义菜单 
	* @param @param accessToken
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getMenu(String accessToken,String appId);
	/**
	 * 
	* @Description:删除当前使用的自定义菜单 
	* @param @param accessToken
	* @return JSONObject  
	* 			正确返回参数：  {"errcode":0,"errmsg":"ok"} 
	* 			错误示例：          {"errcode":40018,"errmsg":"invalid button name size"}
	* @throws
	 */
	public JSONObject deleteMenu(String accessToken,String appId);
}

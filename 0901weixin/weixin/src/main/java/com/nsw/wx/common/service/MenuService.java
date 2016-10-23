package com.nsw.wx.common.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.bson.types.ObjectId;


public interface MenuService {
	
	/**
	 * 
	* @Description: 获取公众号菜单接口
	* @param @param appId
	* @param @return   
	* @return Map<String,Object> 
	* @throws
	 */
	Map<String,Object>  getMenu(String appId);
	
	/**
	 * 
	* @Description:删除菜单
	* @param @param menuId
	* @param @return   
	* @return boolean  
	* @throws
	 */
	boolean delMainMenu(String id);
	
	/**
	 * 
	* @Description: 更新菜单
	* @param @param menu
	* @param @return   
	* @return boolean  
	* @throws
	 */
	boolean updateMenu(Map<String,Object> menu);
	
	
	
	Map<String,Object> addMenu(Map<String,Object> menu);
	
	/**
	 * 
	* @Description: 将菜单信息同步到微信服务器
	* @param @param entity 菜单实体
	* @param @param accessToken
	* @param @return   
	* @return boolean  
	* @throws
	 */
	boolean syncWxServer(JSONObject entity,String accessToken,String appId);
	
	/**
	 * 
	* @Description: 处理菜单事件
	* @param    
	* @return void  
	* @throws
	 */
	void handMenuEvent( HttpServletResponse response,Map<String,String> wxMsg,String appId,int type);
	
	
}

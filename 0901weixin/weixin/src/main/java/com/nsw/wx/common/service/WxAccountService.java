package com.nsw.wx.common.service;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Update;

import com.nsw.wx.common.docmodel.Account;

public interface WxAccountService {
	
	
	/**
	 * 
	* @Description:获取企业下的所以微信公众号信息
	* @param @param siteId
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws
	 */
	List<Map<String,Object>> getWxAccountList(String siteId);
	
	
	List<Map<String,Object>> getWxAccountListByFilter(String site,String filter);
	
	
	/**
	 * 
	* @Description: 获取微信账号 
	* @param @param id
	* @param @return   
	* @return Map<String,Object>  
	* @throws
	 */
	Map<String,Object> getAccount(String id);
	
	
	/**
	 * 
	* @Description: 获取微信账号 
	* @param @param appId
	* @param @return   
	* @return Map<String,Object>  
	* @throws
	 */
	Map<String,Object> getAccountByAppId(String appId);
	
	
	/**
	 * 
	* @Description: 获取微信账号 
	* @param @param userName
	* @param @return   
	* @return Map<String,Object>  
	* @throws
	 */
	Map<String,Object> getAccountByUserName(String userName);
	
	
	/**
	 * 
	* @Description: 获取微信账号 
	* @param @param url
	* @param @return   
	* @return Map<String,Object>  
	* @throws
	 */
	Map<String,Object> getAccountByUrl(String url);
	
	
	
	/**
	 * 
	* @Description: 更新微信账号 
	* @param @param entity
	* @param @return   
	* @return boolean
	* @throws
	 */
	boolean updateAccount(Map<String,Object> account,Update update);
	
	
	/**
	 * 
	* @Description: 删除公众账号 
	* @param @param appId
	* @param @return   
	* @return boolean  
	* @throws
	 */
	boolean deleteAccount(Map<String,Object> account);
	
	/**
	 * 
	* @Description: 添加公共账号
	* @param @param entity
	* @param @return   
	* @return boolean  
	* @throws
	 */
	Map<String,Object> addAccount(Map<String,Object> entity);

}

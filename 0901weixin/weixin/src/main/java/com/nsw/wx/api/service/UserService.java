package com.nsw.wx.api.service;

import java.util.List;

import net.sf.json.JSONObject;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月13日 上午10:10:00
* @Description: 用户管理接口
 */
public interface UserService extends BasicServices{
	
	
	/**
	* @Description: 获取用户基本信息 
	* @param @param accessToken  调用接口凭证
	* @param @param openId 用户标识
	* @param @return
	* @param @throws Exception   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getUserInfo(String accessToken, String openId,String appId) ;
	/**
	 * 
	* @Description: 设置用户备注名
	* @param @param accessToken
	* @param @param openid 用户唯一标识id
	* @param @param remark 备注
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject setUserRemark(String accessToken, String openid,String remark,String appId);
	/**
	 * 
	* @Description: 获取用户列表 
	* @param @param accessToken 调用接口凭证
	* @param @param next_openid 第一个拉取的OPENID，不填默认从头开始拉取
	* @param @return
	* @param @throws Exception   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getUserList(String accessToken, String next_openid,String appId);

	/**
	 * 
	* @Description: 创建分组
	* @param @param accessToken
	* @param @param name 分组名称
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject createGroup(String accessToken, String name,String appId);
	/**
	 * 
	* @Description: 查询所有分组
	* @param @param accessToken
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getAllGroup(String accessToken,String appId);
	/**
	 * 
	* @Description: 查询用户所在分组
	* @param @param accessToken
	* @param @param openid 用户标识
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getUsersGroup(String accessToken, String openid,String appId);
	/**
	 * 
	* @Description: 修改分组名
	* @param @param accessToken
	* @param @param id 组id
	* @param @param name 组名称
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject editGroup(String accessToken, int id , String name,String appId);
	/**
	 * 
	* @Description: 移动用户分组
	* @param @param accessToken
	* @param @param openid 用户标识id
	* @param @param to_groupid 组id
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject changeUserGroup(String accessToken, String openid ,int to_groupid,String appId);
	/**
	 * 
	* @Description:批量移动用户到分组
	* @param @param accessToken
	* @param @param openid_list 用户openid集合
	* @param @param to_groupid  组id
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject batchChangeUsersGroup(String accessToken, List<String> openid_list,int to_groupid ,String appId);
	
	/**
	 * 
	* @Description: 删除分组
	* @param @param accessToken
	* @param @param groupId 组id
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject deleteGroup(String accessToken,int groupId,String appId);
}

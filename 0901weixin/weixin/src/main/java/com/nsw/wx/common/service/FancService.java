package com.nsw.wx.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FancService {

	/**
	 * 
	* @Description: 拉取粉丝组信息
	* @param @param accessToken
	* @param @return   
	* @return boolean  
	* @throws
	 */
	boolean loadFansGroup(String appId);
	
	/**
	 * 
	* @Description: 拉取粉丝信息
	* @param @param appId
	* @param @param accessToken
	* @param @return   
	* @return boolean  
	* @throws
	 */
	boolean loadFansList(String appId,String next_openid);

	/**
	 * 根据标签拉取粉丝信息
	 * @param appId
	 * @param tagId
	 * @return
	 */
	boolean loadFansByTagId(String appId,String tagId);

	/**
	 * 删除粉丝
	 * @param appId
	 * @param openId
	 * @return
	 */
	boolean delFans(String appId,String openId);
	/**
	 * 添加粉丝
	 * @param appId
	 * @param openId
	 * @return
	 */
	boolean addFans(String appId,String openId);
	
	/**
	 * 获取粉丝组粉丝数量
	 * @param appId
	 * @param groupId
	 * @return
	 */
	long getFansCountByGroupId(String appId, String groupId);

	/**
	 * 获取标签组粉丝数量
	 * @param appId
	 * @param tagId
	 * @return
	 */
	long getFansCountByTagId(String appId, String tagId);
	
	/**
	 * 获取数据库粉丝信息
	 * @param appId
	 * @param openId
	 * @return
	 */
	Map<String,Object> getFans(String appId,String openId);
	
	/**
	 * 拉取粉丝标签信息  (获取公众号已创建的标签)
	 * @param appId
	 * @return
	 */
	boolean  loadFansTags(String appId);
	
	/**
	 * 通过标签获取粉丝
	 * @param appId
	 * @param tagId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	Map<String,Object> getFansByTag(String appId,String tagId,String filter,String pageNum,String pageSize);
	
	/**
	 * 
	 * @param appId
	 * @param tagId
	 * @return
	 */
	public List<HashMap> getFansByTag(String appId, String tagId);
	
	/**
	 * 根据appId和openId查询粉丝信息
	 * @param appId
	 * @param openId
	 * @return
	 */
	Map<String,Object>  getFansByOpenId(String appId,String openId);
	
	/**
	 * 获取全部粉丝数量 和黑名单数量
	 * @param appId
	 * @return
	 */
	Map<String,Object>  getFansCount(String appId);

	/**
	 * 同步粉丝标签和粉丝信息
	 * @param appId
	 * @return
	 */
	boolean  syncTagAndFans(String appId);

	/**
	 * 同步粉丝、标签的job
	 * @param appId
	 */
	 void syncJobDo(String appId);
	
}

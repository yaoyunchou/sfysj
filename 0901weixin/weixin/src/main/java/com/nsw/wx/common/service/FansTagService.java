package com.nsw.wx.common.service;

import java.util.List;

import net.sf.json.JSONObject;
import org.springframework.data.mongodb.core.query.Update;

import com.nsw.wx.common.docmodel.FansTag;

public interface FansTagService {

	
	/**
	 * 获取标签信息
	 * @param id
	 * @return
	 */
	FansTag  getTag(String id);
	
	/**
	 * 
	 * @param appId
	 * @param name
	 * @return
	 */
	public FansTag getTagByName(String appId,String name) ;
	
	/**
	 * 移除数据库所有标签
	 * @param appId
	 * @return
	 */
	boolean  delAllTag(String appId);
	
	/**
	 * 删除标签
	 * @param tagId
	 * @return
	 */
	boolean  delTag(String appId, String tagId);
	
	/**
	 * 获取标签列表
	 * @param appId
	 * @return
	 */
	List<FansTag> getList(String appId);
	
	/**
	 * 添加标签
	 * @param tag
	 * @return
	 */
	boolean addTag(FansTag tag);
	
	/**
	 * 更新标签
	 * @param tag
	 * @return
	 */
	boolean updateTag(Update update, FansTag tag);
	
	/**
	 * 
	 * @param tagId
	 * @return
	 */
	long getFansCountByTag(String appId, String tagId) ;

	/**
	 * 获取标签
	 * @param id
	 * @param appId
	 * @return
	 */
	public FansTag getTagById(int id,String appId);

	/**
	 * 获取微信标签的集合
	 * @param accessToken
	 * @param appId
	 * @return
	 */
	List<Integer> getWxTagIdList(String accessToken,String appId);



}

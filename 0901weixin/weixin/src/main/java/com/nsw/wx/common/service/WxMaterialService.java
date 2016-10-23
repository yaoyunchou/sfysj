package com.nsw.wx.common.service;

import java.util.HashMap;
import java.util.List;

import com.nsw.wx.common.views.Message;

public interface WxMaterialService {
	
	/**
	 * 将微信素材数据同步到本地
	 * @param appId
	 * @param type   (image|news)
	 * @return
	 */
	Message SyncWxMaterial(String appId , String type);
	
	/**
	 * 获取素材总数 
	 * @param appId
	 * @param type  (image|news)
	 * @param filter
	 * @return
	 */
	Long wxMaterialCount(String appId,String type,String filter);
	
	/**
	 * 素材的分页
	 * @param appId
	 * @param type  (image|news)
	 * @param filter
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	//List<HashMap> listMaterial(String appId,String type,String filter,int pageNum,int pageSize);

}

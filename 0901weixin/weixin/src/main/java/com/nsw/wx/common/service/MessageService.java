package com.nsw.wx.common.service;

public interface MessageService {

	/**
	 * 根据图片id获取素材id
	 * @param fileId
	 * @param appId
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public String  getImageMedia(String fileId,String appId);
	
	/**
	 * 获取回复图片消息字符串
	 * @param mediaId
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public String resImage(String mediaId ,String toUserName, String fromUserName);
	/**
	 * 获取回复文本消息字符串 
	 * @param fromUserName
	 * @param toUserName
	 * @param content
	 * @return
	 */
	public String resText(String fromUserName,String toUserName ,String content);
	
	public String resMusic();
	
	public String resNews(String appId,String newsId,String fromUserName,String toUserName ) ;
	
	
}

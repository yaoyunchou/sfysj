package com.nsw.wx.common.service;

import java.util.Map;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月30日 下午7:38:42
* @Description: 消息接收处理(目前针对图片，语音，链接，文字进行处理)
 */
public interface RequestMsgService {
	
	/**
	 * 
	* @Description: 保存用户发送过来的消息
	* @param @param appId
	* @param @param msgType
	* @param @param contentMap   
	* @return void  
	* @throws
	 */
	void saveRequestMsg(String appId,Map<String,String> contentMap);

	/**
	 * 检测消息是否重复
	 * @param appId
	 * @param contentMap
	 * @return
	 */
	boolean isRepeat(String appId,Map<String,String> contentMap);

}

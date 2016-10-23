package com.nsw.wx.common.service;

import com.nsw.wx.common.docmodel.MassMessage;
import com.nsw.wx.common.views.Message;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Map;

public interface SendMassMsgService {
	
	/**
	 * 
	* @Description: 群发后处理服务器推送的消息，保存到数据库
	* @param    
	* @return void  
	* @throws
	 */
	void handEvent(Map<String, String> map,String appId);

	/**
	 * 定时群发
	 * @param massMsg
	 * @return
	 */
	Message sendMassMsgByJob(MassMessage massMsg);

	/**
	 *对微信图文内容进行正则过滤，对外链的image  src进行匹配并上传到微信获取url
	 * @param content
	 * @param appId
	 * @param accessToken
	 * @return
	 */
	public String handleContentByRegex(String content, String appId,String accessToken);

	/**
	 * 上传图文消息
	 * @param arr
	 * @param appId
	 * @param accessToken
	 * @return
	 */
	public JSONObject UploadNews(JSONArray arr, String appId, String accessToken);

	/**
	 *上传图文消息
	 * @param msg
	 * @param accessToken
	 * @return
	 */
	public JSONObject UploadNews(MassMessage msg, String accessToken);

	/**
	 *
	 * @param appId
	 * @param fileId
	 * @param accessToken
	 * @param fileType
	 * @return
	 */
	public JSONObject UploadThumbMediaImage(String appId,String fileId,String accessToken,String fileType);

	/**
	 * 上传图片到临时素材
	 * @param appId
	 * @param fileId
	 * @param accessToken
	 * @param fileType
	 * @return
	 */
	public JSONObject UploadTempImage(String appId,String fileId,String accessToken,String fileType);

}

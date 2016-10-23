package com.nsw.wx.api.service;

import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.nsw.wx.api.model.Media;
import com.nsw.wx.api.model.MediaListPara;
import com.nsw.wx.api.model.News;
import com.nsw.wx.api.model.VideoInfo;

import net.sf.json.JSONObject;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月13日 下午3:07:47
* @Description: 素材管理
 */
public interface MaterialService extends BasicServices{
	
	/**
	 * 
	* @Description: 添加临时素材 
	* @param @param accessToken 调用接口凭证
	* @param @param type    媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
							图片（image）: 不超过1M，支持JPG格式
							语音（voice）：不超过2M，播放长度不超过60s，支持AMR\MP3格式
							视频（video）：不超过10MB，支持MP4格式
							缩略图（thumb）：不超过64KB，支持JPG格式
	* @param @param filePath  要上传的文件=
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject addTempMatter(String accessToken, String type, InputStream file,String fileName,String appId);
	
	/**
	 * 
	* @Description: 下载临时素材
	* @param @param accessToken 调用接口凭证
	* @param @param media_id 媒体文件ID
	* @param @param saveDir  文件保存路径 示例:e://test 或者 e://test/
	* @param @return   
	* @return    返回值为新的文件路径    不为空即成功
	* @throws
	 */
	public String downLoadTempMatter(String accessToken, String media_id, String saveDir,String appId);
	
	/**
	 *  1、新增的永久素材也可以在公众平台官网素材管理模块中看到
		2、永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
		3、素材的格式大小等要求与公众平台官网一致。具体是，图片大小不超过2M，支持bmp/png/jpeg/jpg/gif格式，语音大小不超过5M，长度不超过60秒，支持mp3/wma/wav/amr格式

	* @Description: 上传永久素材
	* @param @param accessToken
	* @param @param filePath 要上传的文件
	* @param @param videoInfo 封装的视频信息，上传视频需要传入的参数，其他类型文件可传null即可
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject addMatter(String accessToken, InputStream file,String fileName,String type,String appId);
	/**
	 * 
	* @Description: 获取永久素材
	* @param @param accessToken 调用接口凭证
	* @param @param media_id 媒体文件ID  必须是永久文件的媒体id
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject downLoadMatter(String accessToken, Media media_id,String appId);
	/**
	 * 
	* @Description: 删除永久素材 
	* @param @param accessToken 调用接口凭证
	* @param @param media_id 媒体id
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject delMatter(String accessToken, Media media_id,String appId);
	/**
	 * 
	* @Description: 获取素材总数 
	* @param @param accessToken
	* @param @return   返回值里分别可取到各类素材的总数
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getMatterCount(String accessToken,String appId);
	
	/**
	 * 
	* @Description: 获取素材列表
	* @param @param accessToken
	* @param @param mediaListPara 返回列表信息
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getMatterList(String accessToken, MediaListPara mediaListPara,String appId);
	
	/**
	 * 
	* @Description: 上传图文接口
	* @param @param accessToken
	* @param @param news
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject addNews(String accessToken,News news,String appId);
	
	/**
	 * 
	* @Description: 上传图片接口，返回的json里包含成功后的url
	* @param @param accessToken
	* @param @param type
	* @param @param file
	* @param @param fileName
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject uploadImageLogo(String accessToken, InputStream file,String fileName,String appId);
	
	/**
	 * 
	* @Description: 上传媒体文件
	* @param @param accessToken
	* @param @param file
	* @param @param type
	* @param @param fileName
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject uploadMedia(String accessToken, InputStream file,String type,String fileName,String appId);
	
	
}

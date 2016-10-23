package com.nsw.wx.api.service;

import com.nsw.wx.api.model.Media;
import com.nsw.wx.api.model.News;
import com.nsw.wx.api.model.mass.ImageMass;
import com.nsw.wx.api.model.mass.ImageText;
import com.nsw.wx.api.model.mass.Mass;
import com.nsw.wx.api.model.mass.TextMass;
import com.nsw.wx.api.model.mass.VedioMass;
import com.nsw.wx.api.model.mass.VoiceMass;

import net.sf.json.JSONObject;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月20日 下午2:21:06
* @Description: 群发消息管理
 */
public interface MessageService extends BasicServices {

	/**
	 * 
	* @Description:根据分组进行图文消息群发【订阅号与服务号认证后均可用】
	* 				
	* @param @param imageText
	* @param @param accessToken
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject massImageTextByGroup(ImageText imageText,String accessToken,String appId,String openId);

	/**
	 * 根据分组进行群发
	 * @param jsonStr
	 * @param accessToken
	 * @param appId
	 * @return
	 */
	public JSONObject massMsgByGroup(String jsonStr,String accessToken,String appId,String openId);
	
	/**
	 * 
	* @Description:根据分组进行文本群发【订阅号与服务号认证后均可用】
	* @param @param textMass
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject massTextByGroup(TextMass textMass,String accessToken,String appId,String openId);
	
	/**
	 * 
	* @Description: 根据分组进行群发图片信息
	* @param @param imageMass
	* @param @param accessToken
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject massImageByGroup(ImageMass imageMass,String accessToken ,String appId,String openId);
	
	
	
	/**
	 * 
	* @Description: 根据分组进行语音群发【订阅号与服务号认证后均可用】
	* @param @param voiceMass
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject massVoiceByGroup(VoiceMass voiceMass,String accessToken,String appId);
	
	
	/**
	 * 
	* @Description: 根据分组进行视频群发【订阅号与服务号认证后均可用】
	* @param @param vedioMass
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject massVedioByGroup(VedioMass vedioMass,String accessToken,String appId);
	
	
	/**
	 * 
	* @Description: 获取群发消息状态
	* @param @param mass
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getMassStatus(Mass mass,String accessToken,String appId);
	
	
	
	public JSONObject delMass(String msg_id,String accessToken,String appId);
	
	/**
	 * 
	* @Description: 上传图文消息素材【订阅号与服务号认证后均可用】
	* @param @param news
	* @param @return   
	* @return JSONObject  
	*  正确返回结果
	* {
		   "type":"news",
		   "media_id":"CsEf3ldqkAYJAU6EJeIkStVDSvffUJ54vqbThMgplD-VJXXof6ctX5fI6-aYyUiQ",
		   "created_at":1391857799
		}
	* @throws
	 */
	public JSONObject uploadNews(News news,String accessToken,String appId);
}

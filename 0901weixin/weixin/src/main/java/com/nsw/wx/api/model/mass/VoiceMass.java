package com.nsw.wx.api.model.mass;

import com.nsw.wx.api.model.Media;


/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月20日 上午11:59:58
* @Description: 群发消息之语音
 */
public class VoiceMass extends BaseMass{

	//群发类型  语音
	private String msgtype = "voice";
	
	private Media voice;

	public Media getVoice() {
		return voice;
	}

	public void setVoice(Media voice) {
		this.voice = voice;
	}

	public String getMsgtype() {
		return msgtype;
	}
}

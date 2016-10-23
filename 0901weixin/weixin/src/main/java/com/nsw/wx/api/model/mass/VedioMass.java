package com.nsw.wx.api.model.mass;

import com.nsw.wx.api.model.Media;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月20日 上午11:59:29
* @Description: 群发消息之视频
 */
public class VedioMass extends BaseMass{

	private String msgtype = "mpvideo";
	
	private Media mpvideo;

	public String getMsgtype() {
		return msgtype;
	}

	public Media getMpvideo() {
		return mpvideo;
	}

	public void setMpvideo(Media mpvideo) {
		this.mpvideo = mpvideo;
	}
}

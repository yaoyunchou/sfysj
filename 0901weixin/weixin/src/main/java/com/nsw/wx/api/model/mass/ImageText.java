package com.nsw.wx.api.model.mass;

import com.nsw.wx.api.model.Media;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月20日 上午11:16:32
* @Description: 群发消息之图文消息
 */
public class ImageText extends BaseMass{
	
	private String msgtype = "mpnews";
	
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	private Media mpnews;

	public String getMsgtype() {
		return msgtype;
	}

	public Media getMpnews() {
		return mpnews;
	}

	public void setMpnews(Media mpnews) {
		this.mpnews = mpnews;
	}

}

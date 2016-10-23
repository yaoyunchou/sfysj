package com.nsw.wx.api.model.message.res;

import com.nsw.wx.api.common.MessageUtils;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月16日 下午3:47:50
* @Description: 响应的视频实体封装
 */
public class VedioMessageRes extends BaseMessageRes{
	
	private Video Video;
	
	public String getXmlStr(){
		MessageUtils m = new MessageUtils();
		return m.getXmlForBean(this);
	}

	public Video getVideo() {
		return Video;
	}

	public void setVideo(Video video) {
		Video = video;
	}
}

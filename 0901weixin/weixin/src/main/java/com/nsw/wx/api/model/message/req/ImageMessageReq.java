package com.nsw.wx.api.model.message.req;

import com.nsw.wx.api.common.MessageUtils;


/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月15日 下午5:33:15
* @Description: 图片消息
 */
public class ImageMessageReq extends BaseMessageReq {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	
	public String getXmlStr(){
		MessageUtils m = new MessageUtils();
		return m.getXmlForBean(this);
	}
	
}

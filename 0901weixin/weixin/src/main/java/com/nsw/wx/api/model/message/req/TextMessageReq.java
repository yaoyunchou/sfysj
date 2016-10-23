package com.nsw.wx.api.model.message.req;

import com.nsw.wx.api.common.MessageUtils;


/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月15日 下午5:32:35
* @Description: 文本消息
 */
public class TextMessageReq extends BaseMessageReq {
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	public String getXmlStr(){
		MessageUtils m = new MessageUtils();
		return m.getXmlForBean(this);
	}
}
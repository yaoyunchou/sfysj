package com.nsw.wx.api.model.message.req;

import com.nsw.wx.api.common.MessageUtils;


/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月15日 下午5:33:01
* @Description: 链接消息
 */
public class LinkMessageReq extends BaseMessageReq {
	// 消息标题
	private String Title;
	// 消息描述
	private String Description;
	// 消息链接
	private String Url;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}
	
	public String getXmlStr(){
		MessageUtils m = new MessageUtils();
		return m.getXmlForBean(this);
	}
}

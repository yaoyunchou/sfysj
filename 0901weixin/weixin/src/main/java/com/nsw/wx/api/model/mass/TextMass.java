package com.nsw.wx.api.model.mass;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月20日 上午11:59:45
* @Description: 群发消息之文本
 */
public class TextMass extends BaseMass{
	//消息类型
	private String msgtype = "text";
	//文本
	private Text text ;

	public String getMsgtype() {
		return msgtype;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}
	
}

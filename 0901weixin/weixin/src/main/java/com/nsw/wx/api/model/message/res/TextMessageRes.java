package com.nsw.wx.api.model.message.res;


import com.nsw.wx.api.common.MessageUtils;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月15日 下午5:16:47
* @Description: 文本消息
 */
public class TextMessageRes extends BaseMessageRes {
	// 文本内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	
	/**
	 * 
	* @Description: 获得微信格式的xml数据 
	* @param @return   
	* @return String  
	* @throws 返回示例
	* 	<xml>
		  <ToUserName><![CDATA[xsaxa]]></ToUserName>
		  <FromUserName><![CDATA[xsaxs]]></FromUserName>
		  <CreateTime><![CDATA[1444979411214]]></CreateTime>
		  <MsgType><![CDATA[text]]></MsgType>
		  <FuncFlag><![CDATA[0]]></FuncFlag>
		</xml>
	 */
	public String getXmlStr(){
		MessageUtils m = new MessageUtils();
		return m.getXmlForBean(this);
	}
}
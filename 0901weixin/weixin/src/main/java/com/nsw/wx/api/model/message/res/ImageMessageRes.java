package com.nsw.wx.api.model.message.res;

import com.nsw.wx.api.common.MessageUtils;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月16日 下午3:45:09
* @Description: 回复图片消息实体
 */
public class ImageMessageRes extends BaseMessageRes{
	
	private Image Image;
	
	public String getXmlStr(){
		MessageUtils m = new MessageUtils();
		return m.getXmlForBean(this);
	}

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}
	
	
}

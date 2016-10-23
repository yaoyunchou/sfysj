package com.nsw.wx.api.model.mass;

import com.nsw.wx.api.model.Media;


/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月20日 上午11:31:37
* @Description: 群发消息之图片
 */
public class ImageMass extends BaseMass{
	//消息类型
	private String msgtype = "image";
	//注意此处media_id需通过基础支持中的上传下载多媒体文件来得到
	private Media image;
	
	
	public Media getImage() {
		return image;
	}

	public void setImage(Media image) {
		this.image = image;
	}

	public String getMsgtype() {
		return msgtype;
	}
}

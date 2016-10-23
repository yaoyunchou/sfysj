package com.nsw.wx.api.model;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月15日 上午10:57:19
* @Description: 素材基类
 */
public class Media {
	
	public Media(){}
	
	public Media(String media_id){
		this.media_id = media_id;
	}
	
	//素材id
	private String  media_id;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
}

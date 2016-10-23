package com.nsw.wx.api.model;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月15日 下午2:20:07
* @Description: TODO
 */
public class VideoInfo {
	
	private String title;//视频标题
	
	private String introduction;//视频描述
	
	public VideoInfo(){
		
	}

	public VideoInfo(String title, String introduction) {
		this.title = title;
		this.introduction = introduction;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}

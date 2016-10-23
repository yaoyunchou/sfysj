package com.nsw.wx.api.model.message.res;

import com.nsw.wx.api.common.MessageUtils;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月16日 下午3:56:45
* @Description: 微信响应之音乐对象封装
 */
public class MusicMessageRes extends BaseMessageRes {

	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
	
	public String getXmlStr(){
		MessageUtils m = new MessageUtils();
		return m.getXmlForBean(this);
	}
	
	
}

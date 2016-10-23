package com.nsw.wx.common.json;
/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月27日 下午6:28:34
* @Description: json处理的异常基类
*/
public class JSONAccessException extends RuntimeException {
	private static final long serialVersionUID = 6751739892881112518L;

	public JSONAccessException(String msg) {
		super(msg);
	}
	
	public JSONAccessException(Throwable cause){
		super(cause);
	}

	public JSONAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}
}

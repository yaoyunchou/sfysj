package com.nsw.wx.common.model;

import java.io.Serializable;


/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月17日 上午10:53:58
* @Description: 文件上传内置进度对象
*/
public class Progress implements Serializable {

	private static final long serialVersionUID = -3594348614126639843L;
	Long pBytesRead;
	Long pContentLength;
	int pItems;

	public Long getpBytesRead() {
		return pBytesRead;
	}
	public void setpBytesRead(Long pBytesRead) {
		this.pBytesRead = pBytesRead;
	}
	public Long getpContentLength() {
		return pContentLength;
	}
	public void setpContentLength(Long pContentLength) {
		this.pContentLength = pContentLength;
	}
	public int getpItems() {
		return pItems;
	}
	public void setpItems(int pItems) {
		this.pItems = pItems;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"pBytesRead\":" + pBytesRead);
		sb.append(", \"pContentLength\":" + pContentLength);
		sb.append(", \"pItems\":" + pItems);
		sb.append("}");
		return sb.toString();  
	}
	
}

package com.nsw.wx.common.docmodel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月19日 下午1:28:20
* @Description: 微信图片库表
 */
//@Document(collection = "weixin_image")
public class WxImage {
	
	//@Transient
	private String id;
	//@Id
	private ObjectId _id; 
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	private String fileId;
	
	private String name;
	
	private String update_time;
	
	private String url ;
	
	private String appId;
}

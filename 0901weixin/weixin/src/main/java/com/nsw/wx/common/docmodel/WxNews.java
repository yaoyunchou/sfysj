package com.nsw.wx.common.docmodel;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月19日 下午1:38:49
* @Description: 微信图文库表
 */
//@Document(collection = "weixin_news")
public class WxNews {
	
	private String appId;
	
	private String update_time;
	
	//@Transient
	private String id;
	//@Id
	private ObjectId _id;
	
	private List<WxArticle> articles;
	
	public void addArticles(WxArticle wa){
		articles.add(wa);
	}
	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

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

	public List<WxArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<WxArticle> articles) {
		this.articles = articles;
	}


	public String getUpdate_time() {
		return update_time;
	}


	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
}

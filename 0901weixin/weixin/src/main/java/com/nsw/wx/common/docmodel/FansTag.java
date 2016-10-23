package com.nsw.wx.common.docmodel;

import java.io.Serializable;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
/**
 * 粉丝标签
 * @author liuzp
 *
 */
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "weixin_fanstag")
public class FansTag  implements Serializable{
	
	private static final long serialVersionUID = 551542714602356629L;
	
	@Id
	private ObjectId _id;
	
	private String appId;
	
	private int tagId;
	
	private String name;
	
	private int count;

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
}

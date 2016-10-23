package com.nsw.wx.common.docmodel;

import org.bson.types.ObjectId;
/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月24日 上午11:54:13
* @Description: 粉丝组
 */
public class FansGroup extends BasicAccount{
	
	private String  id;//组id
	
	
	private int groupid;
	
	private String name;//组名称
	
	private int count; //组内数量
	
	private ObjectId _id ;//主键


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


}

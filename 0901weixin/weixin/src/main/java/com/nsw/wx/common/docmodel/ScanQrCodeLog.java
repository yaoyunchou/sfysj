package com.nsw.wx.common.docmodel;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 扫描二维码日志
 * @author liuzp
 *
 */
@Document(collection = "weixin_scanQrCodeLog")
public class ScanQrCodeLog implements Serializable{
	
	private static final long serialVersionUID = 511342714602356629L;
	
	@Id
	private ObjectId _id;//主键
	
	private String appId;//关联公众号
	
	private String scanDate;//扫描日期
	
	private String scene_id;//场景id
	
	private String id;//与二维码关联的主键
	
	//private String fromUserName;//来自哪个用户扫描
	
	private int scanNum;//当天扫描次数
	
	private int followNum;//当天关注人数

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

	public String getScanDate() {
		return scanDate;
	}

	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
	}

	public String getScene_id() {
		return scene_id;
	}

	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getScanNum() {
		return scanNum;
	}

	public void setScanNum(int scanNum) {
		this.scanNum = scanNum;
	}

	public int getFollowNum() {
		return followNum;
	}

	public void setFollowNum(int followNum) {
		this.followNum = followNum;
	}
	
	

	
}

package com.nsw.wx.common.docmodel;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月31日 下午1:59:01
* @Description: 日志表
 */
@Document(collection = "weixin_sysLog")
public class SysLog {
	
	public SysLog(){
		
	}
	
	public SysLog(String operationCode,String appId,String type,String remark){
		this.operationCode = operationCode;
		this.appId =appId;
		this.type=type;
		this.remark=remark;
	}
	

	private ObjectId _id;//主键
	
	private String userId;//用户id
	
	private String appId;//所操作公众号
	
	private String userName;//用户名
	
	private String operationCode;//日志对应的操作码
	
	private String createdTime;//日志创建时间
	
	private String type;//操作类型   system  user
	
	private String remark;//备注
	
	private String ip;//操作人id
	
	private String site;//企业id
	
	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}


	
	
	
	
}

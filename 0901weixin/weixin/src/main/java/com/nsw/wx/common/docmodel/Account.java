package com.nsw.wx.common.docmodel;


import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月16日 上午10:19:23
* @Description: 微信账户表
 */
//@Document(collection = "weixin_account")
public class Account implements Serializable{
	
	private static final long serialVersionUID = 511542714602346629L;
	
	//@Id
	private ObjectId _id; 
	
	@Transient
	private String id;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
	private String  encodingAesKey;//安全模式下的需要的加密加密码
	private boolean  encryptType;//是否加密
	private String  appId;
	private String  token;
	private String  create_time;//信息添加时间
	private int  type;////1为第三方平台，2为授权绑定公众号，3为手工绑定公众号
	private String  appSecret;
	private String  component_verify_ticket;//第三方平台接收到的ticket
	private String  update_time;//信息更新时间
	private String  component_access_token;//第三方平台的token
	private String  refresh_time;//刷新时间
	private String  site;//企业号id
	private String  nick_name;//昵称
	private String  user_name;//原始id
	private String  qrcode_url;//二维码url
	private int  bindType;//绑定类型 0. 订阅号 2.服务号
	private int  verify_type_info;//授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
	private String  head_img;//微信图像
	private String  url;//手动绑定需要填写的url
	private String  authorizer_refresh_token;//授权绑定的刷新票据
	private String  authorizer_access_token;//手动绑定和授权绑定的token
	
	private String name;//微信号
	
	public String getAuthorizer_refresh_token() {
		return authorizer_refresh_token;
	}
	public void setAuthorizer_refresh_token(String authorizer_refresh_token) {
		this.authorizer_refresh_token = authorizer_refresh_token;
	}
	public String getAuthorizer_access_token() {
		return authorizer_access_token;
	}
	public void setAuthorizer_access_token(String authorizer_access_token) {
		this.authorizer_access_token = authorizer_access_token;
	}
	@Transient  
	private boolean  newAccount;//标志是否是新建账号
	
	
	
	public boolean getNewAccount() {
		return newAccount;
	}
	public void setNewAccount(boolean newAccount) {
		this.newAccount = newAccount;
	}

	public String getEncodingAesKey() {
		return encodingAesKey;
	}
	public void setEncodingAesKey(String encodingAesKey) {
		this.encodingAesKey = encodingAesKey;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getComponent_verify_ticket() {
		return component_verify_ticket;
	}
	public void setComponent_verify_ticket(String component_verify_ticket) {
		this.component_verify_ticket = component_verify_ticket;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getComponent_access_token() {
		return component_access_token;
	}
	public void setComponent_access_token(String component_access_token) {
		this.component_access_token = component_access_token;
	}
	public String getRefresh_time() {
		return refresh_time;
	}
	public void setRefresh_time(String refresh_time) {
		this.refresh_time = refresh_time;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getQrcode_url() {
		return qrcode_url;
	}
	public void setQrcode_url(String qrcode_url) {
		this.qrcode_url = qrcode_url;
	}
	public int getBindType() {
		return bindType;
	}
	public void setBindType(int bindType) {
		this.bindType = bindType;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public int getVerify_type_info() {
		return verify_type_info;
	}
	public void setVerify_type_info(int verify_type_info) {
		this.verify_type_info = verify_type_info;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isEncryptType() {
		return encryptType;
	}
	public void setEncryptType(boolean encryptType) {
		this.encryptType = encryptType;
	}
	public ObjectId get_id() {
		return _id;
	}
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
}

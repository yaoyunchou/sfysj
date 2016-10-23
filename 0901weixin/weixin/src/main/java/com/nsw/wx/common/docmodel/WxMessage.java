package com.nsw.wx.common.docmodel;

public class WxMessage {
	
	private String appId;

	//发出消息还是收到消息(get,out)
	private String getOrPut;

	//接收方账号
	private String openId;
	//消息创建时间
	private String createTime;
	//消息类型
	private String msgType;//txt news pic
	//文本消息内容
	private String content;// txt类型的必填值
	//通过素材管理接口上传多媒体文件，得到的id
	private String mediaId;  //图文为key
	
	private String fileId;//图片为fileId的值   
	
	
	public String getGetOrPut() {
		return getOrPut;
	}
	public void setGetOrPut(String getOrPut) {
		this.getOrPut = getOrPut;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

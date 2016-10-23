package com.nsw.wx.common.docmodel;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author liuzp
 *二维码数据来源统计
 */
@Document(collection = "weixin_qrCode")
public class QrCode  implements Serializable{
	
	private static final long serialVersionUID = 511542714602356629L;
	
	@Id
	private ObjectId _id;//主键
	@Transient
	private String id;//主键传递字符串
	//appId
	private String appId;
	//二维码标题
	private String title;
	//二维码类型(temp |forever)  临时 | 永久
	private String type;
	//扫描后默认的用户分组(groupId)
	private String groupId;
	//扫描后是否触发自动回复(false |true)
	private boolean reply ;
	//触发自动回复类型(txt | image | news)
	private  String replyType;
	//自动回复类型信息 （文本  |图片 | 图文 ）（内容 |fileId|图文主键）
	private String replyContent;
	//备注
	private String  remark;
	//扫描次数
	private long scanNum;
	//已关注扫描次数
	private long scanFollowNum;
	//新增用户
	private long addFollowNum;
	//最新扫描时间
	private String scanDate;
	//二维码创建时间
	private String createTime;
	//二维码更新时间
	private String updateTime;
	//二维码图片
	private String qrCodeUrl;
	//场景值id
	private String scene_id;
	//扫描二维码的url
	private String url;

	private String style;//nomal  personality


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
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getReplyType() {
		return replyType;
	}
	public void setReplyType(String replyType) {
		this.replyType = replyType;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getScanNum() {
		return scanNum;
	}
	public void setScanNum(long scanNum) {
		this.scanNum = scanNum;
	}
	public long getScanFollowNum() {
		return scanFollowNum;
	}
	public void setScanFollowNum(long scanFollowNum) {
		this.scanFollowNum = scanFollowNum;
	}
	public long getAddFollowNum() {
		return addFollowNum;
	}
	public void setAddFollowNum(long addFollowNum) {
		this.addFollowNum = addFollowNum;
	}
	public String getScanDate() {
		return scanDate;
	}
	public void setScanDate(String scanDate) {
		this.scanDate = scanDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getQrCodeUrl() {
		return qrCodeUrl;
	}
	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}
	public String getScene_id() {
		return scene_id;
	}
	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isReply() {
		return reply;
	}
	public void setReply(boolean reply) {
		this.reply = reply;
	}


	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}

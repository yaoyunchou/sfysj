package com.nsw.wx.common.docmodel;

import net.sf.json.JSONObject;

/**
 * Created by liuzp on 2016/8/24.
 * 模板消息
 */
public class TemplateMsg {

	private String id;

	private  String appId;//公众号唯一标识

	private  boolean job;//是否是定时任务

	private  String sendTime;//定时发送时间

	private  String  title;//标题

	private String templateId;//模板id

	private String url;//消息链接

	private String content;//模版内容

	private JSONObject para;//参数{"aaa",:"111","bbb":222}

	private  String  groupId;//发送对象，发送全部为-100,否则为标签id的值(tagId)

	private String createTime;//创建时间

	private  String updateTime;//更新时间

	private boolean status ;//禁用启动

	private String sendStatus;//发送状态

	private String jobId ;//定时任务id




	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public JSONObject getPara() {
		return para;
	}

	public void setPara(JSONObject para) {
		this.para = para;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public boolean isJob() {
		return job;
	}

	public void setJob(boolean job) {
		this.job = job;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
}

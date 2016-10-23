package com.nsw.wx.common.docmodel;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月26日 下午2:46:16
* @Description: 群发
 */
public class MassMessage {
		
	private String appId;
	
	private String massType;//群发类型  txt pic news
	
	//private int sex ; //性别 1,2,3  男 女 全部
	
	private String content;//如果类型为 txt  则 文本内容不能为空
	
	private String fileId;//如果类型为pic  则不为空
	
	private String groupid;//组id   如果为全部用户  则为-100
	
	private String mediaId;//素材id 如果类型为news 则不为空
	
	//private String province;//省份 （群发地区）

	private String jobId;
	
	private String openId;

	private String jobTime;//定时发送时间

	public  String getJobId(){
		return  jobId;
	}
	public  void setJobId(String jobId){
		this.jobId =  jobId;
	}

	public String getJobTime(){
		   return jobTime;
	}
	public void  setJobTime(String jobTime){
		this.jobTime = jobTime;
	}
	
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMassType() {
		return massType;
	}

	public void setMassType(String massType) {
		this.massType = massType;
	}

/*	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}
*/
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

/*	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
*/
	
	
	
}

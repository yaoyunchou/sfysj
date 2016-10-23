package com.nsw.wx.api.model.message.res;

public class Video {
	// 通过素材管理接口上传多媒体文件，得到的id
	private String MediaId;

	// 视频消息的标题 非必须
	private String Title;

	// 视频消息的描述 非必须
	private String Description;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

}

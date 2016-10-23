package com.nsw.wx.common.docmodel;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月19日 下午3:27:22
* @Description: 文章实体
 */
public class WxArticle {
	//图文消息缩略图的media_id，可以在基础支持-上传多媒体文件接口中获得
		private String fileId;
		//图文消息的作者 非必须字段
		private String author;
		//图文消息的标题
		private String title;
		//在图文消息页面点击“阅读原文”后的页面 非必须字段
		private String content_source_url;
		//图文消息页面的内容，支持HTML标签。具备微信支付权限的公众号，可以使用a标签，其他公众号不能使用
		private String content;
		//图文消息的描述  非必须字段
		private String digest;
		//是否显示封面，1为显示，0为不显示  非必须字段
		private String show_cover_pic;
		
		private String url ;//图文页的URL，或者，当获取的列表是图片素材列表时，该字段是图片的URL 


		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent_source_url() {
			return content_source_url;
		}

		public void setContent_source_url(String content_source_url) {
			this.content_source_url = content_source_url;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getDigest() {
			return digest;
		}

		public void setDigest(String digest) {
			this.digest = digest;
		}

		public String getShow_cover_pic() {
			return show_cover_pic;
		}

		public void setShow_cover_pic(String show_cover_pic) {
			this.show_cover_pic = show_cover_pic;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getFileId() {
			return fileId;
		}

		public void setFileId(String fileId) {
			this.fileId = fileId;
		}
}

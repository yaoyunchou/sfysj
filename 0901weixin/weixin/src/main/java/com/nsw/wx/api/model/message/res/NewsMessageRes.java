package com.nsw.wx.api.model.message.res;

import java.util.List;

import com.nsw.wx.api.common.MessageUtils;


/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月15日 下午5:15:45
* @Description: 微信响应之图文消息
 */
public class NewsMessageRes extends BaseMessageRes {
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
	
	
	public String getXmlStr(){
		MessageUtils u = new MessageUtils();
		u.xstream.alias("xml", new NewsMessageRes().getClass());
		u.xstream.alias("item", new Article().getClass());
		return u.xstream.toXML(this);
	}
	
}
package com.nsw.wx.api.service;

import net.sf.json.JSONObject;

public interface UserTagService  extends BasicServices{
	
	
/*	//1. 创建标签
	http请求方式：POST（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/tags/create?access_token=ACCESS_TOKEN

	POST数据格式：JSON

	POST数据例子：

	{

	  "tag" : {

	    "name" : "广东"//标签名

	  }

	}*/
	
	public JSONObject createTag(String accessToken, String tagName ,String appId);
	
	
	
/*	2. 获取公众号已创建的标签
	http请求方式：GET（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/tags/get?access_token=ACCESS_TOKEN
*/	
	public JSONObject getTagList(String accessToken,String appId);
	
	
	
/*	3. 编辑标签
	http请求方式：POST（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/tags/update?access_token=ACCESS_TOKEN

	POST数据格式：JSON

	POST数据例子：

	{

	  "tag" : {

	    "id" : 134,

	    "name" : "广东人"

	  }

	}*/
	
	public JSONObject editTag(String accessToken, JSONObject data, String appId);
	
	/*
	4. 删除标签

	请注意，当某个标签下的粉丝超过10w时，后台不可直接删除标签。此时，开发者可以对该标签下的openid列表，先进行取消标签的操作，直到粉丝数不超过10w后，才可直接删除该标签。

	接口调用请求说明
	http请求方式：POST（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=ACCESS_TOKEN

	POST数据格式：JSON

	POST数据例子：

	{

	  "tag":{

	       "id" : 134

	  }

	}*/
	public JSONObject delTag(String accessToken, int tagId, String appId);
	
	
/*	5. 获取标签下粉丝列表

	接口调用请求说明

	http请求方式：GET（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=ACCESS_TOKEN

	POST数据格式：JSON

	POST数据例子：

	{

	  "tagid" : 134,

	  "next_openid":""//第一个拉取的OPENID，不填默认从头开始拉取

	}*/
	
	public JSONObject getFansByTag(String accessToken, int tagId,String next_openid, String appId);
	
	
	
/*	标签功能目前支持公众号为用户打上最多三个标签。

	1. 批量为用户打标签

	接口调用请求说明

	http请求方式：POST（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=ACCESS_TOKEN

	POST数据格式：JSON

	POST数据例子：

	{

	  "openid_list" : [//粉丝列表

	    "ocYxcuAEy30bX0NXmGn4ypqx3tI0",

	    "ocYxcuBt0mRugKZ7tGAHPnUaOW7Y"

	  ],

	  "tagid" : 134

	}*/
	
	public JSONObject batchAddTagToFans(String accessToken, String[] openIdList,int tagId, String appId);
	
	
/*	2. 批量为用户取消标签

	接口调用请求说明

	http请求方式：POST（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=ACCESS_TOKEN

	POST数据格式：JSON

	POST数据例子：

	{

	  "openid_list" : [//粉丝列表

	    "ocYxcuAEy30bX0NXmGn4ypqx3tI0",

	    "ocYxcuBt0mRugKZ7tGAHPnUaOW7Y"

	  ],

	  "tagid" : 134

	}*/
	
	public JSONObject batchDelTagToFans(String accessToken, String[] openIdList,int tagId, String appId);
	
	
/*	3. 获取用户身上的标签列表

	接口调用请求说明

	http请求方式：POST（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=ACCESS_TOKEN

	POST数据格式：JSON

	POST数据例子：

	{

	  "openid" : "ocYxcuBt0mRugKZ7tGAHPnUaOW7Y"

	}*/
	
	public JSONObject getUserTags(String accessToken, String openId, String appId);
	
}

package com.nsw.wx.api.model;

import java.util.List;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月14日 上午10:53:26
* @Description: TODO
 */
public class MenuButton
{
	
	/**菜单的响应动作类型*/
	private String type;
	/**
	 * 响应动作类型有：
	 * 1、click：点击推事件
	 * 2、view：跳转URL
	 * 3、scancode_push：扫码推事件
	 * 4、scancode_waitmsg：扫码推事件且弹出“消息接收中”提示框
	 * 5、pic_sysphoto：弹出系统拍照发图
	 * 6、pic_photo_or_album：弹出拍照或者相册发图
	 * 7、pic_weixin：弹出微信相册发图器
	 * 8、location_select：弹出地理位置选择器
	 * 9、media_id：下发消息（除文本消息）
	 * 10、view_limited：跳转图文消息URL
	 */
	
	/**菜单标题，不超过16个字节，子菜单不超过40个字节*/
	private String name;
	
	/**菜单KEY值，用于消息接口推送，不超过128字节*/
	private String key;
	
	/**网页链接，用户点击菜单可打开链接，不超过256字节*/
	private String url;
	
	/**
	 * 二级菜单数组，个数应为1~5个
	 * 非必须字段
	 */
	private List<MenuButton> sub_button;
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getKey()
	{
		return key;
	}
	
	public void setKey(String key)
	{
		this.key = key;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public List<MenuButton> getSub_button()
	{
		return sub_button;
	}
	
	public void setSub_button(List<MenuButton> sub_button)
	{
		this.sub_button = sub_button;
	}
	
}

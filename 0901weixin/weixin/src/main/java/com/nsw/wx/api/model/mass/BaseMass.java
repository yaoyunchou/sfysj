package com.nsw.wx.api.model.mass;


/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月20日 上午10:14:51
* @Description: 群发基础配置
 */
public class BaseMass {

	//用于设定图文消息的接收者
	private  MassFilter filter;

	public MassFilter getFilter() {
		return filter;
	}

	public void setFilter(MassFilter filter) {
		this.filter = filter;
	}
	
	
	
}

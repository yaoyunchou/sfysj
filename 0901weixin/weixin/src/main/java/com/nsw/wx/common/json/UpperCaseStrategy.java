package com.nsw.wx.common.json;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;

/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月27日 下午6:28:46
* @Description: 属性命名策略
*/
public class UpperCaseStrategy extends PropertyNamingStrategyBase{

	private static final long serialVersionUID = -4026899649446970771L;

	@Override
	public String translate(String propertyName) {
		return propertyName.toUpperCase();
	}

}

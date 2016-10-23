package com.nsw.wx.api.model;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月21日 下午7:40:40
* @Description: TODO
 */
public class DateModel {
	
	//数据的起始日期 yyyy-MM-dd
	private String begin_date ;
	//数据的结束日期 yyyy-MM-dd
	private String end_date;

	public String getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
}

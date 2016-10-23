package com.nsw.wx.common.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateUtils {
	
	public static String getCurrentTime(){
		return DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
	}
	
	public static String getCurrentMillis(){
		return DateTime.now().getMillis()+"";
	}
	
	public static String customFormatTime(String time ,String fromFormat,String toFormat){
		
		return DateTime.parse(time,DateTimeFormat.forPattern(fromFormat)).toString(toFormat);
	}
	
	public static String toYmd(String time){
		
		try {
			return DateTime.parse(time,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")).toString("yyyy-MM-dd");
		} catch (Exception e) {
			return DateTime.parse(time,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toString("yyyy-MM-dd");
		}
	}
}

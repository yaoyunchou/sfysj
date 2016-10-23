package com.nsw.wx.common.json;

import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月27日 下午6:27:59
* @Description: json工具类，这里对json实现做一个封装，如果需要换json实现，修改这个类
*/
public abstract class JSON {

	private static IJson json;
	static{
		//json = new GsonAdapter();
		//json = new StrutsJsonAdapter();
		json = new Jackson();
	}
	
	public static Map<String,Object> fromJsonObject(String jsonString){
		return json.fromJsonObject(jsonString);
	}
	
	public static Map<String,Object> fromJsonObject(Reader reader){
		return json.fromJsonObject(reader);
	}
	
	public static <T> T fromJsonObject(String jsonString, Class<T> clazz){
		return json.fromJsonObject(jsonString, clazz);
	}
	
	public static List<Map<String, Object>> fromJsonArray(String jsonString){
		return json.fromJsonArray(jsonString);
	}
	
	public static List<Map<String, Object>> fromJsonArray(Reader reader){
		return json.fromJsonArray(reader);
	}
	
	public static <T> String stringify(T bean){
		return json.stringify(bean);
	}

	public static <T> List<T> fromJsonArray(String jsonString,
			Class<List<?>> collectionClass, Class<T> elementClass) {
		return json.fromJsonArray(jsonString, collectionClass, elementClass);
		
	}
}

package com.nsw.wx.common.json;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月27日 下午6:29:08
* @Description: json适配接口
*/
public interface IJson {
	
	/** 
	* @Description: json串到map 
	* @param @param jsonString
	* @param @return   
	* @return Map<String,Object>  
	* @throws 
	*/ 
	Map<String,Object> fromJsonObject(String jsonString);
	
	/** 
	* @Description: json串流到Map
	* @param @param reader
	* @param @return   
	* @return Map<String,Object>  
	* @throws 
	*/ 
	Map<String,Object> fromJsonObject(Reader reader);
	
	/** 
	* @Description: json数组到list 
	* @param @param jsonString
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws 
	*/ 
	List<Map<String, Object>> fromJsonArray(String jsonString);
	
	/** 
	* @Description: json数组流到list 
	* @param @param reader
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws 
	*/ 
	List<Map<String, Object>> fromJsonArray(Reader reader);
	
	/** 
	* @Description: jons串到bean
	* @param @param jsonString
	* @param @param clazz
	* @param @return   
	* @return T  
	* @throws 
	*/ 
	<T> T fromJsonObject(String jsonString, Class<T> clazz);
	
	/** 
	* @Description: jons串流到bean 
	* @param @param io
	* @param @param clazz
	* @param @return   
	* @return T  
	* @throws 
	*/ 
	<T> T fromJsonObject(InputStream io, Class<T> clazz);
	
	/** 
	* @Description: json串到list<bean> 
	* @param @param jsonString
	* @param @param collectionClass
	* @param @param elementClass
	* @param @return   
	* @return List<T>  
	* @throws 
	*/ 
	<T> List<T> fromJsonArray(String jsonString, Class<List<?>> collectionClass, Class<T> elementClass);
	
	/** 
	* @Description: bean到json串 
	* @param @param bean
	* @param @return   
	* @return String  
	* @throws 
	*/ 
	<T> String stringify(T bean);
}

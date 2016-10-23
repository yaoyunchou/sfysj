package com.nsw.wx.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ObjectUtils;

import com.mongodb.BasicDBObject;

/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月17日 上午10:58:05
* @Description: map工具类
*/
public class MapUtil {
	 
	public static Logger logger = Logger.getLogger(MapUtil.class); 
	
	/**
	 * @param key
	 * @param value
	 * @return 构造过滤条件
	 */
	public static Map<String, Object> putFilter(String key, Object value){
		return putMap(key, value);
	}
	
	
	public static Map<String, Object> putMap(String key, Object value){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(key, value);
		return map;
	}
	public static Map<String, Object> put(Map<String, Object> map, String key, Object value){
		map.put(key, value);
		return map;
	}
	
    
	
	/**
	 * @param key
	 * @param value
	 * @return 更新map的属性
	 */
	public static Map update(Map map, String key, Object value){
		if(null != map.get(key)){
			map.put(key, value);
		}
		return map;
	}
	
	
	public static Map buildReMap(String... a){
		
		BasicDBObject map = new BasicDBObject();
		for (int i = 0; i < a.length; i++) {
			map.put(a[i], "");
		}
		return map;
		
	}
	
	
	public static boolean isEmpty(Object obj){
		
		if(obj == null){
			return true;
		}
		if(obj instanceof List){
			
			if(((List)obj).size() > 0){
				return false;
			}
		}else if(obj instanceof Map){
			if(((Map)obj).size() > 0){
				return false;
			}
		}else if(obj instanceof String ){
			if(StringUtils.isNotEmpty((String)obj)){
				return false;
			}
		}
		return true;
	}
	
	
	/** 
	* @Description: 通过.层级结构获取map中的值
	* @param @param map
	* @param @param key
	* @param @return   
	* @return Object  
	* @throws 
	*/ 
	public static Object getValue(Map map, String keyStr){
		if(null!=map && keyStr!=null){
			String[] keys = keyStr.split("\\.");
			if(keys.length ==1){
				if(null!=map.get(keys[0]) && map.get(keys[0]) instanceof Map){//对于值为map,则判断其values任意值不为空则返回该对象，否则返回null
					Map res =(Map)map.get(keys[0]);
					for(Object temp : res.values()){
						if(null!=temp){
							if(temp instanceof String){
								if(!StringUtils.isEmpty((String)temp)){
									return res;
								}
							}else{
								return res;
							}
						}
					}
					return null;
				}else{
					return map.get(keys[0]);
				}
			}else if(keys.length>1 && null!=map.get(keys[0]) && map.get(keys[0]) instanceof Map){
				return getValue((Map) map.get(keys[0]), keyStr.substring(keyStr.indexOf(".")+1));
			}
		}
		return null;
	}
}


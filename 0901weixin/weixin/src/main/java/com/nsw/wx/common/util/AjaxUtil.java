package com.nsw.wx.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月17日 上午10:58:05
* @Description: ajax请求返回封装工具类
*/
public class AjaxUtil {
	 
	public static Logger logger = Logger.getLogger(AjaxUtil.class); 
	
	private static String AJAX_RESPONSE_FLAG_KEY = "isSuccess";
	private static String AJAX_RESPONSE_DATA_KEY = "data";
	private static String AJAX_RESPONSE_LIST_KEY = "list";
	private static String AJAX_RESPONSE_TOTALITEMS_KEY = "totalItems";
	private static String AJAX_RESPONSE_MES_KEY = "message";
	
    /** 
    * @Description: 封装前端数据
    * @param @param response
    * @param @param content
    * @param @throws IOException   
    * @return void  
    * @throws 
    */ 
    private static void renderText(HttpServletResponse response, String content)  
        throws IOException {  
        response.setCharacterEncoding(Constants.UPPER_UTF8);  
        response.getWriter().write(content);  
    }    

    /** 
    * @Description: 封装ajax返回自定义结构的信息
    * @param @param response
    * @param @param isSuccess
    * @param @param responseTextMap   
    * @return void  
    * @throws 
    */ 
    public static void renderCustomMsg(HttpServletResponse response, boolean isSuccess,
         Map<String, Object> responseTextMap){  
        try {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put(AJAX_RESPONSE_FLAG_KEY, isSuccess);
        	map.putAll(responseTextMap);  
        	ObjectMapper mapper = new ObjectMapper();
			renderText(response, mapper.writeValueAsString(map));  
		} catch (Exception e) {
			logger.error("json数据封装异常", e); 
		}  
    }
    
    /** 
    * @Description: 封装成功的ajax返回字串信息 
    * @param @param response
    * @param @param isSuccess
    * @param @param msg   
    * @return void  
    * @throws 
    */ 
    public static void renderSuccessMsg(HttpServletResponse response, String msg){  
    	try {
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put(AJAX_RESPONSE_FLAG_KEY, true);
    		map.put(AJAX_RESPONSE_DATA_KEY, msg);
    		ObjectMapper mapper = new ObjectMapper();
    		renderText(response, mapper.writeValueAsString(map));  
    	} catch (Exception e) {
    		logger.error("json数据封装异常", e);
    	}  
    }
    
    /** 
     * @Description: 封装失败的ajax返回字串信息 
     * @param @param response
     * @param @param isSuccess
     * @param @param msg   
     * @return void  
     * @throws 
     */ 
    public static void renderFailMsg(HttpServletResponse response, String msg){  
    	try {
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put(AJAX_RESPONSE_FLAG_KEY, false);
    		map.put(AJAX_RESPONSE_DATA_KEY, msg);
    		ObjectMapper mapper = new ObjectMapper();
    		renderText(response, mapper.writeValueAsString(map));  
    	} catch (Exception e) {
    		logger.error("json数据封装异常", e);
    	}  
    }
    
    
    
    
    /** 
     * @Description: 封装ajax返回自定义结构的信息
     * @param @param response
     * @param @param isSuccess
     * @param @param responseTextMap   
     * @return void  
     * @throws 
     */ 
    public static String renderCustomMsg(boolean isSuccess,
    		Map<String, Object> responseTextMap){  
    	try {
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put(AJAX_RESPONSE_FLAG_KEY, isSuccess);
    		map.putAll(responseTextMap);  
    		ObjectMapper mapper = new ObjectMapper();
    		return mapper.writeValueAsString(map); 
    	} catch (Exception e) {
    		logger.error("json数据封装异常", e); 
    		return "";
    	}  
    }
    
    /** 
     * @Description: 封装ajax返回自定义分页结构的信息
     * @param @param response
     * @param @param isSuccess
     * @param @param list
     * @param @param totalItems
     * @param @param responseTextList   
     * @return void  
     * @throws 
     */ 
    public static String renderListPageMsg(boolean isSuccess,
    		List responseTextList, Long totalItems){  
    	try {
    		 
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(AJAX_RESPONSE_LIST_KEY, responseTextList);
			data.put(AJAX_RESPONSE_TOTALITEMS_KEY, totalItems);
			
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put(AJAX_RESPONSE_FLAG_KEY, isSuccess);
    		map.put(AJAX_RESPONSE_DATA_KEY, data);  
    		ObjectMapper mapper = new ObjectMapper();
    		return mapper.writeValueAsString(map); 
    	} catch (Exception e) {
    		logger.error("json数据封装异常", e); 
    		return "";
    	}  
    }
    
    
    /** 
     * @Description: 封装成功的ajax返回字串信息 
     * @param @param response
     * @param @param isSuccess
     * @param @param msg   
     * @return void  
     * @throws 
     */ 
    public static String renderSuccessMsg(String msg){  
    	try {
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put(AJAX_RESPONSE_FLAG_KEY, true);
    		map.put(AJAX_RESPONSE_DATA_KEY, msg);
    		ObjectMapper mapper = new ObjectMapper();
    		return mapper.writeValueAsString(map); 
    	} catch (Exception e) {
    		logger.error("json数据封装异常", e);
    		return "";
    	}  
    }
    
    /** 
     * @Description: 封装成功的ajax返回map信息 
     * @param @param response
     * @param @param isSuccess
     * @param @param msg   
     * @return void  
     * @throws 
     */ 
    public static String renderSuccessMsg(Object res){  
    	try {
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put(AJAX_RESPONSE_FLAG_KEY, true);
    		map.put(AJAX_RESPONSE_DATA_KEY, res);
    		ObjectMapper mapper = new ObjectMapper();
    		return mapper.writeValueAsString(map); 
    	} catch (Exception e) {
    		logger.error("json数据封装异常", e);
    		return "";
    	}  
    }
    

    
    /** 
     * @Description: 封装失败的ajax返回字串信息 
     * @param @param response
     * @param @param isSuccess
     * @param @param msg   
     * @return void  
     * @throws 
     */ 
    public static String renderFailMsg(String msg){  
    	try {
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put(AJAX_RESPONSE_FLAG_KEY, false);
    		map.put(AJAX_RESPONSE_DATA_KEY, msg);
    		ObjectMapper mapper = new ObjectMapper();
    		return mapper.writeValueAsString(map); 
    	} catch (Exception e) {
    		logger.error("json数据封装异常", e);
    		return "";
    	}  
    }
    
    
    public static void renderJson(HttpServletResponse response, boolean isSuccess, String msg){  
    	try {
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put(AJAX_RESPONSE_FLAG_KEY, isSuccess);
    		map.put(AJAX_RESPONSE_MES_KEY, msg);
    		ObjectMapper mapper = new ObjectMapper();
    		String json = mapper.writeValueAsString(map); 
    		renderText(response, json.toString());  
    	} catch (Exception e) {
    		logger.error("json数据封装异常", e);
    	}  
    }
    
    
}


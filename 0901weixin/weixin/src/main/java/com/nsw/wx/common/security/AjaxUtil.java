package com.nsw.wx.common.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 返回消息工具类
 * @author Administrator
 */
public class AjaxUtil {
	 
    public static void rendText(HttpServletResponse response, String content)  
        throws IOException {  
        response.setCharacterEncoding("UTF-8");  
        response.getWriter().write(content);  
    }    
       
    /**封装ajax返回自定义结构的信息
     * @param response
     * @param success
     * @param responseTextMap
     */
    public static void rendJson(HttpServletResponse response, boolean isSuccess,
         Map<String, Object> responseTextMap){  
        try {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("isSuccess", isSuccess);
        	map.putAll(responseTextMap);  
        	ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(map); 
			rendText(response, json.toString());  
		} catch (Exception e) {
			e.printStackTrace();  
		}  
    }
    /**封装ajax返回字串信息
     * @param response
     * @param success
     * @param msg
     */
    public static void rendJson(HttpServletResponse response, boolean isSuccess, String msg){  
    	try {
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put("isSuccess", isSuccess);
    		map.put("message", msg);
    		ObjectMapper mapper = new ObjectMapper();
    		String json = mapper.writeValueAsString(map); 
    		rendText(response, json.toString());  
    	} catch (Exception e) {
    		e.printStackTrace();  
    	}  
    }
    
    
    /** @Description: 判断是否ajax请求
	 * @param @param request
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isAjax(HttpServletRequest request) {
		return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest"
				.equals(request.getHeader("X-Requested-With").toString()));
	}
	
	
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}


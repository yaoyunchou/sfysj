package com.nsw.wx.common.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月19日 上午9:35:01
* @Description: 远程从http获取流
 */
public class HttpConnectionUtils {
	
	private static Logger logger = Logger
			.getLogger(HttpConnectionUtils.class);
	
	 public static InputStream getImageFromNetByUrl(String strUrl){  
	        try {  
	            URL url = new URL(strUrl);  
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	            conn.setRequestMethod("GET");  
	            conn.setConnectTimeout(5 * 1000);  
	            conn.setReadTimeout(5 * 1000);
	            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
	            return inStream;  
	        } catch (Exception e) {  
	        	if(e instanceof java.net.SocketTimeoutException){
	        		logger.error("获取网络资源--->" + strUrl +"  连接超时!",e);
	        	}else if(e instanceof java.net.UnknownHostException){
	        		logger.error("未知的目标主机 --->" + strUrl,e);
	        	}else{
	        		e.printStackTrace();
	        	}
	        }  
	        return null;  
	    }

	public static long getImageSizeFromNetByUrl(String strUrl){
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(5 * 1000);
			return conn.getContentLength();
		} catch (Exception e) {
			if(e instanceof java.net.SocketTimeoutException){
				logger.error("获取网络资源--->" + strUrl +"  连接超时!",e);
			}else if(e instanceof java.net.UnknownHostException){
				logger.error("未知的目标主机 --->" + strUrl,e);
			}else{
				e.printStackTrace();
			}
			return 0;
		}
	}

	 
	/* public static void main(String[] args) {
		 InputStream in = getImageFromNetByUrl("http://wx.qlogo.cn/mmopen/LTpwfH82ricn5VWUZGxic3lBSVrhjYJibnPnqG1hiaf17zTtUJaIQZAwuL3ldhNoT9cYUVlibLjdwBOFryfmic1VCgo7gBGn58DCNc/0");
	
		 System.out.println(in);
	 }*/

}

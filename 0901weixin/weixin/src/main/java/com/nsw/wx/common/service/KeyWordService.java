package com.nsw.wx.common.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;


public interface KeyWordService {
	
	/**
	 * 
	* @Description: 关键字回复处理
	* @param @param response
	* @param @param map
	* @param @param appId
	* @param @param type   
	* @return void  
	* @throws
	 */
   void	handTxtEvent( HttpServletResponse response,Map<String, String> map,String appId,int type);
   
   
   /**
    * 
   * @Description: 关注时回复
   * @param @param response
   * @param @param map
   * @param @param appId
   * @param @param type   
   * @return void  
   * @throws
    */
   void attentionEvent( HttpServletResponse response,Map<String, String> map,String appId,int type);
}

package com.nsw.wx.common.service;

import com.nsw.wx.common.docmodel.SysLog;

public interface LogService {
	
	/**
	 * 
	* @Description:保存日志，日志编码通过对象传过来，其他信息从session取得
	* @param @param log   
	* @return void  
	* @throws
	 */
	void saveLog(SysLog log);
	
	
}

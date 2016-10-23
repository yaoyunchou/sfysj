package com.nsw.wx.common.service;


import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.nsw.wx.common.docmodel.ScanQrCodeLog;

public interface ScanQrCodeLogService {

	/**
	 * 添加扫描日志
	 * @param log
	 * @return
	 */
	ScanQrCodeLog  addScanLog(ScanQrCodeLog log);
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	long queryScanLogCount(Query query);
	
	/**
	 * 根据条件查找扫描日志
	 * @param appId
	 * @param date
	 * @param qrcodeId
	 * @return
	 */
	public ScanQrCodeLog getScanLog(String appId,String date,String qrcodeId);
	
	
	
	List<ScanQrCodeLog> getScanLogList(String appId,String date);
	
	
	public boolean updateScanLog(String id,Update update) ;
	
	List<ScanQrCodeLog> getScanLogListByDate(String qrcodeId,String beginDate,String endDate);
	
}

package com.nsw.wx.common.service;

import java.io.InputStream;

public interface FileService {
	
	/**
	 * 根据文件路径获取文件流
	 * @param destUrl
	 * @return
	 */
	 public  InputStream getStreamByUrl(String destUrl);
	 
	 /**
	  * 根据url获取文件类型
	  * @param url
	  * @return
	  */
	 public String insertImageToDb(InputStream inputStream,String fileType);

}

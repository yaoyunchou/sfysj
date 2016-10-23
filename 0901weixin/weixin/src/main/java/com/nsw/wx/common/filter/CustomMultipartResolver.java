package com.nsw.wx.common.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月14日 下午3:46:20
* @Description: 文件等复杂数据接口重写：进度监控注入
*/

public class CustomMultipartResolver extends CommonsMultipartResolver {
	
	final Logger logger = Logger.getLogger(CustomMultipartResolver.class);
	
	/**
	* @Fields progressListener : 此处用New创建bean，因war发布到tomcat时此bean未优先被实例化导致Resoler报错
	*/

	/*
	* @Description: 处理request请求
	* @param request
	* @return
	* @throws MultipartException 
	* @see org.springframework.web.multipart.commons.CommonsMultipartResolver#parseRequest(javax.servlet.http.HttpServletRequest) 
	*/ 
	@Override
	public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		String encoding = determineEncoding(request);
		FileUpload fileUpload = prepareFileUpload(encoding);
		//progressListener.setSession(request.getSession());
		//fileUpload.setProgressListener(progressListener);
		try{
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			return parseFileItems(fileItems, encoding);
		}
		catch (FileUploadBase.SizeLimitExceededException ex){
			logger.error("上传文件失败：文件超出限制大小", ex);
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		}
		catch (FileUploadException ex) {
			logger.error("文件上传失败", ex);
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
	}

} 
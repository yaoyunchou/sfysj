package com.nsw.wx.common.service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.DBObject;

/**
 * 文件处理(文件或图片上传,删除,查询,获取文件属性)
 * @author lzhen
 */
public interface MongoFileOperationService {
    
	
	/***
	* @Description: 保存文件并返回主键信息 
	* @param @param content
	* @param @param fileName
	* @param @param metaData
	* @param @param contentType
	* @param @return   
	* @return GridFSFile  
	* @throws
	 */
	public Map addGridfs(InputStream content, String fileName,
			DBObject metaData, String contentType);
	
	
	/**
	 * @Description: 文件列表 
	 * @param @return   
	 * @return List<Map<String,Object>>  
	 * @throws
	 */
	public List<Map<String, Object>> listFiles(String param, int pageNum, int pageSize);
	
	/**
	* @Description: 满足条件的记录 
	* @param @param param
	* @param @return   
	* @return Long  
	* @throws
	 */
	public Long fileCount(String param);
	
	
	/**
	* @Description: 保存文件
	* @param @param param
	* @param @return   
	* @return Long  
	* @throws
	 */
	public void save(String content, String fileName, DBObject metaData, String contentType);
	
	
	/**
	* @Description: 根据条件删除指定文件（fastdfs redis mongo(Resources)）
	* @param @param param
	* @param @return   
	* @return Long  
	* @throws
	 */
	public void delete(Query query);
	
	
	/**
	* @Description: 根据条件查询文件
	* @param @param param
	* @param @return   
	* @return Long  
	* @throws
	 */
	public Map<String, Object> findOne(Query query);
	
	/**
	* @Description: 根据条件查询文件
	* @param @param param
	* @param @return   
	* @return Long  
	* @throws
	 */
	public List findByQuery(Query query);
	
	/**
	* @Description: 根据fastdfs的fileId返回输入流
	* @param @param param
	* @param @return   
	* @return Long  
	* @throws
	 */
	public BufferedInputStream getBufferedInputStream(String fileId);

	
	/**
	* @Description: 保存文件内容(fastdfs mongo redis)
	* @param @param param
	* @param @return   
	* @return Long  
	* @throws
	 */
	public void saveContent(String content, String fileName, DBObject metaData,String contentType);
	
	
	/**
	* @Description: 根据文件获取文件流
	* @param @param param
	* @param @return   
	* @return Long  
	* @throws
	 */
	public InputStream getFileInputStream(String fileId);

}

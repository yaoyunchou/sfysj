package com.nsw.wx.common.service;

import com.nsw.wx.common.util.QiNiuFileUtils;
import org.csource.manager.FileOperationService;
import org.csource.manager.FileOperationServiceImpl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;
import com.nsw.wx.common.util.DateUtils;

/***
 * fastfs文件操作工具类
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年10月21日 下午5:00:15
 * @Description: TODO
 */
@SuppressWarnings("all")
@Service
public class MongoFileOperationServiceImpl implements MongoFileOperationService {

	
	private Logger logger = Logger.getLogger(MongoFileOperationServiceImpl.class);
	
	final static int BUFFER_SIZE = 40960;
	
	private FileOperationService fileOperationService = new FileOperationServiceImpl();
	
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	QiNiuFileUtils qiNiuFileUtils = new QiNiuFileUtils();

	

	private String prefix = "/nswcms/";
	
	/**
	 * 保存文件并返回信息
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> addGridfs(InputStream content, String fileName,
			DBObject metaData, String contentType) {
		Map result = null;
		Map map = new HashMap();
		map.putAll(metaData == null ? new HashMap() : metaData.toMap());
		map.put("filename", fileName);
		map.put("fileId", fileName);
		map.put("uploadTime", DateUtils.getCurrentTime());
		map.put("contentType", contentType);
		map.put("isDeleted", false);
		Query query = new Query();
		Pattern queryPattern = Pattern.compile("^" + fileName + "$", Pattern.CASE_INSENSITIVE);
		query.addCriteria(Criteria.where("fileId").regex(queryPattern));
		boolean flag = baseMongoTemplate.removeByQuery(query, Constants.T_RESOURCES);
		result = baseMongoTemplate.save(Constants.T_RESOURCES, map);//保存文件信息到基础表的resources
//		if(result != null && !result.isEmpty() && content != null){
//			try {
//				qiNiuFileUtils.upload_covered(inputStreamTOByte(content), fileName);//覆盖上传
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return result;
	}

    /***
    * @Description:  图库查询
    * @param @param buf
    * @param @return   
    * @return InputStream  
    * @throws
     */
	public List<Map<String, Object>> listFiles(String param, int pageNum,
			int pageSize) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(
			//	Criteria.where("projId").is(ContextUtil.getCurrentProject()),
				Criteria.where("site").is(ContextUtil.getSite()),
				Criteria.where("contentType").regex("^image")));
				if (param  != null  &&  !"".equals(param)) {
					Pattern queryPattern = Pattern.compile("^.*" + param + ".*$", Pattern.CASE_INSENSITIVE);
					query.addCriteria(Criteria.where("name").regex(queryPattern));
				}
		query.skip((pageNum - 1) * pageSize).limit(pageSize);
		query.with(new Sort(Direction.DESC , "uploadTime"));
		List<Map<String,Object>> listFile = baseMongoTemplate.queryMulti(query, Constants.T_RESOURCES);
		
		Map map = null;
		if(listFile != null  &&  listFile.size() > 0){
			for(Map file : listFile){
					if (file != null) {
							String filename = file.get("filename")+"";
							map = new HashMap<String, Object>();
							map.put("_id", file.get("_id").toString());
							map.put("url",filename);
							if ((file.get("name")== null || "".equals(file.get("name"))) && filename.contains("/")) {
								filename = filename.substring(filename.lastIndexOf("/") + 1);
								map.put("fileName", filename);
							} else{
								map.put("fileName", file.get("name"));
							}
							list.add(map);
					}
			}
	}
		return list;
		
 }

	/****
	 * @param @return
	 * @throws 内容
	 *  fileName metadata.projId metadata.moduleId metadata.isPublish
	 *  0 根据fileName判断是否存在
	 */
	public Long fileCount(String param) {
		
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(
				//Criteria.where("projId").is(ContextUtil.getCurrentProject()),
				Criteria.where("site").is(ContextUtil.getSite()),
				Criteria.where("contentType").regex("^image")));
			if (param  !=  null  &&  !"".equals(param)) {// 条件不为空
				query.addCriteria(Criteria.where("name").regex("^.*" + param + ".*$"));
			}
		 long  totalItem = baseMongoTemplate.findCountByQuery(query, Constants.T_RESOURCES);
		return totalItem;
	}

	/****
	 * @param @param collection
	 * @param @return
	 * @throws Exception
	 * @throws 内容
	 * fileName metadata.projId metadata.moduleId metadata.isPublish
	 * 0 根据fileName判断是否存在 http://hgw072139.my3w.com/dir/PROJCESHI/
	 */
	public void save(String content, String fileName, DBObject metaData,
			String contentType) {
/*	
		String projId = ContextUtil.getCurrentProject();
		String site = ContextUtil.getSite();
		
		String path = PropertiesUtils.getParametersKey("fileServerPath") + projId + TypeConstant.LEFT_SPRIT;
		InputStream stream = null;
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					content.getBytes());
			stream = HtmlCleanerUtil.updateBaseByStream(byteArrayInputStream,
					path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("projId").is(projId).and("filename").is(fileName).and("site").is(site));
		delete(query);
		metaData.put("site", site);
		addGridfs(stream, fileName, metaData, contentType);*/
	}

    /***
    * @Description: 保存文件内容 
    * @param @param buf
    * @param @return   
    * @return InputStream  
    * @throws
     */
	public void saveContent(String content, String fileName, DBObject metaData, String contentType) {
		
		//String projId = ContextUtil.getCurrentProject();
		String site = ContextUtil.getSite();
		
		Query query = new Query();
		query.addCriteria(Criteria.where("filename").is(fileName).and("site").is(site));
		delete(query);
		InputStream in = IOUtils.toInputStream(content);
		metaData.put("site", site);
		metaData.put("publishDate", "");
		if(in != null){
			addGridfs(in, fileName, metaData, contentType);
		}
	}

	/****
	 * @param @param collection
	 * @param @return
	 * @throws Query条件删除
	 */
	public void delete(Query query){
		
	/*	Map<String, Object> map = baseMongoTemplate.findOneByQuery(query, Constants.T_RESOURCES);
		if(map != null  &&  !map.isEmpty()){
				try {
				  boolean delete_flag =	fileOperationService.deleteFile(map.get("fileId")+"");//删除fastdfs文件
				  boolean remove_flag = baseMongoTemplate.removeByQuery(query, Constants.T_RESOURCES);//删除mongo数据文件
					if(delete_flag && remove_flag){
						redisTemplate.delete(prefix + map.get("filename")+"");//删除redis文件
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}*/
	}
	
	/****
	 * @param @param collection
	 * @param @return
	 * @throws Query条件查找一个
	 */
	public Map<String, Object> findOne(Query query) {
		
//		Map<String, Object> map = baseMongoTemplate.findOneByQuery(query, Constants.T_RESOURCES);
//		
//		return map;
		
		return null;
	}
	
	/****
	 * @param @param collection
	 * @param @return
	 * @throws Query条件查找列表
	 */
	public List findByQuery(Query query) {
		
		List<Map<String,Object>> list = baseMongoTemplate.queryMulti(query, Constants.T_RESOURCES);
		return list;
	}
	
	
    /***
     * byte转为inputstream
    * @Description: TODO 
    * @param @param buf
    * @param @return   
    * @return InputStream  
    * @throws
     */
	public InputStream byteToInputStream(byte[] buf) {
		
		InputStream input = null;
		if (buf != null) {
			input = new ByteArrayInputStream(buf);
		}
		return input;
	}
	
	/***
	 * @Description:根据fileId获取文件流
	 * @param @param in
	 * @param @return
	 * @param @throws IOException   
	 * @return BufferedInputStream
	 * @throws
	 */
	public InputStream getFileInputStream(String fileId) {
		
		InputStream input = null;
		byte[] buf = null;
		try {
			buf = fileOperationService.downloadFile(fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (buf != null) {
			input = new ByteArrayInputStream(buf);
		}
		return input;
	}
	
	/***
	 * @Description:根据fileId获取输入流
	 * @param @param in
	 * @param @return
	 * @param @throws IOException   
	 * @return BufferedInputStream
	 * @throws
	 */
	public BufferedInputStream getBufferedInputStream(String fileId) {
		
		BufferedInputStream input = null;
		byte[] buf = null;
		try {
			buf = fileOperationService.downloadFile(fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (buf != null) {
			input = new BufferedInputStream(new ByteArrayInputStream(buf), BUFFER_SIZE);
		}
		return input;
	}
	
	/***
	 * @Description: InputStream转为byte数组 
	 * @param @param in
	 * @param @return
	 * @param @throws IOException   
	 * @return byte[]  
	 * @throws
	 */
	 public  byte[] inputStreamTOByte(InputStream in) throws IOException{
         
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	        byte[] data = new byte[BUFFER_SIZE];
	        int count = 0;
	        while((count = in.read(data, 0, 100)) > 0){
	            outStream.write(data, 0, count);
	        }
	        byte datas[] = outStream.toByteArray();
	        outStream.close();
	        return datas;
	    }
	
}

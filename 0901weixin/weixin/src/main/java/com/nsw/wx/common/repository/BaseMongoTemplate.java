package com.nsw.wx.common.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/***
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月23日 下午3:53:31
 * @Description: Mongo通用数据访问层接口
 */
public interface BaseMongoTemplate {
	
	
	/*public Map<String, Object> getSite(String collectionName);*/

	/**
	 * @Description: 保存
	 * @param @param collectionName
	 * @param @param obj
	 * @param @param key
	 * @return void
	 * @throws
	 */
	public void saveData(String collectionName, BasicDBObject obj, String key);

	/****
	 * @Description: 保存并返回带主键的Map集合
	 * @param @param collectionName
	 * @param @param obj
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> save(String collectionName, BasicDBObject obj);

	/**
	 * @Description: 保存并返回带主键的Map集合
	 * @param @param collectionName
	 * @param @param map
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> save(String collectionName,
			Map<String, Object> map);

	/**
	 * @Description: 保存list集合，并返回带主键的list集合
	 * @param @param collectionName
	 * @param @param dbList
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> saveDataList(String collectionName,
			List<Map<String, Object>> dbList);

	/**
	 * @Description: 保存json串
	 * @param @param collectionName
	 * @param @param jsonStr
	 * @return void
	 * @throws
	 */
	public void saveJsonStr(String collectionName, String jsonStr);

	/**
	 * @Description: 根据条件查询，取第一个
	 * @param @param collectionName
	 * @param @param map
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> findOne(String collectionName,
			Map<String, Object> map);

	/**
	 * @Description: 根据ID查询
	 * @param @param collectionName
	 * @param @param id
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getById(String collectionName, String id);

	/**
	 * @Description: 条件查询多条
	 * @param @param collectionName
	 * @param @param param
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> queryMulti(String collectionName,
			Map<String, Object> param);

	/**
	 * 移除满足条件的记录
	 * @Description: TODO
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean removeByQuery(Query query, String collection) ;



	/**
	 * @Description: 去除重复
	 * @param @param collection
	 * @param @param key
	 * @param @param query
	 * @param @return
	 * @return List
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public List distinct(String collection, String key, DBObject query);

	/**
	 * @Description: TODO
	 * @param @param collection
	 * @param @param key
	 * @param @param query
	 * @param @param cursorHandle
	 * @return void
	 * @throws
	 */
	public void distinctWithHandle(String collection, String key,
			DBObject query, CursorHandle cursorHandle);

	/**
	 * 
	 * @Description: TODO
	 * @param @param collection
	 * @param @param key
	 * @param @param query
	 * @param @param pageNo
	 * @param @param pageSize
	 * @param @param cursorHandle
	 * @return void
	 * @throws
	 */
	public void distinctWithHandle(String collection, String key,
			DBObject query, int pageNo, int pageSize, CursorHandle cursorHandle);

	/**
	 * @Description: 条件查询一条
	 * @param @param collection
	 * @param @param query
	 * @param @param fields
	 * @param @return
	 * @return DBObject
	 * @throws
	 */
	public DBObject findOne(String collection, DBObject query, DBObject fields);

	/**
	 * @Description: 分页查询
	 * @param @param collection
	 * @param @param query
	 * @param @param fields
	 * @param @param orderBy
	 * @param @param pageNum
	 * @param @param pageSize
	 * @param @return
	 * @return List<DBObject>
	 * @throws
	 */
	public List<DBObject> find(String collection, DBObject query,
			DBObject fields, DBObject orderBy, int pageNum, int pageSize);


	/**
	 * @Description: 更新
	 * @param @param collectionName
	 * @param @param query
	 * @param @param update
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean update(String collectionName, Map<String, Object> query,
			Map<String, Object> update);


	/***
	 * @Description: 删除
	 * @param @param collectionName
	 * @param @param obj
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean deleteData(String collectionName, Map<String, Object> obj);

	/**
	 * @Description: 批量删除
	 * @param @param collection
	 * @param @param list
	 * @return void
	 * @throws
	 */
	public boolean deleteBatch(String collection, List<Map<String, Object>> list);
	


	/**
	 * @Description: Map转为DBObject
	 * @param @param map
	 * @param @return
	 * @return DBObject
	 * @throws
	 */
	public DBObject map2Object(Map<String, Object> map);

	/**
	 * 
	* @Description: TODO 
	* @param @param collection
	* @param @param query
	* @param @param fields
	* @param @param orderBy
	* @param @param pageNum
	* @param @param pageSize
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws
	 */
	public List<Map<String, Object>> find(String collection,
			Map<String, Object> query, Map<String, Object> fields,
			Map<String, Object> orderBy, int pageNum, int pageSize);

	/**
	 * 
	* @Description: TODO 
	* @param @param collectionName
	* @param @param query
	* @param @return   
	* @return long  
	* @throws
	 */
	public long count(String collectionName, Map<String, Object> query);

	/**
	 * @return
	 * @Description: 数据拷贝
	 * @param @param sourceId
	 * @param @param sourceCollectionName
	 * @param @param destCollectionName
	 * @param @param addProperties
	 * @param @param ignoreProperties
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	public Map<String, Object> copyObject(String sourceId,
			String sourceCollectionName, String destCollectionName,
			Map<String, Object> addProperties, String... ignoreProperties)
			throws Exception;

	/**
	 * 
	* @Description: TODO 
	* @param @param collection
	* @param @param query
	* @param @param fields
	* @param @return   
	* @return DBObject  
	* @throws
	 */
	public DBObject findOne(String collection, Map<String, Object> query,
			Map<String, Object> fields);

	/***
	 * 
	* @Description: TODO 
	* @param @param collectionName
	* @param @param id
	* @param @param fields
	* @param @return   
	* @return Map<String,Object>  
	* @throws
	 */
	public Map<String, Object> getById(String collectionName, String id,
			Map<String, Object> fields);

	/**
	 * @return *
	 * @Description: 数据拷贝
	 * @param @param sourceCollectionName
	 * @param @param query
	 * @param @param fields
	 * @param @param destCollectionName
	 * @param @param addProperties
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	public Map<String, Object> copyObject(String sourceCollectionName,
			Map<String, Object> query, Map<String, Object> fields,
			String destCollectionName, Map<String, Object> addProperties)
			throws Exception;



	/**
	 * 
	* @Description: TODO 
	* @param @param collectionName
	* @param @param map
	* @param @param orderBy
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws
	 */
	public List<Map<String, Object>> queryMultiAndOrder(String collectionName,
			Map<String, Object> map, String orderBy);
 
	/**
	 * 批量修改
	* @Description: TODO 
	* @param @param collectionName
	* @param @param query
	* @param @param update
	* @param @return   
	* @return boolean  
	* @throws
	 */
	public boolean updateMulti(String collectionName, Map<String, Object> query,
			Map<String, Object> update);
 
	/***
	 * 查询多条并指定显示字段
	* @Description: TODO 
	* @param @param collectionName
	* @param @param param
	* @param @param fields
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws
	 */
	public List<Map<String, Object>> queryMulti(String collectionName,
			Map<String, Object> param, Map<String, Object> fields);

	/**
	 * 更新满足条件的第一条记录
	* @Description: TODO 
	* @param @param query
	* @param @param update
	* @param @param collection   
	* @return void  
	* @throws
	 */
	public boolean updateFirst(Query query, Update update,
			String collection);

	/**
	 * 查询并排序
	* @Description: TODO 
	* @param @param string
	* @param @param condition
	* @param @param fields
	* @param @param orderBy
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws
	 */
	public List<Map<String, Object>> queryMulti(String string,
			Map<String, Object> condition, Map<String, Object> fields,
			Map<String, Object> orderBy);
	
	/**
	 * 查询分页
	* @Description: TODO 
	* @param @param string
	* @param @param condition
	* @param @param fields
	* @param @param orderBy
	* @param @param limit
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws
	 */
	public List<Map<String, Object>> queryMulti(String string,
			Map<String, Object> condition, Map<String, Object> fields,
			Map<String, Object> orderBy,int limit);
	
	
	
	/****
	 * 根据固定的json条件查询
	 * @param @param collection
	 * @param @return 
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> query(String collectionName,
			String queryJson,String orderBy,String pageIndex,String size);
	
	
	
	
	/***
	 * 根据query查询列表
	 * 
	 * @Description: TODO
	 * @param @param query
	 * @param @param collection
	 * @param @return
	 * @return List<HashMap>
	 * @throws
	 */
	public List<HashMap> findByQuery(Query query, String collection);

	/**
	 * 根据query查询记录数
	 * 
	 * @Description: TODO
	 * @param @param query
	 * @param @param collection
	 * @param @return
	 * @return long
	 * @throws
	 */
	public long findCountByQuery(Query query, String collection);

	/**
	 * 移除满足条件的记录
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean removeByqyery(Query query, String collection) ;

	/**
	 * 查询query满足的记录，批量update更新
	 * @Description: TODO
	 * @param @param query
	 * @param @param update
	 * @param @param collectionName
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean update(Query query, Update update, String collectionName);

	/***
	 * 查询query满足的记录，批量update更新第一个
	* @Description: TODO 
	* @param @param query
	* @param @param update
	* @param @param collectionName
	* @param @return   
	* @return boolean  
	* @throws
	 */
	public boolean updateOne(Query query, Update update, String collectionName);

	
	
	/***
	 * 查询query满足的记录，批量update更新第一个
	* @Description: TODO 
	* @param @param query
	* @param @param update
	* @param @param collectionName
	* @param @return   
	* @return boolean  
	* @throws
	 */
	public List<Map<String,Object>> queryMulti(Query query, String collection);
}

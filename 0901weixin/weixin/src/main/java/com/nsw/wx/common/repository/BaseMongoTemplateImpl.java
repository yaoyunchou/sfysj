package com.nsw.wx.common.repository;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import ch.qos.logback.core.Context;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.*;
import com.mongodb.util.JSON;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015骞�鏈�鏃�涓嬪崍12:32:25
 * @Description:mongoDB鍩虹API閫氱敤妯℃澘
 */
@SuppressWarnings("all")
@Repository
public class BaseMongoTemplateImpl implements BaseMongoTemplate {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	private static int MAX_SKIP = 1;

	private static int MAX_LIMIT = 10000;

	private static Logger logger = Logger
			.getLogger(BaseMongoTemplateImpl.class);

	/*String[] filterCollection = new String[] { Constants.PRODUCT,
			Constants.INFO, Constants.T_INFO_ARTICLE, Constants.T_INFO_CTG,
			Constants.T_PROJECT, Constants.T_PROJ_PAGE_TPL,
			Constants.T_PROJ_BLK_TPL, Constants.T_PROJ_PAGE_TPL_PACK,
			Constants.T_PRODUCT, Constants.T_PRODUCT_CTG,
			Constants.T_PROJ_MOUDUEL, Constants.T_PUB_RECYLEBIN,
			Constants.T_PROJ_TAG, Constants.T_PROJ_TAG_Ctg, Constants.EXTEND,
			Constants.PROD, Constants.OTHERS,Constants.T_PROJ_FORM};*/

	/**
	 * 获取当前的登录用户站点进行过滤
	 * @Description: TODO
	 * @param @return
	 * @return String
	 * @throws
	 */
	/*public Map<String, Object> getSite(String collectionName) {
		Map<String, Object> site = new HashMap<String, Object>();
		if (Arrays.asList(filterCollection).contains(collectionName)) {
			site.put("site", ContextUtil.getSite());
		}
		return site;
	}*/

	/**
	 * 淇濆瓨鏁版嵁
	 * 
	 * @Description: TODO
	 * @param @param collection
	 * @param @param query
	 * @param @param fields
	 * @param @return
	 * @return DBObject
	 * @throws
	 */
	public void saveData(String collectionName, BasicDBObject obj, String key) {
		//obj.putAll(getSite(collectionName));
		DBCollection collection = mongoTemplate.getCollection(collectionName);
		BasicDBObject doc = new BasicDBObject();
		doc.put(key, obj);
		collection.insert(doc);
		collection.setWriteConcern(WriteConcern.SAFE);
	}

	/**
	 * SAFE 淇濆瓨鏁版嵁
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param obj
	 * @throws
	 */
	public Map<String, Object> save(String collectionName, BasicDBObject obj) {
		/*obj.putAll(getSite(collectionName));
		obj.putAll(getSite(collectionName));*/
		WriteResult wr = mongoTemplate.getCollection(collectionName)
				.insert(obj);
		logger.info("the id is: " + obj.get("_id"));
		return obj.toMap();
	}

	/**
	 * 淇濆瓨鏁版嵁
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public Map<String, Object> save(String collectionName,
			Map<String, Object> map) {
		/*if (map.get("site") == null || "".equals(map.get("site"))) {
			map.putAll(getSite(collectionName));
		}*/
		DBObject dbObject = map2Object(map);
		WriteResult wr = mongoTemplate.getCollection(collectionName).save(
				dbObject);
		logger.info("the id is: " + dbObject.get("_id"));
		dbObject.put("_id", dbObject.get("_id").toString());
		return dbObject.toMap();
	}

	/**
	 * 鎵归噺淇濆瓨鏁版嵁
	 * 1）insert()保存记录时，要插入的文档的_id值必须在集合中是独一无二的（如果使用MongoDB默认的_id是不会出现重复的，
	 * 但是，如果使用自定的_id就需要考虑_id值重复的问题了
	 * ），否则就会出现com.mongodb.MongoException$DuplicateKey错误
	 * 2）save()保存记录时，如果集合中存在和要插入的文档相同的_id值的文档记录，那么就会执行更新操作，如果不存在，就执行插入操作。
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public List<Map<String, Object>> saveDataList(String collectionName,
			List<Map<String, Object>> dbList) {
		List returnList = new ArrayList<>();
		for (Map<String, Object> map : dbList) {
			/*if(map != null && map.get("site") == null){
				map.putAll(getSite(collectionName));
			}*/
			logger.info("map:===========================:" + map);
			Map returnValue = save(collectionName, map);
			returnList.add(returnValue);
		}
		logger.info(returnList);
		return returnList;
	}

	/**
	 * 鎵归噺JSON鏁版嵁
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public void saveJsonStr(String collectionName, String jsonStr) {
		DBObject dbObject = (DBObject) JSON.parse(jsonStr);
		/*dbObject.putAll(getSite(collectionName));*/
		mongoTemplate.getCollection(collectionName).insert(dbObject);
	}

	/**
	 * 鏌ヨ涓�潯鏁版嵁
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public Map<String, Object> findOne(String collectionName,
			Map<String, Object> map) {
		/*if (map.get("site") == null || "".equals(map.get("site"))) {
			map.putAll(getSite(collectionName));
		}*/
		DBObject doc = mongoTemplate.getCollection(collectionName).findOne(
				map2Object(map));
		if (doc != null) {
			Map<String, Object> returnMap = (Map<String, Object>) doc.toMap();
			returnMap.put("_id", returnMap.get("_id").toString());
			return returnMap;
		}
		return null;
	}

	/**
	 * 鏍规嵁ID鏌ユ壘
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public Map<String, Object> getById(String collectionName, String id) {
		DBCollection dbCol = mongoTemplate.getCollection(collectionName);
		DBObject obj = new BasicDBObject();
		if (ObjectId.isValid(id)) {
			obj.put("_id", new ObjectId(id));
		}
		DBObject object = dbCol.findOne(obj);
		if (object != null) {
			object.put("_id", object.get("_id").toString());
			return object.toMap();
		}
		return new HashMap<>();
	}

	@Override
	public boolean removeByQuery(Query query, String collection) {
		return deleteData(collection, query.getQueryObject().toMap());
	}

	/**
	 * 澶氭潯浠舵煡璇�
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public List<Map<String, Object>> queryMulti(String collectionName, Map param) {
		/*param.putAll(getSite(collectionName));*/
		if (param.get("_id") != null) {
			if ((param.get("_id") instanceof String)) {
				param.put("_id", new ObjectId(param.get("_id") + ""));
			}
		}
		DBCursor cursor = mongoTemplate.getCollection(collectionName).find(
				map2Object(param));
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				Map<String, Object> map = object.toMap();
				map.put("_id", map.get("_id").toString());
				list.add(map);
			}
		} finally {
			cursor.close();
		}
		return list;
	}

	/**
	 * 鏌ヨ鎵�湁
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public List<Map<String, Object>> readAllData(String collectionName) {
		DBCollection collection = mongoTemplate.getCollection(collectionName);
		DBCursor cursor = collection.find().sort(new BasicDBObject("_id", 1));
		List<Map<String, Object>> list = new ArrayList<>();
		while (cursor.hasNext()) {
			BasicDBObject bdbObj = (BasicDBObject) cursor.next();
			Map<String, Object> map = bdbObj.toMap();
			map.put("_id", map.get("_id").toString());
			list.add(map);
		}
		return list;
	}

	/**
	 * 鏍规嵁鏉′欢鏌ヨ骞跺幓閲�
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public List distinct(String collection, String key, DBObject query) {
		return mongoTemplate.getCollection(collection).distinct(key, query);
	}

	/**
	 * 灏忔暟鎹噺鏌ヨ骞跺幓閲�
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public void distinctWithHandle(String collection, String key,
			DBObject query, CursorHandle cursorHandle) {
		List<DBObject> pipeLine = new ArrayList<>();
		pipeLine.add(new BasicDBObject("$match", query));
		String groupStr = String.format("{$group:{_id:'$%s'}}", key);
		pipeLine.add((DBObject) JSON.parse(groupStr));
		Cursor cursor = mongoTemplate.getCollection(collection).aggregate(
				pipeLine, AggregationOptions.builder().build());
		cursorHandle.handle(cursor);
	}

	/**
	 * 澶ф暟鎹噺鏌ヨ骞跺垎椤�
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public void distinctWithHandle(String collection, String key,
			DBObject query, int pageNo, int pageSize, CursorHandle cursorHandle) {
		List<DBObject> pipeLine = new ArrayList<>();
		pipeLine.add(new BasicDBObject("$match", query));
		String groupStr = String.format("{$group:{_id:'$%s'}}", key);
		pipeLine.add((DBObject) JSON.parse(groupStr));
		pipeLine.add(new BasicDBObject("$skip", (pageNo - 1) * pageSize));
		pipeLine.add(new BasicDBObject("$limit", pageSize));
		Cursor cursor = mongoTemplate.getCollection(collection).aggregate(
				pipeLine, AggregationOptions.builder().build());
		cursorHandle.handle(cursor);
	}

	/**
	 * 鏌ユ壘涓�潯璁板綍
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public DBObject findOne(String collection, DBObject query, DBObject fields) {
		/*query.putAll(getSite(collection));*/
		return mongoTemplate.getCollection(collection).findOne(query, fields);
	}

	/**
	 * 鍒嗛〉鏌ヨ
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public List<DBObject> find(String collection, DBObject query,
			DBObject fields, DBObject orderBy, int pageNum, int pageSize) {
		/*query.putAll(getSite(collection));*/
		List<DBObject> list = new ArrayList<>();
		Cursor cursor = mongoTemplate.getCollection(collection)
				.find(query, fields).skip((pageNum - 1) * pageSize)
				.limit(pageSize).sort(orderBy);
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list.size() > 0 ? list : null;
	}

	/**
	 * 鏇存柊鏁版嵁
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public boolean update(String collectionName, Map<String, Object> query,
			Map<String, Object> update) {
		DBCollection table = mongoTemplate.getCollection(collectionName);
		DBObject queryObj = map2Object(query);
		BasicDBObject updateObj = new BasicDBObject("$set", update);
		WriteResult ws = table.update(queryObj, updateObj);
		if (ws.isUpdateOfExisting()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 鍒犻櫎婊¤冻鏉′欢鐨勬暟鎹�
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public boolean deleteData(String collectionName, Map map) {
		DBCollection collection = mongoTemplate.getCollection(collectionName);
		DBObject deleteObj = map2Object(map);
		WriteResult ws = collection.remove(deleteObj);
		if (ws.getN() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 鎵归噺鍒犻櫎婊¤冻鏉′欢鐨勬暟鎹�
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @param @param map
	 * @throws
	 */
	public boolean deleteBatch(String collection, List<Map<String, Object>> list) {
		if (list == null || list.isEmpty()) {
			return false;
		}
		boolean flag = true;
		for (int i = 0; i < list.size(); i++) {
		   try {
			flag = deleteData(collection,list.get(i));
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		}
		return flag;
	}

	/**
	 * 鍒犻櫎鍏ㄩ儴(鎱庣敤)
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @throws
	 */
	public void deleteAll(String collection) {
		List<Map<String, Object>> rs = readAllData(collection);
		if (rs != null && !rs.isEmpty()) {
			for (int i = 0; i < rs.size(); i++) {
				mongoTemplate.getCollection(collection).remove(
						map2Object(rs.get(i)));
			}
		}
	}

	/**
	 * Map杞崲涓篋BObject
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @throws
	 */
	public DBObject map2Object(Map<String, Object> map) {
		if (map == null) {
			map = new HashMap<>();
		}
		if (map.get("_id") != null) {
			if (!(map.get("_id") instanceof ObjectId)) {
			}
			if ((map.get("_id") instanceof String)) {
				if (ObjectId.isValid(map.get("_id").toString())) {
					map.put("_id", new ObjectId(map.get("_id") + ""));
				}
			}
		}
		DBObject dbObject = new BasicDBObject();
		Iterator<Entry<String, Object>> iterable = map.entrySet().iterator();
		while (iterable.hasNext()) {
			Entry<String, Object> entry = iterable.next();
			Object value = entry.getValue();
			String key = entry.getKey();
			if (key.startsWith("$") && value instanceof Map) {
				BasicBSONList basicBSONList = new BasicBSONList();
				Map<String, Object> conditionsMap = ((Map) value);
				Set<String> keys = conditionsMap.keySet();
				for (String k : keys) {
					Object conditionsValue = conditionsMap.get(k);
					if (conditionsValue instanceof Collection) {
						conditionsValue = convertArray(conditionsValue);
					}
					DBObject dbObject2 = new BasicDBObject(k, conditionsValue);
					basicBSONList.add(dbObject2);
				}
				value = basicBSONList;
			} else if (value instanceof Collection) {
				value = convertArray(value);
			} else if (!key.startsWith("$") && value instanceof Map) {
				value = map2Object(((Map) value));
			}
			dbObject.put(key, value);
		}
		return dbObject;
	}

	private static Object[] convertArray(Object value) {
		Object[] values = ((Collection) value).toArray();
		return values;
	}

	/**
	 * @Description: 条件查询并分页 排序
	 * @param @param collection
	 * @throws
	 */
	@Override
	public List<Map<String, Object>> find(String collection,
			Map<String, Object> query, Map<String, Object> fields,
			Map<String, Object> orderBy, int pageNum, int pageSize) {
		/*if (query.get("site") == null || "".equals(query.get("site"))) {
			query.putAll(getSite(collection));
		}*/
		List<Map<String, Object>> list = new ArrayList<>();
		Cursor cursor = mongoTemplate.getCollection(collection)
				.find(map2Object(query), map2Object(fields))
				.skip((pageNum - 1) * pageSize).limit(pageSize)
				.sort(map2Object(orderBy));// 从第skip条记录起取limit条记录为
		while (cursor.hasNext()) {
			BasicDBObject bdbObj = (BasicDBObject) cursor.next();
			Map<String, Object> map = bdbObj.toMap();
			map.put("_id", map.get("_id").toString());// 为了不影响ID序列化格式问题，这里取出来转换为string
			list.add(map);
		}
		logger.info("param : " + query);
		//logger.info("result: " + list);
		return list.size() > 0 ? list : null;
	}

	/**
	 * 满足条件的记录总数
	 * 
	 * @Description: TODO
	 * @param @param collectionName
	 * @throws
	 */
	public long count(String collectionName, Map<String, Object> query) {
		/*if(query != null && query.get("site")==null){
			query.putAll(getSite(collectionName));
		}*/
		long count = mongoTemplate.getCollection(collectionName).count(
				map2Object(query));
		logger.info("total count: " + count);
		return count;
	}

	/**
	 * @throws Exception
	 * @throws Exception
	 * @Description: 数据拷贝
	 * @param @param sourceId源对象ID,sourceCollectionName源对象集合名
	 *        destCollectionName目标集合名
	 * @param addProperties需要新增的属性
	 *            ,ignoreProperties忽略的属性
	 * @throws
	 */
	public Map<String, Object> copyObject(String sourceId,
			String sourceCollectionName, String destCollectionName,
			Map<String, Object> addProperties, String... ignoreProperties)
			throws Exception {
		DBObject obj = new BasicDBObject();
		if (ObjectId.isValid(sourceId)) {
			obj.put("_id", new ObjectId(sourceId));
		} else {
			logger.info("ObjectId " + sourceId + " is invalid");
		}
		DBObject ingoreFileds = new BasicDBObject();
		if (ignoreProperties != null && ignoreProperties.length > 0) {
			for (String ignore : ignoreProperties) {
				ingoreFileds.put(ignore, false);
			}
		}
		logger.info("==========ignoreProperties==========" + ingoreFileds);
		DBObject object = mongoTemplate.getCollection(sourceCollectionName)
				.findOne(obj, ingoreFileds);
		if (object != null) {
			if (!mongoTemplate.collectionExists(destCollectionName)) {// 不存在
				logger.info("======create a collection: " + destCollectionName);
			}
			Map map = object.toMap();
			map.putAll(addProperties == null ? new HashMap<>() : addProperties);
			Map<String, Object> returnMap = save(destCollectionName, map);
			logger.info("==========copy successfully=========="
					+ object.toMap());
			return returnMap;
		} else {
			// throw new
			// Exception("目标对象不能为空！请检查sourceId和sourceCollectionName是否正确");
			logger.info("查找到的对象为空！");
			return new HashMap();
		}

	}

	/**
	 * @throws Exception
	 * @throws Exception
	 * @Description: 条件查询一条记录 可以控制需要查询的字段
	 * @throws
	 */
	public DBObject findOne(String collection, Map query, Map fields) {
		/*query.putAll(getSite(collection));*/
		return mongoTemplate.getCollection(collection).findOne(
				map2Object(query), map2Object(fields));
	}

	public Map<String, Object> getById(String collectionName, String id,
			Map fields) {
		Map<String, Object> query = new HashMap<String, Object>();
		if (ObjectId.isValid(id)) {
			query.put("_id", new ObjectId(id));
		}
		DBObject object = findOne(collectionName, query, fields);
		if (object != null) {
			return object.toMap();
		}
		return null;
	}

	/**
	 * @throws Exception
	 * @throws Exception
	 * @Description: 数据拷贝
	 * @param @param sourceCollectionName源集合名称,query源集合查询条件,fields忽略或需要显示的属性
	 * @param destCollectionName目标集合名称
	 *            ,addProperties拷贝新增的属性
	 * @throws
	 */
	public Map<String, Object> copyObject(String sourceCollectionName,
			Map<String, Object> query, Map<String, Object> fields,
			String destCollectionName, Map<String, Object> addProperties)
			throws Exception {
		DBObject object = findOne(sourceCollectionName, query, fields);
		if (object != null) {
			if (!mongoTemplate.collectionExists(destCollectionName)) {// 不存在
				logger.info("======create a collection: " + destCollectionName);
			}
			Map map = object.toMap();
			map.putAll(addProperties == null ? new HashMap<String, Object>()
					: addProperties);
			Map<String, Object> returnMap = save(destCollectionName, map);
			logger.info("==========copy successfully=========="
					+ object.toMap());
			return returnMap;
		} else {
			// throw new
			// Exception("目标对象不能为空！请检查sourceId和sourceCollectionName是否正确");
			return new HashMap<>();
		}
	}





	/****
	 * @Description: TODO
	 * @param @param collection
	 * @param @param query
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> find(String collection, Map query) {
		/*query.putAll(getSite(collection));*/
		List<Map<String, Object>> list = new LinkedList<>();
		Cursor cursor = mongoTemplate.getCollection(collection).find(
				map2Object(query));
		while (cursor.hasNext()) {
			Map returnMap = cursor.next().toMap();
			returnMap.put("_id", returnMap.get("_id").toString());
			list.add(returnMap);
		}
		return list.size() > 0 ? list : null;
	}

	/**
	 * @Description: 条件查询并排序
	 * @param @param collection
	 * @param @param query
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> queryMultiAndOrder(String collectionName,
			Map<String, Object> param, String orderBy) {
		if(param == null){
			param = new HashMap<String, Object>();
		}
		/*param.putAll(getSite(collectionName));*/
		DBObject orderPath = new BasicDBObject(orderBy, 1);
		DBCursor cursor = mongoTemplate.getCollection(collectionName)
				.find(map2Object(param)).sort(orderPath);
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				Map<String, Object> map = object.toMap();
				map.put("_id", map.get("_id").toString());
				list.add(map);
			}
		} finally {
			cursor.close();
		}
		return list;
	}

	@Override
	public boolean updateMulti(String collectionName,
			Map<String, Object> query, Map<String, Object> update) {
		DBCollection table = mongoTemplate.getCollection(collectionName);
		DBObject queryObj = map2Object(query);
		BasicDBObject updateObj = new BasicDBObject("$set", update);
		WriteResult ws = table.updateMulti(queryObj, updateObj);
		if (ws.isUpdateOfExisting()) {
			return true;
		} else {
			return false;
		}
	}

	/****
	 * @Description: 条件查询并制定显示字段
	 * @param @param collection
	 * @param @param query
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	@Override
	public List<Map<String, Object>> queryMulti(String collectionName,
			Map<String, Object> param, Map<String, Object> fields) {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		/*param.putAll(getSite(collectionName));*/
		DBCursor cursor = mongoTemplate.getCollection(collectionName).find(
				map2Object(param), map2Object(fields));
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				Map<String, Object> map = object.toMap();
				map.put("_id", map.get("_id").toString());
				list.add(map);
			}
		} finally {
			cursor.close();
		}
		return list;
	}

	/****
	 * @Description: 更新满足的第一条记录
	 * @param @param collection
	 * @param @param query
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	@Override
	public boolean updateFirst(Query query, Update update, String collectionName) {
	WriteResult ws = mongoTemplate.updateFirst(query, update, collectionName);
	if (ws.isUpdateOfExisting()) {
		return true;
	} else {
		return false;
	}
	}

	/****
	 * @Description:查询排序
	 * @param @param collection
	 * @param @param query
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	@Override
	public List<Map<String, Object>> queryMulti(String collectionName,
			Map<String, Object> condition, Map<String, Object> fields,
			Map<String, Object> orderBy) {
		/*condition.putAll(getSite(collectionName));*/
		DBObject orderPath = new BasicDBObject("_id", 1);
		DBCursor cursor = mongoTemplate.getCollection(collectionName)
				.find(map2Object(condition), map2Object(fields))
				.sort(map2Object(orderBy));
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				Map<String, Object> map = object.toMap();
				map.put("_id", map.get("_id").toString());
				list.add(map);
			}
		} finally {
			cursor.close();
		}
		return list;
	}

	/****
	 * @Description:查询分页
	 * @param @param collection
	 * @param @param query
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	@Override
	public List<Map<String, Object>> queryMulti(String collectionName,
			Map<String, Object> condition, Map<String, Object> fields,
			Map<String, Object> orderBy, int limit) {
		/*condition.putAll(getSite(collectionName));*/
		DBObject orderPath = new BasicDBObject("_id", 1);
		DBCursor cursor = mongoTemplate.getCollection(collectionName)
				.find(map2Object(condition), map2Object(fields))
				.sort(map2Object(orderBy)).limit(limit);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				Map<String, Object> map = object.toMap();
				map.put("_id", map.get("_id").toString());
				list.add(map);
			}
		} finally {
			cursor.close();
		}
		return list;
	}

	/****
	 * @Description: 条件查询并制定显示字段
	 * @param @param collection
	 * @param @param queryJson {'name':'mkyong', 'age':30}
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> query(String collectionName,
			String queryJson, String orderBy, String pageIndex, String size) {
		DBObject param = null;
		DBObject order = null;
		int pageNum = 0;
		int pageSize = 0;
		try {
			param = (DBObject) JSON.parse(queryJson);
			order = (DBObject) JSON.parse(orderBy);
			pageNum = Integer.parseInt(pageIndex);
			pageSize = Integer.parseInt(size);
			if (param.get("_id") != null && !"".equals(param.get("_id") + "")) {
				param.put("_id", new ObjectId(param.get("_id") + ""));
			}
		} catch (Exception e) {
			logger.info("data conversion exception");
			e.printStackTrace();
		}
		DBCursor cursor = null;
		try {
			cursor = mongoTemplate.getCollection(collectionName).find(param)
					.skip((pageNum - 1) * pageSize).limit(pageSize).sort(order);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Map<String, Object>> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				Map<String, Object> map = object.toMap();
				map.put("_id", map.get("_id").toString());
				list.add(map);

			}
			logger.info("Size: " + list == null ? 0 : list.size());
		} finally {
			cursor.close();
		}
		return list;
	}
	
	

	/***
	 * 根据query查询列表
	 * @Description: TODO
	 * @param @param query
	 * @param @param collection
	 * @param @return
	 * @return List<HashMap>
	 * @throws
	 */
	public List<HashMap> findByQuery(Query query, String collection) {
		List<HashMap> listMap = mongoTemplate.find(query, HashMap.class,
				collection);
		return listMap;
	}

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
	public long findCountByQuery(Query query, String collection) {
		long count = mongoTemplate.count(query, collection);
		return count;
	}

	/**
	 * 移除满足条件的记录
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean removeByqyery(Query query, String collection) {
		WriteResult ws = mongoTemplate.remove(query, collection);
		if (ws.getN() > 0) {
			return true;
		} else {
			return false;
		}
	}

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
	public boolean update(Query query, Update update, String collectionName) {
		WriteResult ws = mongoTemplate.updateMulti(query, update,
				collectionName);
		if (ws.isUpdateOfExisting()) {
			return true;
		} else {
			return false;
		}

	}

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
	public boolean updateOne(Query query, Update update, String collectionName) {
		WriteResult ws = mongoTemplate.updateFirst(query, update,
				collectionName);
		if (ws.isUpdateOfExisting()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Map<String, Object>> queryMulti(Query query, String collection) {
		Map<String, Object> condation = query.getQueryObject().toMap();

		Map<String, Object> fields = query.getFieldsObject() == null ? new HashMap()
				: query.getFieldsObject().toMap();
		Map<String, Object> sortObject = query.getSortObject() == null ? new HashMap()
				: query.getSortObject().toMap();
		int skip = query.getSkip() == 0 ? MAX_SKIP : query.getSkip();
		int limit = query.getLimit() == 0 ? MAX_LIMIT : query.getLimit();
		int pageNum = (skip / limit) + 1;
		List<Map<String, Object>> listMap = find(collection, condation, fields,
				sortObject, pageNum, limit);

		if (listMap != null && listMap.size() > 0) {
			for (Map<String, Object> map : listMap) {
				map.put("_id", map.get("_id").toString());
			}
		}
		listMap = listMap==null?new ArrayList<Map<String, Object>>():listMap;
		return listMap;
	}
}

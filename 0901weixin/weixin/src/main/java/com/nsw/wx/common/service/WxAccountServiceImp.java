package com.nsw.wx.common.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月14日 下午3:07:50
* @Description: TODO
 */
@Service("wxAccountService")
public class WxAccountServiceImp implements WxAccountService{
	
	@Autowired
	BaseMongoTemplate baseMongoTemplate;
	
	private Logger logger = Logger.getLogger(WxAccountServiceImp.class);

	@Override
	public Map<String,Object> getAccount(String  id) {
		// TODO Auto-generated method stub
		try {
	    	Map<String,Object> que = new HashMap<String, Object>();
	    	que.put("_id", new ObjectId(id));
	    	Map<String,Object> accout = baseMongoTemplate.findOne(Constants.WXACCOUNT_T,que);
			return accout;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取账号信息出错！"+e.getMessage());
			return null;
		}
		
	}

	@Override
	public boolean updateAccount(Map<String,Object> entity,Update update) {
		// TODO Auto-generated method stub
		try {
	    	Query query = new Query();
			Criteria criteria=Criteria.where("_id").is(new ObjectId(entity.get("id")+""));
	    	query.addCriteria(criteria);
	    	boolean results =   baseMongoTemplate.updateFirst(query, update, Constants.WXACCOUNT_T);
	    	return results;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("更新账号信息出错！"+e.getMessage());
			return false;
		}
		
		
	}

	@Override
	public boolean deleteAccount(Map<String,Object> account) {
		// TODO Auto-generated method stub
		try {
			Query query = new Query();
			Criteria criteria=Criteria.where("_id").is(new ObjectId(account.get("_id")+""));
	    	query.addCriteria(criteria);
			return  baseMongoTemplate.removeByqyery(query, Constants.WXACCOUNT_T);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("删除账号信息出错！"+e.getMessage());
			return false;
		}
		
		
	}

	@Override
	public Map<String,Object> addAccount(Map<String,Object> entity) {
		try {
			
			return baseMongoTemplate.save(Constants.WXACCOUNT_T, entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public List<Map<String,Object>> getWxAccountList(String siteId) {

		try {
			Query query = new Query();
			Criteria criteria=Criteria.where("site").is(siteId);
	    	query.addCriteria(criteria);
	    	query.with(new Sort(Direction.DESC , "create_time"));
	    	List<HashMap> accountList = baseMongoTemplate.findByQuery(query, Constants.WXACCOUNT_T);
	    	List<Map<String,Object>> accountLists = new ArrayList<Map<String,Object>>();
			for(HashMap map:accountList){
				accountLists.add(map);
			}
			return accountLists;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取账号信息列表出错！"+e.getMessage());
			return null;
		}
		
	}

	@Override
	public Map<String,Object> getAccountByUserName(String userName) {
		// TODO Auto-generated method stub
		try {
	    	Map<String,Object> que = new HashMap<>();
	    	que.put("user_name", userName);
	    	Map<String,Object> account = 	baseMongoTemplate.findOne( Constants.WXACCOUNT_T, que);
			return account;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取账号信息出错！"+e.getMessage());
			return null;
		}
				
	}

	@Override
	public Map<String,Object> getAccountByUrl(String url) {
		// TODO Auto-generated method stub
		try {
			Map<String,Object> que = new HashMap<>();
	    	que.put("url", url);
	    	Map<String,Object> account = 	baseMongoTemplate.findOne( Constants.WXACCOUNT_T, que);
			return account;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取账号信息出错！"+e.getMessage());
			return null;
		}
	
	}

	@Override
	public Map<String,Object> getAccountByAppId(String appId) {
		try {
	    	Map<String,Object> que = new HashMap<>();
	    	que.put("appId", appId);
	    	Map<String,Object> account = 	baseMongoTemplate.findOne( Constants.WXACCOUNT_T, que);
			return account;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("获取账号信息出错！"+e.getMessage());
			return null;
		}
		
	}

	@Override
	public List<Map<String,Object>> getWxAccountListByFilter(String site,String filter) {

			try {
				Query querys = new Query();
				Criteria cr = new Criteria();
				querys.addCriteria(cr.orOperator(
				    Criteria.where("nick_name").regex(filter)
				    ,Criteria.where("user_name").regex(filter)));
				querys.addCriteria(Criteria.where("site").is(site));
				querys.with(new Sort(Direction.DESC , "create_time"));
				List<HashMap> accountList =  baseMongoTemplate.findByQuery(querys, Constants.WXACCOUNT_T);
				 List<Map<String,Object>>  list = new ArrayList<Map<String,Object>>();
				for(HashMap h:accountList){
					list.add(h);
				}
				return list;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("模糊查找信息出错！"+e.getMessage());
				return null;
			}
			
			
	}

}

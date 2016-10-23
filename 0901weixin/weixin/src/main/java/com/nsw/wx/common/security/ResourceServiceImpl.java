package com.nsw.wx.common.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.UserService;
import com.nsw.wx.common.util.Constants;



@SuppressWarnings("all")
@Transactional
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

	public static Logger logger = Logger.getLogger(ResourceServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	@Autowired
	public UserService userService;

	public Set getAll() {
		
		//初始化权限数据
		long authrityAccount =  baseMongoTemplate.findCountByQuery(new org.springframework.data.mongodb.core.query.Query(), Constants.AUTHORITY);
		if(authrityAccount==0){
			// 开始初始化日志数据
			String jsonStr =   readJsonFile("authorityInitData.json");
			JSONArray arr = JSONArray.fromObject(jsonStr);
			baseMongoTemplate.saveDataList(Constants.AUTHORITY, arr);
		}
		
		org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
		List<Map<String,Object>> list = baseMongoTemplate.queryMulti(query, Constants.AUTHORITY);//权限
		Set<Map> set = new HashSet();
		for(Map map : list){
			Map m = new HashMap();
			m.put("url", map.get("url")+"");
			m.put("code", map.get("code")+"");
			m.put("method", map.get("method")+"");
			set.add(m);
		}
		List<Map<String,Object>> lists = baseMongoTemplate.queryMulti(query, Constants.MENU);//菜单
		for(Map map : lists){
			String code = map.get("code")+"";
			org.springframework.data.mongodb.core.query.Query querys = new org.springframework.data.mongodb.core.query.Query();
			querys.addCriteria(Criteria.where("menuCode").is(code));
			List<Map<String,Object>> menu = baseMongoTemplate.queryMulti(querys,  Constants.MENUROLE);//菜单
			Set<String> codeSet = new HashSet<String>();
			for(Map ma : menu){
				codeSet.add(ma.get("roleCode")+"");
			}
			Map map1 = new HashMap();
			map1.put("url", map.get("href")+"");
			map1.put("code", codeSet);
			map1.put("method", "GET");
			set.add(map1);
		}
		return set;
	}

	public String readJsonFile(String fileName) {
			
			ClassPathResource resources = new ClassPathResource(fileName);
			BufferedReader reader = null;
			String laststr = "";
			try {
				InputStream fileInputStream = resources.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(
						fileInputStream, "UTF-8");
				reader = new BufferedReader(inputStreamReader);
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					laststr += tempString;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return laststr;
		}
	
	

	
}

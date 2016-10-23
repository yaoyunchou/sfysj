package com.nsw.wx.common.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.nsw.wx.common.model.Enterprise;
import com.nsw.wx.common.model.User;
import com.nsw.wx.common.repository.UserRepository;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;



@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	//@Autowired
	//CustomerRepository customerRepository;

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	//@Autowired
	//InternalUserRepository internalUserRepository;


	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findOne(Long id) {
		return userRepository.findOne(id);
	}



	@Override
	public User findByUserName(String username) {
		
		Map<String, Object> map =null;
		String querySql = "select * from user where user_name='"+username+"'" ;
		Query search = em.createNativeQuery(querySql);
		search.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> rows = search.getResultList();
		if (rows != null && rows.size() > 0) {
			map = (Map<String, Object>) rows.get(0);
			if(map != null ){
				User user = new User();
				try {
					user.setUserName(map.get("user_name")==null?"":map.get("user_name")+"");
					user.setId(   Integer.parseInt(map.get("id")+""));
					user.setLoginTimes(map.get("login_times") == null ? 0 : (long)Integer.parseInt(map.get("login_times")+""));
					user.setLastloginip(map.get("last_login_time")==null?"":map.get("last_login_time")+"");
					user.setLastLoginTime(map.get("last_login_time")==null?new Date(): new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get("last_login_time")+"" ));
					user.setStatus(map.get("status")+"");
					user.setType(  Integer.parseInt(map.get("type")+""));
					user.setEnterpriseRelationId(map.get("enterprise_relation_id")+"");
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return user;
			}
		}
		
		return null;
	}



	/***
	 * 更新密码
	 * @Description: TODO
	 * @param @param id
	 * @param @param passowrd
	 * @return void
	 * @throws
	 */
	public int updatePassword(Long id, String passowrd) {
		String jpql = "update User user set user.passWord='" + passowrd
				+ "' where user.id=" + id;
		Query query = em.createQuery(jpql);
		int flag = query.executeUpdate();
		return flag;
	}


	/***
	 * 通过项目ID获取企业ID和项目类型
	 * @Description: TODO
	 * @param @param id
	 * @param @param passowrd
	 * @return void
	 * @throws
	 */
	public Map<String, Object> getProjectType(String projId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String querySql = "select * from project where projId='"+projId+"'" ;
		Query search = em.createNativeQuery(querySql);
		search.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> rows = search.getResultList();
		if (rows != null && rows.size() > 0) {
			map = (Map<String, Object>) rows.get(0);
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> getProjectList(String site,
													List<Integer> listTypes) {

		Map<String, Object> map = null;
		String querySql = "select * from project where enterpriseId= " +Integer.parseInt(site) +" and projectType in "+listTypes ;
		querySql = querySql.replace("[", "(").replace("]",")");
		Query search = em.createNativeQuery(querySql);
		search.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> rows = search.getResultList();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if (rows != null && rows.size() > 0) {
			for(int i = 0;i<rows.size();i++){
				map = (Map<String, Object>) rows.get(i);
				list.add(map);
			}
		}
		return list;
	}
	
	
	
	/**
	 * 获取企业开通的所有项目
	 * 并且获取是否开通 
	 * @param userId
	 * @return
	 */
	public Map<String,Object> getAllProjectInfo(long userId){
		Map<String, Object> maps = new HashMap<>();
		List<Map<String, Object> > listMap = new ArrayList<Map<String,Object>>();
		String querySql = "select * from project pj where pj.enterpriseId = (select p.enterpriseId from project p where p.projId = ( select u.enterprise_relation_id from user u where u.id= "+userId+"))";
		
		Query search = em.createNativeQuery(querySql);
		search.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> rows = search.getResultList();
		String projecTypes = "";
		if(rows != null && rows.size()>0){
			for(int i=0;i<rows.size();i++){
				Map<String, Object> map  = (Map<String, Object>) rows.get(i);
				projecTypes += (map.get("projectType")==null?"":map.get("projectType"))+";";
				listMap.add(map);
			}
			maps.put("list", listMap);
		}else{
			maps.put("is_pc_mb", false);//是否开通pc和手机
			maps.put("list", new ArrayList<>());
			return maps;
		}
		if(projecTypes.indexOf("4")!=-1 && projecTypes.indexOf("5")!=-1){//是否开通pc和手机
			maps.put("is_pc_mb", true);
		}else{
			maps.put("is_pc_mb", false);
		}
		return maps;
	}
	
	
	
	/***
	 * 通过企业ID获取企业信息
	 * @Description: TODO
	 * @param @param id
	 * @param @param passowrd
	 * @return void
	 * @throws
	 */
	public Enterprise getEnterPrise(String enterpriseId) {
		Map<String, Object> map = null;
		String jpql = "select enterprise from Enterprise enterprise where enterprise.id="+enterpriseId ;
		Query query = em.createQuery(jpql);
		List<Enterprise> enterPrise = query.getResultList();
		if(enterPrise != null && enterPrise.size()>0){
			return enterPrise.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> getUserInfo(String type, Long id) {
		Map<String, Object> map = null;
		String querySql = "";
		if ("0".equals(type)) {//内部用户更新internal_user表
			querySql = "select * from internal_user  where id = " + id;
		} else {// 企业用户
				querySql = "select * from  enterprise where id = (select enterprise_id from customer where id = "+id+" ) ";
		}
		if (!"".equals(querySql)) {
			Query search = em.createNativeQuery(querySql);
			search.unwrap(SQLQuery.class).setResultTransformer(
					Transformers.ALIAS_TO_ENTITY_MAP);
			List<?> rows = search.getResultList();
			if (rows != null && rows.size() > 0) {
				map = (Map<String, Object>) rows.get(0);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getProjectGroupBySite(String enterpriseId) {
		Map<String, Object> map = null;
		String querySql = "select * from  site where type = 7 and  enterprise_id = "+enterpriseId+" ";
		Query search = em.createNativeQuery(querySql);
		search.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		List<?> rows = search.getResultList();
		if (rows != null && rows.size() > 0) {
			map = (Map<String, Object>) rows.get(0);
		}
		return map;
	}


	public void updateSiteInfo(Long siteId, Map map) {
		String sql = "update site set " + "formal_domain = '"
				+ map.get("formalDomain") + "' , " + "file_number = '"
				+ map.get("fileNumber") + "' " + "where id =" + siteId;
		Query query = em.createNativeQuery(sql);
		query.executeUpdate();
	}
	
	public boolean checkUserPermission(long userId){
		boolean flag = false;
		String []roleIds = new String[]{Constants.ROLE_QY_ADMIN,Constants.ROLE_PT_SHKF,Constants.ROLE_PT_CH};
		Map<String, Object> map =  getRoleByUserId(userId);
		if(map!=null){
			String roleCode = map.get("roleCode")+"";
			for(int  i = 0;i < roleIds.length; i++){
				if(roleIds[i].equals(roleCode)){
					flag = true;
					break;
				}
			}
		}
		return flag;
	}


	@Override
	public Map<String, Object> getRoleByUserId(long userId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = null;
		String jpql = "select role_id from user_role  where user_id="+userId ;
		Query query = em.createNativeQuery(jpql);
		List<Object> queryList =   query.getResultList();
		String userole_id = null;
		if(queryList!=null && queryList.size()>0){
			userole_id = queryList.get(0).toString();
		}
		
		jpql =  "select * from role  where id="+userole_id ;
		Query query2 = em.createNativeQuery(jpql);
		query2.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		List enterPrise = query2.getResultList();
		
		if(enterPrise != null && enterPrise.size()>0){
			map = (Map) enterPrise.get(0);
			if(map != null){
				int roleId =  Integer.parseInt(map.get("id").toString());
				String roleCode = null;
				switch (roleId) {
					case Constants.ROLE_QY_ADMIN_ID://企业管理员 
						roleCode = Constants.ROLE_QY_ADMIN;
						map.put("roleName", "企业管理员 ");
						break;
					case Constants.ROLE_QY_BJTG_ID://企业编辑推广
						roleCode = Constants.ROLE_QY_BJTG;
						map.put("roleName", "企业编辑推广");
						break;
					case Constants.ROLE_QY_XSKF_ID://企业销售客服
						roleCode = Constants.ROLE_QY_XSKF;
						map.put("roleName", "企业销售客服");
						break;
					case Constants.ROLE_PT_BJ_ID://平台编辑人员
						roleCode = Constants.ROLE_PT_BJ;
						map.put("roleName", "平台编辑人员");
						break;
					case Constants.ROLE_PT_CH_ID://平台策划人员
						roleCode = Constants.ROLE_PT_CH;
						map.put("roleName", "平台策划人员");
						break;
					case Constants.ROLE_PT_SHKF_ID://平台售后客服
						roleCode = Constants.ROLE_PT_SHKF;
						map.put("roleName", "平台售后客服");
						break;
					case Constants.ROLE_PT_CQ_ID://平台裁切人员
						roleCode = Constants.ROLE_PT_CQ;
						map.put("roleName", "平台裁切人员");
						break;
					case Constants.ROLE_MBSJ_ID://模板设计小组
						roleCode = Constants.ROLE_MBSJ;
						map.put("roleName", "模板设计小组");
						break;
					default:
						map.put("roleName", "");
						break;
				}
				map.put("roleCode", roleCode);
			}
			
			return map;
		}
		
		return null;
	}

	@Override
	public int updateUserInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub

		int flag = 0;
		String type = ContextUtil.getLoginUser().getType();
		String sql = "";
		if ("0".equals(type)) {// 内部用户更新internal_user表
			sql = "update internal_user set full_name = '" + map.get("name")
					+ "', dept_name = '" + map.get("deptName") + "',"
					+ "phone_number='" + map.get("mobile")
					+ "',email_address='" + map.get("email") + "' where id = "
					+ map.get("id");
		} else { // 企业用户
				sql = "update enterprise set name = '"
						+ map.get("name") + "', " + "contact_email = '"
						+ map.get("email") + "',contact_phone_number='"
						+ map.get("mobile") + "',dept_name='"+map.get("deptName")+"' where id = "
						+ Long.parseLong(ContextUtil.getSite());//site保存的是企业ID
		}
		if (!"".equals(sql)) {
			Query query = em.createNativeQuery(sql);
			flag = query.executeUpdate();
		}
		return flag;
	}
	
	
	



}

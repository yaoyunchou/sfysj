package com.nsw.wx.common.service;

import java.util.List;
import java.util.Map;

import com.nsw.wx.common.model.Enterprise;
import com.nsw.wx.common.model.User;


public interface UserService {

	/**
	 * 
	 * @Description: 查询用户列表
	 * @param @return
	 * @return List<User>
	 * @throws
	 */

	//List<User> findAll();

	/**
	 * 根据Id查询用户
	 * 
	 * @Description: TODO
	 * @param @param id
	 * @param @return
	 * @return User
	 * @throws
	 */

	User findOne(Long id);

	

	/**
	 * 根据名称查询用户
	 * 
	 * @Description: TODO
	 * @param @param username
	 * @param @return
	 * @return User
	 * @throws
	 */
	User findByUserName(String username);


	
	public int updateUserInfo(Map<String, Object> map);
	
	/**
	 * 根据用户id获取用户角色
	 */
	public Map<String,Object> getRoleByUserId(long userId);

	/***
	 * 更新密码
	 */
	public int updatePassword(Long id, String passowrd);


	/***
	 * 通过项目ID获取企业ID和项目类型
	 * 
	 * @Description: TODO
	 * @param @param projId
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String, Object> getProjectType(String projId);

	Map getUserInfo(String type, Long id);

   Map<String,Object> getProjectGroupBySite(String enterpriseId);
	
	public Enterprise getEnterPrise(String enterpriseId) ;

	
	public Map<String,Object> getAllProjectInfo(long userId);
	
	
	public boolean checkUserPermission(long userId);

	List<Map<String, Object>> getProjectList(String site, List<Integer> listTypes);

}

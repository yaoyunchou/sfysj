package com.nsw.wx.common.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nsw.wx.common.model.Role;
import com.nsw.wx.common.model.User;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.UserService;
import com.nsw.wx.common.util.Constants;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月18日 下午5:14:26
 * @Description: 认证处理逻辑
 */
@SuppressWarnings("all")
@Service("currentUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = Logger
			.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		User user = userService.findByUserName(userName.trim());
		
		if (user == null)
			throw new UsernameNotFoundException(" 用户名：" + userName + "不存在！");
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		Role roles = new Role();
		
		Map roleMap = userService.getRoleByUserId(user.getId());
		if(roleMap != null ){
			roles.setName(roleMap.get("roleCode")+"");
		}else{
			roles.setName("guest");
		}
		Set<Role> roleSet = new HashSet<Role>();
		roleSet.add(roles);
		Set<String> setRoleName = new HashSet();
		if (roleSet != null) {
			for (Role role : roleSet) {
				setRoleName.add(role.getName());
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			}
		}
		
		List<Map<String,Object>> authority = null;
		if(setRoleName.contains(Constants.SUPERROLE)){//超级管理员
			Query query = new Query();
			authority = baseMongoTemplate.queryMulti(query, Constants.AUTHORITY);
		}else{
			for(String roleName : setRoleName){
				Query query = new Query();
				query.addCriteria(Criteria.where("roleCode").in(new String[]{roleName}));//需要根据code去处重复
			 authority = baseMongoTemplate.queryMulti(query, Constants.AUTHORITY);
			}
		}
		
		if (authority != null) {
			for (Map map : authority) {
				authorities.add(new SimpleGrantedAuthority(map.get("code")+""));
			}
		}
		
		/**菜单需要的角色*/
		boolean isEnabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		if (!"enabled".equalsIgnoreCase(user.getStatus())) {
			isEnabled = true;
		}
		logger.info("用户名为：" + user.getUserName() + " ,拥有的权限为--：" + authorities);
		CustomSecurityUser securityUser = new CustomSecurityUser(user.getUserName(),
				"", isEnabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities,
				"",user.getId(),user.getType()+"",user.getLoginTimes(),user.getLastloginip(),user.getLastLoginTime());
		return securityUser;
	}

	/**
	 * @Description: 获得用户所有角色的权限集合
	 * @param @param user
	 * @param @return
	 * @return Set<GrantedAuthority>
	 * @throws
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	private Set<GrantedAuthority> obtainGrantedAuthorities(User user) {
		Set<GrantedAuthority> authSet = new HashSet();
		for (Role role : user.getRoles()) {
			authSet.add(new GrantedAuthorityImpl(role.getName()));
		}
		return authSet;
	}
	
	/**
     * @Description: 获取当前登录用户
     * @param @param 
     * @param @return   
     * @return User  
     * @throws
     */
	public CustomSecurityUser getCurrentUser(){
		try {
			CustomSecurityUser customSecurityUser=(CustomSecurityUser) 
					SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			logger.info("获取登录用户   "+customSecurityUser.getUsername()+"拥有的权限名称："+customSecurityUser.getAuthorities());
			return customSecurityUser;
		} catch (Exception e) {
			logger.error("获取登录用户异常   "+e.getStackTrace());
			return null;
		}
	}
	
}

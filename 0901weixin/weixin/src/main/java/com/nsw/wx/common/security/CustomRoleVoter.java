package com.nsw.wx.common.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月24日 下午3:15:15
 * @Description: 自定义决策策略
 */
@SuppressWarnings({ "all", "rawtypes" })
public class CustomRoleVoter implements AccessDecisionVoter {
	
	public static Logger logger = Logger.getLogger(CustomRoleVoter.class);
	
	public CustomRoleVoter(){
		logger.info("--投票器初始化--");
	}

	public boolean supports(ConfigAttribute attribute) {

		return true;
	}

	public boolean supports(Class clazz) {
		return true;
	}

	public int vote(Authentication authentication, Object object,
			Collection attributes) {
		Collection<GrantedAuthority> authorities = extractAuthorities(authentication); // 当前用户拥有的权限(角色)
		Collection<ConfigAttribute> attributesList = (Collection<ConfigAttribute>) attributes;// 当前访问资源需要的权限(角色)
		logger.info("正在访问的url是" + object.toString());
		for (ConfigAttribute attribute : attributesList) {
			String needPermission = attribute.getAttribute();
			logger.info("===需要的权限是"+ needPermission + "===当前拥有的权限是："
					+ authentication.getAuthorities());
				List<String> listPermission = new ArrayList<String>();
				if (needPermission != null) {
					listPermission = Arrays.asList(needPermission.split(","));
				}
				if(needPermission == null){
					needPermission = "ROLE_ANONYMOUS";
				}
				if ("ROLE_ANONYMOUS".equals(needPermission)) {//不需要权限,可以匿名访问
					return ACCESS_GRANTED;
	            }
				List listRole = new ArrayList();
				if (authentication.getAuthorities() != null) {
					for (GrantedAuthority ga : authentication.getAuthorities()) { //当前用拥有的权限
	                listRole.add(ga.getAuthority());
					}
					//disjoint()两个Collection对象有相同元素的时候返回false，没有则返回true，存在null的也返回true。
					if (Collections.disjoint(listRole, listPermission) == false) {
						 return ACCESS_GRANTED;
					}
				}
		}
		return ACCESS_DENIED;
	}

	Collection extractAuthorities(Authentication authentication) {
		return authentication.getAuthorities();
	}
}

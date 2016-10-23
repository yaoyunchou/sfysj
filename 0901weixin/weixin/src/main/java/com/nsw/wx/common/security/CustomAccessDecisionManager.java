package com.nsw.wx.common.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月18日 下午4:18:26
 * @Description: 验证是否拥有访问的权限
 */

@SuppressWarnings("all")
public class CustomAccessDecisionManager implements AccessDecisionManager {

	public static Logger logger = Logger
			.getLogger(CustomAccessDecisionManager.class);

	private boolean allowIfAllAbstainDecisions = false;

	public boolean isAllowIfAllAbstainDecisions() {
		return allowIfAllAbstainDecisions;
	}

	public void setAllowIfAllAbstainDecisions(boolean allowIfAllAbstainDecisions) {
		this.allowIfAllAbstainDecisions = allowIfAllAbstainDecisions;
	}

	protected final void checkAllowIfAllAbstainDecisions() {
		if (!this.isAllowIfAllAbstainDecisions()) {
			throw new AccessDeniedException("没有权限访问");
		}
	}

	public CustomAccessDecisionManager() {
		logger.info("投票器初始化");
	}

	// http://my.oschina.net/u/1177710/blog/299707
	@SuppressWarnings("unchecked")
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		// logger.info("正在访问的url是" + object.toString() +
		// (System.currentTimeMillis()));
		// 如果访问资源不需要任何权限则直接通过
		if (configAttributes == null) {
			return;
		}
		if (authentication.getAuthorities() == null
				|| authentication.getAuthorities().isEmpty()) {
			throw new AccessDeniedException("无权限");
		}
		// 所请求的资源拥有的权限(一个资源对多个权限)
		Iterator<ConfigAttribute> iterator = configAttributes.iterator();
		// 遍历configAttributes看用户是否有访问资源的权限
		while (iterator.hasNext()) {
			ConfigAttribute configAttribute = iterator.next();
			String needPermission = configAttribute.getAttribute();
			// 如果需要的权限是空(也许在数据库没有对应的url，则匿名可以访问)
			logger.info("===需要的权限是 " + needPermission + "===当前拥有的权限是："
					+ authentication.getAuthorities());
			List<String> listPermission = new ArrayList<String>();
			if (needPermission != null) {
				listPermission = Arrays.asList(needPermission.split(","));
			}
			if (needPermission == null) {
				needPermission = "ROLE_ANONYMOUS";
			}

			if ("ROLE_ANONYMOUS".equals(needPermission)) {//不需要权限,可以匿名访问
				return;
			}
			List listRole = new ArrayList();
			if (authentication.getAuthorities() != null) {
				for (GrantedAuthority ga : authentication.getAuthorities()) {
					if (needPermission.trim().equals(ga.getAuthority().trim())) {
						return;
					}
				}
			}
		}
		checkAllowIfAllAbstainDecisions();
	}

	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	public boolean supports(Class<?> clazz) {
		return true; // 必须放回true,否则会提醒类型错误
	}
}

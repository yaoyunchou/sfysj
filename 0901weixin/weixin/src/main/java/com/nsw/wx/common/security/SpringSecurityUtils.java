package com.nsw.wx.common.security;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * 
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月29日 下午3:29:25
 * @Description: 工具类，获取当前登录用户相关信息
 */
@SuppressWarnings("all")
public class SpringSecurityUtils {

	static Logger logger = Logger.getLogger(SpringSecurityUtils.class);

	public static <T extends org.springframework.security.core.userdetails.User> T getCurrentUser() {
		Authentication authentication = getAuthentication();

		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof User)) {
			return null;
		}
		return (T) principal;
	}

	/**
	 * 
	 * @Description: TODO
	 * @param @return
	 * @return 取得当前用户的登录名, 如果当前用户未登录则返回空字符串.
	 * @throws
	 */
	public static String getCurrentUserName() {
		Authentication authentication = getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			return "";
		}
		return authentication.getName();
	}

	/**
	 * @Description: TODO
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getCurrentName() {
		return getCurrentUser().getUsername();
	}

	/**
	 * @Description: TODO
	 * @param @return
	 * @return 取得当前用户登录IP, 如果当前用户未登录则返回空字符串.
	 * @throws
	 */
	public static String getCurrentUserIp() {
		Authentication authentication = getAuthentication();

		if (authentication == null) {
			return "";
		}

		Object details = authentication.getDetails();
		if (!(details instanceof WebAuthenticationDetails)) {
			return "";
		}

		WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
		return webDetails.getRemoteAddress();
	}

	/**
	 * 
	 * @Description: TODO
	 * @param @param roles
	 * @param @return
	 * @return 判断用户是否拥有角色, 如果用户拥有参数中的任意一个角色则返回true.
	 * @throws
	 */
	@SuppressWarnings("all")
	public static boolean hasAnyRole(String... roles) {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<GrantedAuthority> grantedAuthorityList = (Collection<GrantedAuthority>) authentication
				.getAuthorities();
		for (String role : roles) {
			for (GrantedAuthority authority : grantedAuthorityList) {
				if (role.equals(authority.getAuthority())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @Description: UserDetails保存到Security Context.
	 * @param @param userDetails
	 * @param @param request
	 * @return void
	 * @throws
	 */
	public static void saveUserDetailsToContext(UserDetails userDetails,
			HttpServletRequest request) {
		PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
				userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		if (request != null) {
			authentication.setDetails(new WebAuthenticationDetails(request));
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	/**
	 * 
	 * @Description: 取得Authentication, 如当前SecurityContext为空时返回.
	 * @param @return
	 * @return Authentication
	 * @throws
	 */
	public static Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return null;
		}
		return context.getAuthentication();
	}

	/**
	 * @Description: 获取当前登录用户
	 * @param @return
	 * @param @throws AuthenticationException
	 * @return User
	 * @throws
	 */
	public static CustomSecurityUser getCurrentLoginUser()
			throws AuthenticationException {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		CustomSecurityUser currentUser = null;
		if (auth != null) {
			if (auth.getPrincipal() instanceof CustomSecurityUser) {
				currentUser = (CustomSecurityUser) auth.getPrincipal();
			} else if (auth.getDetails() instanceof UserDetails) {
				currentUser = (CustomSecurityUser) auth.getDetails();
			}
		} else {
			logger.debug("用户认证出错");
			//throw new AccessDeniedException("用户认证出错！");
		}
		return currentUser;
	}

}

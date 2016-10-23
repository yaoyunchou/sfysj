package com.nsw.wx.common.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import com.nsw.wx.common.model.Site;
import com.nsw.wx.common.service.FTPService;
import com.nsw.wx.common.service.UserService;


/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月21日 下午5:10:24
 * @Description: 认证成功时处理操作，加入自己的业务处理
 */
@SuppressWarnings("all")
public class CustomAuthenticationSuccessHandler implements
		AuthenticationSuccessHandler {

	Logger logger = Logger.getLogger(CustomAuthenticationSuccessHandler.class);

	private String defaultTargetUrl;

	private Integer session_timeOut_seconds;

	@Autowired
	private UserService userService;
	
	
	@Autowired
	private FTPService ftpService;

	public Integer getSession_timeOut_seconds() {
		return session_timeOut_seconds;
	}

	public void setSession_timeOut_seconds(Integer session_timeOut_seconds) {
		this.session_timeOut_seconds = session_timeOut_seconds;
	}


	private RequestCache requestCache = new HttpSessionRequestCache();

	public RequestCache getRequestCache() {
		return requestCache;
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	public String getDefaultTargetUrl() {
		return defaultTargetUrl;
	}

	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}

	public CustomAuthenticationSuccessHandler() {
		logger.info("=====认证成功处理器=====");
	}

	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		SecurityContext ctx = SecurityContextHolder.getContext();
		Authentication auth = ctx.getAuthentication();
		CustomSecurityUser securityUser = null;
		HttpSession session = request.getSession(true);
		if (auth.getPrincipal() instanceof UserDetails) {
			securityUser = (CustomSecurityUser) auth.getPrincipal();
		}
		Map<String, Object> projInfo = ftpService
				.getProjectInfo(securityUser.getId());
		if (projInfo != null && !projInfo.isEmpty()) {
			securityUser.setProjId(projInfo.get("projId") + "");
			securityUser.setSite(projInfo.get("enterpriseId")+"");
			securityUser.setProjectType(projInfo.get("projectType") + "");
//			if (projInfo != null && !projInfo.isEmpty()) {
//				securityUser.setProjId(projInfo.get("projId") + "");
//				securityUser.setSite(projInfo.get("enterpriseId") + "");
//				securityUser.setProjectType(projInfo.get("projectType") + "");
//			} else {
//				//查找是否开通微信项目
//				Site site = ftpService.getSiteInfo(Long.parseLong(securityUser.getSite()) , securityUser.getProjectType());
//				if(site == null ){
//					logger.error("该用户还未开通微信项目！");
//					response.sendRedirect(request.getContextPath()
//							+ getDefaultTargetUrl());//跳转到无权限页面
//					return;
//				}
//			}
		}
//		else{
//			response.sendRedirect(request.getContextPath()
//					+ getDefaultTargetUrl());//跳转到无权限页面
//			return;
//		}

		securityUser.setSite(securityUser.getSite());
		securityUser.setLoginIp(AjaxUtil.getIpAddress(request));
		logger.info("项目信息" + projInfo + "用户类型为："
				+ ("0".equals(securityUser.getType()) ? "内部用户" : "企业用户"));
		PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(
				securityUser, securityUser.getPassword(),
				securityUser.getAuthorities());
		authenticationToken.setDetails(new WebAuthenticationDetails(request));
		//存放authentication到SecurityContextHolder
		authenticationToken.setDetails(securityUser);
		ctx.setAuthentication(authenticationToken);
		
		//在session中存放security context,方便同一个session中控制用户的其他操作
		session.setMaxInactiveInterval(session_timeOut_seconds);
		session.setAttribute("SPRING_SECURITY_CONTEXT", ctx);

		if (AjaxUtil.isAjax(request)) {
			logger.info("【" + securityUser.getUsername() + "】"
					+ "登录验证成功，异步跳转到: " + request.getContextPath()
					+ getDefaultTargetUrl());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("msg", "登陆成功！");
			map.put("redirectUrl", getDefaultTargetUrl());
			AjaxUtil.rendJson(response, true, map);
			//requestCache.removeRequest(request, response);
			//clearAuthenticationAttributes(request);
		} else {
			logger.info("【" + securityUser.getUsername() + "】"
					+ "登录验证成功，Http请求跳转到： " + request.getContextPath()
					+ getDefaultTargetUrl());
			response.sendRedirect(request.getContextPath()
					+ getDefaultTargetUrl());
			/**
			 * Object savedRequestObject = request.getSession().getAttribute(
			 * "SPRING_SECURITY_SAVED_REQUEST"); defaultTargetUrl =
			 * ((SavedRequest) savedRequestObject).getRedirectUrl();
			 * handle(request, response, authentication);
			 * clearAuthenticationAttributes(request);
			 */
		}
	}

	protected void handle(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException {

		defaultTargetUrl = determineTargetUrl(authentication);

		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to "
					+ defaultTargetUrl);
			return;
		}
		redirectStrategy.sendRedirect(request, response, defaultTargetUrl);
	}

	protected String determineTargetUrl(Authentication authentication) {
		boolean isUser = false;
		boolean isAdmin = false;
		Collection<? extends GrantedAuthority> authorities = authentication
				.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
				isUser = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}
		if (isUser) {
			return "/index.html";
		} else if (isAdmin) {
			return "/systemIndex.html";
		} else {
			throw new IllegalStateException();
		}
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
	

}

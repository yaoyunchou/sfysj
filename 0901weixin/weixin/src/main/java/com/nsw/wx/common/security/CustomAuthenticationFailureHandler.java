package com.nsw.wx.common.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nsw.wx.common.service.UserService;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月17日 下午5:08:38
 * @Description: 认证失败时处理操作，加入自己的业务处理
 */
@SuppressWarnings("all")
public class CustomAuthenticationFailureHandler implements
		AuthenticationFailureHandler {

	Logger logger = Logger.getLogger(CustomAuthenticationFailureHandler.class);

	private String defaultFailureUrl;
	
	public CustomAuthenticationFailureHandler(UserService userService) {
		this.userService=userService;
	}

	private UserService userService;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private boolean forwardToDestination = false;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private int maxTryCount;

	public int getMaxTryCount() {
		return maxTryCount;
	}

	public void setMaxTryCount(int maxTryCount) {
		this.maxTryCount = maxTryCount;
	}

	public final static String TRY_MAX_COUNT = "tryMaxCount";
	
	public  final static String TRY_MAX_ERROR_COUNT = "tryErrorCount";//用户名密码错误次数超过三次则输入验证码

	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

	public CustomAuthenticationFailureHandler() {

	}

	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		//request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
		logger.info("登录失败了,用户名" + request.getParameter("username") + ", 输入 密码"
				+ request.getParameter("password") + " 最大尝试次数"+maxTryCount);
		logger.info("异常信息:" + exception.getMessage());
		//exception.getAuthentication().getDetails();
		if (AjaxUtil.isAjax(request)) {
			logger.info("来自异步请求处理");
			Map<String, Object> map = new HashMap<String, Object>();
			/**
			 * if(exception instanceof BadCredentialsException){ map.put("msg",
			 * "用户名或密码错误!"); logger.info("用户名或密码错误!"); } if(exception instanceof
			 * DisabledException){ map.put("msg", "您的账号已被禁用,无法登录!");
			 * logger.info("您的账号已被禁用,无法登录!"); } if(exception instanceof
			 * LockedException){ map.put("msg", "您的账号已被锁定,无法登录!");
			 * logger.info("您的账号已被锁定,无法登录!"); } if(exception instanceof
			 * AccountExpiredException){ map.put("msg", "您的账号已过期,无法登录!");
			 * logger.info("您的账号已过期,无法登录!"); } if(exception instanceof
			 * CredentialsExpiredException){ map.put("msg", "证书过期!");
			 * logger.info
			 * (" 证书过期!"+exception.getMessage()+"  "+exception.getStackTrace());
			 * }
			 **/
			if(exception instanceof BadCredentialsException){//密码输入错误次数
				HttpSession session = request.getSession();
				//Integer errorTimes=userService.getErrorsTimesByUserName("user");
				//userService.updateErrorsTimesByUserName(request.getParameter("username"),"");
				Integer tryCount = (Integer) session.getAttribute(TRY_MAX_ERROR_COUNT);
				if (tryCount == null || "".equals(tryCount)) {
					session.setAttribute(TRY_MAX_ERROR_COUNT, 1);// 增加失败次数
				}else{
					if(tryCount>=2){
						logger.info("---密码错误超过3次,显示验证码----");
						map.put("showCaptchaCode", true);
						session.setAttribute(TRY_MAX_ERROR_COUNT, tryCount + 1);
					}else{
						session.setAttribute(TRY_MAX_ERROR_COUNT, tryCount + 1);
					}
				}
			}else{
				map.put("showCaptchaCode", false);
			}
			map.put("msg", exception.getMessage());
			AjaxUtil.rendJson(response, false, map);
		} else {
			logger.info("来自http请求处理,跳转到:" + getDefaultFailureUrl());
			saveException(request, exception);
			Exception springSecurityLastException = (Exception) request
					.getSession()
					.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
			// AuthenticationException ex = ((AuthenticationException)
			// request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION));
			if(springSecurityLastException!=null){
				logger.info(springSecurityLastException.getMessage());
			}
			response.sendRedirect(request.getContextPath()
					+ getDefaultFailureUrl());
			return; // 不加return报错
		}

		HttpSession session = request.getSession();
		Integer tryCount = (Integer) session.getAttribute(TRY_MAX_COUNT);
		if (tryCount == null) {
			session.setAttribute(TRY_MAX_COUNT, 1);// 增加失败次数
		} else {
			if (tryCount > maxTryCount - 1) {
				// 锁定账户
				// BaseDao baseDao=(BaseDao)
				// ConfigConstants.context.getBean("baseDao");
				String name = request
						.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
				// List<User>
				// userList=baseDao.getHibernateTemplate().find("from User u where u.name=?",
				// name);
				// if(userList.size()>0){
				if (true) {
					// userList.get(0).setIsLocked(true);
					// baseDao.getHibernateTemplate().update(userList.get(0));
				}
				// session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
				// new
				// MaxTryLoginException("超过最大登录尝试次数"+maxTryCount+",用户已被锁定"));
			}
			session.setAttribute("tryMaxCount", tryCount + 1);
		}

		// 觉得默认跳转的地方
		if (defaultFailureUrl == null) {
			logger.debug("No failure URL set, sending 401 Unauthorized error");
			// response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
			// "Authentication Failed: " + exception.getMessage());
		} else {

			if (forwardToDestination) {
				logger.debug("Forwarding to " + defaultFailureUrl);
				// request.getRequestDispatcher(defaultFailureUrl).forward(request,
				// response);
			} else {
				logger.debug("Redirecting to " + defaultFailureUrl);
				// redirectStrategy.sendRedirect(request, response,
				// defaultFailureUrl);
			}
		}
	}

	/**
	 * @Description: 保存错误信息到session中， key为SPRING_SECURITY_LAST_EXCEPTION
	 * @param @param request
	 * @param @param exception
	 * @return void
	 * @throws
	 */
	protected final void saveException(HttpServletRequest request,
			AuthenticationException exception) {
		if (forwardToDestination) {
			request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
					exception);
		} else {
			HttpSession session = request.getSession(false);
			if (session != null) {
				request.getSession().setAttribute(
						WebAttributes.AUTHENTICATION_EXCEPTION, exception);
			}
		}
	}
}

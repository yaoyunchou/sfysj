package com.nsw.wx.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月28日 下午3:19:40
 * @Description: Spring security登录核心拦截器 , 拦截登录表单提交验证请求
 */
public class CustomUsernamePasswordAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {

	public static final String SPRING_SECURITY_RESTFUL_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_RESTFUL_PASSWORD_KEY = "password";
	public static final String SPRING_SECURITY_RESTFUL_LOGIN_URL = "/user/systemLogin";
	private String usernameParameter = SPRING_SECURITY_RESTFUL_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_RESTFUL_PASSWORD_KEY;
	private String captchaCodeParemeter="captchaCode";
	private String sessionCaptchaCodeKey="validateCode";
	
	private boolean postOnly = true;

	Logger logger = Logger
			.getLogger(CustomUsernamePasswordAuthenticationFilter.class);

	public CustomUsernamePasswordAuthenticationFilter() {
		super(new AntPathRequestMatcher(SPRING_SECURITY_RESTFUL_LOGIN_URL,
				"POST"));
		logger.info("初始化自定义认证处理器");
	}

	public Authentication attemptAuthentication(HttpServletRequest request,

	HttpServletResponse response) throws AuthenticationException,

	IOException, ServletException {
		if (postOnly && !request.getMethod().equals("POST")) {

			throw new AuthenticationServiceException(
					"Authentication method not supported: "
							+ request.getMethod());
		}
		logger.info("===自定义认证处理器===");
		String username = obtainUsername(request);
		String password = obtainPassword(request);
        String captchaCode = this.obtainCaptchaCode(request); //输入验证码
        String captchaSessionCode = this.obtainSessionCaptchaCode(request); //session验证码
		if (username == null) {
			username = "";
		}
		if (password == null) {
			password = "";
		}
		HttpSession session = request.getSession();
		Integer tryCount = (Integer) session.getAttribute("tryErrorCount");
		if(tryCount != null && tryCount>=3){
			 logger.info("开始校验验证码，生成的验证码为："+captchaSessionCode+" ，输入的验证码为："+captchaCode);  
				if(captchaSessionCode == null || "".equals(captchaSessionCode)){
					throw new CaptchaException(this.messages.getMessage("LoginAuthentication.captchaError"));
				}
				if(!captchaSessionCode.equalsIgnoreCase(captchaCode)){  
		            throw new CaptchaException(this.messages.getMessage("LoginAuthentication.captchaError"));  
		        }  
		}
		username = username.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);
		setDetails(request, authRequest);//允许子类设置详细属性  
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}

	protected String obtainUsername(HttpServletRequest request) {

		return request.getParameter(usernameParameter);
	}
	
	protected String obtainCaptchaCode(HttpServletRequest request) {

		return request.getParameter(captchaCodeParemeter);
	}
	
	
	 protected String obtainSessionCaptchaCode (HttpServletRequest request){  
	       return (String)request.getSession().getAttribute(sessionCaptchaCodeKey);  
	   }  

	protected void setDetails(HttpServletRequest request,

	UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource
				.buildDetails(request));
	}

	public String getUsernameParameter() {

		return usernameParameter;
	}

	public void setUsernameParameter(String usernameParameter) {

		this.usernameParameter = usernameParameter;
	}

	public String getPasswordParameter() {

		return passwordParameter;
	}

	public void setPasswordParameter(String passwordParameter) {

		this.passwordParameter = passwordParameter;
	}

	public boolean isPostOnly() {

		return postOnly;
	}

	public void setPostOnly(boolean postOnly) {

		this.postOnly = postOnly;
	}

	public void setLoginUrl(String loginUrl) {

		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(
				loginUrl, "POST"));
	}
}

package com.nsw.wx.common.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.core.Constants;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wukang
 * @Copyright: www.nsw88.comb Inc. All rights reserved.
 * @date 2015年9月17日 下午5:11:53
 * @Description: 访问被拒绝处理操作
 */
@SuppressWarnings("all")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	Logger logger = Logger.getLogger(this.getClass());

	private String accessDeniedUrl;

	public CustomAccessDeniedHandler(String accessDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
		logger.info("访问拒绝跳转页面: " + accessDeniedUrl);
	}

	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		logger.info("===" + accessDeniedException.getMessage()
				+ " info:你没有权限访问此页面!" + request.getRequestURI());
		if (AjaxUtil.isAjax(request)) {
			/**
			 * response.setContentType("text/html; charset=UTF-8"); Map map=new
			 * HashMap<>(); map.put("type","accessDenied");
			 * map.put("message","对不起,您没有权限访问此页面！"); AjaxUtil.rendJson(response,
			 * false, map);
			 */
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/plain");
			response.getWriter().write("权限不足,访问被拒绝！");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().close();
		} else if (!response.isCommitted()) {//可检查看服务端是否已将数据输出到客户端.如果返回值是TRUE则已将数据输出到客户端,是FALSE则还没有
			if (!StringUtils.isEmpty(accessDeniedUrl)) {
				request.setAttribute(WebAttributes.ACCESS_DENIED_403,
						accessDeniedException.getMessage());
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				RequestDispatcher dispatcher = request
						.getRequestDispatcher(accessDeniedUrl);
				String deniedMessage = accessDeniedException.getMessage();
				request.getSession().setAttribute("deniedMessage",
						deniedMessage);
				dispatcher.forward(request, response); // 地址栏不会变(应该使用这种方式)
				//response.sendRedirect(request.getContextPath()+accessDeniedUrl);
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN,
						accessDeniedException.getMessage());
			}
		}
	}
	

	public void setAccessDeniedUrl(String accessDeniedUrl) {
		if ((accessDeniedUrl != null) && !accessDeniedUrl.startsWith("/")) {
			throw new IllegalArgumentException("错误页面必须以 '/'开始");
		}
		this.accessDeniedUrl = accessDeniedUrl;
	}
}

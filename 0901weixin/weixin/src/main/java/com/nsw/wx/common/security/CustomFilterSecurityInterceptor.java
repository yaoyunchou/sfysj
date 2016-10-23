package com.nsw.wx.common.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved. 
 * @date 2015年9月28日 下午3:11:51
 * @Description: Spring security核心过滤器
 */
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter{

	public static Logger logger = Logger.getLogger(CustomFilterSecurityInterceptor.class);
  
	private FilterInvocationSecurityMetadataSource securityMetadataSource;  
	
    public void destroy() {  
    
    }  
	
    /**
	 * @Description: 拦截请求 
	 * @param @param password
	 * @param @return   
	 * @return String  
	 * @throws
	 */
    public void doFilter(ServletRequest request, ServletResponse response,  
            FilterChain chain) throws IOException, ServletException {  
//    	HttpServletRequest httpRequest=(HttpServletRequest) request;
//    	HttpServletResponse httpResponse = (HttpServletResponse) response;
//    	httpResponse.setHeader("Cache-Control", "no-cache");  
//    	httpResponse.setHeader("Pragma", "no-cache");  
//    	httpResponse.setDateHeader("Expires", 0);
    	//logger.info("开始拦截,请求地址：" + httpRequest.getRequestURL()+System.currentTimeMillis());
        FilterInvocation fi = new FilterInvocation(request, response, chain);  
        invoke(fi);
    }  
   
    public void invoke(FilterInvocation fi) throws IOException, ServletException {    
      InterceptorStatusToken token = super.beforeInvocation(fi);   
      
      try {     
        fi.getChain().doFilter(fi.getRequest(), fi.getResponse());  
      } finally {    
        super.afterInvocation(token, null);  
      }  
    } 
    
    
    public void init(FilterConfig arg0) throws ServletException {  
    	 logger.info(this.getClass()+"初始化Spring security核心过滤器");
    }  
  
    
    @Override  
    public Class<? extends Object> getSecureObjectClass() {  
        //下面的MyAccessDecisionManager的supports方面必须放回true,否则会提醒类型错误    
        return FilterInvocation.class;  
    }  

  
    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {  
        return securityMetadataSource;  
    }  
  
    public void setSecurityMetadataSource(  
            FilterInvocationSecurityMetadataSource securityMetadataSource) {  
        this.securityMetadataSource = securityMetadataSource;  
    }  
   
    @Override  
    public SecurityMetadataSource obtainSecurityMetadataSource() {  
        return this.securityMetadataSource;  
    }  
  
}

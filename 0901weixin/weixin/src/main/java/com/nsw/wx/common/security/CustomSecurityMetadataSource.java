package com.nsw.wx.common.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.validator.Resources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;


/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月24日 上午10:32:01
 * @Description: 负责读取数据库中的url对应的权限
 */
@SuppressWarnings("all")
public class CustomSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	public static Logger logger = Logger.getLogger(CustomSecurityMetadataSource.class);

	public static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	
	public static  Set<HashMap> resourcesList = null;

//	public static List<Resource> getResourcesList() {
//		return resourcesList;
//	}
//
//	public static void setResourcesList(List<Resource> resourcesList) {
//		CustomSecurityMetadataSource.resourcesList = resourcesList;
//	}

	public static Map<String, Collection<ConfigAttribute>> getResourceMap() {
		return resourceMap;
	}

	public static void setResourceMap(
			Map<String, Collection<ConfigAttribute>> resourceMap) {
		CustomSecurityMetadataSource.resourceMap = resourceMap;
	}

	// RegexUrlPathMatcher默认不进行小写转换，而AntUrlPathMatcher默认要进行小写转换
	private RequestMatcher pathMatcher;
	
	 PathMatcher matcher = new AntPathMatcher();  

	private ResourceService resourceService;

	public CustomSecurityMetadataSource(ResourceService resourceService) {
		this.resourceService = resourceService;
		logger.info("====系统启动加载系统权限====");
		loadResourceDefine();
	}
	
	public CustomSecurityMetadataSource() {
		
	}
	
	
	// 加载所有资源与权限的关系
	public void loadResourceDefine() {

		if (resourceMap == null) {
		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		//如果没有开启权限，就不加载权限资源
		 Properties prop =  new  Properties();    
	     ClassPathResource resources=new ClassPathResource("application.properties");
	     try {
			prop.load(resources.getInputStream());
			String permissionstatus = prop.getProperty("permissionstatus");
			if("close".equals(permissionstatus)){
				logger.info("****permissionstatus="+permissionstatus+",没有开启权限，不加载资源");
				return ;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("****读取配置文件出错****"+e.getMessage());
		}
		 resourcesList = this.resourceService.getAll();
		for (Map resource : resourcesList) {
			Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
			ConfigAttribute configAttribute = null;
			if (!StringUtils.isEmpty(resource.get("code"))) {//关联表中资源对应的角色不存在
				if(resource.get("code") instanceof Collection){
					Set<String> codes = (Set<String>) resource.get("code");
					for(String code:codes){
						configAttribute = new SecurityConfig(code);
						configAttributes.add(configAttribute);
					}
				}else{
					configAttribute = new SecurityConfig(resource.get("code")+"");
					configAttributes.add(configAttribute);
				}
				
			} else {
				configAttribute = new SecurityConfig("ROLE_ANONYMOUS");
				configAttributes.add(configAttribute);
			}
			//url过滤掉后面的#/  以免匹配不准确
			String url = resource.get("url")+"";
			String method = resource.get("method")+"";
			if(url != null ){
				if(url.contains("#")){
					String temp = url.substring(0, url.indexOf("#"));
//					if(Constants.INFOMENU.equals(temp)||Constants.PRODUCTMENU.equals(temp)){//如果是文章或产品链接，不加载到资源匹配池，因为已经对后端url做了过滤
//						continue;
//					}
					url = url.substring(0, url.indexOf("#"))+"#method="+method;
				}else{
					url = url + "#method="+method;
				}
			}
			
			resourceMap.put(url, configAttributes);
		}
		logger.info("========加载权限与资源的对用关系=======" + resourceMap);
		for (String key : resourceMap.keySet()) {
			logger.info("key= " + key + " and value= "
					+ resourceMap.get(key));
		}
		}
	}

	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		   	   String urI = ((FilterInvocation) object).getHttpRequest().getRequestURI()+"";
		   	   urI=urI.replaceAll("//", "/");
			   String contextPath= ((FilterInvocation) object).getRequest().getContextPath();
			   String requestMethod=((FilterInvocation) object).getHttpRequest().getMethod();
			   urI=urI.replace(contextPath, "");
			//   logger.info("根据"+urI+ " 从Map中取出对应的权限！");
			  // logger.info("转换为urI: "+urI+"#method="+requestMethod.toLowerCase());
			   Iterator<String> it = resourceMap.keySet().iterator();
				while (it.hasNext()) {
					String resURL = it.next();
					//pathMatcher = new AntPathRequestMatcher(resURL);
					//if (pathMatcher.matches(((FilterInvocation) object).getRequest())) {
					    String[] resURLS=resURL.split("#method=");
					    String prefix= resURLS[0];
					    // if(prefix .equals("/tpl/page/add")){
					    //	System.out.println(resURL);
					    //}
					    
					    String suffix="";
					    if(resURLS.length>1){
					    	suffix=resURLS[1];
					    }
						if(matcher.match(prefix, urI) && requestMethod.equalsIgnoreCase(suffix)){//url相同并且提交方式相同
						Collection<ConfigAttribute> returnCollection = resourceMap
								.get(resURL);
						return returnCollection;
					}
				}
				//如果上面的两个url地址没有匹配，返回null，不再调用MyAccessDecisionManager类里的decide方法进行权限验证，代表允许访问页面
				//return null;
				return null ;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<ConfigAttribute>();
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

}

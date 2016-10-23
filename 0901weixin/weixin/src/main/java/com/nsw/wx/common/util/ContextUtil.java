package com.nsw.wx.common.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.nsw.wx.common.security.CustomSecurityUser;
import com.nsw.wx.common.security.SpringSecurityUtils;


/**
 * @author Aaron
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年8月17日 下午7:40:48
 * @Description: spring上下文工具类
 */
@Component
public class ContextUtil implements ApplicationContextAware {

	static Logger logger = Logger.getLogger(ContextUtil.class);

	@Value("${user.session.key}")
	public static String USER_SESSION_KEY = "user";
	
	public static final String BASE_PATH = "sysBasePath";

	public static String moduleId = "";

	/**
	 * @Fields appCtx : 上下文对象
	 */
	private static ApplicationContext appCtx;

	/*
	 * @Description: 此方法可以把ApplicationContext对象inject到当前类中作为一个静态成员变量
	 * 
	 * @param applicationContext
	 * 
	 * @throws BeansException
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appCtx = applicationContext;

	}

	/**
	 * @Description: 获取上下文对象
	 * @param @return
	 * @return ApplicationContext
	 * @throws
	 */
	public static ApplicationContext getApplicationContext() {
		return appCtx;
	}

	/**
	 * @Description: 获取bean
	 * @param @param beanName
	 * @param @return
	 * @return Object
	 * @throws
	 */
	public static Object getBean(String beanName) {
		return appCtx.getBean(beanName);
	}

	/**
	 * @Description: 获取环境属性，包括配置文件的属性
	 * @param @param key
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getProperty(String key) {
		logger.info(">>>>" + appCtx.getEnvironment().getProperty(key));
		return appCtx.getEnvironment().getProperty(key);
	}

	/**
	 * @Description: 封装带contextpath的url
	 * @param @param url
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String wrapUrl(String url) {
		url = url.indexOf(Constants.LEFT_SPRIT) == 0 ? url
				: Constants.LEFT_SPRIT + url;
		return ContextUtil.getProperty("server.context-path") + url;
	}

	/**
	 * 获取项目根路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request) {
		return request.getScheme() + Constants.COLON
				+ Constants.DOUBLE_LEFT_SPRIT + request.getServerName()
				+ Constants.COLON + request.getServerPort()
				+ request.getContextPath();
	}
	
	
	/***
	 * 获取当前登录用户
	* @Description: TODO 
	* @param @return   
	* @return CustomSecurityUser  
	* @throws
	 */
	public static CustomSecurityUser getLoginUser(){
		CustomSecurityUser user = SpringSecurityUtils.getCurrentLoginUser();
		return user;
	}

	
	
	/***
	 * 获取当前登录用户ID
	* @Description: TODO 
	* @param @return   
	* @return CustomSecurityUser  
	* @throws
	 */
	public static String getUserId(){
		if(getLoginUser() !=null){
			return getLoginUser().getId().toString();
		}
		return "";
	}
	
	
	/***
	 * 获取公有项目ID
	 * @Description: TODO 
	 * @param @return   
	 * @return String  
	 * @throws
	 */
	public static String getPubProject(String projType) {
		if(projType == null ||"".equals(projType)){
			projType =  ContextUtil.getProjectType();
		}
		if(Constants.PC_PROJ_TYPE.equals(projType)){
			return Constants.PUB_PC_PROJ_ID;
		}else if(Constants.MB_PROJ_TYPE.equals(projType)){
			return Constants.PUB_MB_PROJ_ID;
		}else if(Constants.RS_PROJ_TYPE.equals(projType)){
			return Constants.PUB_RS_PROJ_ID;
		}else{
			return "26655_PC";
		}
	}

	/***
	 * 获取当前站点信息
	 * @Description: TODO 
	 * @param @return   
	 * @return String  
	 * @throws
	 */
	public static String getSite() {
		if (getLoginUser() != null) {
			return getLoginUser().getSite();
		}
		return "27310";
//		return "26672";
//		return "26337"; 
	}
	
	/***
	 * 获取当前项目类型
	 * @Description: TODO 
	 * @param @return   
	 * @return String  
	 * @throws
	 */
	public static String getProjectType() {
		if (getLoginUser() != null) {
			return getLoginUser().getProjectType();
		}
		return "4";
	}



	public String getClassPath() {
		return this.getClass().getResource("/").getPath();
	}

	/** 
	* @Description: 获取发布后classpath路径
	* @param @param filePath 如resources下面的tplHtmlRules.xml文件/tplHtmlRules.xml
	* @param @return   
	* @return String  
	* @throws 
	*/ 
	public static String getClassPath(String filePath) {
		return ContextUtil.class.getResource(filePath).getPath();
	}

}

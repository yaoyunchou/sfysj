package com.nsw.wx.common.security;

import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved. 
 * @date 2015年10月9日 下午2:38:45
 * @Description: 加载异常消息资源文件
 */
//@Component
public class SpringMessageSource extends ReloadableResourceBundleMessageSource {
	
	Logger logger = Logger.getLogger(SpringMessageSource.class);
	
	 public SpringMessageSource() {  
		    logger.info("===加载资源文件==");
	        //setBasename("com.wukang.resources.messages_zh_CN");//ResourceBundleMessageSource  
		    setBasename("classpath:messages");//ReloadableResourceBundleMessageSource
		    setDefaultEncoding("UTF-8");
		    setFallbackToSystemLocale(true);
		    setUseCodeAsDefaultMessage(true);
		    setCacheSeconds(100);
	    }  
	  
	    public static MessageSourceAccessor getAccessor() {  
	        return new MessageSourceAccessor(new SpringMessageSource());  
	    }  
	    
	    //用法
	   /*private MessageSourceAccessor messages = SpringMessageSource.getAccessor();
	    String message=messages.getMessage("PasswordComparisonAuthenticator.badCredentials");*/
}

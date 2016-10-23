package com.nsw.wx.common.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.MultipartFilter;

import com.nsw.wx.common.filter.CustomMultipartResolver;
import com.nsw.wx.common.util.Constants;
/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月17日 上午8:30:39
* @Description: 重写文件上传配置
*/
@Configuration
public class MultipartConfig extends MultipartAutoConfiguration {
	
	public Logger logger = Logger.getLogger(MultipartConfig.class); 
	
	@Value("${dy.multipart.maxFileSize}")
	private int uploadFileSize;
	
	/** 
	* @Description: 设置文件上传参数 
	* @param @return   
	* @return CustomMultipartResolver  
	* @throws 
	*/ 
	@Bean
    public CustomMultipartResolver customMultipartResolver() {
        final CustomMultipartResolver customMultiResolver = new CustomMultipartResolver();
        customMultiResolver.setMaxUploadSize(Constants.INT_NEGATIVE_ONE);
        //解决CommonsMultipartFile报错：File has been moved
        customMultiResolver.setMaxInMemorySize(uploadFileSize);
        return customMultiResolver;
    }

    /** 
    * @Description: 注入multipart类型文件处理 
    * @param @return   
    * @return FilterRegistrationBean  
    * @throws 
    */ 
    @Bean
    public FilterRegistrationBean multipartFilterRegistrationBean() {
        final MultipartFilter multipartFilter = new MultipartFilter();
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(multipartFilter); 
        filterRegistrationBean.addInitParameter("multipartResolverBeanName", "customMultipartResolver");
        return filterRegistrationBean;
    }
}

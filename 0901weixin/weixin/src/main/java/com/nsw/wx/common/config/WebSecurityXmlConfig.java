package com.nsw.wx.common.config;

/**
 @author wukang
 @Copyright: www.nsw88.com Inc. All rights reserved.
 @date 2015年9月20日 下午1:02:10
 @Description: spring security Xml配置加载类
 **/
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@ImportResource("classpath:spring-security-cas.xml")
@SpringBootApplication
public class WebSecurityXmlConfig extends WebSecurityConfigurerAdapter {

	Logger logger = Logger.getLogger(WebSecurityXmlConfig.class);

	public void configure(WebSecurity web) throws Exception {
		// 设置不拦截规则 
				web.expressionHandler(webSecurityExpressionHandler())
						.ignoring()
						.antMatchers("/**/*.js", "/**/*.png", "/**/*.gif",
								"/**/*.jpg", "/plugin/**/*","/**/*.jsp",
								"/**/*.swf","/**/js/**/*","/img/*","/**/images/*");
	}

	public void configure(HttpSecurity http) throws Exception {
		//不要过滤图片等静态资源，其中**代表可以跨越目录，*不可以跨越目录
		 String [] ignorePattern = new String[]
		 {"/**/*.css","/**/*.js","/**/*.png","/**/*.gif","/**/images/**"};
				http.antMatcher("/**")
						.authorizeRequests()
						.expressionHandler(webSecurityExpressionHandler())
						.antMatchers(ignorePattern).permitAll()
						.anyRequest().authenticated();
	}

	public void configure(AuthenticationManagerBuilder auth) throws Exception {

	}
	
	@Bean
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
		DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		return webSecurityExpressionHandler;
	}
}

package com.nsw.wx.common.security;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved. 
 * @date 2015年10月16日 下午1:17:53
 * @Description: 定时加载资源权限关系放入内存中
 */
//@Component
//@Configurable
//@EnableScheduling
public class MetadataSourceTaskScheduled {
//	
//	public static Logger logger = Logger.getLogger(MetadataSourceTaskScheduled.class);
//	
//	@Autowired
//	private ResourceService resourceService;
//
//	@Scheduled(cron="0 0/30 22-23 * * ?")//早上九点到晚上七点工作时间内每半小时 执行一次
//	public void run(){
//		
//		logger.info("刷新资源列表"+dateFormat().format(new Date()));
//		CustomSecurityMetadataSource customSecurityMetadataSource=
//				new CustomSecurityMetadataSource(resourceService);
//		customSecurityMetadataSource.loadResourceDefine();
//		
//	}
//	
//	 private SimpleDateFormat dateFormat(){
//		    return new SimpleDateFormat ("HH:mm:ss");
//		}

}

package com.nsw.wx.common.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.FancService;
import com.nsw.wx.common.service.OpenCommonService;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月25日 下午9:34:30
* @Description: 用户删除公众号，清除公众号信息JOB
 */
public class DeletetAccountDataJob implements InterruptableJob {
	
	private Logger logger = Logger.getLogger(DeletetAccountDataJob.class);
	
	private JobKey jobKey = null;
	
	private volatile Thread thisThread;
	
	@Autowired
	private OpenCommonService openCommonService =(OpenCommonService) ContextUtil.getBean("openCommonService");
	
	UserService userService = new UserServiceImp();
	
	@Autowired
	private WxAccountService wxAccountService =  (WxAccountService) ContextUtil.getBean("wxAccountService");
	@Autowired
	private FancService fancService =  (FancService) ContextUtil.getBean("fancService");
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		thisThread = Thread.currentThread();
		jobKey = context.getJobDetail().getKey();
		logger.info("取消绑定公众号，清除公众号信息");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String  appId =  dataMap.getString("appId");
		//1.删除菜单信息 
		//2.删除素材信息   图片  图文
		//3.删除粉丝  粉丝组 信息  
		//4.删除 关键词管理信息
		//5.删除群发信息
		openCommonService.deleteAccountInfo(appId);
		logger.info("公众号取消绑定清除信息成功");
		
	}
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		logger.info("Job " + jobKey + "  -- INTERRUPTING --");
           thisThread.interrupt();
	}

}

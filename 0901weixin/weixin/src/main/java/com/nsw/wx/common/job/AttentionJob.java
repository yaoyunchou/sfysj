package com.nsw.wx.common.job;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.api.util.WxUtil;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.FancService;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;
/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月30日 上午9:52:40
* @Description: 关注事件处理（粉丝信息）
 */
public class AttentionJob implements InterruptableJob {
	
	private Logger logger = Logger.getLogger(AttentionJob.class);
	
	private JobKey jobKey = null;
	
	private volatile Thread thisThread;
	
	@Autowired
	private FancService fancService  = (FancService) ContextUtil.getBean("fancService");

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		thisThread = Thread.currentThread();
		jobKey = context.getJobDetail().getKey();
		logger.info("关注事件处理job");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String  appId =  dataMap.getString("appId");
		String openId =  dataMap.getString("openid");
		String event =  dataMap.getString("event");
		
		//取消关注
		if(WxUtil.EVENT_TYPE_UNSUBSCRIBE.equals(event)){
			//从粉丝表里删除粉丝信息
			//logger.info("用户取消关注");
			fancService.delFans(appId, openId);
		}else if(WxUtil.EVENT_TYPE_SUBSCRIBE.equals(event)){//关注
			//1.从粉丝表里加入粉丝信息
			fancService.addFans(appId, openId);
		}
		
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		// TODO Auto-generated method stub
		logger.info("Job " + jobKey + "  -- INTERRUPTING --");
        thisThread.interrupt();
	}

}

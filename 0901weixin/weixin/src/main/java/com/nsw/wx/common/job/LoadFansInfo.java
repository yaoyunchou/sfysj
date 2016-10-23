package com.nsw.wx.common.job;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import com.nsw.wx.common.service.FancService;
import com.nsw.wx.common.util.ContextUtil;

public class LoadFansInfo implements InterruptableJob {
	private Logger logger = Logger.getLogger(LoadFansInfo.class);
	
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
		logger.info("拉取粉丝信息job");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String  appId =  dataMap.getString("appId");
		//fancService.loadFansGroup(appId);
		fancService.loadFansList(appId,"");
		fancService.loadFansTags(appId);
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		// TODO Auto-generated method stub
		logger.info("Job " + jobKey + "  -- INTERRUPTING --");
        thisThread.interrupt();
	}

}

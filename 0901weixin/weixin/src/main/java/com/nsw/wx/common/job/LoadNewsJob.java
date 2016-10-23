package com.nsw.wx.common.job;

import com.nsw.wx.common.service.WxMaterialService;
import com.nsw.wx.common.util.ContextUtil;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liuzp on 2016/8/26.
 */
public class LoadNewsJob implements InterruptableJob {
	private Logger logger = Logger.getLogger(LoadNewsJob.class);


	private JobKey jobKey = null;

	private volatile Thread thisThread;

	@Autowired
	private WxMaterialService wxMaterialService = (WxMaterialService) ContextUtil.getBean("wxMaterialService");

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		logger.info("Job " + jobKey + "  -- INTERRUPTING --");
		thisThread.interrupt();
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		thisThread = Thread.currentThread();
		jobKey = context.getJobDetail().getKey();
		logger.info("同步图文job");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String  appId =  dataMap.getString("appId");
		try {
			logger.info(appId+" SyncWxMaterial news start...");
			wxMaterialService.SyncWxMaterial(appId,"news");
			logger.info(appId+" SyncWxMaterial news end...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

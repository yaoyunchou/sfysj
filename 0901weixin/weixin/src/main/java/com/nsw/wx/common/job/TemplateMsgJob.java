package com.nsw.wx.common.job;

import com.nsw.wx.common.docmodel.TemplateMsg;
import com.nsw.wx.common.service.TemplateMsgService;
import com.nsw.wx.common.util.ContextUtil;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liuzp on 2016/8/24.
 * 模板消息job
 */
public class TemplateMsgJob implements Job {
	private Logger logger = Logger.getLogger(TemplateMsgJob.class);

	private JobKey jobKey = null;

	@Autowired
	private TemplateMsgService service  = (TemplateMsgService)ContextUtil.getBean("templateMsgService");

	private volatile Thread thisThread;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		thisThread = Thread.currentThread();
		jobKey = context.getJobDetail().getKey();
		logger.info("模板消息发送job");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String  appId = (String) dataMap.get("appId");
		String  templateId = (String) dataMap.get("templateMsgId");
		service.exeTemplateMsgJob(appId,templateId);
		logger.info("模板消息执行结束");
	}
}

package com.nsw.wx.common.job;

import java.util.Date;

import com.nsw.wx.common.docmodel.MassMessage;
import com.nsw.wx.common.service.SendMassMsgService;
import com.nsw.wx.common.util.ContextUtil;
import com.nsw.wx.common.views.Message;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

public class MassMsgJob implements Job{
	private Logger logger = Logger.getLogger(MassMsgJob.class);

	private JobKey jobKey = null;

	@Autowired
	private SendMassMsgService massService = (SendMassMsgService)ContextUtil.getBean("sendMassMsgService");

	private volatile Thread thisThread;
	 public MassMsgJob() {
	    }  

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		thisThread = Thread.currentThread();
		jobKey = context.getJobDetail().getKey();
		logger.info("定时群发任务job开始执行");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		MassMessage msg = (MassMessage) dataMap.get("massMsg");
		if(msg != null){
			Message message =  massService.sendMassMsgByJob(msg);
			String isSuccessStr = message.getIsSuc()==true?"成功":"失败";
			logger.info("定时任务群发" + isSuccessStr+"，jobId = "+msg.getJobId()+",jobTime = "+msg.getJobTime());
		}

	}

}

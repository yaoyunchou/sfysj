package com.nsw.wx.common.service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import com.nsw.wx.common.job.LoadNewsJob;
import com.nsw.wx.common.util.Constants;
import net.sf.json.JSONObject;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;
import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


@Service("newsService")
public class NewsServiceImp implements NewsService{

	@Override
	public JSONObject newsUpload(String appId, Map<String, Object> news) {
		// TODO Auto-generated method stub
		return null;
	}

	public void syncNews(String appId){
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			Date startTime = nextGivenSecondDate(null, 1);
			JobDataMap jobMap = new JobDataMap();
			jobMap.put("appId", appId);
			String taskId = UUID.randomUUID().toString().replace("-", "");
			JobDetail job = newJob(LoadNewsJob.class)
					.withIdentity(Constants.TASK_BINDGACCOUNTROUP + taskId,
							Constants.TASKTIME_GROUP + taskId)
					.setJobData(jobMap).build();
			SimpleTrigger trigger = newTrigger()
					.withIdentity(Constants.TASKTIME_TRIGGER + taskId,
							Constants.TASKTIME_GROUP + taskId)
					.startAt(startTime).withSchedule(simpleSchedule()).build();
			sched.scheduleJob(job, trigger);
			sched.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package com.nsw.wx.common.job;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.nsw.wx.common.service.FancService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;


/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年6月15日 上午11:49:37
* @Description: 定时同步认证公众号粉丝信息
 */
@Component
@Configurable
@EnableScheduling
public class SyncFansInfoJob {
	
	
private Logger logger = Logger.getLogger(SyncFansInfoJob.class);
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	UserService userService = new UserServiceImp();
	
	@Value("${appid}")
	private  String TOKENPREFIX ;

	@Autowired
	private FancService fancService;


	
	/**
	 *每隔一小时检测公众号粉丝信息是否一致
	 * 暂时关闭检测，以便和产品经理沟通 是否开同粉丝同步功能
	 */
	@Scheduled(cron = "0 0/30 * * * ? ")
	public void SyncFansInfo(){

		logger.error("检测同步粉丝定时任务开始...");
		long  start = new Date().getTime();
		ArrayList arr = new ArrayList<>();
		arr.add(2);
		arr.add(3);
    	Query que = new Query(Criteria.where("type").in(arr));
        List<Map<String,Object>> entityList =  baseMongoTemplate.queryMulti(que, Constants.WXACCOUNT_T);
      	for(Map<String,Object> m: entityList){
      		//1.认证的公众号才同步检测粉丝
      		if( Integer.parseInt(m.get("verify_type_info")+"") != -1){
      			try {
					String appId = m.get("appId")+"";
					Object cacheAccessToken =  redisTemplate.opsForValue().get(TOKENPREFIX+appId);
					//2.accessToken未失效的公众号才继续检测
					if(cacheAccessToken != null){
						fancService.syncTagAndFans(appId);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(m.get("appId")+"定时任务同步粉丝信息失败");
				}
      		}
      	}
		long  end = new Date().getTime();
		logger.error("检测同步粉丝定时任务结束。耗时秒："+ (end-start)/1000);
	}

}

package com.nsw.wx.common.job;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.data.mongodb.core.query.Update;
import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.ContextUtil;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月16日 下午3:29:21
* @Description: 微信后台绑定成功后，我们平台立即给它缓存token
 */
public class RefreshBindAcountJob implements InterruptableJob {
	
	private Logger logger = Logger.getLogger(RefreshBindAcountJob.class);
	
	private JobKey jobKey = null;
	
	private volatile Thread thisThread;
	
	
	UserService userService = new UserServiceImp();
	
	@Autowired
	private WxAccountService wxAccountService =  (WxAccountService) ContextUtil.getBean("wxAccountService");

	@Override
    @SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		thisThread = Thread.currentThread();
		jobKey = context.getJobDetail().getKey();
		logger.info("手动绑定微信验证成功,开始给绑定的公众号缓存token!" + jobKey + " executing at " + new Date());
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String  appId =  dataMap.getString("appId");
		
		Map<String,Object> account =   wxAccountService.getAccountByAppId(appId);
		AccessTokenHelper help = new AccessTokenHelper();
		JSONObject jsonobject =  help.getAccessToken(account.get("appId")+"", account.get("appSecret")+"");
		if(jsonobject!=null&&!jsonobject.containsKey("errcode") ){
			Update update = Update.update("authorizer_access_token", jsonobject.getString("access_token")).set("refresh_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			account.put("id", account.get("_id")+"");
			if(wxAccountService.updateAccount(account,update)){//更新token到数据库
				logger.info("缓存手动绑定的微信号"+appId+"成功!");
			}
		}
		
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		// TODO Auto-generated method stub
		logger.info("Job " + jobKey + "  -- INTERRUPTING --");
           thisThread.interrupt();
	}

}

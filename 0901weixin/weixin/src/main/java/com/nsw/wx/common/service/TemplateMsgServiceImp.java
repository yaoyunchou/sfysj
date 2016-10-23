package com.nsw.wx.common.service;

import com.nsw.wx.api.service.TempletService;
import com.nsw.wx.api.service.imp.TempletServiceImp;
import com.nsw.wx.api.util.JSONHelper;
import com.nsw.wx.common.docmodel.TemplateMsg;
import com.nsw.wx.common.job.TemplateMsgJob;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.QuartzScheduleMgr;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by liuzp on 2016/8/24.
 */
@Service("templateMsgService")
public class TemplateMsgServiceImp implements TemplateMsgService {

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	TempletService templetService  = new TempletServiceImp();

	@Autowired
	private AccessTokenService tokenService;


	private Logger logger = Logger.getLogger(TemplateMsgServiceImp.class);

	@Override
	public boolean update(Update update, String id) {
		try {
		Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
		return baseMongoTemplate.updateOne(query,update,Constants.WXTEMPLATEMSG_T);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Map<String, Object> add(TemplateMsg msg) {
		String jsonStr = JSONHelper.bean2json(msg);
		JSONObject jsonData = JSONObject.fromObject(jsonStr);
		if(jsonData.containsKey("id")){
			jsonData.remove("id");
		}
		Map<String, Object> entity =  baseMongoTemplate.save(Constants.WXTEMPLATEMSG_T,jsonData);
		if(entity != null){
			entity.put("id",entity.get("_id")+"");
		}
		return entity;
	}

	@Override
	public boolean startTemplateMsgJob(String  appId,String templateMsgId,String sendTime,boolean isJob) {

		try {

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String uuid = UUID.randomUUID().toString().replace("-","");
			Scheduler sched = QuartzScheduleMgr.getInstanceScheduler();
			Date runTime =  null;
			if(isJob){
				runTime =  df.parse(sendTime);
			}else{
				runTime = nextGivenSecondDate(null, 1);
			}
			JobDataMap jobMap = new JobDataMap();
			jobMap.put("templateMsgId", templateMsgId);
			jobMap.put("appId", appId);
			JobDetail job = newJob(TemplateMsgJob.class).withIdentity("job"+uuid, "templateMsg"+uuid).setJobData(jobMap).build();
			//创建一个触发器的名称
			Trigger trigger = newTrigger().withIdentity("trigger", "templateMsg"+uuid).startAt(runTime) .build();
			//设置调度相关的Job
			sched.scheduleJob(job, trigger);
			logger.info(job.getKey() + " will run at: " + runTime);
			//启动调度任务
			sched.start();
			logger.info("-------消息模板定时任务启动成功 ---------"+job.getKey());
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.error("-------消息模板定时任务启动失败 ---------");
			return false;
		} catch (ParseException e) {
			logger.error("-------消息模板定时任务启动失败  ---------");
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 取消定时任务
	 * @param templateMsgId
	 * @return
	 */
	public boolean cancleTemplateMsgJob(String templateMsgId) {

		try {
			Map<String,Object> query = new HashedMap();
			query.put("_id",new ObjectId(templateMsgId));
			Map<String,Object> entity =  baseMongoTemplate.findOne(Constants.WXTEMPLATEMSG_T,query);
			if(entity!=null){
				Query querys = new Query(Criteria.where("_id").is(new ObjectId(templateMsgId) ));
				Update update = new Update();
				update.set("sendStatus","pause");
				return  baseMongoTemplate.updateFirst(querys,update,Constants.WXTEMPLATEMSG_T);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public boolean exeTemplateMsgJob(String appId,String  templateMsgId) {
		Update update = new Update();
		try {
			Map<String,Object> query = new HashedMap();
			query.put("_id",new ObjectId(templateMsgId));
			Map<String,Object> entity =  baseMongoTemplate.findOne(Constants.WXTEMPLATEMSG_T,query);
			if(entity!=null && (boolean)entity.get("status")){
				logger.info("job状态:"+entity.get("sendStatus")+"");
				if("pause".equals(entity.get("sendStatus")+"") ){
					return true;
				}else if("ready".equals(entity.get("sendStatus")+"") ){
					Query query_ = new Query(Criteria.where("appId").is(appId));
					if(!"-100".equals(entity.get("groupId")+"") ){
						List<Integer> tagid_list = new ArrayList<>();
						tagid_list.add(Integer.parseInt(entity.get("groupId")+""));
						query_.addCriteria(Criteria.where("tagid_list").in(tagid_list));
					}
					List<HashMap> fansList =  baseMongoTemplate.findByQuery(query_,Constants.WXFANS_T);
					logger.info("========开始执行消息模板job,预计发送人数:"+fansList.size());
					String accessToken = tokenService.getAccessTokenByRedis(appId);
					int successCount = 0;
					int failCount = 0;
					String templateId = entity.get("templateId")+"";
					String url = entity.get("url")+"";
					JSONObject data = JSONObject.fromObject( entity.get("para"));
					JSONObject newData = new JSONObject();

					Iterator iterator = data.keys();
					while(iterator.hasNext()){
						String key = (String) iterator.next();
						String  value = data.getString(key);
						JSONObject values = new JSONObject();
						values.put("value",value);
						values.put("color","#173177");
						newData.put(key,values);
					}
					for(HashMap fans:fansList){
						if(failCount>50){//50个人没发送成功，可能是accesstoken超时或系统其他原因，直接跳过不执行
							break;
						}
						JSONObject jsonData = new JSONObject();
						jsonData.put("touser",fans.get("openid")+"");
						jsonData.put("template_id",templateId);
						jsonData.put("url",url);
						jsonData.put("topcolor","#FF0000");
						jsonData.put("data",newData);
						JSONObject jsonResult =  templetService.sendTempletMsg(accessToken,jsonData.toString(),appId);
						if(jsonResult.containsKey("errcode") && jsonResult.getInt("errcode") == 0){
							successCount++;
						}else{
							failCount++;
						}
					}
					if(successCount==0 && failCount>0){
						//执行失败
						update.set("sendStatus","fail");
					}else{
						update.set("sendStatus","success");
					}
					update.set("successCount",successCount);
					update.set("failCount",failCount);

					baseMongoTemplate.updateOne(new Query(Criteria.where("_id").is(new ObjectId(templateMsgId))),update,Constants.WXTEMPLATEMSG_T);
					logger.info("job执行完成，成功:"+successCount+",失败:"+failCount);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//执行失败
			update.set("sendStatus","fail");
			baseMongoTemplate.updateOne(new Query(Criteria.where("_id").is(new ObjectId(templateMsgId))),update,Constants.WXTEMPLATEMSG_T);
			return false;
		}

		return false;
	}


	@Override
	public boolean batchDelate(String[] ids) {
		try {
		ArrayList<ObjectId> list = new ArrayList<>();
		for(String id:ids){
			list.add(new ObjectId(id));
		}
		Query query = new Query(Criteria.where("_id").in(list));
		return baseMongoTemplate.removeByqyery(query,Constants.WXTEMPLATEMSG_T);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<HashMap> list(String appId, int pageNum, int pageSize, String filter) {
		filter = filter==null?"":filter;
		Query query = new Query(Criteria.where("appId").is(appId).and("title").regex(filter));
		query.skip((pageNum- 1) * pageSize).limit(pageSize);
		query.with(new Sort(Sort.Direction.DESC , "CreateTime"));
		List<HashMap> list =  baseMongoTemplate.findByQuery(query, Constants.WXTEMPLATEMSG_T);
		for(HashMap m:list){
			m.put("id",m.get("_id")+"");
		}
		return list;
	}
}

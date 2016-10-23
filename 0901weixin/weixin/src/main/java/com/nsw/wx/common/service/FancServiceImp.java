package com.nsw.wx.common.service;

import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.UserTagService;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.api.service.imp.UserTagServiceImp;
import com.nsw.wx.common.docmodel.FansTag;
import com.nsw.wx.common.job.LoadFansInfo;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月21日 下午9:00:48
* @Description: TODO
 */
@Service("fancService")
public class FancServiceImp implements FancService{

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired 
	private MongoTemplate mongoTemplate;
	
	
	@Autowired
	private FansTagService fansTagService;
	
	UserService userService = new UserServiceImp();
	
	UserTagService userTagservice = new UserTagServiceImp();
	final String TAGLIST = "tagList";


	@Autowired
	private RedisTemplate redisTemplate;
	
	
	@Autowired
	private WxAccountService wxAccountService ;
	
	private Logger logger = Logger.getLogger(FancServiceImp.class);
	
	@Autowired
	private AccessTokenService tokenService;


	long getFansCountByTag(String appId,String tagId){
		String accessToken = tokenService.getAccessTokenByRedis(appId);
		JSONObject groupJson = userTagservice.getFansByTag( accessToken, Integer.parseInt(tagId) ,"",  appId);
		if(groupJson.get("count")!=null && groupJson.get("next_openid")==null){
			return groupJson.getInt("count");
		}
		if( groupJson.get("count")==null){
			return -100;
		}
		int c = 0;
		while( groupJson.get("next_openid")!=null && !("".equals( groupJson.getString("next_openid")))){
			//关注者列表  里面存放的都是openid  我们需要根据openid查询到用户
			c += groupJson.getInt("count");
			groupJson = userTagservice.getFansByTag( accessToken, Integer.parseInt(tagId) ,groupJson.get("next_openid")+"",  appId);
		}
		return c;
	}

	@Override
	public boolean loadFansByTagId(String appId, String tagId) {
		//根据标签条件删除


		return false;
	}

	@Override
	public boolean loadFansGroup(String appId) {
		// TODO Auto-generated method stub
		
		//Map<String,Object> account =  wxAccountService.getAccountByAppId(appId);
		
		String accessToken = tokenService.getAccessTokenByRedis(appId);
		//1.将粉丝及分组信息更新到列表
		JSONObject groupJson = userService.getAllGroup(accessToken,appId);
		if(groupJson.containsKey("groups")){
			JSONArray arr = groupJson.getJSONArray("groups");
			//插入分组信息到数据库
			for(int i=0;i<arr.size();i++){
				JSONObject o = arr.getJSONObject(i);
				o.put("groupid", o.getInt("id"));
				o.remove("id");
				o.remove("count");
				o.put("appId", appId);
				//检查和删除：通过appId进行查找，找到匹配的删除掉
				Map<String,Object> delQue = new HashMap<String, Object>();
				delQue.put("appId", appId);
				delQue.put("groupid",  o.getInt("groupid"));
				if(baseMongoTemplate.count(Constants.WXFANSGROUP_T, delQue)<1){
					baseMongoTemplate.save(Constants.WXFANSGROUP_T, o);
				}
			}
			logger.info("拉取公众号分组信息成功!");
		}else{
			logger.info("拉取公众号分组信息失败!"+groupJson);
		}
		
		return false;
	}

	@Override
	public boolean loadFansList(String appId,String next_openid) {
		// TODO Auto-generated method stub
		try {
            String accessToken = tokenService.getAccessTokenByRedis(appId);
			//Map<String,Object> account =  wxAccountService.getAccountByAppId(appId);
			//if(account != null && Integer.parseInt(account.get("verify_type_info")+"") != -1 ){
				//检查和删除：通过appId进行查找，找到匹配的删除掉
				Map<String,Object> delQue = new HashMap<String, Object>();
				delQue.put("appId", appId);
				baseMongoTemplate.deleteData(Constants.WXFANS_T, delQue);
				//1.将粉丝及分组信息更新到列表
				JSONObject groupJson = userService.getUserList(accessToken, next_openid,appId);
				//System.out.println("groupJson size"+groupJson);
				while( groupJson.get("next_openid")!=null && !("".equals( groupJson.getString("next_openid")))){
					//关注者列表  里面存放的都是openid  我们需要根据openid查询到用户
					JSONArray userArr = groupJson.getJSONObject("data").getJSONArray("openid");
					for(int i=0;i<userArr.size();i++){
						String openid = userArr.getString(i);
						addFans(appId, openid);
					}
					groupJson =  userService.getUserList(accessToken, groupJson.get("next_openid")+"",appId);
				}
				if(groupJson.containsKey("errcode")){
					logger.info("拉取公众号粉丝信息失败!"+groupJson);
				}else{
					logger.info("拉取公众号粉丝信息成功!");
				}
				
				return true;
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            logger.info("拉取公众号粉丝信息失败!");
			return false;
		}

	//	return false;
	}

	@Override
	public boolean delFans(String appId, String openId) {
		// TODO Auto-generated method stub
		
		Map<String,Object> delQuery = new HashMap<String, Object>();
		delQuery.put("appId", appId);
		delQuery.put("openid", openId);
		Map<String,Object> delMsgQuery = new HashMap<String, Object>();
		delMsgQuery.put("appId", appId);
		delMsgQuery.put("fromUserName", openId);
		baseMongoTemplate.deleteData(Constants.WXMESSAGE_T, delMsgQuery);
		return baseMongoTemplate.deleteData(Constants.WXFANS_T, delQuery);
		
	}

	@Override
	public boolean addFans(String appId, String openId) {
		// TODO Auto-generated method stub
		UserService userService = new UserServiceImp();
		//Map<String,Object> account =  wxAccountService.getAccountByAppId(appId);
		//如果是认证的才加入粉丝信息
        String accessToken = tokenService.getAccessTokenByRedis(appId);
       // if(account!=null && Integer.parseInt(account.get("verify_type_info")+"") != -1){
			JSONObject jsonResult =  userService.getUserInfo(accessToken, openId,appId);
			if(!jsonResult.containsKey("errcode")){
				jsonResult.put("appId", appId);
				Map<String,Object> query_ = new HashMap<String, Object>();
				query_.put("appId", appId);
				query_.put("openid", openId);
				if(baseMongoTemplate.count(Constants.WXFANS_T, query_)<1){
					if( baseMongoTemplate.save(Constants.WXFANS_T, jsonResult) !=null){
						return true;
					}
				}else{//如果已经存在了删除后再添加
					baseMongoTemplate.deleteData(Constants.WXFANS_T, query_);
					baseMongoTemplate.save(Constants.WXFANS_T, jsonResult);
				}
			}
		//}
		return false;
	}

	@Override
	public long getFansCountByGroupId(String appId,  String groupId) {
		// TODO Auto-generated method stub
		
		try {
			Map<String,Object> query = new HashMap<>();
			query.put("appId", appId);
			if(groupId != null && groupId.trim().length() > 0){
				query.put("groupid", Integer.parseInt(groupId));
			}
			return baseMongoTemplate.count(Constants.WXFANS_T, query);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}


	public long getFansCountByTagId(String appId, String tagId){

		try {
			Query query = new Query(Criteria.where("appId").is(appId));
			ArrayList<Integer> list = new ArrayList<>();
			if(!"-100".equals(tagId)){
				list.add(Integer.parseInt(tagId) );
				query.addCriteria(Criteria.where("tagid_list").in(list));
			}
			return baseMongoTemplate.findCountByQuery(query,Constants.WXFANS_T);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}


	@Override
	public Map<String, Object> getFans(String appId, String openId) {
		// TODO Auto-generated method stub
		Map<String,Object> query = new HashMap<>();
		query.put("appId", appId);
		query.put("openid", openId);
		Map<String,Object> fans =  baseMongoTemplate.findOne(Constants.WXFANS_T, query);
		return fans;
	}

	@Override
	public boolean loadFansTags(String appId) {
		// TODO Auto-generated method stub
		String accessToken = tokenService.getAccessTokenByRedis(appId);
		JSONObject json =  userTagservice.getTagList(accessToken, appId);
		if(!json.containsKey("errcode")){
			//插入前数据清除
			fansTagService.delAllTag(appId);
			JSONArray arr = json.getJSONArray("tags");
			for(int i = 0;i < arr.size(); i++){
				JSONObject obj = arr.getJSONObject(i);
				FansTag fansTag = new FansTag();
				fansTag.setTagId(obj.getInt("id"));
				fansTag.setName(obj.getString("name"));
				fansTag.setCount(obj.getInt("count"));
				fansTag.setAppId(appId);
				fansTagService.addTag(fansTag);
			}
			return true;
		}
		return false;
	}

	@Override
	public Map<String,Object> getFansByTag(String appId, String tagId,String filter,
			String pageNum, String pageSize) {
		// TODO Auto-generated method stub
		int pageNum_ = pageNum==null?1:Integer.parseInt(pageNum);
	    int pageSize_  = pageSize==null?20:Integer.parseInt(pageSize);
	    long totalPages=0;//总页数
	    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    ArrayList<Integer> list = new ArrayList<Integer>(); 
	    if(!StringUtils.isEmpty(tagId) && Integer.parseInt(tagId) != 10000){
	    	list.add(Integer.parseInt(tagId));
	    }
		 Query query = new Query(Criteria.where("appId").is(appId).and("groupid").ne(1));
		 if(!StringUtils.isEmpty(filter)){
			 Criteria cr = new Criteria().orOperator(Criteria.where("nickname").regex(filter), Criteria.where("remark").regex(filter));
			 query.addCriteria(cr);
	     } else{
	     	if(!StringUtils.isEmpty(tagId)){
				if(list.size()>0){
					query.addCriteria(Criteria.where("tagid_list").in(list));
				}else{
					query.addCriteria(Criteria.where("tagid_list").size(0));
				}
			}

	     }
		 long totalRows= mongoTemplate.count( query,Constants.WXFANS_T);//总记录数
	     if ((totalRows % pageSize_) == 0) {
	            totalPages = totalRows / pageSize_;
	     } else {
	            totalPages = totalRows / pageSize_ + 1;
	     }
	     query.skip((pageNum_ - 1) * pageSize_).limit(pageSize_);
		 query.with(new Sort(Direction.DESC , "subscribe_time"));
		 List<HashMap> listData   =  baseMongoTemplate.findByQuery(query, Constants.WXFANS_T);
		 for(HashMap ma:listData){
		 	 //如果有标签的话，找出对应的标签并重新封装
			 ArrayList<Integer>   tagidList = (ArrayList<Integer>) ma.get("tagid_list");
			 if(tagidList.size() > 0){
			 	List<Map<String,Object>> tagListMap = new ArrayList<>();
			 	for(Integer t:tagidList){
				 	FansTag tag  = fansTagService.getTagById(t,appId);
					if(tag != null){
						Map<String,Object> map = new HashMap<>();
						map.put("name",tag.getName());
						map.put("tagId",tag.getTagId());
						tagListMap.add(map);
					}
				}
				 ma.put("tagid_list",tagListMap);
			 }
			 ma.put("id", ma.get("_id")+"");
		 	 ma.put("subscribe_time", df.format(new Date( Long.parseLong(ma.get("subscribe_time")+"") *1000 )) );
		 }
		 Map<String,Object>  result = new HashMap<>();
		 result.put("dataList", listData);
		 result.put("totalRows", totalRows);
		 result.put("totalPages", totalPages);
		return result;
	}
	
	
	public List<HashMap> getFansByTag(String appId, String tagId){
		 ArrayList<Integer> list = new ArrayList<Integer>(); 
		 list.add(Integer.parseInt(tagId));
		 Query query = new Query(Criteria.where("appId").is(appId).and("tagid_list").in(list));
		 List<HashMap> listData   =  baseMongoTemplate.findByQuery(query, Constants.WXFANS_T);
		 return listData == null ? new ArrayList<HashMap>():listData;
	}

	@Override
	public Map<String, Object> getFansByOpenId(String appId, String openId) {
		// TODO Auto-generated method stub
		Map<String,Object> query  = new HashMap<String, Object>();
		query.put("appId", appId);
		query.put("openid", openId);
		return baseMongoTemplate.findOne(Constants.WXFANS_T, query);
	}

	@Override
	public Map<String, Object> getFansCount(String appId) {
		// TODO Auto-generated method stub
		 Map<String, Object> result = null;
		try {
			Query query = new Query(Criteria.where("appId").is(appId).and("groupid").is(1));
			 long hmdNum =  baseMongoTemplate.findCountByQuery(query,Constants.WXFANS_T);
			 Query queryAll = new Query(Criteria.where("appId").is(appId).and("groupid").ne(1));
			 long allNum = baseMongoTemplate.findCountByQuery(queryAll,Constants.WXFANS_T);
			 result = new HashMap<String, Object>();
			 result.put("hmdNum", hmdNum);
			 result.put("allNum", allNum);
			 return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 result = new HashMap<String, Object>();
			 result.put("hmdNum", 0);
			 result.put("allNum", 0);
		}
		 return result;
		
		
	}

    @Override
    public boolean syncTagAndFans(String appId) {

        String accessToken = tokenService.getAccessTokenByRedis(appId);
        JSONObject json =  userTagservice.getTagList(accessToken, appId);
        if(!json.containsKey("errcode")){
            JSONArray arr = json.getJSONArray("tags");
            List<FansTag> getList = fansTagService.getList(appId);
            //查找公众号对应的数据库数目是否匹配
           // if(arr.size() != getList.size()){

	        if(redisTemplate.opsForValue().get(appId+TAGLIST) == null){
		        boolean isTrue = loadFansTags(appId);
		        String  infoStr = isTrue==true?"成功":"失败";
		        logger.info("同步公众号标签"+infoStr+",appId="+appId);
		        if(isTrue){
			        redisTemplate.opsForValue().set(appId+TAGLIST,true,20, TimeUnit.MINUTES);
		        }
	        }

            //}
        }else{
			return false;
		}
      /*  //微信的粉丝量和已同步的粉丝数量做对比，不一致就重新同步
        long  wxCount =  getWxFansCount(appId);
		if(wxCount == -100){
			return false;
		}
		Query q = new Query(Criteria.where("appId").is(appId));
		long localCount =  mongoTemplate.count(q,Constants.WXFANS_T);
		logger.info("微信wxCount:"+wxCount+",localCount:"+localCount);
		if(wxCount != localCount){
			logger.info("本地粉丝数量和微信接口不一致，重新同步");
			loadFansList(appId,"");
		}*/
        return true;
    }

    /**
     * 获取微信粉丝数量
     * @param appId
     * @return
     */
    long getWxFansCount(String appId){
        String accessToken = tokenService.getAccessTokenByRedis(appId);
        JSONObject groupJson = userService.getUserList(accessToken, "",appId);
        long total = 0;
        if( groupJson.get("total")!=null){
            //关注者列表  里面存放的都是openid  我们需要根据openid查询到用户
            total  = groupJson.getInt("count");
        }else{
        	return -100;
		}
        return total;
    }


	public void syncJobDo(String appId){
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			Date startTime = nextGivenSecondDate(null, 1);
			JobDataMap jobMap = new JobDataMap();
			jobMap.put("appId", appId);
			String taskId = UUID.randomUUID().toString().replace("-", "");
			JobDetail job = newJob(LoadFansInfo.class)
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

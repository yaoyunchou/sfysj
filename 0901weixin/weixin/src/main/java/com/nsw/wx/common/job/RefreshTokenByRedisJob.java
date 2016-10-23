package com.nsw.wx.common.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nsw.wx.api.service.BasicServices;
import com.nsw.wx.api.service.imp.BasicServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.DateUtils;


@Component
@Configurable
@EnableScheduling
public class RefreshTokenByRedisJob {
	
	private Logger logger = Logger.getLogger(RefreshTokenByRedisJob.class);
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Value("${appid}")
	private  String TOKENPREFIX ;
	
	
	
	
	@Scheduled(cron = "0 */2 * * * ? ")
	public void refreshBindAccountToken(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<String,Object> queryComponent = new HashMap<String, Object>();
        queryComponent.put("type", 1);
    	BasicServices service  = new BasicServiceImp();
    	Map<String,Object> componentAccount =  baseMongoTemplate.findOne(Constants.WXACCOUNT_T, queryComponent);
    	ArrayList arr = new ArrayList<>();
    	arr.add(2);
    	arr.add(3);
        Criteria criteria=Criteria.where("type").in(arr);
    	Query que = new Query();
    	que.addCriteria(criteria);
        List<Map<String,Object>> entityList =  baseMongoTemplate.queryMulti(que, Constants.WXACCOUNT_T);
        if(entityList != null && entityList.size() > 0){
        	 int c_flag = 0;
        	 long nowTime = new Date().getTime();
            	for(Map<String,Object> en:entityList){
            		//通过redis拿对应公众号accessToken的值,拿不到的说明过期了，重新拉取缓存  ，缓存时间为100分钟
            		String appId = en.get("appId")+"";
            		Object cacheAccessToken =  redisTemplate.opsForValue().get(TOKENPREFIX+appId);
            		if(cacheAccessToken == null || en.get("authorizer_access_token") == null){//过期了
            			if(Integer.parseInt(en.get("type")+"") == 3){
            				try {
								AccessTokenHelper tokenHelper = new AccessTokenHelper(en.get("appId")+"",en.get("appSecret")+"");
								JSONObject jsonRst =  tokenHelper.getAccessToken(en.get("appId")+"",en.get("appSecret")+"");
								if(jsonRst!=null&&!jsonRst.containsKey("errcode") ){
									//缓存到redis，并保存到数据库
									redisTemplate.opsForValue().set(TOKENPREFIX+appId, jsonRst.getString("access_token"), 100, TimeUnit.MINUTES);
									c_flag++;//刷新票据计时器
								 	Criteria criterias=Criteria.where("appId").is(en.get("appId")+"").and("type").is(3);
									Query ques = new Query();
									ques.addCriteria(criterias);
									Update update =  new Update();
									update.set("authorizer_access_token", jsonRst.getString("access_token"));
									update.set("refresh_time",  DateUtils.getCurrentTime());
									baseMongoTemplate.updateOne(ques, update, Constants.WXACCOUNT_T);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
            			}else if(Integer.parseInt(en.get("type")+"") == 2){
            				try {
								JSONObject jo = new JSONObject();
								jo.put("component_appid", componentAccount.get("appId"));
								jo.put("authorizer_appid", en.get("appId"));
								jo.put("authorizer_refresh_token", en.get("authorizer_refresh_token"));
								JSONObject jsonRst =  service.postMethodResult("https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="+componentAccount.get("component_access_token"), jo.toString(),null);
								 if(jsonRst.get("authorizer_refresh_token") != null ){
									    redisTemplate.opsForValue().set(TOKENPREFIX+appId, jsonRst.getString("authorizer_access_token"), 100, TimeUnit.MINUTES);
									 	c_flag++;//刷新票据计时器
									 	Criteria criteria1=Criteria.where("appId").is(en.get("appId")+"").and("type").is(2);
								    	Query que1 = new Query();
								    	que1.addCriteria(criteria1);
								    	Update update =  new Update();
								    	update.set("authorizer_access_token", jsonRst.get("authorizer_access_token")+"");
								    	update.set("refresh_time",  DateUtils.getCurrentTime());
								    	update.set("authorizer_refresh_token",  jsonRst.get("authorizer_refresh_token")+"");
								    	baseMongoTemplate.updateOne(que1, update, Constants.WXACCOUNT_T);
								 }
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
            			}
            		}
	        	}
            	if(c_flag>0){
            		logger.info("%%%%%%%%%%%%%token刷新JOB执行成功,当前时间为"+df.format(new Date())+",刷新"+c_flag+"个公众号token所花时间为"+(new Date().getTime()-nowTime)/(1000)+"秒！----%%%%%%%%%%%%");
            	}
        }
	}
	
	
	
	/**
	 * 服务器定时刷新component_access_token
	 * 每隔5分钟执行一次
	 * 在component_access_token超过100分钟时刷新重新缓存
	 * @throws ParseException 
	 */
	@Scheduled(cron = "0 */2 * * * ? ")
    public void refreshComponentAccessToken() throws ParseException{
    	 	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	    Map<String,Object> query = new HashMap<String, Object>();
	        query.put("type", 1);
	        Map<String,Object> entityAccount =  baseMongoTemplate.findOne(Constants.WXACCOUNT_T, query);
	        	if( entityAccount.get("refresh_time") != null && !"".equals(entityAccount.get("refresh_time")+"")){
	        		String appId = entityAccount.get("appId")+"";
	        		Object cacheAccessToken =  redisTemplate.opsForValue().get(TOKENPREFIX+appId);
	        		if(cacheAccessToken == null){
	        			 BasicServices service  = new BasicServiceImp();
	        		    	JSONObject ob = new JSONObject();
	    		    		ob.put("component_appid", entityAccount.get("appId")+"");
	    		    		ob.put("component_appsecret",  entityAccount.get("appSecret")+"");
	    		    		ob.put("component_verify_ticket", entityAccount.get("component_verify_ticket"));
	        		        JSONObject jsonRst =  service.postMethodResult("https://api.weixin.qq.com/cgi-bin/component/api_component_token", ob.toString(),null);
	        		        if(jsonRst.get("component_access_token") != null ){
	        		        	redisTemplate.opsForValue().set(TOKENPREFIX+appId, jsonRst.getString("component_access_token"), 100, TimeUnit.MINUTES);
	        		        	Criteria criteria=Criteria.where("appId").is(entityAccount.get("appId")+"");
	        			    	Query que = new Query();
	        			    	que.addCriteria(criteria);
	        			    	Update update =  new Update();
	        			    	update.set("component_access_token", jsonRst.get("component_access_token")+"");
	        			    	update.set("refresh_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	        			    	baseMongoTemplate.updateOne(que, update, Constants.WXACCOUNT_T);
	        		        	logger.info("平台token缓存JOB执行成功,当前时间为"+df.format(new Date())+"component_access_token最新时间为"+entityAccount.get("refresh_time")+""+",，刷新成功!");
	        		        }else{
	        		        	logger.error("获取component_access_token错误，错误返回信息为"+jsonRst);
	        		        }
	        		}
	        	}
    }

}

package com.nsw.wx.common.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.nsw.wx.api.service.BasicServices;
import com.nsw.wx.api.service.imp.BasicServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.DateUtils;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年6月7日 下午2:22:45
* @Description: TODO
 */
@Service("accessTokenService")
public class AccessTokenServiceImp implements AccessTokenService{
	
	@Autowired
	private WxAccountService wxAccountService;
	
	private Logger logger = Logger.getLogger(AccessTokenServiceImp.class);
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Value("${appid}")
	private String TOKENPREFIX;

	@Override
	public String checkRuslt(String appId) throws Exception{
		// TODO Auto-generated method stub
		if (appId != null ) {

				logger.info("appId AccessToken time out, start update AccessToken...");
				Map<String, Object> account = wxAccountService
						.getAccountByAppId(appId);
				
				if(account!=null){
					// 公众号类型，2为授权绑定，3为手动绑定
					int type = Integer.parseInt(account.get("type") + "");
					BasicServices service = new BasicServiceImp();
					if (type == 2) {
						Map<String, Object> queryComponent = new HashMap<String, Object>();
						queryComponent.put("type", 1);
						Map<String, Object> componentAccount = baseMongoTemplate
								.findOne(Constants.WXACCOUNT_T, queryComponent);
						JSONObject jo = new JSONObject();
						jo.put("component_appid", componentAccount.get("appId")
								+ "");
						jo.put("authorizer_appid", account.get("appId") + "");
						jo.put("authorizer_refresh_token",
								account.get("authorizer_refresh_token") + "");
						JSONObject jsonRst = service
								.postMethodResult(
										"https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="
												+ componentAccount
														.get("component_access_token"),
										jo.toString(),null);
						if (jsonRst.get("authorizer_refresh_token") != null) {
							redisTemplate.opsForValue().set(TOKENPREFIX + appId,
									jsonRst.getString("authorizer_access_token"),
									100, TimeUnit.MINUTES);
							Criteria criteria1 = Criteria.where("appId").is(appId)
									.and("type").is(2);
							Query que1 = new Query();
							que1.addCriteria(criteria1);
							Update update = new Update();
							update.set("authorizer_access_token",
									jsonRst.get("authorizer_access_token") + "");
							update.set("refresh_time", DateUtils.getCurrentTime());
							update.set("authorizer_refresh_token",
									jsonRst.get("authorizer_refresh_token") + "");
							baseMongoTemplate.updateOne(que1, update,
									Constants.WXACCOUNT_T);
							logger.info("token缓存成功，返回access_token "+jsonRst.getString("authorizer_access_token"));
							return jsonRst.get("authorizer_access_token") + "";
						}
					} else if (type == 3) {
						AccessTokenHelper tokenHelper = new AccessTokenHelper(
								account.get("appId") + "", account.get("appSecret")
										+ "");
						JSONObject jsonRst = tokenHelper.getAccessToken(
								account.get("appId") + "", account.get("appSecret")
										+ "");
						if (jsonRst != null && !jsonRst.containsKey("errcode")) {
							// 缓存到redis，并保存到数据库
							redisTemplate.opsForValue().set(TOKENPREFIX + appId,
									jsonRst.getString("access_token"), 100,
									TimeUnit.MINUTES);
							Criteria criterias = Criteria.where("appId").is(appId)
									.and("type").is(3);
							Query ques = new Query();
							ques.addCriteria(criterias);
							Update update = new Update();
							update.set("authorizer_access_token",
									jsonRst.getString("access_token"));
							update.set("refresh_time", DateUtils.getCurrentTime());
							baseMongoTemplate.updateOne(ques, update,
									Constants.WXACCOUNT_T);
							logger.info("token缓存成功，返回access_token "+jsonRst.getString("access_token"));
							return jsonRst.getString("access_token");
						}
					}
				}
			}
		return null;
		}

	@Override
	public String getAccessTokenByRedis(String appId) {
		// TODO Auto-generated method stub
		if(appId != null){
			Object token = redisTemplate.opsForValue().get(TOKENPREFIX + appId);
			if(token != null){
				return (String)token;
			}else{
				Map<String, Object> account_Map = wxAccountService.getAccountByAppId(appId);
				if(account_Map != null && account_Map.containsKey("authorizer_access_token")){
					return account_Map.get("authorizer_access_token")+ "";
				}
			}
		}
		return null;
	}

}

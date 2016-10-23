package com.nsw.wx.common.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsw.wx.common.security.CustomSecurityUser;
import com.nsw.wx.common.util.ContextUtil;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.nsw.wx.api.service.OpenWxService;
import com.nsw.wx.api.service.imp.OpenWxServiceImp;
import com.nsw.wx.common.controller.OpenController;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;


@Service("openCommonService")
public class OpenCommonServiceImp implements OpenCommonService{
	
	@Autowired
	BaseMongoTemplate baseMongoTemplate;

	@Autowired
	private WxAccountService wxAccountService;
	
	@Value("${appid}")
	private String appid;  //本地访问路径
	
	@Value("${appsecret}")
	private String appsecret;  //本地访问路径
	
	@Value("${weixinencodingaeskey}")
	private String weixinencodingaeskey;  //本地访问路径
	
	@Value("${token}")
	private String token;  //本地访问路径
	
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Value("${appid}")
	private  String TOKENPREFIX ;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	private Logger logger = Logger.getLogger(OpenCommonServiceImp.class);

	@Override
	public Account getAccountBasic() {
		// TODO Auto-generated method stub
		Account account = new Account();
		account.setType(1);
		account.setToken(token);
		account.setAppId(appid);
		account.setAppSecret(appsecret);
		account.setEncodingAesKey(weixinencodingaeskey);
		return account;
	}

	@Override
	public Map<String, Object> getComponentAccessToken(String type) {
		// TODO Auto-generated method stub
		Map<String, Object> object;
		try {
			Map<String,Object> query  = new HashMap<String, Object>();
			query.put("type", 1);
			object = baseMongoTemplate.findOne(Constants.WXACCOUNT_T, query);
			return object;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	/**
	 * 判断第三方 component_access_token 是否过期
	 * 过期后执行刷新并重新获取
	 * false:过期
	 * true :有效
	 * @param access_token
	 * @return
	 */
	public Map<String, Object>  isExpireAndRefresh(Map<String, Object> access_token){
		if(access_token == null){
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			long nowTime = new Date().getTime();
			String update_date = access_token.get("update_date") + "";
			Date date = df.parse(update_date);
			long timediff = (nowTime - date.getTime())/(1000 * 60 );
    		if(timediff > 110){//如果距今超过100分钟（失效时间是2小时）
    			Map<String, Object> comAccount =  getComponentAccessToken("1");
				JSONObject ob = new JSONObject();
	    		ob.put("component_appid", comAccount.get("appId")+"");
	    		ob.put("component_appsecret",  comAccount.get("appSecret")+"");
	    		ob.put("component_verify_ticket", comAccount.get("component_verify_ticket"));
	    		OpenWxService openWxService = new OpenWxServiceImp();
	    		JSONObject comAccessToken =  openWxService.getComponentAccessToken(ob,null);
	    		if(comAccessToken != null && comAccessToken.get("component_access_token") != null){
	    			Criteria criteria=Criteria.where("type").is(1);
			    	Query que = new Query();
			    	que.addCriteria(criteria);
			    	Update update =  new Update();
			    	update.set("component_access_token", comAccessToken.get("component_access_token"));
			    	update.set("update_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			    	baseMongoTemplate.updateOne(que, update, Constants.WXACCOUNT_T);
			    	access_token =getComponentAccessToken("1");
			    	logger.info("success,获取ComponentAccessToken时过期，被动更新成功!!");
	    		}else{
	    			logger.error("fail,获取ComponentAccessToken时过期，被动更新失败!!");
	    		}
    			return access_token;
    		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return access_token;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteAccountInfo(String appId) {
		// TODO Auto-generated method stub
		try {
			//清除redis缓存
			Object cacheAccessToken =  redisTemplate.opsForValue().get(TOKENPREFIX+appId);
			if(cacheAccessToken != null){
				redisTemplate.delete(TOKENPREFIX+appId);
			}
			Query query = new Query();
			Criteria criteria=Criteria.where("appId").is(appId);
			query.addCriteria(criteria);
			String [] tables = new String[]{Constants.WXMENU_T,Constants.WXFANS_T,Constants.WXFANSGROUP_T,Constants.WXKEYWORD_T,Constants.WXMASSLOG_T,Constants.WXMASSMSG_T,Constants.WXMESSAGE_T,"weixin_qrCode","weixin_scanQrCodeLog"};
			//Constants.WXIMAGE_T,Constants.WXNEWS_T,  暂时不清除图文和图片素材，因为是所有公众号公用的
			for(int i=0;i<tables.length;i++){
				try {
					if(mongoTemplate.count(query, tables[i])>0){
						mongoTemplate.remove(query, tables[i]);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("清除公众号表"+tables[i]+"出错!"+e.getMessage());
				}
			}



		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("公众号清除用户信息出错");
			
		}
	}
	

}

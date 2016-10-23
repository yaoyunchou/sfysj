package com.nsw.wx.common.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.nsw.wx.common.docmodel.LogCode;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;

/**
 * 系统启动时初始化数据用 如果第三方平台账号不存在数据库，则从配置文件加载进数据库
 * 
 * @author liuzp
 *
 */

@Component
public class InitComponentData implements CommandLineRunner {

	private Logger logger = Logger.getLogger(InitComponentData.class);

	@Value("${appid}")
	private String appid;

	@Value("${appsecret}")
	private String appsecret;

	@Value("${weixinencodingaeskey}")
	private String weixinencodingaeskey;

	@Value("${token}")
	private String token;

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("appId", appid);

		//初始化第三方平台信息
		Map<String, Object> entity = baseMongoTemplate.findOne(
				Constants.WXACCOUNT_T, query);
		if (entity == null) {
			query.put("encodingAesKey", weixinencodingaeskey);
			query.put("type", 1);
			query.put("token", token);
			query.put("create_time",
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
			query.put("appSecret", appsecret);
			//query.put("site", "nswwx");
			baseMongoTemplate.save(Constants.WXACCOUNT_T, query);
			logger.info("system data init success!");
		}
		//初始化日志数据
		List<LogCode> list = mongoTemplate.findAll(LogCode.class);
		// 开始初始化日志数据
		String jsonStr =   readJsonFile("logInitData.json");
		JSONArray arr = JSONArray.fromObject(jsonStr);
		boolean isSync = list.size() != arr.size() ?false:true;
		if(!isSync){
			logger.info("日志数据未同步，执行同步!");
			mongoTemplate.remove(new Query(), LogCode.class);
			for(int i=0;i<arr.size();i++){
				JSONObject obj = arr.getJSONObject(i);
				LogCode logCode = new LogCode();
				logCode.setDescription(obj.getString("description"));
				logCode.setOperationCode(obj.getString("operationCode"));
				mongoTemplate.save(logCode);
			}
		}
		
		
	}

	public String readJsonFile(String fileName) {
		
		ClassPathResource resources = new ClassPathResource(fileName);
		BufferedReader reader = null;
		String laststr = "";
		try {
			InputStream fileInputStream = resources.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}

}

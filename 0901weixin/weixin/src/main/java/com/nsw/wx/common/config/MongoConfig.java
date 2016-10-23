package com.nsw.wx.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月17日 上午8:27:45
* @Description: mongo配置重写
*/
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
	
	private Logger logger = Logger.getLogger(MongoConfig.class);
	
	@Value("${mongodb.host}")
	private String mongodbHost;
	
	@Value("${mongodb.port}")
	private int mongodbPort;
	
	@Value("${mongodb.name}")
	private String mongodbName;
	
    @Value("${mongodb.user}")
    private String mongodbUser;

	@Value("${mongodb.password}")
	private String mongodbpwd;
	
	@Value("${authentification}")
	private boolean authentification;
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		
		MongoTemplate mongoTemplate = 
		    new MongoTemplate(mongo(), mongodbName);
		logger.info("*******"+mongoTemplate.getDb().getName() +"基础库");
		return mongoTemplate;
 
	}
    @Override
    protected String getDatabaseName() {
        return mongodbName;
    }

    @Override
    public Mongo mongo() throws Exception {
    
    	 MongoClient mongoClient;
    	 MongoCredential credential = MongoCredential.createMongoCRCredential(mongodbUser,mongodbName,mongodbpwd.toCharArray());
         MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(3000)
                .threadsAllowedToBlockForConnectionMultiplier(10)
                .readPreference(ReadPreference.nearest())
                .build();
        List<ServerAddress> addresses = new ArrayList<ServerAddress>();
        String[] str = this.mongodbHost.split(",");
        for (String strHost : str) {
          ServerAddress address = new ServerAddress(strHost, mongodbPort);
          addresses.add(address);
        }
        if(authentification){
        	mongoClient = new MongoClient(addresses,Arrays.asList(credential), options);
        }else{
        	 mongoClient = new MongoClient(addresses, options);	
        }
    	
    	 return mongoClient;
    }

}

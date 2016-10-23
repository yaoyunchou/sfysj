package com.nsw.wx.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.nsw.wx.common.docmodel.SysLog;
import com.nsw.wx.common.security.CustomSecurityUser;
import com.nsw.wx.common.security.SpringSecurityUtils;
import com.nsw.wx.common.util.DateUtils;

@Service("logService")
public class LogServiceImp implements LogService{
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void saveLog(SysLog log) {
		// TODO Auto-generated method stub
		CustomSecurityUser user = SpringSecurityUtils.getCurrentLoginUser();
		if(user!=null && log!=null){
			log.setCreatedTime(DateUtils.getCurrentTime());
			log.setUserId(user.getId()+"");
			log.setUserName(user.getName());
			log.setIp(SpringSecurityUtils.getCurrentUserIp());
			log.setSite(user.getSite());
			mongoTemplate.save(log);
		}
		
	}

}

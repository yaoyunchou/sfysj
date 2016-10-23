package com.nsw.wx.common.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;
import com.nsw.wx.common.docmodel.ScanQrCodeLog;


@Service("scanQrCodeLogService")
public class ScanQrCodeLogServiceImp implements ScanQrCodeLogService{
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ScanQrCodeLog addScanLog(ScanQrCodeLog log) {
		// TODO Auto-generated method stub
		try {
			mongoTemplate.save(log);
			return log;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ScanQrCodeLog getScanLog(String appId,String date,String qrcodeId) {
		// TODO Auto-generated method stub
		try {
			Query query =  new Query();
			Criteria criteria = Criteria.where("appId").is(appId).and("scanDate").is(date).and("id").is(qrcodeId);
			query.addCriteria(criteria);
			ScanQrCodeLog  log =  mongoTemplate.findOne(query, ScanQrCodeLog.class);
			return log;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public boolean updateScanLog(String id,Update update) {
		// TODO Auto-generated method stub
		try {
			Query query =  new Query();
			Criteria criteria = Criteria.where("_id").is(new ObjectId(id));
			query.addCriteria(criteria);
			WriteResult wr =    mongoTemplate.updateFirst(query, update, ScanQrCodeLog.class);
			if (wr.isUpdateOfExisting()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	

	@Override
	public long queryScanLogCount(Query query) {
		// TODO Auto-generated method stub
		
		try {
			return mongoTemplate.count(query, ScanQrCodeLog.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public List<ScanQrCodeLog> getScanLogList(String appId, String date) {
		// TODO Auto-generated method stub
		try {
			Query query =  new Query();
			Criteria criteria = Criteria.where("appId").is(appId).and("scanDate").is(date);
			query.addCriteria(criteria);
			List<ScanQrCodeLog>  logList =  mongoTemplate.find(query, ScanQrCodeLog.class);
			return logList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ScanQrCodeLog> getScanLogListByDate(
			String qrcodeId, String beginDate, String endDate) {
		Query query =  new Query();
		Criteria criteria = Criteria.where("id").is(qrcodeId)
				.and("scanDate").gte(beginDate).lte(endDate);
		query.addCriteria(criteria);
		query.with(new Sort(Direction.ASC , "scanDate"));
		List<ScanQrCodeLog>  logList =  mongoTemplate.find(query, ScanQrCodeLog.class);
		return logList;
	}


}

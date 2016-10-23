package com.nsw.wx.common.service;

import java.util.List;
import java.util.Map;

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
import com.nsw.wx.common.docmodel.QrCode;
import com.nsw.wx.common.docmodel.ScanQrCodeLog;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;

@Service("wxQrCodeService")
public class WxQrCodeServiceImp implements WxQrCodeService{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	
	@Autowired
	private ScanQrCodeLogService scanQrCodeLogService;
	

	@Override
	public QrCode addQrCode(QrCode qrCode) {
		// TODO Auto-generated method stub
		
		try {
			mongoTemplate.save(qrCode);
			return qrCode;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public boolean updateQrCode(String id, Update update) {
		// TODO Auto-generated method stub
		try {
			Query query = new Query();
			Criteria criteria = Criteria.where("_id").is(new ObjectId(id));
			query.addCriteria(criteria);
			WriteResult wr =    mongoTemplate.updateFirst(query, update, QrCode.class);
			return wr.getN()==1?true:false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteQrCode(String[] qrCodes) {
		// TODO Auto-generated method stub
		
		try {
			ObjectId[] ids = new ObjectId[qrCodes.length];
			for(int i=0;i<ids.length;i++){
				ids[i] = new ObjectId(qrCodes[i]);
			}
			Query query = new Query();
			Criteria criteria = Criteria.where("_id").in(ids);
			query.addCriteria(criteria);
			mongoTemplate.remove(query, QrCode.class);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<Map<String,Object>> getQrCodeList(String appId, int pageNum,
			int pageSize,String title,String beginDate,String endDate,boolean isPaging) {
		// TODO Auto-generated method stub
		Query query = new Query();
		Criteria criteria = Criteria.where("appId").is(appId).and("title").regex(title==null?"":title);
		query.addCriteria(criteria);
		if(isPaging){
			query.skip((pageNum - 1) * pageSize).limit(pageSize);
		}
		query.with(new Sort(Direction.DESC , "createTime"));
		List<Map<String,Object>> qrCodeList =  baseMongoTemplate.queryMulti(query, Constants.WXQRCODE_T);
		//List<QrCode> qrCodeList =  mongoTemplate.find(query,Constants.WXQRCODE_T);
		for(Map<String,Object> qrCode :  qrCodeList){
			qrCode.put("id",qrCode.get("_id").toString());
			if(beginDate != null && endDate != null){
				//根据时间条件去统计二维码  扫描次数  关注人数
				long scanNum = 0 ;
				//long scanFollowNum = 0 ;
				long addFollowNum = 0 ;
				List<ScanQrCodeLog>  logList =  scanQrCodeLogService.getScanLogListByDate(qrCode.get("_id")+"", beginDate, endDate);
				for(ScanQrCodeLog log: logList){
					scanNum += log.getScanNum();
					addFollowNum += log.getFollowNum();
				}
				qrCode.put("scanNum", scanNum);
				qrCode.put("scanFollowNum", scanNum - addFollowNum);
				qrCode.put("addFollowNum", addFollowNum);
			}
			
		}
		return qrCodeList;
	}

	@Override
	public boolean checkTitleReply(String appId, String title) {
		// TODO Auto-generated method stub
		Query query = new Query();
		Criteria criteria = Criteria.where("appId").is(appId).and("title").is(title);
		query.addCriteria(criteria);
		int num = mongoTemplate.find(query, QrCode.class).size();
		if(num>0){
			return true;
		}
		return false;
	}

	@Override
	public QrCode getQrCode(String id) {
		// TODO Auto-generated method stub
		Query query = new Query();
		Criteria criteria = Criteria.where("_id").is(new ObjectId(id));
		query.addCriteria(criteria);
		return  mongoTemplate.findOne(query, QrCode.class);
	}
	
	
	@Override
	public QrCode getQrCodeByQuery(Query query) {
		return  mongoTemplate.findOne(query, QrCode.class);
	}
	

	@Override
	public int getQrCodeCount(String   appId,String title) {
		// TODO Auto-generated method stub
		Query query = new Query();
		Criteria criteria = Criteria.where("appId").is(appId).and("title").regex(title==null?"":title);
		query.addCriteria(criteria);
		return (int)mongoTemplate.count(query, QrCode.class);
	}

	@Override
	public List<QrCode> getQrCodeList(String appId) {
		// TODO Auto-generated method stub
		Query query = new Query();
		Criteria criteria = Criteria.where("appId").is(appId);
		List<QrCode>  list	 = 	mongoTemplate.find(query, QrCode.class);
		return list;
	}
	
}

package com.nsw.wx.common.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.nsw.wx.api.service.UserTagService;
import com.nsw.wx.api.service.imp.UserTagServiceImp;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.AjaxUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mongodb.WriteResult;
import com.nsw.wx.common.docmodel.FansTag;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.PinYin2Abbreviations;

@Service("fansTagService")
public class FansTagServiceImp implements FansTagService{
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	UserTagService userTagService = new UserTagServiceImp();

	@Override
	public FansTag getTag(String id) {
		// TODO Auto-generated method stub
		Map<String,Object> query = new HashMap<>();
		query.put("_id",new ObjectId(id));
		Map<String,Object>  fans =  baseMongoTemplate.findOne(Constants.WXFANSTAG_T,query);
		if(fans!=null){
			FansTag fansTag = new FansTag();
			ObjectId ids = new ObjectId(fans.get("_id")+"");
			fansTag.set_id(ids);
			fansTag.setAppId(fans.get("appId")+"");
			fansTag.setTagId(Integer.parseInt(fans.get("tagId")+""));
			fansTag.setName(fans.get("name")+"");
			fansTag.setCount(Integer.parseInt(fans.get("count")+""));
			return fansTag;
		}
		return null;
	}


	public FansTag getTagById(int id,String appId) {
		// TODO Auto-generated method stub
		Map<String,Object> query = new HashMap<>();
		query.put("appId",appId);
		query.put("tagId",id);
		Map<String,Object>  fans =  baseMongoTemplate.findOne(Constants.WXFANSTAG_T,query);
		if(fans!=null){
			FansTag fansTag = new FansTag();
			ObjectId ids = new ObjectId(fans.get("_id")+"");
			fansTag.set_id(ids);
			fansTag.setAppId(fans.get("appId")+"");
			fansTag.setTagId(Integer.parseInt(fans.get("tagId")+""));
			fansTag.setName(fans.get("name")+"");
			fansTag.setCount(Integer.parseInt(fans.get("count")+""));
			return fansTag;
		}
		return null;
	}

	@Override
	public List<Integer> getWxTagIdList(String accessToken, String appId) {
		List<Integer> list = new ArrayList<>();
		JSONObject json =  userTagService.getTagList(accessToken, appId);
		if(!json.containsKey("errcode")){
			JSONArray arr = json.getJSONArray("tags");
			for(int i = 0;i < arr.size(); i++){
				JSONObject obj = arr.getJSONObject(i);
				list.add(obj.getInt("id"));
			}
		}
		return list;
	}

	public FansTag getTagByName(String appId,String name) {
		// TODO Auto-generated method stub
		Map<String,Object> query = new HashMap<>();
		query.put("appId",appId);
		query.put("name",name);
		Map<String,Object>  fans =  baseMongoTemplate.findOne(Constants.WXFANSTAG_T,query);
		if(fans!=null){
			FansTag fansTag = new FansTag();
			ObjectId oid = new ObjectId(fans.get("_id")+"");
			fansTag.set_id(oid);
			fansTag.setAppId(fans.get("appId")+"");
			fansTag.setTagId(Integer.parseInt(fans.get("tagId")+""));
			fansTag.setName(fans.get("name")+"");
			fansTag.setCount(Integer.parseInt(fans.get("count")+""));
			return fansTag;
		}
		return null;
	}
	

	@Override
	public boolean delAllTag(String appId) {
		// TODO Auto-generated method stub
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("appId").is(appId));
			baseMongoTemplate.removeByqyery(query,Constants.WXFANSTAG_T);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean delTag(String appId, String tagId) {
		// TODO Auto-generated method stub
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("appId").is(appId).and("tagId").is(Integer.parseInt(tagId)));
			WriteResult wr =  mongoTemplate.remove(query,Constants.WXFANSTAG_T);
			return wr.getN()>0?true:false;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<FansTag> getList(String appId) {
		// TODO Auto-generated method stub
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("appId").is(appId));
			List<FansTag> fansList = new ArrayList<>();
			List<HashMap> list =  baseMongoTemplate.findByQuery(query,Constants.WXFANSTAG_T);
			//List<FansTag> list =  mongoTemplate.find(query, FansTag.class);
		  Collections.sort(list, new Comparator<HashMap>() {
    		    public int compare(HashMap o1,HashMap o2) {
    		    	String name1 = o1.get("name")+"";
    		    	String name2 = o2.get("name")+"";
    		    	String name1_char =  PinYin2Abbreviations.cn2py(name1);
    		    	String name2_char =  PinYin2Abbreviations.cn2py(name2);
    		    	name1_char = name1_char==null ? "" :name1_char.substring(0, 1);
    		    	name2_char = name2_char==null ? "" :name2_char.substring(0, 1);
    		    	int result = name1_char.compareTo(name2_char);
    		    	if(result > 0){
    		    		return 1;
    		    	}else if(result == 0){
    		    		return 0;
    		    	}else{
    		    		return -1;
    		    	}
    		    }
    	   });
			for(HashMap fans:list){
				FansTag fansTag = new FansTag();
				ObjectId oid = new ObjectId(fans.get("_id")+"");
				fansTag.set_id(oid);
				fansTag.setAppId(fans.get("appId")+"");
				fansTag.setTagId(Integer.parseInt(fans.get("tagId")+""));
				fansTag.setName(fans.get("name")+"");
				long  count =  getFansCountByTag(appId,fans.get("tagId")+"");
				fansTag.setCount((int)count);
				fansList.add(fansTag);
			}
			return fansList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<FansTag>();
		}
		
	}

	@Override
	public boolean addTag(FansTag tag) {
		// TODO Auto-generated method stub
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("appId").is(tag.getAppId()).and("tagId").is(tag.getTagId()));
			if(mongoTemplate.count(query, Constants.WXFANSTAG_T) > 0){
				mongoTemplate.remove(query,Constants.WXFANSTAG_T);
			}
			mongoTemplate.save(tag);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean updateTag(Update update, FansTag tag) {
		// TODO Auto-generated method stub
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("appId").is(tag.getAppId()).and("tagId").is(tag.getTagId()));
			WriteResult wr =  mongoTemplate.updateFirst(query, update,Constants.WXFANSTAG_T);
			return wr.getN()>0?true:false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}

	@Override
	public long getFansCountByTag(String appId, String tagId) {
		// TODO Auto-generated method stub
		Query query = new Query();
		ArrayList<Integer> list = new ArrayList<>();
		query.addCriteria(Criteria.where("appId").is(appId));//.and("groupid").ne(1)
		if(Integer.parseInt(tagId) != 10000){
			list.add(Integer.parseInt(tagId) );
			query.addCriteria(Criteria.where("tagid_list").in(list));
		}else{
			query.addCriteria(Criteria.where("tagid_list").is(new ArrayList<Integer>()));
		}
		return  mongoTemplate.count(query, Constants.WXFANS_T);
	}

}

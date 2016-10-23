package com.nsw.wx.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.common.docmodel.KeyWordReply;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.DateUtils;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月25日 上午10:03:00
* @Description: 关键字回复   关注时回复   无匹配回复
 */

@Controller
@RequestMapping("/keyWord")
public class KeyWordReplyController {
	
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private WxAccountService wxAccountService ;
	
	UserService userService = new UserServiceImp();
	
	/**
	 * 
	* @Description: ，添加
	* @param @param map
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "reply", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Map<String,Object> key){
		try {
			if(key != null && key.get("appId") != null  ){
				JSONObject json = JSONObject.fromObject(key);
				//关注时回复，    无匹配回复
				if(Integer.parseInt(key.get("type")+"")  == 0 || Integer.parseInt(key.get("type")+"")  == 2){
					//查重
					Query query = new Query();
					Criteria criteria=Criteria.where("appId").is(key.get("appId")+"").and("type").is(Integer.parseInt(key.get("type")+""));
			    	query.addCriteria(criteria);
					if(baseMongoTemplate.findCountByQuery(query, Constants.WXKEYWORD_T)>0){
						return  AjaxUtil.renderFailMsg("已存在回复类型");				
					}
				}else if(Integer.parseInt(key.get("type")+"")  == 1){//查重
					if(key.get("ruleName")!=null){
						Query query = new Query();
						Criteria criteria=Criteria.where("appId").is(key.get("appId")+"").and("ruleName").is(key.get("ruleName")+"");
				    	query.addCriteria(criteria);
					   if(baseMongoTemplate.findCountByQuery(query,Constants.WXKEYWORD_T)>0){
						   return  AjaxUtil.renderFailMsg("规则名不能重复");	
					   }
					}
				}
				if(key.get("replyType") !=null){
					if("pic".equals(key.get("replyType")+"")){
						if(key.get("fileId")==null ||"".equals(key.get("fileId")+"")) {
							return  AjaxUtil.renderFailMsg("图片不能为空");	
						}
					}
					if("news".equals(key.get("replyType")+"") && (key.get("mediaId")==null ||"".equals(key.get("mediaId")+"")) ){
						return  AjaxUtil.renderFailMsg("图文不能为空");	
					}
					if("txt".equals(key.get("replyType")+"") && (key.get("content")==null ||"".equals(key.get("content")+"")) ){
						return  AjaxUtil.renderFailMsg("文本内容不能为空");	
					}
					
				}
				json.put("enable", false);
				json.put("replyNum", 0);
				json.put("createTime", DateUtils.getCurrentTime());
				json.remove("_id");
				json.remove("id");
				//关键词不得超过200条
				Map<String,Object> countQuery = new HashMap<String, Object>();
				countQuery.put("appId", key.get("appId")+"");
				countQuery.put("type", 1);
				if(baseMongoTemplate.count(Constants.WXKEYWORD_T, countQuery) > 199){
					return AjaxUtil.renderFailMsg("添加关键字不能超过200条!");
				}
				
				Map<String,Object> map =  baseMongoTemplate.save(Constants.WXKEYWORD_T, json);
				if(map != null){
					map.put("id", map.get("_id")+"");
					return AjaxUtil.renderSuccessMsg(map);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("添加失败");
	}
	
	
	/**
	 * 
	* @Description: 查找
	* @param @param map
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "attentionOrMatching", method = RequestMethod.GET)
	@ResponseBody
	public String get( String appId,String type){
		if(appId != null && type != null && ("2".equals(type) || ("0".equals(type)))){
	    	Map<String,Object> query = new HashMap<String, Object>();
	    	query.put("appId", appId);
	    	query.put("type", Integer.parseInt(type));
	    	Map<String,Object> entity =  baseMongoTemplate.findOne(Constants.WXKEYWORD_T, query);
	    	if(entity!=null){
	    		entity.put("id", entity.get("_id")+"");
	    		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	    		list.add(entity);
	    		Map re = new HashMap<>();
	    		re.put("dataList", list);
	    		return AjaxUtil.renderSuccessMsg(re);
	    	}else{
	    	    Map re = new HashMap<>();
                re.put("dataList", new ArrayList<Map<String,Object>>());
	    		return AjaxUtil.renderSuccessMsg(re);
	    	}
	    	
		}
		return AjaxUtil.renderFailMsg("操作失败");
	}
	
	
	@RequestMapping(value = "reply", method = RequestMethod.GET)
	@ResponseBody
	public String getReply(String id){
		if(!StringUtils.isEmpty(id)){
			Map<String,Object> query = new HashMap<String, Object>();
			query.put("_id", new ObjectId(id));
			Map<String,Object> entity =  baseMongoTemplate.findOne(Constants.WXKEYWORD_T, query);
			if(entity != null){
				entity.put("id", entity.get("_id")+"");
				return AjaxUtil.renderSuccessMsg(entity);
			}
			return AjaxUtil.renderFailMsg("没有找到数据!");
		}
		return AjaxUtil.renderFailMsg("参数错误!");
	}
	
	
	
	/**
	 * 
	* @Description: 修改
	* @param @param map
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "reply", method = RequestMethod.PUT)
	@ResponseBody
	public String update(@RequestBody Map<String,Object> key){
		
		if(key != null){
			if(Integer.parseInt(key.get("type")+"")  == 1){
				if(key.get("ruleName")!=null){
					Map<String,Object> mapQuery = new HashMap<String, Object>();
					mapQuery.put("_id", new ObjectId(key.get("id")+""));
			    	Map<String,Object> mapEntity =  baseMongoTemplate.findOne(Constants.WXKEYWORD_T, mapQuery);
			    	if(!(mapEntity.get("ruleName")+"") .equals(key.get("ruleName")+"") ){
			    		mapQuery = new HashMap<String, Object>();
			    		mapQuery.put("appId", key.get("appId")+"");
			    		mapQuery.put("ruleName", key.get("ruleName")+"");
			    		if(baseMongoTemplate.count(Constants.WXKEYWORD_T, mapQuery) > 0){
			    			 return  AjaxUtil.renderFailMsg("规则名不能重复,请重新输入");	
			    		}
			    	}
				}
			}
			
			Query query = new Query();
			Criteria criteria=Criteria.where("appId").is(key.get("appId")).and("_id").is(key.get("id")+"");
	    	query.addCriteria(criteria);
	    	Update update = new Update();
	    	if(key.get("ruleName")!=null){
	    		update.set("ruleName", key.get("ruleName")+"");
	    	}
	    	
	    	if(key.get("matchType")!=null){
	    		update.set("matchType", key.get("matchType")+"");
	    	}
	    	if( key.get("replyType")!=null){
	    		update.set("replyType", key.get("replyType"));
	    	}
	    	if(key.get("enable")!=null){
	    		update.set("enable", key.get("enable"));
	    	}
	    	if(key.get("content")!=null){
	    		update.set("content", key.get("content"));
	    	}
	    	
	    	if(key.get("mediaId")!=null){
	    		update.set("mediaId", key.get("mediaId"));
	    	}
	    	
	    	if(key.get("fileId")!=null){
	    		update.set("fileId", key.get("fileId"));
	    	}
	    	if(key.get("keyWordList")!=null){
	    		update.set("keyWordList", key.get("keyWordList"));
	    	}
	    	if(baseMongoTemplate.update(query, update, Constants.WXKEYWORD_T)){
	    		return AjaxUtil.renderSuccessMsg(key);
	    	}
		}
		return AjaxUtil.renderFailMsg("操作失败");
	}
	
	
	
	/**
	 * 
	* @Description: ，查询
	* @param @param map
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public String getKeyWordList(String appId,String filter,String pageNum,String pageSize){
		int pageNum_ = pageNum==null?1:Integer.parseInt(pageNum);
	    int pageSize_  = pageSize==null?20:Integer.parseInt(pageSize);
	    long totalPages=0;//总页数
		
		if(appId !=null ){
			 filter = filter==null?"":filter;
			 Query querys = new Query();
		     Criteria cr = new Criteria();
		     querys.addCriteria(cr.orOperator(
		    Criteria.where("ruleName").regex(filter), Criteria.where("keyWordList").regex(filter)).and("type").is(1).and("appId").is(appId) );
		     long totalRows= mongoTemplate.count( querys,Constants.WXKEYWORD_T);//总记录数
		     if ((totalRows % pageSize_) == 0) {
		            totalPages = totalRows / pageSize_;
		     } else {
		            totalPages = totalRows / pageSize_ + 1;
		     }
		     try {
		    	 Query query = new Query();
			     Criteria crs = new Criteria();
			     query.addCriteria(crs.orOperator(
			    		 Criteria.where("ruleName").regex(filter), Criteria.where("keyWordList").regex(filter)).and("type").is(1).and("appId").is(appId) );
			    	query.addCriteria(crs);
					query.skip((pageNum_ - 1) * pageSize_).limit(pageSize_);
					query.with(new Sort(Direction.DESC , "createTime"));
					List<HashMap> list =  baseMongoTemplate.findByQuery(query, Constants.WXKEYWORD_T);
					if(list != null){
						for(Map<String,Object> w: list){
							w.put("id",w.get("_id")+"");
						}
					}else{
						list = new ArrayList<HashMap>();
					}
					JSONObject result = new JSONObject();
					result.put("dataList", list);
					result.put("totalPages", totalPages);
					result.put("totalRows", totalRows);
					return AjaxUtil.renderSuccessMsg(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return AjaxUtil.renderFailMsg("操作失败");
	}
	
	
	
	/**
	 * 传值
	 * {appId:"xxxx",ids:["id1","id2"] }
	 * 
	* @Description: 关注时回复，删除
	* @param @param map
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "reply", method = RequestMethod.DELETE)
	@ResponseBody
	public String del(String appId,String []ids){
		if(appId  != null){
			if( ids == null || ids.length==0){
				return AjaxUtil.renderFailMsg("请勾选要删除的数据!");
			}
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(int i=0;i<ids.length;i++){
				String _id = ids[i];
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("appId", appId);
				m.put("_id", new ObjectId(_id));
				list.add(m);
			}
			if(baseMongoTemplate.deleteBatch(Constants.WXKEYWORD_T, list)){
				return AjaxUtil.renderSuccessMsg("删除成功");
			}
		}
		return AjaxUtil.renderFailMsg("操作失败");
	}

}

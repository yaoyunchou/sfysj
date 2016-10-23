package com.nsw.wx.common.controller;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.types.ObjectId;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.common.docmodel.FansGroup;
import com.nsw.wx.common.job.LoadFansInfo;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.FancService;
import com.nsw.wx.common.service.AccessTokenService;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.PinYin2Abbreviations;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月6日 下午2:48:26
* @Description: 微信粉丝管理
 */
@Controller
@RequestMapping("/fans")
public class FancController {
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private WxAccountService wxAccountService ;
	
	UserService userService = new UserServiceImp();
	@Autowired
	private FancService fancService;
	
	@Autowired
	private AccessTokenService tokenService;
	
	
	/**
	 * 
	* @Description: 获取粉丝组列表
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "groupList", method = RequestMethod.GET)
	@ResponseBody
	public String groupList(String appId,boolean isMass) {
		try {
			if(appId != null){
				Map<String,Object> query = new HashMap<>();
				query.put("appId", appId);
				
				List<Map<String,Object>> listAll = new ArrayList<>();
				
		    	List<Map<String,Object>> list = baseMongoTemplate.queryMulti(Constants.WXFANSGROUP_T,query);
		    	
		    	if (list==null) {list = new ArrayList<Map<String,Object>>();}
		    	  
	    	  Collections.sort(list, new Comparator<Map<String, Object>>() {
	    		    public int compare(Map<String, Object> o1,
	    		            Map<String, Object> o2) {
	    		    	String name1 = o1.get("name") + "";
	    		    	String name2 = o2.get("name") + "";
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
				int c =0;
				List<Map<String,Object>> removeList =  new ArrayList<Map<String,Object>>();
				
				Map<String,Object> wfz =  null;
				Map<String,Object> xbz =  null;
				for(Map<String,Object> map:list){
					map.put("id", map.get("_id")+"");
					Map<String,Object> querys = new HashMap<>();
					querys.put("appId", appId);
					int groupId =  Integer.parseInt( map.get("groupid") +"");
					querys.put("groupid", groupId);
					long groupCount =  baseMongoTemplate.count( Constants.WXFANS_T, querys);
					map.put("count", groupCount);
					c+=  Integer.parseInt(map.get("count")+""); 
					if(isMass && groupCount == 0){
						removeList.add(map);
					}
					if(groupId == 0){
						wfz = map;
						removeList.add(map);
					}
					if(groupId == 2){
						xbz = map;
						removeList.add(map);
					}
				}
				list.removeAll(removeList);
				if(wfz !=null){
					listAll.add(wfz);
				}
				if(xbz !=null){
					listAll.add(xbz);
				}
				listAll.addAll(list);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("dataList", listAll);
				jsonObject.put("itemTotal", c);
				return AjaxUtil.renderSuccessMsg(jsonObject);
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("获取粉丝分组信息出错！");
	}
	
	
	/**
	 * 
	* @Description: 移动用户到分组
	* @param @param appId
	* @param @param id
	* @param @param groupId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "moveFansToGroup", method = RequestMethod.POST)
	@ResponseBody
	public String moveFansToGroup(@RequestBody Map<String,Object> entity) {
		if(entity!=null){
			JSONObject entity_ = JSONObject.fromObject(entity);
			if(entity_.containsKey("openid")&& entity_.get("appId")!=null && entity_.get("groupid") != null){
				//Map<String,Object> account = wxAccountService.getAccountByAppId(entity.get("appId")+"");
				String accessToken =  tokenService.getAccessTokenByRedis(entity_.get("appId")+"");
				
				JSONArray arr_openid = entity_.getJSONArray("openid");
				List<String> openidList = new ArrayList<String>();
				for(int i=0;i<arr_openid.size();i++){
					openidList.add(arr_openid.getString(i));
				}
				if(accessToken != null){
					   //将用户批量移到分组
					  JSONObject jsonObject = userService.batchChangeUsersGroup(accessToken, openidList, entity_.getInt("groupid"),entity.get("appId")+"");
					  if(jsonObject.containsKey("errcode") && jsonObject.getInt("errcode") == 0){//移动成功，更新数据库状态
						  boolean flag = false;
						  for(String str:openidList){
							  //查粉丝信息
							 Map<String,Object> fansQuery = new HashMap<String, Object>();
							 fansQuery.put("appId", entity_.get("appId")+"");
							 fansQuery.put("openid", str);
							 Map<String,Object> fansUpdate = new HashMap<String, Object>();
							 fansUpdate.put("groupid", entity_.getInt("groupid"));
							 flag =  baseMongoTemplate.update(Constants.WXFANS_T, fansQuery, fansUpdate);
						  }
						  if(flag){
								return AjaxUtil.renderSuccessMsg("移动成功!");
						  }
					  }
				}
			   
				
			}
			
		}
		
		return AjaxUtil.renderFailMsg("操作失败！");
	}
	
	
	
	/**
	 * 
	* @Description:添加分组
	* @param @param entity
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "fansGroup", method = RequestMethod.POST)
	@ResponseBody
	public String addFansGroup(@RequestBody FansGroup entity) {
		
		UserService service = new UserServiceImp();
		if(entity!=null && entity.getAppId()!=null && entity.getName()!=null){
			
			//分组不能重名
			Map<String,Object> mapQuery = new HashMap<String, Object>();
			mapQuery.put("appId", entity.getAppId());
			mapQuery.put("name", entity.getName());
			if(baseMongoTemplate.count(Constants.WXFANSGROUP_T, mapQuery)>0){
				return AjaxUtil.renderFailMsg("分组名称已存在，请重新输入！");
			}
			mapQuery.remove("name");
			if(baseMongoTemplate.count(Constants.WXFANSGROUP_T, mapQuery)>49){
				return AjaxUtil.renderFailMsg("分组已达到上限！");
			}
			
			
			
			//Map<String,Object> account = wxAccountService.getAccountByAppId(entity.getAppId());
			String accessToken =  tokenService.getAccessTokenByRedis( entity.getAppId());
			
			if(accessToken != null ){
				JSONObject result  =  service.createGroup(accessToken,  entity.getName(), entity.getAppId());
				if(result.containsKey("group")){//添加成功    同步到数据库
					Map<String,Object> en = new HashMap<String, Object>();
					en.put("appId", entity.getAppId());
					en.put("name", entity.getName());
					en.put("count", 0);
					en.put("groupid", result.getJSONObject("group").getInt("id"));
					Map<String,Object> resultEntity =  baseMongoTemplate.save(Constants.WXFANSGROUP_T, en);
					if(resultEntity!=null){
						resultEntity.put("id", resultEntity.get("_id")+"");
						return AjaxUtil.renderSuccessMsg(resultEntity);
					}
				}
			}
		}
		return AjaxUtil.renderFailMsg("操作失败！");
	}
	
	
	/**
	 * 
	* @Description:删除分组
	* 注意本接口是删除一个用户分组，删除分组后，所有该分组内的用户自动进入默认分组。 接口调用请求说明 
	* @param @param entity
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "fansGroup", method = RequestMethod.DELETE)
	@ResponseBody
	public String delFansGroup(String id,String appId,String groupId) {
		
		UserService service = new UserServiceImp();
		if(appId!=null && id!=null && groupId!=null){
			//Map<String,Object> account = wxAccountService.getAccountByAppId(appId);
			String accessToken =  tokenService.getAccessTokenByRedis(appId);
			
			if(accessToken != null){
				JSONObject result  =  service.deleteGroup(accessToken, Integer.parseInt(groupId),appId);
				if(result.containsKey("errcode") && result.getInt("errcode")==0){//删除成功    同步到数据库（将数据库该分组下的用户移动到默认分组） ||或者从新拉取用户信息
					Map<String,Object> delQuery = new HashMap<String, Object>();
					delQuery.put("_id", new ObjectId(id));
					if(baseMongoTemplate.deleteData(Constants.WXFANSGROUP_T, delQuery)){
						//将数据库该分组下的用户移动到默认分组
						Update update = new Update();
						update.set("groupid", 0);
						Query query = new Query();
						Criteria criteria=Criteria.where("appId").is(appId).and("groupid").is(Integer.parseInt(groupId));
				    	query.addCriteria(criteria);
				    	if(mongoTemplate.count(query, Constants.WXFANS_T)>0){
							if(baseMongoTemplate.update(query, update, Constants.WXFANS_T)){
								return AjaxUtil.renderSuccessMsg("删除成功");
							}
				    	}
				    	return AjaxUtil.renderSuccessMsg("删除成功");
					}
				}
			}
		}
		return AjaxUtil.renderFailMsg("操作失败！");
	}
	
	
	
	
	/**
	 * 
	* @Description:修改分组
	* @param @param entity
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "fansGroup", method = RequestMethod.PUT)
	@ResponseBody
	public String editFansGroup(@RequestBody FansGroup entity) {
		UserService service = new UserServiceImp();
		if(entity!=null && entity.getAppId()!=null && entity.getName()!=null){
			
			
			Map<String, Object> mapQuery = new HashMap<String, Object>();
			mapQuery.put("_id", new ObjectId(entity.getId()));
			Map<String,Object> fansGroup = baseMongoTemplate.findOne(Constants.WXFANSGROUP_T, mapQuery);
			if(fansGroup != null){
				if(!entity.getName().equals(fansGroup.get("name")+"")){
					//判断修改时分组名是否重复
					mapQuery = new HashMap<String,Object>();
					mapQuery.put("name", entity.getName());
					mapQuery.put("appId", entity.getAppId());
					if(baseMongoTemplate.count(Constants.WXFANSGROUP_T, mapQuery)>1){
						return AjaxUtil.renderFailMsg("该分组名称已存在，请重新输入！");
					}
				}
			}
			String accessToken =  tokenService.getAccessTokenByRedis(entity.getAppId());
			//Map<String,Object> account = wxAccountService.getAccountByAppId(entity.getAppId());
			if(accessToken != null){
				JSONObject result   = service.editGroup(accessToken, entity.getGroupid(), entity.getName(),entity.getAppId());
				if(result.containsKey("errcode") && result.getInt("errcode") == 0){//添加成功    同步到数据库

					Query query = new Query();
					Criteria criteria=Criteria.where("_id").is(new ObjectId(entity.getId()));
			    	query.addCriteria(criteria);
			    	Update update = new Update();
			    	update.set("name", entity.getName());
					
					boolean flag =  baseMongoTemplate.updateOne(query, update, Constants.WXFANSGROUP_T);
					if(flag){
						return AjaxUtil.renderSuccessMsg(entity);
					}
				}
			}
		}
		return AjaxUtil.renderFailMsg("操作失败！");
	}
	
	
	/**
	 * 
	* @Description:修改用户备注
	* @param @param entity
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "fansInfo", method = RequestMethod.PUT)
	@ResponseBody
	public String editFansInfo(@RequestBody Map<String,Object> entity) {
		UserService service = new UserServiceImp();
		if(entity!=null && entity.get("appId")!=null && entity.get("remark")!=null){
			//Map<String,Object> account = wxAccountService.getAccountByAppId(entity.get("appId")+"");
			
			String accessToken =  tokenService.getAccessTokenByRedis( entity.get("appId")+"");
			if(accessToken != null){
				JSONObject result   = service.setUserRemark(accessToken, entity.get("openid")+"", entity.get("remark")+"",entity.get("appId")+"");
				if(result.containsKey("errcode") && result.getInt("errcode") == 0){//添加成功    同步到数据库
					Query query = new Query();
					Criteria criteria=Criteria.where("_id").is(new ObjectId(entity.get("id")+""));
			    	query.addCriteria(criteria);
			    	Update update = new Update();
			    	update.set("remark", entity.get("remark")+"");
					boolean flag =  baseMongoTemplate.updateOne(query, update, Constants.WXFANS_T);
					if(flag){
						return AjaxUtil.renderSuccessMsg(entity);
					}
				}
			}
		}
		return AjaxUtil.renderFailMsg("操作失败！");
	}
	
	
	
	
	/**
	 * 
	* @Description: 获取粉丝列表
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "fansList", method = RequestMethod.GET)
	@ResponseBody
	public String fansList(String appId,String filter,String groupId,String pageNum,String pageSize) {
		try {
			if(appId != null){
				int pageSize_ = pageSize==null?20:Integer.parseInt(pageSize);
				int pageNum_  =  pageNum==null?1:Integer.parseInt(pageNum);
				filter = filter==null?"":filter;
				Query querys = new Query();
			    Criteria cr = new Criteria();
			    querys.addCriteria(cr.orOperator(
						    Criteria.where("nickname").regex(filter)));
			    querys.addCriteria(Criteria.where("appId").is(appId));
				if(groupId != null){
					 querys.addCriteria(Criteria.where("groupid").is(Integer.parseInt(groupId)));
				}
			    long totalRows =  mongoTemplate.count(querys, Constants.WXFANS_T);
			    long totalPages = 0;
			    if ((totalRows % pageSize_) == 0) {
		            totalPages = totalRows / pageSize_;
		        } else {
		            totalPages = totalRows / pageSize_ + 1;
		        }
			    querys.skip((pageNum_ - 1) * pageSize_).limit(pageSize_);
			    querys.with(new Sort(Direction.DESC , "subscribe_time"));
			    
				List<HashMap> lists =   baseMongoTemplate.findByQuery(querys, Constants.WXFANS_T);
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
			    if(lists != null){
			    	for(HashMap ma:lists){
			    		ma.put("id", ma.get("_id")+"");
			    		ma.put("subscribe_time", df.format(new Date( Long.parseLong(ma.get("subscribe_time")+"") *1000 )) );
			    	}
			    	JSONObject result = new JSONObject();
					result.put("dataList", lists);
					result.put("totalPages", totalPages);
					result.put("totalRows", totalRows);
					return AjaxUtil.renderSuccessMsg(result);
			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("获取粉丝分组信息出错！");
	}
	
	@RequestMapping(value = "syncfans", method = RequestMethod.GET)
	@ResponseBody
	public String syncFansInfo(String appId) {
		try {
			fancService.syncJobDo(appId);
			return AjaxUtil.renderSuccessMsg("粉丝信息同步job启动成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxUtil.renderFailMsg("同步失败！");
		}
		
		
	}
	
	@RequestMapping(value = "getFansCount", method = RequestMethod.GET)
	@ResponseBody
	public String getFansCount(String appId) {
		try {
			if(appId != null){
				Map<String,Object> result =  fancService.getFansCount(appId);
				return AjaxUtil.renderSuccessMsg(result);
			}
			return AjaxUtil.renderFailMsg("参数错误");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxUtil.renderFailMsg("操作失败！");
		}
		
		
	}
	
	
	

}

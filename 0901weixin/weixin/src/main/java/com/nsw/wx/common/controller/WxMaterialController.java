package com.nsw.wx.common.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nsw.wx.api.model.MediaListPara;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.security.CustomSecurityUser;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.service.WxMaterialService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;
import com.nsw.wx.common.util.DateUtils;
import com.nsw.wx.common.views.Message;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年6月27日 下午5:22:51
* @Description: TODO
 */

@Controller
@RequestMapping("/wxMaterial")
public class WxMaterialController {
	
	MaterialService materiralService = new MaterialServiceImp();
	
	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	private WxMaterialService wxMaterialService;
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	/**
	 * 素材同步： 将微信素材同步到平台
	 * @param request
	 * @param response
	 * @param appId
	 * @param type  (image|news)
	 * @return
	 */
	@RequestMapping(value = "/sync", method = RequestMethod.GET)
	@ResponseBody
	public String wxMaterialList(HttpServletRequest request,HttpServletResponse response, String appId,String type) {
		if(appId !=null && ("image".equals(type) || "news".equals(type) )){
			Message  msg  =wxMaterialService.SyncWxMaterial(appId, type);
			if(msg.getIsSuc()){
				return AjaxUtil.renderSuccessMsg("同步成功!");
			}else{
				return AjaxUtil.renderSuccessMsg(msg.getMsg());
			}
		}
		return AjaxUtil.renderFailMsg("操作失败!");
	}
	
	
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public String materialList(HttpServletRequest request,
			HttpServletResponse response, String appId,String filter, String pageNum,String pageSize){
			try {
				int pageSize_ = pageSize==null?20:Integer.parseInt(pageSize);
				int pageNum_  =  pageNum==null?1:Integer.parseInt(pageNum);
				long totalPages=0;//总页数
				if(appId != null){
					filter = filter==null?"":filter;
					Query querys = new Query();
				     Criteria cr = new Criteria();
				     querys.addCriteria(cr.orOperator(
							    Criteria.where("articles.title").regex(filter))); 
				     querys.addCriteria(Criteria.where("appId").is(appId).and("type").is("news"));
				    long totalRows=  mongoTemplate.count(querys, Constants.WXNEWS_T);
				    if ((totalRows % pageSize_) == 0) {
			            totalPages = totalRows / pageSize_;
			        } else {
			            totalPages = totalRows / pageSize_ + 1;
			        }
					Query query = new Query();
					Criteria criteria=Criteria.where("appId").is(appId).and("type").is("news").and("articles.title").regex(filter);
			    	query.addCriteria(criteria);
			    	query.skip((pageNum_ - 1) * pageSize_).limit(pageSize_);
					query.with(new Sort(Direction.DESC , "update_time"));
					List<HashMap> list =   baseMongoTemplate.findByQuery(query, Constants.WXNEWS_T);
					if(list!= null){
						for(Map<String,Object> w: list){
							w.put("id",w.get("_id")+"");
							if(w.get("create_time")==null){
								w.put("create_time", DateUtils.getCurrentTime());
							}
						}
					}else{
						list = new ArrayList<HashMap>();
					}
					JSONObject result = new JSONObject();
					result.put("dataList", list);
					result.put("totalPages", totalPages);
					result.put("totalRows", totalRows);
					return AjaxUtil.renderSuccessMsg(result);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return AjaxUtil.renderFailMsg("获取图文失败！");
	
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 获取素材列表并分页
	 * @param request
	 * @param response
	 * @param appId
	 * @param type   (image|news)
	 * @param filter
	 * @param pageNum
	 * @param pageSize
	 * @return
	 *//*
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public String materialList(HttpServletRequest request,
			HttpServletResponse response, String appId,String type,String filter, String pageNum,String pageSize) {
		
			 int pageNum_ = pageNum==null?1:Integer.parseInt(pageNum);
		     int pageSize_  = pageSize==null?20:Integer.parseInt(pageSize);
		     long totalPages=0;//总页数
		     if(appId != null && ( "image".equals(type) || "news".equals(type))){
		    	long totalRows = wxMaterialService.wxMaterialCount(appId, type,filter) ;//总记录数
	    		//计算总页数
	    		totalPages = (totalRows % pageSize_) == 0 ?   totalRows / pageSize_ :   totalRows / pageSize_ + 1;
	    		List<HashMap>  list = wxMaterialService.listMaterial(appId, type, filter, pageNum_, pageSize_);
	    		for(HashMap map: list){
	    			String update_time = map.get("update_time")+"";
	    			String create_time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(update_time)*1000));  
	    			map.put("create_time", create_time);
	    			map.put("id", map.get("_id")+"");
	    		}
	    		JSONObject result = new JSONObject();
				result.put("dataList", list);
				result.put("totalPages", totalPages);
				result.put("totalRows", totalRows);
				return AjaxUtil.renderSuccessMsg(result);
		     }
		     return AjaxUtil.renderFailMsg("获取数据失败!");
	}*/
	
/*	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public String materialList(HttpServletRequest request,
			HttpServletResponse response, String appId,String id) {
			if(appId != null && id != null){
				Map<String,Object> query = new HashMap<String, Object>();
				query.put("appId", appId);
				query.put("_id", new ObjectId(id));
				Map<String,Object> entity =  baseMongoTemplate.findOne( Constants.WXSYNCMATERIAL, query);
				if(entity != null ){
					entity.put("id", entity.get("_id")+"");
					return AjaxUtil.renderSuccessMsg(entity);
				}
			}
		     return AjaxUtil.renderFailMsg("获取数据失败!");
	}*/
	
	
	
}

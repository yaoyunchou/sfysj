package com.nsw.wx.common.controller;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.security.CustomSecurityUser;
import com.nsw.wx.common.service.MenuService;
import com.nsw.wx.common.service.MongoFileOperationService;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;
import com.nsw.wx.common.util.DateUtils;
import com.nsw.wx.common.util.FileUtil;
import com.nsw.wx.common.views.Message;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月16日 下午4:52:57
* @Description: 素材管理控制器
 */
@Controller
@RequestMapping("/material")
public class MaterialController {
	
	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	BaseMongoTemplate basemongoTemplate;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoFileOperationService mongoFileOperationService;
	
	@Autowired
	private MenuService menuService ;

	
	private Logger logger = Logger.getLogger(MaterialController.class);



	/**
	 * @Description: 素材文件保存接口
	 * @param @return
	 * @return 获取token
	 * @throws
	 * @author wuk
	 * filename 图片服务器的图片路径的一部分
	 * originName  原始图片的名称
	 */
	@RequestMapping(value = "/saveFile", method = RequestMethod.POST)
	@ResponseBody
	public String saveFile(@RequestBody List<Map> listData,HttpServletRequest request){

		if(listData != null && listData.size() > 0){
			for(Map data : listData){
				String appId = StringUtils.isEmpty((String)data.get("appId")) ? "":(String)data.get("appId");
				Map<String,Object> metaData = new HashedMap();
				metaData.put("appId",appId);
				metaData.put("site", ContextUtil.getSite());
				metaData.put("contentType", "image/png");
				metaData.put("name", data.get("originName"));
				metaData.put("fileId", data.get("fileName"));
				metaData.put("update_time",  DateUtils.getCurrentTime());
				metaData.put("create_time", DateUtils.getCurrentTime());
				Map<String,Object> result_ = basemongoTemplate.save(Constants.WXIMAGE_T, metaData);
			}
			return AjaxUtil.renderSuccessMsg("图片上传成功！");
		}
		return AjaxUtil.renderFailMsg("图片上传失败！");
	}





	/**
	 * 
	* @Description: 素材上传接口
	* @param @param request
	* @param @param response
	* @param @param appId
	* @param @param file
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/image/upload", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFileImage(HttpServletRequest request,
			HttpServletResponse response, String appId,
			@RequestParam("file") MultipartFile file) {
		
		if(file.getSize()>2*1048576){
			return AjaxUtil.renderFailMsg("文件不能超过2M！");
		}
		//原始文件名
		String originalFilename = file.getOriginalFilename(); 
		//后缀名
		String ectName = originalFilename.substring(originalFilename.lastIndexOf("."));
		//当前字段定义名称如：菊花.jpg
		//String projId = ContextUtil.getCurrentProject();
		//前缀名
		String filePrefix = "wx_material"+(appId==null?"":appId) ;
		DBObject metaData = new BasicDBObject();
		//metaData.put("projId",projId);
		metaData.put("site", ContextUtil.getSite());
		metaData.put("contentType", file.getContentType());
		metaData.put("name", originalFilename);
		metaData.put("appId", appId);
		try {
			String filename = filePrefix +UUID.randomUUID().toString().replaceAll("-", "")+ectName;
			logger.info(filename);
			Map<String, Object> map = mongoFileOperationService.addGridfs(file.getInputStream(), filename, metaData, FileUtil.getFileType(originalFilename));
			logger.info(map);
			if(map != null && !map.isEmpty()){
				Map<String ,Object>  image = new HashMap<String, Object>();
				image.put("fileId", map.get("fileId"));
				image.put("url", filename);
				image.put("appId", appId);
				image.put("name", originalFilename);
				image.put("update_time",  DateUtils.getCurrentTime());
				image.put("create_time", DateUtils.getCurrentTime());
				Map<String,Object> result_ = basemongoTemplate.save(Constants.WXIMAGE_T, image);//保存文件信息到基础表的resources
				if(result_!=null){
					result_.put("id", result_.get("_id")+"");
					return   AjaxUtil.renderSuccessMsg(result_);
				}else{
					AjaxUtil.renderFailMsg("素材上传失败！");
				}
			}else{
				return AjaxUtil.renderFailMsg("图片上传失败！");
			}
		} catch (IllegalStateException | IOException e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("图片上传失败！");
		}
		return AjaxUtil.renderFailMsg("素材上传失败！");
	}
	
	
	
	
	/**
	 * 
	* @Description: 获取当前公众号图片的接口
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "materialInfoImage", method = RequestMethod.GET)
	@ResponseBody
	public String materialInfoImage(String appId,String filter,String pageNum,String pageSize) {
		
		     int pageNum_ = pageNum==null?1:Integer.parseInt(pageNum);
		     int pageSize_  = pageSize==null?20:Integer.parseInt(pageSize);
		     long totalPages=0;//总页数
		     if(appId!=null){
		    	 CustomSecurityUser securityUser = ContextUtil.getLoginUser();
			     List<Map<String,Object>> accountList =   wxAccountService.getWxAccountList(securityUser.getSite());
		    	 if(accountList!=null && accountList.size() > 0){
		    		 ArrayList<String> appIdList = new ArrayList<>();
		    		 for(Map<String,Object> a:accountList){
		    			 appIdList.add(a.get("appId")+"");
		    		 }
		    		 filter = filter==null?"":filter;
					 Query querys = new Query();
				     Criteria cr = new Criteria();
				     querys.addCriteria(cr.orOperator(
				    Criteria.where("name").regex(filter)));
				     querys.addCriteria(Criteria.where("appId").in(appIdList));
				     long totalRows= mongoTemplate.count( querys,Constants.WXIMAGE_T);//总记录数
				     if ((totalRows % pageSize_) == 0) {
				            totalPages = totalRows / pageSize_;
				        } else {
				            totalPages = totalRows / pageSize_ + 1;
				     }
					try {
							Query query = new Query();
							Criteria criteria=Criteria.where("appId").in(appIdList).and("name").regex(filter);
					    	query.addCriteria(criteria);
							query.skip((pageNum_ - 1) * pageSize_).limit(pageSize_);
							query.with(new Sort(Direction.DESC , "create_time"));
							List<HashMap> list =     basemongoTemplate.findByQuery(query, Constants.WXIMAGE_T);
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
		    	 
	    		
		     }
		    
			 return AjaxUtil.renderFailMsg("获取素材图片失败！");
	}
	
	/**
	 * 
	* @Description: 获取当前公众号图文的接口
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "materialInfoNews", method = RequestMethod.GET)
	@ResponseBody
	public String materialInfoNews(String appId,String filter,String pageNum,String pageSize) {
		try {
			int pageSize_ = pageSize==null?20:Integer.parseInt(pageSize);
			int pageNum_  =  pageNum==null?1:Integer.parseInt(pageNum);
			  long totalPages=0;//总页数
			if(appId != null){
				 CustomSecurityUser securityUser = ContextUtil.getLoginUser();
			     List<Map<String,Object>> accountList =   wxAccountService.getWxAccountList(securityUser.getSite());
		    	 if(accountList!=null && accountList.size() > 0){
		    		 ArrayList<String> appIdList = new ArrayList<>();
		    		 for(Map<String,Object> a:accountList){
		    			 appIdList.add(a.get("appId")+"");
		    		 }
						
						filter = filter==null?"":filter;
						Query querys = new Query();
					     Criteria cr = new Criteria();
					     querys.addCriteria(cr.orOperator(
								    Criteria.where("articles.title").regex(filter)));
					     querys.addCriteria(Criteria.where("appId").in(appIdList).and("type").exists(false));
					    long totalRows=  mongoTemplate.count(querys, Constants.WXNEWS_T);
					    
					    if ((totalRows % pageSize_) == 0) {
				            totalPages = totalRows / pageSize_;
				        } else {
				            totalPages = totalRows / pageSize_ + 1;
				        }
						Query query = new Query();
						Criteria criteria=Criteria.where("appId").in(appIdList).and("type").exists(false).and("articles.title").regex(filter);
				    	query.addCriteria(criteria);
				    	query.skip((pageNum_ - 1) * pageSize_).limit(pageSize_);
						query.with(new Sort(Direction.DESC , "create_time"));
				    	
						List<HashMap> list =     basemongoTemplate.findByQuery(query, Constants.WXNEWS_T);
						if(list != null){
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
		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return AjaxUtil.renderFailMsg("获取图文失败！");
		
		
	}
	
	
	/**
	 * 
	* @Description: 获取当前公众号素材的（适用于所有素材）
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "materialInfo", method = RequestMethod.GET)
	@ResponseBody
	public String materialInfo(String type,String id) {
		try {
			if(type != null && id != null){
		    	Map<String,Object> map = new HashMap<String, Object>();
		    	if("image".equalsIgnoreCase(type)){
		    		map.put("fileId",id);
		    		Map<String,Object> image = basemongoTemplate.findOne( Constants.WXIMAGE_T, map);
		    		if(image != null){
		    			image.put("id", id);
		    			return AjaxUtil.renderSuccessMsg(image);
		    		}
		    	}else if("news".equalsIgnoreCase(type)){
		    		map.put("_id", new ObjectId(id));
		    		Map<String,Object> news =   basemongoTemplate.findOne( Constants.WXNEWS_T, map);
		    		if(news != null ){
		    			news.put("id", id);
		    			return AjaxUtil.renderSuccessMsg(news);
		    		}
		    	}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return AjaxUtil.renderFailMsg("获取信息失败！");
		
	}
	
	
	
	
	/**
	 * 
	* @Description: 删除当前公众号素材的（适用于所有素材）
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "materialInfo", method = RequestMethod.DELETE)
	@ResponseBody
	public String materialInfoDel(String type,String id,String appId) {
		try {
			if(type != null && id !=null ){
				if(("".equals(id))){
					return AjaxUtil.renderFailMsg("请勾选要删除的数据!");
				}
				boolean flag = false;
		    	if("image".equalsIgnoreCase(type)){
		    		String ids[] = id.split(",");
		    		List<Map<String,Object>> mapList = new ArrayList<>();
					for (int i = 0; i < ids.length; i++) {
						   Map<String,Object> map = new HashMap<>();
						   map.put("_id", new ObjectId(ids[i]));
						   Map<String,Object> en =  basemongoTemplate.findOne(Constants.WXIMAGE_T, map);
						   if(en!=null){
							   mapList.add(map);
							  /* Message message = 	checkMaterialIsUse(appId, en.get("fileId")+"",type);
							   if(!message.getIsSuc()){
								   return AjaxUtil.renderFailMsg(message.getMsg());
							   }*/
						   }
					}
					flag=  basemongoTemplate.deleteBatch(Constants.WXIMAGE_T, mapList);
		    	}else if("news".equalsIgnoreCase(type)){
		    		//删除图文素材时，需要去检测是否被引用（菜单，关键词回复，等），被引用状态时不能删除。
		    	   Message message = 	checkMaterialIsUse(appId, id,type);
		    		
		    		if(message.getIsSuc()){
			    	    Query query = new Query();
			    		Criteria criteria=Criteria.where("_id").is(new ObjectId(id));
				    	query.addCriteria(criteria);
			    		 WriteResult   result =   mongoTemplate.remove(query,Constants.WXNEWS_T);
			    		 flag =   result.getN()==1?true:false;
		    		}else{
		    			return AjaxUtil.renderFailMsg(message.getMsg());
		    		}
		    	}
		    	if(flag){
		    		return AjaxUtil.renderSuccessMsg("删除成功");
		    	}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return AjaxUtil.renderFailMsg("删除失败！");
		
	}
	
	
	
	
	/**
	 * 
	* @Description: 素材上传(图片)
	* @param @param request
	* @param @param response
	* @param @param zdyName
	* @param @param file
	* @param @return { media_id  新增的永久素材的media_id  url  新增的图片素材的图片URL（仅新增图片素材时会返回该字段） }
 
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(HttpServletRequest request,
			HttpServletResponse response, String appId,
			@RequestParam("file") MultipartFile file) {
		try {
			Map<String,Object> account = wxAccountService.getAccountByAppId(appId);
			MaterialService service = new MaterialServiceImp();
			JSONObject object =  service.addMatter(account.get("authorizer_access_token")+"", file.getInputStream(),file.getName(),"image",appId);//null为上传的不是视频文件
			System.out.println("上传素材返回结果"+object);
			if(!object.containsKey("errcode")){
				return AjaxUtil.renderSuccessMsg(object);
			}
			
		} catch (Exception e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("图片上传失败！");
		}
		return AjaxUtil.renderFailMsg("上传失败");
	}
	
	/**
	 * 
	* @Description: 上传图文
	* @param @param request
	* @param @param response
	* @param @param wxNews
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/materialInfoNews", method = RequestMethod.POST)
	@ResponseBody
	public String materialInfoNews(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String,Object> wxNews) {
		
		try {
			if(wxNews.get("appId") != null){
				Map<String,Object> entity = new HashMap<String, Object>();
				ObjectId  id = new ObjectId();
				entity.put("appId", wxNews.get("appId")+"");
				entity.put("articles", wxNews.get("articles"));
				entity.put("update_time", DateUtils.getCurrentTime());
				entity.put("create_time", DateUtils.getCurrentTime());
				Map<String,Object> m  =  basemongoTemplate.save(Constants.WXNEWS_T, entity);
				m.put("id", m.get("_id")+"");
				return AjaxUtil.renderSuccessMsg(m);
			}
		} catch (Exception e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("图文上传失败！");
		}
		return AjaxUtil.renderFailMsg("添加失败");
		
		
	}
	
	/**
	 * 
	* @Description: 修改图文
	* @param @param request
	* @param @param response
	* @param @param wxNews
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/materialInfoNews", method = RequestMethod.PUT)
	@ResponseBody
	public String materialInfoNewsUpdate(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String,Object> wxNews) {
		
		try {
			if(wxNews.get("appId") != null){
				Query query = new Query();
				Criteria criteria=Criteria.where("_id").is(new ObjectId(wxNews.get("id")+""));
		    	query.addCriteria(criteria);
		    	Update update = new Update();
		    	update.set("articles", wxNews.get("articles"));
		    	update.set("update_time", DateUtils.getCurrentTime());
		    	boolean results =   basemongoTemplate.updateFirst(query, update, Constants.WXNEWS_T);
				if(results){
					return AjaxUtil.renderSuccessMsg(wxNews);
				}
			}
		} catch (Exception e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("图文修改失败！");
		}
		return AjaxUtil.renderFailMsg("修改失败");
		
		
	}
	
	/**
	 * 
	* @Description: 修改图片素材
	* @param @param appId
	* @param @param pageNum
	* @param @param pageSize
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "materialInfoImage", method = RequestMethod.PUT)
	@ResponseBody
	public String materialInfoImage(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String,Object> wxNews) {

		try {
			if(wxNews.get("appId") != null){
				Query query = new Query();
				Criteria criteria=Criteria.where("_id").is(new ObjectId(wxNews.get("id")+""));
		    	query.addCriteria(criteria);
		    	Update update = new Update();
		    	update.set("name", wxNews.get("name"));
		    	update.set("update_time", DateUtils.getCurrentTime());
		    	
		    	boolean results =   basemongoTemplate.updateFirst(query, update, Constants.WXIMAGE_T);
				if(results){
					return AjaxUtil.renderSuccessMsg(wxNews);
				}
			}
		} catch (Exception e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("图片修改失败！");
		}
		return AjaxUtil.renderFailMsg("修改失败");
		 
		
	}
	
	
	
	/**
	 * 
	* @Description: 判断要删除的素材是否被引用
	* @param @param mapEntity
	* @param @return   
	* @return boolean  
	* @throws
	 */
	public Message  checkMaterialIsUse(String appId, String key,String type){
		
		Message message = new Message();
		
		 CustomSecurityUser securityUser = ContextUtil.getLoginUser();
	     List<Map<String,Object>> accountList =   wxAccountService.getWxAccountList(securityUser.getSite());
    	 if(accountList!=null && accountList.size() > 0){
    		 ArrayList<String> appIdList = new ArrayList<>();
    		 for(Map<String,Object> a:accountList){
    			 appIdList.add(a.get("appId")+"");
    		 }
    		 Criteria criteria=Criteria.where("appId").in(appIdList);
    		 Query q1 = new Query(criteria);
    	     q1.addCriteria(new Criteria().orOperator(Criteria.where("button.key").is(key),
			         criteria.where("button.sub_button.key").is(key)));
    		 //1.判断是否被菜单引用
    	     if(mongoTemplate.count(q1, Constants.WXMENU_T)>0){
    	    	 message.setMsg("素材被'菜单'引用,不能删除!");
    			 message.setIsSuc(false);
    			 return message;
    	     }
    	     

    	   //  q2.addCriteria(Criteria.where("button.key").is(key).orOperator(criteria.where("button.sub_button.key").is(key)));
    	   /*  if("image".equalsIgnoreCase(type)){
    	    	 q2.addCriteria(Criteria.where("fileId").is(key));
    	    	 if(mongoTemplate.count(q2, Constants.WXKEYWORD_T)>0){
    	    		 message.setMsg("图片已被使用,无法删除!");
    	 			 message.setIsSuc(false);
    	 			 return message;
    	    	 }
    	    	 
    	     }else */if("news".equalsIgnoreCase(type)){
			     Query q2 = new Query(criteria);
    	    	 q2.addCriteria(Criteria.where("mediaId").is(key));
    	    	 if(mongoTemplate.count(q2, Constants.WXKEYWORD_T)>0){
    	    		 message.setMsg("素材被'自动回复'引用,不能删除!");
    	 			 message.setIsSuc(false);
    	 			 return message;
    	    	 }
    	     }
    	     Query q3 = new Query(criteria);
    	     q3.addCriteria(new Criteria().orOperator(Criteria.where("fileId").is(key),criteria.where("mediaId").is(key)));
    	    if(mongoTemplate.count(q3, Constants.WXMASSMSG_T)>0){
    	    	message.setMsg("素材被'群发'引用,不能删除!");
    			message.setIsSuc(false);
    			 return message;
    	    }

		     Query q4 = new Query(criteria);
		     q4.addCriteria(Criteria.where("replyContent").is(key));
			 if(mongoTemplate.count(q4, Constants.WXQRCODE_T)>0){
				 message.setMsg("图文被'二维码回复'引用,不能删除!");
				 message.setIsSuc(false);
				 return message;
			 }

    	}else{
    		message.setIsSuc(false);
    		message.setMsg("删除失败!");
    		return message;
    	}
    	 message.setIsSuc(true);
	  return message;
}
}

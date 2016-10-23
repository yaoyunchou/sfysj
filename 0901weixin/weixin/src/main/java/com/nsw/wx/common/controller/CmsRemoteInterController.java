package com.nsw.wx.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nsw.wx.common.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.nsw.cms.common.service.WeiXinRemotingService;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.security.CustomSecurityUser;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.service.WxMaterialService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月21日 上午9:45:41
* @Description: cms远程接口，调用相关项目信息接口
 */
@Controller
@RequestMapping("/cms")
public class CmsRemoteInterController {
	
	private Logger logger = Logger.getLogger(CmsRemoteInterController.class);
	
	@Autowired
	private WeiXinRemotingService weiXinRemotingService;
	
	@Value("${cmsRemoteUrl}")
	private String cmsRemoteUrl;
	
	@Value("${wxurl}")
	private String wxurl;
	
	@Autowired
	private WxMaterialService wxMaterialService;
	
	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	BaseMongoTemplate basemongoTemplate;
	
	@Autowired
	MongoTemplate mongoTemplate;


	@Autowired
	private UserService userService;
	
	
	//public final static String NSWCMS = "/nswcms/";
	
	
	/**
	 * 
	* @Description: 获取当前企业开通的 pc手机等项目信息
	* @param @param request
	* @param @param responsex
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getProjects", method = RequestMethod.GET)
	@ResponseBody
	public String getProjects(HttpServletRequest request,
			HttpServletResponse responsex) {
		try {
			CustomSecurityUser securityUser = ContextUtil.getLoginUser();
			String site = 	ContextUtil.getSite();
			String username = securityUser.getUsername();
			Map result = 	getProjectLists(site);
			JSONObject json = JSONObject.fromObject(result);
			if(json.containsKey("projects")){
				JSONArray jsonArr = json.getJSONArray("projects");
				for(int i=0;i<jsonArr.size();i++){
					JSONObject jb = jsonArr.getJSONObject(i);
					jb.remove("lastUpdTime");
					jb.remove("createTime");
					//jb.remove("projName");
					jb.remove("Id");
					jb.remove("enterpriseId");
					String projId = jb.getString("projId");
					if(jb.getInt("projectType")==4||jb.getInt("projectType")==5){
						jb.put("url", cmsRemoteUrl+"user/remoteSwitchProject?username="+username+"&projId="+projId);
						jb.put("projName", jb.getInt("projectType")==4?"PC网站":"手机网站");
					}else if(jb.getInt("projectType")==7){
						jb.put("url",wxurl + Constants.defaultTargetUrl);
						jb.put("projName","微信管家");
					}
				}
				result.put("projects", jsonArr);
			}
			return AjaxUtil.renderSuccessMsg(result);
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return AjaxUtil.renderFailMsg("获取信息失败！");
	}

	public Map getProjectLists(String enterpriseId){
		List<Integer> types = new ArrayList<Integer>();
		types.add(4);
		types.add(5);
		types.add(9);
		types.add(7);
		List  projects = userService.getProjectList(enterpriseId, types);
		Map map = new HashMap();
		map.put("projects", projects);
		return map;
	}
	
	
	
	/**
	 * 
	* @Description: 获取当前企业开通的 pc手机等项目信息
	* @param @param request
	* @param @param responsex
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getProjectLists", method = RequestMethod.GET)
	@ResponseBody
	public String getProjectLists(HttpServletRequest request,
			HttpServletResponse responsex) {
		try {
			String site = 	ContextUtil.getSite();
			Map result = 	getProjectLists(site);
			if(result==null){
				result = new HashMap();
			}
			JSONObject result_ = JSONObject.fromObject(result);
			if(result_.containsKey("projects")){
				JSONArray arr = result_.getJSONArray("projects");
				for(int i=0;i<arr.size();i++){
					if(arr.getJSONObject(i).getInt("projectType")==5){
						return AjaxUtil.renderSuccessMsg(arr.getJSONObject(i).getString("projId"));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return AjaxUtil.renderFailMsg("企业没有开通手机网站！");
	}
	

	
	
	/**
	 * 
	* @Description: 根据项目id获取模块列表
	* @param @param request
	* @param @param response
	* @param @param projId
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getModuleLists", method = RequestMethod.GET)
	@ResponseBody
	public String getModuleLists(HttpServletRequest request,
			HttpServletResponse response,String projId) {
		try {
			Map result = 	weiXinRemotingService.getModuleLists(projId);
			if(result==null){
				result = new HashMap();
			}
			return AjaxUtil.renderSuccessMsg(result);
		} catch (Exception e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("获取模块列表失败！");
		}
	}
	
	/**
	 * 
	* @Description: 根据项目和模块获取分类列表
	* @param @param request
	* @param @param response
	* @param @param projId
	* @param @param moduleId
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getCtgLists", method = RequestMethod.GET)
	@ResponseBody
	public String getCtgLists(HttpServletRequest request,
			HttpServletResponse response,String projId,String moduleId) {
		
		try {
			Map result = 	weiXinRemotingService.getCtgLists(projId, moduleId);
			if(result==null){
				result = new HashMap();
			}
			return AjaxUtil.renderSuccessMsg(result);
		} catch (Exception e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("获取分类列表失败！");
		}
	}
	
	
	/**
	 * 
	* @Description: 根据项目，模块，分类 和 标题   以及分页 获取文章列表
	* @param @param request
	* @param @param response
	* @param @param projId
	* @param @param moduleId
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getArticleLists", method = RequestMethod.GET)
	@ResponseBody
	public String getArticleLists(HttpServletRequest request,
			HttpServletResponse response,String projId,String moduleId,String title,String ctgId,String pageNum,String pageSize) {
		
		try {
			int pageNum_ =pageNum==null?1:Integer.parseInt(pageNum);
			int pageSize_ = pageSize==null?2000:Integer.parseInt(pageSize);
			Map result = 	weiXinRemotingService.getArticleLists(projId, title, moduleId, ctgId, pageNum_, pageSize_);
			logger.info("result"+result);
			if(result==null){
				result = new HashMap();
			}else{
				List<Map<String,Object>>  arr = (List<Map<String, Object>>) result.get("articles");
				for(int i=0;i<arr.size();i++){
					Map<String,Object> j = arr.get(i);
					String type ="article";
					if(!(j.get("imgSm") instanceof Map)){
						type = "product";
					}
					if("article".equals(type)){
						Map<String,Object> json = (Map<String, Object>) j.get("imgSm");
						Object url = json.get("url"); 
						if(url!=null && url.toString().length()>1){
							String FileId = result.get("basePath")+""+url;
							json.put("url", FileId);
							j.put("imgSm", json);
						}
						arr.set(i, j);
					}else{
						List<Map<String,Object>> imageArr = j.get("imgSm")==null?new ArrayList<Map<String,Object>>():(List<Map<String, Object>>) j.get("imgSm");
						if(imageArr.size()>0){
							Map<String,Object> img1 = imageArr.get(0);
							String FileId =  result.get("basePath")+""+img1.get("url");
							img1.put("url", FileId);
							j.put("imgSm", img1);
						}else{
							JSONObject img1  = new JSONObject();
							img1.put("url", "");
							j.put("imgSm", img1);
						}
						//String jsonStr = j.toString().replaceAll("null", "\"\"").replaceAll(null, "\"\"");
						arr.set(i, j);
					}
					
				}
				result.put("articles", arr);
			}
			return AjaxUtil.renderSuccessMsg(result);
		} catch (Exception e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("获取分类列表失败！");
		}
	}
	
	
	
	/**
	 * 
	* @Description: 根据项目，模块，分类 和 标题   以及分页 获取文章列表
	* @param @param request
	* @param @param response
	* @param @param projId
	* @param @param moduleId
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getPublishedArticle", method = RequestMethod.GET)
	@ResponseBody
	public String getPublishedArticle(HttpServletRequest request,
			HttpServletResponse response,String filter,String pageNum,String pageSize) {
		try {
			String cmsRstJson =  getProjectLists(request, response);
			JSONObject cmsRst = JSONObject.fromObject(cmsRstJson);
			int  totalPages = 0;
			if(cmsRst.getBoolean("isSuccess")){
				int pageNum_ =pageNum==null?1:Integer.parseInt(pageNum);
				int pageSize_ = pageSize==null?20:Integer.parseInt(pageSize);
				Map result =  weiXinRemotingService.getPublishedArticle(cmsRst.getString("data"), filter, pageNum_, pageSize_);
//				Map result =  weiXinRemotingService.getPublishedArticle("27465_PC", filter, pageNum_, pageSize_);
				 int totalRows = Integer.parseInt(result.get("count")+"") ;
				 if ((totalRows % pageSize_) == 0) {
			            totalPages = totalRows / pageSize_;
			     } else {
			            totalPages = totalRows / pageSize_ + 1;
			     }
				 result.put("totalRows", result.get("count"));
				 result.put("totalPages", totalPages);
				 result.put("dataList", result.get("articles"));
				 result.remove("articles");
				return AjaxUtil.renderSuccessMsg(result);
			}
//			Map result =  weiXinRemotingService.getPublishedArticle("27465_PC", title, 1, 100);
//			return AjaxUtil.renderSuccessMsg(result);	
			
			Map map = new HashMap();
			map.put("dataList", new ArrayList<>());
			map.put("totalRows", 0);
			map.put("totalPages", 0);
			return AjaxUtil.renderSuccessMsg(map);
			
		} catch (Exception e) {
			logger.error("Error", e);
			return AjaxUtil.renderFailMsg("获取文章列表失败！");
		}
	}
	
	
	
	
	
	/**
	 * 
	* @Description: 获取图片列表
	* @param @param request
	* @param @param response
	* @param @param projId
	* @param @param fileName
	* @param @param title
	* @param @param ctgId
	* @param @param pageNum
	* @param @param pageSize
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/listFiles", method = RequestMethod.GET)
	@ResponseBody
	public String listFiles(HttpServletRequest request,
			HttpServletResponse response,String projId,String filter,String title,String ctgId,String pageNum,String pageSize) {
		try {
				int pageNum_ =pageNum==null?1:Integer.parseInt(pageNum);
				int pageSize_ = pageSize==null?20:Integer.parseInt(pageSize);
				String cmsRstJson =  getProjectLists(request, response);
				JSONObject cmsRst = JSONObject.fromObject(cmsRstJson);
				
				Map result = null;
				try {
					result = weiXinRemotingService.listFiles(new String[]{cmsRst.getString("data")}, filter, pageNum_, pageSize_, ctgId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(result==null){
					result = new HashMap();
				}
				JSONObject result_ = JSONObject.fromObject(result);
					if(result_.containsKey("files")){
						JSONArray arr = result_.getJSONArray("files");
						for(int i=0;i<arr.size();i++){
							JSONObject o = arr.getJSONObject(i);
							if(o.get("url") != null){
								String FileId = result.get("basePath")+""+o.get("url");
							    o.put("fileId", FileId);
							    o.put("id", o.getString("_id"));
							    arr.set(i, o);
							}
						}
						long totalRows = weiXinRemotingService.fileCount(new String[]{projId}, filter, ctgId);
					   long totalPages = 0;
					    if ((totalRows % pageSize_) == 0) {
				            totalPages = totalRows / pageSize_;
				        } else {
				            totalPages = totalRows / pageSize_ + 1;
				        }
						JSONObject results = new JSONObject();
						results.put("dataList", arr);
						results.put("totalPages", totalPages);
						results.put("totalRows", totalRows);
						return AjaxUtil.renderSuccessMsg(results);
						
					}
			
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return AjaxUtil.renderFailMsg("获取分类列表失败！");
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/listImageSize", method = RequestMethod.GET)
	@ResponseBody
	public String listImageSize(HttpServletRequest request,
			HttpServletResponse response,String filter,String  ctgId,String appId) {
		try {
			 CustomSecurityUser securityUser = ContextUtil.getLoginUser();
				Map<String,Object> imageSizeMap = new HashMap<>();
			 if(securityUser!=null && appId!=null){
				
					String cmsRstJson =  getProjectLists(request, response);
					JSONObject cmsRst = JSONObject.fromObject(cmsRstJson);
					if(cmsRst.getBoolean("isSuccess")){
						long cmsTotalRows = weiXinRemotingService.fileCount(new String[]{cmsRst.getString("data")}, filter, null);
						imageSizeMap.put("cmsImageSize", cmsTotalRows);
					}else{
						imageSizeMap.put("cmsImageSize", 0);
					}
					filter = filter==null?"":filter;
				     List<Map<String,Object>> accountList =   wxAccountService.getWxAccountList(securityUser.getSite());
				     if(accountList.size() >0){
				    	 ArrayList<String> appIdList = new ArrayList<>();
			    		 for(Map<String,Object> a:accountList){
			    			 appIdList.add(a.get("appId")+"");
			    		 }
						 Query querys = new Query();
					     Criteria cr = new Criteria();
					     querys.addCriteria(cr.orOperator(
					     Criteria.where("name").regex(filter)));
					     querys.addCriteria(Criteria.where("appId").in(appIdList));
					     long totalRows= mongoTemplate.count( querys,Constants.WXIMAGE_T);//总记录数
					     imageSizeMap.put("wxImageSize", totalRows);
				     }
				     
				    long tatolRow =   wxMaterialService.wxMaterialCount(appId, "image", filter);
				    imageSizeMap.put("wxSyncImageSize", tatolRow);
			 }
			 return AjaxUtil.renderSuccessMsg(imageSizeMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("获取信息失败");
	}	
	
	
	
	

}

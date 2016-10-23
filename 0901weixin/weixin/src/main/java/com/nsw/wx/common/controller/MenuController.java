package com.nsw.wx.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.nsw.wx.api.service.imp.MenuServiceImp;
import com.nsw.wx.common.docmodel.MenuEntity;
import com.nsw.wx.common.docmodel.SysLog;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.LogService;
import com.nsw.wx.common.service.MenuService;
import com.nsw.wx.common.service.AccessTokenService;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.views.Message;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月6日 下午2:48:49
* @Description: 微信菜单管理
 */

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	private WxAccountService wxAccountService ;
	@Autowired
	private MenuService menuService ;
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;	
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private AccessTokenService tokenService;
	
	private Logger logger = Logger.getLogger(MenuController.class);
	/**
	* @Description:获取菜单
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "menuInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getMenuInfo(String appId) {
		try {
			if(appId != null){
				Map<String,Object>  entity =   menuService.getMenu(appId);
				if(entity != null){
					return AjaxUtil.renderSuccessMsg(entity);
				}else{
					return AjaxUtil.renderSuccessMsg(new HashMap<>());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("获取菜单信息出错！");
	}
	
	/**
	 * 
	* @Description:添加 修改菜单
	* @param @param entity
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "menuInfo", method = RequestMethod.PUT)
	@ResponseBody
	public String updateMenuInfo(@RequestBody Map<String,Object> menuEntity) {
		try {
			if(menuEntity != null && menuEntity.get("appId") != null ){
				if(menuEntity.get("enable")==null){
					menuEntity.put("enable", false);
				}
				Map<String,Object>  entity =   menuService.getMenu(menuEntity.get("appId")+"");
				if(entity != null){
					Query query = new Query();
					Criteria criteria=Criteria.where("_id").is(new ObjectId(menuEntity.get("_id")+""));
			    	query.addCriteria(criteria);
			    	Update update = new Update();
			    	update.set("enable", menuEntity.get("enable"));
			    	update.set("button", menuEntity.get("button"));
			    	boolean results =   baseMongoTemplate.updateFirst(query, update, Constants.WXMENU_T);
			    	if(menuEntity.get("enable") != entity.get("enable")){
			    		return AjaxUtil.renderSuccessMsg("切换状态成功");
			    	}
				    if(results){
					  	Message message = menuMapToJson(menuEntity);
					  	logger.info(message);
				    	if(message.getIsSuc()){
				    		//数据返回正确   开始同步到微信
				    		//Map<String,Object> account =  wxAccountService.getAccountByAppId( menuEntity.get("appId") +"");
				    		String accessToken =  tokenService.getAccessTokenByRedis(menuEntity.get("appId") +"");
				    		if(accessToken != null){
//				    			String accessToken = account.get("authorizer_access_token")+"";
				    			com.nsw.wx.api.service.MenuService  menuService = new com.nsw.wx.api.service.imp.MenuServiceImp();
				    			JSONObject dataJson = JSONObject.fromObject(message.getData());
				    			JSONObject result =  menuService.createMenu(dataJson, accessToken, menuEntity.get("appId") +"");
				    			if("ok".equals(result.get("errmsg"))){
				    				logger.info(menuEntity.get("appId")+"菜单同步到微信服务器成功!");
				    				SysLog log = new SysLog("menu_add",menuEntity.get("appId")+"","user","保存菜单并同步到微信");
									logService.saveLog(log);
				    				return AjaxUtil.renderSuccessMsg(menuEntity);
				    			}
				    		}
				    	}
				    	return AjaxUtil.renderFailMsg("菜单同步到微信服务器失败。");
				    }
				}else{
					Map<String,Object> entitys  =  menuService.addMenu(menuEntity);
					if(entitys!=null){
						SysLog log = new SysLog("menu_add",menuEntity.get("appId")+"","user","设置公众号菜单");
						logService.saveLog(log);
						
						return AjaxUtil.renderSuccessMsg(entitys);
					}
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("添加失败！");
	}
	
	/**
	 * 
	* @Description:删除菜单
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "menuInfo", method = RequestMethod.DELETE)
	@ResponseBody
	public String delMenuInfo(String appId) {
		try {
			if(appId != null){
				boolean flag =  menuService.delMainMenu(appId);
				if(flag){
					return AjaxUtil.renderSuccessMsg("删除成功!");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AjaxUtil.renderFailMsg("更新菜单信息出错！");
	}
	
	/**
	 * 
	* @Description:  将菜单map结构进行过滤，得到微信合法格式的json数据
	* @param @param mapEntity
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public Message menuMapToJson(Map<String,Object> mapEntity){
			Message message = new Message();
			JSONObject jsonEntity = JSONObject.fromObject(mapEntity);
			//获取菜单数组
			JSONArray jsonArr = jsonEntity.getJSONArray("button");
			JSONObject sync_entity = new JSONObject();
			JSONArray button = new JSONArray();//一级菜单数组
			if(jsonArr != null && jsonArr.size()>0){
				for(int i=0;i<jsonArr.size();i++){
					JSONObject but = jsonArr.getJSONObject(i);
					JSONObject sync_jb = new JSONObject();
					sync_jb.put("name", but.getString("name")==null?"":but.getString("name"));
					
					JSONArray sub_button = but.getJSONArray("sub_button");
					
					if(sub_button != null && sub_button.size() > 0 ){//一级菜单拥有子菜单
						JSONArray  sync_arr = new JSONArray();//二级菜单数组
						for(int j=0;j<sub_button.size();j++){
							JSONObject en = sub_button.getJSONObject(j);//二级菜单
							JSONObject en_ = new JSONObject();
							en_.put("name", en.getString("name"));
							en_.put("type", en.getString("type"));
							if("view".equals(en.getString("type"))){
								en_.put("url",en.get("url")==null ?"" : en.getString("url"))   ;
							}else if("click".equals(en.getString("type"))){
								en_.put("key",en.get("key")==null? "": en.getString("key"));
							}
							sync_arr.add(en_);
						}
						sync_jb.put("sub_button", sync_arr);
						
					}else{//一级菜单无子菜单
						sync_jb.put("type", but.getString("type"));
						if("view".equals(but.getString("type"))){
							sync_jb.put("url",but.get("url")==null?"": but.getString("url"))   ;
						}else if("click".equals(but.getString("type"))){
							sync_jb.put("key",but.get("key")==null?"":but.getString("key"));
						}
					}
					button.add(sync_jb);
				}
				sync_entity.put("button", button);
				message.setIsSuc(true);
				message.setData(sync_entity);
			}else{
				message.setIsSuc(false);
				message.setMsg("同步到微信错误,菜单不能为空!");
			}
			return message;
	}
	
	
		
}

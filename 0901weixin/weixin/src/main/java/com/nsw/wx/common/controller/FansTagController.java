package com.nsw.wx.common.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nsw.wx.api.service.UserTagService;
import com.nsw.wx.api.service.imp.UserTagServiceImp;
import com.nsw.wx.common.docmodel.FansTag;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.AccessTokenService;
import com.nsw.wx.common.service.FancService;
import com.nsw.wx.common.service.FansTagService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.PinYin2Abbreviations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 粉丝及标签管理
 * @author liuzp
 *
 */
@Controller
@RequestMapping("/fansTag")
public class FansTagController {

	private Logger logger = Logger.getLogger(FansTagController.class);
	
	@Autowired
	private FansTagService fansTagService;
	
	@Autowired
	private FancService  fancService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	@Autowired
	private AccessTokenService tokenService;
	
	UserTagService userTagService = new  UserTagServiceImp();

	@Autowired
	private RedisTemplate redisTemplate;

	final String TAGLIST = "tagList";
	
	/**
	 * 
	 * @param appId
	 * @param showDefault  是否显示未分组
	 * @return
	 */
	@RequestMapping(value = "tagList", method = RequestMethod.GET)
	@ResponseBody
	public String fansTagList(String appId,boolean showDefault) {
		if(appId != null){
		  List<FansTag> tagList = 	new ArrayList<>();
	      if(redisTemplate.opsForValue().get(appId+TAGLIST) != null){
			  tagList = fansTagService.getList(appId);
		  }	else{
			  String accessToken = tokenService.getAccessTokenByRedis(appId);
			  //调用接口后缓存数据，避免频繁请求微信接口
			  JSONObject json =  userTagService.getTagList(accessToken, appId);
			  if(!json.containsKey("errcode")){
				  JSONArray arr = json.getJSONArray("tags");
				  tagList =  fansTagService.getList(appId);
				 // if(tagList.size()!=arr.size()){
					 boolean  flag =  fancService.loadFansTags(appId);
					  tagList =  fansTagService.getList(appId);
				  //}
				  if(flag){
					  redisTemplate.opsForValue().set(appId+TAGLIST,true,20, TimeUnit.MINUTES);
				  }
			  }else{
				  return AjaxUtil.renderFailMsg("获取标签信息出错！"+json);
			  }
		  }
		  List<FansTag> tagLists = new ArrayList<FansTag>();
		  FansTag xbz = null;
		  for(FansTag f:tagList){
			  if(f.getTagId() == 2){//星标组
				  xbz = f;
			  }
		  }
		  tagList.remove(xbz);
		  if(showDefault){//显示未分组
			  FansTag f = new FansTag();
			  f.setAppId(appId);
			  f.setTagId(10000);
			  f.setName("未分组");
			  //查询没有标签的用户的数量
			  long count =  fansTagService.getFansCountByTag(appId, "10000");
			  f.setCount((int)count);
			  tagLists.add(f);
		  }
		  if(xbz != null){
			  tagLists.add(xbz);
		  }
		  tagLists.addAll(tagList);
		  return AjaxUtil.renderSuccessMsg(tagLists);
		}
		return AjaxUtil.renderFailMsg("获取标签信息出错！");
	}
	
	/**
	 * 根据标签查找粉丝
	 * @param appId
	 * @param tagId
	 * @param filter
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "getFansByTag", method = RequestMethod.GET)
	@ResponseBody
	public String getFansByTag(String appId,String tagId,String filter,String pageNum,String pageSize) {
		if(appId != null ){
			//如果是第一页,则去检测该标签的数量和数据库数量是否一致，然后再同步粉丝
			ArrayList<Integer> list = new ArrayList<Integer>();
			/*if(StringUtils.isEmpty(filter) && "1".equals(pageNum) && !"10000".equals(tagId) && !StringUtils.isEmpty(tagId) ){

				if(redisTemplate.opsForValue().get(appId+tagId) == null){
					long  wxCount =  getFansCountByTag(appId,tagId);
					list.add(Integer.parseInt(tagId));
					Query query = new Query(Criteria.where("appId").is(appId).and("tagid_list").in(list));
					long localCount = mongoTemplate.count(query,Constants.WXFANS_T);
					if(wxCount != localCount){
						//重新拉取粉丝(这里优化为重新拉取当前标签下的粉丝);
						logger.info("getFansByTag：标签下粉丝数量与实际不符合，重新拉取...");
						fancService.loadFansList(appId,"");
					}
					redisTemplate.opsForValue().set(appId+tagId,true,10, TimeUnit.MINUTES);
				}

			}*/

			Map<String,Object> rst =  fancService.getFansByTag(appId, tagId, filter, pageNum, pageSize);
			return AjaxUtil.renderSuccessMsg(rst);
		}
		return AjaxUtil.renderFailMsg("参数错误!");
		
	}

	/**
	 * 获取微信接口的标签内粉丝数量
	 * @param appId
	 * @param tagId
	 * @return
	 */
	long getFansCountByTag(String appId,String tagId){
		String accessToken = tokenService.getAccessTokenByRedis(appId);
		JSONObject groupJson = userTagService.getFansByTag( accessToken, Integer.parseInt(tagId) ,"",  appId);
		if(groupJson.get("count")!=null && groupJson.get("next_openid")==null){
			return groupJson.getInt("count");
		}
		if( groupJson.get("count")==null){
			return -100;
		}
		int c = 0;
		while( groupJson.get("next_openid")!=null && !("".equals( groupJson.getString("next_openid")))){
			//关注者列表  里面存放的都是openid  我们需要根据openid查询到用户
			c += groupJson.getInt("count");
			groupJson = userTagService.getFansByTag( accessToken, Integer.parseInt(tagId) ,groupJson.get("next_openid")+"",  appId);
		}
		return c;
	}


	
	/**
	 * 添加标签
	 * @param fanstag
	 * @return
	 */
	@RequestMapping(value = "tag", method = RequestMethod.POST)
	@ResponseBody
	public String add(HttpServletRequest request,
					  HttpServletResponse response, @RequestBody FansTag fanstag) {
		
		if(!StringUtils.isEmpty(fanstag.getAppId() ) && ! StringUtils.isEmpty(fanstag.getName())   ){
			//标签查重
			FansTag f = fansTagService.getTagByName(fanstag.getAppId() ,fanstag.getName());
			if(f != null){
				return AjaxUtil.renderFailMsg("该标签名称重复!");
			}

			String accessToken = tokenService.getAccessTokenByRedis(fanstag.getAppId());
			JSONObject dataJson =  userTagService.createTag(accessToken, fanstag.getName(), fanstag.getAppId());
			if(!dataJson.containsKey("errcode")){
				FansTag tag = new FansTag();
				ObjectId oid = new ObjectId();
				tag.set_id(oid);
				tag.setAppId(fanstag.getAppId());
				tag.setTagId(dataJson.getJSONObject("tag").getInt("id"));
				tag.setName(dataJson.getJSONObject("tag").getString("name"));
				tag.setCount(0);
				if(fansTagService.addTag(tag)){
					return AjaxUtil.renderSuccessMsg(tag);
				}
			}
			return AjaxUtil.renderFailMsg("添加失败!"+dataJson);
		}
		return AjaxUtil.renderFailMsg("参数错误!");
		
	}
	
	/**
	 * 修改标签
	 * @param fanstag
	 * @return
	 */
	
	@RequestMapping(value = "tag", method = RequestMethod.PUT)
	@ResponseBody
	public String edit(@RequestBody FansTag fanstag) {
		
		if(!StringUtils.isEmpty(fanstag.getAppId() )&& ! StringUtils.isEmpty(fanstag.getName())   ){
			String accessToken = tokenService.getAccessTokenByRedis(fanstag.getAppId());
			//查重
			FansTag ftag =  fansTagService.getTagById(fanstag.getTagId(),fanstag.getAppId());
			if(! ftag.getName() .equals(fanstag.getName()) ){
				FansTag f  = fansTagService.getTagByName(fanstag.getAppId(), fanstag.getName());
				if( f != null){
					return AjaxUtil.renderFailMsg("该标签名称重复");
				}
			}
			JSONObject data = new JSONObject();
			JSONObject tag = new JSONObject();
			tag.put("id", fanstag.getTagId());
			tag.put("name", fanstag.getName());
			data.put("tag", tag);
			JSONObject dataJson =  userTagService.editTag(accessToken, data, fanstag.getAppId());
			if(dataJson.containsKey("errcode") && dataJson.getInt("errcode") == 0){
				Update update = new Update();
				update.set("name",fanstag.getName());
				if(fansTagService.updateTag(update,fanstag)){
					return AjaxUtil.renderSuccessMsg(fanstag);
				}
			}
			return AjaxUtil.renderFailMsg("修改失败!"+dataJson);
		}
		return AjaxUtil.renderFailMsg("参数错误!");
		
	}
	
	
	/**
	 * 删除标签
	 * @param appId
	 * @param tagId
	 * @return
	 */
	@RequestMapping(value = "tag", method = RequestMethod.DELETE)
	@ResponseBody
	public String del(String appId,String tagId) {
		if(! StringUtils.isEmpty(appId)  && ! StringUtils.isEmpty(tagId)   ){
			String accessToken = tokenService.getAccessTokenByRedis(appId);
			JSONObject dataJson =  userTagService.delTag(accessToken, Integer.parseInt(tagId), appId);
			if(dataJson.containsKey("errcode") && dataJson.getInt("errcode") == 0){
				try {
					//更新数据库的数据   删除标签  更新粉丝
					fansTagService.delTag(appId, tagId);
					List<HashMap> list =  fancService.getFansByTag(appId, tagId);
					for(HashMap hm :list){
                        Query query = new Query(Criteria.where("_id").is(new ObjectId(hm.get("_id")+"")));
                        List<Integer> arr = (List<Integer>) hm.get("tagid_list");
						if(arr.size() > 0){
							List<Integer> temp = new ArrayList<>();
							if(arr.contains(Integer.parseInt(tagId))){
								temp.add(Integer.parseInt(tagId));
							}
							arr.removeAll(temp);
						}
                        Update update = new Update();
                        update.set("tagid_list", arr);
                        baseMongoTemplate.updateFirst(query, update, Constants.WXFANS_T);
                    }
					return AjaxUtil.renderSuccessMsg("删除成功!");
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return AjaxUtil.renderFailMsg("删除失败!");
				}
			}else{
				//删除失败
				List<Integer> tagIdList =  fansTagService.getWxTagIdList(accessToken,appId);
				if(tagIdList.contains( Integer.parseInt(tagId))){
					return AjaxUtil.renderFailMsg("删除失败!"+dataJson);
				}
				fansTagService.delTag(appId, tagId);
				return AjaxUtil.renderSuccessMsg("删除成功!");
			}
		}
		return AjaxUtil.renderFailMsg("参数错误!");
	}
	
	
	
	/**
	 * 修改用户标签
	 * @param appId
	 * @param tagId
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "userTag", method = RequestMethod.PUT)
	@ResponseBody
	public String userTag(String appId,String [] tagId,String openId ) {
		
		if( !StringUtils.isEmpty(appId )   && !StringUtils.isEmpty(openId ) ){
			if(tagId == null){
				tagId = new String[]{};
			}
			if(tagId.length > 3){
				return AjaxUtil.renderFailMsg("标签不能多于3个!");
			}
			String accessToken = tokenService.getAccessTokenByRedis(appId);
			Map<String,Object>  fanc =  fancService.getFans(appId, openId);
			if(fanc != null){
				//获取用户身上的标签
				JSONObject userTagJson =  userTagService.getUserTags(accessToken,openId,appId) ;
				ArrayList<Integer>   tagidList = new ArrayList<>();
				if(userTagJson.containsKey("tagid_list")){
					JSONArray arr =  userTagJson.getJSONArray("tagid_list");
					for(int i=0;i<arr.size();i++){
						tagidList.add((int)arr.get(i));
					}
				}
				ArrayList<Integer> temp = new ArrayList<>();
				ArrayList<Integer> tagIds = new ArrayList<>();
				for(Integer t:tagidList){
					//如果标签存在，直接跳过
					if(isExistInArr(t+"",tagId)){
						tagIds.add(t);
						continue;
					}else{
						JSONObject json =  userTagService.batchDelTagToFans(accessToken, new String[]{openId},t, appId);
						if( json.getInt("errcode") != 0){
							fancService.addFans(appId,openId);
							return AjaxUtil.renderFailMsg("操作失败!"+json);
						}
					}
				}
				boolean isSync = false;
				//加入标签
				for(String id :tagId){
					temp.add(Integer.parseInt(id));
					if(tagIds.contains(Integer.parseInt(id))){
						continue;
					}else{
						JSONObject json = userTagService.batchAddTagToFans(accessToken, new String[]{openId}, Integer.parseInt(id), appId);
						if(json.getInt("errcode") == 45159){
							isSync=true;
							fancService.loadFansTags(appId);
							continue;
						}else if(json.getInt("errcode") != 0){
							return AjaxUtil.renderFailMsg("操作失败!"+json);
						}
					}
				}
				//更新用户标签信息
				Query query = new Query(Criteria.where("appId").is(appId).and("openid").is(openId));
				Update update = new Update();
				if(isSync){
					userTagJson =  userTagService.getUserTags(accessToken,openId,appId) ;
					if(userTagJson.containsKey("tagid_list")){
						JSONArray arr =  userTagJson.getJSONArray("tagid_list");
						update.set("tagid_list", arr);
					}
				}else{
					update.set("tagid_list", temp);
				}
				boolean flag =  baseMongoTemplate.updateOne(query, update, Constants.WXFANS_T);
				if(flag){
					return AjaxUtil.renderSuccessMsg("操作成功!");
				}
			}
			return AjaxUtil.renderFailMsg("操作失败!");
		}
		return AjaxUtil.renderFailMsg("参数错误!");
		
	}

	boolean isExistInArr(String str,String []arr){
		boolean flag = false;
		for(String s:arr){
			if(s.equals(str)){
				flag =true;
				return flag;
			}
		}
		return flag;
	}

	
	/**
	 * 批量为用户打标签
	 * @param appId
	 * @param tagId
	 * @param openId
	 * @return
	 */
	@RequestMapping(value = "batchAddUserTag", method = RequestMethod.POST)
	@ResponseBody
	public String userTag(String appId,String tagId[],String [] openId ) {
		if(!StringUtils.isEmpty(appId) && !StringUtils.isEmpty(tagId) && !StringUtils.isEmpty(openId)){
			try {
				ArrayList<Integer> tagid_list = new ArrayList<>();
				if(openId.length == 0){
					return AjaxUtil.renderFailMsg("请选择至少一个粉丝进行操作!");
				}
				if(tagId.length > 3 ){
					return AjaxUtil.renderFailMsg("选择标签不能超过3个!");
				}
				String accessToken = tokenService.getAccessTokenByRedis(appId);
				//先批量删除标签，再批量添加标签
				for(String str:openId){
					Map<String,Object> entity =  fancService.getFansByOpenId(appId, str);
					if(entity != null){
						ArrayList<Integer>   tagidList = (ArrayList<Integer>) entity.get("tagid_list");
						for(Integer t:tagidList){
							JSONObject json =  userTagService.batchDelTagToFans(accessToken, new String[]{str}, t, appId);
							if( json.getInt("errcode") != 0){
								return AjaxUtil.renderFailMsg("操作失败!"+json);
							}
						}
					}
				}
				for(String id :tagId){
					tagid_list.add(Integer.parseInt(id));
					JSONObject json = userTagService.batchAddTagToFans(accessToken, openId, Integer.parseInt(id) , appId);
					if( json.getInt("errcode") != 0){
						return AjaxUtil.renderFailMsg("操作失败!"+json);
					}
				}

				for(String str:openId){
					Query query = new Query(Criteria.where("appId").is(appId).and("openid").is(str));
					Update update = new Update();
					update.set("tagid_list", tagid_list);
					baseMongoTemplate.updateOne(query, update, Constants.WXFANS_T);
				}
				return AjaxUtil.renderSuccessMsg("操作成功!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return AjaxUtil.renderFailMsg("操作失败!");
			}
		}
		return AjaxUtil.renderFailMsg("参数错误!");
	}
	

}

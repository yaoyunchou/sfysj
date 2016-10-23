package com.nsw.wx.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.nsw.wx.common.util.DateUtils;
import com.nsw.wx.common.util.QiNiuFileUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.nsw.wx.api.model.MediaListPara;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.views.Message;

@Service("wxMaterialService")
public class WxMaterialServiceImp implements WxMaterialService {

	@Autowired
	private WxAccountService accountService;
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	@Autowired
	private FileService fileService;
	
	
	MaterialService materiaService = new MaterialServiceImp();
	
	private Logger logger = Logger.getLogger(WxMaterialServiceImp.class);
	
	@Override
	public Message SyncWxMaterial(String appId,String type) {
		
		Message msg = new Message(false,"同步素材失败");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		QiNiuFileUtils qiniuFileUtil = new QiNiuFileUtils();

	
		if(appId !=null && ("image".equals(type) || "news".equals(type) )){
			Map<String,Object> account =  accountService.getAccountByAppId(appId);
			String nickName = account.get("nick_name")+"";
			String verify_type_info = account.get("verify_type_info")+"";
			try {
				//认证公众号才去同步微信素材
				//if(verify_type_info != null  && !( "-1".equals(verify_type_info))){
					/**获取素材总数 {"voice_count":COUNT,"video_count":COUNT,"image_count":COUNT,"news_count":COUNT} **/
					JSONObject materCount = materiaService.getMatterCount(account.get("authorizer_access_token")+"", appId);
					if(materCount.containsKey("errcode")){
						logger.error(nickName+"同步素材失败，失败原因：获取素材总数失败"+materCount);
						msg.setMsg(materCount+"");
						return msg;
					}
					logger.info(nickName+"开始同步微信素材..."+materCount);
					//删除已有的微信素材
				    long totalPages=0;//总页数
					int totalRows = "news".equals(type) ? materCount.getInt("news_count"):materCount.getInt("image_count");
					if(totalRows==0){//没有找到素材直接返回
						 msg = new Message(true,"同步成功!");
						logger.info(nickName+"素材同步成功!");
						return msg;
					}
					
					totalPages = (totalRows % 20) == 0 ?   totalRows / 20 :   totalRows / 20 + 1;
					MediaListPara para = null;
					for(int i=0;i < totalPages; i++){
						 para = new MediaListPara(type, i*20+"", "20");
						if(i==totalPages-1 ){
							 para = new MediaListPara(type, i*20+"", totalRows-i*20+"");
						}
						JSONObject json =  materiaService.getMatterList(account.get("authorizer_access_token")+"", para, appId);
						if(!json.containsKey("errcode")){
							JSONArray itemArr = json.getJSONArray("item");
							List<Map<String,Object>> newsArr = new ArrayList<>();
							for(int j = 0;j <itemArr.size(); j++){
								Map<String,Object> objMap = new HashMap<String, Object>();
								JSONObject obj = itemArr.getJSONObject(j);
								if("news".equals(type) ){
									//将图文的缩略图下载到本地数据库保存，保存为fileId
									JSONArray articlesArray = obj.getJSONObject("content").getJSONArray("news_item");
									JSONArray removeList = new JSONArray();
									for(int a = 0;a < articlesArray.size(); a ++){
										JSONObject article = articlesArray.getJSONObject(a);
										if(article.get("thumb_url") != null){
											String thumb_url = article.getString("thumb_url");
											if(thumb_url.length()<1){
												continue;
											}
											//获取图片类型
											String imageType = thumb_url.split("wx_fmt=")[1];

											String fileName = UUID.randomUUID().toString().replaceAll("-","")+imageType;
											logger.info("素材同步:thumb_url"+thumb_url);
											//根据url获取文件流
											//InputStream in =  fileService.getStreamByUrl(thumb_url);
											//根据文件流上传文件到图片库
											qiniuFileUtil.uploadWebSiteRes(thumb_url,fileName);
											//String fileId = fileService.insertImageToDb(in, "jpg");
											//in.close();
											if(fileName != null ){
												article.put("fileId", fileName);
												articlesArray.set(a, article);
											}else{
												removeList.add(article);
												break;
											}
										}else{
											removeList.add(article);
										}
									}
									articlesArray.removeAll(removeList);
									//obj.put("articles", articlesArray);
								    //obj.remove("content");
								    objMap.put("articles", articlesArray);
								}
								objMap.put("create_time", df.format(new Date( Long.parseLong(obj.get("update_time")+"") *1000 )) );
								objMap.put("update_time", objMap.get("create_time")+"" );
								objMap.put("appId", appId);
								objMap.put("type", type);
								objMap.put("media_id", obj.getString("media_id"));
								Map<String,Object> queryNews = new HashMap<String, Object>();
								queryNews.put("media_id", obj.getString("media_id"));
								queryNews.put("appId", appId);
								Map<String,Object> entity =  baseMongoTemplate.findOne(Constants.WXNEWS_T, queryNews);
								if(entity != null){
									String _id = entity.get("_id").toString();
									objMap.put("_id", new ObjectId(_id));
								}
								newsArr.add(objMap);
							}
							//删除原有数据，
							Query querys = new Query();
							querys.addCriteria(Criteria.where("appId").is(appId).and("media_id").exists(true));
							baseMongoTemplate.removeByqyery(querys, Constants.WXNEWS_T);
							//添加新数据
							baseMongoTemplate.saveDataList(Constants.WXNEWS_T, newsArr);
							msg.setIsSuc(true);
							logger.info(nickName+"素材同步成功!");
						}
					}
//				}else{
//					msg.setMsg("请认证公众号再使用此功能!");
//				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.setIsSuc(false);
				msg.setMsg("同步失败！");
			}
		}
		
		return msg;
	}

	@Override
	public Long wxMaterialCount(String appId, String type, String filter) {
		// TODO Auto-generated method stub
        try {
			Query querys = new Query();
			querys.addCriteria(Criteria.where("appId").is(appId).and("type").is(type));
			if(filter!=null){
			    Criteria cr = new Criteria();
			    querys.addCriteria(cr.orOperator(Criteria.where( "news".equals(type) ? "articles.title" : "name").regex(filter)));
			}
			long totalRows=  mongoTemplate.count(querys, Constants.WXNEWS_T);
			return totalRows;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0l;
		}
	}
	
	
	

/*	@Override
	public List<HashMap> listMaterial(String appId, String type, String filter,
			int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		Query querys = new Query();
		querys.addCriteria(Criteria.where("appId").is(appId).and("type").is(type));
		if(filter!=null){
		    Criteria cr = new Criteria();
		    querys.addCriteria(cr.orOperator(Criteria.where( "news".equals(type) ? "articles.title" : "name").regex(filter)));
		}
		querys.skip((pageNum - 1) * pageSize).limit(pageSize);
		querys.with(new Sort(Direction.DESC , "update_time"));
		List<HashMap> list =     baseMongoTemplate.findByQuery(querys, Constants.WXSYNCMATERIAL);
		return list==null?new ArrayList<HashMap>():list;
	}*/

}

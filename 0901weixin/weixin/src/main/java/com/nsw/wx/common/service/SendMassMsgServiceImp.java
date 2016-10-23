package com.nsw.wx.common.service;

import com.nsw.wx.api.model.Article;
import com.nsw.wx.api.model.Media;
import com.nsw.wx.api.model.News;
import com.nsw.wx.api.model.mass.*;
import com.nsw.wx.api.service.*;
import com.nsw.wx.api.service.imp.*;
import com.nsw.wx.common.docmodel.MassMessage;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.HttpConnectionUtils;
import com.nsw.wx.common.util.ImageFactory;
import com.nsw.wx.common.util.QiNiuFileUtils;
import com.nsw.wx.common.views.Message;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("sendMassMsgService")
public class SendMassMsgServiceImp implements SendMassMsgService {
	
	
	@Autowired
	BaseMongoTemplate baseMongoTemplate;


	@Value("${fileServerPath}")
	private String fileServerPath;

	MaterialService materService = new MaterialServiceImp();

	@Autowired
	private FancService fancService;

	@Autowired
	private  FileService fileService;

	@Autowired
	private AccessTokenService tokenService;

	com.nsw.wx.api.service.MessageService service  =  new com.nsw.wx.api.service.imp.MessageServiceImp();

	private Logger logger = Logger.getLogger(SendMassMsgServiceImp.class);
	@Override
	public void handEvent(Map<String, String> map,String appId) {
		// TODO Auto-generated method stub
		String ToUserName = map.get("ToUserName")+"";
		String FromUserName = map.get("FromUserName")+"";
		String CreateTime = map.get("CreateTime")+"";
		String MsgID = map.get("MsgID")+"";
		String Status = map.get("Status")+"";
		String TotalCount = map.get("TotalCount")+"";
		String FilterCount = map.get("FilterCount")+"";
		String SentCount = map.get("SentCount")+"";
		String ErrorCount = map.get("ErrorCount")+"";
		
		//根据MsgId去库里查询发送类型
		Map<String,Object> query = new HashMap<String, Object>();
		query.put("appId", appId);
		query.put("msg_id", MsgID);
		
		Map<String,Object> entity =  baseMongoTemplate.findOne(Constants.WXMASSMSG_T, query);
		if(entity!=null){
			JSONObject obj = new JSONObject();
			obj.put("ToUserName", ToUserName);
			obj.put("FromUserName", FromUserName);
			obj.put("CreateTime", CreateTime);
			obj.put("MsgID", MsgID);
			obj.put("Status", Status);
			obj.put("TotalCount", TotalCount);
			obj.put("FilterCount", FilterCount);
			obj.put("SentCount", SentCount);
			obj.put("ErrorCount", ErrorCount);
			obj.put("appId", appId);
			obj.put("massType", entity.get("massType")+"");
			baseMongoTemplate.save(Constants.WXMASSLOG_T, obj);
		}else{
			logger.error("保存群发日志时出错");
		}
		
	}

	@Override
	public Message sendMassMsgByJob(MassMessage msg) {
		Message meesage = new Message();
		meesage.setIsSuc(false);
		//查询群发日志
		String jobId =  msg.getJobId() ;
		Map<String,Object> query = new HashMap<>();
		query.put("MsgID",jobId);
		query.put("appId",msg.getAppId());
		Map<String,Object> entity =  baseMongoTemplate.findOne(Constants.WXMASSLOG_T,query);
		if(entity != null){
			String accessToken = tokenService.getAccessTokenByRedis(msg.getAppId());
			//待执行的json字符串
			String exeStr = entity.get("jobContent")+"";
			JSONObject jsonResult  = service.massMsgByGroup(exeStr,accessToken,msg.getAppId(),null);
			Query querys = new Query(Criteria.where("appId").is(msg.getAppId()).and("MsgID").is(jobId));
			Update update = new Update();
			//执行成功，更改记录状态
			if (jsonResult.containsKey("errcode") && jsonResult.getInt("errcode") == 0){
				update.set("Status","send success");
				meesage.setIsSuc(true);
			}else{
				update.set("Status","send fail");
			}
			baseMongoTemplate.updateFirst(querys,update,Constants.WXMASSLOG_T);
		}
		return   meesage;
	}


	/**
	 *
	 * @Description: 上传图片到临时素材
	 * @param @param msg
	 * @param @return
	 * @return JSONObject
	 * @throws
	 */
	public JSONObject UploadTempImage(String appId,String fileId,String accessToken,String fileType){
		// 图片消息需要先将图片上传，得到MediaId
		InputStream stream = null;
		try {

			QiNiuFileUtils qiniuUtils = new QiNiuFileUtils();
			stream = (fileId+"").startsWith("http")? HttpConnectionUtils.getImageFromNetByUrl(fileId):qiniuUtils.getDownload(fileId);
			if(stream ==null){
				return null;
			}
			//图片不得超过1M   1048576
			long fileSize = (fileId+"").startsWith("http")? HttpConnectionUtils.getImageSizeFromNetByUrl(fileId):qiniuUtils.getDownloadFileSize(fileId);
			if(fileSize==0){
				return null;
			}
			ImageFactory imageFactory = new ImageFactory();
			while(fileSize>1048576){
				stream = imageFactory.imgCompressToScale(stream, 0.5f, "jpg");
				fileSize = stream.available();
			}

			String fileName =  fileId.substring(
					fileId.lastIndexOf("/") + 1,
					fileId.length());
			if (accessToken != null) {
				JSONObject mediaJson = materService
						.addMatter(accessToken, stream, fileName,fileType,appId);
				//stream.close();
				return mediaJson;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	public JSONObject UploadThumbMediaImage(String appId,String fileId,String accessToken,String fileType){
		// 图片消息需要先将图片上传，得到MediaId
		InputStream stream = null;
		try {
			QiNiuFileUtils qiniuUtils = new QiNiuFileUtils();
			stream = (fileId+"").startsWith("http")? HttpConnectionUtils.getImageFromNetByUrl(fileId):qiniuUtils.getDownload(fileId);
			if(stream ==null){
				return null;
			}
			long fileSize = (fileId+"").startsWith("http")? HttpConnectionUtils.getImageSizeFromNetByUrl(fileId):qiniuUtils.getDownloadFileSize(fileId);
			if(fileSize==0){
				return null;
			}
			ImageFactory imageFactory = new ImageFactory();
			//不得超过 65536
			while(fileSize>65536){
				stream = imageFactory.imgCompressToScale(stream, 0.5f, "jpg");
				fileSize = stream.available();
			}

			String fileName =  fileId.substring(
					fileId.lastIndexOf("/") + 1,
					fileId.length());
			if (accessToken != null) {
				JSONObject mediaJson = materService
						.uploadMedia(accessToken, stream, fileType,fileName,appId);
				//stream.close();
				return mediaJson;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @Description:上传图文消息
	 * @param @param msg
	 * @param @return
	 * @return JSONObject
	 * @throws
	 */
	public JSONObject UploadNews(JSONArray arr,String appId, String accessToken){
			//JSONArray arr = news.getJSONArray("articles");
			List<Article> list = new ArrayList<Article>();
			for(int i=0;i<arr.size();i++){
				JSONObject j = arr.getJSONObject(i);
				JSONObject upResult =  UploadThumbMediaImage(appId, j.getString("fileId"),accessToken,"thumb");
				if(upResult!=null && upResult.containsKey("thumb_media_id")){
					Article a = new Article();
					a.setAuthor(j.get("author")==null?"":j.getString("author"));
					a.setContent_source_url(j.get("content_source_url")==null?"":j.getString("content_source_url"));
					a.setDigest(j.get("digest")==null?"":j.getString("digest"));
					a.setThumb_media_id(upResult.getString("thumb_media_id"));
					boolean show_cover_pic = false;	//是否在里面显示图片
					if(j.get("show_cover_pic")==null || "".equals(j.get("show_cover_pic")+"")){
						show_cover_pic = false;
					}else{
						show_cover_pic = (boolean)j.get("show_cover_pic");
					}
					a.setShow_cover_pic(show_cover_pic==true?"1":"0");
					a.setTitle(j.get("title")==null? "": j.getString("title"));
					if( j.get("content")!=null && !"".equals( j.getString("content") )){
						//对图文content进行正则匹配，将网络上的图片上传到微信获取url
						String newContent =  handleContentByRegex(j.getString("content") ,appId, accessToken);
						a.setContent( newContent);
					}else{
						a.setContent( j.get("content")==null? "" : j.getString("content"));
					}
					list.add(a);
				}else{
					return null;
				}
			}
			if(list.size()>0){
				News newss = new News();
				newss.setArticles(list);
				//上传图文消息，获取media_id
				JSONObject json =  service.uploadNews(newss,accessToken,appId);
				return json;
			}
		return null;
	}







	/**
	 *
	 * @Description:上传图文消息
	 * @param @param msg
	 * @param @return
	 * @return JSONObject
	 * @throws
	 */
	public JSONObject UploadNews(MassMessage msg,String accessToken){

		Map<String,Object> map = new HashMap<String, Object>();
		map.put("_id", new ObjectId(msg.getMediaId()));
		Map<String,Object> newsEn = baseMongoTemplate.findOne(Constants.WXNEWS_T, map);
		if(newsEn !=null){
			if(newsEn.get("media_id") != null){
				JSONObject json = new JSONObject();
				json.put("media_id", newsEn.get("media_id")+"");
				json.put("type", "news");
				return json;
			}

			JSONObject news = JSONObject.fromObject(newsEn);
			JSONArray arr = news.getJSONArray("articles");
			List<Article> list = new ArrayList<Article>();
			for(int i=0;i<arr.size();i++){
				JSONObject j = arr.getJSONObject(i);
				JSONObject upResult =  UploadThumbMediaImage(msg.getAppId(), j.getString("fileId"),accessToken,"thumb");
				if(upResult.containsKey("thumb_media_id")){
					Article a = new Article();
					a.setAuthor(j.get("author")==null?"":j.getString("author"));
					a.setContent_source_url(j.get("content_source_url")==null?"":j.getString("content_source_url"));
					a.setDigest(j.get("digest")==null?"":j.getString("digest"));
					a.setThumb_media_id(upResult.getString("thumb_media_id"));
					boolean show_cover_pic = false;	//是否在里面显示图片
					if(j.get("show_cover_pic")==null || "".equals(j.get("show_cover_pic")+"")){
						show_cover_pic = false;
					}else{
						show_cover_pic = (boolean)j.get("show_cover_pic");
					}
					a.setShow_cover_pic(show_cover_pic==true?"1":"0");
					a.setTitle(j.get("title")==null? "": j.getString("title"));
					if( j.get("content")!=null && !"".equals( j.getString("content") )){
						//对图文content进行正则匹配，将网络上的图片上传到微信获取url
						String newContent =  handleContentByRegex(j.getString("content") ,msg.getAppId(), accessToken);
						a.setContent( newContent);

					}else{
						a.setContent( j.get("content")==null? "" : j.getString("content"));
					}
					list.add(a);
				}else{
					return null;
				}
			}
			if(list.size()>0){
				News newss = new News();
				newss.setArticles(list);
				//上传图文消息，获取media_id
				JSONObject json =  service.uploadNews(newss,accessToken,msg.getAppId());
				return json;
			}
		}
		return null;
	}

	/**
	 *
	 * @Description:对微信图文内容进行正则过滤，对外链的image  src进行匹配并上传到微信获取url
	 * @param @param content
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String handleContentByRegex(String content, String appId,String accessToken){
		String imagePatternStr = "<img\\s+[^>]*?src=[\"|\']((\\w+?:?//|\\/|\\w*)[^\"]*?)[\"|\'][^>]*?>";
		Pattern imagePattern = Pattern.compile(imagePatternStr);
		Matcher matcher = imagePattern.matcher(content);
		MaterialService service = new MaterialServiceImp();
		while (matcher.find()) {
			//String imageFragment = matcher.group(0);// img整个标签
			String imageFragmentURL = matcher.group(1);	// img src中图片的url前缀
			if(imageFragmentURL!=null){
				String srcUrl = imageFragmentURL;
				srcUrl = srcUrl.startsWith("http")?srcUrl:fileServerPath+srcUrl;//如果是相对路径加上图片服务器的前缀
				logger.info( " url:" + imageFragmentURL  );
				try {
					InputStream inputStream =  fileService.getStreamByUrl(srcUrl);
					if(inputStream!=null){
						JSONObject object =  service.uploadImageLogo(accessToken, inputStream,"1.jpg",appId);
						logger.info("上传图文消息内的图片获取URL"+object);
						if(!object.containsKey("errcode")){
							//替换url
							content = content.replaceAll(imageFragmentURL, object.getString("url"));
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
		}
		return content;
	}

}

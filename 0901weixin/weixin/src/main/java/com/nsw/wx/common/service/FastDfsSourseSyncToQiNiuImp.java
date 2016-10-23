package com.nsw.wx.common.service;

import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.QiNiuFileUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

/**
 * Created by liuzp on 2016/8/30.
 */
@Service("fastDfsSourseSyncToQiNiu")
public class FastDfsSourseSyncToQiNiuImp implements FastDfsSourseSyncToQiNiu {

	@Autowired
	private  MongoFileOperationService mongoFileOperationService;

	@Value("${fastDfsServerUrl}")
	private String fastDfsServerUrl;  //本地访问路径

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;


	private Logger logger = Logger.getLogger(FastDfsSourseSyncToQiNiuImp.class);

	QiNiuFileUtils qiniuFileUtil = new QiNiuFileUtils();

	@Override
	public void syncSourseToQiNiuYun() {

		/**
		 * 需要处理的表同步
		 * 1.weixin_image
		 * 2.weixin_keyword
		 * 3.weixin_news
		 * 4.weixin_qrCode
		 * 5.weixin_menu
		 */

		try {
			handleImage();
			handleKeyword();
			handleNews();
			handleQrCode();
			handleMenu();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理图片素材
	 * @return
	 */
	boolean handleImage(){
		logger.info("---------开始处理图片素材同步到七牛云---------------");
		Query query = new Query(Criteria.where("fileId").exists(true));
		List<HashMap> listImage = baseMongoTemplate.findByQuery(query,Constants.WXIMAGE_T);
		logger.info("--------预计同步"+listImage.size()+"个记录到七牛云，开始同步......");
		int c = 0;
		InputStream stream = null;
		for(HashMap image:listImage){
			String fileId = image.get("fileId")+"";
			stream =  mongoFileOperationService.getFileInputStream(fileId);
			byte[] data =  InputStreamTOByte(stream);
			if(data != null){
				boolean flag = upload(data,fileId);
				if(flag){
					c++;
				}
			}
		}
		logger.info("处理图片素材结束,总记录:"+listImage.size()+",成功记录:"+c);
		return false;
	}

	/**
	 * 处理关键字
	 * @return
	 */
	boolean handleKeyword(){
		logger.info("---------开始处理关键字回复图片素材同步到七牛云---------------");
		Query query = new Query(Criteria.where("fileId").exists(true));
		List<HashMap> listImage = baseMongoTemplate.findByQuery(query,Constants.WXKEYWORD_T);
		logger.info("--------预计同步"+listImage.size()+"个记录到七牛云，开始同步......");
		InputStream stream = null;
		int c = 0;
		for(HashMap image:listImage){
			String fileId = image.get("fileId")+"";
			stream =  mongoFileOperationService.getFileInputStream(fileId);
			byte[] data =  InputStreamTOByte(stream);
			if(data != null){
				boolean flag = upload(data,fileId);
				if(flag){
					c++;
				}
			}
		}
		logger.info("处理处理关键字回复图片素材结束,总记录:"+listImage.size()+",成功记录:"+c);
		return false;
	}

	/**
	 * 处理图文素材
	 * @return
	 */
	boolean handleNews(){
		logger.info("---------开始处理图片素材同步到七牛云---------------");
		List<HashMap> listImage = baseMongoTemplate.findByQuery(new Query(),Constants.WXNEWS_T);
		InputStream stream = null;
		int c = 0;
		for(HashMap image:listImage){
			JSONObject json = JSONObject.fromObject(image);
			JSONArray arr = json.getJSONArray("articles");
			for(int i=0;i<arr.size();i++){
				JSONObject obj = arr.getJSONObject(i);
				if(obj.containsKey("fileId")){
					String fileId = image.get("fileId")+"";
					stream =  mongoFileOperationService.getFileInputStream(fileId);
					byte[] data =  InputStreamTOByte(stream);
					if(data != null){
						boolean flag = upload(data,fileId);
					}
				}
			}
		}
		logger.info("---------图片素材同步到七牛云结束---------------");
		return false;
	}

	/**
	 * 处理二维码
	 * @return
	 */
	boolean handleQrCode(){
		logger.info("---------开始处理二维码图片素材同步到七牛云---------------");
		Query query = new Query(Criteria.where("replyType").is("pic").and("reply").is(true));
		List<HashMap> list = baseMongoTemplate.findByQuery(query,Constants.WXQRCODE_T);
		logger.info("--------预计同步"+list.size()+"个记录到七牛云，开始同步......");
		InputStream stream = null;
		int c = 0;
		for(HashMap map:list){
			if(map.get("replyContent")!=null){
				String fileId = map.get("replyContent")+"";
				stream =  mongoFileOperationService.getFileInputStream(fileId);
				byte[] data =  InputStreamTOByte(stream);
				if(data != null){
					boolean flag = upload(data,fileId);
					if(flag){
						c++;
					}
				}
			}
		}
		logger.info("处理二维码图片素材结束,总记录:"+list.size()+",成功记录:"+c);
		return false;
	}



	/**
	 * 处理菜单
	 * @return
	 */
	boolean handleMenu(){
		logger.info("---------开始菜单数据同步到七牛云---------------");
		List<HashMap> list = baseMongoTemplate.findByQuery( new Query(),Constants.WXMENU_T);
		InputStream stream = null;
		for(HashMap map:list){
			JSONObject obj = JSONObject.fromObject(map);
			JSONArray  button = obj.getJSONArray("button");
			for(int i=0;i<button.size();i++){
				JSONObject bu = button.getJSONObject(i);
				if(bu.containsKey("materialType") &&"pic".equals(bu.getString("materialType")) ){
					String fileId = bu.get("key")+"";
					stream =  mongoFileOperationService.getFileInputStream(fileId);
					byte[] data =  InputStreamTOByte(stream);
					if(data != null){
						upload(data,fileId);
					}
				}else if(bu.containsKey("sub_button") && bu.getJSONArray("sub_button").size() > 0){
					JSONArray  sub_button = bu.getJSONArray("sub_button");
					for(int j=0;j<sub_button.size();j++){
						JSONObject sub_o =  sub_button.getJSONObject(j);
						if(sub_o.containsKey("materialType") &&"pic".equals(sub_o.getString("materialType")) ){
							String fileId = sub_o.get("key")+"";
							stream =  mongoFileOperationService.getFileInputStream(fileId);
							byte[] data =  InputStreamTOByte(stream);
							if(data != null){
								upload(data,fileId);
							}
						}
					}
				}

			}
		}
		logger.info("---------菜单数据同步到七牛云结束---------------");
		return false;
	}

	/**
	 * 上传文件到七牛云
	 * @param data
	 * @param key
	 */
	boolean upload(byte[] data,String key){
		try {
			qiniuFileUtil.upload(data,key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}



	// 将InputStream转换成byte数组
	public static byte[] InputStreamTOByte(InputStream in)  {

		try {
			if(in == null ){
				return null;
			}

			ByteArrayOutputStream outStream = new ByteArrayOutputStream();

			byte[] data = new byte[BUFFER_SIZE];

			int count = -1;

			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)

				outStream.write(data, 0, count);

			data = null;

			return outStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	// 将byte数组转换成InputStream
	public static InputStream byteTOInputStream(byte[] in) throws Exception {

		ByteArrayInputStream is = new ByteArrayInputStream(in);

		return is;

	}

}

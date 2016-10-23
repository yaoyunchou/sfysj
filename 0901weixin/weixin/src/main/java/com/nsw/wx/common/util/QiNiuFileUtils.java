package com.nsw.wx.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/***
 * 七牛云存储工具类
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年10月21日 下午5:00:15
 * @Description: TODO
 * http://blog.csdn.net/never_cxb/article/details/50534859
 * http://iamyida.iteye.com/blog/2262613
 */
//@Configuration
//@Component
public class QiNiuFileUtils{

	private static String ACCESS_KEY;

	private static String SECRET_KEY;

	private static String BUCKET_NAME;

	private static String QINIU_DOMAIN;

	private Logger logger = Logger.getLogger(QiNiuFileUtils.class);

	static {
		Properties prop =  new  Properties();
		Resource resource = new ClassPathResource("application.properties");
		try {
			prop.load(resource.getInputStream());
			ACCESS_KEY = prop.getProperty("7Niu.access_key");
			SECRET_KEY = prop.getProperty("7Niu.secret_key");
			BUCKET_NAME = prop.getProperty("7Niu.bucket_name");
			QINIU_DOMAIN = prop.getProperty("7Niu.domain");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


   Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
   
   UploadManager uploadManager = new UploadManager();
   //获取空间管理器
   BucketManager bucketManager = new BucketManager(auth);
   
   OkHttpClient okHttpClient = new OkHttpClient();
   Request request = null;
   Call call = null;
   Response res = null;
   /***
    * 获取上传的token
    * @author wukang
    * @Copyright: www.nsw88.com Inc. All rights reserved.
    * @date 2015年10月21日 下午5:00:15
    * @Description: TODO
    */
    public  String getUpToken(){
    
        return auth.uploadToken(BUCKET_NAME);
    }
    
    
   public  String getUpToken(String key){
    	
        return auth.uploadToken(BUCKET_NAME,key);
    }
    
    
    /***
     * 上传文件到七牛服务器【简单上传】
     * @author wukang
     * @Copyright: www.nsw88.com Inc. All rights reserved.
     * @date 2015年10月21日 下午5:00:15
     * @Description: TODO
     */
    public void upload(byte [] data,String fileName){
    	
    	try {
			Response res = uploadManager.put(data, fileName, getUpToken());
			logger.info(res.toString());
		} catch (QiniuException e) {
			 Response r = e.response;
			    logger.info(r.toString());
	          try {
	        	logger.info(r.bodyString());
	          } catch (QiniuException e1) {
	        	  e1.printStackTrace();
	          }
		}
    }
    
    
    
    /***
     * 上传文件到七牛服务器【覆盖上传】
     * @author wukang
     * @Copyright: www.nsw88.com Inc. All rights reserved.
     * @date 2015年10月21日 下午5:00:15
     * @Description: TODO
     */
 public void upload_covered(byte [] data,String fileName){
    	
    	try {
			res = uploadManager.put(data, fileName, getUpToken(fileName));
			if(res.isOK()){
				logger.info(res.bodyString() + "   " + res.address);
/**				File files =new File("D:\\"+fileName);
**				try {
**				FileUtils.writeByteArrayToFile(files, data);
**					} catch (IOException e) {
**				e.printStackTrace();
**				}
**/			}
		} catch (QiniuException e) {
			 Response r = e.response;
			    logger.info(r.toString());
	          try {
	        	logger.info(r.bodyString());
	          } catch (QiniuException e1) {
	        	  e1.printStackTrace();
	          }
		}
    }
 
 

	/**
	 *  上传文件到七牛服务器【网络图片下载下来保存到七牛】
	 *  提取网络资源并上传到七牛空间里,不指定key，则默认使用originalUrl作为文件的key
	 * @param originalUrl  网络上一个资源文件的URL
	 * @param file_key  空间内文件的key[唯一的]
	 */
 public void uploadWebSiteRes(String originalUrl,String file_key){
	  
	    try {
			bucketManager.fetch(originalUrl, BUCKET_NAME, file_key);
		} catch (QiniuException e) {
			e.printStackTrace();
		}
	    logger.info("succeed upload image");
}
 
 
 /** 
  * @Author: Lanxiaowei(7475402366@qq.com) 
  * @Title: deleteFile 
  * @Description: 七牛空间内文件删除 
  * @param @throws QiniuException
  * @return void 
  * @throws 
  */ 
 public  void deleteFile(String key) throws QiniuException {  
     try {
		bucketManager.delete(BUCKET_NAME, key);
	} catch (QiniuException  e) {
		Response r = e.response;
	    logger.info(r.toString());
	}  
 }  
 
 
 /** 
  * @Author: 获取下载链接
  * @Title: deleteFile 
  * @Description: 七牛空间内文件删除 
  * @param @throws QiniuException
  * @return void 
  * @throws 
  */ 
 public void getDownloadUrl(String filename){
	 
	 String downloadRUL = auth.privateDownloadUrl(QINIU_DOMAIN + filename,3600);
     logger.info(downloadRUL);
     InputStream stream = HttpConnectionUtils.getImageFromNetByUrl(QINIU_DOMAIN + filename);
     logger.info(stream);
 }

	public long getDownloadFileSize(String key){
		key = key.replace(" ", "%20");
		String url = QINIU_DOMAIN + key+"?v="+System.nanoTime();
		return HttpConnectionUtils.getImageSizeFromNetByUrl(url);
	}


	/**
	 * 获取下载链接
	 * @param key 空间内文件的key[唯一的]
	 * @return
	 */
 public InputStream getDownload(String key){
	  key = key.replace(" ", "%20");
	  String url = QINIU_DOMAIN + key+"?v="+System.nanoTime();
//	  request = new Request.Builder().url(url).build();
//	  call = okHttpClient.newCall(request);
//	  InputStream stream = null;
//	  try {
//		okhttp3.Response response = call.execute();
//	    stream = response.body().byteStream();
//	    logger.info(stream);
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
	    int i=0;
	    InputStream stream = null;
		while(i < 3){  
			try {
				stream = HttpConnectionUtils.getImageFromNetByUrl(url);
				i++;
				if(stream != null){
					return stream;
				}

			} catch (Exception e) {
				i ++;
				logger.info("下载文件异常！");
			}
		}
	    return stream;
}

	/**
	 *
	 * @param url 空间名称
	 * @param bucket 空间名称
	 * @param key 空间内文件的key[唯一的]
	 * @return
	 */
   //图片转存
	public String getUrl(String url, String bucket, String key){
		String entry = bucket + ":" + key;
		String saveurl = UrlSafeBase64.encodeToString(entry);
		String newString = url + "|saveas/" +saveurl;
		String sign = UrlSafeBase64.encodeToString(auth.create(ACCESS_KEY, SECRET_KEY).sign(newString.getBytes()));
		String finalUrl = newString + "/sign/" + ACCESS_KEY + ":" + sign;
		return finalUrl;
	}	
	
	
	/**
	 * @Description:清除指定空间的缓存
	 * @param @param in
	 * @param @return
	 * @throws
	 */
	 public  String refreshCache() throws Exception{
	 	
			String path ="http://fusion.qiniuapi.com/refresh";
			HttpPost httpost = new HttpPost(path);  
			httpost.setHeader("Authorization"," QBox "+getUpToken());
			httpost.setHeader("Content-Type", "application/json"); 
			List list=new ArrayList();
			list.add("http://obdt9tuqa.bkt.clouddn.com/wukang.jpg");
			Map map  =new HashMap();
			map.put("urls", list);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(map);
			StringEntity  entity = new StringEntity(json.toString());
			httpost.setEntity(entity);
		  //   HttpClient httpClient=new DefaultHttpClient();  
		 //    HttpResponse  httpResponse=httpClient.execute(httpost);
		  //   System.out.println(httpResponse.getStatusLine());
//		if( == HttpStatus.){    
//		         HttpEntity entitys = httpResponse.getEntity();    
//		         String charset = EntityUtils.getContentCharSet(entity);    
//		         System.out.println(entity.getContent());
//		  }    
		return "";
	 }
}

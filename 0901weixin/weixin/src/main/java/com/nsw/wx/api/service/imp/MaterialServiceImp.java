package com.nsw.wx.api.service.imp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.Media;
import com.nsw.wx.api.model.MediaListPara;
import com.nsw.wx.api.model.News;
import com.nsw.wx.api.model.VideoInfo;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.api.util.JSONHelper;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月13日 下午4:36:53
* @Description: TODO
 */
public class  MaterialServiceImp extends BasicServiceImp implements MaterialService {

	@Override
	public JSONObject addTempMatter(String accessToken, String type,
			InputStream file,String fileName,String appId) {
		String uploadUrl=WeiXinApiUrl.AddTempmatter_Post.replace(Constants.ACCESSTOKEN, accessToken).replace("TYPE", type);
		return upLoadFile(uploadUrl,file,fileName );
	}
	
	public String downLoadTempMatter(String accessToken, String media_id,String saveDir,String appId) {
		    // 拼接请求地址
		    String requestUrl = WeiXinApiUrl.GetTempmatter_Get.replace(Constants.ACCESSTOKEN, accessToken).replace("MEDIA_ID", media_id);
		   
		    return downFile(media_id, saveDir, requestUrl);
	}
	
	/**
	 * 
	* @Description: 文件下载 
	* @param @param media_id
	* @param @param saveDir
	* @param @param requestUrl
	* @param @return   
	* @return String  
	* @throws
	 */
	public String downFile(String media_id, String saveDir, String requestUrl) {
		String filePath = null;
		try {
		  URL url = new URL(requestUrl);
		  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		  conn.setDoInput(true);
		  conn.setRequestMethod("GET");
		  File f = new File(saveDir);
		  if(!f.exists()){
			  f.mkdirs();
		  }
		  if (!saveDir.endsWith("/")) {
			  saveDir += "/";
		  }
		  // 根据内容类型获取扩展名
		  String filehead = conn.getHeaderField("Content-Type");
		  String fileExt = filehead.substring(filehead.lastIndexOf("/")+1, filehead.length());
		  // 将mediaId作为文件名
		  filePath = saveDir + media_id +"."+ fileExt;
		  BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		  FileOutputStream fos = new FileOutputStream(new File(filePath));
		  byte[] buf = new byte[8096];
		  int size = 0;
		  while ((size = bis.read(buf)) != -1)
		    fos.write(buf, 0, size);
		  fos.close();
		  bis.close();
		  conn.disconnect();
		  String info = String.format("下载媒体文件成功，filePath=" + filePath);
		  System.out.println(info);
		} catch (Exception e) {
		  filePath = null;
		  e.printStackTrace();
		}
		return filePath;
	}

	@Override
	public JSONObject addMatter(String accessToken, InputStream file,String fileName, String type,String appId) {
		String upLoadFileUrl = WeiXinApiUrl.AddMatter_Post.replace(Constants.ACCESSTOKEN, accessToken).replace("TYPE", type);
			JSONObject result = upLoadFile(upLoadFileUrl, file,fileName);
			if(result.containsKey(Constants.ERRCODE)){
				int csNum=0;//如果系统繁忙 尝试次数
				while(result.containsKey(Constants.ERRCODE) && result.getInt(Constants.ERRCODE)==-1&&csNum<3){//系统繁忙
					result = upLoadFile(upLoadFileUrl, file,fileName);
					csNum++;
				}
			}
			return result;
		/*String title =  videoInfo.getTitle() ==null?null:videoInfo.getTitle();
		String introduction = videoInfo.getIntroduction()==null?null:videoInfo.getIntroduction();
		
//		JSONObject result = upLoadFile(upLoadFileUrl, file,fileName,title, introduction);
		JSONObject result = upLoadFile(upLoadFileUrl, file,fileName);
		if(result.containsKey(Constants.ERRCODE)){
			int csNum=0;//如果系统繁忙 尝试次数
			while(result.containsKey(Constants.ERRCODE)&& result.getInt(Constants.ERRCODE)==-1&&csNum<3){//系统繁忙
//				result = upLoadFile(upLoadFileUrl, file,fileName,title, introduction);
				result = upLoadFile(upLoadFileUrl, file,fileName);
				csNum++;
			}
		}
		return result;*/
	}
	
	public  JSONObject postFile(String url,String type, InputStream file,
			String filename, String title,String introduction) {
			return upLoadFile(url, file,filename);
			}
	
	//上传文件
/*	public JSONObject upLoadFile(String url, InputStream input,String fileName, String title,
			String introduction) {
		String result = null;
		try {
		URL url1 = new URL(url); 
		HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(30000);  
		            conn.setDoOutput(true);  
		            conn.setDoInput(true);  
		            conn.setUseCaches(false);  
		            conn.setRequestMethod("POST"); 
		            conn.setRequestProperty("Connection", "Keep-Alive");
		            conn.setRequestProperty("Cache-Control", "no-cache");
		            String boundary = "-----------------------------"+System.currentTimeMillis();
		            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
		OutputStream output = conn.getOutputStream();
		output.write(("--" + boundary + "\r\n").getBytes());
		output.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", fileName).getBytes());  
		output.write("Content-Type: application/octet-stream\r\n\r\n".getBytes());
		        byte[] data = new byte[1024];
		        int len =0;
		       // InputStream input = file.getInputStream();
		while((len=input.read(data))>-1){
		output.write(data, 0, len);
		}
		output.write(("--" + boundary + "\r\n").getBytes());
		output.write("Content-Disposition: form-data; name=\"description\";\r\n\r\n".getBytes());
		if(title!=null&&introduction!=null){
			output.write(String.format("{\"title\":\"%s\", \"introduction\":\"%s\"}",title,introduction).getBytes());
		}
		output.write(("\r\n--" + boundary + "--\r\n\r\n").getBytes());
		output.flush();
		output.close();
		input.close();
		InputStream resp = conn.getInputStream();
		StringBuffer sb = new StringBuffer();
		while((len= resp.read(data))>-1)
		sb.append(new String(data,0,len,Constants.UTFCODE));
		
		
		resp.close();
		result = sb.toString();
		//System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject object = JSONObject.fromObject(result);
		return object;
	}*/
	
	
	public static  JSONObject upLoadFile(String urlStr, InputStream in,String fileName) {
	    String result = null;
	    try {
	        // 拼装请求地址
//	        String uploadMediaUrl = String.format(
//	                "http://api.weixin.qq.com/cgi-bin/material/add_material?access_token=%1s&type=%2s",
//	                access_token,
//	                type);
	        URL url = new URL(urlStr);
	        //File file = new File("mediaFileUrl");
//	        if (!media.exists() || !media.isFile()) {
//	            System.out.println("上传的文件不存在");
//	        }
	         
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
	        con.setDoInput(true);
	        con.setDoOutput(true);
	        con.setUseCaches(false); // post方式不能使用缓存
	        // 设置请求头信息
	        con.setRequestProperty("Connection", "Keep-Alive");
	        con.setRequestProperty("Charset", "UTF-8");
	        // 设置边界
	        String BOUNDARY = "----------" + System.currentTimeMillis();
	        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
	         
	        // 请求正文信息
	        // 第一部分：
	        StringBuilder sb = new StringBuilder();
	        sb.append("--"); // 必须多两道线
	        sb.append(BOUNDARY);
	        sb.append("\r\n");
	        sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\" \r\n");
	        sb.append("Content-Type:application/octet-stream\r\n\r\n");
	        byte[] head = sb.toString().getBytes("utf-8");
	 
	        // 获得输出流
	        OutputStream out = new DataOutputStream(con.getOutputStream());
	        // 输出表头
	        out.write(head);
	        // 文件正文部分
	        // 把文件已流文件的方式 推入到url中
	        int bytes = 0;
	        byte[] bufferOut = new byte[1024];
	        while ((bytes = in.read(bufferOut)) != -1) {
	            out.write(bufferOut, 0, bytes);
	        }
	        in.close();
	 
	        // 结尾部分
	        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
	        out.write(foot);
	        out.flush();
	        out.close();
	         
	        StringBuffer buffer = new StringBuffer();
	        BufferedReader reader = null;
	        // 定义BufferedReader输入流来读取URL的响应
	        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            buffer.append(line);
	        }
	        if (result == null) {
	            result = buffer.toString();
	        }
	        
	        JSONObject resultOj = JSONObject.fromObject(result);
	        
	        return resultOj;
	         
	    } catch (IOException e) {
	        System.out.println("发送POST请求出现异常！" + e);
	        e.printStackTrace();
	    } finally {
	    	if(in!=null){
	    		try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	    return null;
	     
	}
	
	
	
			 

	@Override
	public JSONObject downLoadMatter(String accessToken, Media media_id,String appId) {
		String jsonStr = JSONHelper.bean2json(media_id);
		String url = WeiXinApiUrl.GetMatter_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return postMethodResult(url, jsonStr,appId);
	
	}

	@Override
	public JSONObject delMatter(String accessToken, Media media_id,String appId) {
		String url = WeiXinApiUrl.DelMatter_Post.replace(Constants.ACCESSTOKEN, accessToken);
		String jsonStr = JSONHelper.bean2json(media_id);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getMatterCount(String accessToken,String appId) {
		String url = WeiXinApiUrl.GetMatterCount_Get.replace(Constants.ACCESSTOKEN, accessToken);
		return getMethodResult(url,appId);
	}

	@Override
	public JSONObject getMatterList(String accessToken,
			MediaListPara mediaListPara,String appId) {
		String url = WeiXinApiUrl.GetMatterList_Post.replace(Constants.ACCESSTOKEN, accessToken);
		String jsonStr = JSONHelper.bean2json(mediaListPara);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject addNews(String accessToken, News news,String appId) {
		// TODO Auto-generated method stub
		String url = WeiXinApiUrl.AddNews_Post.replace(Constants.ACCESSTOKEN, accessToken);
		String jsonStr = JSONHelper.bean2json(news);
		JSONObject obj = JSONObject.fromObject(jsonStr);
		if(obj.containsKey("media_id")){
			obj.remove("media_id");
		}
		if(obj.containsKey("appId")){
			obj.remove("appId");
		}
		return postMethodResult(url, obj.toString(),appId);
	}

	@Override
	public JSONObject uploadImageLogo(String accessToken,
			InputStream file, String fileName,String appId) {
		String uploadUrl=WeiXinApiUrl.UploadLogo_Post.replace(Constants.ACCESSTOKEN, accessToken);
		return upLoadFile(uploadUrl,file,fileName);
	}

	@Override
	public JSONObject uploadMedia(String accessToken, InputStream file,
			String type,String fileName,String appId) {
		String uploadUrl=WeiXinApiUrl.UploadMedia_Post.replace(Constants.ACCESSTOKEN, accessToken).replace("TYPE", type);
		return upLoadFile(uploadUrl,file,fileName );
	}

	
	

}

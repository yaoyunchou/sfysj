package com.nsw.wx.common.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nsw.wx.common.util.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.nsw.wx.common.service.MongoFileOperationService;

/**
 *
 * @author chenan
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年10月9日 上午9:26:31
 * @Description: TODO
 */
@Controller
@RequestMapping(value = "file")
public class FileCtrl {

	private Logger logger = Logger.getLogger(FileCtrl.class);
	

	@Autowired
	private MongoFileOperationService mongoFileOperationService;

	QiNiuFileUtils qiniuFileUtil = new QiNiuFileUtils();
	
	@RequestMapping(value = "/local/uploadBase64", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(@RequestBody Map<String,Object> data) {
		if(data != null && data.get("image") != null){
			try {
				//image/png;base64
				BASE64Decoder decoder = new BASE64Decoder();
				String imageStr = (data.get("image")+"").split("image/png;base64,")[1];
				// Base64解码
				byte[] bytes = decoder.decodeBuffer(imageStr);
				for (int i = 0; i < bytes.length; ++i) {
				    if (bytes[i] < 0) {// 调整异常数据
				        bytes[i] += 256;
				    }
				}
				String userName = null;
				try {
					userName = UUID.randomUUID().toString().replaceAll("-","")+".png";
					qiniuFileUtil.upload(bytes,userName);
					return AjaxUtil.renderSuccessMsg(userName);
				} catch (Exception e) {
					e.printStackTrace();
					return AjaxUtil.renderFailMsg("上传失败!");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return AjaxUtil.renderFailMsg("图片上传失败");
	}

	/**
	 * @Description: 文件保存接口
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
				String appId = StringUtils.isEmpty((String)data.get("appId")) ? "" :(String)data.get("appId");
				DBObject datas = new BasicDBObject();
				datas.put("site", ContextUtil.getSite());
				datas.put("contentType", "image/png");
				datas.put("name", data.get("originName"));
				datas.put("appId", appId);
				datas.put("fileId", data.get("fileName"));
				Map<String, Object> map = mongoFileOperationService.addGridfs(null, (String)data.get("fileName"), datas, FileUtil.getFileType(data.get("fileName")+""));
			}
			return AjaxUtil.renderSuccessMsg("图片上传成功！");
		}
		return AjaxUtil.renderFailMsg("图片上传失败！");
	}



	/**
	 * @author 删除文件
	 * @Copyright: www.nsw88.com Inc. All rights reserved.
	 * @date 2015年10月9日 上午9:26:31
	 * @Description: TODO
	 */
	@RequestMapping(value = "/remove/{fileId}", method = RequestMethod.DELETE)
	@ResponseBody
	public String removeFile(@PathVariable("fileId") String fileId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(fileId));
			mongoFileOperationService.delete(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AjaxUtil.renderSuccessMsg("删除成功！");
	}

	/**
	 * @Description: 文件列表
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public String listFiles(String fileName, Integer pageNum, Integer pageSize) {
		pageSize = 3000;
		pageNum = (pageNum == null ? 1 : pageNum);
		pageSize = (pageSize == null ? 16 : pageSize);
		List<Map<String, Object>> fileList = null;
		fileList = mongoFileOperationService.listFiles(fileName, pageNum,
				pageSize);
		Long totalItem = mongoFileOperationService.fileCount(fileName);
		logger.info("persize: " + fileList == null ? 0 : fileList.size());
		logger.info("totalCount:" + totalItem);
		return AjaxUtil.renderListPageMsg(true, fileList, totalItem);
	}

	/**
	 * @Description: 七牛云存储
	 * @param @return
	 * @return 获取token
	 * @throws
	 * @author wuk
	 */
	@RequestMapping("getUpToken")
	@ResponseBody
	public Map getUpToken(){

		QiNiuFileUtils qiNiuFileUtils = new QiNiuFileUtils();
		String token =  qiNiuFileUtils.getUpToken();
		Map map = new HashMap();
		map.put("uptoken", token);
		return map;
	}

	/**
	 * @Description: 申城图片UUID名称的接口
	 * @param @return
	 * @return 获取token
	 * @throws
	 * @author wuk
	 */
	@RequestMapping("getFileName")
	@ResponseBody
	public String getRandomName(){

		String random = UUID.randomUUID().toString();
		return AjaxUtil.renderSuccessMsg(random.replace("-", ""));
	}



}

package com.nsw.wx.common.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.AjaxUtil;

@Controller
@RequestMapping("/image")
public class ImageUploadController {

	@Autowired
	private WxAccountService wxAccountService;
	
	private Logger logger = Logger.getLogger(ImageUploadController.class);
	
	/**
	 * 
	* @Description:上传图片到微信服务器  返回url
	* @param @param request
	* @param @param response
	* @param @param appId
	* @param @param file
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/uploadlogo", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(HttpServletRequest request,
			HttpServletResponse response, String appId,
			@RequestParam("file") MultipartFile file) {
		try {
			Map<String,Object> account = wxAccountService.getAccountByAppId(appId);
			MaterialService service = new MaterialServiceImp();
			JSONObject object =  service.uploadImageLogo(account.get("authorizer_access_token")+"", file.getInputStream(),file.getName(),appId);
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
}

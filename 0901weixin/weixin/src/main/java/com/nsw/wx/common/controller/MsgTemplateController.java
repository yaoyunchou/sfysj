package com.nsw.wx.common.controller;

import com.nsw.wx.api.service.TempletService;
import com.nsw.wx.api.service.imp.TempletServiceImp;
import com.nsw.wx.common.service.AccessTokenService;
import com.nsw.wx.common.util.AjaxUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzp on 2016/8/22.
 * 消息模板
 */
@Controller
@RequestMapping("msgTemplate")
public class MsgTemplateController {

	@Autowired
	private AccessTokenService tokenService;

	TempletService templetService =new TempletServiceImp();

	/**
	 * 获取公众号的模板列表
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public String getMenuInfo(String appId) {
		if(appId != null){
			String  accessToken = tokenService.getAccessTokenByRedis(appId);
			JSONObject json =  templetService.getAllTemplate(accessToken,appId);
			if(json.containsKey("template_list")){
				return AjaxUtil.renderSuccessMsg(json.getJSONArray("template_list"));
			}else{
				return AjaxUtil.renderFailMsg("获取模板列表失败!"+json);
			}
		}
		return AjaxUtil.renderFailMsg("获取模板列表失败!");
	}


	/**
	 * 获取服务公众号的行业信息
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "industry", method = RequestMethod.GET)
	@ResponseBody
	public String getIndustry(String appId) {
		if(appId != null){
			String  accessToken = tokenService.getAccessTokenByRedis(appId);
			JSONObject json =  templetService.getIndustry(accessToken,appId);
			if(json.containsKey("primary_industry")){
				return AjaxUtil.renderSuccessMsg(json);
			}else{
				return AjaxUtil.renderFailMsg("信息获取失败"+json);
			}
		}
		return AjaxUtil.renderFailMsg("获取模板列表失败!");
	}


}



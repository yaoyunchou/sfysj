package com.nsw.wx.common.controller;

import com.nsw.wx.common.service.FastDfsSourseSyncToQiNiu;
import com.nsw.wx.common.util.AjaxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuzp on 2016/8/30.
 */
@Controller
@RequestMapping("qiniu")
public class SourseSyncToQiNiu {

	@Autowired
	private FastDfsSourseSyncToQiNiu fastDfsSourseSyncToQiNiu;

	@RequestMapping(value ="sync", method = RequestMethod.GET)
	@ResponseBody
	public String getUserInfo() {
		fastDfsSourseSyncToQiNiu.syncSourseToQiNiuYun();
		return AjaxUtil.renderSuccessMsg("同步成功!");
	}
}

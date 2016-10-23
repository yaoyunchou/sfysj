package com.nsw.wx.common.controller;

import com.nsw.wx.api.service.TempletService;
import com.nsw.wx.api.service.imp.TempletServiceImp;
import com.nsw.wx.api.util.JSONHelper;
import com.nsw.wx.common.docmodel.TemplateMsg;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.AccessTokenService;
import com.nsw.wx.common.service.TemplateMsgService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.DateUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import  java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liuzp on 2016/8/24.
 */

@Controller
@RequestMapping("/templateMsg")
public class TemplateMsgController {

	@Autowired
	private AccessTokenService tokenService;

	@Autowired
	private TemplateMsgService templateMsgService;

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	TempletService templetService  = new TempletServiceImp();

	/**
	 * 获取服务号的全部模板消息
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "getAllTemplate", method = RequestMethod.GET)
	@ResponseBody
	public String getAllTemplate(String appId){
		if(!StringUtils.isEmpty(appId)){
			String accessToken = tokenService.getAccessTokenByRedis(appId);
			JSONObject json =  templetService.getAllTemplate(accessToken,appId);
			if(json.containsKey("template_list")){
				return AjaxUtil.renderSuccessMsg(json.getJSONArray("template_list"));
			}else{
				return AjaxUtil.renderFailMsg(json+"");
			}
		}
		return AjaxUtil.renderFailMsg("获取数据失败");
	}

	@RequestMapping(value = "cancleJob", method = RequestMethod.POST)
	@ResponseBody
	public String cancleJob(String id){
		if(!StringUtils.isEmpty(id)){
			boolean flag = templateMsgService.cancleTemplateMsgJob(id);
			if(flag){
				return AjaxUtil.renderSuccessMsg("取消成功!");
			}
		}
		return AjaxUtil.renderFailMsg("取消失败!");
	}



	@RequestMapping(value = "template", method = RequestMethod.POST)
	@ResponseBody
	public String add(HttpServletRequest request,
	                  HttpServletResponse response, @RequestBody TemplateMsg msg){
		if(!StringUtils.isEmpty(msg.getAppId()) && !StringUtils.isEmpty(msg.getContent()) &&
				!StringUtils.isEmpty(msg.getTitle()) && !StringUtils.isEmpty(msg.getPara())){
			String jobId = UUID.randomUUID().toString().replaceAll("-","");
			msg.setJobId(jobId);
			msg.setCreateTime(DateUtils.getCurrentTime());
			msg.setUpdateTime(DateUtils.getCurrentTime());
			msg.setStatus(true);
			msg.setUrl(msg.getUrl()==null?"#":msg.getUrl());
			msg.setSendStatus("ready");
			if(msg.isJob()){
				if(StringUtils.isEmpty(msg.getSendTime())){
					return AjaxUtil.renderFailMsg("请选择发送时间!");
				}
				if(StringUtils.isEmpty(msg.getGroupId())){
					return AjaxUtil.renderFailMsg("请选择对应的标签!");
				}
			}
			Map<String,Object> map =  templateMsgService.add(msg);
			if(map!=null){
				if(msg.isJob()){
					//触发定时任务
					templateMsgService.startTemplateMsgJob(msg.getAppId(),map.get("id")+"",msg.getSendTime(),true);
				}else{
					//立刻执行
					templateMsgService.startTemplateMsgJob(msg.getAppId(),map.get("id")+"",msg.getSendTime(),false);
				}

				return AjaxUtil.renderSuccessMsg(map);
			}
		}
		return AjaxUtil.renderFailMsg("添加失败");
	}


	@RequestMapping(value = "template", method = RequestMethod.PUT)
	@ResponseBody
	public String update(HttpServletRequest request,
	                  HttpServletResponse response, @RequestBody TemplateMsg msg){
		if(!StringUtils.isEmpty(msg.getAppId()) && !StringUtils.isEmpty(msg.getContent()) &&
				!StringUtils.isEmpty(msg.getTitle()) && !StringUtils.isEmpty(msg.getPara())){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(msg.isJob()){
				if(StringUtils.isEmpty(msg.getSendTime())){
					return AjaxUtil.renderFailMsg("请选择发送时间!");
				}
				/*try {
					long sendTime =  df.parse(msg.getSendTime()).getTime();
					long nowTime = new Date().getTime();
					if(sendTime < nowTime){
						return AjaxUtil.renderFailMsg("发送时间不得小于当前时间!");
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}*/
			}
			Update update = new Update();
			update.set("groupId",msg.getGroupId());
			update.set("updateTime",DateUtils.getCurrentTime());
			update.set("status",msg.isStatus());
			update.set("job",msg.isJob());
			update.set("para",msg.getPara());
			update.set("url",msg.getUrl());
			update.set("sendTime",msg.getSendTime());
			boolean flag =  templateMsgService.update(update,msg.getId());
			if(flag){
				return AjaxUtil.renderSuccessMsg("更新成功");
			}
		}
		return AjaxUtil.renderFailMsg("更新失败");
	}

	@RequestMapping(value = "template", method = RequestMethod.DELETE)
	@ResponseBody
	public String delate(String [] ids){
		if(ids!=null){
			if(ids.length==0){
				return  AjaxUtil.renderFailMsg("请选择要删除的数据");
			}
			boolean flag =  templateMsgService.batchDelate(ids);
			if(flag){
				return AjaxUtil.renderSuccessMsg("删除成功");
			}
		}
		return  AjaxUtil.renderFailMsg("删除失败");
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public String list(String appId,String pageNum,String pageSize,String filter){
		int pageNum_ = pageNum==null?1:Integer.parseInt(pageNum);
		int pageSize_  = pageSize==null?20:Integer.parseInt(pageSize);
		if(appId!=null){
			Query query = new Query(Criteria.where("appId").is(appId).and("title").regex(filter==null?"":filter));
			int totalRows = (int)baseMongoTemplate.findCountByQuery(query, Constants.WXTEMPLATEMSG_T);
			long totalPages = (totalRows % pageSize_) == 0 ? totalRows / pageSize_:totalRows / pageSize_ + 1;
			List<HashMap> list =templateMsgService.list(appId,pageNum_,pageSize_,filter);
			JSONObject result = new JSONObject();
			result.put("dataList", list);
			result.put("totalPages", totalPages);
			result.put("totalRows", totalRows);
			return AjaxUtil.renderSuccessMsg(result);
		}
		return  AjaxUtil.renderFailMsg("获取数据失败");
	}


}

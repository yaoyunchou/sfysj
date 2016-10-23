package com.nsw.wx.common.service;

import com.nsw.wx.common.docmodel.TemplateMsg;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by liuzp on 2016/8/24.
 */
public interface TemplateMsgService {

	/**
	 * 更新模板消息
	 * @param update
	 * @param id
	 * @return
	 */
	boolean update(Update update, String  id);

	/**
	 * 添加模板消息
	 * @param msg
	 * @return
	 */
	Map<String,Object> add(TemplateMsg msg);

	/**
	 * 启动消息模板的定时任务
	 * @param templateMsgId
	 * @param sendTime
	 * @return
	 */
	boolean startTemplateMsgJob(String  appId,String templateMsgId,String sendTime,boolean isJob );

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	boolean batchDelate(String ids[]);


	/**
	 * 获取列表
	 * @param appId
	 * @param pageNum
	 * @param pageSize
	 * @param filter
	 * @return
	 */
	List<HashMap> list(String appId, int pageNum, int pageSize, String filter);

	/**
	 * 取消job
	 * @param templateMsgId
	 * @return
	 */
	public boolean cancleTemplateMsgJob(String templateMsgId);

	/**
	 * 执行job
	 * @param templateMsgId
	 * @return
	 */
	public boolean exeTemplateMsgJob(String appId,String templateMsgId);



}

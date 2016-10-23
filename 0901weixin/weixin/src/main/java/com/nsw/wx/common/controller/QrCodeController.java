package com.nsw.wx.common.controller;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nsw.wx.common.service.MongoFileOperationService;
import com.nsw.wx.common.util.HttpConnectionUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.bson.types.ObjectId;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.RedisZSetCommands.Aggregate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.nsw.wx.api.service.QrCodeService;
import com.nsw.wx.api.service.imp.QrCodeServiceImp;
import com.nsw.wx.common.docmodel.QrCode;
import com.nsw.wx.common.docmodel.ScanQrCodeLog;
import com.nsw.wx.common.service.AccessTokenService;
import com.nsw.wx.common.service.ScanQrCodeLogService;
import com.nsw.wx.common.service.WxQrCodeService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.DateUtils;
import com.nsw.wx.common.views.Message;

@Controller
@RequestMapping("/qrCodeMana")
public class QrCodeController {
	
	@Autowired
	private WxQrCodeService wxQrCodeService;
	
	@Autowired
	private AccessTokenService tokenService;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MongoFileOperationService mongoFileOperationService;
	
	
	@Autowired
	private ScanQrCodeLogService scanQrCodeLogService;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "scanOneQrcodeList", method = RequestMethod.GET)
	@ResponseBody
	public String scanOneQrcodeList(String id,String beginDate,String endDate,String pageNum,String pageSize ){
		if(id != null && beginDate!= null && endDate !=null ){
			 int pageNum_ = pageNum==null?1:Integer.parseInt(pageNum);
		     int pageSize_  = pageSize==null?20:Integer.parseInt(pageSize);
		     long totalPages=0;//总页数
		     Query query = new Query();
		     Criteria criteria = Criteria.where("id").is(id).and("scanDate").gte(beginDate).lte(endDate);
		     query.addCriteria(criteria);
			 long totalRows = scanQrCodeLogService.queryScanLogCount(query);
			 if ((totalRows % pageSize_) == 0) {
		            totalPages = totalRows / pageSize_;
		     } else {
		            totalPages = totalRows / pageSize_ + 1;
		     }
			 query.skip((pageNum_ - 1) * pageSize_).limit(pageSize_);
			 query.with(new Sort(Direction.DESC , "scanDate"));
			 List<ScanQrCodeLog> list =  mongoTemplate.find(query, ScanQrCodeLog.class);
			 JSONArray arr = new JSONArray();
			 for(ScanQrCodeLog log : list){
				 JSONObject obj = new JSONObject();
				 obj.put("date", log.getScanDate());
				 obj.put("scanNum", log.getScanNum());
				 obj.put("followNum", log.getFollowNum());
				 obj.put("scanFollowNum", log.getScanNum() - log.getFollowNum());
				 arr.add(obj);
			 }
			 	JSONObject result = new JSONObject();
				result.put("dataList", arr);
				result.put("totalPages", totalPages);
				result.put("totalRows", totalRows);
				return AjaxUtil.renderSuccessMsg(result);
			 
			
		}
		return AjaxUtil.renderFailMsg("参数错误!");
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "scanOneQrcodeListExport", method = RequestMethod.GET)
	public void scanOneQrcodeList(HttpServletRequest request  
            , HttpServletResponse response,String id,String beginDate,String endDate){
		if(id != null && beginDate!= null && endDate !=null ){
		     try {
				Query query = new Query();
				 Criteria criteria = Criteria.where("id").is(id).and("scanDate").gte(beginDate).lte(endDate);
				 query.addCriteria(criteria);
				 query.with(new Sort(Direction.DESC , "scanDate"));
				 List<ScanQrCodeLog> list =  mongoTemplate.find(query, ScanQrCodeLog.class);
				 response.reset();// 清空输出流  
				 response.setHeader("Content-disposition", "attachment; filename=qrCodeScanRecord.xls");// 设定输出文件头  
				 response.setContentType("application/msexcel");// 定义输出类型  
				 
				 // 第一步，创建一个webbook，对应一个Excel文件  
				 HSSFWorkbook wb = new HSSFWorkbook();  
				 // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
				 HSSFSheet sheet = wb.createSheet("二维码扫描记录表");  
				 sheet.setDefaultColumnWidth(20);
				 sheet.setDefaultRowHeightInPoints(20);
				 
				 // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
				 HSSFRow row = sheet.createRow((int) 0);  
				 // 第四步，创建单元格，并设置值表头 设置表头居中  
				 HSSFCellStyle style = wb.createCellStyle();  
				 style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				 style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
				 style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
				 
				 HSSFCell cell = row.createCell(0); 
				 cell.setCellValue("时间");  
				 cell.setCellStyle(style);  
				 cell = row.createCell((short) 1);  
				 cell.setCellValue("扫描次数");  
				 cell.setCellStyle(style);  
				 cell = row.createCell((short) 2);  
				 cell.setCellValue("已关注用户扫描");  
				 cell.setCellStyle(style);  
				 cell = row.createCell((short) 3);  
				 cell.setCellValue("新增关注");  
				 cell.setCellStyle(style);  
				 
				// List<Map<String,Object>>  qrCodeList =  wxQrCodeService.getQrCodeList(appId, 1, 20, null,beginDate,endDate,false);
				 HSSFCellStyle rowStyle = wb.createCellStyle();  
				 rowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
				 // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
				 for (int i = 0; i < list.size(); i++)  
				 {  	
					 ScanQrCodeLog log =  list.get(i);
				     row = sheet.createRow((int) i + 1);  
				     // 第四步，创建单元格，并设置值  
				     row.createCell((short) 0).setCellValue(log.getScanDate());  
				     row.createCell((short) 1).setCellValue(log.getScanNum());  
				     row.createCell((short) 2).setCellValue(log.getScanNum() - log.getFollowNum());
				     row.createCell((short) 3).setCellValue(log.getFollowNum());
				     row.setRowStyle(rowStyle);
				 }  
				  wb.write(response.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	/**
	 * 
	* @Description:单个二维码扫描趋势
	* @param @param id
	* @param @param beginDate
	* @param @param endDate
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "qrCodeScanDev", method = RequestMethod.GET)
	@ResponseBody
	public String qrCodeScanDev(String id,String beginDate,String endDate){
		
		if(id != null && beginDate != null && endDate!=null){
			List<ScanQrCodeLog> list = 	scanQrCodeLogService.getScanLogListByDate( id, beginDate, endDate);
			JSONArray jsonArr = new JSONArray();
			for(ScanQrCodeLog log: list){
				JSONObject obj = new JSONObject();
				obj.put("scanDate", log.getScanDate());
				obj.put("count", log.getScanNum());
				jsonArr.add(obj);
			}
			return AjaxUtil.renderSuccessMsg(jsonArr);
		}
		return AjaxUtil.renderFailMsg("参数错误!");
	}



	private JSONObject groupByDate(String id, String beginDate, String endDate) {
		Criteria criteria = new Criteria().andOperator(
				Criteria.where("id").is(id).and("scanDate").gte(beginDate).lte(endDate));
		GroupBy groupBy = GroupBy.key("scanDate").initialDocument("{count:0}") .reduceFunction("function(key, values){values.count+=1;}");
		GroupByResults<ScanQrCodeLog>	list = 	mongoTemplate.group(criteria, "weixin_scanQrCodeLog", groupBy, ScanQrCodeLog.class);
		DBObject dbobject =  list.getRawResults();
		JSONObject json = JSONObject.fromObject(dbobject);
		return json;
	}
	
	
	@RequestMapping(value = "qrCode", method = RequestMethod.POST)
	@ResponseBody
	public String addQrCode(@RequestBody QrCode qrCode){
		try {
			//数据校验
			Message msg = validateQrCode(qrCode);
			//判断二维码标题是否重复
			if(wxQrCodeService.checkTitleReply(qrCode.getAppId(),qrCode.getTitle())){
				msg.setMsg("二维码名称重复");
				msg.setIsSuc(false);
			}
			if(!msg.getIsSuc()){
				return AjaxUtil.renderFailMsg(msg.getMsg());
			}
			qrCode.setCreateTime(DateUtils.getCurrentTime());
			qrCode.setUpdateTime(DateUtils.getCurrentTime());
			qrCode.setScanFollowNum(0);
			qrCode.setScanNum(0);
			qrCode.setAddFollowNum(0);
			qrCode.set_id(new ObjectId());
			qrCode.setId(qrCode.get_id().toString());
			QrCode q = wxQrCodeService.addQrCode(qrCode);
			if(q != null){
				return AjaxUtil.renderSuccessMsg(q);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxUtil.renderFailMsg("添加失败!");
		}
		return AjaxUtil.renderFailMsg("添加失败!");
		
	}
	
	
	
	
	@RequestMapping(value = "getQrCodeUrl", method = RequestMethod.GET)
	@ResponseBody
	public String getQrCodeUrl(String appId,String type){
		QrCodeService service = new QrCodeServiceImp();
		if(appId != null && type !=null &&("temp".equals(type)||"forever".equals(type))){
			JSONObject data = new JSONObject();
			if("temp".equals(type)){
				data.put("action_name", "QR_SCENE");
				data.put("expire_seconds", 2592000);
			}else{
				data.put("action_name", "QR_LIMIT_SCENE");
			}
			
			Random random = new Random();
			String scene_id =  1 + random.nextInt(99999) +"";
			boolean isReply = true;
			while(isReply){
				//查重，确定scene_id的唯一性
				Query query = new Query();
				Criteria criteria = Criteria.where("appId").is(appId).and("scene_id").is(scene_id);
				query.addCriteria(criteria);
				QrCode qrCode  =  wxQrCodeService.getQrCodeByQuery(query);
				if(qrCode!=null){
					scene_id =  1 + random.nextInt(99999) +"";
				}else{
					isReply = false;
				}
			}
			
			JSONObject action_info = new JSONObject();
			JSONObject scene = new JSONObject();
			scene.put("scene_id", scene_id);
			action_info.put("scene", scene);
			data.put("action_info", action_info);
			String tokenAccess = tokenService.getAccessTokenByRedis(appId);
			JSONObject jsonRst =  service.createGrcode(tokenAccess, data, appId);
			if(!jsonRst.containsKey("errcode")){
				jsonRst.put("scene_id", scene_id);
				return AjaxUtil.renderSuccessMsg(jsonRst);
			}else{
				return AjaxUtil.renderFailMsg(jsonRst.toString());
			}
		}
		return AjaxUtil.renderFailMsg("获取失败");
		
	}
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "qrCode", method = RequestMethod.DELETE)
	@ResponseBody
	public String delQrCode( String[] ids) {
	
		try {
			if(ids == null && ids.length ==0){
				return AjaxUtil.renderFailMsg("请选择要删除的数据!");
			}
			boolean delQst =  wxQrCodeService.deleteQrCode(ids);
			if(delQst){
				return AjaxUtil.renderSuccessMsg("删除成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxUtil.renderFailMsg("删除失败!");
		}
		return AjaxUtil.renderFailMsg("删除失败!");
	}
	
	@RequestMapping(value = "qrCode", method = RequestMethod.GET)
	@ResponseBody
	public String getQrCode(String id){
		if(id != null){
			QrCode qrCode=  wxQrCodeService.getQrCode(id);
			if(qrCode != null ){
				qrCode.setId(qrCode.get_id().toString());
				return AjaxUtil.renderSuccessMsg(qrCode);
			}
		}
		return AjaxUtil.renderFailMsg("获取数据失败!");
	}
	
	
	
	
	@RequestMapping(value = "qrCode", method = RequestMethod.PUT)
	@ResponseBody
	public String updateQrCode(@RequestBody QrCode qrCode){
		
		try {
			Message validateMsg = validateQrCode(qrCode);
			if(!validateMsg.getIsSuc()){
				return AjaxUtil.renderFailMsg(validateMsg.getMsg());
			}
			QrCode q =  wxQrCodeService.getQrCode(qrCode.getId());
			if(q != null){
				if(!q.getTitle().equals(qrCode.getTitle())){
					if(wxQrCodeService.checkTitleReply(qrCode.getAppId(),qrCode.getTitle())){
						return AjaxUtil.renderFailMsg("二维码名称重复!");
					}
				}
			}
			Update update = new Update();
			update.set("style",qrCode.getStyle()==null?"nomal":qrCode.getStyle());
			update.set("scene_id", qrCode.getScene_id());
			update.set("title", qrCode.getTitle());
			update.set("type", qrCode.getType());
			update.set("groupId", qrCode.getGroupId());
			update.set("reply", qrCode.isReply());
			update.set("replyType", qrCode.getReplyType());
			update.set("replyContent", qrCode.getReplyContent());
			update.set("remark", qrCode.getRemark());
			update.set("updateTime", DateUtils.getCurrentTime());
			update.set("qrCodeUrl", qrCode.getQrCodeUrl());
			if(wxQrCodeService.updateQrCode(qrCode.getId(), update)){
				return AjaxUtil.renderSuccessMsg("更新成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxUtil.renderFailMsg("更新失败");
		}
		return AjaxUtil.renderFailMsg("更新失败");
	}
	
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public String getQrCodeList(String appId,String filter,String pageNum,String pageSize,String beginDate, String endDate) {
		
		try {
			int pageNum_ = pageNum==null?1:Integer.parseInt(pageNum);
			  int pageSize_  = pageSize==null?20:Integer.parseInt(pageSize);
			  long totalPages=0;//总页数
			  if(appId != null){
				  int  totalRows =  wxQrCodeService.getQrCodeCount(appId, filter);
				  if ((totalRows % pageSize_) == 0) {
			            totalPages = totalRows / pageSize_;
			       }else{
			            totalPages = totalRows / pageSize_ + 1;
			       }
				  List<Map<String,Object>>  qrCodeList =  wxQrCodeService.getQrCodeList(appId, pageNum_, pageSize_, filter,beginDate,endDate,true);
				  Map<String, Object> result  = new HashMap<String, Object>();
				  result.put("dataList", qrCodeList);
				  result.put("totalPages", totalPages);
				  result.put("totalRows", totalRows);
				  return AjaxUtil.renderSuccessMsg(result);
			  }
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return AjaxUtil.renderFailMsg("获取数据失败");
		}
		 return AjaxUtil.renderFailMsg("获取数据失败");
	}
	
	/**
	 * 统计今日二维码扫描情况
	 * @param appId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "todayStatistics", method = RequestMethod.GET)
	@ResponseBody
	public String getQrCodeStatistics(String appId,String id){
		if(appId != null){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String today = df.format(new Date());
			JSONObject result = new JSONObject();
			int allScanCount = 0 ;
			int followCount = 0 ;
			if(id != null && !"".equals(id)){//查询单个二维码今日数据
				ScanQrCodeLog log = scanQrCodeLogService.getScanLog(appId, today, id);
				QrCode qrCode =   wxQrCodeService.getQrCode(id);
				if(qrCode != null){
					allScanCount = (int) qrCode.getScanNum();
				}
				if(log != null){
					followCount  =   log.getScanNum();
				}
			}else{//查询所有二维码今日数据
				List<ScanQrCodeLog> logList = scanQrCodeLogService.getScanLogList(appId, today);
				List<QrCode>  qrCodeList = 	wxQrCodeService.getQrCodeList(appId);
				if(logList != null ){
					for(ScanQrCodeLog log : logList){
						followCount += log.getScanNum() ;
					}
				}
				for(QrCode q : qrCodeList){
					allScanCount += q.getScanNum();
				}
			}
			result.put("allScanCount", allScanCount);
			result.put("followCount",followCount);
			 return AjaxUtil.renderSuccessMsg(result);
		}
		
		 return AjaxUtil.renderFailMsg("获取统计数据失败");
	}

	@RequestMapping(value = "downQrCode", method = RequestMethod.GET)
	public void downQrCode(HttpServletRequest request
			, HttpServletResponse response,String url){

		//制定浏览器头
		//在下载的时候这里是英文是没有问题的
		//resp.setHeader("content-disposition", "attachment;fileName="+fileName);
		//如果图片名称是中文需要设置转码
		response.setHeader("content-disposition", "attachment;fileName=qrCode.png");
		InputStream reader = null;

		OutputStream out = null;
		byte[] bytes = new byte[1024];
		int len = 0;
		try {
			reader = mongoFileOperationService
					.getBufferedInputStream(url);
			if(reader != null){
				// 读取文件
				// 写入浏览器的输出流
				out = response.getOutputStream();
				while ((len = reader.read(bytes)) > 0) {
					out.write(bytes, 0, len);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}



	}




		/**
         * 输出活动到Excel文件
         * @param request
         * @param response
         */
	@RequestMapping(value = "downQrCodeData", method = RequestMethod.GET)
    public void exportOneActivityToExcel(HttpServletRequest request  
            , HttpServletResponse response,String appId,String beginDate, String endDate){  
        //返回excel的文件流  
		
        try {  
        	
        	if(appId != null && beginDate != null && endDate!= null){
        		 response.reset();// 清空输出流  
                 response.setHeader("Content-disposition", "attachment; filename=qrCode.xls");// 设定输出文件头  
                 response.setContentType("application/msexcel");// 定义输出类型  
                 
                 // 第一步，创建一个webbook，对应一个Excel文件  
                 HSSFWorkbook wb = new HSSFWorkbook();  
                 // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
                 HSSFSheet sheet = wb.createSheet("二维码统计表");  
                 sheet.setDefaultColumnWidth(20);
                 sheet.setDefaultRowHeightInPoints(20);
                 
                 // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
                 HSSFRow row = sheet.createRow((int) 0);  
                 // 第四步，创建单元格，并设置值表头 设置表头居中  
                 HSSFCellStyle style = wb.createCellStyle();  
                 style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                 style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景颜色
                 style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充方式，前色填充
                 
                 HSSFCell cell = row.createCell(0); 
                 cell.setCellValue("二维码名称");  
                 cell.setCellStyle(style);  
                 cell = row.createCell((short) 1);  
                 cell.setCellValue("二维码类型");  
                 cell.setCellStyle(style);  
                 cell = row.createCell((short) 2);  
                 cell.setCellValue("扫描次数");  
                 cell.setCellStyle(style);  
                 cell = row.createCell((short) 3);  
                 cell.setCellValue("已关注用户扫描");  
                 cell.setCellStyle(style);  
                 cell = row.createCell((short) 4);  
                 cell.setCellValue("新增关注");  
                 cell.setCellStyle(style);  
                 
                 List<Map<String,Object>>  qrCodeList =  wxQrCodeService.getQrCodeList(appId, 1, 20, null,beginDate,endDate,false);
                 HSSFCellStyle rowStyle = wb.createCellStyle();  
                 rowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
                 // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
                 for (int i = 0; i < qrCodeList.size(); i++)  
                 {  	
                 	Map<String,Object> map = qrCodeList.get(i);
                     row = sheet.createRow((int) i + 1);  
                     // 第四步，创建单元格，并设置值  
                     row.createCell((short) 0).setCellValue(map.get("title")+"");  
                     row.createCell((short) 1).setCellValue("forever".equals(map.get("type")+"")?"永久二维码":"临时二维码");  
                     row.createCell((short) 2).setCellValue((long)map.get("scanNum"));  
                     row.createCell((short) 3).setCellValue((long)map.get("scanFollowNum"));  
                     row.createCell((short) 4).setCellValue((long)map.get("addFollowNum"));  
                     row.setRowStyle(rowStyle);
                 }  
                  wb.write(response.getOutputStream());
    		}
           
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
	
	
	
	
	
	/**
	 * 数据校验
	 * @param qrCode
	 * @return
	 */
	Message validateQrCode(QrCode qrCode){
		Message msg = new Message();
		msg.setIsSuc(true);
		if(qrCode.getType()==null){
			msg.setMsg("请选择二维码类型");
			msg.setIsSuc(false);
		}
		if(qrCode.getTitle()==null){
			msg.setMsg("请填写标题");
			msg.setIsSuc(false);
		}
		if(qrCode.getAppId() == null){
			msg.setMsg("请填写appId");
			msg.setIsSuc(false);
		}
		if(qrCode.isReply()){
			if(qrCode.getReplyType()==null){
				msg.setMsg("请填写回复类型");
				msg.setIsSuc(false);
			}else{
				if(qrCode.getReplyContent()==null){
					msg.setMsg("请填写回复信息");
					msg.setIsSuc(false);
				}
			}
		}
		if(qrCode.getQrCodeUrl()==null||qrCode.getScene_id() == null||qrCode.getUrl() == null){
			msg.setMsg("请先生成二维码");
			msg.setIsSuc(false);
		}
		return msg;
	}
	
	

}

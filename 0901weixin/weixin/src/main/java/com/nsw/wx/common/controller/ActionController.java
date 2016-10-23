package com.nsw.wx.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.quartz.JobBuilder.newJob;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.quartz.TriggerBuilder.newTrigger;
import net.sf.json.JSONObject;
import static org.quartz.DateBuilder.nextGivenSecondDate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import com.nsw.wx.api.service.KfService;
import com.nsw.wx.api.service.OpenWxService;
import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.KfServiceImp;
import com.nsw.wx.api.service.imp.OpenWxServiceImp;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.api.util.AesException;
import com.nsw.wx.api.util.WXBizMsgCrypt;
import com.nsw.wx.api.util.WxUtil;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.job.AttentionJob;
import com.nsw.wx.common.job.DeletetAccountDataJob;
import com.nsw.wx.common.job.LoadFansInfo;
import com.nsw.wx.common.job.RefreshBindAcountJob;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.service.FancService;
import com.nsw.wx.common.service.KeyWordService;
import com.nsw.wx.common.service.MenuService;
import com.nsw.wx.common.service.OpenCommonService;
import com.nsw.wx.common.service.RequestMsgService;
import com.nsw.wx.common.service.ScanService;
import com.nsw.wx.common.service.SendMassMsgService;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.SignUtils;

/**
 * 
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月14日 下午3:07:32
* @Description: TODO
 */
@Controller
@RequestMapping("/msg")
public class ActionController {
	
private Logger logger = Logger.getLogger(ActionController.class);
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	@Autowired
	private OpenCommonService openCommonService;
	@Autowired
	private WxAccountService wxAccountService;
	@Autowired
	private KeyWordService keyWordService;
	@Autowired
	private SendMassMsgService sendMassMsgService;
	
	@Autowired
	private FancService fancService ;
	
	@Autowired
	private ScanService scanService;
	
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	private RequestMsgService requestMsgService;
	
	//@Value("${wxurl}")
	//private String wxurl;  //本地访问路径
	
	  
	  /**
	   * 
	  * @Description: 这里接收手工绑定的公众号信息推送及事件
	  * @param @param request
	  * @param @param response
	  * @param @param APPID
	  * @param @throws Exception   
	  * @return void  
	  * @throws
	   */
	  @RequestMapping(value = "/{APPID}", method = RequestMethod.GET)
	    public void acceptAuthorize(HttpServletRequest request, HttpServletResponse response,@PathVariable("APPID") String appid) throws Exception {
			String signature=request.getParameter("signature");
			String timestamp=request.getParameter("timestamp");//时间戳 
			String nonce=request.getParameter("nonce");//随机数 
			String echostr=request.getParameter("echostr");//随机字符串 
			if(signature == null ){
				return;
			}
            //根据公众账号原始id查询公众账号的信息
            Map<String,Object> account =  wxAccountService.getAccountByAppId(appid);
            if(account == null ){
            	return ;
            }
			PrintWriter out=response.getWriter();
			//通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败 
			if(SignUtils.checkSignature(account.get("token")+"",signature,timestamp,nonce)){
				out.print(echostr);
				if(account != null &&  Integer.parseInt(account.get("verify_type_info")+"") != -1){
					//拉取粉丝信息
					fancService.syncJobDo(appid);
				}
			}
			out.close();
	  }
	  
	  /**
	   * 
	  * @Description: 这里接收手工绑定的公众号信息推送及事件
	  * @param @param request
	  * @param @param response
	  * @param @param APPID
	  * @param @throws Exception   
	  * @return void  
	  * @throws
	   */
	  @RequestMapping(value = "/{APPID}", method = RequestMethod.POST)
	    public void acceptAuthorize_Post(HttpServletRequest request, HttpServletResponse response,@PathVariable("APPID") String appId) throws Exception {
            // xml请求解析  
            Map<String, String> requestMap = new HashMap<String, String>();
            requestMap = WxUtil.parseXml(request);  
            //消息和事件处理
            handleMsg(request, response, requestMap,appId,2);
	        
	        
	  }
	
	
	
	
	  /**
	   * 
	  * @Description: 一键授权的消息交互和事件交互 (以及手动绑定的公众号)
	  * @param @param request
	  * @param @param response
	  * @param @param APPID
	  * @param @throws Exception   
	  * @return void  
	  * @throws
	   */
	  @RequestMapping(value = "/{APPID}/callback")
	    public void acceptAuthorizeEvent(HttpServletRequest request, HttpServletResponse response,@PathVariable("APPID") String appId) throws Exception {
		  	
		    request.setCharacterEncoding("UTF-8");  
	        response.setCharacterEncoding("UTF-8");  
		    String msgSignature = request.getParameter("msg_signature");
		    String timestamp = request.getParameter("timestamp");
	        String signature = request.getParameter("signature");
	        String nonce = request.getParameter("nonce");
	        if(msgSignature == null ){
	        	return ;
	        }
	        Map<String, String> map = new HashMap<String, String>();
	        //// 微信推送给第三方开放平台的消息一定是加过密的，无消息加密无法解密消息       不是我们自己平台绑定的  不处理请求
	        if (StringUtils.isNotBlank(msgSignature)){
	        	Account object =  openCommonService.getAccountBasic();
		        String xml = WxUtil.parseString(request);
		        WXBizMsgCrypt pc = new WXBizMsgCrypt(object.getToken(),object.getEncodingAesKey(), object.getAppId());
		    	String result2 = pc.decryptMsg(msgSignature, timestamp, nonce, xml);
		    	Document doc = DocumentHelper.parseText(result2);
			    Element rootElt = doc.getRootElement();
			    // 得到根元素的所有子节点
	 			List<Element> elementList = rootElt.elements();
	 			// 遍历所有子节点
	 			for (Element e : elementList)
	 				map.put(e.getName(), e.getText());
		    	
	            // 公众帐号  
	            String toUserName = rootElt.elementText("ToUserName");  
	            //全网发布
	            if(toUserName.equals("gh_3c884a361561")){
	            	checkWeixinAllNetworkCheck(request,response,rootElt);
	            }
	        }else{//手动绑定的是不加密的
	        	map = WxUtil.parseXml(request);  
	        }
            handleMsg(request, response, map,appId,3);


	    }
	  
	  
	  
	  public void  handleMsg(HttpServletRequest request, HttpServletResponse response ,Map<String, String> map,String appId,int type){
		  
		  
		  // 发送方帐号（open_id）  
          String fromUserName = map.get("FromUserName");  
          // 公众帐号  
          String toUserName = map.get("ToUserName");  
          // 消息类型  
          String msgType = map.get("MsgType");  
          
          logger.info("接收信息日志:["+toUserName+"]收到来自["+fromUserName+"]的("+msgType+")类型消息。内容"+map.toString());
		  
	        // 文本消息  
          if (WxUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
          	  //判断接收到的消息是否重复,重复不处理
			 boolean isRepeat =  requestMsgService.isRepeat(appId, map);
			  if(!isRepeat){
				  //消息处理
				  requestMsgService.saveRequestMsg(appId, map);
				  //处理用户文本消息
				  keyWordService.handTxtEvent(response, map, appId, type);
			  }
          //图片消息
          }  else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_IMAGE)) {
			  boolean isRepeat =  requestMsgService.isRepeat(appId, map);
			  if(!isRepeat){
				  //消息处理
				  requestMsgService.saveRequestMsg(appId, map);
			  }
          //语音
          } else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_VOICE)) {
          	
          	
          	
          //视频	
          }else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_VIDEO)) {
          	
          	
          	
          //地理位置消息	
          }else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_LOCATION)) {
          	
          	
          	
          	
          	
          // 链接消息
          }else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_LINK)) {
        	  //消息处理
        	  requestMsgService.saveRequestMsg(appId, map);
          	
          	
          	
          // 事件推送
          }else if (msgType.equals(WxUtil.REQ_MESSAGE_TYPE_EVENT)) {
			  boolean isRepeat =  requestMsgService.isRepeat(appId, map);
			  if(!isRepeat){
					//消息处理
				  requestMsgService.saveRequestMsg(appId, map);
				  // 事件类型
				  String eventType = map.get("Event");
				  // 关注
				  if (eventType.equals(WxUtil.EVENT_TYPE_SUBSCRIBE)) {
					  logger.info("关注事件日志:appId为["+appId+"]的粉丝["+fromUserName+"]关注你了");
					  //1.从粉丝表里加入粉丝信息
//					scanService.scanEvent(response ,map , appId, type);
					  //2.关注时回复信息
					  keyWordService.attentionEvent(response, map, appId, type);
				  }
				  // 取消关注
				  else if (eventType.equals(WxUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					  logger.info("取消关注事件日志:appId为["+appId+"]的粉丝["+fromUserName+"]取消关注你了");
					  // TODO 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
					  //从粉丝表里删除粉丝信息
					  fancService.delFans(appId, fromUserName);
				  }
				  // 自定义菜单
				  else if (eventType.equals(WxUtil.EVENT_TYPE_CLICK)) {
					  //根据菜单类型触发相应条件
					  menuService.handMenuEvent(response, map, appId,type);

					  //事件推送群发结果
				  }else if(eventType.equals("MASSSENDJOBFINISH")){
					  //sendMassMsgService.handEvent(map, appId);
				  }else if("SCAN".equals(eventType)){//用户已关注时的事件推送
					  scanService.scanEvent(response, map, appId, type);
				  }
			  }

          }
	        
	  }
	  
	  
	  
	  
	  /**
	   * 
	  * @Description:全网发布检测接口
	  * @param @param request
	  * @param @param response
	  * @param @param rootElt
	  * @param @throws DocumentException
	  * @param @throws IOException   
	  * @return void  
	  * @throws
	   */
	    
	    public void checkWeixinAllNetworkCheck(HttpServletRequest request, HttpServletResponse response,Element rootElt) throws DocumentException, IOException{
	 
	        String msgType = rootElt.elementText("MsgType");
	        String toUserName = rootElt.elementText("ToUserName");
	        String fromUserName = rootElt.elementText("FromUserName");
	 
//	        LogUtil.info("---全网发布接入检测--step.1-----------msgType="+msgType+"-----------------toUserName="+toUserName+"-----------------fromUserName="+fromUserName);
//	        LogUtil.info("---全网发布接入检测--step.2-----------xml="+xml);
	        if("event".equals(msgType)){
//	        	 LogUtil.info("---全网发布接入检测--step.3-----------事件消息--------");
	        	 String event = rootElt.elementText("Event");
		         replyEventMessage(request,response,event,toUserName,fromUserName);
	        }else if("text".equals(msgType)){
//	        	 LogUtil.info("---全网发布接入检测--step.3-----------文本消息--------");
	        	 String content = rootElt.elementText("Content");
		         processTextMessage(request,response,content,toUserName,fromUserName);
	        }
	    }
	    
	    
	    public void replyEventMessage(HttpServletRequest request, HttpServletResponse response, String event, String toUserName, String fromUserName) throws DocumentException, IOException {
	        String content = event + "from_callback";
	        logger.info("---全网发布接入检测------step.4-------事件回复消息  content="+content + "   toUserName="+toUserName+"   fromUserName="+fromUserName);
	        replyTextMessage(request,response,content,toUserName,fromUserName);
	    }
	 
	    public void processTextMessage(HttpServletRequest request, HttpServletResponse response,String content,String toUserName, String fromUserName) throws IOException, DocumentException{
	        if("TESTCOMPONENT_MSG_TYPE_TEXT".equals(content)){
	            String returnContent = content+"_callback";
	            replyTextMessage(request,response,returnContent,toUserName,fromUserName);
	        }else if(content.startsWith("QUERY_AUTH_CODE") ){
	            output(response, "");
	            //接下来客服API再回复一次消息
	            replyApiTextMessage(request,response,content.split(":")[1],fromUserName);
	        }
	    }
	 
	    public void replyApiTextMessage(HttpServletRequest request, HttpServletResponse response, String auth_code, String fromUserName) throws DocumentException, IOException {
	       String authorization_code = auth_code;
	        // 得到微信授权成功的消息后，应该立刻进行处理！！相关信息只会在首次授权的时候推送过来
	        System.out.println("------step.1----使用客服消息接口回复粉丝----逻辑开始-------------------------");
	        Map<String,Object> account = 	openCommonService.getComponentAccessToken("1");
	        
	        try {
	   
	        	String component_access_token = account.get("component_access_token")+"";
	        	
	        	System.out.println("------step.2----使用客服消息接口回复粉丝------- component_access_token = "+component_access_token + "---------authorization_code = "+authorization_code);
	        	
	          	//1.使用授权码换取公众号的接口调用凭据和授权信息
	        	OpenWxService service = new OpenWxServiceImp();
	        	JSONObject query = new JSONObject();
	        	query.put("component_appid", account.get("appId"));
	        	query.put("authorization_code", authorization_code);
	        	JSONObject entity =  service.getAuthorizerAccessTokenAndRefreshToken(account.get("component_access_token")+"", query,account.get("appId")+"");
	        	System.out.println("------step.3----使用客服消息接口回复粉丝-------------- 获取authorizationInfoJson = "+entity);
	        	net.sf.json.JSONObject infoJson = entity.getJSONObject("authorization_info");
	        	String authorizer_access_token = infoJson.getString("authorizer_access_token");
	        	JSONObject obj = new JSONObject();
	        	JSONObject msgMap = new JSONObject();
	        	String msg = auth_code + "_from_api";
	        	msgMap.put("content", msg);
	        	obj.put("touser", fromUserName);
	        	obj.put("msgtype", "text");
	        	obj.put("text", msgMap);
	        	KfService kfservice = new KfServiceImp();
	        	JSONObject result =  kfservice.sendMsg(authorizer_access_token, obj,null);
	        	logger.info("step.3 用客服发送消息返回结果如下:"+result);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	    }   
	    
	    /**
	     * 回复微信服务器"文本消息"
	     * @param request
	     * @param response
	     * @param content
	     * @param toUserName
	     * @param fromUserName
	     * @throws DocumentException
	     * @throws IOException
	     */
	    public void replyTextMessage(HttpServletRequest request, HttpServletResponse response, String content, String toUserName, String fromUserName) throws DocumentException, IOException {
	     
	      Account account = 	openCommonService.getAccountBasic();
	    	
	    	Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
	        StringBuffer sb = new StringBuffer();
	        sb.append("<xml>");
			sb.append("<ToUserName><![CDATA["+fromUserName+"]]></ToUserName>");
			sb.append("<FromUserName><![CDATA["+toUserName+"]]></FromUserName>");
			sb.append("<CreateTime>"+createTime+"</CreateTime>");
			sb.append("<MsgType><![CDATA[text]]></MsgType>");
			sb.append("<Content><![CDATA["+content+"]]></Content>");
			sb.append("</xml>");
			String replyMsg = sb.toString();
	        
	        String returnvaleue = "";
	        try {
	            WXBizMsgCrypt pc = new WXBizMsgCrypt(account.getToken(), account.getEncodingAesKey(), account.getAppId());
	            returnvaleue = pc.encryptMsg(replyMsg, createTime.toString(), "easemob");
//	            System.out.println("------------------加密后的返回内容 returnvaleue： "+returnvaleue);
	        } catch (AesException e) {
	            e.printStackTrace();
	        }
	        output(response, returnvaleue);
	    }



	    /**
	     * 工具类：回复微信服务器"文本消息"
	     * @param response
	     * @param returnvaleue
	     */
	    public void output(HttpServletResponse response,String returnvaleue){
			try {
				PrintWriter pw = response.getWriter();
				pw.write(returnvaleue);
//				System.out.println("****************returnvaleue***************="+returnvaleue);
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

}

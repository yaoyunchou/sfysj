package test.controller;

import com.nsw.wx.Application;
import com.nsw.wx.common.config.MongoConfig;
import com.nsw.wx.common.docmodel.TemplateMsg;
import com.nsw.wx.common.service.AccessTokenService;
import net.sf.json.JSONObject;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.nsw.wx.api.service.TempletService;
import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.TempletServiceImp;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeTest;

import javax.servlet.annotation.MultipartConfig;
import java.util.Iterator;

/**
 * Created by liuzp on 2016/8/22.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class TemplateCtrlTest {

	@Autowired
	private AccessTokenService tokenService;
	String appId = "wx6628d33ac319e694";
	String accessToken = "";

	TempletService templetService =new TempletServiceImp();

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	@Test
	public void init(){
		System.out.print("init...");
		 accessToken = tokenService.getAccessTokenByRedis(appId);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	/**
	 * 添加
	 *
	 * 前端传值参考：
	 *
	 * {"appId":"wx6628d33ac319e694","status":false,"sendTime":"2016-08-25 14:22:00","job":true,"sendStatus":"","url":"http://baidu.com","para":{"name":"111","time":"2016-08-25 14:22:00","number":111,"remark":"备注"},"content":"{{name.DATA}}：\\n\\n{{productTpye.DATA}}\\n交易时间：{{time.DATA}}\\n交易金额：{{number.DATA}}\\n{{remark.DATA}}","id":"","templateId":"QGwfXUVfLRqpUAAYL_LXw9nXcQpa-qzTlpObH5eDBTw","groupId":"123","title":"交易失败提醒"}
	 *
	 *
	 */
	@Test
	public void add() throws Exception{
		TemplateMsg msg = new TemplateMsg();
		msg.setAppId(appId);
		msg.setUrl("http://baidu.com");
		msg.setTemplateId("QGwfXUVfLRqpUAAYL_LXw9nXcQpa-qzTlpObH5eDBTw");
		msg.setTitle("交易失败提醒");
		msg.setContent("{{name.DATA}}：\\n\\n{{productTpye.DATA}}\\n交易时间：{{time.DATA}}\\n交易金额：" +
				"{{number.DATA}}\\n{{remark.DATA}}");
		JSONObject para = new JSONObject();
		para.put("name","111");
		para.put("time","2016-08-25 14:22:00");
		para.put("number",111);
		para.put("remark","备注");
		msg.setPara(para);
		msg.setGroupId("123");
		msg.setJob(true);
		msg.setSendTime("2016-08-25 14:22:00");
		JSONObject json = JSONObject.fromObject(msg);
		System.out.print("json->"+json.toString());

		mockMvc.perform(
				MockMvcRequestBuilders.post("/templateMsg/template")
						.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	/**
	 *获取列表
	 * @throws Exception
	 */
	@Test
	public void list() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.get("/templateMsg/list").param("appId", appId).param("pageNum","1").param("pageSize","10"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}

	/**
	 * 获取所有模板
	 * @throws Exception
	 */

	@Test
	public void listAllTemplate() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.get("/templateMsg/getAllTemplate").param("appId", appId))
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}


	/**
	 * 获取所属行业
	 */
	@org.junit.Test
	public void getIndustry(){
		JSONObject json =  templetService.getIndustry(accessToken,appId);
		System.out.print(json);

	}

	/**
	 * 获取所有的消息模板
	 */
	@org.junit.Test
	public void getTemplateList(){
		JSONObject json =  templetService.getAllTemplate(accessToken,appId);
		System.out.print(json);

	}

	/**
	 * 发送模板消息
	 */
	@org.junit.Test
	public void sendMsgTemplate(){
		/**
		 *  {
		 "touser":"OPENID",
		 "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
		 "url":"http://weixin.qq.com/download",
		 "data":{
		 "first": {
		 "value":"恭喜你购买成功！",
		 "color":"#173177"
		 },
		 "keynote1":{
		 "value":"巧克力",
		 "color":"#173177"
		 },
		 "keynote2": {
		 "value":"39.8元",
		 "color":"#173177"
		 },
		 "keynote3": {
		 "value":"2014年9月22日",
		 "color":"#173177"
		 },
		 "remark":{
		 "value":"欢迎再次购买！",
		 "color":"#173177"
		 }
		 }
		 }
		 */

		JSONObject jsonData = new JSONObject();
		jsonData.put("touser","odqwHuAoLi6YUVi07PVwPxHFii3w");
		jsonData.put("template_id","X5s0Zyg_cwMELd6-7jSCzmgTZqHxYQLEAf5UZ_2YIbQ");
		jsonData.put("url","http://baidu.com");
		jsonData.put("topcolor","#FF0000");




		JSONObject data = new JSONObject();
		JSONObject first = new JSONObject();
		first.put("value","您好,刘志鹏同学,你今天的课程安排来了,请提前登陆客户端准备上课!");
		first.put("color","#173177");
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value","2015-10-10  10:00:00 - 11:00:00");
		keyword1.put("color","#173177");
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value","励志演讲课");
		keyword2.put("color","#173177");
		JSONObject keyword3 = new JSONObject();
		keyword3.put("value","测评课");
		keyword3.put("color","#173177");
		JSONObject keyword4 = new JSONObject();
		keyword4.put("value","姚运筹");
		keyword4.put("color","#173177");
		JSONObject keyword5 = new JSONObject();
		keyword5.put("value","刘志鹏");
		keyword5.put("color","#173177");
		JSONObject remark = new JSONObject();
		remark.put("value","若课程有任何疑问或者改动,请及时联系陈冠希老师");
		remark.put("color","#173177");

		data.put("first",first);
		data.put("keyword1",keyword1);
		data.put("keyword2",keyword2);
		data.put("keyword3",keyword3);
		data.put("keyword4",keyword4);
		data.put("keyword5",keyword5);
		data.put("remark",remark);
		jsonData.put("data",data);
		//JSONObject json =  templetService.sendTempletMsg(accessToken,jsonData.toString(),appId);
		//System.out.print(json);


		Iterator iterator = data.keys();
		while(iterator.hasNext()){
			String key = (String) iterator.next();
			JSONObject value = data.getJSONObject(key);
			System.out.println("key:"+key);
			System.out.println("value:"+value);


		}

	}


}

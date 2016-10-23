package test.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nsw.wx.Application;
import com.nsw.wx.api.model.Media;
import com.nsw.wx.api.service.KfService;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.imp.KfServiceImp;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.common.config.MongoConfig;
import com.nsw.wx.common.docmodel.MassMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class SendMassMsgCtrlTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	
	@Test
	@Before
	public void beforeTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	/**
	 * 
	* @Description:群发文本消息
	* @param @throws Exception   
	* @return void  
	* @throws
	 */
	@Test
	public void massSendTxt_post() throws Exception{

		MassMessage  msg = new MassMessage();
		msg.setAppId("wx030cc5e930d2ce1f");
		msg.setContent("我就测试一哈预览接口");
		msg.setGroupid("-100");
		msg.setMassType("txt");
		msg.setOpenId("oS46Cs6aglXHgdkhJdjvrFWBt7JQ");
		JSONObject json = JSONObject.fromObject(msg);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/massmsg/sendMassMsg")
				.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
							.andDo(MockMvcResultHandlers.print()).andReturn();
	}


	//@Test
	public void massSendByJob() throws Exception{

		MassMessage  msg = new MassMessage();
		msg.setAppId("wx030cc5e930d2ce1f");
		msg.setContent("定时群发测试:做客友家，主邀用餐，觥筹交错，把酒言欢，忽停电，稍倾乃有屁出，觉有异，使手摸，触之湿润，嗅之曰：SHIT！\n" +
				"忽电来，尴尬不已，涂于馍，吞之曰：酱不错！");
		msg.setGroupid("-100");
		msg.setMassType("txt");
		msg.setJobTime("2016-08-10 15:29:00");

		JSONObject json = JSONObject.fromObject(msg);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/massmsg/sendMassMsgByJob")
						.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
	
	/**
	 * 
	* @Description:群发图片消息
	* @param @throws Exception   
	* @return void  
	* @throws
	 */
	@Test
	public void massSendPic_post() throws Exception{

		MassMessage  msg = new MassMessage();
		msg.setAppId("wx030cc5e930d2ce1f");
		msg.setGroupid("-100");
		msg.setMassType("pic");
		msg.setFileId("group1/M00/00/03/oYYBAFdjlfuAWpWDAAbkr2tX8Cw477.jpg");
		msg.setOpenId("oS46Cs6aglXHgdkhJdjvrFWBt7JQ");
		
		JSONObject json = JSONObject.fromObject(msg);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/massmsg/sendMassMsg")
				.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
							.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	/**
	 * 
	* @Description: 群发图文消息
	* @param @throws Exception   
	* @return void  
	* @throws
	 */
	@Test
	public void massSendNews_post() throws Exception{

		MassMessage  msg = new MassMessage();
		msg.setAppId("wx030cc5e930d2ce1f");
		msg.setGroupid("-100");
		msg.setMassType("news");
		msg.setMediaId("5767947a2e9dd7076bdccae9");
		msg.setOpenId("oS46Cs6aglXHgdkhJdjvrFWBt7JQ");
		
		JSONObject json = JSONObject.fromObject(msg);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/massmsg/sendMassMsg")
				.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
							.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
	
	@Test
	public void massSendNews_getList() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.get("/massmsg/massMsgList").param("appId", "wx030cc5e930d2ce1f"))
							.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
	/**
	 * 
	* @Description: TODO 测试客服接口发图文
	* @param    
	* @return void  
	* @throws
	 */
	@Test
	public void testSendNewsByKfInter(){
		
		KfService s = new KfServiceImp();
		JSONObject j = new JSONObject();
		j.put("touser", "oS46Cs6aglXHgdkhJdjvrFWBt7JQ");
		j.put("msgtype", "mpnews");
		JSONObject c = new JSONObject();
		c.put("media_id", "a2diQpKgiWMHbTJRKMmnOArim2-LA_Re_W8l_IXZWCEyP5od0KUzDy1zck55rTc6");
		j.put("mpnews",c);
		JSONObject a =  s.sendMsg("1uZBHTxGRbTGbjrtugyMEhPMtGEKtg15NN8xOnZMABA_QXmk1XkP8k--tw-_b9bMCCNWzFf2oHUrAzFU6oNDtpdS8SDKMuzyd2CboDcLFwgLPJdAIAFJD", j,null);
		System.out.println(a);
		
	}
	/**
	 * 
	* @Description: 获取永久素材
	* @param    
	* @return void  
	* @throws
	 */
	@Test
	public void testGetNewsUrl(){
		MaterialService service = new MaterialServiceImp();
		Media m = new Media("en_3WIIU66IDzu9Ftpr5Rrluahhdire2Ql9BIQmjYATSYDHhfD3V6nnEeXTypDuv");
		JSONObject o = service.downLoadMatter("5FjzJl0u3DiiLanzAP5WpBGvgfNjZEf2F6SiF4CBXo2tLXv2Uxb6uDbv3wm9WYR_1afDLG4Y-lHuhCZElYUW3t5G72dEBIdFod1o-MKkAU5hQQCWkrZdDheykP7TrNjrGVJgAEDWLZ", m, null);
		System.out.println(o);
	}
	
	
	
}

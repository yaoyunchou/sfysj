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
import com.nsw.wx.common.config.MongoConfig;
import com.nsw.wx.common.docmodel.WxMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class MessageCtrlTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	
	
	@Test
	@Before
	public void beforeTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
//	@Test
	public void sendMsgTxt_post() throws Exception{

		WxMessage msg = new WxMessage();
		msg.setAppId("wx030cc5e930d2ce1f");
		msg.setContent("1919191919");
		msg.setOpenId("oS46Cs6aglXHgdkhJdjvrFWBt7JQ");
		msg.setMsgType("txt");
		JSONObject json = JSONObject.fromObject(msg);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/message/sendMsg")
				.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
							.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
//	@Test
	public void sendMsgPic_post() throws Exception{
		WxMessage msg = new WxMessage();
		msg.setAppId("wx030cc5e930d2ce1f");
		msg.setMediaId("group1/M00/1D/22/wKgEoFdH7-yAEFV1AAfAcjTe8mY021.jpg");
		msg.setOpenId("oS46Cs6aglXHgdkhJdjvrFWBt7JQ");
		msg.setMsgType("pic");
		JSONObject json = JSONObject.fromObject(msg);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/message/sendMsg")
				.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
							.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
//	@Test
	public void sendMsgNews_post() throws Exception{
		WxMessage msg = new WxMessage();
		msg.setAppId("wx030cc5e930d2ce1f");
		msg.setMediaId("574c30732e9d78da993b135c");
		msg.setOpenId("oS46Cs6aglXHgdkhJdjvrFWBt7JQ");
		msg.setMsgType("news");
		JSONObject json = JSONObject.fromObject(msg);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/message/sendMsg")
				.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
							.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
	

}

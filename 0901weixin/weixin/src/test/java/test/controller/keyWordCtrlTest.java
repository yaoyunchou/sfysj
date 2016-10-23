package test.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class keyWordCtrlTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	

	@Test
	@Before
	public void beforeTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	//@Test
	public void keyWord_get() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.get("/keyWord/list").param("appId", "wx6628d33ac319e694").param("type", "1"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		
		
	}
	
	//@Test
	public void keyWord_del() throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("appId", "111");
		map.put("ids", "111");
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/keyWord/reply").param("appId", "111").param("ids", new String[]{"111"}))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		
		
	}

}

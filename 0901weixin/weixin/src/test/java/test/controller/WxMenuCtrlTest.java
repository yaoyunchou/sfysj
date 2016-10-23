package test.controller;

import java.util.ArrayList;
import java.util.List;
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
import com.nsw.wx.api.model.MenuButton;
import com.nsw.wx.api.model.MenuCreate;
import com.nsw.wx.common.config.MongoConfig;
import com.nsw.wx.common.docmodel.MenuEntity;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class WxMenuCtrlTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	
	@Test
	@Before
	public void beforeTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	
	//@Test//已调通
	public void menu_post() throws Exception{
		
		MenuEntity  en = new MenuEntity();
		MenuCreate create = new MenuCreate();
		List<MenuButton>  list = new ArrayList<MenuButton>();
		MenuButton b1 = new MenuButton();
		b1.setType("click");
		b1.setName("name");
		b1.setKey("V1001_TODAY_MUSIC");
		list.add(b1);
		create.setButton(list);
		en.setAppId("wx6628d33ac319e694");
		
		JSONObject json = JSONObject.fromObject(en);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/menu/menuInfo")
				.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
							.andDo(MockMvcResultHandlers.print()).andReturn();
		
		
	}
	
	
	
	//@Test//已调通
	public void menu_get() throws Exception{
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/menu/menuInfo").param("appId", "wx6628d33ac319e694"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		
	}
	
	//@Test//已调通
	public void menu_del() throws Exception{
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/menu/menuInfo").param("appId", "wx6628d33ac319e694"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		
	}
	
	
	//@Test
	public void menu_put() throws Exception{
		
		MenuEntity  en = new MenuEntity();
		List<MenuButton>  list = new ArrayList<MenuButton>();
		MenuButton b1 = new MenuButton();
		b1.setType("click");
		b1.setName("name");
		b1.setKey("12331");
		list.add(b1);
		en.setAppId("wxcb9bc8ace20d2c5c");
		en.setButton(list);
		
		JSONObject json = JSONObject.fromObject(en);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/menu/menuInfo")
				.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
							.andDo(MockMvcResultHandlers.print()).andReturn();
		
		
	}
	
	

}

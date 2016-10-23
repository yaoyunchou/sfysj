package test.controller;

import javax.servlet.annotation.MultipartConfig;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nsw.wx.Application;
import com.nsw.wx.api.service.QrCodeService;
import com.nsw.wx.api.service.imp.QrCodeServiceImp;
import com.nsw.wx.common.config.MongoConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class QrCodeCtrlTest {
	
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	
	
	@Test
	@Before
	public void beforeTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	
	@Test
	public void createQrCode(){
		QrCodeService service = new QrCodeServiceImp();
		
		JSONObject data = new JSONObject();
		data.put("action_name", "pengge");
		JSONObject action_info = new JSONObject();
		JSONObject scene = new JSONObject();
		scene.put("scene_str", "123");
		action_info.put("scene", scene);
		data.put("action_info", action_info);
		
		JSONObject jsonRst =   service.createGrcode("WSaZ9mEmvKy0GieHbklVCqZqtVnr-GLOeWMoy95uY4Ju4LcLJS4UO0PAQnruWfUUX6pBs3Uk3GU6NML6QeZMugEukfatRC9fPwpabtrFdqZHZdN5cM-402n4MUbvmSG1TGNjAEDBOV", data, null);
		System.out.println(jsonRst);
	}

	public static void main(String[] args) {
		System.out.println(111);
	}
	
}

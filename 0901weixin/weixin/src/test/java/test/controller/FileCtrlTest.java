package test.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nsw.wx.Application;
import com.nsw.wx.common.config.MongoConfig;
import com.nsw.wx.common.json.Jackson;
import com.nsw.wx.common.service.MongoFileOperationService;
import com.nsw.wx.common.util.ContextUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class FileCtrlTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	//public static String localFilePath = ContextUtil.getClassPath("/upload/img/");
	@Autowired
	private MongoFileOperationService mongoFileOperationService;
	@Test
	@Before
	public void beforeTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		//http://192.168.4.160:8080/nswcms/appid/resource/images/appid.png
	}
	
	//@Test
	public void saveFileToServerTest() throws Exception {
		File file = new File("d://111.jpg");
		FileInputStream fis = new FileInputStream(file);
		 HashMap<String, String> contentTypeParams = new HashMap<String, String>();
	     MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
	     contentTypeParams.put("names", "blk");
	    //上传一次后，再上传需要修改上传名称，因为在fs上已经存在该文件
		MockMultipartFile mFile = new MockMultipartFile("file", "blk_default.png", mediaType.getType(), fis);
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/file/local/upload").file(mFile).param("appId", "appid"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		String jsonString = result.getResponse().getContentAsString();
		Map<String, Object> map = new Jackson().fromJsonObject(jsonString);
		boolean flag = (boolean) map.get("isSuccess");
		if(flag){
			Map<String, Object> data =  (Map<String, Object>) map.get("data");
			String _id = (String) data.get("_id");
			System.out.println(_id);
			//mongoFileOperationService.delete(query);
		}
	
		
	}
	
	
	
	/**
	 * 
	* @Description: 上传图片到微信素材图片库
	* @param @throws Exception   
	* @return void  
	* @throws
	 */
	//@Test
	public void saveWXImage() throws Exception {
		File file = new File("d://111.jpg");
		FileInputStream fis = new FileInputStream(file);
		 HashMap<String, String> contentTypeParams = new HashMap<String, String>();
	     MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
	     contentTypeParams.put("names", "blk");
	    //上传一次后，再上传需要修改上传名称，因为在fs上已经存在该文件
		MockMultipartFile mFile = new MockMultipartFile("file", "blk_default.png", mediaType.getType(), fis);
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/material/image/upload").file(mFile).param("appId", "appid"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		String jsonString = result.getResponse().getContentAsString();
		Map<String, Object> map = new Jackson().fromJsonObject(jsonString);
		boolean flag = (boolean) map.get("isSuccess");
		if(flag){
			Map<String, Object> data =  (Map<String, Object>) map.get("data");
			String _id = (String) data.get("_id");
			System.out.println(_id);
			//mongoFileOperationService.delete(query);
		}
	
		
	}
	
	
	
	
	/***
	 * 文件列表查询
	 * @throws Exception 
	 */
	public void fileListTest() throws Exception{
			mockMvc.perform(
					MockMvcRequestBuilders.get("/file/list").param("pageNum", "1").param("pageSize", "30")).
					andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
}

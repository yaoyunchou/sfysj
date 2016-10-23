package test.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

import com.mongodb.WriteResult;
import com.nsw.wx.Application;
import com.nsw.wx.api.model.Article;
import com.nsw.wx.api.model.News;
import com.nsw.wx.common.config.MongoConfig;
import com.nsw.wx.common.docmodel.Account;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.DateUtils;




@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class MaterialCtrlTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	
	@Autowired
	private BaseMongoTemplate baseMongoTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	@Before
	public void beforeTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	public void saveFileToServerTest() throws Exception {
/*		File file = new File(localFilePath + "blk_default.png");
		FileInputStream fis = new FileInputStream(file);
		 HashMap<String, String> contentTypeParams = new HashMap<String, String>();
	     MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
	     contentTypeParams.put("names", "blk");
	    //上传一次后，再上传需要修改上传名称，因为在fs上已经存在该文件
		MockMultipartFile mFile = new MockMultipartFile("file", "blk_default.png", mediaType.getType(), fis);
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/file/local/upload").file(mFile))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		String jsonString = result.getResponse().getContentAsString();
		Map<String, Object> map = new Jackson().fromJsonObject(jsonString);
		boolean flag = (boolean) map.get("isSuccess");
		if(flag){
			Map<String, Object> data =  (Map<String, Object>) map.get("data");
			String _id = (String) data.get("_id");
		//	mongoFileOperationService.deleteFile(_id);
		}
		
		mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/file/local/upload").file(mFile).param("name", "wukangTest.jpg"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/material/materialInfoImage").param("appId", "wx6628d33ac319e694"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		*/
	}
	
	//@Test//已调通
	public void upload() throws Exception{
		File file = new File("D://111.jpg");
		FileInputStream fis = new FileInputStream(file);
		 HashMap<String, String> contentTypeParams = new HashMap<String, String>();
	     MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
	    //上传一次后，再上传需要修改上传名称，因为在fs上已经存在该文件
		MockMultipartFile mFile = new MockMultipartFile("file", "444.jpg", mediaType.getType(), fis);
	    mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/image/uploadlogo").file(mFile).param("appId", "wx6628d33ac319e694"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		
		
	}
	
	
	@Test//已调通  素材图片上传
	public void uploadImage() throws Exception{
		File file = new File("D://2.gif");
		FileInputStream fis = new FileInputStream(file);
		 HashMap<String, String> contentTypeParams = new HashMap<String, String>();
	     MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
	    //上传一次后，再上传需要修改上传名称，因为在fs上已经存在该文件
		MockMultipartFile mFile = new MockMultipartFile("file", "2.gif", mediaType.getType(), fis);
	    mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/material/image/upload").file(mFile).param("appId", "wx6628d33ac319e694"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		
		
	}
	
	
	
	/**
	 * 
	* @Description: 查询素材库图片
	* @param @throws Exception   
	* @return void  
	* @throws
	 */
	
//	@Test//已调通
	public void materialInfoImage_get() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.get("/material/materialInfoImage").param("appId", "wx6628d33ac319e694"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
		
		
	}
	
	
//	@Test//已调通
	public void materialInfo_get() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.get("/material/materialInfo").param("appId", "wx6628d33ac319e694").param("id", "b727VYJUEw-WW3TqWVGhzbOmltbrIv_zez-ibT0Ty6A"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
	
	//@Test//已调通
	public void materialInfo_delete() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/material/materialInfo").param("appId", "wx6628d33ac319e694").param("media_id", "PblfvHRfrTiPoxpSjKVQMpiwR9J7CRbxm1xdBy829oA"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
	
//	@Test
	public void updateAccount(){
		Criteria criteria=Criteria.where("nick_name").is("小胖子");
    	Query que = new Query();
    	que.addCriteria(criteria);
    	Update update = new Update(); 
    	update.set("component_verify_ticket", DateUtils.getCurrentTime());
    	update.set("update_time",DateUtils.getCurrentTime());
    	WriteResult wr =  mongoTemplate.updateFirst(que, update, Account.class);
    	boolean  status =wr.getN()==1?true:false;
    	if(status){
    		System.out.println(("##############推送component_verify_ticket成功-->ticket =###################,更新到数据库状态? "+status));
    	}
		
	}
	
	
	
	/**
	 * 
	* @Description: 查询素材库图文
	* @param @throws Exception   
	* @return void  
	* @throws
	 */
//	@Test//已调通
	public void materialInfoNews_get() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.get("/material/materialInfoNews").param("appId", "wx6628d33ac319e694"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
	/**
	 * 
	* @Description: 删除素材
	* @param @throws Exception   
	* @return void  
	* @throws
	 */
	//@Test//已调通
	public void materialInfoDel() throws Exception{
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/material/materialInfo").param("appId", "wx6628d33ac319e694").
				param("media_id", "b727VYJUEw-WW3TqWVGhzaCCz8ucH3wqr8-yz2qc1AY"))
				.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
//	@Test//已调通   图文上传
	public void materialInfoNews_post() throws Exception{

	    Map<String,Object> map = new HashMap<String, Object>();
	    map.put("appid", "");
	    map.put("articles", "");
	    
		mockMvc.perform(
				MockMvcRequestBuilders.post("/material/materialInfoNews").param("appId", "wx6628d33ac319e694")
				.contentType(MediaType.APPLICATION_JSON).content(map.toString()))
							.andDo(MockMvcResultHandlers.print()).andReturn();
	}
	
	
	
}

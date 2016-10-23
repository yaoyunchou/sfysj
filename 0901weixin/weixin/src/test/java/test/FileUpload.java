package test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import javax.servlet.annotation.MultipartConfig;
import javax.ws.rs.core.MediaType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.nsw.wx.Application;
import com.nsw.wx.common.config.MongoConfig;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class FileUpload {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	
	@Test
	@Before
	public void beforeTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void wxAccountInfo() throws Exception {
		
		 mockMvc.perform(
						MockMvcRequestBuilders.delete("/account/wxAccountInfo?id=wxcb9bc8ace20d2c5c")
								)
				                .andDo(MockMvcResultHandlers.print())
				                .andReturn();
	}
	
	
	
	/**
     * 存储文件 
     * @param collectionName 集合名 
     * @param file 文件 
     * @param fileid 文件id 
     * @param companyid 文件的公司id 
     * @param filename 文件名称
     */
	@Test
    public void SaveFile() {
        try {
            DB db = mongoTemplate.getDb();
            // 存储fs的根节点
            GridFS gridFS = new GridFS(db, "resource");
            File file = new File("D:/Chrysanthemum.jpg");
            
            GridFSInputFile gfs = gridFS.createFile(file);
            gfs.put("appId", "12121");
            gfs.put("fileId", "121");
            gfs.put("name", "12121");
            gfs.put("contentType", "");
            gfs.save();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("存储文件时发生错误！！！");
        }
    }
	
	@Test
	public void uploadFileTest()throws Exception{
		 String TEST_FILE = "D:/Chrysanthemum.jpg";
		 HashMap<String, String> contentTypeParams = new HashMap<String, String>();
	     MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
	     contentTypeParams.put("boundary", "265001916915724");
	    //上传一次后，再上传需要修改上传名称，因为在fs上已经存在该文件
		MockMultipartFile file1 = new MockMultipartFile("file", "test路虎3.zip", mediaType.getType(), new FileInputStream(TEST_FILE));
		//上传文件类型错误
		//MockMultipartFile file2 = new MockMultipartFile("file", "test路虎3.html", mediaType.getType(), new FileInputStream(TEST_FILE));
		/*//没有填写企业用户名*/
		mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/account/wxAccountInfo")
				        .file(file1)
				        )
			           .andDo(MockMvcResultHandlers.print())
			           .andReturn();
	}
	
	

}

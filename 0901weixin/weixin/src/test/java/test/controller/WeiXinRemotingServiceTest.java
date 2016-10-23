package test.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nsw.cms.common.service.WeiXinRemotingService;
import com.nsw.wx.Application;
import com.nsw.wx.common.config.MongoConfig;




@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class WeiXinRemotingServiceTest {
	
	@Autowired
	private WeiXinRemotingService weiXinRemotingService;
	
	//@Test
	public void listFilesTest(){
		//Map map = weiXinRemotingService.listFiles(new String[]{"1001"}, "李志鹏", 1, 20, "");
		//1.获取企业项目信息，比如是否开通 Pc 手机 等项目信息
		//Map map1 =  weiXinRemotingService.getProjectLists("27527");
		//System.out.println(map1);
		/**
		 * 返回结果   {projects=[{lastUpdTime=null, projId=27335_MP, projName=null, enterpriseId=27335, Id=1358, projectType=5}]}
		 */
		//2. 获取模块清单
		Map map2 = weiXinRemotingService.getModuleLists("27527_PC");
		
		System.out.println(map2);
		
	}
	
	
}

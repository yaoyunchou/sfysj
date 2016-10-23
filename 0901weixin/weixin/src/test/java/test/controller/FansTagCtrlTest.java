package test.controller;

import com.nsw.wx.Application;
import com.nsw.wx.common.config.MongoConfig;
import com.nsw.wx.common.repository.BaseMongoTemplate;
import com.nsw.wx.common.util.Constants;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.annotation.MultipartConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzp on 2016/8/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, MongoConfig.class, MultipartConfig.class })
@WebAppConfiguration
public class FansTagCtrlTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private BaseMongoTemplate baseMongoTemplate;

    @org.junit.Test
    @Before
    public void beforeTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testQuery() throws Exception{
        ArrayList arr = new ArrayList<>();
        arr.add(2);
        arr.add(3);
        Query query = new Query(Criteria.where("type").in(arr));
        List<Map<String,Object>> list =  baseMongoTemplate.queryMulti(query, Constants.WXACCOUNT_T);
        System.out.print("list size :"+list.size());
        for(Map<String,Object> m :list){
            System.out.print(m.toString());
        }
    }



    /**
     * 查找标签
     * @throws Exception
     */
    @Test
    public void tagList() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/fansTag/tagList").param("appId", "wx030cc5e930d2ce1f"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }

    /**
     * 查找粉丝
     * @throws Exception
     */
    @Test
    public void getFansByTag() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/fansTag/getFansByTag").param("appId", "wx030cc5e930d2ce1f"))
                .andDo(MockMvcResultHandlers.print()).andReturn();
    }




    /**
     * 修改用户标签
     * @throws Exception
     */
    @Test
    public void addUserTag() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.put("/fansTag/userTag").param("appId", "wx030cc5e930d2ce1f").
                        param("tagId",new String[]{"268","271","272"}).param("openId","oS46Cs6aglXHgdkhJdjvrFWBt7JQ"))
                .andDo(MockMvcResultHandlers.print()).andReturn();


    }

    /**
     * 批量为用户打标签
     */
    //@Test
    public void batchAddUserTag() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.post("/fansTag/batchAddUserTag").param("appId", "wx030cc5e930d2ce1f").
                        param("tagId",new String[]{"268","271"}).param("openId",new String[]{"oS46Cs6aglXHgdkhJdjvrFWBt7JQ","oS46Cs8qZuIndpKvSwjwDIzxQpM0"}))
                .andDo(MockMvcResultHandlers.print()).andReturn();

    }




}

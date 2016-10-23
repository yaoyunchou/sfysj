package test;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月23日 下午5:07:01
* @Description: 接口测试
 */
public class UserTest {
	public static void main(String[] args) {
		UserService userservice =new UserServiceImp();
		
		AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		String accessToken = AccessTokenHelper.getAccessToken();
		System.out.println("--------1获取token---------");
		System.out.println(accessToken);
		
		System.out.println("--------2获取用户基本信息--------");
		JSONObject use = userservice.getUserInfo(accessToken, "oS46Cs6aglXHgdkhJdjvrFWBt7JQ",null);
		System.out.println(use);
		
		System.out.println("--------3获取用户列表--------");
		JSONObject userList = userservice.getUserList(accessToken, "",null);
		System.out.println(userList);
		
		System.out.println("--------4设置用户备注名--------");
		JSONObject useremark = userservice.setUserRemark(accessToken, use.get("openid").toString(),"shuaige",null);
		System.out.println(useremark);
		
		System.out.println("--------5创建用户组--------");
		JSONObject json1=	userservice.createGroup(accessToken,"测试组11",null);
		System.out.println(json1);
		
		System.out.println("--------6查询所有分组--------");
		JSONObject json2 =userservice.getAllGroup(accessToken,null);
		System.out.println(json2);
		
		System.out.println("--------7查询用户所在分组--------");
		JSONObject json3 = userservice.getUsersGroup(accessToken,use.getString("openid"),null);
		System.out.println(json3);
		
		System.out.println("--------8修改分组--------");
		JSONObject json4 =userservice.editGroup(accessToken, 100,"xiugaihou",null);
		System.out.println(json4);
		
		System.out.println("--------9移动用户到分组--------");
		JSONObject json5  =userservice.changeUserGroup(accessToken, use.getString("openid"),2,null);
		
		System.out.println("--------10批量移动用户到分组--------");
		List<String> list = new ArrayList<String>();
		list.add(use.getString("openid"));
		JSONObject json7  =userservice.batchChangeUsersGroup(accessToken, list,2,null);
		System.out.println(json7);
		
		System.out.println("--------11删除组--------");
		JSONObject json6 = userservice.deleteGroup(accessToken,121,null);
		System.out.println(json6);
	}
}

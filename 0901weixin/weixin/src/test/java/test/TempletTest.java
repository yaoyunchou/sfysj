package test;

import net.sf.json.JSONObject;

import com.nsw.wx.api.service.TempletService;
import com.nsw.wx.api.service.UserService;
import com.nsw.wx.api.service.imp.TempletServiceImp;
import com.nsw.wx.api.service.imp.UserServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月23日 下午5:25:50
* @Description: 模板消息测试
 */
public class TempletTest {
	public static void main(String[] args) {
		TempletService templetService =new TempletServiceImp();
		UserService userservice =new UserServiceImp();
		
		AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		String accessToken = AccessTokenHelper.getAccessToken();
		
		System.out.println("2发送模板消息");
		//-poKBbUm_M17tXhJtCzAvKsGqKG-xUK5cwNZFdFQ9mo
		
		JSONObject use = userservice.getUserInfo(accessToken, "oS46Cs6aglXHgdkhJdjvrFWBt7JQ",null);

		String openid = (String) use.get("openid");
		
		
		String para = "{ \"touser\":\""+openid+"\","+
		           "\"template_id\":\"-poKBbUm_M17tXhJtCzAvKsGqKG-xUK5cwNZFdFQ9mo\","+
		           "\"url\":\"http://baidu.com\","+            
		           "\"data\":{\"first\": {\"value\":\"恭喜你购买成功！\",\"color\":\"#173177\"},\"keynote1\":{\"value\":\"巧克力\",\"color\":\"#173177\"},\"remark\":{\"value\":\"欢迎再次购买！\",\"color\":\"#173177\"}}}";
		
		JSONObject object1 =  templetService.sendTempletMsg(accessToken, para,null);
		System.out.println(object1);
		
		System.out.println("3.设置所属分类");
		
		String  para1 ="{\"industry_id1\":\"1\",\"industry_id2\":\"4\"}" ;
		JSONObject object2 = templetService.setIndustry(accessToken, para1,null);
		System.out.println(object2);
		System.out.println("4.获取微信服务器ip");
		JSONObject object3 = templetService.getWxIp(accessToken);
		System.out.println(object3);
	
	}
}

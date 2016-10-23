package test;


import net.sf.json.JSONObject;
import com.nsw.wx.api.service.MenuService;
import com.nsw.wx.api.service.imp.MenuServiceImp;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月23日 下午5:25:35
* @Description: 菜单接口测试
 */
public class MenuTest {
	
	public static void main(String[] args) {
		MenuService menuService = new MenuServiceImp();
		//AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		//AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		//String accessToken = AccessTokenHelper.getAccessToken();
		String accessToken = "dfmhuuIBZladNvN8Emgx8ZA94QONC9ZScRPGC6meDYboozMdhojeomeHeOfGnV250lOVngon7yNTI8y6Y2WUXLl0mpenkn1UR8Vj8lrM5EiA3iXuDR0uWzs_47NBbfs0TMVhAEDDWF";
		String str = "{\"button\":[{	\"type\":\"click\", \"name\":\"今日歌曲\", \"key\":\"V1001_TODAY_MUSIC\" }]}";
		JSONObject jsonStr  = JSONObject.fromObject(str);
		JSONObject json1 =  menuService.createMenu(jsonStr, accessToken,null);
		System.out.println("创建菜单结果"+json1);
		System.out.println("--------2获取菜单-----------");
		JSONObject json2 = menuService.getMenu(accessToken,null);
		System.out.println("获取菜单"+json2);
		
		//System.out.println("--------3删除菜单-----------");
		//JSONObject json3 = menuService.deleteMenu(accessToken);
		//System.out.println(json3);
	}
	
}

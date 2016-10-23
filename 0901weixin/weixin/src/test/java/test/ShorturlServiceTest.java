package test;

import net.sf.json.JSONObject;

import com.nsw.wx.api.service.imp.ShorturlServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月28日 下午5:55:16
* @Description: 长链接转短链接
 */
public class ShorturlServiceTest {
	public static void main(String[] args) {
		AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		String accessToken = AccessTokenHelper.getAccessToken();
		
		String a = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx030cc5e930d2ce1f&redirect_uri=http%3A%2F%2F112.74.102.103%2Foauths%2FOAuthAPIServlet&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		JSONObject obj =  new ShorturlServiceImp().longUrlToShort(a, accessToken,null);
		
		System.out.println(obj);
	}
}

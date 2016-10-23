package com.nsw.wx.api.util;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.AccessToken;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.service.BasicServices;
import com.nsw.wx.api.service.imp.BasicServiceImp;

public class AccessTokenHelper {
	
	public static AccessToken accessToken = null; 
	 // 第三方用户唯一凭证  
    public static String appid = "";  
    // 第三方用户唯一凭证密钥  
    public static String appsecret = ""; 
    
    public AccessTokenHelper(){
    	
    }
    
    public AccessTokenHelper(String appid,String appsecret){
    	this.appid = appid;
    	this.appsecret = appsecret;
    }
    
    
	/**
	 * 
	* @Description: 获取accessToken
	* @param @param appid
	* @param @param secret
	* @param @return   
	* @return AccessToken  
	* @throws
	 */
	public static  AccessToken setAccessToken()  {
		String tokenUrl=WeiXinApiUrl.AccesStoken_Get.replace("APPID", appid).replace("APPSECRET", appsecret);
		BasicServices service = new BasicServiceImp();
		JSONObject jsonObject = service.getMethodResult(tokenUrl,appid);
		accessToken = new AccessToken();
		if(jsonObject!=null&&!jsonObject.containsKey("errcode")){
			accessToken.setToken(jsonObject.getString("access_token"));
			accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			accessToken.setSavetime(System.currentTimeMillis());
			System.out.println(":::::::::已获取AccessToken:::::::::");
		}
		return accessToken;
	}
	/**
	 * 
	* @Description:获取accessToken
	* @param @return   
	* @return String  
	* @throws
	 */
	public static  String getAccessToken(){
//		boolean flag = isUse();
////		//如果失效或为空
//		if(flag==false){
//			setAccessToken();
//		}
		return accessToken.getToken();
	}
	/**
	 * 
	* @Description:获取token
	* @param @param appid
	* @param @param secret
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject getAccessToken(String appid, String secret)  {
		String tokenUrl=WeiXinApiUrl.AccesStoken_Get.replace("APPID", appid).replace("APPSECRET", secret);
		BasicServices service = new BasicServiceImp();
		JSONObject jsonObject =service. getMethodResult(tokenUrl,appid);
		return jsonObject;
	}
	/**
	 * 
	* @Description: 判断token是否失效
	* @param @return   
	* @return boolean  false为失效
	* @throws
	 */
	public static boolean isUse(){
		boolean flag = false;
		if(accessToken!=null){
			//判断是否失效  为避免微信服务器时间差  7200取7000
			flag = (System.currentTimeMillis() - accessToken.getSavetime())/1000-7000<0;
		}
		return flag;
	}
}

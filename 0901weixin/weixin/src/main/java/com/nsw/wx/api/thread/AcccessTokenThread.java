package com.nsw.wx.api.thread;  

import net.sf.json.JSONObject;
import com.nsw.wx.api.model.AccessToken;
import com.nsw.wx.api.util.AccessTokenHelper;
  
  
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月20日 下午5:47:59
* @Description: 定时获取微信access_token的线程 
 */
public class AcccessTokenThread implements Runnable {  
    // 第三方用户唯一凭证  
    public static String appid = "";  
    // 第三方用户唯一凭证密钥  
    public static String appsecret = "";  
    public static AccessToken accessToken = null;  
    
    public void run() {  
        while (true) {  
            try {  
            	AccessTokenHelper help = new AccessTokenHelper();
                JSONObject tokenjson =  help.getAccessToken(appid, appsecret);
                accessToken = new AccessToken();
                accessToken.setToken(tokenjson.getString("access_token"));
                accessToken.setExpiresIn(tokenjson.getInt("expires_in"));
                
                if (null != accessToken) {  
                    //System.out.println("获取access_token成功，有效时长{}秒 token:{}"+accessToken.getExpiresIn()+" "+accessToken.getToken());  
                    // 休眠7000秒  
                    Thread.sleep((accessToken.getExpiresIn() - 200) * 1000);  
                } else {  
                    // 如果access_token为null，60秒后再获取  
                    Thread.sleep(60 * 1000);  
                }  
            } catch (InterruptedException e) {  
                try {  
                    Thread.sleep(60 * 1000);  
                } catch (InterruptedException e1) {  
                   // log.error("{}", e1);  
                }  
                //log.error("{}", e);  
            }  
        }  
    }  
}  
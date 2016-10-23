package com.nsw.wx.api.model;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月15日 下午2:20:37
* @Description: TODO
 */
public class AccessToken {
	// 获取到的凭证  
    private String token;  
    // 凭证有效时间，单位：秒  
    private int expiresIn;  
    //取得token时的时间，可根据当前时间比较判断是否过期
    private long savetime;
    
    public String getToken() {  
        return token;  
    }  
  
    public void setToken(String token) {  
        this.token = token;  
    }  
  
    public int getExpiresIn() {  
        return expiresIn;  
    }  
  
    public void setExpiresIn(int expiresIn) {  
        this.expiresIn = expiresIn;  
    }

	public long getSavetime() {
		return savetime;
	}

	public void setSavetime(long savetime) {
		this.savetime = savetime;
	}  
}

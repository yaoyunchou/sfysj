package com.nsw.wx.api.exception;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月14日 上午10:15:00
* @Description: 微信异常类
 */
public class WexinApiException extends Exception
{
	
	private static final long serialVersionUID = 1L;
	
	public WexinApiException(String message)
	{
		super(message);
	}
	
	public WexinApiException(Throwable cause)
	{
		super(cause);
	}
	
	public WexinApiException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

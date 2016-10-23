package com.nsw.wx.common.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved. 
 * @date 2015年10月12日 下午12:07:01
 * @Description: 验证码错误异常类，继承AuthenticationException，如果验证码输入错误则由AuthenticationException统一处理
 * @Description: 在CustomAuthenticationFailureHandler统一输出
 */

@SuppressWarnings("all")
public class CaptchaException extends AuthenticationException {

	public CaptchaException(String msg) {
		super(msg);
	}
}

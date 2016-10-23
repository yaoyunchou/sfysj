package com.nsw.wx.common.service;

import java.util.Map;

import com.nsw.wx.common.docmodel.Account;

public interface OpenCommonService {
	
	/**
	 * 获取账号信息   获取 component_verify_ticket
	 * @param Appid
	 * 在公众号第三方平台创建审核通过后，微信服务器会向其“授权事件接收URL”每隔10分钟定时推送component_verify_ticket。
	 * 第三方平台方在收到ticket推送后也需进行解密（详细请见【消息加解密接入指引】），接收到后必须直接返回字符串success。
	 * 
	 * @return
	 */
	Account getAccountBasic();
	
	
	/**
	 * 获取第三方平台component_access_token
	 * @param Appid
	 * 第三方平台compoment_access_token是第三方平台的下文中接口的调用凭据，也叫做令牌（component_access_token）。
	 * 每个令牌是存在有效期（2小时）的，且令牌的调用不是无限制的，请第三方平台做好令牌的管理，在令牌快过期时（比如1小时50分）再进行刷新。
	 * @return
	 */
	Map<String,Object> getComponentAccessToken(String type);
	
	/**
	 * 
	* @Description: 删除公众号相关信息 
	* @param @param appId   
	* @return void  
	* @throws
	 */
	void deleteAccountInfo(String appId);
	
	
	
	
	/**
	 * 检测compontacccesstoken是否超时并提供刷新动作
	 * @param access_token
	 * @return
	 */
	public Map<String, Object> isExpireAndRefresh(Map<String, Object> access_token);
	
	
}

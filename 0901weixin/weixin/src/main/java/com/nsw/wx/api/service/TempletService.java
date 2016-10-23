package com.nsw.wx.api.service;

import net.sf.json.JSONObject;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月13日 上午10:28:28
* @Description: 模板消息管理
 */
public interface TempletService extends BasicServices{
	/**
	* @Description: 获取模板id
	* @param  accessToken
	* @param  para  参数示例:{"template_id_short":"TM00015"}
	* 	template_id_short		模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式
	* @return JSONObject  
	* 
	 */
	public JSONObject getTempletId(String accessToken, String para,String appId);
	/**
	 * 
	* @Description: 发送模板消息
	* @param  accessToken
	* @param  para  所需的post参数
	* 参数示例：
	*   { "touser":"OPENID",
           "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
           "url":"http://weixin.qq.com/download",            
           "data":{"first": {"value":"恭喜你购买成功！","color":"#173177"},"keynote1":{"value":"巧克力","color":"#173177"},"remark":{"value":"欢迎再次购买！","color":"#173177"}}
         }
	* @return JSONObject  
	* 
	 */
	public JSONObject sendTempletMsg(String accessToken, String para,String appId);
	
	
	/**
	 * 
	* @Description: 设置所属行业 ，一个月方可修改一次，设置行业可在MP中完成
	* @param @param accessToken
	* @param @param para
	* 	     {
          		"industry_id1":"1",
          		"industry_id2":"4"
       		}
       		公众号模板消息所属行业编号，行业代码查询参考微信api
	* @param @return   
	* @return JSONObject  
	* @throws
	 */
	public JSONObject setIndustry(String accessToken, String para,String appId);

	/**
	 * 获取设置的行业信息
	 * @param accessToken
	 * @param appId
	 * @return
	 */
	public JSONObject getIndustry(String accessToken,String appId);


	/**
	 * //获取模板列表
	 * @param accessToken
	 * @param appId
	 * @return
	 */
	public JSONObject getAllTemplate(String accessToken,String appId);

	/**
	 * 删除模板
	 * @param accessToken
	 * @param appId
	 * @param template_id
	 * @return
	 */
	public JSONObject delTemplate(String accessToken,String template_id, String appId);

}

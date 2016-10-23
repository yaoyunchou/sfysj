package test;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.DateModel;
import com.nsw.wx.api.service.DataUserService;
import com.nsw.wx.api.service.imp.DataUserServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月23日 下午5:06:07
* @Description: 
	
 */
public class DataUserServiceTest {
	/**
	
	参数	是否必须	说明
	access_token	是	调用接口凭证
	begin_date	是	获取数据的起始日期，begin_date和end_date的差值需小于“最大时间跨度”（比如最大时间跨度为1时，begin_date和end_date的差值只能为0，才能小于1），否则会报错
	end_date	是	获取数据的结束日期，end_date允许设置的最大值为昨日
	ref_date	数据的日期
	user_source	用户的渠道，数值代表的含义如下：
	0代表其他（包括带参数二维码） 3代表扫二维码 17代表名片分享 35代表搜号码（即微信添加朋友页的搜索） 39代表查询微信公众帐号 43代表图文页右上角菜单
	
	new_user	新增的用户数量
	cancel_user	取消关注的用户数量，new_user减去cancel_user即为净增用户数量
	cumulate_user	总用户量
	 *
	 */
	public static void main(String[] args) {
		DataUserService s = new DataUserServiceImp();
		AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		String accessToken = AccessTokenHelper.getAccessToken();
		
		 //设置时间跨度  时间跨度最大为 7
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 String end_date = df.format(new Date(new Date().getTime()-1000*60*60*24));
		 String begin_date = df.format(new Date(new Date().getTime()-1000*60*60*24*7));
		 DateModel d = new DateModel();
		 d.setBegin_date(begin_date);
		 d.setEnd_date(end_date);
		 
		//获取用户增减数据
		JSONObject o =  s.getusercumulate(accessToken, d,null);
		//获取累计用户数据 
		JSONObject o1 =  s.getusersummary(accessToken, d,null);
		System.out.println(o);
		System.out.println(o1);
		
		/**
		 * 正确返回示例：
		 * 获取用户增减数据  {"list":[{"ref_date":"2015-10-16","user_source":0,"cumulate_user":1},{"ref_date":"2015-10-17","user_source":0,"cumulate_user":1},{"ref_date":"2015-10-18","user_source":0,"cumulate_user":1},{"ref_date":"2015-10-19","user_source":0,"cumulate_user":1},{"ref_date":"2015-10-20","user_source":0,"cumulate_user":2},{"ref_date":"2015-10-21","user_source":0,"cumulate_user":2},{"ref_date":"2015-10-22","user_source":0,"cumulate_user":2}]}
		       获取累计用户数据 {"list":[{"ref_date":"2015-10-20","user_source":0,"new_user":0,"cancel_user":0},{"ref_date":"2015-10-20","user_source":17,"new_user":1,"cancel_user":0},{"ref_date":"2015-10-21","user_source":0,"new_user":0,"cancel_user":0},{"ref_date":"2015-10-22","user_source":0,"new_user":0,"cancel_user":0}]}
	
	
	
	
	返回参数说明

	参数	说明
	ref_date	数据的日期
	user_source	用户的渠道，数值代表的含义如下：
	0代表其他（包括带参数二维码） 3代表扫二维码 17代表名片分享 35代表搜号码（即微信添加朋友页的搜索） 39代表查询微信公众帐号 43代表图文页右上角菜单
	
	new_user	新增的用户数量
	cancel_user	取消关注的用户数量，new_user减去cancel_user即为净增用户数量
	cumulate_user	总用户量
		 */
		
	}
}

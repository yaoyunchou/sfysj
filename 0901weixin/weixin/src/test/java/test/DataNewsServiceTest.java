package test;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;
import com.nsw.wx.api.model.DateModel;
import com.nsw.wx.api.service.DataNewsService;
import com.nsw.wx.api.service.imp.DataNewsServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;

public class DataNewsServiceTest {
	/**
	 * 说明：
	 接口名称		最大时间跨度	
	获取图文群发每日数据（getarticlesummary）	1	
	获取图文群发总数据（getarticletotal）	1	
	获取图文统计数据（getuserread）	3	
	获取图文统计分时数据（getuserreadhour）	1	
	获取图文分享转发数据（getusershare）	7	
	获取图文分享转发分时数据（getusersharehour）	1	
最大时间跨度是指一次接口调用时最大可获取数据的时间范围，如最大时间跨度为7是指最多一次性获取7天的数据。access_token的实际值请通过“获取access_token”来获取。
	 */
	public static void main(String[] args) {
		
		AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		String accessToken = AccessTokenHelper.getAccessToken();
		DataNewsService service = new DataNewsServiceImp();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String end_date = df.format(new Date(new Date().getTime()-1000*60*60*24));
		String begin_date = df.format(new Date(new Date().getTime()-1000*60*60*24));

		DateModel d = new DateModel();
		d.setBegin_date(begin_date);
		d.setEnd_date(end_date);
		//时间跨度以上只是示例，具体根据业务封装

		 JSONObject object =   service.getarticlesummary(accessToken, d,null);
		 JSONObject object1 =  service.getarticletotal(accessToken, d,null);
		 JSONObject object2 =  service.getuserread(accessToken, d,null);
		 JSONObject object3 =  service.getuserreadhour(accessToken, d,null);
		 JSONObject object4 =  service.getusershare(accessToken, d,null);
		 JSONObject object5 =  service.getusersharehour(accessToken, d,null);
		 JSONObject object6 =  service.getarticletotal(accessToken, d,null);
		 
		 /**
		  * 返回参数说明

返回参数说明

参数	说明
ref_date	数据的日期，需在begin_date和end_date之间
ref_hour	数据的小时，包括从000到2300，分别代表的是[000,100)到[2300,2400)，即每日的第1小时和最后1小时
stat_date	统计的日期，在getarticletotal接口中，ref_date指的是文章群发出日期， 而stat_date是数据统计日期
msgid	请注意：这里的msgid实际上是由msgid（图文消息id，这也就是群发接口调用后返回的msg_data_id）和index（消息次序索引）组成， 例如12003_3， 其中12003是msgid，即一次群发的消息的id； 3为index，假设该次群发的图文消息共5个文章（因为可能为多图文），3表示5个中的第3个
title	图文消息的标题
int_page_read_user	图文页（点击群发图文卡片进入的页面）的阅读人数
int_page_read_count	图文页的阅读次数
ori_page_read_user	原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0
ori_page_read_count	原文页的阅读次数
share_scene	分享的场景
1代表好友转发 2代表朋友圈 3代表腾讯微博 255代表其他

share_user	分享的人数
share_count	分享的次数
add_to_fav_user	收藏的人数
add_to_fav_count	收藏的次数
target_user	送达人数，一般约等于总粉丝数（需排除黑名单或其他异常情况下无法收到消息的粉丝）
user_source	在获取图文阅读分时数据时才有该字段，代表用户从哪里进入来阅读该图文。0:会话;1.好友;2.朋友圈;3.腾讯微博;4.历史消息页;5.其他
		  */
	}
}

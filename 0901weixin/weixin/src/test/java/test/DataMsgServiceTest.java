package test;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.DateModel;
import com.nsw.wx.api.service.DataMsgService;
import com.nsw.wx.api.service.imp.DataMsgServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月23日 下午5:42:11
* @Description: 消息分析数据接口测试
 */
public class DataMsgServiceTest {
	
	/**
接口名称	最大时间跨度	接口调用地址（必须使用https）
获取消息发送概况数据（getupstreammsg）	7	https://api.weixin.qq.com/datacube/getupstreammsg?access_token=ACCESS_TOKEN
获取消息分送分时数据（getupstreammsghour）	1	https://api.weixin.qq.com/datacube/getupstreammsghour?access_token=ACCESS_TOKEN
获取消息发送周数据（getupstreammsgweek）	30	https://api.weixin.qq.com/datacube/getupstreammsgweek?access_token=ACCESS_TOKEN
获取消息发送月数据（getupstreammsgmonth）	30	https://api.weixin.qq.com/datacube/getupstreammsgmonth?access_token=ACCESS_TOKEN
获取消息发送分布数据（getupstreammsgdist）	15	https://api.weixin.qq.com/datacube/getupstreammsgdist?access_token=ACCESS_TOKEN
获取消息发送分布周数据（getupstreammsgdistweek）	30	https://api.weixin.qq.com/datacube/getupstreammsgdistweek?access_token=ACCESS_TOKEN
获取消息发送分布月数据（getupstreammsgdistmonth）	30	https://api.weixin.qq.com/datacube/getupstreammsgdistmonth?access_token=ACCESS_TOKEN
	 */
	public static void main(String[] args) {
		AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		String accessToken = AccessTokenHelper.getAccessToken();

		DataMsgService service = new DataMsgServiceImp();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String end_date = df.format(new Date(new Date().getTime()-1000*60*60*24));
		String begin_date = df.format(new Date(new Date().getTime()-1000*60*60*24));

		DateModel d = new DateModel();
		d.setBegin_date(begin_date);
		d.setEnd_date(end_date);
		
		//时间跨度自己根据业务封装，以上只是示例
		
		 JSONObject object =  service.getupstreammsg(accessToken, d,null);
		 JSONObject object1 =  service.getupstreammsghour(accessToken, d,null);
		 JSONObject object2 =  service.getupstreammsgweek(accessToken, d,null);
		 JSONObject object3 =  service.getupstreammsgmonth(accessToken, d,null);
		 JSONObject object4 =  service.getupstreammsgdist(accessToken, d,null);
		 JSONObject object5 =  service.getupstreammsgdistweek(accessToken, d,null);
		 JSONObject object6 =  service.getupstreammsgdistmonth(accessToken, d,null);
		 
		 /**
		  * 返回参数说明

		参数	说明
		ref_date	数据的日期，需在begin_date和end_date之间
		ref_hour	数据的小时，包括从000到2300，分别代表的是[000,100)到[2300,2400)，即每日的第1小时和最后1小时
		msg_type	消息类型，代表含义如下：
		1代表文字 2代表图片 3代表语音 4代表视频 6代表第三方应用消息（链接消息）
		
		msg_user	上行发送了（向公众号发送了）消息的用户数
		msg_count	上行发送了消息的消息总数
		count_interval	当日发送消息量分布的区间，0代表 “0”，1代表“1-5”，2代表“6-10”，3代表“10次以上”
		int_page_read_count	图文页的阅读次数
		ori_page_read_user	原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0
		  */
	}
}

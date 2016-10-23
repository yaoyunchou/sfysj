package test;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;
import com.nsw.wx.api.model.DateModel;
import com.nsw.wx.api.service.DataInterfaceService;
import com.nsw.wx.api.service.imp.DataInterfaceServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;

/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月23日 下午5:48:45
* @Description:测试接口分析数据接口
 */
public class DataInterfaceServiceTest {
	
	public static void main(String[] args) {
		/**
		 * 
			接口名称	最大时间跨度	接口调用地址（必须使用https）
			获取接口分析数据（getinterfacesummary）	30	
			获取接口分析分时数据（getinterfacesummaryhour）	1	
		 */
		
		AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		String accessToken = AccessTokenHelper.getAccessToken();

		DataInterfaceService service = new DataInterfaceServiceImp();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String end_date = df.format(new Date(new Date().getTime()-1000*60*60*24));
		String begin_date = df.format(new Date(new Date().getTime()-1000*60*60*24));
		DateModel d = new DateModel();
		d.setBegin_date(begin_date);
		d.setEnd_date(end_date);
		//最大时间跨度自行根据业务进行封装
		

		JSONObject object =  service.getinterfacesummary(accessToken, d,null);
		JSONObject object1 =  service.getinterfacesummaryhour(accessToken, d,null);
		
		/**
		 * 返回参数说明

		参数	说明
		ref_date	数据的日期
		ref_hour	数据的小时
		callback_count	通过服务器配置地址获得消息后，被动回复用户消息的次数
		fail_count	上述动作的失败次数
		total_time_cost	总耗时，除以callback_count即为平均耗时
		max_time_cost	最大耗时
		 */
	}
}

package test;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.mass.MassFilter;
import com.nsw.wx.api.model.mass.Text;
import com.nsw.wx.api.model.mass.TextMass;
import com.nsw.wx.api.service.MessageService;
import com.nsw.wx.api.service.imp.MessageServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;

/**
 * 群发接口测试
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月20日 下午2:38:57
* @Description: 群发接口测试
 */
public class MessageTest {
	public static void main(String[] args) {
		MessageService service = new MessageServiceImp();
		AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		String accessToken = AccessTokenHelper.getAccessToken();
		TextMass textMass = new TextMass();
		Text text  = new Text();
		text.setContent("群发文本消息测试ss");
		MassFilter filter = new MassFilter();
		filter.setGroup_id("");//如果是分组群发此处需要设置组id 
		filter.setIs_to_all(true);//如果是发送全部此处设为true否则为false
		textMass.setFilter(filter);
		textMass.setText(text);
		
		JSONObject object1 =  service.massTextByGroup(textMass, accessToken,null,null);
		System.out.println(object1);
	}
}

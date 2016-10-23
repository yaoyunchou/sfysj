package test;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.Custorm;
import com.nsw.wx.api.service.KfService;
import com.nsw.wx.api.service.imp.KfServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月23日 下午5:29:22
* @Description: 客服接口
 */
public class KfServiceTest {
	public static void main(String[] args) {
		
		KfService s = new KfServiceImp();
		/*AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		String accessToken = AccessTokenHelper.getAccessToken();
		//获取所有客服列表
		JSONObject object1 =  s.getCustormList(accessToken);
		System.out.println(object1);
		Custorm custorm = new Custorm();
		custorm.setKf_account("");
		custorm.setNickname("");
		custorm.setPassword("");
		//添加
		s.addCustorm(accessToken, custorm);
		//修改
		s.editCustorm(accessToken, custorm);
		//删除
		s.delCustorm(accessToken, custorm);*/
		JSONObject j = new JSONObject();
		j.put("touser", "odqwHuCyHukxR85PVdRvlw31JzTw");
		j.put("msgtype", "mpnews");
		JSONObject c = new JSONObject();
		c.put("media_id", "a2diQpKgiWMHbTJRKMmnOArim2-LA_Re_W8l_IXZWCEyP5od0KUzDy1zck55rTc6");
		
		j.put("mpnews",c);
		
		
		JSONObject a =  s.sendMsg("1uZBHTxGRbTGbjrtugyMEhPMtGEKtg15NN8xOnZMABA_QXmk1XkP8k--tw-_b9bMCCNWzFf2oHUrAzFU6oNDtpdS8SDKMuzyd2CboDcLFwgLPJdAIAFJD", j,null);
		System.out.println(a);
		
	}
}

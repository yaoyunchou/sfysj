package test;


import com.nsw.wx.api.service.OauthService;
import com.nsw.wx.api.service.imp.OauthServiceImp;

public class OauthServiceTest {
	public static void main(String[] args) {
		OauthService  service = new OauthServiceImp();
		
		/*//如果在具体项目中获取授权信息 ，可以这样调用
		
		//String code = request.getParameter("code");
		OauthService dao=new OauthServiceImp();
	    UserEntity entity=null;
		try {
			OAuthAccessToken oac = dao.getOAuthAccessToken(useValue.AppId, useValue.AppSecret,code);//通过code换取网页授权access_token
			//log.info("--------------------"+oac.getAccessToken()+";"+oac.getRefreshToken()+";"+oac.getScope()+";"+oac.getOpenid());
			OAuthAccessToken oacd=dao.refershOAuthAccessToken(useValue.AppId, oac.getRefreshToken());//刷新access_token
			//log.info("--------------------"+oacd.getAccessToken()+";"+oacd.getRefreshToken()+";"+oacd.getScope()+";Openid:"+oacd.getOpenid());
			entity=dao.acceptOAuthUserNews(oacd.getAccessToken(),oacd.getOpenid());//获取用户信息
			log.info("--------------------"+entity.getNickname()+";"+entity.getCountry());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("user", entity);
		//跳转到界面
		request.getRequestDispatcher("index.jsp").forward(request, response);*/
		
		//获取授权的地址
		
		
		String url="http://112.74.102.103/wxjar1/OAuthAPIServlet";
		String pathUrl=null;
		
		String appid = "wx030cc5e930d2ce1f";
		
		try {
			pathUrl=service.getCodeUrl(appid,url,"snsapi_userinfo", "STATE",null);
		} catch (Exception e) {
			
		}
		System.out.println(pathUrl);
		//正确输出结果  ：  https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx030cc5e930d2ce1f&redirect_uri=http%3A%2F%2F112.74.102.103%2FOAUTH%2Fservlet%2FOAuthAPIServlet&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect

		
	}
}

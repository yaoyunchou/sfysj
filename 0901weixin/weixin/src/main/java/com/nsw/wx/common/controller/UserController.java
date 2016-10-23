package com.nsw.wx.common.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nsw.wx.common.model.SiteGroup;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.nsw.wx.common.model.Enterprise;
import com.nsw.wx.common.model.Site;
import com.nsw.wx.common.model.User;
import com.nsw.wx.common.security.CustomSecurityUser;
import com.nsw.wx.common.security.SpringSecurityUtils;
import com.nsw.wx.common.service.FTPService;
import com.nsw.wx.common.service.UserService;
import com.nsw.wx.common.service.WxAccountService;
import com.nsw.wx.common.util.AjaxUtil;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;


@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	private FTPService ftpService;
	
	@Value("${cmsRemoteUrl}")
	private String cmsRemoteUrl;
	
	
	@Value("${superadmin_domain}")
	private String superadmin_domain;
	
	@Autowired
	private UserDetailsService currentUserDetailsService;
	
	Logger logger = Logger.getLogger(UserController.class);
	
	/***
	 * 显示登录名
	 * @Description: TODO
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "loginUser")
	@ResponseBody
	public String getCurrentUser() {
		CustomSecurityUser securityUser = ContextUtil.getLoginUser();
		if(securityUser != null ){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", SpringSecurityUtils.getCurrentUserName());
			//如果开通了pc，返回ftp主页信息 formal_domain,没开通或者没填域名 就返回 #
			Site site =  ftpService.findSite(Long.parseLong(securityUser.getSite()), 4);
			String formal_domain = "#";
			formal_domain = (site==null?formal_domain:site.getFormalDomain());
			if(formal_domain.length()>1){
				formal_domain = formal_domain.startsWith("http://")?formal_domain:"http://"+formal_domain;
			}
			map.put("formal_domain", formal_domain);
			map.put("cmsProjectIndex", cmsRemoteUrl+"js/personal/index.html#/switchProject");
			User user = userService.findOne(securityUser.getId());
			map.put("displayLogoutBtn", true);
			if(user != null){
				String resource = user.getResource();
				if(resource!=null){
					String propertyData = superadmin_domain;
					logger.info("用户存储:" +resource + "  "+ "配置文件" +propertyData + "  "+    (resource.indexOf(propertyData) != -1)      );
					boolean displayLogoutBtn = true;//默认显示按钮
					if(resource != null && !"".equals(resource) && resource.toLowerCase().indexOf(propertyData.toLowerCase()) != -1){//从超级后台来的ZHJZXQ-25
							displayLogoutBtn = false;//从超级后台来的ZHJZXQ-25超级后台登录的都隐藏2016-05-14 需求变更
					}
					map.put("displayLogoutBtn", displayLogoutBtn);
				}else{
					map.put("displayLogoutBtn", true);
				}
			}
			return AjaxUtil.renderSuccessMsg(map);
		}
		return AjaxUtil.renderFailMsg("请重新登录!");
		
	}
	
	
	/***
	 * 获取用户信息
	 * @Description: TODO
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping(value ="userInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getUserInfo() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		CustomSecurityUser securityUser = ContextUtil.getLoginUser();
		Map<String, Object> role = null;
		if (securityUser != null) {
			Site site =  ftpService.findSite(Long.parseLong(securityUser.getSite()), 7);
			if(site!=null){
				
				long nowAccountNum =  wxAccountService.getWxAccountList(securityUser.getSite()).size();
				
				//企业开通信息，账号还有多久过期，公众号数量
				//开通时长
				Float duration = site.getDuration();
				//微信公众号个数 
				int wechatPublicAccountNum = site.getWechatPublicAccountNum();
				JSONObject wxInfo = new JSONObject();
				int allDay = (int) (duration * 365);
				String  expirationTime =   afterNDay(allDay);
				wxInfo.put("duration", duration);
				wxInfo.put("allDay", allDay);
				wxInfo.put("expirationTime", expirationTime);
				wxInfo.put("wechatPublicAccountNum", wechatPublicAccountNum);
				wxInfo.put("wechatMessageNum", site.getWechatMessageNum());
				wxInfo.put("useAccountNum", nowAccountNum);
				map.put("wxInfo", wxInfo);
			}
			
			 role = userService.getRoleByUserId(securityUser.getId());
			Enterprise enterPrise = userService.getEnterPrise(ContextUtil.getSite());
			if(enterPrise != null){
				map.put("fullCustomerName", enterPrise.getFullCustomerName());
			}
			role = role == null ? new HashMap<String, Object>() : role;
			Map<String, String> user = null;
			try {
				user = userService.getUserInfo(securityUser.getType(),securityUser.getId() );
			} catch (Exception e) {
				//user = new HashMap<String, String>();
				e.printStackTrace();
			}
			map.put("lastLoginTime",
					securityUser.getLastLoginTime() == null ? "" : securityUser.getLastLoginTime());
			map.put("lastLoginIp", securityUser.getLastLoginIp() == null ? "": securityUser.getLastLoginIp());
			map.put("roleName", securityUser.getRoleName() == null ? "": securityUser.getRoleName());
			map.put("loginTimes", securityUser.getLoginTimes() == null ? "": securityUser.getLoginTimes());
			map.put("loginIp", securityUser.getLoginIp() == null ? "": securityUser.getLoginIp());
//			map.put("deptName", user.get("dept_name") + "");
			if(user == null){
				return AjaxUtil.renderSuccessMsg(map);
			}
			
			if ("0".equals(securityUser.getType())) {// 内部用户
				map.put("name",user.get("full_name") == null ? "" : user.get("full_name") + "");
				map.put("deptName",user.get("dept_name") == null ? "" : user.get("dept_name") + "");
				map.put("mobile",user.get("phone_number") == null ? "" : user.get("phone_number") + "");
				map.put("email",user.get("email_address") == null ? "" : user.get("email_address") + "");
				map.put("roleName",role.get("roleName") == null ? "" : role.get("roleName") + "");
			} else {// 企业用户
				String contact_phone_number = user.get("contact_phone_number")==null?"":user.get("contact_phone_number");
				if (contact_phone_number != null&& contact_phone_number.length() == 11) {
					contact_phone_number = contact_phone_number.substring(0, 3)+ "****"+ contact_phone_number.substring(7,contact_phone_number.length());
				}
				map.put("name",user.get("name") == null ? "" : user.get("name") + "");// 真实名字
				map.put("deptName",user.get("dept_name") == null ? "" : user.get("dept_name") + "");
				map.put("mobile", contact_phone_number == null ? "": contact_phone_number);
				map.put("email",user.get("contact_email") == null ? "" : user.get("contact_email") + "");
				map.put("roleName",role.get("roleName") == null ? "" : role.get("roleName") + "");
				map.put("userType", "1");
			}
		}
		if(securityUser != null ){
			User user = userService.findOne(securityUser.getId());
			if(user != null){
				//String resource = user.getResource();
				//String propertyData = PropertiesUtils.getParametersKey("superadmin_domain");
				//logger.info("用户存储:" +resource + "  "+ "配置文件" +propertyData + "  "+    (resource.indexOf(propertyData) != -1)      );
				//boolean displayLogoutBtn = true;//默认显示按钮
				//if(resource != null && !"".equals(resource) && resource.toLowerCase().indexOf(propertyData.toLowerCase()) != -1){//从超级后台来的ZHJZXQ-25
					//	displayLogoutBtn = false;//从超级后台来的ZHJZXQ-25超级后台登录的都隐藏2016-05-14 需求变更
			//	}
				map.put("displayLogoutBtn", true);
			}
		}
		return AjaxUtil.renderSuccessMsg(map);
	}
	
	/***
	 * 修改用户信息
	 * @Description: TODO
	 * @param @param map
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "userInfo", method = RequestMethod.PUT)
	@ResponseBody
	private String updateUserInfo(@RequestBody Map<String, Object> map) {

		CustomSecurityUser customSecurityUser = ContextUtil.getLoginUser();
		if (customSecurityUser != null) {
			map.put("id", customSecurityUser.getId());
			int flag = userService.updateUserInfo(map);
			if (flag > 0) {
				return AjaxUtil.renderSuccessMsg("操作成功！");
			} else {
				return AjaxUtil.renderFailMsg("操作失败！");
			}
		}
		return AjaxUtil.renderFailMsg("操作失败！用户还没有登录！");
	}
	
	
	/**
	 * 
	* @Description:根据用户显示菜单信息，根据公众号显示菜单信息
	* @param @param appId
	* @param @return   
	* @return String  
	* @throws
	 */
	@RequestMapping(value = "menu", method = RequestMethod.GET)
	@ResponseBody
	public String getMenu(String appId) {
		try {
			CustomSecurityUser securityUser = ContextUtil.getLoginUser();
			if(securityUser != null ){
				boolean isShowMenu = false;
				Map<String, Object> role = userService.getRoleByUserId(securityUser.getId());
				List<String> list  = getAuthoritiesCode(securityUser.getSite());
				if(role != null){
					String roleCode = role.get("roleCode")+"";
					switch (roleCode){
						case Constants.ROLE_QY_ADMIN://企业管理员 
							isShowMenu = true;
							break;
						case Constants.ROLE_PT_CH://平台策划人员
							isShowMenu = true;
							break;
						case Constants.ROLE_PT_SHKF://平台售后客服
							isShowMenu = true ; 
							break;
						case Constants.SUPERROLE://超级管理员
							isShowMenu = true ; 
							break;	
					}
				}else{
					isShowMenu = false ; 
				}
				JSONObject json = readJsonFile();
				JSONArray group =json.getJSONArray("groups");
				Map<String,Object> account =  wxAccountService.getAccountByAppId(appId);
				if(isShowMenu &&appId != null && account != null){
					JSONArray arr = new JSONArray();
					if(Integer.parseInt(account.get("verify_type_info")+"") == -1){//未认证
						for(int i=0;i<group.size();i++){
							JSONObject j = group.getJSONObject(i);
							String key = j.get("key")+"";
							if("tools".equals(key)||"member".equals(key)||"message".equals(key) ||"qr-code".equals(key)){
								arr.add(j);
							}
							if(Integer.parseInt(account.get("bindType")+"")==0){//服务号有菜单权限
								if("menus".equals(key)){
									arr.add(j);
								}
							}
						}
						group.removeAll(arr);
						arr = new JSONArray();
					}else{
						for(int i=0;i<group.size();i++){
							JSONObject j = group.getJSONObject(i);
							String key = j.get("key")+"";
							//如果是已经认证的，需要根据版本号确定是否显示二维码，消息模版等
							if(Integer.parseInt(account.get("bindType")+"")==0 ||!list.contains("qrCode")){//订阅号没有二维码
								if("qr-code".equals(key)){
									arr.add(j);
								}
							}



						}
						group.removeAll(arr);
					}
					json.put("groups", group);
				}else{
					for(int i=0;i<group.size();i++){
						JSONObject j = group.getJSONObject(i);
						String key = j.get("key")+"";
						if("welcome".equals(key)){
							group.removeAll(group);
							group.add(j);
						}
					}
					json.put("groups", group);
				}
				
				return AjaxUtil.renderSuccessMsg(json);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return AjaxUtil.renderFailMsg("获取菜单出错！");
		}
		
		return null;
	}
	
	
	/**
	 * 
	* @Description: 读取Json文件返回字符串信息
	* @param @return   
	* @return String  
	* @throws
	 */
	public JSONObject readJsonFile() {
		ClassPathResource resources = new ClassPathResource(
				"menu-options.json");
		BufferedReader reader = null;
		String laststr = "";
		try {
			InputStream fileInputStream = resources.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return JSONObject.fromObject(laststr) ;
	}
	
	/**
	 * 
	* @Description:取得当前日期之后N天的日期  
	* @param @param n
	* @param @return   
	* @return String  
	* @throws
	 */
	public static String afterNDay(int n){   
		Calendar c=Calendar.getInstance();   
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");   
		c.setTime(new Date());   
		c.add(Calendar.DATE,n);   
		Date d2=c.getTime();   
		String s=df.format(d2);   
		return s;   
		}


	/**
	 * 获取用户权限列表
	 */
	@RequestMapping("getAuthorities")
	public String getAuthorities() {
		Authentication auth = SpringSecurityUtils.getAuthentication();
		Collection<? extends GrantedAuthority> set = null;
		if (auth != null) {
			set = auth.getAuthorities();
		}
		return AjaxUtil.renderSuccessMsg(set);
	}


	List<String>  getAuthoritiesCode(String siteId ){
		List<String> list = new ArrayList<>();
		//Site site =  ftpService.findSite(Long.parseLong(siteId), 7);
	 Map<String,Object> siteGroup = userService.getProjectGroupBySite(siteId);
		if(siteGroup != null){
			//4注册用户组
			//15普通用户组
			//16高级用户组
			//17VIP用户组   VIP才有定时群发 和二维码管理
			String siteGroupId = siteGroup.get("site_group_id")+"";
			String groupStr = "ptyhz";
			if("17".equals(siteGroupId)){
				groupStr = "vipyhz";
				list.add("massJobByTime");
				list.add("qrCode");
			}
			list.add(groupStr);
		}
		if(isHavePhoneProj(siteId)){
			list.add("phoneProj");
		}

		return list;
	}


	/**
	 * 获取用户权限列表(用于前端判断按钮显示权限)
	 */
	@RequestMapping(value = "getAuthoritiesCode", method = RequestMethod.GET)
	@ResponseBody
	public String getAuthoritiesCode() {
		CustomSecurityUser customSecurityUser = ContextUtil.getLoginUser();
		String siteId = customSecurityUser.getSite();
		List<String>   list = getAuthoritiesCode(siteId);
		return AjaxUtil.renderSuccessMsg(list);
	}

	public boolean isHavePhoneProj(String enterpriseId){
		List<Integer> types = new ArrayList<Integer>();
		types.add(5);
		List  projects = userService.getProjectList(enterpriseId, types);
		if(projects.size()>0){
			return true;
		}
		return false;
	}
	
	
	
	/***
	 * 超级后台登录此账号(不用拦截)
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	@RequestMapping(value = "remoteSwitchProject")
	public void remoteSwitchProject(HttpServletRequest request,HttpServletResponse response) {
		
		String username = request.getParameter("username");
		String enterpriseId = request.getParameter("enterpriseId");
		CustomSecurityUser securityUser = (CustomSecurityUser) currentUserDetailsService.loadUserByUsername(username);
		//查询项目的企业
		SecurityContext ctx = SecurityContextHolder.getContext();
		Authentication auth = ctx.getAuthentication();
		if(securityUser == null){
			try {
				throw new Exception("未知的用户！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			securityUser.setSite(enterpriseId);
			securityUser.setProjectType("7");
			PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(
					securityUser, securityUser.getPassword(),
					securityUser.getAuthorities());
			authenticationToken
					.setDetails(new WebAuthenticationDetails(request));
			authenticationToken.setDetails(securityUser);
			ctx.setAuthentication(authenticationToken);
			HttpSession session = request.getSession(true);
			session.setMaxInactiveInterval(Constants.MAX_INACTIVE_INTERVAL);
			session.setAttribute("SPRING_SECURITY_CONTEXT", ctx);
			logger.info(ctx.getAuthentication().getDetails()+"***"+auth.isAuthenticated());
			try {
				response.sendRedirect(request.getContextPath() + Constants.defaultTargetUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}

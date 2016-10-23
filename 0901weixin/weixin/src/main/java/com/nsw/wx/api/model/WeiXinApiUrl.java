package com.nsw.wx.api.model;

public class WeiXinApiUrl {
	//获取access token
	public final static String AccesStoken_Get = "https://101.226.90.58/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//获取微信服务器IP地址
	public final static String Getcallbackip_Get = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";
	//上传多媒体文件
	public final static String UploadMedia_Post = "http://file.api.weixin.qq.com/cgi-bin/media/upload?type=TYPE&access_token=ACCESS_TOKEN";
	//下载多媒体文件
	public final static String DownloadMedia_Get = "http://file.api.weixin.qq.com/cgi-bin/media/get?type=TYPE&access_token=ACCESS_TOKEN";
	/**
	 * 用户管理
	 */
	//获取用户基本信息(包括UnionID机制)
	public final static String GetUserBaseInfo_Get = "https://101.226.90.58/cgi-bin/user/info?openid=OPENID&lang=zh_CN&access_token=ACCESS_TOKEN";
	//获取用户列表
	public final static String GetUserInfoList_Get = "https://api.weixin.qq.com/cgi-bin/user/get?next_openid=NEXT_OPENID&access_token=ACCESS_TOKEN";
	//批量获取用户基本信息
	public final static String BatchgetGetUserInfo_Get = "https://api.weixin.qq.com/cgi-bin/user/info/batchget";
	//设置备注名
	public final static String Remark_Post = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=ACCESS_TOKEN";
	//创建分组
	public final static String CreateGroup_Post = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
	//查询所有分组
	public final static String GetGroup_Get = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
	//查询用户所在分组
	public final static String GetGroupByUser_Post = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";
	//修改分组名
	public final static String EditGroupName_Post = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
	//移动用户分组
	public final static String MoveGroupforUser_Post = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
	//批量移动用户分组
	public final static String MoveGroupforManyUser_Post = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=ACCESS_TOKEN";
	//删除分组
	public final static String DeleteGroup_Post = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=ACCESS_TOKEN";
	
	/**
	 * 标签管理
	 * tag mana
	 */
	//新建标签
	public final static String createTag_Post = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=ACCESS_TOKEN";
	//获取所有标签
	public final static String getTagList_Get = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=ACCESS_TOKEN";
	//编辑标签
	public final static String editTag_Post = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=ACCESS_TOKEN";
	//删除标签
	public final static String delTag_Post = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=ACCESS_TOKEN";
	//获取标签下的粉丝列表
	public final static String getFansByTag_Post = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=ACCESS_TOKEN";
	//批量为用户打标签
	public final static String batchAddTagToFans_Post = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=ACCESS_TOKEN";
	//批量为用户取消标签
	public final static String batchDelTagToFans_Post = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=ACCESS_TOKEN";
	//获取用户身上的标签
	public final static String getUserTags_Post = "https://api.weixin.qq.com/cgi-bin/tags/getidlist?access_token=ACCESS_TOKEN";
	
	
	
	/**
	 * 公众号管理
	 */
	//创建公共菜单
	public final static String MenuCreate_Post = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	//获取公共号菜单
	public final static String Getmenu_Get = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	//删除公众号菜单
	public final static String DeleteMenu_Get = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	//创建二维码ticket
	public final static String CreateGrcode_Post = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
	//长链接转短链接接口
	public final static String ShorturlCreate_Post = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
	
	
	/**
	 * 模板管理
	 */
	//获得模板id
	public final static String GetTempletID_Post = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
	//发送模板消息
	public final static String SendTempletMsg_Post = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	//设置所属行业
	public final static String SetIndustry_Post = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
	//获取所属行业
	public final static String GetIndustry_Get = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";
	//获取模板列表
	public final static String GetAllTemplate_Get = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";
	//删除模板
	public final static String DelTemplate_Post = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=ACCESS_TOKEN";




	
	/**
	 * 素材管理
	 */
	//新增临时素材 
	public final static String AddTempmatter_Post = "https://api.weixin.qq.com/cgi-bin/media/upload?type=TYPE&access_token=ACCESS_TOKEN";
	//获取临时素材 
	public final static String GetTempmatter_Get = "https://api.weixin.qq.com/cgi-bin/media/get?media_id=MEDIA_ID&access_token=ACCESS_TOKEN";
	//新增永久素材 
	public final static String AddMatter_Post = "https://api.weixin.qq.com/cgi-bin/material/add_material?type=TYPE&access_token=ACCESS_TOKEN";
	//获取永久素材 
	public final static String GetMatter_Post = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN";
	//删除永久素材 
	public final static String DelMatter_Post = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=ACCESS_TOKEN";
	//修改永久图文素材
	public final static String UpdateMatter_Post = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=ACCESS_TOKEN";
    //上传永久图文素材
	public final static String AddNews_Post = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";
	//获取素材总数
	public final static String GetMatterCount_Get = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=ACCESS_TOKEN";
	//获取素材列表
	public final static String GetMatterList_Post = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN"; 

	//上传logo接口
	public final static String UploadLogo_Post = "http://api.weixin.qq.com/cgi-bin/media/uploadimg?type=image&access_token=ACCESS_TOKEN";
	
	
	/**
	 * 消息群发
	 */
	
	//上传图文消息素材
	public final static String uploadNews_Post  ="https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";
	//根据分组进行群发
	public final static String sendAll_Post =  "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
	//删除群发 
	public final static String deleteMass_Post = "https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=ACCESS_TOKEN";
	//查询群发发送状态
	public final static String queryMassState_Post = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=ACCESS_TOKEN";
	//群发预览接口
	public final static String preViewMass_Post = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN";
	
	
	
	/**
	 * 数据统计接口
	 */
	/**
	 * 用户分析数据接口
	 */
	//获取用户增减数据
	public final static String Getusersummary_Post ="https://api.weixin.qq.com/datacube/getusersummary?access_token=ACCESS_TOKEN";
	//获取累计用户数据
	public final static String Getusercumulate_Post ="https://api.weixin.qq.com/datacube/getusercumulate?access_token=ACCESS_TOKEN";

	/**
	 * 图文分析数据接口
	 */
	//获取图文群发每日数据
	public final static String Getarticlesummary_Post = "https://api.weixin.qq.com/datacube/getarticlesummary?access_token=ACCESS_TOKEN";
	//获取图文群发总数据
	public final static String Getarticletotal_Post = "https://api.weixin.qq.com/datacube/getarticletotal?access_token=ACCESS_TOKEN";
	//获取图文统计数据
	public final static String Getuserread_Post ="https://api.weixin.qq.com/datacube/getuserread?access_token=ACCESS_TOKEN";
	//获取图文统计分时数据
	public final static String Getuserreadhour_Post ="https://api.weixin.qq.com/datacube/getuserreadhour?access_token=ACCESS_TOKEN";
	//获取图文分享转发数据
	public final static String Getusershare_Post ="https://api.weixin.qq.com/datacube/getusershare?access_token=ACCESS_TOKEN";
	//获取图文分享转发分时数据
	public final static String Getusersharehour_Post ="https://api.weixin.qq.com/datacube/getusersharehour?access_token=ACCESS_TOKEN";

	/**
	 * 消息分析数据接口
	 */
	//获取消息发送概况数据
	public final static String Getupstreammsg_Post = "https://api.weixin.qq.com/datacube/getupstreammsg?access_token=ACCESS_TOKEN";
	//获取消息分送分时数据
	public final static String Getupstreammsghour_Post = "https://api.weixin.qq.com/datacube/getupstreammsghour?access_token=ACCESS_TOKEN";
	//获取消息发送周数据
	public final static String Getupstreammsgweek_Post = "https://api.weixin.qq.com/datacube/getupstreammsgweek?access_token=ACCESS_TOKEN";
	//获取消息发送月数据
	public final static String Getupstreammsgmonth_Post = "https://api.weixin.qq.com/datacube/getupstreammsgmonth?access_token=ACCESS_TOKEN";
	//获取消息发送分布数据
	public final static String Getupstreammsgdist_Post = "https://api.weixin.qq.com/datacube/getupstreammsgdist?access_token=ACCESS_TOKEN";
	//获取消息发送分布周数据
	public final static String Getupstreammsgdistweek_Post ="https://api.weixin.qq.com/datacube/getupstreammsgdistweek?access_token=ACCESS_TOKEN";
	//获取消息发送分布月数据
	public final static String Getupstreammsgdistmonth_Post = "https://api.weixin.qq.com/datacube/getupstreammsgdistmonth?access_token=ACCESS_TOKEN";

	
	/**
	 * 接口分析数据接口
	 */
	//获取接口分析数据
	public final static String Getinterfacesummary_Post = "https://api.weixin.qq.com/datacube/getinterfacesummary?access_token=ACCESS_TOKEN";
	//获取接口分析分时数据
	public final static String Getinterfacesummaryhour_Post ="https://api.weixin.qq.com/datacube/getinterfacesummaryhour?access_token=ACCESS_TOKEN";

	
	/**
	 * 客服接口
	 */
	//添加客服账号
	public final static String Addkf_Post = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN";
	//修改客服账号
	public final static String Editkf_Post = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=ACCESS_TOKEN";
	//删除客服账号
	public final static String Delkf_Post = "https://api.weixin.qq.com/customservice/kfaccount/del?access_token=ACCESS_TOKEN";
	//获取所有客服账号
	public final static String GetKfList_Get = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=ACCESS_TOKEN";
	//客服接口发消息
	public final static String KfSend_Post = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	
	
	
	/**
	 * 授权接口
	 */
	public final static String getTokenUrl ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public final static String getCodeUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	public final static String getOAuthAccessToken="https://101.226.90.58/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	public final static String getreferAccessUrl="https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	public final static String getOAuthUserNews="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	public final static String isOAuthAccessToken="https://api.weixin.qq.com/sns/auth?openid=OPENID&access_token=ACCESS_TOKEN";


	/**
	 * 第三方平台接口
	 */
	//获取第三方平台component_access_token
	//第三方平台compoment_access_token是第三方平台的下文中接口的调用凭据，也叫做令牌（component_access_token）。
	//每个令牌是存在有效期（2小时）的，且令牌的调用不是无限制的，请第三方平台做好令牌的管理，在令牌快过期时（比如1小时50分）再进行刷新。
	public final static String component_access_token_Post = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
	
	
	//3、获取预授权码pre_auth_code
	//该API用于获取预授权码。预授权码用于公众号授权时的第三方平台方安全验证。
	public final static String pre_auth_code_Post = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
	
	
	
	
	//、使用授权码换取公众号的接口调用凭据和授权信息
	//该API用于使用授权码换取授权公众号的授权信息，并换取authorizer_access_token和authorizer_refresh_token。 授权码的获取，需要在用户在第三方平台授权页中完成授权流程后，在回调URI中通过URL参数提供给第三方平台方
	public final static String api_query_auth_Post = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=COMPONENT_ACCESS_TOKEN";
	
	
	//获取授权方的公众号帐号基本信息
	public final static String api_get_authorizer_info_Post = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";
	
	
	
	
	
	

}

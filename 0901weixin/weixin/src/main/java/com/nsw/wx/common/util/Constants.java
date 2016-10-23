package com.nsw.wx.common.util;


/**
 * @Description 常量工具类
 * @author  lzhen
 * @date 创建时间：2015年6月12日 下午4:59:34
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public interface Constants {
	
	
	//搜索主体版块的动态标记
	public static final String SEARCH_CONTENT_DYNA_START = "<!--SEARCH";
	public static final String SEARCH_CONTENT_DYNA_END = "SEARCH-->";
	public static final String ITEMS = "items";
	public static final String DETAIL = "detail";
	public static final String CTG = "ctg";
	public static final String MODULE = "module";
	
	//公有项目ID
	public static final String PUB_PC_PROJ_ID = "26308_PC";
	public static final String PUB_MB_PROJ_ID = "26373_PC";
	public static final String PUB_RS_PROJ_ID = "26501_PC";
	public static final String PC_PROJ_TYPE = "4";
	public static final String MB_PROJ_TYPE = "5";
	public static final String RS_PROJ_TYPE = "9";
	//产品列表
	public static final String PRODUCT = "PRODUCT";
	
	//主导航
	public static final String MAIN_NAV = "MAIN_NAV";
	//副导航
	public static final String LEFT_NAV = "LEFT_NAV";
	//底部导航
	public static final String BOTTOM_NAV = "BOTTOM_NAV";
	//广告
	public static final String AD = "AD";
	//资讯列表
	public static final String INFO = "INFO";
	//标签
	public static final String TAG = "TAG";
	//链接
	public static final String LINK = "LINK";
	//搜索
	public static final String SEARCH = "SEARCH";
	public static final String SEARCH_CONTENT = "SEARCH_CONTENT";
	//产品详情
	public static final String PROD_DETAIL = "PROD_CONTENT";
	//产品详情橱窗
	public static final String PROD_SHOWCASE = "PROD_SHOWCASE";
	//文章详情
	public static final String INFO_DETAIL = "INFO_CONTENT";
	//产品详情
	public static final String PROD_LIST_CONTENT = "PROD_LIST_CONTENT";
	//文章详情
	public static final String INFO_LIST_CONTENT = "INFO_LIST_CONTENT";
	//直通车
	public static final String SHOP = "SHOP_NAV";
	//头部
	public static final String HEADER = "HEADER";
	//底部
	public static final String FOOTER = "FOOTER";
	//正则或
	public static final String REGEX_OR = "|";
	
	//文件上传，流上传-业务逻辑分割点
		public static int BUSINESS_START_PERCENT = 90;
	
	   //zip文件上传任务组                    
		public static String TASKTIME_GROUPFILEUPLOAD ="GROUPFILEUPLOAD";
	
	//zip文件上传任务组
		public static String TASKTIME_FILEUPLOAD="FILEUPLOAD";
		
		 //zip文件上传触发器
		public static String TASKTIME_TRIGGERFILEUPLOAD="TRIGGERFILEUPLOAD";
	
	// 整数1000
		public static final int INT_ONETHOUSAND = 1000;
		
		  //可视化编辑任务组
		public static String TASKTIME_GROUPSMARTUNLOCK ="GROUPSMARTUNLOCK";
		
	//可视化编辑任务组
		public static String TASKTIME_SMARTUNLOCK="SMARTUNLOCK";
	
	//整数60
	public static final int INT_SIXTY = 60;
	
    //可视化编辑触发器
	public static String TASKTIME_TIRGGERSMARTUNLOCK ="TRIGGERSMARTUNLOCK";
	
	//contentType-文件类型
	public static final String FILE = "file";
	//contentType-ZIP
	public static final String FType = ".zip";
	//contentType-图片
	public static final String IMAGE = "image";
	//contentType-html
	public static final String T_HTML = "text/html";
	//contentType -css
	public static final String T_CSS = "text/css";
	//contentType-图片
	public static final String T_GIF = "image/gif";
	//contentType-图片
	public static final String T_JPEG = "image/jpeg";
	//contentType-图片
	public static final String T_PNG = "image/png";
	//小写utf-8
	public static final String LOWER_UTF8 = "utf-8";
	//大写utf-8
	public static final String UPPER_UTF8 = "UTF-8";
	//大写gbk
	public static final String UPPER_GBK = "GBK";
	//大写iso-8859-1
	public static final String UPPER_ISO8859 = "ISO-8859-1";
	//contentType-页面
	public static final String PAGE = "page";
	//source-原始版本
	public static final String ORIGINAL = "original";
	//source-当前版本
	public static final String NOW = "now";
	//source-备份版本
	public static final String BACKUP = "backup";
	//source-拷贝版本
	public static final String COPY = "copy";
	//source-拷贝版本
	public static final String COPY_CHINA = "复制";
	//中划线
	public static final String LINETHROUGH = "-";
	//冒号
	public static final String COLON = ":";
	//下划线Underline
	public static final String UNDERLINE = "_";
	//反斜杠
	public static final String LEFT_SPRIT = "/";
	//双反斜杠
	public static final String DOUBLE_LEFT_SPRIT = "//";
	//下划线Underline
	public static final String RIGHT_SPRIT = "\\";
	//句号
	public static final String POINT = ".";
	//分号
	public static final String SEMICOLON = ";";
	//逗号
	public static final String COMMA = ",";
	//字符0
	public static final String STR_ZERO = "0";
	//字符1
	public static final String STR_ONE = "1";
	//字符2
	public static final String STR_TWO = "2";
	//字符3
	public static final String STR_THREE = "3";
	//字符4
	public static final String STR_FOUR = "4";
	//字符5
	public static final String STR_FIVE = "5";
	//字符6
	public static final String STR_SIX = "6";
	//字符7
	public static final String STR_SEVEN = "7";
	//字符8
	public static final String STR_EIGHT = "8";
	//字符9
	public static final String STR_NINE = "9";
	//字符10
	public static final String STR_TEN = "10";
	//项目名称序列号
	public static final String SERIAL = "serial";
	//整数0
	public static final int INT_ZERO = 0;
	//整数1
	public static final int INT_ONE = 1;
	//整数2
	public static final int INT_TWO = 2;
	//整数3
	public static final int INT_THREE = 3;
	//整数4
	public static final int INT_FOUR = 4;
	//整数5
	public static final int INT_FIVE = 5;
	//整数6
	public static final int INT_SIX = 6;
	//整数7
	public static final int INT_SEVEN = 7;
	//整数8
	public static final int INT_EIGHT = 8;
	//整数9
	public static final int INT_NINE = 9;
	//整数10
	public static final int INT_TEN = 10;
	//整数30
	public static final int INT_THIRTY = 30;
	//整数100
	public static final int INT_HUNDRED = 100;
	//负数-1
	public static final int INT_NEGATIVE_ONE = -1;
	//ftp连接成功返回码
	public static final Integer SUCCESSCODE  = 230;
	//命令操作成功返回码
	public static final int COMMAND_OK = 200;
	//0内部用户
	public final static Integer USER_INTERNALUSER_TYPE = 0; 
	//1企业用户
	public final static Integer USER_ENTERPRISE_TYPE = 1;    
	//Long 1
	public final static Long LONG_ONE = 1l;
	//超级管理员角色
	public final static long SUPERADMIN = 176;
	//做单角色
	public final static long DOORDER = 263;
	//开户角色
	public final static long OPENACCOUNT = 262;
	//做单管理
	public final static long DOORDERMANAGEMENT = 260;
	//开户管理
	public final static long OPENACCOUNTMANAGEMENT = 261;
	//快效系统类型
	public final static int KXXT_TYPE = 8;
	//可用状态
	public final static String USER_STATUS_USABLE = "1";   
	//不可用
	public final static String USER_STATUS_DISABLED = "0";  
	//用户回收
	public final static String USER_STATUS_ISDEL = "3";
	//成功
	public final static String SUCCESS = "Success";
	//失败
	public final static String FAIL = "Fail";
	//成功
	public final static String success = "success";
	//失败
	public final static String fail = "fail";
	//存在
	public final static String exist = "exist";
	//错误
	public final static String error = "error";
	//禁用
	public final static String disabled = "disabled";
	//关闭
	public final static String close = "close";
	//左划线P
	public final static String  SLASH_P = "/P";
	//P
	public final static String  P = "P";
	//网站首页
	public final static String  INDEX_HTML = "index.html";
	//验证码
	public final static String  VALIDATE_CODE = "validateCode";
	//首页验证码
	public final static String  INDEX_VALIDATE_CODE = "indexValidateCode";
	//user用户获取session中的用户
	//public final static String  USER = ContextUtil.USER_SESSION_KEY;
	//login.html
	public final static String  LOGIN_HTML = "login.html";
	//oldpassword旧密码
	public final static String  OLD_PASSWORD = "oldpassword";
	//newpassword新密码
	public final static String  NEW_PASSWORD = "newpassword";
	//inputCode
	public final static String  INPUT_CODE = "inputCode";
	//main
	public final static String  MAIN = "main";
	//1024代表bit进制
	public final static int  BIT_SIZE = 1024;
	//日期格式化
	public final static String  DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	//pccms总配置信息表名
	public final static String T_CMS_CONFIG = "CmsConfig";
	//资讯文章表名
	public final static String T_INFO_ARTICLE = "InfoArticle";
	//资讯分类表名
	public final static String T_INFO_CTG = "InfoCtg";
	//项目基本信息表名
	public final static String T_PROJECT = "Project";
	//公用页面模板表名
	public final static String T_PUB_PAGE_TPL= "PubPageTpl";
	//公用板块模板表名
	public final static String T_PUB_BLK_TPL= "PubBlkTpl";
	//公用页面套餐模板表名
	public final static String T_PUB_PAGE_TPL_PACK= "PubPageTplPack";
	//项目单页面(嵌套板块模板)表名
	public final static String T_PROJ_PAGE_TPL= "ProjPageTpl";
	//项目板块表名
	public final static String T_PROJ_BLK_TPL= "ProjBlkTpl";
	//项目页面套餐表名
	public final static String T_PROJ_PAGE_TPL_PACK= "ProjPageTplPack";
	//标签表名
	public final static String T_TAG = "Tag";
	//数据源表名
	public final static String T_TPL_DS = "TplDs";
	//表单配置基础表名
	public final static String T_FORM_CONFIG = "FormConfig";
	
	//网站地图表名
	public final static String T_Site_Map = "siteMap";
	
	//表单项目配置表名
	public final static String T_PROJ_FORM = "ProjForm";
	
	//产品表名
	public final static String T_PRODUCT = "Product";
	//产品分类表名
	public final static String T_PRODUCT_CTG = "ProductCtg";
	
	//项目模块表名
	public final static String T_MOUDUEL_CONFIG = "ModuleConfig";
	//项目模块表名
	public final static String T_PROJ_MOUDUEL = "ProjModule";
	
	//回收站
	public final static String T_PUB_RECYLEBIN = "RecyleBin";
	
	
	//文件库表
	
	public final static String T_RESOURCES = "Resources";
	
	
	//项目标签
	public final static String T_PROJ_TAG = "ProjTag";
	//项目分类
	public final static String T_PROJ_TAG_Ctg = "ProjTagCtg";
	//在线客服
	public final static String T_ONLINE_SRV="OnlineSrv";
	//类型配置表
	public final static String T_PROJ_CONFIG="ProjConfig";
	
	//意向订单表名称
	public final static String T_INTENTIONALORDER = "IntentionalOrder";
	
	//意向订单表单
	public final static String T_INTENTIONALORDERFORM = "IntentionalOrderForm";
	
	//意向订单字段表
	public final static String T_FORMFIELD = "FormField";
	
	//意向订单留言分类
	public final static String MESSAGECLASSIFICATION = "MessageClassification";
	
	public static final String EXTEND = "EXTEND";
	    
	public static final String PROD = "PROD";
	    
	public static final String OTHERS = "OTHERS";
	
	//编辑器模板
	public final static String T_EditorTpl = "EditorTpl";
	
//	public static String ftpUserName ="hgw072139";
//	public static String ftpPassword ="vchdd123";
//	public static String ftpIpAddress ="121.198.156.139";
//	public static String url ="hgw072139.my3w.com";
	/**
	 * 列表页导出时总页数key
	 */
	public static final String PAGES = "pages";
	/**
	 * 数据源的key
	 */
	public static final String TPLDSDATA = "data";
	public static final String PREVDETAIL = "prev";
	public static final String NEXTDETAIL = "next";
	
	public static final String TARGET_BLANK = "_BLANK";
	
	
	public static final String TARGET_SELF = "_SELF";
	
	public static final String TARGET_PARENT = "_PARENT";
	
	public static final String TARGET_TOP = "_TOP";
	
	public static final String DEFAULT = "default";
	
	public static String ftpUserName ="hgw072139";
	public static String ftpPassword ="vchdd123";
	public static String ftpIpAddress ="121.198.156.139";
	public static String url ="hgw072139.my3w.com";
	//在线客服部门分类
	public static String ONLINESRV_DEPT="onlineSrv_dept";
	//广告管理
	public static String T_ADVERTISEMENT = "Advertisement";
	//关键词表
	public final static String T_KEYWORDS="Keywords";
	
	//全局配置seo
	public static String PROJCONFIG_TYPE_DEFAULTSEO = "DefaultSeo";
	//全局配置皮肤
	public static String PROJCONFIG_TYPE_SKINS = "SKINS";
	//图片转换配置
	public static String PROJCONFIG_TYPE_IMGTRAN = "imgTransfer";
	
	
	/**
	 * 留言表单（系统）
	 */
	public final static String LY_FORM_NAME="留言表单（系统）";
	
	/**
	 * 招商加盟表单（系统）
	 */
	public final static String ZS_FORM_NAME="招商加盟表单（系统）";
	
	/**
	 * 详细页询盘表单（系统）
	 */
	public final static String XXY_FORM_NAME="详细页询盘表单（系统）";
	
	/**
	 * 初始化表单或动态字段文件名
	 */
	public final static String INITFORMORFIELDSDATA = "initFormOrFieldsData";
	/**
	 * 默认字段key值
	 */
	public final static String DEFAULTFIELDS = "defaultFields";
	
	/**
	 * 访客订单名称
	 */
	public final static String VCENTER_ORDERNAME="访客订单";
	//友情链接
	public final static String T_FRIENDLYLINK="FriendlyLink";
	
	
	
	/**
	 * 角色  企业管理员
	 */
	
	public final static String ROLE_QY_ADMIN = "Role_qy_admin";
	
	public final static int ROLE_QY_ADMIN_ID = 270;
	
	/**
	 * 角色  企业编辑推广
	 */
	public final static String ROLE_QY_BJTG = "Role_qy_bjtg";
	
	public final static int ROLE_QY_BJTG_ID = 271;
	
	/**
	 * 角色  企业销售客服
	 */
	public final static String ROLE_QY_XSKF = "Role_qy_xskf";
	
	public final static int ROLE_QY_XSKF_ID = 272;
	
	/**
	 * 角色  平台售后客服
	 */
	public final static String ROLE_PT_SHKF = "Role_pt_shkf";
	
	public final static int ROLE_PT_SHKF_ID = 266;
	
	/**
	 * 角色  平台策划-
	 */
	public final static String ROLE_PT_CH = "Role_pt_ch";
	
	public final static int ROLE_PT_CH_ID = 264;
	
	/**
	 * 角色  平台编辑
	 */
	public final static String ROLE_PT_BJ = "Role_pt_bj";
	
	public final static int ROLE_PT_BJ_ID = 268;
	/**
	 * 角色  平台裁切
	 */
	public final static String ROLE_PT_CQ = "Role_pt_cq";
	
	public final static int ROLE_PT_CQ_ID = 265;
	
	/**
	 * 角色  模板设计小组
	 */
	public final static String ROLE_MBSJ = "Role_mbsj";
	
	public final static int ROLE_MBSJ_ID = 269;

	
	//全权限用户
	public final static String  SUPERROLE = "superRole";
	
	
	public final static String INFOMENU = "/temp/info/index.html";
	
	public final static String PRODUCTMENU = "/temp/product/index.html";
	
	
	public final static String MENU = "Menu";
	
	public final static String MENUROLE = "Menu-Role";
	
	public final static String AUTHORITY = "Authority";
	
	public final static String FILE_DIR = "/resource/images/";
	
	public final static String FILE_DIR_WX = "resource/images/wx/";
	
	
	public final static String defaultTargetUrl ="/app/index.html#/wechat/account";
	
	public final static int MAX_INACTIVE_INTERVAL = 7200;//当前会话的失效时(单位秒)
	
	public final static String ERRCODE = "errcode";
	public final static String UTFCODE = "utf-8";
	public final static String ACCESSTOKEN = "ACCESS_TOKEN";
	
	//公共账户表账号
	public static String WXACCOUNT_T = "weixin_account";
	//微信素材图片表
	public static String WXIMAGE_T = "weixin_image";
	//微信素材图文表	
	public static String WXNEWS_T = "weixin_news";
	//微信粉丝组表
	public static String WXFANSGROUP_T = "weixin_fansGroup";
	//微信粉丝表
	public static String WXFANS_T = "weixin_fans";
	//关键词回复
	public static String WXKEYWORD_T = "weixin_keyword";
	//二维码表
	public static String WXQRCODE_T = "weixin_qrCode";
	
	
	//群发记录表
	public static String WXMASSMSG_T = "weixin_massmsg";
	
	public static String WXMASSLOG_T = "weixin_masslog";
	//微信信息表
	public static String WXMESSAGE_T = "weixin_message";
	//粉丝标签表
	public static String WXFANSTAG_T = "weixin_fanstag";

	//模板消息表
	public static String WXTEMPLATEMSG_T = "weixin_templatemsg";

	
	
	//public static String WXSYNCMATERIAL = "weixin_syncmaterial";
	
	
	//微信菜单表
	public static String WXMENU_T = "weixin_menu";
	
	public static String TASK_BINDGACCOUNTROUP = "TASK_REFRESHACCOUNTROUP";

	
	//发布定时触发器
	public static String TASKTIME_TRIGGER ="TRIGGER";
	
	//发布定时任务组
	public static String TASKTIME_GROUP = "GROUP";
	
	
	public  static String cmsDefaultTargetUrl ="/js/personal/index.html#/personalInfo";
	

    
}

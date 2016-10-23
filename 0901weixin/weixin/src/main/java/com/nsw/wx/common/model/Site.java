package com.nsw.wx.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.nsw.wx.common.util.Constants;
/**
 * 
* @author Eason
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月17日 下午4:36:40
* @Description: 站点表实体
 */
@JsonFilter("site")
@Entity
@Table(name = "site")
public class Site implements Serializable{
	/**
	* @Fields serialVersionUID : TODO
	*/
	
	private static final long serialVersionUID = 726644000188488348L;
	//站点id
	private long id; 
	//正式售后时间
	private Date afterSalesStartTime;
	//售后 状态
	private String afterSalesStatus; 
	//文章列表页缩略图规格(宽)
	private Integer articleListPicWidthMobile = 130; 
	//文章列表页缩略图规格(高)
	private Integer articleListPicHeightMobile = 130;                         
	private String attDomain;
	private String attIPAddress;
	private String attAddress;
	private String attUserName;
	private String attPassword;
	//网站附件端口
	private String attPort = "21";                                                            
	private String attUploadDirectory = Constants.LEFT_SPRIT;  
	//att上传模式
	private Integer attUploadMode;                                               
	private Integer contentDetailedReducedPicHeightMobile;
	//内容详情（编辑器）图片压缩宽度
	private Integer contentDetailedReducedPicWidthMobile = 280;  
	//使用时长 
	private Float duration;
	//编辑ID
	private Long editorId;  
    //PCEmail数
	private Integer emailNumPC = 100;     
	//备案号
	private String fileNumber;    
	//备案状态
	private String fileStatus;    
	//网站正式域名
	private String formalDomain;
	//ftp服务器IP地址
	private String ftpIPAddress;
	//ftp密码
	private String ftpPassword;
	 //FTP端口
	private String ftpPort ="21"; 
	//ftp上传目录
	private String ftpUploadDirectory = Constants.LEFT_SPRIT; 
	//ftp上传模式
	private Integer ftpUploadMode;
	//ftp用户名
	private String ftpUserName;
	//空间大小
	private Integer indexPageSpaceSize = 50;
	//登陆页个数
	private Integer indexPageNum = 1;
	//制作开始时间
	private Date manufactureTime = new Date(); 
	//制作公司
	private String manufacturingCompany;
	//移动推广页数
	private Integer mobilePopularizePageNum; 
	//移动推广空间大小
	private Integer mobilePopularizeSpace; 
	//是否上线
	private String onlineStatus;
	//上线时间
	private Date onlineTime; 
	 //开通时间
	private Date openupTime;                                                     
	private String picAddress;
	private String picIPAddress;
	private Integer picDomain;
	private String picPassword;
	//网站图床端口
	private String picPort="21";                                                          
	private String picUploadDirectory=Constants.LEFT_SPRIT;
	//pic上传模式
	private Integer picUploadMode; 
	//pic用户名
	private String picUserName;
    //策划
	private String planId; 
	//美工
    private String artId;  
    //产品详情页轮看图片规格(宽)
	private Integer productDetailedPicWidthMobile=240; 
	//产品详情页轮看图片规格(高)
	private Integer productDetailedPicHeightMobile=240;
	//产品列表页缩略图规格(宽)
	private Integer productPicWidthMobile=130;
	//产品列表页缩略图规格(高)
	private Integer productPicHeightMobile=130; 
	//程序
	private Long programerId; 
	//质检
	private String QCId; 
	//pc赠送短信数量
	private Integer smsPC = 10;
	//分站个数
	private Integer subSiteMobile = 0; 
	//PC分站数量 
	private Integer subSitePC = 0; 
	//PC设计页面个数
	private Integer pagesPC = 10; 
	//裁切
	private String tailorId;
	//培训
	private String trainingId;                                                         
	//消息发送数量
	private Long wechatMessageNum = 50000L; 
	//微信公众号个数 
	private Integer wechatPublicAccountNum = 8;                           
	//允许总容量 -移动推广
	private Integer popularAllowAmount = 500;
	//活动数量-移动推广
	private Integer popularActivityNum = 20;
	//每个活动内页数量-移动推广
	private Integer popularPagePerActivity = 10;
	// 关联站点组 多对一
	private SiteGroup siteGroup; 
	//关联会员 多对一
    private Enterprise enterprise; 
    //站点类型
    private Integer type;
    //PC数据同步到手机
    private String pcSyncMobile = "1";
    //编辑人名字
    private String editorName;
    //程序人名字
    private String programerName;
    
    private String ftpdir;//说明是phone还是其他， 发布目录ftpUploadDirectory/ftpdir  访问域名 formalDomain + ftpdir
	
    @Column(name = "ftp_dir")
	public String getFtpdir() {
		return ftpdir;
	}

	public void setFtpdir(String ftpdir) {
		this.ftpdir = ftpdir;
	}

	@ManyToOne(cascade = {CascadeType.MERGE})           
    @JoinColumn(name = "site_group_id")     
	public SiteGroup getSiteGroup() {
		return siteGroup;
	}
	
	public void setSiteGroup(SiteGroup siteGroup) {
		this.siteGroup = siteGroup;
	}
	@JsonBackReference
	@ManyToOne(cascade = {CascadeType.MERGE})           
    @JoinColumn(name = "enterprise_id")
	public Enterprise getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}
	public Site(){

	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	@Column(name = "type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name = "pc_sync_mobile")
	public String getPcSyncMobile() {
		return pcSyncMobile;
	}
	public void setPcSyncMobile(String pcSyncMobile) {
		this.pcSyncMobile = pcSyncMobile;
	}
	@Column(name = "after_sales_start_time")
	public Date getAfterSalesStartTime() {
		return afterSalesStartTime;
	}

	public void setAfterSalesStartTime(Date afterSalesStartTime) {
		this.afterSalesStartTime = afterSalesStartTime;
	}
	@Column(name = "after_sales_status")
	public String getAfterSalesStatus() {
		return afterSalesStatus;
	}

	public void setAfterSalesStatus(String afterSalesStatus) {
		this.afterSalesStatus = afterSalesStatus;
	}
	
	@Column(name = "article_list_pic_width_mobile")
	public Integer getArticleListPicWidthMobile() {
		return articleListPicWidthMobile;
	}
	public void setArticleListPicWidthMobile(Integer articleListPicWidthMobile) {
		this.articleListPicWidthMobile = articleListPicWidthMobile;
	}
	@Column(name = "article_list_pic_height_mobile")
	public Integer getArticleListPicHeightMobile() {
		return articleListPicHeightMobile;
	}
	public void setArticleListPicHeightMobile(Integer articleListPicHeightMobile) {
		this.articleListPicHeightMobile = articleListPicHeightMobile;
	}
	@Column(name = "att_domain")
	public String getAttDomain() {
		return attDomain;
	}

	public void setAttDomain(String attDomain) {
		this.attDomain = attDomain;
	}
	@Column(name = "att_ip_address")
	public String getAttIPAddress() {
		return attIPAddress;
	}

	public void setAttIPAddress(String attIPAddress) {
		this.attIPAddress = attIPAddress;
	}
	@Column(name = "att_address")
	public String getAttAddress() {
		return attAddress;
	}
	public void setAttAddress(String attAddress) {
		this.attAddress = attAddress;
	}
	
	@Column(name = "att_password")
	public String getAttPassword() {
		return attPassword;
	}

	public void setAttPassword(String attPassword) {
		this.attPassword = attPassword;
	}
	@Column(name = "att_user_name")
	public String getAttUserName() {
		return attUserName;
	}
	public void setAttUserName(String attUserName) {
		this.attUserName = attUserName;
	}
	@Column(name = "att_port")
	public String getAttPort() {
		return attPort;
	}

	public void setAttPort(String attPort) {
		this.attPort = attPort;
	}
	@Column(name = "att_upload_directory")
	public String getAttUploadDirectory() {
		return attUploadDirectory;
	}

	public void setAttUploadDirectory(String attUploadDirectory) {
		this.attUploadDirectory = attUploadDirectory;
	}
	@Column(name = "att_upload_mode")
	public Integer getAttUploadMode() {
		return attUploadMode;
	}

	public void setAttUploadMode(Integer attUploadMode) {
		this.attUploadMode = attUploadMode;
	}

	@Column(name = "content_pic_height_mobile")
	public Integer getContentDetailedReducedPicHeightMobile() {
		return contentDetailedReducedPicHeightMobile;
	}

	public void setContentDetailedReducedPicHeightMobile(
			Integer contentDetailedReducedPicHeightMobile) {
		this.contentDetailedReducedPicHeightMobile = contentDetailedReducedPicHeightMobile;
	}
	@Column(name = "content_pic_width_mobile")
	public Integer getContentDetailedReducedPicWidthMobile() {
		return contentDetailedReducedPicWidthMobile;
	}

	public void setContentDetailedReducedPicWidthMobile(
			Integer contentDetailedReducedPicWidthMobile) {
		this.contentDetailedReducedPicWidthMobile = contentDetailedReducedPicWidthMobile;
	}
	@Column(name = "duration")
	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}
	@Column(name = "editor_id")
	public Long getEditorId() {
		return editorId;
	}

	public void setEditorId(Long editorId) {
		this.editorId = editorId;
	}
	@Column(name = "file_number")
	public String getFileNumber() {
		return fileNumber;
	}
	@Column(name = "email_num_pc")
	public Integer getEmailNumPC() {
		return emailNumPC;
	}
	public void setEmailNumPC(Integer emailNumPC) {
		this.emailNumPC = emailNumPC;
	}
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}
	@Column(name = "file_status")
	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	@Column(name = "formal_domain")
	public String getFormalDomain() {
		return formalDomain;
	}

	public void setFormalDomain(String formalDomain) {
		this.formalDomain = formalDomain;
	}
	@Column(name = "ftp_ip_address")
	public String getFtpIPAddress() {
		return ftpIPAddress;
	}

	public void setFtpIPAddress(String ftpIPAddress) {
		this.ftpIPAddress = ftpIPAddress;
	}
	@Column(name = "ftp_password")
	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	@Column(name = "ftp_port")
	public String getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}
	@Column(name = "ftp_upload_directory")
	public String getFtpUploadDirectory() {
		return ftpUploadDirectory;
	}

	public void setFtpUploadDirectory(String ftpUploadDirectory) {
		this.ftpUploadDirectory = ftpUploadDirectory;
	}
	@Column(name = "ftp_upload_mode")
	public Integer getFtpUploadMode() {
		return ftpUploadMode;
	}

	public void setFtpUploadMode(Integer ftpUploadMode) {
		this.ftpUploadMode = ftpUploadMode;
	}
	@Column(name = "ftp_user_name")
	public String getFtpUserName() {
		return ftpUserName;
	}

	public void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}
	
	@Column(name = "index_space_size")
	public Integer getIndexPageSpaceSize() {
		return indexPageSpaceSize;
	}

	public void setIndexPageSpaceSize(Integer indexPageSpaceSize) {
		this.indexPageSpaceSize = indexPageSpaceSize;
	}
	@Column(name = "index_page_num")
	public Integer getIndexPageNum() {
		return indexPageNum;
	}
	public void setIndexPageNum(Integer indexPageNum) {
		this.indexPageNum = indexPageNum;
	}
	@Column(name = "manufacture_time")
	public Date getManufactureTime() {
		return manufactureTime;
	}

	public void setManufactureTime(Date manufactureTime) {
		this.manufactureTime = manufactureTime;
	}
	@Column(name = "manufacturing_company")
	public String getManufacturingCompany() {
		return manufacturingCompany;
	}

	public void setManufacturingCompany(String manufacturingCompany) {
		this.manufacturingCompany = manufacturingCompany;
	}                    
	@Column(name = "mobile_popula_page_num")
	public Integer getMobilePopularizePageNum() {
		return mobilePopularizePageNum;
	}

	public void setMobilePopularizePageNum(Integer mobilePopularizePageNum) {
		this.mobilePopularizePageNum = mobilePopularizePageNum;
	}
	@Column(name = "mobile_popular_space")
	public Integer getMobilePopularizeSpace() {
		return mobilePopularizeSpace;
	}

	public void setMobilePopularizeSpace(Integer mobilePopularizeSpace) {
		this.mobilePopularizeSpace = mobilePopularizeSpace;
	}

	@Column(name = "online_status")
	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	@Column(name = "online_time")
	public Date getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}
	@Column(name = "openup_time")
	public Date getOpenupTime() {
		return openupTime;
	}

	public void setOpenupTime(Date openupTime) {
		this.openupTime = openupTime;
	}
	@Column(name = "pic_address")
	public String getPicAddress() {
		return picAddress;
	}

	public void setPicAddress(String picAddress) {
		this.picAddress = picAddress;
	}
	@Column(name = "pic_ip_address")
	public String getPicIPAddress() {
		return picIPAddress;
	}
	public void setPicIPAddress(String picIPAddress) {
		this.picIPAddress = picIPAddress;
	}
	@Column(name = "pic_domain")
	public Integer getPicDomain() {
		return picDomain;
	}

	public void setPicDomain(Integer picDomain) {
		this.picDomain = picDomain;
	}
	@Column(name = "pic_password")
	public String getPicPassword() {
		return picPassword;
	}

	public void setPicPassword(String picPassword) {
		this.picPassword = picPassword;
	}
	@Column(name = "pic_port")
	public String getPicPort() {
		return picPort;
	}

	public void setPicPort(String picPort) {
		this.picPort = picPort;
	}
	@Column(name = "pic_upload_directory")
	public String getPicUploadDirectory() {
		return picUploadDirectory;
	}

	public void setPicUploadDirectory(String picUploadDirectory) {
		this.picUploadDirectory = picUploadDirectory;
	}
	@Column(name = "pic_upload_mode")
	public Integer getPicUploadMode() {
		return picUploadMode;
	}

	public void setPicUploadMode(Integer picUploadMode) {
		this.picUploadMode = picUploadMode;
	}
	@Column(name = "pic_user_name")
	public String getPicUserName() {
		return picUserName;
	}

	public void setPicUserName(String picUserName) {
		this.picUserName = picUserName;
	}
	@Column(name = "plan_id")
	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}
	@Column(name = "art_id")
	public String getArtId() {
		return artId;
	}
	public void setArtId(String artId) {
		this.artId = artId;
	}
	@Column(name = "product_detailed_pic_width_mobile")
	public Integer getProductDetailedPicWidthMobile() {
		return productDetailedPicWidthMobile;
	}

	public void setProductDetailedPicWidthMobile(Integer productDetailedPicWidthMobile) {
		this.productDetailedPicWidthMobile = productDetailedPicWidthMobile;
	}
	@Column(name = "product_detailed_pic_height_mobile")
	public Integer getProductDetailedPicHeightMobile() {
		return productDetailedPicHeightMobile;
	}
	public void setProductDetailedPicHeightMobile(
			Integer productDetailedPicHeightMobile) {
		this.productDetailedPicHeightMobile = productDetailedPicHeightMobile;
	}
	@Column(name = "product_pic_width_mobile")
	public Integer getProductPicWidthMobile() {
		return productPicWidthMobile;
	}
	public void setProductPicWidthMobile(Integer productPicWidthMobile) {
		this.productPicWidthMobile = productPicWidthMobile;
	}
	@Column(name = "product_pic_height_mobile")
	public Integer getProductPicHeightMobile() {
		return productPicHeightMobile;
	}
	public void setProductPicHeightMobile(Integer productPicHeightMobile) {
		this.productPicHeightMobile = productPicHeightMobile;
	}
	@Column(name = "programer_id")
	public Long getProgramerId() {
		return programerId;
	}

	public void setProgramerId(Long programerId) {
		this.programerId = programerId;
	}
	@Column(name = "qc_id")
	public String getQCId() {
		return QCId;
	}

	public void setQCId(String qCId) {
		QCId = qCId;
	}
	@Column(name = "sub_site_mobile")
	public Integer getSubSiteMobile() {
		return subSiteMobile;
	}
	@Column(name = "sms_pc")
	public Integer getSmsPC() {
		return smsPC;
	}
	public void setSmsPC(Integer smsPC) {
		this.smsPC = smsPC;
	}
	public void setSubSiteMobile(Integer subSiteMobile) {
		this.subSiteMobile = subSiteMobile;
	}
	@Column(name = "sub_site_pc")
	public Integer getSubSitePC() {
		return subSitePC;
	}
	public void setSubSitePC(Integer subSitePC) {
		this.subSitePC = subSitePC;
	}
	@Column(name = "pages_pc")
	public Integer getPagesPC() {
		return pagesPC;
	}
	public void setPagesPC(Integer pagesPC) {
		this.pagesPC = pagesPC;
	}
	@Column(name = "tailor_id")
	public String getTailorId() {
		return tailorId;
	}

	public void setTailorId(String tailorId) {
		this.tailorId = tailorId;
	}
	@Column(name = "training_id")
	public String getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}
	
	@Column(name = "wechat_message_num")
	public Long getWechatMessageNum() {
		return wechatMessageNum;
	}

	public void setWechatMessageNum(Long wechatMessageNum) {
		this.wechatMessageNum = wechatMessageNum;
	}
	@Column(name = "wechat_account_num")
	public Integer getWechatPublicAccountNum() {
		return wechatPublicAccountNum;
	}

	public void setWechatPublicAccountNum(Integer wechatPublicAccountNum) {
		this.wechatPublicAccountNum = wechatPublicAccountNum;
	}
	
	@Column(name = "popular_allow_amount")
	public Integer getPopularAllowAmount() {
		return popularAllowAmount;
	}
	public void setPopularAllowAmount(Integer popularAllowAmount) {
		this.popularAllowAmount = popularAllowAmount;
	}
	@Column(name = "popular_activity_num")
	public Integer getPopularActivityNum() {
		return popularActivityNum;
	}

	public void setPopularActivityNum(Integer popularActivityNum) {
		this.popularActivityNum = popularActivityNum;
	}
	@Column(name = "popular_page_per_activity")
	public Integer getPopularPagePerActivity() {
		return popularPagePerActivity;
	}
	public void setPopularPagePerActivity(Integer popularPagePerActivity) {
		this.popularPagePerActivity = popularPagePerActivity;
	}
	@Column(name = "editor_name")
	public String getEditorName() {
		return editorName;
	}
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	@Column(name = "programer_name")
	public String getProgramerName() {
		return programerName;
	}
	public void setProgramerName(String programerName) {
		this.programerName = programerName;
	}
	public void finalize() throws Throwable {

	}

}
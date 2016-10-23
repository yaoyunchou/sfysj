package com.nsw.wx.common.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 企业用户
 * 
 * @author Eason
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年8月17日 下午4:57:23
 * @Description: 企业用户表实体
 */
@Entity
@Table(name = "enterprise")
public class Enterprise implements Serializable {

	private static final long serialVersionUID = -7087022920378371888L;
	private long id;
	// 企业简称
	private String shortCustomerName;
	// 企业全称
	private String fullCustomerName;
	// 联系人名
	private String contactName = "";
	// 联系人邮箱
	private String contactEmail;
	// 地址
	private String address;
	// 企业所属行业
	private String industry;
	// 企业盈利模式
	private String profitModel;
	// 营业执照
	private String businessLicense;
	// 法人身份证
	private String corporateIdentityCard;
	// 企业座机
	private String fixedPhoneNum;
	// 传真
	private String fax;
	private Integer groupId;
	private Customer customer;
	// 备注
	private String memo;

	private String qq;
	
	private String logo;//PC上的logo
	
	private String mplogo;
	
	
	private String alt;
	private String icon;
	// 手机网站底部二维码
	private String dimensionalcode;
	// 商品分类展示方式
	private String displayform;
	// 新浪企业微博
	private String sinamicroblog;
	// 在线客服展示方式
	private String online_displayform;
	// 公司地图经度
	private String longitude;
	// 公司地图纬度
	private String latitude;
	// 默认短信号码
	private String default_message_num;

	private String websitephone;

	//驼峰命名jpa用_隔开字段
	//下面这些字段适用于PC
	private String btmlogo;// 底部Logo
	private String btmlogoalt;// 底部Logo的Alt标签
	private String qrcode;// 二维码(多个)
	private String xlweibo;// 新浪微博
	//private String qyWeiBo;// 企业微博

	@Column(name = "btmlogo")
	public String getBtmlogo() {
		return btmlogo;
	}

	public void setBtmlogo(String btmlogo) {
		this.btmlogo = btmlogo;
	}

	@Column(name = "btmlogoalt")
	public String getBtmlogoalt() {
		return btmlogoalt;
	}

	public void setBtmlogoalt(String btmlogoalt) {
		this.btmlogoalt = btmlogoalt;
	}
	
	
	@Column(name = "qrcode")
	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	@Column(name = "xlweibo")
	public String getXlweibo() {
		return xlweibo;
	}

	public void setXlweibo(String xlweibo) {
		this.xlweibo = xlweibo;
	}


//	@Column(name = "qyWeiBo")
//	public String getQyWeiBo() {
//		return qyWeiBo;
//	}
//
//	public void setQyWeiBo(String qyWeiBo) {
//		this.qyWeiBo = qyWeiBo;
//	}

	@Column(name = "websitephone")
	public String getWebsitephone() {
		return websitephone;
	}

	
	public void setWebsitephone(String websitephone) {
		this.websitephone = websitephone;
	}

	@Column(name = "dimensionalcode")
	public String getDimensionalcode() {
		return dimensionalcode;
	}

	public void setDimensionalcode(String dimensionalcode) {
		this.dimensionalcode = dimensionalcode;
	}

	@Column(name = "displayform")
	public String getDisplayform() {
		return displayform;
	}

	public void setDisplayform(String displayform) {
		this.displayform = displayform;
	}

	@Column(name = "sinamicroblog")
	public String getSinamicroblog() {
		return sinamicroblog;
	}

	public void setSinamicroblog(String sinamicroblog) {
		this.sinamicroblog = sinamicroblog;
	}

	@Column(name = "online_displayform")
	public String getOnline_displayform() {
		return online_displayform;
	}

	public void setOnline_displayform(String online_displayform) {
		this.online_displayform = online_displayform;
	}

	@Column(name = "default_message_num")
	public String getDefault_message_num() {
		return default_message_num;
	}

	public void setDefault_message_num(String default_message_num) {
		this.default_message_num = default_message_num;
	}

	@Column(name = "longitude")
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(name = "latitude")
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Column(name = "qq")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "logo")
	public String getLogo() {
		return logo;
	}
	

	@Column(name = "mplogo")
	public String getMplogo() {
		return mplogo;
	}

	public void setMplogo(String mplogo) {
		this.mplogo = mplogo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Column(name = "alt")
	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	@Column(name = "icon")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	// 联系人手机
	private String contactPhoneNumber;
	private Set<Site> sites = new HashSet<Site>();

	@JsonBackReference
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "enterprise")
	// 双向一对一关联以EnterpriseEntity类的customerEntity属性配置来进行关联
	public Customer getCustomer() {
		return customer;
	}

	@JsonBackReference
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "enterprise", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	public Set<Site> getSites() {
		return sites;
	}

	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "short_customer_name")
	public String getShortCustomerName() {
		return shortCustomerName;
	}

	public void setShortCustomerName(String shortCustomerName) {
		this.shortCustomerName = shortCustomerName;
	}

	@Column(name = "full_customer_name")
	public String getFullCustomerName() {
		return fullCustomerName;
	}

	public void setFullCustomerName(String fullCustomerName) {
		this.fullCustomerName = fullCustomerName;
	}

	@Column(name = "contact_name")
	public String getContactName() {
		return contactName;
	}

	@Column(name = "contact_email")
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "industry")
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Column(name = "profit_model")
	public String getProfitModel() {
		return profitModel;
	}

	public void setProfitModel(String profitModel) {
		this.profitModel = profitModel;
	}

	@Column(name = "business_license")
	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	@Column(name = "corporate_identity_card")
	public String getCorporateIdentityCard() {
		return corporateIdentityCard;
	}

	public void setCorporateIdentityCard(String corporateIdentityCard) {
		this.corporateIdentityCard = corporateIdentityCard;
	}

	@Column(name = "fixed_phone_num")
	public String getFixedPhoneNum() {
		return fixedPhoneNum;
	}

	public void setFixedPhoneNum(String fixedPhoneNum) {
		this.fixedPhoneNum = fixedPhoneNum;
	}

	@Column(name = "fax")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "group_id")
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Column(name = "memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "contact_phone_number")
	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}

	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

}

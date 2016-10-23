package com.nsw.wx.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
/**
* @author Eason
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月17日 下午4:43:54
* @Description: 用户表实体
 */
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)  

public class User implements Serializable{
	
	/**
	* @Fields serialVersionUID : TODO
	*/
	
	private static final long serialVersionUID = 2275699351384511899L;
	private long id;
	//用户名
	private String userName;
	//密码
	private String passWord;
	//创建人
	private String createdBy;
	//创建时间
	private Date createdTime;
	//登陆次数
 	private Long loginTimes=0L;
 	//最后登陆时间
	private Date lastLoginTime;
	//用户状态
	private String status;
	//最后登陆IP
	private String lastloginip;
	//删除时间
	private Date deleteTime;
	//异常登陆次数
	private Integer errorsTimes = 0;
	//用户锁定时间
	private Date lockTime;
	//0表示内部用户，1表示企业用户
	private Integer type = 0;    
	
	private String salt;
	
	private String enterpriseRelationId;
	
	private String resource;
	
	@Column(name = "resource")
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	
	@Column(name = "enterprise_relation_id")
	public String getEnterpriseRelationId() {
		return enterpriseRelationId;
	}
	
	
	public void setEnterpriseRelationId(String enterpriseRelationId) {
		this.enterpriseRelationId = enterpriseRelationId;
	}
	@Transient
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	private  Set<Role> roles;
	@JsonBackReference
	@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST}) 
	public Set<Role> getRoles() {
		return roles;
	}
	@JsonBackReference
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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
	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "pass_word")
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	@Column(name = "created_by")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name = "created_time")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
//	@Column(name = "login_times")
//	public int getLoginTimes() {
//		return loginTimes;
//	}
//
//	public void setLoginTimes(int loginTimes) {
//		this.loginTimes = loginTimes;
//	}
	@Column(name = "last_login_time")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "last_login_ip")
	public String getLastloginip() {
		return lastloginip;
	}

	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}
	
	
	@Column(name = "login_times")
	public Long getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Long loginTimes) {
		this.loginTimes = loginTimes;
	}
	@Column(name = "delete_time")
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}


	@Column(name = "errors_times")
	public Integer getErrorsTimes() {
		return errorsTimes;
	}
	public void setErrorsTimes(Integer errorsTimes) {
		this.errorsTimes = errorsTimes;
	}
	
	@Column(name = "lock_time")
	public Date getLockTime() {
		return lockTime;
	}
	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}
	@Column(name = "type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	

}

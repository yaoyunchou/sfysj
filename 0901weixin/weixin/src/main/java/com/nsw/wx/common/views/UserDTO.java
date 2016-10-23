package com.nsw.wx.common.views;
import java.io.Serializable;
import java.util.Set;

import com.nsw.wx.common.model.Role;


/**
 * 内部用户与外部用户共用数据
* @author Eason
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月17日 下午4:44:18
* @Description: TODO
 */

public class UserDTO implements Serializable{
	/**
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = -2852371165328772141L;
	//用户ID
	private Long userId;
	//0表示内部用户，1表示企业用户
	private Integer userType;
	//用户名称
	private String userName;
	//部门名称（内部用户才有）
	private String deptName;
	//邮箱
	private String email;
	//手机号码
	private String phoneNumber;
	//登陆IP
	private String loginIp;
	//最后登陆时间
	private String lastLoginTime;
	//角色名称
	private String roleName;
	//最后登陆IP
    private String lastloginip;
    //登陆次数
    private Long loginTimes = 0L;
    //密码
	private String passWord;
	//创建人
	private String createdBy;
	//内部用户名称
	private String fullName;
	//企业用户联系人名称
	private String contactName;  
    private  Set<Role> roles;
    
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLastloginip() {
		return lastloginip;
	}

	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}

	public Long getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Long loginTimes) {
		this.loginTimes = loginTimes;
	}
	
	
	

}

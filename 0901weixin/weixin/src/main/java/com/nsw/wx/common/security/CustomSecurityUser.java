package com.nsw.wx.common.security;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved. 
 * @date 2015年9月19日 下午3:26:18
 * @Description: SecurityUser为系统user和spring security的userDetails的中间转换类
 */
public class CustomSecurityUser extends User{
	
	
	
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(CustomSecurityUser.class);

	private String salt; 
	
	private Long id ;

	private String projId ;
	
	private String site;
	
	private String lastLoginTime;
	
	private String lastLoginIp;
	
	private String deptName;
	
	private String roleName;
	
	private Long loginTimes;
	
	private String loginIp;
	
	private String mobile;
	
	private String projectType;
	
	private String type; //内部用户0   企业用户1
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	

	public Long getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Long loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CustomSecurityUser(String username, String password, boolean enabled,  
	            boolean accountNonExpired, boolean credentialsNonExpired,  
	            boolean accountNonLocked,  
	            Collection<? extends GrantedAuthority> authorities, String salt, Long id,String type,Long loginTimes,
	            String lastLoginIp,Date lastLoginTime ) {  
		
	        super(username, password, enabled, accountNonExpired,credentialsNonExpired, accountNonLocked, authorities);  
	        this.salt = salt;  
	        this.id = id;
			this.projId = projId;
	        this.type = type;
	        this.loginTimes = loginTimes;
	        this.lastLoginIp = lastLoginIp;
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        if(lastLoginTime != null){
	         this.lastLoginTime = format.format(lastLoginTime);
	        }
	        logger.info("userId " + id);
	    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}


	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
	}
}

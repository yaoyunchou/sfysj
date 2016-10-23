package com.nsw.wx.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "role")
@PrimaryKeyJoinColumn(name = "id")
public class Role implements Serializable{
	/**
	* @Fields serialVersionUID : TODO
	*/
	
	private static final long serialVersionUID = -710943221794635642L;
	// 编号
	private long id;
	// 角色名称
	private String name;
	// 是否禁用
	private String disabled;
	// 时间
	private Date createdTime;
	// 登录用户ID
	private long createdBy;
	// 角色分类(1.系统用户, 2.赚得快系统 , 3.微信管家, 4.移动推广,5.手机建站平台，6.PC建站平台)
	private int type;
	// 备注
	private String memo;
	//private Set<Title> titles;
	private Set<User> users = new HashSet<User>();

	@JsonManagedReference
	@ManyToMany(cascade = { CascadeType.PERSIST })
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
	public Set<User> getUsers() {
		return users;
	}

	@JsonManagedReference
	public void setUsers(Set<User> users) {
		this.users = users;
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

//	@JsonBackReference
//	@ManyToMany(cascade = CascadeType.MERGE)
//	@JoinTable(name = "role_title", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "title_id") })
//	public Set<Title> getTitles() {
//		return titles;
//	}
//
//	@JsonBackReference
//	public void setTitles(Set<Title> titles) {
//		this.titles = titles;
//	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "disabled")
	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	@Column(name = "type")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "created_time")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "created_by")
	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

}

package com.nsw.wx.common.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
/**
 * 站点组
* @author Eason
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年8月17日 下午4:37:49
* @Description: TODO
 */

@JsonFilter("site_group")
@Entity
@Table(name = "site_group")
public class SiteGroup implements Serializable {
   
	/**
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = 4197756685708654051L;
	private long id;
	//组名称
	private String name;
	//备注
	private String memo;
	//创建时间
	private Date createdTime;
	private Set<Site> sites;
	//类型
	private Integer type;
	//private List<Resource> resources=new ArrayList<Resource>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")	
	public long getId() {
		return id;
	}
	@Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@OneToMany(mappedBy = "siteGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)	
	public Set<Site> getSites() {
		return sites;
	}	
	
	public void setSites(Set<Site> sites) {
		this.sites = sites;	
	}
	
	@Column(name = "memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "created_time")
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
//	@JsonManagedReference
//	@ManyToMany (cascade = { CascadeType.PERSIST })
//	@JoinTable(name = "group_resource", joinColumns = {@JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "resource_id") })
//	@OrderBy (value = "order asc")	
//	public List<Resource> getResources() {
//		return resources;
//	}
//	@JsonManagedReference
//	public void setResources(List<Resource> resources) {
//		this.resources = resources;
//	}

}
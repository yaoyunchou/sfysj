package com.nsw.wx.common.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name="id")  
public class Customer extends User {
	
	private static final long serialVersionUID = 8183589900851431976L;
	private Enterprise enterprise;
	@JsonManagedReference
	@OneToOne(cascade=CascadeType.ALL)  
	@JoinColumn(name="enterprise_id")
	public Enterprise getEnterprise() {
		return enterprise;
	}
	@JsonManagedReference
	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}
	 
}

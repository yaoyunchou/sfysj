package com.nsw.wx.common.docmodel;

import java.util.List;

import com.nsw.wx.api.model.MenuButton;

/**
 * 菜单实体
* @author pzliu
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2016年5月18日 下午3:36:28
* @Description: TODO
 */
public class MenuEntity {
	private List<MenuButton> button;
	private String appId;
	private boolean enable ;
	private String materialType;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public List<MenuButton> getButton() {
		return button;
	}
	public void setButton(List<MenuButton> button) {
		this.button = button;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
}

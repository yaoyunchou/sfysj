package com.nsw.wx.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nsw.wx.common.model.Enterprise;
import com.nsw.wx.common.model.Site;

public interface SiteRepository extends JpaRepository<Site, Integer> {
	
	List<Site> findByEnterpriseAndType(Enterprise enterprise, int type);
}

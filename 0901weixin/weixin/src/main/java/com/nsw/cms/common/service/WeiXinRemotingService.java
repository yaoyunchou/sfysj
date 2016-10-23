package com.nsw.cms.common.service;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信提供接口(Rest或者RMI)
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2016年5月18日 下午13:57:46
 * @Description: TODO
 */
public interface WeiXinRemotingService {
	
	/**
	 * 文件查询
	 * @desc: www.nsw88.com Inc. All rights reserved.
	 * @date 2016年5月18日 下午4:31:46
	 * @Description: 微信提供文件查询接口
	 * @param projIds匹配的项目ID清单
	 * @param ctgId文件分类ID
	 * @param fileName匹配的文件名称
	 * @param pageNum当前页索引
	 * @param pageSize每页显示的记录数
	 */
	public Map listFiles(String projIds[],String fileName, int pageNum, int pageSize,String ctgId) ;
	
	
	
	/**
	    * @Description:  图库分类列表
	    * @param @param buf
	    * @param @return   
	    * @return InputStream  
	    * @throws
	     */
	public Map listFileCtg(String projId);
	
	
	/**
	    * @Description:  图库查询满足条件的记录数
	    * @param @param buf
	    * @param @return   
	    * @return InputStream  
	    * @throws
	     */
	public Long fileCount(String[] projIds, String fileName, String ctgId);

	
	/**
	 * 获取企业的项目清单
	 * @desc: www.nsw88.com Inc. All rights reserved.
	 * @date 2016年5月18日 下午4:31:46
	 * @Description:  获取企业的项目清单
	 * @param enterpriseId 企业ID
	 */
	public Map getProjectLists(String enterpriseId);
	
	/**
	 * 获取文章清单
	 * @desc: www.nsw88.com Inc. All rights reserved.
	 * @date 2016年5月18日 下午4:31:46
	 * @Description:  获取文章清单
	 */
	public Map getArticleLists(String projId, String title, String moduleId,String ctgId, Integer pageNum, Integer pageSize);

	/**
	 * 满足条件的文章数量
	 * @desc: www.nsw88.com Inc. All rights reserved.
	 * @date 2016年5月18日 下午4:31:46
	 * @Description:  获取文章清单
	 */
	public Long getArticlCount(String projId, String title, String moduleId,Set ctgId);

	/**
	 * 获取模块清单
	 * @desc: www.nsw88.com Inc. All rights reserved.
	 * @date 2016年5月18日 下午4:31:46
	 * @Description:  获取模块清单
	 */
	public Map getModuleLists(String projId);

	/**
	 * 获取分类清单
	 * @desc: www.nsw88.com Inc. All rights reserved.
	 * @date 2016年5月18日 下午15:31:46
	 * @Description:  获取分类清单
	 */
	public Map getCtgLists(String projId, String moduleId);
	
	
	/**
	 * 微信切换项目
	 * @desc: www.nsw88.com Inc. All rights reserved.
	 * @date 2016年5月18日 下午15:31:46
	 * @Description:  获取分类清单
	 */
	public Map remoteSwitchProject(HttpServletRequest request);
	
	/**
	 * 微信查询已经发布的文章和产品(分页参数,项目Id,title)
	 * @date 2016年5月18日 下午4:31:46
	 * @Description: 微信提供文件查询接口
	 * @param projIds匹配的项目ID清单
	 * @param ctgId文件分类ID
	 * @param fileName匹配的文件名称
	 * @param pageNum当前页索引
	 * @param pageSize每页显示的记录数
	 */
	public Map getPublishedArticle(String projId, String title, Integer pageNum, Integer pageSize);
	

}

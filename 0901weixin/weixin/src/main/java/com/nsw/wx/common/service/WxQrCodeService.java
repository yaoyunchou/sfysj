package com.nsw.wx.common.service;


import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.nsw.wx.common.docmodel.QrCode;

/**
 * 
 * @author liuzp
 * 二维码数据来源统计管理
 */
public interface WxQrCodeService {
	
	/**
	 * 添加二维码
	 * @param qrCode
	 * @return
	 */
	QrCode  addQrCode(QrCode qrCode); 

	/**
	 * 更新二维码
	 * @param qrCode
	 * @return
	 */
	boolean  updateQrCode(String id, Update update); 
	
	/**
	 * 删除二维码
	 * @param qrCodes
	 * @return
	 */
	boolean  deleteQrCode(String [] qrCodes);
	
	/**
	 * 二维码列表
	 * @param appId
	 * @return
	 */
	List<Map<String,Object>> getQrCodeList(String appId,int pageNum,int pageSize,String title,String beginDate,String endDate,boolean isPaging);
	/**
	 * 检测二维码标题是否重复
	 * @param appId
	 * @param title
	 * @return
	 */
	boolean checkTitleReply(String appId,String title);
	
	
	/**
	 * 获取二维码数据
	 * @param id
	 * @return
	 */
	QrCode getQrCode(String id);
	
	/**
	 * 根据条件查询二维码数据数量
	 * @param appId
	 * @param title
	 * @return
	 */
	int getQrCodeCount(String appId, String title);
	
	/**
	 * 根据条件查询二维码
	 * @param query
	 * @return
	 */
	QrCode getQrCodeByQuery(Query query);
	
	
	/**
	 * 查询全部二维码
	 * @param appId
	 * @return
	 */
	List<QrCode> getQrCodeList(String appId);
	
}

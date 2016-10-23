package com.nsw.wx.common.views;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 信息工具类
 * @author lzhen
 * @date 创建时间：2015年6月27日 下午5:14:45
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class Message implements Serializable {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 2269158947840583289L;
	// 是否成功
	private boolean isSuc;
	// 信息
	private String msg;
	// 信息列表
	private List<Object> messages;
	//返回数据
	private Object data;
	

	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<Object> getMessages() {
		return messages;
	}
	public void setMessages(List<Object> messages) {
		this.messages = messages;
	}
	public boolean getIsSuc() {
		return isSuc;
	}
	public void setIsSuc(boolean isSuc) {
		this.isSuc = isSuc;
	}
	public Message(boolean isSuc, String msg){
		this.isSuc = isSuc;
		this.msg = msg;
	}
	
	public Message(boolean isSuc, Object data){
		this.isSuc = isSuc;
		this.data = data;
	}
	public Message(){
		
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	
	
}

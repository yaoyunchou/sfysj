package com.nsw.wx.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;


/**
* @author Aaron
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年9月23日 下午3:04:55
* @Description: 通用的工具类
*/
public class CommonUtil {
	 
	public static Logger logger = Logger.getLogger(CommonUtil.class); 
	
	public static String escapeExprSpecialWord(String keyword) {  
	    if (StringUtils.isNotBlank(keyword)) {  
	        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
	        for (String key : fbsArr) {  
	            if (keyword.contains(key)) {  
	                keyword = keyword.replace(key, "\\" + key);  
	            }  
	        }  
	    }  
	    return keyword;  
	} 
    /** 
    * @Description: 基于本地化流，深度拷贝一个List对象，list的对象需要实现serialization
    * @param @param src
    * @param @return
    * @param @throws IOException
    * @param @throws ClassNotFoundException   
    * @return List  
    * @throws 
    */ 
    public static List deepCopy(List src) throws IOException, ClassNotFoundException {  
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();  
		ObjectOutputStream out = new ObjectOutputStream(byteout);  
		out.writeObject(src);  
		ByteArrayInputStream bytein = new ByteArrayInputStream(byteout  
		        .toByteArray());  
		ObjectInputStream in = new ObjectInputStream(bytein);  
		List dest = (List) in.readObject();  
		return dest;  
    }
    /**
    * @Description: 基本原理是将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**，或其他的也可以）。这样就可以直接例用length方法获得字符串的字节长度了
    * @param @param s
    * @param @return   
    * @return int  
    * @throws
     */
    public static  int getWordCountRegex(String s)  
    {  
  
        s = s.replaceAll("[^\\x00-\\xff]", "**");  
        int length = s.length();  
        return length;  
    }  
    
    /** 
    * @Description: 去除页面不以“/”开头的projId 
    * @param @param content
    * @param @param projId
    * @param @return   
    * @return String  
    * @throws 
    */ 
    public static String removeProjId(String content, String projId){  
    	
    	StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile("[^/]{1}" + projId + "/");
        Matcher m = p.matcher(content);
        while (m.find()) {
        	m.appendReplacement(sb, m.group().replace(projId + "/", ""));
        } 
        m.appendTail(sb); 
        
        return sb.toString();
    }  
    public static boolean checkIsLink(String linkUrl){
    	if(linkUrl.trim().indexOf("http://") == 0 || linkUrl.trim().indexOf("https://") == 0){
			return true;
		}
		return false;
    }
    
    	
  /*  public static  Writer generateStaticFile(Context context, InputStream is){
    	String
    	VelocityEngine velocityEngine = (VelocityEngine) ContextUtil
				.getBean("velocityEngine");
		StringWriter sw = new StringWriter();
		velocityEngine.evaluate(context, sw, "", rule);
    }*/
}


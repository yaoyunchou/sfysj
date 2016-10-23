package com.nsw.wx.common.security;

import java.security.SecureRandom;
import java.util.Random;
import org.apache.log4j.Logger;
import org.springframework.util.DigestUtils;

/**
 * 
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved. 
 * @date 2015年9月12日 下午3:34:44
 * @Description: 密码盐值加密测试 
 */
public class TestSaltWithPassword {
	
	public static Logger logger = Logger.getLogger(TestSaltWithPassword.class);

	
	  public static final int SALT_LENGTH = 16;    
		
	  
	   public String SaltGenerates() throws Exception {
	        Random random = new SecureRandom();
	        byte[] byteSalt = new byte[SALT_LENGTH];
	        random.nextBytes(byteSalt);   
	        String salt=byteSalt.toString();
	       //logger.info(salt.equals(byteSalt) + " " + salt.equals(byteSalt.toString()));
	        String strMd5Salt = DigestUtils.md5DigestAsHex(salt.getBytes());
	       //logger.info(strMd5Salt+" "+PropsUtil.passwordMD5(salt));
	       //logger.info(Md5PasswordEncoderUtil.encodePassword("admin",strMd5Salt));
	       //logger.info(DigestUtils.md5DigestAsHex("admin".getBytes()).toString()
	       //.equals("21232f297a57a5a743894a0e4a801fc3"));
	        return strMd5Salt;
	    }

}

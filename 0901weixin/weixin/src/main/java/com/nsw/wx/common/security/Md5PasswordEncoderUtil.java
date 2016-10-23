package com.nsw.wx.common.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年9月25日 下午2:57:03
 * @Description: MD5加密, 盐值生成工具类
 */
public class Md5PasswordEncoderUtil {

	static Logger logger = Logger.getLogger(Md5PasswordEncoderUtil.class);
	
	public static String encodePassword(String rawPass, Object salt) {
		Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		md5PasswordEncoder.setEncodeHashAsBase64(false);
		String passWord = md5PasswordEncoder.encodePassword(rawPass, salt);
		return passWord;
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private Object salt;

	private String algorithm;

	public Md5PasswordEncoderUtil(Object salt, String algorithm) {
		this.salt = salt;
		this.algorithm = algorithm;
	}

	public Md5PasswordEncoderUtil() {
	}

	public String encode(String rawPass) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			// 加密后的字符串
			result = byteArrayToHexString(md.digest(mergePasswordAndSalt(
					rawPass).getBytes("utf-8")));
		} catch (Exception ex) {
		}
		return result;
	}

	public boolean isPasswordValid(String encPass, String rawPass) {
		String pass1 = "" + encPass;
		String pass2 = encode(rawPass);

		return pass1.equals(pass2);
	}

	/**
	 * @Description: 密码和盐值融合
	 * @param @param password
	 * @param @return
	 * @return String
	 * @throws
	 */
	private String mergePasswordAndSalt(String password) {
		if (password == null) {
			password = "";
		}

		if ((salt == null) || "".equals(salt)) {
			return password;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static void test() {
		String salt = "helloworld";
		Md5PasswordEncoderUtil encoderMd5 = new Md5PasswordEncoderUtil(salt,
				"MD5");
		String encode = encoderMd5.encode("test");
		System.out.println(encode + "  " + salt);
		boolean passwordValid = encoderMd5.isPasswordValid(
				"1bd98ed329aebc7b2f89424b5a38926e", "test");
		System.out.println(passwordValid);

		Md5PasswordEncoderUtil encoderSha = new Md5PasswordEncoderUtil(salt,
				"SHA");
		String pass2 = encoderSha.encode("test");
		System.out.println(pass2 + "  " + salt);
		boolean passwordValid2 = encoderSha.isPasswordValid(
				"1bd98ed329aebc7b2f89424b5a38926e", "test");
		System.out.println(passwordValid2);
	}

	// 生成盐值
	public void SaltGenerate() throws UnsupportedEncodingException {
		Random ranGen = new SecureRandom();
		byte[] aesKey = new byte[20];
		ranGen.nextBytes(aesKey);
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < aesKey.length; i++) {
			String hex = Integer.toHexString(0xff & aesKey[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		System.out.println(hexString);
	}

	public static final int SALT_LENGTH = 16;

	public static String SaltGenerates() throws Exception {
		Random RANDOM = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH];
		RANDOM.nextBytes(salt);
		String strMd5Salt = DigestUtils.md5DigestAsHex(salt);
		logger.info(salt + "  " + strMd5Salt);
		logger.info(encodePassword("admin", strMd5Salt));
		return strMd5Salt;
	}

}

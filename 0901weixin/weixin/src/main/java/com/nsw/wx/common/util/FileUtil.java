package com.nsw.wx.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.nsw.wx.common.json.Jackson;

/**
 * @author Aaron
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2015年8月17日 上午11:06:46
 * @Description: 文件处理工具类
 */
public class FileUtil {

	static Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * 获取两个文件或者文件夹大小比，-1, 1-100
	 * 
	 * @param srcPath
	 *            源文件/文件夹路径
	 * @param targetPath
	 *            目标文件/文件夹路径
	 * @return
	 */
	public static int fileComparison(String srcPath, String targetPath) {
		File src = new File(srcPath);
		File target = new File(targetPath);
		if (src.exists() && target.exists()) {
			return Double.valueOf(
					Math.floor(getFileSize(new File(srcPath))
							/ getFileSize(new File(targetPath)) * 100))
					.intValue();
		} else {
			return Constants.INT_NEGATIVE_ONE;
		}
	}

	/**
	 * @Description: 获取文件或者文件夹大小
	 * @param @param file
	 * @param @return
	 * @return double
	 * @throws
	 */
	public static double getFileSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				for (File f : children)
					size += getFileSize(f);
				return size;

				// 如果是文件则直接返回其大小, 以“兆”为单位
			} else {
				double size = (double) file.length() / Constants.BIT_SIZE
						/ Constants.BIT_SIZE;
				return size;
			}
		} else {
			logger.info("文件或者文件夹不存在，请检查路径是否正确！");
			return Integer.valueOf(Constants.INT_ZERO).doubleValue();
		}
	}

	/**
	 * @Description: 获取文件类型
	 * @param @param filename
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getFileType(String filename) {
		String filetype = "";
		String suffix = filename.substring(
				filename.lastIndexOf(Constants.POINT) + Constants.INT_ONE)
				.toLowerCase();

		if (suffix.equalsIgnoreCase("html")) {
			filetype = Constants.T_HTML;
		}
		if (suffix.equalsIgnoreCase("css")) {
			filetype = Constants.T_CSS;
		}
		if (suffix.equalsIgnoreCase("js")) {
			filetype = "application/x-javascript";
		}
		if (suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg")) {
			filetype = Constants.T_JPEG;
		}
		if (suffix.equalsIgnoreCase("gif")) {
			filetype = Constants.T_GIF;
		}
		if (suffix.equalsIgnoreCase("png")) {
			filetype = Constants.T_PNG;
		}
		return filetype;
	}

	/**
	 * @Description: 创建文件
	 * @param @param destFileName
	 * @param @return
	 * @param @throws Exception
	 * @return boolean
	 * @throws
	 */
	public static boolean CreateFile(String destFileName) throws Exception {
		File file = new File(destFileName);
		if (file.exists()) {
			logger.info("文件已存在");
		}
		if (destFileName.endsWith(File.separator)) {

		}
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				throw new Exception("");
			}
		}
		try {
			if (file.createNewFile()) {
				return true;
			} else {
				throw new Exception("" + destFileName + "");
			}
		} catch (IOException e) {
			logger.error("新建文件失败", e);
			throw e;
		}
	}

	/**
	 * @Description: 创建文件夹
	 * @param @param destDirName
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		if (dir.mkdirs()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 保存上传文件
	 * 
	 * @param path
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @param file
	 *            上传的文件
	 * @throws Exception
	 */
	public static File saveFile(String path, String fileName, MultipartFile file)
			throws Exception {
		// 创建目录
		createDir(path);
		// 创建文件
		CreateFile(path + Constants.LEFT_SPRIT + fileName);
		File f = new File(path + Constants.LEFT_SPRIT + fileName);
		// 保存上传的文件
		file.transferTo(f);
		return f;
	}

	// 可接受的文件类型
	public static boolean isAvailableFileType(String str, String[] ACCEPT_TYPES) {
		boolean flag = false;
		for (String temp : ACCEPT_TYPES) {
			if (temp.equalsIgnoreCase(str)) {
				flag = true;
			}
		}
		return flag;
	}

	public static List<Map<String, Object>> parseJsonToListMap(String fileName) {
		fileName = FileUtil.class.getResource("/" + fileName).getPath();
		logger.info(fileName + "*************************");
		try {
			return new Jackson().fromJsonArray(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>();
	}

	public static Map<String, Object> parseJsonToMap(String fileName) {
		fileName = FileUtil.class.getResource("/" + fileName).getPath();
		try {
			return new Jackson().fromJsonObject(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new HashMap<String, Object>();
	}

	public static boolean copyFile(BufferedInputStream buffer, String path,
			String filename) {
		
		FileOutputStream fs =null;
		try {
		    fs = new FileOutputStream(path+"/"+filename);
			byte[] byteArray = new byte[1024];
			int tmp = 0;
			while ((tmp = buffer.read(byteArray)) != -1) {
				fs.write(byteArray,0,tmp);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info( "**********下载临时文件出错***************"+path+filename);
			return false;
		} finally {
			try {
				if (buffer != null) {
					buffer.close();
				}
				if(fs != null){
					fs.close();
				}
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

}

package com.nsw.wx.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


public class ImageFactory {

	private static Logger logger = Logger
			.getLogger(ImageFactory.class);
	
	public static void main(String[] args) {
		int width = 200;
		int height = 200;
		String filePath = "d:\\111.jpg";
		String resultFilePath = "d:\\222.jpg";
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(filePath);
			fos = new FileOutputStream(resultFilePath);
			// InputStream is = imgCompress(fis, width, height, null);
			InputStream is = imgCompressToScale(fis, 0.2f,"jpg");
			int c;
			while ((c = is.read()) != -1) {
				fos.write(c);
			}
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean checkCompressSize(int srcWidth, int srcHeight,
			int width, int height) {
		if (srcWidth < width) {
			return false;
		}
		if (height > 0) {
			if (srcHeight < height) {
				return false;
			}
		}
		return true;
	}

	public static InputStream imgCompress(InputStream is, int width, int height) {
		return imgCompress(is, width, height, null);
	}
	
	
	/**
	 * 获取图片输入流的宽度
	 ***/
	public static Map getImgWidth(InputStream is){
		Map map = new HashMap();
		BufferedImage srcImage = null;
			try {
				srcImage = ImageIO.read(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int srcWidth = srcImage.getWidth();
			int srcHeight = srcImage.getHeight();
			map.put("width", srcWidth);
			map.put("height", srcHeight);
		return map;
	}
	

	public static InputStream imgCompress(InputStream is, int width,
			int height, String formatName) {
		if (formatName == null || "".equals(formatName)) {
			formatName = "JPEG";
		}
		BufferedImage srcImage = null;
		BufferedImage dstImage = null;
		ByteArrayInputStream bis = null;
		try {
			srcImage = ImageIO.read(is);
			int srcWidth = srcImage.getWidth();
			int srcHeight = srcImage.getHeight();
			if (!checkCompressSize(srcWidth, srcHeight, width, height)) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(srcImage, formatName, bos);
				return new ByteArrayInputStream(bos.toByteArray());
			}
			double srcRatio = (double) srcWidth / (double) srcHeight;
			double destRatio = (double) width / (double) height;
			int resizeWidth = 0;
			int resizeHeight = 0;
			// 如果原始图片的宽高比例大于指定尺寸的宽高比例，则原始图片的宽缩放到取指定宽，而高按宽缩变比例变化
			if (srcRatio > destRatio) {
				resizeHeight = (int) (((double) srcHeight / (double) srcWidth) * (double) width);
				resizeWidth = width;
			} else {
				resizeWidth = (int) (((double) srcWidth / (double) srcHeight) * (double) height);
				resizeHeight = height;
			}
			dstImage = ImageScaler.changeSize(srcImage, resizeWidth,
					resizeHeight);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(dstImage, formatName, out);
			byte[] data = out.toByteArray();
			bis = new ByteArrayInputStream(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bis;
	}

	public static InputStream imgCompressToScale(InputStream is, float scale) {
		return imgCompressToScale(is, scale, null);
	}

	public static InputStream imgCompressToScale(InputStream is, float scale,
			String formatName) {
		if (formatName == null || "".equals(formatName)) {
			formatName = "JPEG";
		}
		if (scale <= 0.0f || scale > 1.0f) {
			try {
				throw new Exception("指定缩放图片的的比例只能在0到1之间");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		BufferedImage srcImage = null;
		BufferedImage dstImage = null;
		ByteArrayInputStream bis = null;
		try {
			srcImage = ImageIO.read(is);
			int srcWidth = srcImage.getWidth();
			int srcHeight = srcImage.getHeight();
			int resizeWidth = (int) (srcWidth * scale);
			int resizeHeight = (int) (srcHeight * scale);
			dstImage = ImageScaler.changeSize(srcImage, resizeWidth,
					resizeHeight);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(dstImage, formatName, out);
			byte[] data = out.toByteArray();
			bis = new ByteArrayInputStream(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bis;
	}
	
	
	public static InputStream imgCompressToWidth(InputStream is, int width) {
		return imgCompressToWidth(is, width, null);
	}

	public static InputStream imgCompressToWidth(InputStream is, int width,
			String formatName) {
		if (formatName == null || "".equals(formatName)) {
			formatName = "JPEG";
		}
		BufferedImage srcImage = null;
		BufferedImage dstImage = null;
		ByteArrayInputStream bis = null;
		try {
			srcImage = ImageIO.read(is);
			int srcWidth = srcImage.getWidth();
			int srcHeight = srcImage.getHeight();
			if (!checkCompressSize(srcWidth, srcHeight, width, 0)) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(srcImage, formatName, bos);
				return new ByteArrayInputStream(bos.toByteArray());
			}
			int resizeWidth = width;
			int resizeHeight = 0;
			resizeHeight = (int) ((double) srcHeight / ((double) srcWidth / (double) width));
			dstImage = ImageScaler.changeSize(srcImage, resizeWidth,
					resizeHeight);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(dstImage, formatName, out);
			byte[] data = out.toByteArray();
			bis = new ByteArrayInputStream(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bis;
	}
	
	
	
	//产品的图片小与320不压缩
	public static InputStream imgCompressProduct(InputStream is, int width,
			int height, String formatName) {
		if (formatName == null || "".equals(formatName)) {
			formatName = "JPEG";
		}
		BufferedImage srcImage = null;
		BufferedImage dstImage = null;
		ByteArrayInputStream bis = null;
		try {
			srcImage = ImageIO.read(is);
			int srcWidth = srcImage.getWidth();
			int srcHeight = srcImage.getHeight();
			if (srcWidth < 320 || srcHeight < 320) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(srcImage, formatName, bos);
				return new ByteArrayInputStream(bos.toByteArray());
			}
			double srcRatio = (double) srcWidth / (double) srcHeight;
			double destRatio = (double) width / (double) height;
			int resizeWidth = 0;
			int resizeHeight = 0;
			// 如果原始图片的宽高比例大于指定尺寸的宽高比例，则原始图片的宽缩放到取指定宽，而高按宽缩变比例变化
			if (srcRatio > destRatio) {
				resizeHeight = (int) (((double) srcHeight / (double) srcWidth) * (double) width);
				resizeWidth = width;
			} else {
				resizeWidth = (int) (((double) srcWidth / (double) srcHeight) * (double) height);
				resizeHeight = height;
			}
			dstImage = ImageScaler.changeSize(srcImage, resizeWidth,
					resizeHeight);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(dstImage, formatName, out);
			byte[] data = out.toByteArray();
			bis = new ByteArrayInputStream(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bis;
	}
}

package com.nsw.wx.common.util;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;

public class ImageScaler {

	
	public static BufferedImage scalingWidth(BufferedImage bimage, int resizeWidth) throws IOException {
		BigDecimal width = new BigDecimal(bimage.getWidth());
		BigDecimal height = new BigDecimal(bimage.getHeight());
		BigDecimal resizeWidthB = new BigDecimal(resizeWidth);
		BigDecimal resizeHeight = (resizeWidthB.divide(width, 2, BigDecimal.ROUND_HALF_UP)).multiply(height);
		resizeHeight = resizeHeight.setScale(0, BigDecimal.ROUND_HALF_UP);
		return scaling(bimage, resizeWidthB.intValue(), resizeHeight.intValue());
	}

	public static BufferedImage scalingHeight(BufferedImage bimage, int resizeHeight) throws IOException {
		BigDecimal width = new BigDecimal(bimage.getWidth());
		BigDecimal height = new BigDecimal(bimage.getHeight());
		BigDecimal resizeHeightB = new BigDecimal(resizeHeight);
		BigDecimal resizeWidth = resizeHeightB.divide(height, 2, BigDecimal.ROUND_HALF_UP).multiply(width);
		resizeWidth = resizeWidth.setScale(0, BigDecimal.ROUND_HALF_UP);
		return scaling(bimage, resizeWidth.intValue(), resizeHeightB.intValue());
	}

	private static BufferedImage scaling(BufferedImage bimage, int resizeWidth, int resizeHeight) throws IOException {
		BufferedImage buffImage = new BufferedImage(resizeWidth, resizeHeight, bimage.getType());
		buffImage.getGraphics().drawImage(
				bimage.getScaledInstance(resizeWidth, resizeHeight, Image.SCALE_AREA_AVERAGING),
				0, 0, resizeWidth, resizeHeight, null
				);
		return buffImage;
	}

	public static BufferedImage changeSize(BufferedImage bimage, int width, int height) throws IOException {
		if (isExceedSpecifiedSize(bimage, width, height)) {
			bimage = scalingWidth(bimage, width);
		} else {
			bimage = scalingHeight(bimage, height);
		}
		return overlayingImage(bimage, width, height);
	}
	
	private static boolean isExceedSpecifiedSize(BufferedImage bimage, int resizeWidth, int resizeHeight) {
		BigDecimal width = new BigDecimal(bimage.getWidth());
		BigDecimal height = new BigDecimal(bimage.getHeight());
		BigDecimal resizeWidthB = new BigDecimal(resizeWidth);
		BigDecimal resizeHeightB = (resizeWidthB.divide(width, 2, BigDecimal.ROUND_HALF_UP)).multiply(height);
		return resizeHeightB.doubleValue() <= resizeHeight;
	}
	
	private static BufferedImage overlayingImage(BufferedImage bimage, int width, int height) throws IOException {
		BufferedImage wimage = createImageBgWhite(width, height);
		Graphics2D gr = wimage.createGraphics();
		gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		gr.drawImage(
			bimage, 
			(wimage.getWidth() - bimage.getWidth()) / 2,
			(wimage.getHeight() - bimage.getHeight()) / 2, null
		);
		gr.dispose();
		return wimage;
	}
	
	private static BufferedImage createImageBgWhite(int width, int height) throws IOException {
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = im.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		im.flush();
		return im;
	}
}

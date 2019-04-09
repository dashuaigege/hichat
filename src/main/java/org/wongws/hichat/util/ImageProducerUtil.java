package org.wongws.hichat.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageProducerUtil {

	private static String userimg;

	@Value("${hichat.user.imgurl}")
	public void setUserimg(String user_img) {
		this.userimg = user_img;
	}

	public static String getUserimg() {
		return userimg;
	}

	public static void create(Long imgIndex, String content) throws Exception {
		String rootPath = userimg;
		for (ChatEnum.EnuPicType item : ChatEnum.EnuPicType.values()) {
			Color contentColor = Color.WHITE;
			if (item.getValue() == ChatEnum.EnuPicType.online.getValue())
				contentColor = Color.RED;
			createImage(content, contentColor, new Font("宋体", Font.PLAIN, 100),
					Paths.get(rootPath, imgIndex + "_" + item.getValue() + ".png").toFile());
		}
	}

	private static int[] getWidthAndHeight(String text, Font font) {
		Rectangle2D r = font.getStringBounds(text,
				new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
		int unitHeight = (int) Math.floor(r.getHeight());//
		// 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
		int width = (int) Math.round(r.getWidth()) + 1;
		// 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
		int height = unitHeight + 3;
		System.out.println("width:" + width + ", height:" + height);
		return new int[] { width, height };
	}

	// 根据str,font的样式以及输出文件目录
	private static void createImage(String text, Color contentColor, Font font, File outFile) throws Exception {
		// 获取font的样式应用在str上的整个矩形
		int[] arr = getWidthAndHeight(text, font);
		int width = arr[0];
		int height = arr[1];
		// 创建图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);// 创建图片画布
		Graphics g = image.getGraphics();
		g.setColor(contentColor); // 先用白色填充整张图片,也就是背景
		g.fillRect(0, 0, width, height);// 画出矩形区域，以便于在矩形区域内写入文字
		g.setColor(Color.black);// 再换成黑色，以便于写入文字
		g.setFont(font);// 设置画笔字体
		g.drawString(text, 0, font.getSize());// 画出一行字符串
		g.drawString(text, 0, 2 * font.getSize());// 画出第二行字符串，注意y轴坐标需要变动
		g.dispose();
		ImageIO.write(image, "png", outFile);// 输出png图片
	}
}

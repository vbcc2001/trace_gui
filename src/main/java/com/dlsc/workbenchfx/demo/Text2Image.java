package com.dlsc.workbenchfx.demo;

//import com.sdz.code.TwoDimensionCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;




public class Text2Image {


	/**
	 * 获取字体高度
	 * @param font
	 * @return
	 */

	/**
	 * 先把整个标签做成图像缓存
	 * @throws Exception
	 */
	public static  BufferedImage createTextImage() throws Exception {
		
		Font font1 = new Font("黑体", Font.BOLD, 36);//标题字体
		Font font2 = new Font("宋体", Font.BOLD, 28);//内容字体
		Font font3 = new Font("宋体", Font.BOLD, 24);//编号字体
		
		// 创建图片
		BufferedImage image = new BufferedImage(480, 320, BufferedImage.TYPE_INT_BGR);// 创建图片画布
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE); // 先用白色填充整张图片,也就是背景
		g.fillRect(0, 0, 480, 320);// 画出矩形区域，以便于在矩形区域内写入文字
		g.setColor(Color.black);// 再换成黑色，以便于写入文字
		g.setFont(font1);// 设置画笔字体
		g.drawString("样品标识", 120, 42);// 画出一行字符串
		g.setFont(font2);
		g.drawString("样品编号:", 5, 74);
		g.setFont(font3);
		g.drawString("SGSIL-18/03/29-001-1-20", 5, 106);
		g.setFont(font2);
		g.drawString("委托编号:", 5, 140); 
		g.setFont(font3);
		g.drawString("CEPR1-SGSIL-2018-9999", 5, 170);
		
		
		g.setFont(font2);
		g.drawString("样品名称:一二三四五六七八九十一", 5, 240); 
		
		
		g.drawString("待检", 10, 300);
		g.drawRect(70, 279, 40, 25);
		g.drawString("待检", 115, 300);
		g.drawRect(175, 279, 40, 25);
		g.drawString("待检", 220, 300);
		g.drawRect(280, 279, 40, 25);
		g.drawString("待检", 325, 300);
		g.drawRect(385, 279, 40, 25);
		
		g.dispose();  
		
		return image;
	}
}


package com.dlsc.workbenchfx.demo;

//import com.sdz.code.TwoDimensionCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;




public class ImageProducerUtil {

	public static void main(String[] args) throws Exception {
		new ImageProducerUtil().createImage();
	}
	

	/**
	 * 获取字体高度
	 * @param font
	 * @return
	 */
	//private static int getFontHeight(Font font) {
	//	return sun.font.FontDesignMetrics.getMetrics(font).getHeight();
	//}


	/**
	 * 先把整个标签做成图像缓存
	 * @throws Exception
	 */
	public static  BufferedImage createImage() throws Exception {
		
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
		
		
		//二维码
		
		//TwoDimensionCode handler = new TwoDimensionCode();
        String encoderContentStr = "样品编号：TX2-18/03/29-003-123456-123\n委托编号：CEPR1-TX2-2018-130-1234567";
		//BufferedImage codeImg = handler.encoderQRCode(encoderContentStr, "png", 8);  
		//g.drawImage(codeImg, 343, 40, codeImg.getWidth(), codeImg.getHeight(), null);  
		
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

	public static  BufferedImage createImage1() throws Exception {

		Font font1 = new Font("黑体", Font.BOLD, 36);//标题字体
		Font font2 = new Font("方正兰亭黑简体", Font.BOLD, 24);//内容字体
		Font font3 = new Font("宋体", Font.BOLD, 24);//编号字体

		int w = 300*38, h = 300*12;
		// 创建图片
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);// 创建图片画布
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE); // 先用白色填充整张图片,也就是背景
		g.fillRect(0, 0, w, h);// 画出矩形区域，以便于在矩形区域内写入文字
		g.setColor(Color.black);// 再换成黑色，以便于写入文字
		g.setFont(font3);// 设置画笔字体
		g.drawString("米家电动剃须刀 S101", 10, 30);// 画出一行字符串
		//g.setFont(font2);


		//二维码
		//TwoDimensionCode handler = new TwoDimensionCode();
		//String encoderContentStr = "样品编号：TX2-18/03/29-003-123456-123\n委托编号：CEPR1-TX2-2018-130-1234567";
		//BufferedImage codeImg = handler.encoderQRCode(encoderContentStr, "png", 9);
		//g.drawImage(codeImg, 40, 50, codeImg.getWidth(), codeImg.getHeight(), null);
		g.dispose();

		return image;
	}
	
}

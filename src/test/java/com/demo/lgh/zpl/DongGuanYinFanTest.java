package com.demo.lgh.zpl;

import com.dlsc.workbenchfx.demo.DongGuanYinFan;

import java.awt.*;

import org.junit.jupiter.api.Test;

/**
 * 江西乐丰标签打印Demo
 * @author LiangGuanHao
 *
 */
public class DongGuanYinFanTest {
	public static void main(String[] args) {
		GraphicsEnvironment grapEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNameList = grapEnv.getAvailableFontFamilyNames();

		for(String fontName : fontNameList){

			System.out.println(fontName);

		}
		StringBuilder sb=new StringBuilder();
		Integer i=-10;
		sb.append("^MD").append(i);
		System.out.println(sb);
	}

	@Test
	public void colorBox() {//彩盒标签 42*28mm 600dpi
		System.out.println(DongGuanYinFan.ColorBox(true, 300, "40088/AFAGWF2SE00001"));
	}

}

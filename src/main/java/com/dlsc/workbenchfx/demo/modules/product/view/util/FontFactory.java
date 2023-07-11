package com.dlsc.workbenchfx.demo.modules.product.view.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

public class FontFactory {
	
	private PdfFont lanTingSimHei;//兰亭黑简
	public PdfFont getLanTingSimHei() throws IOException, URISyntaxException {
		if(lanTingSimHei==null) {
			URL heitiUrl=FontFactory.class.getResource("/fonts/lanting/SIMHEI.TTF");
			FontProgram program=FontProgramFactory.createFont(new File(heitiUrl.toURI()).getAbsolutePath());
			lanTingSimHei=PdfFontFactory.createFont(program);
		}
		return lanTingSimHei;
	}
	private PdfFont lanTingZhunHei;
	
	public PdfFont getLanTingZhunHei() throws IOException, URISyntaxException {
		if(lanTingZhunHei==null) {
			URL heitiUrl=FontFactory.class.getResource("/fonts/lanting/ZHUNHEI.TTF");
			FontProgram program=FontProgramFactory.createFont(new File(heitiUrl.toURI()).getAbsolutePath());
			lanTingZhunHei=PdfFontFactory.createFont(program);
		}
		return lanTingZhunHei;
	}
	private PdfFont heiti;
	public PdfFont getHeiTi() throws IOException, URISyntaxException {
		if(heiti==null) {
			//加载黑体
			URL heitiUrl=FontFactory.class.getResource("/fonts/heiti/SIMHEI.TTF");
			FontProgram program=FontProgramFactory.createFont(new File(heitiUrl.toURI()).getAbsolutePath());
			heiti=PdfFontFactory.createFont(program);
		}
		return heiti;
	}
}

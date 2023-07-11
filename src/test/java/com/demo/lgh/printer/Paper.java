package com.demo.lgh.printer;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Table;

public interface Paper {
	/**
	 * 
	 * @return 打印纸DPI
	 */
	int getDPI();
	/**
	 * 
	 * @return 打印纸宽度,单位mm
	 */
	float getWidth();
	/**
	 * 
	 * @return 打印纸高度,单位mm
	 */
	float getHeight();
	/**
	 * 
	 * @return 打印纸是否需要旋转
	 */
	boolean isRotate();
	Table drawTable(PdfDocument pdfDoc) throws Exception;
}

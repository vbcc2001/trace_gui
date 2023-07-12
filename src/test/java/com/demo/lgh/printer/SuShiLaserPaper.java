package com.demo.lgh.printer;

import java.io.IOException;
import java.net.URISyntaxException;

import com.dlsc.workbenchfx.demo.modules.product.view.util.FontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
/**
 * 素士产品镭射标签纸
 * @author LiangGuanHao
 *
 */
public class SuShiLaserPaper implements Paper{
	private final int dpi;
	private final float width;
	private final float height;
	private final FontFactory fontFactory=new FontFactory();
	private final float fontSize;
	
	public SuShiLaserPaper(int dpi, float width, float height,float fontSize) {
		super();
		this.dpi = dpi;
		this.width = width;
		this.height = height;
		this.fontSize=fontSize;
	}
	@Override
	public int getDPI() {
		return dpi;
	}
	@Override
	public float getWidth() {
		return width;
	}
	@Override
	public float getHeight() {
		return height;
	}
	@Override
	public boolean isRotate() {
		return false;
	}
	@Override
	public Table drawTable(PdfDocument pdfDoc) throws Exception {
		addPO("米家电动剃须刀 S101");
		return getTable();
	}
	private Table table;
	private Table getTable() throws IOException, URISyntaxException {
		if(table==null) {
			table=new Table(2).useAllAvailableWidth();
			table.setFont(fontFactory.getHeiTi()).setFontSize(fontSize);
			table.setTextAlignment(TextAlignment.CENTER);
		}
		return table;
	}
	private void addPO(String po) throws IOException, URISyntaxException {//采购订单号
		Cell keyCell=new Cell().add(new Paragraph("采购订单号\r\nP.O."));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//		keyCell.setWidth(width[1]).setHeight(height[0]);
//		keyCell.setWidth(UnitValue.createPercentValue(10));
		getTable().addCell(keyCell);
		Cell valCell=new Cell().add(new Paragraph(po));
//		valCell.setWidth(UnitValue.createPercentValue(20));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//		valCell.setWidth(width[2]).setHeight(height[0]);
		getTable().addCell(valCell);
	}
	public static void main(String[] args) throws Exception {
		
		Paper paper=new SuShiLaserPaper(300, 38, 12, 12);
//		Paper paper=new SuShiLaserPaper(600, 297, 210, 24);
		IText7Printer printer=new IText7Printer(paper);
		String serviceName = "ZDesigner ZD420-300dpi ZPL (副本 1)";
//		serviceName="Microsoft Print to PDF";s
		printer.printPaper(serviceName);
	}
}

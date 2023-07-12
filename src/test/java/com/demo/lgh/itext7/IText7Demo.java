package com.demo.lgh.itext7;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.dlsc.workbenchfx.demo.modules.product.view.util.FontFactory;
import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

public class IText7Demo {
//	private final float tableWidth;//表格宽度,单位UnitValue,
//	private final float tableHeight;
	private final float fontSize;
	private final float[] colRems=new float[5];
	private final float rowPer=10;//分10行，每行10%
//	private final int rowRem;//每行宽度，共10行
	private Table table;
	private final FontFactory fontFactory=new FontFactory();
	public IText7Demo(float fontSize) {
		super();
		this.fontSize=fontSize;
		//A4纸横向打印297mm*210mm 约11.69英寸*8.27英寸
//		this.tableHeight = this.tableWidth = 100;//A4纸宽度定义约2px/mm 约
		colRems[1]=10;
		colRems[3]=colRems[2]=colRems[0]=20;
		colRems[4]=30;
	}
	private PdfWriter writer;
	private PdfDocument pdfDoc;
	private void init(final File dest) throws IOException, URISyntaxException {
		writer=new PdfWriter(dest);
		pdfDoc=new PdfDocument(writer);
		pdfDoc.addFont(fontFactory.getHeiTi());
		pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
	}
	private void printTable(Table table) {
		try(Document doc=new Document(pdfDoc,PageSize.A4.rotate());){//A4纸横向打印297mm*210mm 约11.69英寸*8.27英寸
			doc.add(table);
		}
	}
	private Table createTable() {//生成5列表格
		table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth().setHeight(UnitValue.createPercentValue(100));
		return table;
	}
	/**
	 * 
	 * @param row 行数
	 * @param col 列数
	 * @param startPos 起始列,取值范围[1,5]
	 * @return 单元格
	 */
	private Cell createCell(int row,int col,int startPos) {
		Cell cell=new Cell(row, col);
		if(startPos<1||startPos>5) 
			throw new IndexOutOfBoundsException("startPos取值范围[1,5]");
		float percent=0;
		for(int i=0;i<col;i++) {
			percent+=colRems[startPos-1+i];
		}
		cell.setWidth(UnitValue.createPercentValue(percent));
		cell.setHeight(UnitValue.createPercentValue(rowPer*row));
		return cell;
	}
	public static void main(String[] args) throws URISyntaxException, IOException {
		final float fontSize=16.0f;//用于计算边距
		final IText7Demo demo=new IText7Demo(fontSize);
	
		URL root=demo.getClass().getResource("");
		Path rootPath=Paths.get(root.toURI());
		final File file=rootPath.resolve("test.pdf").toFile();
		demo.init(file);
		
		final URL logoUrl=demo.getClass().getResource("/wutian.jpg");
		final ImageData logoData=ImageDataFactory.create(logoUrl);
		final Image logo=new Image(logoData).setAutoScale(true);
		
		Table table = demo.createTable();
		Cell c11=demo.createCell(2, 1, 1).add(logo);
		table.addCell(c11);
		
		//采购订单号
		demo.addSimpleKeyValue("采购订单号\r\nP.O.", "4500123456",2);
		demo.addSimpleKeyValue("产品SKU", "BHR6726CN",4);
		demo.addBarcodeValue("产品ID", "43563",1,1,1,2);
		demo.addBarcodeValue("生产日期", "20220616", 1,1,1,4);
		demo.addSimpleKeyValue("产品名称", "米家电动剃须刀 S101",1,1,2,1);
		demo.addSimpleKeyValue("装托数量", "875 PCS/PLT",4);
		demo.addSimpleKeyValue("托盘毛重\r\n(kg)", "0.5kg",2,1,1,1);
		demo.addBarcodeValue("托盘号", "PBHR6726CN28A000001AJK",2,1,2,3);
		
		List<String> codes=new ArrayList<>();
		int serial=2800001;
		for(int i=0;i<35;i++) {
			codes.add("MZXHBHR6726CN"+(serial++)+"AJK");
		}
		Cell comment=demo.createCell(5, 1, 1).add(demo.createParagraph("备注")).setVerticalAlignment(VerticalAlignment.MIDDLE);
		table.addCell(comment);
//		String commentValue=String.join("\t", codes);
		StringBuilder commentValue=new StringBuilder();
		int counter=0;
		int line=0;//36/3=12行，不足自动补齐
		for(String code:codes) {
			if(counter==3) {
				counter=0;
				line++;
				commentValue.append("\t\r\n");
			}
			commentValue.append("\t").append(code);
			counter++;	
		}
		while(line<12) {
			commentValue.append("\r\n");
			line++;
		}
		Cell c=demo.createCell(5, 4, 2).add(demo.createParagraph(commentValue.toString()).setTextAlignment(TextAlignment.LEFT).setMarginLeft(fontSize*3)).setVerticalAlignment(VerticalAlignment.MIDDLE);
//		c.setHorizontalAlignment(HorizontalAlignment.LEFT);
		table.addCell(c);
		
		
		demo.printTable(table);
		System.out.println("输出文件路径:"+file.getAbsolutePath());
	}
	/**
	 * 
	 * @param keyText key 
	 * @param valueText value
	 * @param row 行数
	 * @param keyCol key列数
	 * @param valCol value列数
	 * @param startPos 起始列,[1,5]
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void addBarcodeValue(String keyText,String valueText,int row,int keyCol,int valCol,int startPos) throws IOException, URISyntaxException {
		Cell name=createCell(row, keyCol, startPos).add(createParagraph(keyText)).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER);		
		table.addCell(name);
		Cell value=createCell(row, valCol, startPos+keyCol);
		Barcode128 barcode=createBarcode(pdfDoc, valueText);
		Image image=new Image(barcode.createFormXObject(pdfDoc)).scale(0.75f, 0.75f);	
		value.add(image).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER);
		image.setWidth(UnitValue.createPercentValue(100));
		image.setHeight(UnitValue.createPercentValue(100));
		value.setPaddingLeft(fontSize);
		value.setPaddingTop(fontSize*0.5f);
//		image.setMarginLeft(fontSize);
//		image.setMarginRight(fontSize);
//		image.setPaddingTop(0.5f*fontSize);
//		image.setPaddingBottom(0.5f*fontSize);
//		System.out.println("imageWidth:"+image.getStrokeWidth());
//		float rMargin=getPointWidth(2, fontSize);
//		value.setMargins(rMargin/2, rMargin, rMargin/2, rMargin);
//		value.setWidth(getPointWidth(valFontCount, fontSize));
		table.addCell(value);
	}
	/**
	 * 
	 * @param keyText key值
	 * @param valueText value值
	 * @param row 行数
	 * @param keyCol key显示列数
	 * @param valCol value显示列数
	 * @param startPos key的起始列，取值[1,5]
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void addSimpleKeyValue(String keyText,String valueText,int row,int keyCol,int valCol,int startPos) throws IOException, URISyntaxException {
		Cell name=createCell(row, keyCol, startPos).add(createParagraph(keyText)).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER);
		table.addCell(name);		
		Cell value=createCell(row, valCol, startPos+keyCol).add(createParagraph(valueText)).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER);
		table.addCell(value);
	}
	/**
	 * 
	 * @param keyText key
	 * @param valueText value
	 * @param startPos key的起始列,取值[1,5]
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void addSimpleKeyValue(String keyText,String valueText,int startPos) throws IOException, URISyntaxException {
		addSimpleKeyValue(keyText, valueText, 1, 1, 1,startPos);
	}
	
	private Paragraph createParagraph(String text) throws IOException, URISyntaxException {
		Paragraph p=new Paragraph(text);
		p.setFont(fontFactory.getHeiTi());
		p.setTextAlignment(TextAlignment.CENTER);
//		p.setVerticalAlignment(VerticalAlignment.MIDDLE);
//		p.setPaddingTop(2);
//		p.setPaddingBottom(2);	
		return p;
	}
	private Barcode128 createBarcode(PdfDocument pdfDoc,String code) {
		Barcode128 barcode = new Barcode128(pdfDoc);
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(code);
        return barcode;
	}
}

package com.dlsc.workbenchfx.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.dlsc.workbenchfx.demo.modules.palletSN.model.PalletSN;
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

public class XiaoMiPalletDemo {
	private final float fontSize;
	private final String targetPath;
	private final PageSize pageSize;
	private final FontFactory fontFactory=new FontFactory();

	public XiaoMiPalletDemo(float fontSize, String targetPath) {
		super();
		this.fontSize = fontSize;
		this.targetPath = targetPath;
		this.pageSize=PageSize.A4.rotate();//横向A4纸,297*210mm
	}

	private PdfWriter writer;
	private PdfDocument pdfDoc;

	private PdfWriter getWriter() throws FileNotFoundException {
		if (writer == null) {
			writer = new PdfWriter(targetPath);
		}
		return writer;
	}

	private PdfDocument getPdfDoc() throws FileNotFoundException {
		if (pdfDoc == null) {
			pdfDoc = new PdfDocument(getWriter());
			pdfDoc.setDefaultPageSize(pageSize);// A6纸 105*148
		}
		return pdfDoc;
	}
	
	private Table table;
	private Table getTable() throws IOException, URISyntaxException {
		if(table==null) {
			table=new Table(5).useAllAvailableWidth();
			table.setFont(fontFactory.getHeiTi()).setFontSize(fontSize);
			table.setTextAlignment(TextAlignment.CENTER);
		}
		return table;
	}
	
	private void addLogo(String logoPath) throws IOException, URISyntaxException {
		final URL url=getClass().getResource(logoPath);
		final ImageData logoData=ImageDataFactory.create(url);
		final Image logo=new Image(logoData);
		Cell cell=new Cell(2,1).add(logo);
		cell.setWidth(UnitValue.createPercentValue(15));
		getTable().addCell(cell);
	}
	private void addPO(String po) throws IOException, URISyntaxException {//采购订单号
		Cell keyCell=new Cell().add(new Paragraph("采购订单号\r\nP.O."));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(UnitValue.createPercentValue(10));
		getTable().addCell(keyCell);
		Cell valCell=new Cell().add(new Paragraph(po));
		valCell.setWidth(UnitValue.createPercentValue(20));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(valCell);
	}
	private void addSKU(String sku) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("产品SKU"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(UnitValue.createPercentValue(15));
		getTable().addCell(keyCell);
		Cell valCell=new Cell().add(new Paragraph(sku));
		valCell.setWidth(UnitValue.createPercentValue(40));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(valCell);
		
	}
	
	private void addProductId(String id) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("产品ID"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setHeight(UnitValue.createPercentValue(5));
		getTable().addCell(keyCell);
		Barcode128 barcode = new Barcode128(getPdfDoc());
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(id);
        Image image=new Image(barcode.createFormXObject(getPdfDoc())).scale(2.0f, 1.5f);
//        image.setWidth(UnitValue.createPercentValue(100));
//        image.setMargins(5, 10, 5, 10);
        Cell cell=new Cell().add(image);
        cell.setPaddings(5, 15, 5, 15);
        cell.setHeight(UnitValue.createPercentValue(5));
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		getTable().addCell(cell);
	}
	
	private void addCreateDate(String date) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("生产日期"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setHeight(UnitValue.createPercentValue(5));
		getTable().addCell(keyCell);
		Barcode128 barcode = new Barcode128(getPdfDoc());
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(date);
        Image image=new Image(barcode.createFormXObject(getPdfDoc())).scale(2.5f, 1.5f);
//        image.setWidth(UnitValue.createPercentValue(100));
        Cell cell=new Cell().add(image);
        cell.setPaddings(5, 15, 5, 15);
        cell.setHeight(UnitValue.createPercentValue(5));
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		getTable().addCell(cell);
	}
	
	private void addProductName(String name) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("产品名称"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(keyCell);
		Cell valCell=new Cell(1,2).add(new Paragraph(name));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(valCell);
	}
	
	private void addCount(String count) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("装托数量"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(keyCell);
		Cell valCell=new Cell().add(new Paragraph(count));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(valCell);
	}
	
	private void addGrossWeight(String weight) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("托盘毛重\r\n(kg)"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(keyCell);
		Cell valCell=new Cell().add(new Paragraph(weight));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(valCell);
	}
	
	private void addPalletCode(String code) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("托盘号"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(keyCell);
		Cell valCell=new Cell(1,2).add(new Paragraph(code));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(valCell);
	}
	
	private void addComment(String[] codes) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("备注"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(keyCell);
		Cell valCell=new Cell(1,4).add(toTable(codes));
		valCell.setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(valCell);
	}
	
	private Table toTable(String[] codes) throws IOException, URISyntaxException {
		Table t=new Table(3);//12行3列的固定子表
		t.setFont(fontFactory.getHeiTi()).setFontSize(fontSize).useAllAvailableWidth();
		t.setTextAlignment(TextAlignment.CENTER);
		t.setWidth(UnitValue.createPercentValue(100));
		for(int i=0;i<36;i++) {
			Cell cell=new Cell();
			if(i>=codes.length) {
				cell.add(new Paragraph("\n").setMultipliedLeading(1.2f));
			}else {
				cell.add(new Paragraph(codes[i]));
			}			
			cell.setBorder(null);
			t.addCell(cell);
		}
		return t;
	}
	
//	private String toComment(List<String> codes) {
//		StringBuilder commentValue=new StringBuilder();
//		int counter=0;
//		int line=0;//36/3=12行，不足自动补齐
//		for(String code:codes) {
//			if(counter==3) {
//				counter=0;
//				line++;
//				commentValue.append("\t\r\n");
//			}
//			commentValue.append("\t").append(code);
//			counter++;	
//		}
//		while(line<12) {
//			commentValue.append("\r\n");
//			line++;
//		}
//		return commentValue.toString();
//	}
	
	public void printPDF(String boxBarCodeStrs, PalletSN pallet) throws IOException, URISyntaxException {
		try(Document doc=new Document(getPdfDoc());){
			addLogo("/xiaomi.png");
			addPO(pallet.getPO());
			addSKU(pallet.getSKU());
			addProductId(pallet.getProductId());
			addCreateDate(pallet.getCreateDate());
			addProductName(pallet.getProductName());
			addCount(pallet.getQuantity());
			addGrossWeight(pallet.getGrossWeight());
			addPalletCode(pallet.getNickName());
			String[] boxBarcodes = boxBarCodeStrs.split(";");
//			List<String> codes=new ArrayList<>();
//			int serial=2800001;
//			for(int i=0;i<35;i++) {
//				codes.add("MZXHBHR6726CN"+(serial++)+"AJK");
//			}
			addComment(boxBarcodes);
			doc.add(getTable());
		}
	}
}

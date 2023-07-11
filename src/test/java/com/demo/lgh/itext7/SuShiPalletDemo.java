package com.demo.lgh.itext7;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import com.dlsc.workbenchfx.demo.modules.product.view.util.FontFactory;
import com.itextpdf.barcodes.Barcode128;
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

public class SuShiPalletDemo {
	private final float fontSize;
	private final String targetPath;
	private final PageSize pageSize;
	private final FontFactory fontFactory=new FontFactory();
	
	public SuShiPalletDemo(float fontSize, String targetPath) {
		super();
		this.fontSize = fontSize;
		this.targetPath = targetPath;
		this.pageSize=PageSize.A4.rotate();//横向A4纸
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
	private Table getTable() throws IOException, URISyntaxException {//生成3列表格
		if(table==null) {
			table=new Table(1);
			table.setCharacterSpacing(1f).useAllAvailableWidth()
			.setFontSize(fontSize).setFont(fontFactory.getHeiTi())
			.setTextAlignment(TextAlignment.CENTER).setBorder(null);
		}
		return table;
	}
	
	private void addText(String key,String value) throws IOException, URISyntaxException {
		Cell cell=new Cell().add(new Paragraph(key+":"+value).setMultipliedLeading(1.2f));
		cell.setBorder(null);
//		cell.setHeight(UnitValue.createPercentValue(15));
		getTable().addCell(cell);
	}
	
	private void addBarCode(String code) throws IOException, URISyntaxException {
		Barcode128 barcode = new Barcode128(getPdfDoc());
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(code);
        Image image=new Image(barcode.createFormXObject(getPdfDoc())).scale(1, 1.6f);
        image.setWidth(UnitValue.createPercentValue(80))
        .setHorizontalAlignment(HorizontalAlignment.CENTER);
        Cell cell=new Cell().add(image).setPaddingTop(fontSize);
//        cell.setHeight(UnitValue.createPercentValue(60));
		cell.setBorder(null);
		getTable().addCell(cell);
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		final float fontSize = 24.0f;// 用于计算边距
		final String targetPath = "target/suShiPallet.pdf";
		SuShiPalletDemo demo=new SuShiPalletDemo(fontSize, targetPath);
		try (Document doc=new Document(demo.getPdfDoc());){
			demo.addText("出货地", "道滘");
			demo.addText("目的地", "深圳");
			demo.addBarCode("P0Y11NO1001");
			doc.add(demo.getTable());
		}
	}
}

package com.dlsc.workbenchfx.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
/**
 * 素士彩盒标签打印Demo
 * @author LiangGuanHao
 *
 */
public class SuShiLaserDemo {
	private final String targetPath;
	private final PageSize pageSize;

	public SuShiLaserDemo(String targetPath) {
		super();
		this.targetPath = targetPath;
		this.pageSize=new PageSize(141, 43);//38*12mm/108*35  50*15mm/141*43 
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
			pageSize.applyMargins(1, 10, 1, 10, false);
		}
		return pdfDoc;
	}
	
	private Image createBarCode(String code) throws IOException, URISyntaxException {
		Barcode128 barcode = new Barcode128(getPdfDoc());
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(code);
        Image image=new Image(barcode.createFormXObject(getPdfDoc()))
        		.setWidth(UnitValue.createPercentValue(100));
//        image.setBorder(new SolidBorder(1));
        return image;
	}
	
	public static void manipulatePdf(String targetPath, String barcode) throws IOException, URISyntaxException {
		//final String targetPath = "target/suShiLaser.pdf";
		SuShiLaserDemo demo=new SuShiLaserDemo(targetPath);
		//final String barcode="1000000000000009";
		try (Document doc=new Document(demo.getPdfDoc());){
			doc.setMargins(5, 10, 5, 10);
			doc.setHorizontalAlignment(HorizontalAlignment.CENTER);
			doc.add(demo.createBarCode(barcode));
		}
	}
}

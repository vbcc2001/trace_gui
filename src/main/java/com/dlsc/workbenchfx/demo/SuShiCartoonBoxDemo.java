package com.dlsc.workbenchfx.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.dlsc.workbenchfx.demo.modules.product.view.util.FontFactory;
import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
/**
 * 素士中箱标签Demo
 * @author LiangGuanHao
 *
 */
public class SuShiCartoonBoxDemo {
	private final float fontSize;
	private final String targetPath;
	private final PageSize pageSize;
	private final FontFactory fontFactory=new FontFactory();

	public SuShiCartoonBoxDemo(float fontSize, String targetPath) {
		super();
		this.fontSize = fontSize;
		this.targetPath = targetPath;
		this.pageSize=new PageSize(298, 270);//105mm*95mm
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
	private Table mainTable;//主表 3行2列
	private Table ltTable;//左上表 5行2列
	private Table getLTTable() {
		if(ltTable==null) {
			ltTable=new Table(2);
			ltTable.setKeepTogether(true).setKeepWithNext(true);
		}
		return ltTable;
	}
	private Table getMainTable() {//生成3列表格
		if(mainTable==null) {
			mainTable=new Table(2);
			mainTable.setCharacterSpacing(1f).useAllAvailableWidth();
		}
		return mainTable;
	}

	private void addLogo(String logoPath) throws MalformedURLException {
		final URL url=getClass().getResource(logoPath);
		final ImageData logoData=ImageDataFactory.create(url);
		final Image logo=new Image(logoData).scale(0.75f, 0.5f);
		Cell header=new Cell(1, 2);
		header.setBorder(null);
		header.add(logo);	
		getMainTable().addHeaderCell(header);
	}
	private void addLTTable(String[] keys,String[] values) throws IOException, URISyntaxException {
		Table t=getLTTable();
		t.setFont(fontFactory.getLanTingSimHei());
		t.setFontSize(fontSize).useAllAvailableWidth();
		for(int i=0;i<keys.length;i++) {
			Cell keyCell=new Cell().add(new Paragraph(keys[i]).setMultipliedLeading(0.8f));//1倍行间距	
			keyCell.setBorder(null);
			keyCell.setWidth(UnitValue.createPercentValue(50));
			t.addCell(keyCell);
			Cell valueCell=new Cell().add(new Paragraph(values[i]).setMultipliedLeading(1));//1倍行间距
			valueCell.setBorder(null);
			t.addCell(valueCell);
		}
		Cell cell=new Cell().add(t);
		cell.setPadding(0);
//		cell.setMargin(0);
		cell.setWidth(UnitValue.createPercentValue(60));
		cell.setBorder(null);
		cell.setBorderBottom(new SolidBorder(DeviceRgb.BLACK, 1));
		getMainTable().addCell(cell);
	}
	private void addQRCode(String content) throws FileNotFoundException {
		Map<EncodeHintType, Object> hints=new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		var codeObject = new BarcodeQRCode(content,hints);
		PdfFormXObject codeFormObject = codeObject.createFormXObject(getPdfDoc());
		Image codeImage = new Image(codeFormObject);
		codeImage.scale(2.2f, 2.2f);
		codeImage.setMarginTop(0).setMarginBottom(0);
		codeImage.setPadding(0);
		codeImage.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		Cell cell=new Cell().add(codeImage).setWidth(UnitValue.createPercentValue(40));
		cell.setPadding(0);
		cell.setMargin(0);
//		cell.setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP);
		cell.setBorder(null);
		cell.setBorderBottom(new SolidBorder(DeviceRgb.BLACK, 1));
		getMainTable().addCell(cell);
	}
	private void addCreateDate(String date) throws IOException, URISyntaxException {
		Cell cell=new Cell(1,2).add(new Paragraph("生产日期: "+date).setMultipliedLeading(1));
		cell.setFont(fontFactory.getLanTingZhunHei());
		cell.setBorder(null);
		getMainTable().addCell(cell);
	}
	
	private void addMidTable(String[] keys,String[] values) throws IOException, URISyntaxException {
		boolean isOdd=false;//索引是否为奇数
		Table t=new Table(2).setFont(fontFactory.getLanTingZhunHei()).useAllAvailableWidth().setFontSize(fontSize);
		final UnitValue p_odd=UnitValue.createPercentValue(60);
		final UnitValue p_even=UnitValue.createPercentValue(40);
		for(int i=0;i<keys.length;i++) {
			Cell cell=new Cell(1, 1);
			cell.setFont(fontFactory.getLanTingZhunHei());
			if(isOdd) {
				cell.setWidth(p_odd);
				isOdd=false;
			}else {
				cell.setWidth(p_even);
				isOdd=true;
			}
			cell.setPadding(0).setMargin(0).setVerticalAlignment(VerticalAlignment.MIDDLE);
			cell.add(new Paragraph(keys[i]+"  "+values[i]).setMultipliedLeading(0.8f));//行间距0.5倍
			cell.setBorder(null);
			t.addCell(cell);
		}
		Cell tableCell=new Cell(1,2).add(t);
		tableCell.setBorder(null).setWidth(UnitValue.createPercentValue(100));
		getMainTable().addCell(tableCell);
	}
	
	private void addBarCode(String code) throws FileNotFoundException {
		Barcode128 barcode = new Barcode128(getPdfDoc());
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(code);
        Image image=new Image(barcode.createFormXObject(getPdfDoc())).scale(1f, 0.5f);
        image.setWidth(UnitValue.createPercentValue(100));
        Cell cell=new Cell(1,2).add(image);
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		cell.setWidth(UnitValue.createPercentValue(100));
		cell.setBorder(null);
		getMainTable().addCell(cell);
	}
	
	public static void manipulatePdf(String targetPath, String productName, String productType, String productColor, String productPartNumber
            ,String date,String QTY,String DIM, String NW, String GW, String barCode
            ) throws IOException, URISyntaxException {
		final float fontSize = 10.0f;// 用于计算边距
		//final String targetPath = "target/cartoonBox.pdf";
		SuShiCartoonBoxDemo cbi = new SuShiCartoonBoxDemo(fontSize, targetPath);
		try (Document doc=new Document(cbi.getPdfDoc());){
			cbi.addLogo("/sushi.jpg");
			
			String[] keys=new String[] {"品名:","型号:","颜色:","料号"};
			String[] values=new String[] {productName,productType,productColor,productPartNumber};		
			cbi.addLTTable(keys,values);
			StringBuilder content=new StringBuilder();
			for(int i=0;i<keys.length;i++) {
				content.append(keys[i]).append(" ").append(values[i]).append("\n");
			}
//			Log.info(content.toString());
			cbi.addQRCode(content.toString());
			cbi.addCreateDate(date);
			
			keys=new String[] {"QTY:","DIM:","N.W:","G.W:"};
			values=new String[] {QTY,DIM,NW,GW};	
			
			cbi.addMidTable(keys, values);
			
			cbi.addBarCode(barCode);
			doc.add(cbi.getMainTable());
		}

	}
}

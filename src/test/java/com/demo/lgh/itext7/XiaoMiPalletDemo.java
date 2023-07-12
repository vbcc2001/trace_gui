package com.demo.lgh.itext7;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

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
import com.itextpdf.layout.properties.VerticalAlignment;
/**
 * 小米栈板标签打印，打印机分辨率300dpi,标签尺寸自测约99.2*69.1mm,按97.4*69.1mm计算(相当于A4纸按比例缩放)<br>
 * 1英寸=25.4mm,1mm像素宽度约11.8;计算打印纸的像素宽度为300*97.4/25.4=1150.39,像素高为300*69.1/25.4=816.14<br>
 * 标签共5列，左侧3mm(35.43),右侧2mm(23.62),像素宽依次为283.46(24mm)+193.7(16.4mm)+200.79(17mm)*2+212.60(18mm)<br>
 * 标签共5行，顶部2mm(23.62),底部3mm(35.43),像素高依次为141.73(12mm)*4+190.17(16.1mm)  
 * @author LiangGuanHao
 *
 */
public class XiaoMiPalletDemo {
	private final float fontSize;
	private final String targetPath;
	private final PageSize pageSize;
	private final float borderWidth=16;
	private final float[] width=new float[]{283.46f-borderWidth,193.70f-borderWidth,200.79f-borderWidth,200.79f-borderWidth,212.60f-borderWidth};//border按0.5mm计算
	private final float[] height=new float[] {141.73f-borderWidth,141.73f-borderWidth,141.73f-borderWidth,141.73f-borderWidth,190.17f-borderWidth};
//	private final int dpi=300;
//	private final float width=97.4f;
//	private final float height=69.1f;
	private final FontFactory fontFactory=new FontFactory();

	public XiaoMiPalletDemo(float fontSize, String targetPath) {
		super();
		this.fontSize = fontSize;
		this.targetPath = targetPath;
		this.pageSize=new PageSize(1150.39f, 816.14f);//文档需求是横向A4纸,297*210mm,实际生产是99.2*69.1mm,按98*69计算s
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
			table=new Table(width);
			table.setFont(fontFactory.getHeiTi()).setFontSize(fontSize);
			table.setTextAlignment(TextAlignment.CENTER);
		}
		return table;
	}
	
	private void addLogo(String logoPath) throws IOException, URISyntaxException {
		final URL url=getClass().getResource(logoPath);
		final ImageData logoData=ImageDataFactory.create(url);
		final Image logo=new Image(logoData).setWidth(width[0]-24).setHeight(width[0]-24);
		Cell cell=new Cell(2,1).add(logo);
		cell.setPadding(0);
		cell.setWidth(width[0]).setHeight(height[0]+height[1]);
//		cell.setWidth(UnitValue.createPercentValue(15));
		getTable().addCell(cell);
	}
	private void addPO(String po) throws IOException, URISyntaxException {//采购订单号
		Cell keyCell=new Cell().add(new Paragraph("采购订单号\r\nP.O."));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(width[1]).setHeight(height[0]);
//		keyCell.setWidth(UnitValue.createPercentValue(10));
		getTable().addCell(keyCell);
		Cell valCell=new Cell().add(new Paragraph(po));
//		valCell.setWidth(UnitValue.createPercentValue(20));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		valCell.setWidth(width[2]).setHeight(height[0]);
		getTable().addCell(valCell);
	}
	private void addSKU(String sku) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("产品SKU\nProduct SKU"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(width[3]).setHeight(height[0]);
//		keyCell.setWidth(UnitValue.createPercentValue(15));
		getTable().addCell(keyCell);
		Cell valCell=new Cell().add(new Paragraph(sku));
//		valCell.setWidth(UnitValue.createPercentValue(40));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		valCell.setWidth(width[4]).setHeight(height[0]);
		getTable().addCell(valCell);
		
	}
	
	private void addProductId(String id) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("产品ID"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(width[1]).setHeight(height[1]);
//		keyCell.setHeight(UnitValue.createPercentValue(5));
		getTable().addCell(keyCell);
		Barcode128 barcode = new Barcode128(getPdfDoc());
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(id);
        Image image=new Image(barcode.createFormXObject(getPdfDoc())).setWidth(width[2]-24).setHeight(height[1]-72);
//        image.setWidth(UnitValue.createPercentValue(100));
//        image.setMargins(5, 10, 5, 10);
        Cell cell=new Cell().add(image);
        cell.setWidth(width[2]).setHeight(height[1]);
        cell.setPaddings(12, 36, 12, 36);
//        cell.setHeight(UnitValue.createPercentValue(5));
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		getTable().addCell(cell);
	}
	
	private void addCreateDate(String date) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("生产日期"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(width[3]).setHeight(height[1]);
//		keyCell.setHeight(UnitValue.createPercentValue(5));
		getTable().addCell(keyCell);
		Barcode128 barcode = new Barcode128(getPdfDoc());
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(date);
        Image image=new Image(barcode.createFormXObject(getPdfDoc())).setWidth(width[4]-24).setHeight(height[1]-72);
//        image.setWidth(UnitValue.createPercentValue(100));
        Cell cell=new Cell().add(image);
        cell.setWidth(width[4]).setHeight(height[1]);
        cell.setPaddings(12, 36, 12, 36);
//        cell.setHeight(UnitValue.createPercentValue(5));
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		getTable().addCell(cell);
	}
	
	private void addProductName(String name) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("产品名称"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(width[0]).setHeight(height[2]);
		getTable().addCell(keyCell);
		Cell valCell=new Cell(1,2).add(new Paragraph(name));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		valCell.setWidth(width[1]+width[2]).setHeight(height[2]);
		getTable().addCell(valCell);
	}
	
	private void addCount(String count) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("装托数量"));
		keyCell.setWidth(width[3]).setHeight(height[2]);
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(keyCell);
		Cell valCell=new Cell().add(new Paragraph(count));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		valCell.setWidth(width[4]).setHeight(height[2]);
		getTable().addCell(valCell);
	}
	
	private void addGrossWeight(String weight) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("托盘毛重\r\n(kg)"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(width[0]).setHeight(height[3]);
		getTable().addCell(keyCell);
		Cell valCell=new Cell().add(new Paragraph(weight));
		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		valCell.setWidth(width[1]).setHeight(height[3]);
		getTable().addCell(valCell);
	}
	
	private void addPalletCode(String code) throws IOException, URISyntaxException {
		Cell keyCell=new Cell().add(new Paragraph("托盘号"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(width[2]).setHeight(height[3]);
		getTable().addCell(keyCell);
		Barcode128 barcode = new Barcode128(getPdfDoc());
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(code);
        final float w=width[3]+width[4];
        Image image=new Image(barcode.createFormXObject(getPdfDoc())).setWidth(w-24).setHeight(height[3]-72);
//        image.setWidth(UnitValue.createPercentValue(100));
        Cell cell=new Cell(1,2).add(image);
        cell.setWidth(w).setHeight(height[3]);
        cell.setPaddings(12, 36, 12, 36);
//        cell.setHeight(UnitValue.createPercentValue(5));
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.RIGHT);
		getTable().addCell(cell);
		
//		
//		Cell valCell=new Cell(1,2).add(new Paragraph(code));
//		valCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//		valCell.setWidth(width[3]+width[4]).setHeight(height[3]);
//		getTable().addCell(valCell);
	}
	private void addComment() throws IOException, URISyntaxException {		
		Cell keyCell=new Cell().add(new Paragraph("备注"));
		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		keyCell.setWidth(width[0]).setHeight(height[4]);
		getTable().addCell(keyCell);
		Cell valCell=new Cell(1,4);
		valCell.setWidth(width[1]+width[2]+width[3]+width[4]).setHeight(height[4]);
		valCell.setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
		getTable().addCell(valCell);
	}
//	private void addComment(List<String> codes) throws IOException, URISyntaxException {
//		if(codes.size()>36)
//			throw new IllegalArgumentException("装箱数量不能超过36");
//		Cell keyCell=new Cell().add(new Paragraph("备注"));
//		keyCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//		getTable().addCell(keyCell);
//		Cell valCell=new Cell(1,4).add(toTable(codes));
//		valCell.setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
//		getTable().addCell(valCell);
//	}
//	
//	private Table toTable(List<String> codes) throws IOException, URISyntaxException {
//		Table t=new Table(3);//4行3列的固定子表
//		t.setFont(fontFactory.getHeiTi()).setFontSize(fontSize).useAllAvailableWidth();
//		t.setTextAlignment(TextAlignment.CENTER);
//		t.setWidth(UnitValue.createPercentValue(100));
//		for(int i=0;i<12;i++) {
//			Cell cell=new Cell();
//			if(i>=codes.size()) {
//				cell.add(new Paragraph("\n").setMultipliedLeading(1.2f));
//			}else {
//				cell.add(new Paragraph(codes.get(i)));
//			}			
//			cell.setBorder(null);
//			t.addCell(cell);
//		}
//		return t;
//	}
	
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
	
	public static void main(String[] args) throws IOException, URISyntaxException, PrintException {
		test();
//		test();
	}
	
	static void test() throws IOException, URISyntaxException, PrintException {
		final float fontSize = 12.0f;// 用于计算边距
		final String targetPath = "target/xiaoMiPallet.pdf";		
		XiaoMiPalletDemo demo=new XiaoMiPalletDemo(fontSize,targetPath);
		try(PdfDocument pdfDoc=demo.getPdfDoc();
				Document doc=new Document(pdfDoc);){
			demo.addLogo("/xiaomi.png");
			demo.addPO("4500123456");
			demo.addSKU("BHR6726CN");
			demo.addProductId("43563");
			demo.addCreateDate("20220616");
			demo.addProductName("米家电动剃须刀 S101");
			demo.addCount("875 PCS/PLT");
			demo.addGrossWeight("0.5kg");
			demo.addPalletCode("PBHR6726CN28A000001AJK");
			
//			List<String> codes=new ArrayList<>();
//			int serial=2800001;
//			for(int i=0;i<35;i++) {
//				codes.add("MZXHBHR6726CN"+(serial++)+"AJK");
//			}
//			demo.addComment(codes);
			demo.addComment();
			doc.add(demo.getTable());
			doc.flush();
		}
		final String serviceName="Microsoft Print to PDF";
		demo.print(serviceName);
	}
	
	private void print(String serviceName) throws PrintException, IOException {
		HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();      
        DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
        PrintService printServices[] = PrintServiceLookup.lookupPrintServices(null, pras);
        for (PrintService printService : printServices) {
//			System.out.println(printService.getSupportedDocFlavors());
			if(serviceName.equals(printService.getName())) {
//				for(DocFlavor f:printService.getSupportedDocFlavors()) {
//					System.out.println("name:"+f.getClass().getName());
//					System.out.println("mediaType:"+f.getMediaType());
//					System.out.println("mime:"+f.getMimeType());
//				}
				PDDocument document = PDDocument.load(new File(targetPath));
				PDFRenderer pdfRenderer = new PDFRenderer(document);
				BufferedImage bim = pdfRenderer.renderImage(0, 1.0f, ImageType.GRAY);//300dpi
				System.out.println("图片宽度:"+bim.getWidth());
				DocPrintJob job = printService.createPrintJob(); // 创建打印作业	             
                DocAttributeSet das = new HashDocAttributeSet(); 
                ImageIO.write(bim, "png", new FileOutputStream("target/test.png"));
                Doc doc = new SimpleDoc(bufferedImageToInputStream(bim), flavor, das);
                job.print(doc, pras);
               
			}
			
		}
	}
	
	InputStream bufferedImageToInputStream(BufferedImage image){
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try {
	        ImageIO.write(image, "png", os);
	        InputStream input = new ByteArrayInputStream(os.toByteArray());
	        return input;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}

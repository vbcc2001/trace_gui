package com.demo.lgh.printer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterResolution;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

public class IText7Printer {
	private Paper paper;

	/**
	 * 
	 * @param width  标签纸宽度
	 * @param height 标签纸高度
	 */
	public IText7Printer(Paper paper) {
		super();
		this.paper = paper;

	}

	private PdfDocument pdfDoc;
	private PdfWriter writer;
	private PageSize pageSize;

	private PageSize getPageSize() {
		if (pageSize == null) {// 1in≈25.4mm
			float w = paper.getWidth() * 72 / 25.4f;
			float h = paper.getHeight() * 72 / 25.4f;
			pageSize = new PageSize(w, h);
			if (paper.isRotate())
				pageSize.rotate();
		}
		return pageSize;
	}

	private ByteArrayOutputStream buffer;

	private PdfWriter getWriter() {
		if (writer == null) {
			buffer = new ByteArrayOutputStream();
			writer = new PdfWriter(buffer);
		}
		return writer;
	}

	private PdfDocument getPdfDoc() {
		if (pdfDoc == null) {
			pdfDoc = new PdfDocument(getWriter());
			pdfDoc.setDefaultPageSize(getPageSize());// A6纸 105*148

		}
		return pdfDoc;
	}

	public void printPaper(String serviceName) throws Exception {
		try (PdfDocument pdfDoc = getPdfDoc(); 
				Document doc = new Document(pdfDoc);) {
			Table table = paper.drawTable(pdfDoc);
			doc.add(table);
			doc.flush();
		}
		byte[] pdfContent = null;
		try {
			pdfContent = buffer.toByteArray();
		} finally {
			try {
				buffer.close();
			} catch (Throwable t) {

			}
		}
		if (pdfContent == null) {
			System.err.println("没有PDF数据");
			return;
		}
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(OrientationRequested.PORTRAIT);
		/* crossFeedResolution 切纸方向的分辨率 feedResolution 走纸方向的分辨率 */
		PrinterResolution resolution = new PrinterResolution(paper.getDPI(), paper.getDPI(), ResolutionSyntax.DPI);
		aset.add(resolution);
//		aset.add(new Copies(1));
//		aset.add(new PageRanges(1));
//		MediaPrintableArea area = new MediaPrintableArea(2, 1, paper.getWidth() - 4, paper.getHeight() - 2,MediaPrintableArea.MM);
//		aset.add(area);
		aset.add(new JobName("My job", null));
		DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
		PrintService service = getService(serviceName, aset, flavor);
		if (service == null) {
			System.err.println("找不到打印机:" + serviceName);
			return;
		}

		try {
			DocPrintJob pj = service.createPrintJob();
			Doc doc = new PrintTask(pdfContent, paper.getDPI(),flavor);
			pj.print(doc, aset);
			System.out.println("已发送打印请求"+pj.toString());
		} catch (Exception pe) {
			System.err.println(pe);
		}
	}

	private PrintService getService(String serviceName, PrintRequestAttributeSet aset, DocFlavor flavor)
			throws FileNotFoundException {

		PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, aset);
		for (int i = 0; i < services.length; i++) {
			PrintService service=services[i];
			if (serviceName.equals(service.getName())) {			
				return service;
			}	
		}
		return null;
	}
}

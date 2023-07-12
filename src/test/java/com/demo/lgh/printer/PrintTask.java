package com.demo.lgh.printer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.attribute.DocAttributeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PrintTask implements Doc{
	private BufferedImage bim;
	private DocFlavor flavor;
	private InputStream in;
	
	public PrintTask(byte[] pdfBytes,int dpi,DocFlavor flavor) throws IOException {
		try(PDDocument document = PDDocument.load(pdfBytes);){
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			bim = pdfRenderer.renderImageWithDPI(0, dpi, ImageType.GRAY);
			//ImageIO.write(bim, "png", new FileOutputStream("target/test.png"));
			this.flavor=flavor;
		}	
	}

	@Override
	public DocFlavor getDocFlavor() {
		return flavor;
	}

	@Override
	public Object getPrintData() throws IOException {
		return getStreamForBytes();
	}

	@Override
	public DocAttributeSet getAttributes() {
		return null;
	}

	@Override
	public Reader getReaderForText() throws IOException {
		return null;
	}

	@Override
	public InputStream getStreamForBytes() throws IOException {
		synchronized (this) {
			if(in==null) {	
			    try(ByteArrayOutputStream os = new ByteArrayOutputStream();) {
			        ImageIO.write(bim, "png", os);
			        in = new ByteArrayInputStream(os.toByteArray());			       
			    } catch (IOException e) {
			        e.printStackTrace();
			    }			
			}
		}
		return in;
	}

//	@Override
//	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//		if (pageIndex == 0) {
////			Graphics2D g2d = (Graphics2D) graphics;
//			System.out.println("wi:"+bim.getWidth());
//			System.out.println("hi:"+bim.getHeight());
//			Paper paper=pageFormat.getPaper();
//			pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
//			System.out.println("w2:"+paper.getWidth());
//			System.out.println("h2:"+paper.getHeight());
//			graphics.setClip(0, 0, (int)paper.getWidth(), (int)paper.getHeight());
//			System.out.println(graphics.getClipBounds());
//			paper.setImageableArea(2, 1, paper.getImageableWidth(), paper.getImageableHeight());
//			System.out.println(paper.getImageableX()+","+paper.getImageableY()+","+paper.getImageableWidth()+","+paper.getImageableHeight());
//			
//			
//			graphics.drawImage(bim, 0, 0, bim.getWidth(), bim.getHeight(), 0, 0, bim.getWidth(), bim.getHeight(), null);
//			
//			//g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
////			g2d.drawImage(bim, null, 0, 0);
//			return Printable.PAGE_EXISTS;
//		} else {
//			return Printable.NO_SUCH_PAGE;
//		}
//	}

}

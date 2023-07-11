package com.dlsc.workbenchfx.demo;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class BarCodes {
    //1mm = 72 ÷ 25.4 ≈ 2.834645...(pt)
    public static final float WIDTH = (float)(2.83 * 168);
    public static final String DEST = "./target/barcodes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        //new BarCodes().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest, String boxBarCodeStr, String productBarCodeStrs, String productName) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();
        //Table table = new Table(UnitValue.createPercentArray(5)).setWidth(WIDTH);

        Cell cell1 = new Cell().add(new Paragraph(""));
		//cell1.setBorder(Border.NO_BORDER);
        cell1.setTextAlignment(TextAlignment.CENTER);
        cell1.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        Cell cell2 = createBarcode(boxBarCodeStr, pdfDoc, 3, 70);
        cell2.setBorder(Border.NO_BORDER);
		//cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell2);

        Cell cell3 = new Cell().add(new Paragraph(productName));
        cell3.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell3.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell3.setTextAlignment(TextAlignment.CENTER);
        cell3.setBorder(Border.NO_BORDER);
        table.addCell(cell3);
      
        String[] productBarCodes = productBarCodeStrs.split(",");
        for (int i = 0; i < productBarCodes.length; i++) {
            String barcode = productBarCodes[i];
            table.addCell(createBarcode(barcode, pdfDoc, 1, 10).setBorder(Border.NO_BORDER));
        }
        table.setNextRenderer(new TableBorderRenderer(table));
        doc.add(table);

        doc.close();
    }

    private static Cell emptyCell(){
        Cell cell = new Cell().add(new Paragraph("hello"));
		// 设置无边框
		cell.setBorder(Border.NO_BORDER);
        cell.setTextAlignment(TextAlignment.CENTER);
        return cell;
    }

    private static Cell createBarcode(String code, PdfDocument pdfDoc, int colspan, int padx) {
        Barcode128 barcode = new Barcode128(pdfDoc);
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(code);

        // Create barcode object to put it to the cell as image
        PdfFormXObject barcodeObject = barcode.createFormXObject(null, null, pdfDoc);
        Cell cell = new Cell(1,colspan).add(new Image(barcodeObject).setAutoScale(true));
        cell.setPaddingTop(10);
        cell.setPaddingRight(padx);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(padx);

        return cell;
    }
    private static class TableBorderRenderer extends TableRenderer {
        public TableBorderRenderer(Table modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TableBorderRenderer((Table) modelElement);
        }

        @Override
        protected void drawBorders(DrawContext drawContext) {
            Rectangle rect = getOccupiedAreaBBox();
            drawContext.getCanvas()
                .saveState()
                .rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight())
                .stroke()
                .restoreState();
        }
    }
}

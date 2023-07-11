package com.dlsc.workbenchfx.demo;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

public class ColorBoxBarCodes {
    //1mm = 72 ÷ 25.4 ≈ 2.834645...(pt)
    public static final float WIDTH = (float)(3 * 42);
    public static final String DEST = "./target/barcodes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        //new BarCodes().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest,String productName, String productColor, String snCode, String SKU, String code69, String createDate) throws Exception {      
        float fontSize = 5.0f;
        String font_path = getClass().getClassLoader().getResource("fonts/heiti/SIMHEI.TTF").getPath();
        PdfFont sysFont = PdfFontFactory.createFont(font_path, PdfEncodings.IDENTITY_H);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        //Table table = new Table(UnitValue.createPercentArray(10)).setWidth(WIDTH);
        //UnitValue[] uvs=new UnitValue[10];
        //for(int i=0;i<10;i++){
        //    uvs[i]=UnitValue.createPercentValue(10);
        //};
        //Table table = new Table(uvs).setWidth(WIDTH);
        Table table = new Table(new float[] {1,1,1,1,1,1,1,1,1,1 }).setWidth(WIDTH);

        Cell cell1 = new Cell(1,6).add(new Paragraph(productName).setFont(sysFont).setFontSize(fontSize));
        cell1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
        //cell1.setBorder(Border.NO_BORDER);
        table.addCell(cell1);

        Cell cell2 = new Cell(1,4).add(new Paragraph("颜色:"+productColor).setFont(sysFont).setFontSize(fontSize));
        cell2.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        cell2.setVerticalAlignment(VerticalAlignment.MIDDLE);
        //cell2.setBorder(Border.NO_BORDER);
        table.addCell(cell2);

        Barcode128 barcode = new Barcode128(pdfDoc);
        barcode.setFont(null);
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode(snCode);
        PdfFormXObject barcodeObject = barcode.createFormXObject(null, null, pdfDoc);
        Cell cell3 = new Cell(1,10).add(new Image(barcodeObject).setAutoScale(false).setWidth(UnitValue.createPercentValue(100)));
        cell3.setPaddingLeft(5);
        cell3.setPaddingRight(5);
        //cell3.setBorder(Border.NO_BORDER);
        table.addCell(cell3);

        Cell cell4 = new Cell(1,6).add(new Paragraph(snCode).setFontSize(fontSize));
        //cell1.setTextAlignment(TextAlignment.CENTER);
        cell4.setHorizontalAlignment(HorizontalAlignment.LEFT);
        cell4.setVerticalAlignment(VerticalAlignment.MIDDLE);
        //cell4.setBorder(Border.NO_BORDER);
        table.addCell(cell4);

        Cell cell5 = new Cell(1,4).add(new Paragraph("SKU:"+SKU).setFontSize(fontSize));
        //cell1.setTextAlignment(TextAlignment.CENTER);
        cell5.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        cell5.setVerticalAlignment(VerticalAlignment.MIDDLE);
        //cell5.setBorder(Border.NO_BORDER);
        table.addCell(cell5);

        BarcodeEAN barcode13 = new BarcodeEAN(pdfDoc);
        barcode13.setCodeType(BarcodeEAN.EAN13);
        barcode13.setCode(code69);
        PdfFormXObject barcodeObject13 = barcode13.createFormXObject(null, null, pdfDoc);
        Cell cell6 = new Cell(1,10).add(new Image(barcodeObject13).setAutoScale(false).setWidth(UnitValue.createPercentValue(100)));
        cell6.setPaddingLeft(5);
        cell6.setPaddingRight(5);
        //cell3.setBorder(Border.NO_BORDER);
        table.addCell(cell6);

        Cell cell7 = new Cell(1,10).add(new Paragraph("生产日期: "+createDate).setFont(sysFont).setFontSize(fontSize));
        //cell1.setTextAlignment(TextAlignment.CENTER);
        cell7.setHorizontalAlignment(HorizontalAlignment.LEFT);
        cell7.setVerticalAlignment(VerticalAlignment.MIDDLE);
        //cell4.setBorder(Border.NO_BORDER);
        table.addCell(cell7);
        table.setNextRenderer(new TableBorderRenderer(table));

        doc.add(table);

        doc.close();
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

package com.dlsc.workbenchfx.demo.zplTemplate.layouts;

import com.cg.core.util.FontName;
import com.cg.core.zpl.Cell;
import com.cg.core.zpl.FontSettings;
import com.cg.core.zpl.ZPLConfig;
import com.cg.core.zpl.ZPLLabel;

import java.io.InputStream;

public class PalletMark {

    /**
     * A4大小的栈板唯一码标签<br>
     * 如果boxSNs!=null,带出中箱号<br>
     */
    public static ZPLLabel layout02_297_210(boolean setTransPose, boolean isMock,int dpi,String PO,String SKU,String id,String createDate,String productName
            ,String productColor,String pcs,String grossWeight,String palletNumber, String[] boxSNs, int rows, int cols) {
        //TODO 文档中的图纸边距为0,感觉有点怪怪的
        final String cnFontName="黑体";
        final String enFontName="Arial";
        final float fontHeight = 6.7f;

        final float xf = 297f/117f;
        final float yf = 210f/60f;

        ZPLConfig config = new ZPLConfig(dpi, 356f, 252f, 297f);
        config.setDarkness(10);
        config.setDefaultFont(new FontSettings(FontName.HEITI, 2.1f));
        config.setPortrait(setTransPose);//是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        InputStream in = PalletMark.class.getResourceAsStream("/zpl/xiaomi.png");

        Cell cell0 = label.addCell(6.7f, 6.0f, 55.4f, 56.4f,true,true,true,true);
        label.addCellImage(cell0,in,24.8f,28f);

        Cell cell1 = label.addCellRight(cell0, 118, (cell0.getTop()+cell0.getBottom())/2f, false,true,true,true);
        label.addCellText(cell1,"采购订单号", "P.O.", fontHeight,cnFontName);

        Cell cell2 = label.addCellRight(cell1,185f,false,true,true,true);
        label.addCellText(cell2,PO, fontHeight,cnFontName);

        Cell cell3 = label.addCellRight(cell2, 260f,false,true,true,true);
        label.addCellText(cell3, "产品SKU", fontHeight,cnFontName);

        Cell cell4 = label.addCellRight(cell3, 350, false,true,true,true);
        label.addCellText(cell4, SKU, fontHeight,enFontName);

        Cell cell5 = label.addCellBottom(cell1, false,false,true,true);
        label.addCellText(cell5, "产品ID", fontHeight,cnFontName);

        Cell cell6 = label.addCellBottom(cell2,false,false,true,true);
        label.addCellCode128(cell6, id, fontHeight,cnFontName);

        Cell cell7 = label.addCellBottom(cell3, false,false,true,true);
        label.addCellText(cell7,"生产日期", fontHeight,cnFontName);

        Cell cell8 = label.addCellBottom(cell4,false,false,true,true);
        label.addCellCode128(cell8,createDate, fontHeight,cnFontName);

        Cell cell9 = label.addCellBottom(cell0, 81f, true, false, true, true);
        label.addCellText(cell9, "产品名称", fontHeight,cnFontName);

        Cell cell10 = label.addCellRight(cell9, cell2.getRight(),false,false,true,true);
        label.addCellText(cell10, productName+" "+productColor, fontHeight,cnFontName);

        Cell cell11 = label.addCellRight(cell10, cell3.getRight(),false,false,true,true);
        label.addCellText(cell11, "装托数量", fontHeight,cnFontName);

        Cell cell12 = label.addCellRight(cell11, cell4.getRight(), false,false, true,true);
        label.addCellText(cell12, pcs, fontHeight,cnFontName);

        Cell cell13 = label.addCellBottom(cell9, 120f,true,false,true,true);
        label.addCellText(cell13, "托盘毛重", "(kg)", fontHeight,cnFontName);

        Cell cell14 = label.addCellRight(cell13, cell1.getRight(), false, false, true,true);
        label.addCellText(cell14, grossWeight+"kg", fontHeight,enFontName);

        Cell cell15 = label.addCellRight(cell14, cell2.getRight(), false,false,true,true);
        label.addCellText(cell15, "托盘号", fontHeight,cnFontName);

        Cell cell16  = label.addCellRight(cell15, cell4.getRight(),false,false,true,true);
        label.addCellCode128(cell16,palletNumber, fontHeight,cnFontName);

        Cell cell17 = label.addCellBottom(cell13, 245f,true,false,true,true);
        label.addCellText(cell17,"备注", fontHeight,cnFontName);

        Cell cell18 = label.addCellRight(cell17,cell4.getRight(),false,false,true,true);

        if(boxSNs!=null){
            //超过行列数后中箱打印处理,有余数多加1
            if(boxSNs.length > rows*cols){
                rows = boxSNs.length % cols >0? boxSNs.length/cols+1 :boxSNs.length/cols;
            }
            label.addGridText(cell18, boxSNs, fontHeight*0.7f, cnFontName, rows, cols, true);
        }
        return label;
    }

    /**
     * 117*60尺寸大小的栈板唯一码标签<br>
     * 没有中箱号信息<br>
     */
    public static ZPLLabel layout03_117_60(boolean setTransPose, boolean isMock,int dpi,String PO,String SKU,String id,String createDate,String productName
            ,String productColor,String pcs,String grossWeight,String palletNumber) {
        //TODO 文档中的图纸边距为0,感觉有点怪怪的
        final String cnFontName="黑体";
        final String enFontName="Arial";
        final float fontHeight = 2.1f;

        ZPLConfig config = new ZPLConfig(dpi, 117f, 60f, 117);
        config.setDarkness(10);
        config.setDefaultFont(new FontSettings(FontName.HEITI, 2.1f));
        config.setPortrait(setTransPose);//是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        InputStream in = PalletMark.class.getResourceAsStream("/zpl/xiaomi.png");
        label.addBox(2, 2, 23.3f, 16.6f);
        label.addImage(in, 9.75f, 6.7f, 7.8f, 9.4f);

        label.addBox(25.3f, 2f, 14.0f, 8.4f);
        label.addText("采购订单号", 25.3f, 4.1f, 14.0f, fontHeight,fontHeight,cnFontName);
        label.addText("P.O.", 25.3f, 6.2f, 14.0f,fontHeight,fontHeight,enFontName);

        label.addBox(39.3f, 2f, 22.7f, 8.4f);
        label.addText(PO, 39.3f, 5.15f, 22.7f,fontHeight,fontHeight,cnFontName);

        label.addBox(62.0f, 2f, 25.6f, 8.4f);
        label.addCellText(getCell(62.0f, 2f, 25.6f, 8.4f),"产品SKU", fontHeight,cnFontName);

        label.addBox(87.6f, 2f, 27.4f, 8.4f);
        label.addText(SKU, 87.6f, 5.15f, 27.4f,fontHeight,fontHeight,enFontName);

        label.addBox(25.3f, 10.4f, 14.0f, 8.2f);
        label.addCellText(getCell(25.3f, 10.4f, 14.0f, 8.2f),"产品ID", fontHeight,cnFontName);

        label.addBox(39.3f, 10.4f, 22.7f, 8.2f);
        label.addCode128(id, id,42.35f, 11.95f, 16.6f, 3.0f);

        label.addBox(62.0f, 10.4f, 25.6f, 8.2f);
        label.addCellText(getCell(62.0f, 10.4f, 25.6f, 8.2f),"生产日期", fontHeight,cnFontName);

        label.addBox(87.6f, 10.4f, 27.4f, 8.2f);
        label.addCode128(createDate, createDate,93f, 11.95f, 16.6f, 3.0f);

        label.addBox(2f, 18.6f, 23.3f, 8.4f);
        label.addCellText(getCell(2f, 18.6f, 23.3f, 8.4f),"产品名称", fontHeight,cnFontName);

        label.addBox(25.3f, 18.6f, 36.7f, 8.4f);
        label.addText(productName+" "+productColor, 25.3f, 21.75f, 36.7f,fontHeight,fontHeight,cnFontName);

        label.addBox(62.0f, 18.6f, 25.6f, 8.4f);
        label.addCellText(getCell(62.0f, 18.6f, 25.6f, 8.4f),"装托数量", fontHeight,cnFontName);

        label.addBox(87.6f, 18.6f, 27.4f, 8.4f);
        label.addText(pcs, 87.6f, 21.75f, 27.4f,fontHeight,fontHeight,cnFontName);

        label.addBox(2f, 27.0f, 23.3f, 13.4f);
        label.addCellText(getCell(2f, 27.0f, 23.3f, 13.4f),"托盘毛重", fontHeight,cnFontName);

        label.addBox(25.3f, 27.0f, 14.0f, 13.4f);
        label.addText(grossWeight+"kg", 25.3f, 32.65f, 14.0f,fontHeight,fontHeight,enFontName);

        label.addBox(39.3f, 27.0f, 22.7f, 13.4f);
        label.addCellText(getCell(39.3f, 27.0f, 22.7f, 13.4f),"托盘号", fontHeight,cnFontName);

        label.addBox(62.0f, 27.0f, 53.0f, 13.4f);
        label.addCode128(palletNumber, palletNumber,63.5f, 29.6f, 50f, 6.1f);

        label.addBox(2f, 40.4f, 23.3f, 17.6f);
        label.addCellText(getCell(2f, 40.4f, 23.3f, 17.6f),"备注", fontHeight,cnFontName);

        label.addBox(25.3f, 40.4f, 89.7f, 17.6f);

        return label;
    }
    /**
     * 小米M13 TP45390
     * 栈板唯一码标签(297f * 210f)
     * @param isMock  是否Web模拟打印(生产环境和Web模拟打印有一部分参数不一样)
     * @param dpi     打印机DPI
     * @param PO 采购订单号
     * @param SKU 产品SKU
     * @param id 产品id,例如:40088
     * @param createDate 生产日期，格式yyyyMMdd
     * @param productName 产品名称
     * @param pcs 装托数量
     * @param grossWeight 托盘毛重
     * @param palletNumber 托盘号
     * @param productLaserSNs 如果boxSNs!=null,带出中箱号
     * @return zpl
     */
    public static ZPLLabel layout04_297_210(boolean setTransPose, boolean isMock,int dpi,String PO,String SKU,String id,String createDate,String productName
            ,String productColor,String pcs,String grossWeight,String palletNumber, String[] productLaserSNs, int rows, int cols) {
        //TODO 文档中的图纸边距为0,感觉有点怪怪的
        final String cnFontName="黑体";
        final String enFontName="Arial";
        final float fontHeight = 6.7f;

        final float xf = 297f/117f;
        final float yf = 210f/60f;

        ZPLConfig config = new ZPLConfig(dpi, 356f, 252f, 297f);
        config.setDarkness(10);
        config.setDefaultFont(new FontSettings(FontName.HEITI, 2.1f));
        config.setPortrait(setTransPose);//是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        InputStream in = PalletMark.class.getResourceAsStream("/zpl/xiaomi.png");

        Cell cell0 = label.addCell(6.7f, 6.0f, 55.4f, 56.4f,true,true,true,true);
        label.addCellImage(cell0,in,24.8f,28f);

        Cell cell1 = label.addCellRight(cell0, 118, (cell0.getTop()+cell0.getBottom())/2f, false,true,true,true);
        label.addCellText(cell1,"采购订单号", "P.O.", fontHeight,cnFontName);

        Cell cell2 = label.addCellRight(cell1,185f,false,true,true,true);
        label.addCellText(cell2,PO, fontHeight,cnFontName);

        Cell cell3 = label.addCellRight(cell2, 260f,false,true,true,true);
        label.addCellText(cell3, "产品SKU", fontHeight,cnFontName);

        Cell cell4 = label.addCellRight(cell3, 350, false,true,true,true);
        label.addCellText(cell4, SKU, fontHeight,enFontName);

        Cell cell5 = label.addCellBottom(cell1, false,false,true,true);
        label.addCellText(cell5, "产品ID", fontHeight,cnFontName);

        Cell cell6 = label.addCellBottom(cell2,false,false,true,true);
        label.addCellCode128(cell6, id, fontHeight,cnFontName);

        Cell cell7 = label.addCellBottom(cell3, false,false,true,true);
        label.addCellText(cell7,"生产日期", fontHeight,cnFontName);

        Cell cell8 = label.addCellBottom(cell4,false,false,true,true);
        label.addCellCode128(cell8,createDate, fontHeight,cnFontName);

        Cell cell9 = label.addCellBottom(cell0, 81f, true, false, true, true);
        label.addCellText(cell9, "产品名称", fontHeight,cnFontName);

        Cell cell10 = label.addCellRight(cell9, cell2.getRight(),false,false,true,true);
        label.addCellText(cell10, productName+" "+productColor, fontHeight,cnFontName);

        Cell cell11 = label.addCellRight(cell10, cell3.getRight(),false,false,true,true);
        label.addCellText(cell11, "装托数量", fontHeight,cnFontName);

        Cell cell12 = label.addCellRight(cell11, cell4.getRight(), false,false, true,true);
        label.addCellText(cell12, pcs, fontHeight,cnFontName);

        Cell cell13 = label.addCellBottom(cell9, 120f,true,false,true,true);
        label.addCellText(cell13, "托盘毛重", "(kg)", fontHeight,cnFontName);

        Cell cell14 = label.addCellRight(cell13, cell1.getRight(), false, false, true,true);
        label.addCellText(cell14, grossWeight+"kg", fontHeight,enFontName);

        Cell cell15 = label.addCellRight(cell14, cell2.getRight(), false,false,true,true);
        label.addCellText(cell15, "托盘号", fontHeight,cnFontName);

        Cell cell16  = label.addCellRight(cell15, cell4.getRight(),false,false,true,true);
        label.addCellCode128(cell16,palletNumber, fontHeight,cnFontName);

        Cell cell17 = label.addCellBottom(cell13, 245f,true,false,true,true);
        label.addCellText(cell17,"备注", fontHeight,cnFontName);

        Cell cell18 = label.addCellRight(cell17,cell4.getRight(),false,false,true,true);


        if(productLaserSNs!=null && productLaserSNs.length>0){
            label.addGridText(cell18, productLaserSNs, fontHeight*0.55f, cnFontName, rows, cols, true);
        }
        return label;
    }
    protected static Cell getCell(float left, float top, float width, float height){
        return  new Cell(left,  top,  left+width,  top+height);
    }
}

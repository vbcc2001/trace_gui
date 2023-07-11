package com.dlsc.workbenchfx.demo.zplTemplate.layouts;

import com.cg.core.util.FontName;
import com.cg.core.zpl.FontSettings;
import com.cg.core.zpl.ZPLConfig;
import com.cg.core.zpl.ZPLLabel;

import java.io.InputStream;

/** 中箱唯一码 */
public class BoxCode {
    /**
     * 中箱唯一码标签<br>
     *  77*80
     */
    public static ZPLLabel layout02_77_80(boolean setTransPose, int dpi, String boxCode, String productName,String color,String[] productSNs) {
        float totalWidth = 77f,totalHeight = 80f; //总尺寸
        ZPLConfig config = new ZPLConfig(dpi, totalWidth, totalHeight, totalWidth);// 标签宽度110*60mm
        config.setDefaultFont(new FontSettings(FontName.ARIAL, 2.0f));
        config.setPortrait(setTransPose);//是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        /** --------------添加图标------------- **/
        InputStream in = BoxCode.class.getResourceAsStream("/zpl/xiaomi.png");
        label.addImage(in, 3.0f, 2.7f, 5.4f, 6.1f);
        /** --------------添加标题------------- **/
        var titleFont = new FontSettings(FontName.HEITI, 2.7f); //标题字段：黑体
        label.addText(productName+" "+color, 13f, 4.3f, titleFont.getHeight(), titleFont.getFontName());
        /** --------------添加中箱SN------------- **/
        var boxSNFont = new FontSettings(FontName.ARIAL, 2.6f); //Arial
        label.addCode128(boxCode, boxCode, 14.0f, 12.0f, 47.0f, 6.0f,boxSNFont.getHeight(),boxSNFont.getFontName());
        /** --------------添加产品SN------------- **/
        var productSNFont = new FontSettings(FontName.ARIAL, 2.4f); //Arial
        var countPerRow=2;//每行2条记录

        float start[] = {2.0f ,25f}; //其起始点
        float productSize[] = {35f,4.7f}; //产品尺寸
        float marginX = totalWidth-2*start[0]-2*productSize[0];
        float marginY = 6.7f;
        for (var i=0;i<productSNs.length;i++){
            var x= start[0];
            var y= start[1] + (productSize[1] + marginY)* (i/countPerRow);
            //第一列
            if (i%countPerRow == 0) { }
            //第二列
            if (i%countPerRow == 1) {
                x = x + productSize[0] +marginX;
            }
            label.addCode128(productSNs[i], productSNs[i], x, y, productSize[0], productSize[1],productSNFont.getHeight(),productSNFont.getFontName());
        }
        return label;
    }
    /**
     * 中箱唯一码标签<br>
     *  110*60
     */
    public static ZPLLabel layout03_110_60(boolean setTransPose, int dpi, String boxCode, String productName,String color,String[] productSNs) {
        // 字体大小要随时可变,目前接口还不支持。
        ZPLConfig config = new ZPLConfig(dpi, 110f, 60f, 110f);// 标签宽度110*60mm
        config.setDarkness(10);
        config.setDefaultFont(new FontSettings(FontName.ARIAL, 2.5f));
        config.setPortrait(setTransPose);//是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        /** --------------添加图标------------- **/
        InputStream in = BoxCode.class.getResourceAsStream("/zpl/xiaomi.png");
        label.addImage(in, 2.0f, 4.0f, 5.8f, 6.5f);
        /** --------------添加标题------------- **/
        var titleFont = new FontSettings(FontName.HEITI, 3.0f); //标题字段：黑体
        label.addText(productName+" "+color, 12f, 5.3f, titleFont.getHeight(), titleFont.getFontName());
        // label.addBox(3.8f, 3.8f, 501f, 273f);
        label.addCode128(boxCode, boxCode, 58.5f, 3.4f, 48f, 6.8f,3.0f);
        final int countPerRow=3;//每行3条记录
        float[] lefts=new float[] {4.4f-3,39.8f-2.5f,75.6f-2.0f};
        float hStep=4.4f+4.4f;//条码高度+间距
        int i=0;
        float top=15.0f;
        for(String pSN:productSNs) {
            label.addCode128(pSN, pSN, lefts[i], top, 30.9f+2f, 4.0f+0.4f);
            i++;
            if(i==countPerRow) {
                i=0;
                top+=hStep;
            }
        }
        return label;
    }
    /**
     * 中箱唯一码标签<br>
     *  117*60
     */
    public static ZPLLabel layout04_117_60(boolean setTransPose, int dpi, String boxCode, String productName, String color, String[] productSNs) {
        float totalWidth = 117f,totalHeight = 60f; //总尺寸
        ZPLConfig config = new ZPLConfig(dpi, totalWidth, totalHeight, totalWidth);// 标签宽度110*60mm
        config.setDefaultFont(new FontSettings(FontName.ARIAL, 2.0f));
        config.setPortrait(setTransPose);//是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        /** --------------添加图标------------- **/
        InputStream in = BoxCode.class.getResourceAsStream("/zpl/xiaomi.png");
        label.addImage(in, 10.0f, 4.0f, 6.6f, 7.7f);
        /** --------------添加标题------------- **/
        var titleFont = new FontSettings(FontName.HEITI, 3.5f); //标题字段：黑体
        label.addText(productName+" "+color, 64.0f, 5.2f, titleFont.getHeight(), titleFont.getFontName());
        /** --------------添加中箱SN------------- **/
        var boxSNFont = new FontSettings(FontName.ARIAL, 3.05f); //Arial
        label.addCode128(boxCode, boxCode, 34.0f, 12.0f, 50f, 6.8f,boxSNFont.getHeight(),boxSNFont.getFontName());
        /** --------------添加产品SN------------- **/
        var productSNFont = new FontSettings(FontName.ARIAL, 3.5f); //Arial
        var countPerRow=2;//每行2条记录

        float start[] = {9.6f ,24.4f}; //其起始点
        float productSize[] = {45f,5.2f}; //产品尺寸
        float marginX = totalWidth-2*start[0]-2*productSize[0];
        float marginY = 7.2f;
        for (var i=0;i<productSNs.length;i++){
            var x= start[0];
            var y= start[1] + (productSize[1] + marginY)* (i/countPerRow);
            //第一列
            if (i%countPerRow == 0) { }
            //第二列
            if (i%countPerRow == 1) {
                x = x + productSize[0] +marginX;
            }
            label.addCode128(productSNs[i], productSNs[i], x, y, productSize[0], productSize[1],productSNFont.getHeight(), productSNFont.getFontName());
        }
        return label;
    }

    /**
     * 中箱唯一码标签<br>
     * 70*100
     */
    public static ZPLLabel layout05_70_100(boolean setTransPose, int dpi, String boxCode, String productName, String color,String productId, String[] productSNs) {
        //-----------------------------配置尺寸---------------------------//
        //总尺寸
        var pageCell = new NewCell(0f,0f,70f,100f);
        //图片尺寸
        var imageCell = new NewCell(4.4f,6.1f,7.4f,8.5f);
        //产品名尺寸
        var nameCell = new NewCell(35.6f,7.6f,0,0);
        //产品ID尺寸
        var productIdCell = new NewCell(35.6f,10.3f,0,0);
        //中箱SN尺寸
        var boxSNWidth = 62.7f;
        var boxSNHeight = 8.4f;
        var boxSNTop = 22.7f;
        var boxSNCell = new NewCell((pageCell.getWidth()-boxSNWidth)/2,boxSNTop,boxSNWidth,boxSNHeight);
        //产品SN尺寸
        var countPerRow=2;//每行2条记录
        var productSNWidth = 29.2f;
        var productSNHeight = 5.7f;
        var productSNTop = 36.8f;
        float marginX = (pageCell.getWidth()/countPerRow-productSNWidth)/2;
        float marginY = 11.6f;
        var productSNCell = new NewCell(marginX,productSNTop,0,0);
        //字体
        var titleFont = new FontSettings(FontName.HEITI, 2.4f); //标题字段：黑体
        var productIdFont = new FontSettings(FontName.ARIAL, 2.4f); //Arial
        var boxSNFont = new FontSettings(FontName.ARIAL, 3.25f); //Arial
        var productSNFont = new FontSettings(FontName.ARIAL, 2.0f); //Arial

        //-----------------------------打印布局---------------------------//
        ZPLConfig config = new ZPLConfig(dpi, pageCell.getWidth(), pageCell.getHeight(), pageCell.getWidth());
        config.setDefaultFont(new FontSettings(FontName.ARIAL, 2.0f));
        config.setPortrait(setTransPose);//是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        /** --------------添加图标------------- **/
        InputStream in = BoxCode.class.getResourceAsStream("/zpl/xiaomi.png");
        label.addImage(in, imageCell.getLeft(), imageCell.getTop(), imageCell.getWidth(), imageCell.getHeight());
        /** --------------添加标题------------- **/
        label.addText(productName+" "+color, nameCell.getLeft(), nameCell.getTop(), titleFont.getHeight(), titleFont.getFontName());
        /** --------------添加产品ID------------- **/
        label.addText(productId, productIdCell.getLeft(), productIdCell.getTop(), productIdFont.getHeight(), productIdFont.getFontName());
        /** --------------添加中箱SN------------- **/
        label.addCode128(boxCode, boxCode, boxSNCell.getLeft(), boxSNCell.getTop(), boxSNCell.getWidth(), boxSNCell.getHeight(),
                boxSNFont.getHeight(),boxSNFont.getFontName());
        /** --------------添加产品SN------------- **/
        for (var i=0;i<productSNs.length;i++){
            //没有换行时候，X轴增加
            if (i>0 && i%countPerRow != 0) {
                productSNCell.setLeft(productSNCell.getLeft()+productSNWidth+marginX*2);
            }
            //换行，X轴归零，Y轴增加
            if (i>0 && i%countPerRow == 0) {
                productSNCell.setLeft(marginX);
                productSNCell.setTop(productSNCell.getTop()+marginY);
            }
            label.addCode128(productSNs[i], productSNs[i],
                    productSNCell.getLeft(),productSNCell.getTop(), productSNWidth, productSNHeight,
                    productSNFont.getHeight(), productSNFont.getFontName());
        }
        return label;
    }

    /**
     * 中箱唯一码标签<br>
     * 60*138
     */
    public static ZPLLabel layout06_60_138(boolean setTransPose, int dpi, String boxCode, String productName, String color,String productId, String[] productSNs) {
        //-----------------------------配置尺寸---------------------------//
        //总尺寸
        var pageCell = new NewCell(0f,0f,60f,138f);
        //图片尺寸
        var imageCell = new NewCell(4.5f,4.5f,5.3f,6.3f);
        //产品名尺寸
        var nameCell = new NewCell(27.0f,5.0f,0,0);
        //产品ID尺寸
        var productIdCell = new NewCell(27.0f,7.4f,0,0);
        //中箱SN尺寸
        var boxSNWidth = 54f;
        var boxSNHeight = 7.5f;
        var boxSNTop = 18f;
        var boxSNCell = new NewCell((pageCell.getWidth()-boxSNWidth)/2,boxSNTop,boxSNWidth,boxSNHeight);
        //产品SN尺寸
        var countPerRow=2;//每行2条记录
        var productSNWidth = 27f;
        var productSNHeight = 4.5f;
        var productSNTop = 30f;
        float marginX = (pageCell.getWidth()/countPerRow-productSNWidth)/2;
        float marginY = 10.4f;
        var productSNCell = new NewCell(marginX,productSNTop,0,0);
        //字体
        var titleFont = new FontSettings(FontName.HEITI, 2.2f); //标题字段：黑体
        var productIdFont = new FontSettings(FontName.ARIAL, 2.2f); //Arial
        var boxSNFont = new FontSettings(FontName.ARIAL, 3.05f); //Arial
        var productSNFont = new FontSettings(FontName.ARIAL, 1.8f); //Arial

        //-----------------------------打印布局---------------------------//
        ZPLConfig config = new ZPLConfig(dpi, pageCell.getWidth(), pageCell.getHeight(), pageCell.getWidth());
        config.setDefaultFont(new FontSettings(FontName.ARIAL, 2.0f));
        config.setPortrait(setTransPose);//是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        /** --------------添加图标------------- **/
        InputStream in = BoxCode.class.getResourceAsStream("/zpl/xiaomi.png");
        label.addImage(in, imageCell.getLeft(), imageCell.getTop(), imageCell.getWidth(), imageCell.getHeight());
        /** --------------添加标题------------- **/
        label.addText(productName+" "+color, nameCell.getLeft(), nameCell.getTop(), titleFont.getHeight(), titleFont.getFontName());
        /** --------------添加产品ID------------- **/
        label.addText(productId, productIdCell.getLeft(), productIdCell.getTop(), productIdFont.getHeight(), productIdFont.getFontName());
        /** --------------添加中箱SN------------- **/
        label.addCode128(boxCode, boxCode, boxSNCell.getLeft(), boxSNCell.getTop(), boxSNCell.getWidth(), boxSNCell.getHeight(),
                boxSNFont.getHeight(),boxSNFont.getFontName());
        /** --------------添加产品SN------------- **/
        for (var i=0;i<productSNs.length;i++){
            //没有换行时候，X轴增加
            if (i>0 && i%countPerRow != 0) {
                productSNCell.setLeft(productSNCell.getLeft()+productSNWidth+marginX*2);
            }
            //换行，X轴归零，Y轴增加
            if (i>0 && i%countPerRow == 0) {
                productSNCell.setLeft(marginX);
                productSNCell.setTop(productSNCell.getTop()+marginY);
            }
            label.addCode128(productSNs[i], productSNs[i],
                    productSNCell.getLeft(),productSNCell.getTop(), productSNWidth, productSNHeight,
                    productSNFont.getHeight(), productSNFont.getFontName());
        }
        return label;
    }

}

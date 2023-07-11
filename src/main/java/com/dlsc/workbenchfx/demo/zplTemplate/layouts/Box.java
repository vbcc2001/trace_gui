package com.dlsc.workbenchfx.demo.zplTemplate.layouts;

import com.cg.core.util.FontName;
import com.cg.core.zpl.Cell;
import com.cg.core.zpl.FontSettings;
import com.cg.core.zpl.ZPLConfig;
import com.cg.core.zpl.ZPLLabel;

import java.io.InputStream;

/** 中箱物流 */
public class Box {
    /**
     * 中箱物流标签打印<br>
     * 117*60
     */
    public static ZPLLabel layout02_117_60(boolean isPortrait,boolean isMock, int dpi, String productId, String color, String SKU, String PO,
                              String createDate, String supplier, String prodArea, String salesRegion, String productName, String productSpec, String pkgSpec,
                              String inspection, String boxGrossWeight, String boxNetWeight, String boxSize, String volume, String dstCity, String dstArea) {// 中箱物流标签
        final String cnFontName=FontName.HEITI.getFontName();
        final String enFontName="Arial";
        final float fontHeight=1.9f;
        float imageWidth=117f;
        float imageHeight=60f;
        ZPLConfig config = new ZPLConfig(dpi, imageWidth, imageHeight, 117);// 标签宽度117*60
        config.setDarkness(10);
        config.setDefaultFont(new FontSettings(FontName.HEITI, fontHeight));
        config.setPortrait(isPortrait);// 是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        float margin=2.0f;
        float[] cws=new float[]{20.1f,22.5f,13.5f,19.2f,20.1f,17.6f};//列宽,共6列
        float avgH=(imageHeight-margin*2)/11f;//均分12行
        //第一列
        label.addBox(margin, margin, cws[0], avgH*2);
        InputStream in = Box.class.getResourceAsStream("/zpl/xiaomi.png");
        label.addImage(in, margin+(cws[0]-5.97f)/2, margin+(avgH*2-6.55f)/2, 5.7f, 6.55f);
        float left=margin+cws[0];
        label.addBox(left, margin, cws[1], avgH);
        label.addCellText(new Cell(left,margin,left+cws[1],margin+avgH),"供应商",fontHeight,cnFontName);

        left+=cws[1];
        float cw=cws[2]+cws[3]+cws[4]+cws[5];
        label.addBox(left, margin, cw, avgH);
        float fontTop=margin+(avgH-fontHeight)/2;
        label.addText(supplier, left+fontHeight, fontTop, 0, fontHeight, fontHeight, cnFontName);//左侧对齐

        left=margin+cws[0];
        float top=margin+avgH;
        label.addBox(left, top, cws[1], avgH);
        float fontTop2=top+(avgH-fontHeight*2)/2;
        label.addCellText(new Cell(left,top,left+cws[1],top+avgH),"生产地",fontHeight,cnFontName);

        left+=cws[1];
        label.addBox(left, top, cw, avgH);
        fontTop=top+(avgH-fontHeight)/2;
        label.addText(prodArea, left+fontHeight, fontTop, 0f,fontHeight,fontHeight,cnFontName); //生产地

        left=margin;
        top=top+avgH;
        float h=avgH+1;
        fontTop2=top+(h-fontHeight*2)/2;
        label.addBox(left, top, cws[0], h);
        label.addCellText(new Cell(left,top,left+cws[0],top+h),"产品ID",fontHeight,cnFontName);

        left+=cws[0];
        label.addBox(left, top, cws[1], h);
        float codeW=cws[1]-fontHeight*2;
        float codeH=3.0f;
        float codeFontTop=top+(h-codeH-fontHeight)/2;
        label.addCode128(productId, productId, left+(cws[1]-codeW)/2, codeFontTop, codeW, codeH);

        left+=cws[1];
        label.addBox(left, top, cws[2], h);
        label.addCellText(new Cell(left,top,left+cws[2],top+h),"产品颜色",fontHeight,cnFontName);

        left+=cws[2];
        fontTop=top+(h-fontHeight)/2;
        label.addBox(left, top, cws[3], h);
        label.addText(color, left, fontTop, cws[3],fontHeight,fontHeight,cnFontName);

        left+=cws[3];
        label.addBox(left, top, cws[4], h);
        label.addCellText(new Cell(left,top,left+cws[4],top+h),"销售区域",fontHeight,cnFontName);

        left+=cws[4];
        label.addBox(left, top, cws[5], h);
        label.addText(salesRegion, left, fontTop, cws[5], fontHeight,fontHeight,cnFontName);//销售区域

        left=margin;
        top+=h;
        h=avgH;
        fontTop2=top+(h-fontHeight*2)/2;
        label.addBox(left, top, cws[0], h);
        label.addCellText(new Cell(left,top,left+cws[0],top+h),"产品名称",fontHeight,cnFontName);

        left+=cws[0];
        fontTop+=h;
        label.addBox(left, top, cws[1], h);
        label.addCellText(new Cell(left,top,left+cws[1],top+h),productName,fontHeight,cnFontName);


        left+=cws[1];
        label.addBox(left, top, cws[2], h);
        label.addCellText(new Cell(left,top,left+cws[2],top+h),"产品料号",fontHeight,cnFontName);

        left+=cws[2];
        label.addBox(left, top, cws[3], h);
        label.addCellText(new Cell(left,top,left+cws[3],top+h),SKU,fontHeight,enFontName);

        left+=cws[3];
        label.addBox(left, top, cws[4], h);
        label.addCellText(new Cell(left,top,left+cws[4],top+h),"产品规格",fontHeight,cnFontName);

        left+=cws[4];
        label.addBox(left, top, cws[5], h);
        label.addCellText(new Cell(left,top,left+cws[5],top+h),productSpec,fontHeight,cnFontName);

        left=margin;
        top+=h;
        h=2*avgH;
        fontTop2=top+(h-fontHeight*2)/2;
        float fontTop3=fontTop2+fontHeight;
        label.addBox(left, top, cws[0], h+0.1f);
        label.addCellText(new Cell(left,top,left+cws[0],top+h+0.1f),"包装规格",fontHeight,cnFontName);

        left+=cws[0];
        float w=cws[1]+cws[2];
        label.addBox(left, top, w, h+0.1f);
        label.addCellText(new Cell(left,top,left+w,top+h+0.1f),pkgSpec,fontHeight,cnFontName);

        left+=w;
        w=cws[3];
        h=avgH*2;
        fontTop2=top+(h-fontHeight*3)/2 + 1f;
        fontTop3=fontTop2+fontHeight;
        label.addBox(left, top, w, h+0.1f);
        label.addText("采购订单号", left, fontTop2, w,fontHeight,fontHeight,cnFontName);
        label.addText("P.O.", left, fontTop3, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[4]+cws[5];
        fontTop2=top+(h-fontHeight*2)/2;
        fontTop3=fontTop2+fontHeight;
        label.addBox(left, top, w, h+0.1f);
        label.addCellText(new Cell(left,top,left+w,top+h+0.1f),PO,fontHeight,cnFontName);


//        left=margin;
//        w=cws[0];
//        top=top+avgH;
//        h=avgH;
//        fontTop2=top+(h-fontHeight*2)/2;
//        fontTop3=fontTop2+fontHeight;
//        label.addBox(left, top, w, h+0.2f);
//        label.addText("采购订单号", left, fontTop2+0.2f, w,fontHeight,fontHeight,cnFontName);
//        label.addText("P.O.", left, fontTop3+0.2f, w,fontHeight,fontHeight,enFontName);
//
//        left+=w;
//        w=cws[1]+cws[2];
//        fontTop+=avgH;
//        label.addBox(left, top, w, h+0.2f);
//        label.addText(PO, left, fontTop, w, fontHeight,fontHeight,enFontName);//销售区域

        left=margin;
        w=cws[0];
        top+=h;
        h=avgH*2-1;
        label.addBox(left, top, w, h, true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h),"生产日期",fontHeight,cnFontName);

        left+=w;
        w=cws[1]+cws[2];
        label.addBox(left, top, w, h,true,true,false,true);
        codeW=w-fontHeight*2;
        codeH=3.8f;
        codeFontTop=top+(h-codeH-fontHeight)/2;
        label.addCode128(createDate, createDate,left+(w-codeW)/2, codeFontTop, codeW, codeH);

        left+=w;
        w=cws[3];
        label.addBox(left, top, w, h,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h),"检验员/检验批次",fontHeight,cnFontName);

        left+=w;
        w=cws[4]+cws[5];
        fontTop=top+(h-fontHeight)/2;
        label.addBox(left, top, w, h,true,true,false,true);
        label.addText(inspection, left, fontTop, w,fontHeight,fontHeight,cnFontName);

        left=margin;
        top+=h-0.04f;
        h=avgH;
        w=cws[0];
        fontTop2=top+(h-fontHeight*2)/2;
        fontTop3=fontTop2+fontHeight;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"毛重(kg)",fontHeight,cnFontName);

        left+=w;
        w=cws[1]-fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"净重(kg)",fontHeight,cnFontName);

        left+=w;
        w=cws[2]+fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText("中箱尺寸", left, fontTop2, w,fontHeight,fontHeight,cnFontName);
        label.addText("(cm*cm*cm)", left, fontTop3, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[3];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        float split=left+w/2;//分界线
        label.addRightText("体积", split, fontTop2+1.0f, 0,fontHeight,fontHeight,cnFontName);//对半靠右对齐
        label.addText("(m³)", split, fontTop2+1.0f, 0,fontHeight,fontHeight,enFontName);//对半靠左对齐
//        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"体积    ",fontHeight,cnFontName);
//        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"        (m³)",fontHeight,enFontName);

        left+=w;
        w=cws[4];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"目的地城市",fontHeight,cnFontName);

        left+=w;
        w=cws[5];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"目的仓",fontHeight,cnFontName);

        left=margin;
        top+=h;
        h=avgH;
        w=cws[0];
        fontTop=top+(h-fontHeight)/2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(boxGrossWeight, left, fontTop, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[1]-fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(boxNetWeight, left, fontTop, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[2]+fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(boxSize, left, fontTop, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[3];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(volume, left, fontTop, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[4];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(dstCity, left, fontTop, w,fontHeight,fontHeight,cnFontName);

        left+=w;
        w=cws[5];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(dstArea, left, fontTop, w,fontHeight,fontHeight,cnFontName);

        left=margin;
        top+=h;
        h=avgH;
        w=cws[0];
        fontTop=top+(h-fontHeight)/2;
        label.addBox(left, top, w, h,true,true,false,true);
        label.addText("备注", left, fontTop, w,fontHeight,fontHeight,cnFontName);

        left+=w;
        w=cws[1]+cws[2]+cws[3]+cws[4]+cws[5];
        label.addBox(left, top, w, h,true,true,false,true);
        return label;
    }
    /**
     * 中箱物流标签打印<br>
     * 100*70
     */
    public static ZPLLabel layout03_100_70(boolean isPortrait, boolean isMock, int dpi, String productId, String color, String SKU, String PO,
                              String createDate, String supplier, String prodArea, String salesRegion, String productName, String productSpec, String pkgSpec,
                              String inspection, String boxGrossWeight, String boxNetWeight, String boxSize, String volume, String dstCity, String dstArea) {// 中箱物流标签
        final String cnFontName= FontName.HEITI.getFontName();
        final String enFontName="Arial";
        final float fontHeight=1.9f;
        float imageWidth=100f;
        float imageHeight=70f;
        ZPLConfig config = new ZPLConfig(dpi, imageWidth, imageHeight, 100f);
        config.setDarkness(10);
        config.setDefaultFont(new FontSettings(FontName.HEITI, fontHeight));
        config.setPortrait(isPortrait);// 是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        float marginX=2.0f;
        float marginY=2.8f;
        float[] ws=new float[]{11.4f,20.0f,17.0f,16.6f,15.3f,15.6f};//列宽,共6列
        float[] hs=new float[]{5.9f,5.9f,7.7f,5.9f,11.6f,11.6f,5.9f,5.9f,5.9f};//行高,共9行
        float left = marginX;
        float top = marginY;
        float h = hs[0];
        float w = ws[0];
        //第一行第一列
        label.addBox(left, top, w, h+hs[1]+0.1f);
        InputStream in = Box.class.getResourceAsStream("/zpl/xiaomi.png");
        label.addImage(in, left+(w-5.4f)/2, top+(h+hs[1]-6.0f)/2, 5.4f, 6.0f);
        //第一行第二列
        left+=ws[0];
        w = ws[1];
        label.addBox(left, top, w, h);
        label.addCellText(new Cell(left,marginY,left+w,marginY+h),"供应商",fontHeight,cnFontName);
        //第一行第三列
        left+=ws[1];
        w = ws[2]+ws[3]+ws[4]+ws[5]+0f;
        label.addBox(left, marginY, w, h);
        label.addText(supplier, left+fontHeight, marginY+(h-fontHeight)/2, 0, fontHeight, fontHeight, cnFontName);//供应商,左侧对齐
        //第二行第二列
        left=marginX+ws[0];
        top+=h;
        w = ws[1];
        h = hs[1];
        label.addBox(left, top, w, h);
        label.addCellText(new Cell(left,top,left+w,top+h),"生产地",fontHeight,cnFontName);
        //第二行第三列
        left+=ws[1];
        w = ws[2]+ws[3]+ws[4]+ws[5];
        label.addBox(left, top, w, h);
        label.addText(prodArea, left+fontHeight, top+(h-fontHeight)/2, 0f,fontHeight,fontHeight,cnFontName); //生产地,左侧对齐
        //第三行第一列
        left=marginX;
        top+=h;
        w=ws[0];
        h=hs[2];
        label.addBox(left, top, w, h);
        label.addCellText(new Cell(left,top,left+w,top+h),"产品ID",fontHeight,cnFontName);
        //第三行第二列
        left+=ws[0];
        label.addBox(left, top, ws[1], h);
        float codeW=16.0f;
        float codeH=4.6f;
        float codeFontTop=top+(h-codeH-fontHeight)/2;
        label.addCode128(productId, productId, left+(ws[1]-codeW)/2, codeFontTop, codeW, codeH,1.6f,enFontName);
        //第三行第三列
        left+=ws[1];
        label.addBox(left, top, ws[2], h);
        label.addCellText(new Cell(left,top,left+ws[2],top+h),"产品颜色",fontHeight,cnFontName);
        //第三行第四列
        left+=ws[2];
        label.addBox(left, top, ws[3], h);
        label.addText(color, left, top+(h-fontHeight)/2, ws[3],fontHeight,fontHeight,cnFontName);
        //第三行第五列
        left+=ws[3];
        label.addBox(left, top, ws[4], h);
        label.addCellText(new Cell(left,top,left+ws[4],top+h),"销售区域",fontHeight,cnFontName);
        //第三行第六列
        left+=ws[4];
        label.addBox(left, top, ws[5], h);
        label.addText(salesRegion, left, top+(h-fontHeight)/2, ws[5], fontHeight,fontHeight,cnFontName);//销售区域
        //第四行第一列
        left=marginX;
        top+=h;
        h=hs[3];
        label.addBox(left, top, ws[0], h);
        label.addCellText(new Cell(left,top,left+ws[0],top+h),"产品名称",fontHeight,cnFontName);
        //第四行第二列
        left+=ws[0];
        label.addBox(left, top, ws[1], h);
        label.addCellText(new Cell(left,top,left+ws[1],top+h),productName,1.7f,cnFontName);
        //第四行第三列
        left+=ws[1];
        label.addBox(left, top, ws[2], h);
        label.addCellText(new Cell(left,top,left+ws[2],top+h),"产品料号",fontHeight,cnFontName);
        //第四行第四列
        left+=ws[2];
        label.addBox(left, top, ws[3], h);
        label.addCellText(new Cell(left,top,left+ws[3],top+h),SKU,1.7f,enFontName);
        //第四行第五列
        left+=ws[3];
        label.addBox(left, top, ws[4], h);
        label.addCellText(new Cell(left,top,left+ws[4],top+h),"产品规格",fontHeight,cnFontName);
        //第四行第六列
        left+=ws[4];
        label.addBox(left, top, ws[5], h);
        label.addCellText(new Cell(left,top,left+ws[5],top+h),productSpec,1.6f,cnFontName);
        //第五行第一列
        left=marginX;
        top+=h;
        h=hs[4];
        label.addBox(left, top, ws[0], h+0.1f);
        label.addCellText(new Cell(left,top,left+ws[0],top+h+0.1f),"包装规格",fontHeight,cnFontName);
        //第五行第二-三列
        left+=ws[0];
        w=ws[1]+ws[2];
        label.addBox(left, top, w, h+0.1f);
        label.addCellText(new Cell(left,top,left+w,top+h+0.1f),pkgSpec,fontHeight,cnFontName);
        //第五行第四列
        left+=w;
        w=ws[3];
        label.addBox(left, top, w, h+0.1f);
        label.addText("采购订单号", left, top+(h-fontHeight*3)/2 + 1f, w,fontHeight,fontHeight,cnFontName);
        label.addText("P.O.", left, top+(h-fontHeight*3)/2 + 1f+fontHeight, w,fontHeight,fontHeight,enFontName);
        //第五行第五-六列
        left+=w;
        w=ws[4]+ws[5];
        label.addBox(left, top, w, h+0.1f);
        label.addCellText(new Cell(left,top,left+w,top+h+0.1f),PO,2.4f,cnFontName);
        //第六行第一列
        left=marginX;
        top+=h;
        h=hs[5];
        w=ws[0];
        label.addBox(left, top, w, h, true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h),"生产日期",fontHeight,cnFontName);
        //第六行第二-三列
        left+=w;
        w=ws[1]+ws[2];
        label.addBox(left, top, w, h,true,true,false,true);
        codeW=26.0f;
        codeH=7.6f;
        codeFontTop=top+(h-codeH-fontHeight)/2;
        label.addCode128(createDate, createDate,left+(w-codeW)/2, codeFontTop, codeW, codeH,2.6f,enFontName);
        //第六行第四列
        left+=w;
        w=ws[3];
        label.addBox(left, top, w, h,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h),"检验员/检验批次",fontHeight,cnFontName);
        //第六行第五-六列
        left+=w;
        w=ws[4]+ws[5];
        label.addBox(left, top, w, h,true,true,false,true);
        label.addText(inspection, left, top+(h-fontHeight)/2, w,fontHeight,fontHeight,cnFontName);
        //第七行第一列
        left=marginX;
        top+=h-0.04f;
        h=hs[6];
        w=ws[0];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"毛重(kg)",fontHeight,cnFontName);
        //第七行第二列
        left+=w;
        w=ws[1]-fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"净重(kg)",fontHeight,cnFontName);
        //第七行第三列
        left+=w;
        w=ws[2]+fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText("中箱尺寸", left, top+(h-fontHeight*2)/2, w,fontHeight,fontHeight,cnFontName);
        label.addText("(cm*cm*cm)", left, top+(h-fontHeight*2)/2+fontHeight, w,fontHeight,fontHeight,enFontName);
        //第七行第四列
        left+=w;
        w=ws[3];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        float split=left+w/2;//分界线
        label.addRightText("体积", split, top+(h-fontHeight*2)/2+1.0f, 0,fontHeight,fontHeight,cnFontName);//对半靠右对齐
        label.addText("(m³)", split, top+(h-fontHeight*2)/2+1.0f, 0,fontHeight,fontHeight,enFontName);//对半靠左对齐
        //第七行第五列
        left+=w;
        w=ws[4];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"目的地城市",fontHeight,cnFontName);
        //第七行第六列
        left+=w;
        w=ws[5];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"目的仓",fontHeight,cnFontName);
        //第八行第一列
        left=marginX;
        top+=h-0.04f;
        h=hs[7];
        w=ws[0];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(boxGrossWeight, left, top+(h-fontHeight)/2, w,fontHeight,fontHeight,enFontName);
        //第八行第二列
        left+=w;
        w=ws[1]-fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(boxNetWeight, left, top+(h-fontHeight)/2, w,fontHeight,fontHeight,enFontName);
        //第八行第三列
        left+=w;
        w=ws[2]+fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(boxSize, left, top+(h-fontHeight)/2, w,fontHeight,1.6f,enFontName);
        //第八行第四列
        left+=w;
        w=ws[3];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(volume, left, top+(h-fontHeight)/2, w,fontHeight,fontHeight,enFontName);
        //第八行第五列
        left+=w;
        w=ws[4];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(dstCity, left, top+(h-fontHeight)/2, w,fontHeight,fontHeight,cnFontName);
        //第八行第六列
        left+=w;
        w=ws[5];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(dstArea, left, top+(h-fontHeight)/2, w,fontHeight,fontHeight,cnFontName);
        //第九行第一列
        left=marginX;
        top+=h-0.04f;
        h=hs[8];
        w=ws[0];
        label.addBox(left, top, w, h,true,true,false,true);
        label.addText("备注", left, top+(h-fontHeight)/2, w,fontHeight,fontHeight,cnFontName);
        //第九行第二-六列
        left+=w;
        w=ws[1]+ws[2]+ws[3]+ws[4]+ws[5];
        label.addBox(left, top, w, h,true,true,false,true);
        return label;
    }
    /**
     * 中箱物流标签打印<br>
     * 117*60
     * 产品名称两行特殊处理
     */
    public static ZPLLabel layout04_60_117(boolean isPortrait,boolean isMock, int dpi, String productId, String color, String SKU, String PO,
                              String createDate, String supplier, String prodArea, String salesRegion, String productName, String productSpec, String pkgSpec,
                              String inspection, String boxGrossWeight, String boxNetWeight, String boxSize, String volume, String dstCity, String dstArea) {// 中箱物流标签
        final String cnFontName=FontName.HEITI.getFontName();
        final String enFontName="Arial";
        final float fontHeight=1.9f;
        float imageWidth=117f;
        float imageHeight=60f;
        ZPLConfig config = new ZPLConfig(dpi, imageWidth, imageHeight, 117);// 标签宽度117*60
        config.setDarkness(10);
        config.setDefaultFont(new FontSettings(FontName.HEITI, fontHeight));
        config.setPortrait(isPortrait);// 是否纵向打印
        final ZPLLabel label = new ZPLLabel(config);
        float margin=2.0f;
        float[] cws=new float[]{20.1f,22.5f,13.5f,19.2f,20.1f,17.6f};//列宽,共6列
        float avgH=(imageHeight-margin*2)/11f;//均分12行
        //第一列
        label.addBox(margin, margin, cws[0], avgH*2);
        InputStream in = Box.class.getResourceAsStream("/zpl/xiaomi.png");
        label.addImage(in, margin+(cws[0]-5.97f)/2, margin+(avgH*2-6.55f)/2, 5.7f, 6.55f);
        float left=margin+cws[0];
        label.addBox(left, margin, cws[1], avgH);
        label.addCellText(new Cell(left,margin,left+cws[1],margin+avgH),"供应商",fontHeight,cnFontName);

        left+=cws[1];
        float cw=cws[2]+cws[3]+cws[4]+cws[5];
        label.addBox(left, margin, cw, avgH);
        float fontTop=margin+(avgH-fontHeight)/2;
        label.addText(supplier, left+fontHeight, fontTop, 0, fontHeight, fontHeight, cnFontName);//左侧对齐

        left=margin+cws[0];
        float top=margin+avgH;
        label.addBox(left, top, cws[1], avgH);
        float fontTop2=top+(avgH-fontHeight*2)/2;
        label.addCellText(new Cell(left,top,left+cws[1],top+avgH),"生产地",fontHeight,cnFontName);

        left+=cws[1];
        label.addBox(left, top, cw, avgH);
        fontTop=top+(avgH-fontHeight)/2;
        label.addText(prodArea, left+fontHeight, fontTop, 0f,fontHeight,fontHeight,cnFontName); //生产地

        left=margin;
        top=top+avgH;
        float h=avgH+1;
        fontTop2=top+(h-fontHeight*2)/2;
        label.addBox(left, top, cws[0], h);
        label.addCellText(new Cell(left,top,left+cws[0],top+h),"产品ID",fontHeight,cnFontName);

        left+=cws[0];
        label.addBox(left, top, cws[1], h);
        float codeW=cws[1]-fontHeight*2;
        float codeH=3.0f;
        float codeFontTop=top+(h-codeH-fontHeight)/2;
        label.addCode128(productId, productId, left+(cws[1]-codeW)/2, codeFontTop, codeW, codeH);

        left+=cws[1];
        label.addBox(left, top, cws[2], h);
        label.addCellText(new Cell(left,top,left+cws[2],top+h),"产品颜色",fontHeight,cnFontName);

        left+=cws[2];
        fontTop=top+(h-fontHeight)/2;
        label.addBox(left, top, cws[3], h);
        label.addText(color, left, fontTop, cws[3],fontHeight,fontHeight,cnFontName);

        left+=cws[3];
        label.addBox(left, top, cws[4], h);
        label.addCellText(new Cell(left,top,left+cws[4],top+h),"销售区域",fontHeight,cnFontName);

        left+=cws[4];
        label.addBox(left, top, cws[5], h);
        label.addText(salesRegion, left, fontTop, cws[5], fontHeight,fontHeight,cnFontName);//销售区域

        left=margin;
        top+=h;
        h=avgH;
        fontTop2=top+(h-fontHeight*2)/2;
        label.addBox(left, top, cws[0], h);
        label.addCellText(new Cell(left,top,left+cws[0],top+h),"产品名称",fontHeight,cnFontName);

        left+=cws[0];
        fontTop+=h;
        label.addBox(left, top, cws[1], h);
        productName = productName.replace(""+color,"").trim(); //去掉颜色及空格
        //label.addCellText(new Cell(left,top,left+cws[1],top+h-1.1f),productName,fontHeight,cnFontName);
        //label.addCellText(new Cell(left,top,left+cws[1],top+h+1.1f),color,fontHeight,cnFontName); //换行添加颜色

        label.addText(productName, left, top+h/2-fontHeight+0.1f, cws[1],fontHeight-0.1f,fontHeight,cnFontName);
        label.addText(color, left, top+h/2+0.4f, cws[1],fontHeight,fontHeight-0.1f,cnFontName);

        left+=cws[1];
        label.addBox(left, top, cws[2], h);
        label.addCellText(new Cell(left,top,left+cws[2],top+h),"产品料号",fontHeight,cnFontName);

        left+=cws[2];
        label.addBox(left, top, cws[3], h);
        label.addCellText(new Cell(left,top,left+cws[3],top+h),SKU,fontHeight,enFontName);

        left+=cws[3];
        label.addBox(left, top, cws[4], h);
        label.addCellText(new Cell(left,top,left+cws[4],top+h),"产品规格",fontHeight,cnFontName);

        left+=cws[4];
        label.addBox(left, top, cws[5], h);
        label.addCellText(new Cell(left,top,left+cws[5],top+h),productSpec,fontHeight,cnFontName);

        left=margin;
        top+=h;
        h=2*avgH;
        fontTop2=top+(h-fontHeight*2)/2;
        float fontTop3=fontTop2+fontHeight;
        label.addBox(left, top, cws[0], h+0.1f);
        label.addCellText(new Cell(left,top,left+cws[0],top+h+0.1f),"包装规格",fontHeight,cnFontName);

        left+=cws[0];
        float w=cws[1]+cws[2];
        label.addBox(left, top, w, h+0.1f);
        label.addCellText(new Cell(left,top,left+w,top+h+0.1f),pkgSpec,fontHeight,cnFontName);

        left+=w;
        w=cws[3];
        h=avgH*2;
        fontTop2=top+(h-fontHeight*3)/2 + 1f;
        fontTop3=fontTop2+fontHeight;
        label.addBox(left, top, w, h+0.1f);
        label.addText("采购订单号", left, fontTop2, w,fontHeight,fontHeight,cnFontName);
        label.addText("P.O.", left, fontTop3, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[4]+cws[5];
        fontTop2=top+(h-fontHeight*2)/2;
        fontTop3=fontTop2+fontHeight;
        label.addBox(left, top, w, h+0.1f);
        label.addCellText(new Cell(left,top,left+w,top+h+0.1f),PO,fontHeight,cnFontName);


//        left=margin;
//        w=cws[0];
//        top=top+avgH;
//        h=avgH;
//        fontTop2=top+(h-fontHeight*2)/2;
//        fontTop3=fontTop2+fontHeight;
//        label.addBox(left, top, w, h+0.2f);
//        label.addText("采购订单号", left, fontTop2+0.2f, w,fontHeight,fontHeight,cnFontName);
//        label.addText("P.O.", left, fontTop3+0.2f, w,fontHeight,fontHeight,enFontName);
//
//        left+=w;
//        w=cws[1]+cws[2];
//        fontTop+=avgH;
//        label.addBox(left, top, w, h+0.2f);
//        label.addText(PO, left, fontTop, w, fontHeight,fontHeight,enFontName);//销售区域

        left=margin;
        w=cws[0];
        top+=h;
        h=avgH*2-1;
        label.addBox(left, top, w, h, true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h),"生产日期",fontHeight,cnFontName);

        left+=w;
        w=cws[1]+cws[2];
        label.addBox(left, top, w, h,true,true,false,true);
        codeW=w-fontHeight*2;
        codeH=3.8f;
        codeFontTop=top+(h-codeH-fontHeight)/2;
        label.addCode128(createDate, createDate,left+(w-codeW)/2, codeFontTop, codeW, codeH);

        left+=w;
        w=cws[3];
        label.addBox(left, top, w, h,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h),"检验员/检验批次",fontHeight,cnFontName);

        left+=w;
        w=cws[4]+cws[5];
        fontTop=top+(h-fontHeight)/2;
        label.addBox(left, top, w, h,true,true,false,true);
        label.addText(inspection, left, fontTop, w,fontHeight,fontHeight,cnFontName);

        left=margin;
        top+=h-0.04f;
        h=avgH;
        w=cws[0];
        fontTop2=top+(h-fontHeight*2)/2;
        fontTop3=fontTop2+fontHeight;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"毛重(kg)",fontHeight,cnFontName);

        left+=w;
        w=cws[1]-fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"净重(kg)",fontHeight,cnFontName);

        left+=w;
        w=cws[2]+fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText("中箱尺寸", left, fontTop2, w,fontHeight,fontHeight,cnFontName);
        label.addText("(cm*cm*cm)", left, fontTop3, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[3];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        float split=left+w/2;//分界线
        label.addRightText("体积", split, fontTop2+1.0f, 0,fontHeight,fontHeight,cnFontName);//对半靠右对齐
        label.addText("(m³)", split, fontTop2+1.0f, 0,fontHeight,fontHeight,enFontName);//对半靠左对齐
//        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"体积    ",fontHeight,cnFontName);
//        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"        (m³)",fontHeight,enFontName);

        left+=w;
        w=cws[4];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"目的地城市",fontHeight,cnFontName);

        left+=w;
        w=cws[5];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addCellText(new Cell(left,top,left+w,top+h+0.2f),"目的仓",fontHeight,cnFontName);

        left=margin;
        top+=h;
        h=avgH;
        w=cws[0];
        fontTop=top+(h-fontHeight)/2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(boxGrossWeight, left, fontTop, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[1]-fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(boxNetWeight, left, fontTop, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[2]+fontHeight*2;
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(boxSize, left, fontTop, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[3];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(volume, left, fontTop, w,fontHeight,fontHeight,enFontName);

        left+=w;
        w=cws[4];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(dstCity, left, fontTop, w,fontHeight,fontHeight,cnFontName);

        left+=w;
        w=cws[5];
        label.addBox(left, top, w, h+0.2f,true,true,false,true);
        label.addText(dstArea, left, fontTop, w,fontHeight,fontHeight,cnFontName);

        left=margin;
        top+=h;
        h=avgH;
        w=cws[0];
        fontTop=top+(h-fontHeight)/2;
        label.addBox(left, top, w, h,true,true,false,true);
        label.addText("备注", left, fontTop, w,fontHeight,fontHeight,cnFontName);

        left+=w;
        w=cws[1]+cws[2]+cws[3]+cws[4]+cws[5];
        label.addBox(left, top, w, h,true,true,false,true);
        return label;
    }
}

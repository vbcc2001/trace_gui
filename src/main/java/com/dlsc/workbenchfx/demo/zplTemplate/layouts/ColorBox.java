package com.dlsc.workbenchfx.demo.zplTemplate.layouts;

import com.cg.core.util.FontName;
import com.cg.core.zpl.FontSettings;
import com.cg.core.zpl.ZPLConfig;
import com.cg.core.zpl.ZPLImage;
import com.cg.core.zpl.ZPLLabel;

/** 彩盒标签 */
public class ColorBox {
    /** 小米彩盒标签打印 */
    public static ZPLLabel layout01_42_28(boolean isMock, int dpi, String SNCode, String SKU, String color,
                                   String createDate, String productName, String code69) {
        float width=52.5f;
        ZPLConfig config = new ZPLConfig(dpi, width, 35f, 42);// 标签宽度42*28mm,左右边距2mm
        config.setEnableTextRenderingHints(true);
        config.setDarkness(10);
        final String fontName=FontName.FZLTH.getFontName();
        final float fontHeight=1.8f;
        config.setDefaultFont(new FontSettings(FontName.FZLTH, 1.8f));
        config.setDarkness(null);
        final ZPLLabel label = new ZPLLabel(config);
        label.addText(productName, 2.5f, 2.1f, 0f, 3.3f, fontHeight, fontName);//产品名称
        label.addRightText("颜色: " + color, 50.0f, 2.1f, 0f, 3.3f, fontHeight, fontName);//产品名称
        label.addCode128(SNCode, 2.5f, 5.4f, 47.5f, 6.6f);
        label.addText("SN:" + SNCode, 2.5f, 12.0f, 0, 2.18f, 1.9f, "Arial");
        label.addRightText("SKU:" + SKU, 50.0f, 12.0f, 0, 2.18f, 1.9f, "Arial");
        //final String code69 = "693417778482";// 69码,最后一个校验码为8,由打印机自动计算,实际扫描结果为6934177784828
        label.addCode13(code69, 2.5f, 15.1f, 47.5f, 15.8f,FontName.AUMS,3.2f,4.7f);
        label.addText("生产日期："+ createDate, 2.5f, 30f, 0f, 2.2f, 1.8f, fontName);
        float textH=2.0f;//字体高度
        float textTop=30.0f;
        ZPLImage image=label.addRightText("合格证 已检验", 50.0f, textTop, 12.0f, textH, fontHeight, fontName);
//		int boxThickness=3;
//		float boxLineW=boxThickness/config.getDpm();//线宽
        float whiteDistTop=0.5f;//字体边框留白距离
        float whiteDistLeft=0.2f;
        float boxL=image.getOriginLeft()-whiteDistLeft;
        float boxTop=textTop-whiteDistTop;
        float boxW=image.getOriginWidth()+whiteDistLeft*2;
        float boxH=textH+whiteDistTop*2;
        label.addBox(boxL, boxTop, boxW, boxH).setThickness(3);
        return label;
    }
}

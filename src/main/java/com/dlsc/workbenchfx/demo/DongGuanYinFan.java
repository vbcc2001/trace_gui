package com.dlsc.workbenchfx.demo;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.cg.core.util.FontName;
import com.cg.core.util.Log;
import com.cg.core.zpl.FontSettings;
import com.cg.core.zpl.ZPLConfig;
import com.cg.core.zpl.ZPLLabel;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 东莞因范标签打印工具类
 * 
 */
public class DongGuanYinFan {

    public static enum Level {
        ONLY_BOX,
        ONLY_QRCODE,
        BOTH
    }

    public static ZPLLabel ColorBox(boolean isMock, int dpi, String productSN){
        ZPLConfig config = new ZPLConfig(dpi, 38f, 12f, 38);// 标签宽度38*12mm
        config.setDefaultFont(new FontSettings(FontName.FZLTH, 1.8f));
        config.setDarkness(null);
        final ZPLLabel label = new ZPLLabel(config);
        //label.addCode128(productSN, productSN, 1f, 1f,36f, 9f);
        label.addCode128(productSN, productSN, 1f, 1.5f,36f, 7f, 3f, "方正兰亭黑");
        return label;
    }
    public static ZPLLabel Pallet(boolean isMock, int dpi, String PalletSN,String from,String to){
        ZPLConfig config = new ZPLConfig(dpi, 100f, 70f, 100);// 标签宽度38*12mm
        config.setDefaultFont(new FontSettings(FontName.FZLTH, 4f));
        config.setDarkness(null);
        final ZPLLabel label = new ZPLLabel(config);
        if(from!=null && to!=null && from.length()!=to.length()){
            label.addText("出货地: "+from, 5f, 6f,8, FontName.FZLTH.getFontName());
            label.addText("目的地: "+to, 5f, 18f, 8, FontName.FZLTH.getFontName());
        }else{
            label.addText("出货地: "+from, 5f, 6f, 90f,8f,8, FontName.FZLTH.getFontName());
            label.addText("目的地: "+to, 5f, 18f, 90f,8f,8, FontName.FZLTH.getFontName());
        }
        label.addCode128(PalletSN, PalletSN, 5f, 30f,90f, 30f,8f);
        return label;
    }

	public static ZPLLabel box(boolean isMock, int dpi, String productSNs, CartoonBoxSN cartoonBoxSN, Level qrCode) {

        String newProductSNs = "";
        for(String productSn:productSNs.split(",")){
            newProductSNs += productSn + "\r\n"; 
        }
        String barCodeStr = cartoonBoxSN.getNickName();
        String productName = cartoonBoxSN.getProductName();
        String productType = cartoonBoxSN.getProductType();
        String productColor = cartoonBoxSN.getProductColor();
        String productPartNumber = cartoonBoxSN.getProductPartNumber();
        String createTimeStr = cartoonBoxSN.getCreateDate();
        createTimeStr = createTimeStr.split(" ")[0];
        createTimeStr = dateFormat(createTimeStr);
        String QTY = cartoonBoxSN.getQuantity() + "PCS";
        String DIM = MainUtils.float2IntOrFloat(cartoonBoxSN.getBoxLength()) + "*" + MainUtils.float2IntOrFloat(cartoonBoxSN.getBoxWidth())
            + "*" + MainUtils.float2IntOrFloat(cartoonBoxSN.getBoxHeight()) + "mm";
        String NW = cartoonBoxSN.getBoxNetWeigth() + "KG";
        String GW = cartoonBoxSN.getBoxGrossWeight() + "KG";

		ZPLConfig config = new ZPLConfig(dpi, 105f, 95f, 105);// 标签宽度42*28mm,左右边距2mm
		config.setDarkness(10);
		final String fontName=FontName.FZLTH.getFontName();
		final float fontHeight=4.2f;
		config.setDefaultFont(new FontSettings(FontName.FZLTH, 1.8f));
		config.setDarkness(null);
		final ZPLLabel label = new ZPLLabel(config);

        float x = 59f;
        float y0 = 29f;
        float x2 = 74f;
        float y = 41f;

        float org_x = 51.3f;
        float org_y = 25.5f;

        x -= org_x;
        y0 -= org_y;
        x2 -= org_x;
        y -= org_y;

        



        if (qrCode==Level.ONLY_QRCODE){
            newProductSNs = "";
            for(String productSn:productSNs.split(",")){
                newProductSNs += productSn + "_0D_0A"; 
            }

            String content = "中箱箱号：%s\r\n品名：%s\r\n型号：%s\r\n料号：%s\r\n装箱数量：%s\r\n产品SN号：\r\n%s";
            //content = String.format(content,newProductSNs);
            content = String.format(content,barCodeStr,productName,productType,productPartNumber,QTY,newProductSNs);
            try{
                //label.addQRCode(content, x, y0, 80f);
                content = zplQRCode(barCodeStr, productName, productType, productPartNumber, QTY, newProductSNs);
                Log.info(content);
                label.addZPL(content);
                return label;
            }catch(Exception e){
                Log.error("",e);
            }

        }


		InputStream in = DongGuanYinFan.class.getResourceAsStream("/素士logo.png");
        label.addImage(in, x, y0, 35, 7);

        float y2 = y + 1.8f*fontHeight;
        if(productName.length()<=8){
            label.addText("品名："+productName, x, y, fontHeight, fontName);
        }else{
            label.addText("品名："+productName.substring(0, 8), x, y, fontHeight, fontName);
            label.addText(productName.substring(8), x2-3.6f, y2, fontHeight, fontName);
            y2 = y2 + 1.8f*fontHeight;
        }
		label.addText("型号：" + productType, x, y2, fontHeight, fontName);
        y2 += 1.8f*fontHeight;
		label.addText("颜色：" + productColor, x, y2, fontHeight, fontName);
        y2 += 1.8f*fontHeight;
		label.addText("料号：" + productPartNumber, x, y2, fontHeight, fontName);

        if(qrCode==Level.BOTH){
            String content = "中箱箱号：%s\r\n品名：%s\r\n型号：%s\r\n料号：%s\r\n装箱数量：%s\r\n产品SN号：\r\n%s";
            content = String.format(content,barCodeStr,productName,productType,productPartNumber,QTY,newProductSNs);
            try{
                label.addQRCode(content, 115f-org_x-0.6f, y, 34f);
            }catch(Exception e){
                Log.error("",e);
            }
        }
        final float gap = 3f;
        y2 = y2 + fontHeight + gap;
        label.addHorizonLine(x, y2, 88.4f);

        y2 = y2 + gap;
		label.addText("生产日期：" + createTimeStr, x, y2, fontHeight, fontName);
        y2 = y2 + fontHeight+ gap-0.2f;
		label.addText("QTY：" + QTY +"    "+ "DIM："+DIM, x, y2, fontHeight, fontName);
        y2 = y2 + fontHeight+ gap-0.2f;
		label.addText("N.W：" + NW +"    "+ "G.W："+GW, x, y2, fontHeight, fontName);
        y2 = y2 + fontHeight+ gap-0.2f;
		label.addCode128(barCodeStr, barCodeStr, x, y2, 89f, 11f, fontHeight-0.5f, fontName);
		return label;
	}

    private static String dateFormat(String createTimeStr){
        String[] y_m_d = createTimeStr.split("-");
        String ymd = y_m_d[0] + "." + y_m_d[1] + "." + y_m_d[2];
        return ymd;
    }

    private static String zplQRCode(String barCodeStr, String productName, String productType, String productPartNumber, String QTY, String newProductSNs){
        String zplCode = """
            ^JMA^LL1260^PW1260^MD10^PR2^PON^LRN^LH0,0
            ^SEE:GB18030.DAT^CI26                        
            ^FO130,90
            ^BQN,2,6
            ^FH^FDLM,B4000
            中箱箱号：%s_0D_0A
            品名：%s_0D_0A
            型号：%s_0D_0A
            料号：%s_0D_0A
            装箱数量：%s_0D_0A
            产品SN号：_0D_0A
            %s
            ^FS
            """;

        zplCode = String.format(zplCode,barCodeStr,productName,productType,
                productPartNumber,QTY,newProductSNs);

        Log.info("测投稿时！！！！！！！！！！！！！！！！！！！！！！");

        return zplCode;
    }

}

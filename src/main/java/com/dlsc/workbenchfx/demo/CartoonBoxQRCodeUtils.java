//package com.dlsc.workbenchfx.demo;
// 
// 
//import javax.imageio.ImageIO;
//
//import com.alibaba.fastjson2.JSON;
//import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.oned.Code128Writer;
//import com.google.zxing.qrcode.QRCodeWriter;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//
//import org.apache.commons.io.FileUtils;
//
//import javafx.util.Pair;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.List;
// 
///**
// * @author
// */
//public class CartoonBoxQRCodeUtils {
// 
//    /**
//     *  热敏打印机纸张大约宽度（4cm*6cm）, 1 cm = 37.5 pixels
//     */
//    /**
//     *  热敏打印机纸张大约高度（4cm*6cm）
//     *  300dpi下，1厘米=118.11像素
//     *  一般是 1厘米28像素
//     */
//    private static int factor = 28;
//    /**
//     *
//     * @param code 编码
//     * @param path 二维码存放路劲
//     * @return 最新图片生成路径
//     */
//    public static BufferedImage CartoonBoxQRCode(String productSNs, String barCodeStr, String productName,
//            String productType, String productColor, String productPartNumber,
//            String createTimeStr, String QTY, String DIM, String NW, String GW) throws Exception{
//        double picWidth = 10.5;
//        double picHeight = 9.5;
//        int width = (int)(picWidth * factor);
//        int height = (int)(picHeight * factor);
//
//        double ratio = picHeight / picWidth;
//
//        int barCodeWidth = (int)(width * 0.8);
//        BufferedImage barCodeImage = getBarCode(barCodeStr,barCodeWidth);//GoogleBarCodeUtils.generateBarCode(barCodeStr);
//        Log.info("barcode width: "+barCodeImage.getWidth() + " total width: "+ width);
//        if(barCodeImage.getWidth()>=width){
//            width = barCodeImage.getWidth() + 10;
//            height = (int)(width * ratio);
//        }
//        //根据一维码得到图片缓冲区
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        //MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
//        //设置UTF-8， 防止中文乱码
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        //设置二维码四周白色区域的大小
//        hints.put(EncodeHintType.MARGIN,0);
//        //设置二维码的容错性
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//        String qrCodeStr = "中箱号:"+barCodeStr+";"+"品名:"+productName+";"+"型号:"+productType+";"+"料号:"+productPartNumber+";"+
//            "装箱数量:"+QTY;//+";"+"产品SN号:"+productSNs;
//        Log.info(qrCodeStr);
//        int qrCodeSize = (int)((double)width*0.4);
//        BitMatrix bitMatrix = null;
//        try{
//            bitMatrix = qrCodeWriter.encode(qrCodeStr, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);
//        }catch(Exception e){
//            Log.error("",e);
//            Log.info("yes Exception");
//        }
//        BufferedImage qrCode = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//        //byte[] barCodeByte = BarCodeUtils.generateBarCode128(barCodeStr, 3D, null, true, false);
//        //InputStream is = new ByteArrayInputStream(barCodeByte);
//        //BufferedImage barCodeImage = ImageIO.read(is);
//
//        //得到它的绘制环境(这张图片的笔)
//        Graphics2D g2 = (Graphics2D) bi.getGraphics();
//        setGraphics2D(g2);
//        setColorWhite(g2);
//        setDrawRect(g2,width-1,height-1);
//        //setDrawRectDottedLine(g2);
//        //g2.fillRect(0, 0, width, height);
//        //设置颜色
//        g2.setColor(Color.WHITE);
//        Color color=new Color(0, 0, 0);
//
//        g2.setFont(new Font("微软雅黑", Font.BOLD, 25));
//        g2.setColor(color);
//        g2.drawString("素士", 5, 30);
//
//        g2.drawImage(qrCode, width-qrCode.getWidth()-5, 15, qrCode.getWidth(), qrCode.getHeight(),null);
//        //g2.drawImage(logoImage, 5, 15, 100, 20,null);
//
//        int infoUpY = g2.getFontMetrics().getHeight()+10;
//        int fontsize =(int)((double)width * 0.05);
//        g2.setFont(new Font("微软雅黑", Font.PLAIN, fontsize));
//        g2.setColor(color);
//        int maxLineWidth = (int)(0.6 * (double)width - 10 - fontsize * 2);
//        Pair<String,Integer> infoUpPair = infoUp(g2, maxLineWidth, productName, productType, productColor, productPartNumber);
//        drawString(g2, infoUpPair.getKey(), 5, infoUpY);
//
//        int lineY = Math.max(qrCodeSize + 20,infoUpY+g2.getFontMetrics().getHeight()*infoUpPair.getValue()+10);
//        g2.drawLine(0, lineY, width, lineY);
//
//        int infoDownY = lineY + 10;
//        String infoDownStr = infoDown(createTimeStr, QTY, DIM, NW, GW);
//        g2.setFont(new Font("微软雅黑", Font.PLAIN, (int)((double)fontsize*0.8)));
//        drawString(g2, infoDownStr, 5, infoDownY);
//
//        int barCodeX = (width - barCodeImage.getWidth())/2;
//        int barCodeY = infoDownY + g2.getFontMetrics().getHeight()*3 + 10;
//        g2.drawImage(barCodeImage, barCodeX, barCodeY, barCodeImage.getWidth(), barCodeImage.getHeight(),null);
//
//        g2.setFont(new Font("微软雅黑", Font.PLAIN, (int)((double)fontsize*0.8)));
//        int barStrX = (width - g2.getFontMetrics().stringWidth(barCodeStr))/2;
//        int barStrY = barCodeY + barCodeImage.getHeight() + 15;
//        g2.drawString(barCodeStr, barStrX, barStrY);
//
//        g2.dispose();
//        bi.flush();
//        return bi;
//    }
//
//    public static String CartoonBoxQRCode(String productSNs, CartoonBoxSN cartoonBoxSN, String version){
//        String zplCode = null;
//            if(version.equals("sushi")){
//                String newProductSNs = "";
//                    for(String productSn:productSNs.split(",")){
//                       newProductSNs += productSn + "_0D_0A"; 
//                    }
//                    String barCodeStr = cartoonBoxSN.getNickName();
//                    String productName = cartoonBoxSN.getProductName();
//                    String productType = cartoonBoxSN.getProductType();
//                    String productColor = cartoonBoxSN.getProductColor();
//                    String productPartNumber = cartoonBoxSN.getProductPartNumber();
//                    String createTimeStr = cartoonBoxSN.getCreateDate();
//                    createTimeStr = createTimeStr.split(" ")[0];
//                    String QTY = cartoonBoxSN.getQuantity() + "PCS";
//                    String DIM = MainUtils.float2IntOrFloat(cartoonBoxSN.getBoxLength()) + "*" + MainUtils.float2IntOrFloat(cartoonBoxSN.getBoxWidth())
//                        + "*" + MainUtils.float2IntOrFloat(cartoonBoxSN.getBoxHeight()) + "mm";
//                    String NW = cartoonBoxSN.getBoxNetWeigth() + "KG";
//                    String GW = cartoonBoxSN.getBoxGrossWeight() + "KG";
//
//                    zplCode = """
//                        ^XA
//                        ^JMA^LL1260^PW1260^MD10^PR2^PON^LRN^LH0,0
//                        ^FO50,50^GFA,7808,7808,64,,::::::::::M06kT038,L01FkT078,L03FkS01FC,L0FF8kR03FC,K01FF8kR0FFE,K03FFCT03FFgJ01FF8gJ0FFCX03FFX01FFE,K07FFCS0KFCgG07JFEgG03KFV0KFCV03IF,K0IFER07LF8Y03LFCY01LFET07LF8U07IF,J01IFEQ03NFX01NF8X0NFCR03NFU0JF8,J03JFQ0OFEW07OFW03OF8Q0OFET0JF8,J07JFP03PF8U01PFCV0PFEP03PF8R01JFC,J0KFP0QFCU07PFEU03QFP0QFCR03JFC,J0KF8N03RFT01RF8T0RFCN03RFR07JFC,I01KF8N07RFCS03RFES01SFN07RFCQ07JFE,I03KFCM01SFES0TFS07SFM01SFEQ0KFE,I03KFCM03TFR01TF8R0SFEM03TFP01KFE,I07KFN07TFCQ03TFEQ01SFCM07TFCO01KF8,I07JFEN0UFEQ07UFQ03SFCM0UFEO03KF,I0KFCM01VFQ0VF8P07SF8L01VFO03JFE,I0KFN03VF8O01VFCP0TFM03VF8N07JFC,I0JFEN07VFCO03VFEO01TFM07VFCN07JF8,001JFEN0WFEO07WFO03SFEM0WFEN07JF,001JFCM01WFEO0XFO07SFEL01WFEN0JFE,001JF8M03MFI01MFN01MF8I0MF8N0MFCI07FFCL03MFI01MFN0JFE,003JF8M07LF8J03LF8M03LFCJ01LFCM01LFEK0FFCL07LF8J03LF8M0JFC,003JFN07KFCL0LFCM03KFEL07KFEM01LFL03F8L07KFCL0LFCM0JFC,003JFN0LF8L03KFCM07KFCL01KFEM03KFEM0F8L0LF8L03KFCL01JF8,003JFM01KFEM01KFEM0LFN0LFM07KF8M07L01KFEM01KFEL01JF8,003IFEM01KFCN07KFM0KFEN03KF8L07KFN01L01KFCN07KFL01JF8,007IFEM03KFO03KFL01KF8N01KF8L0KFCU03KFO03KFL01JF,007IFEM03JFEO01KF8K01KFP0KFCL0KF8U03JFEO01KF8K01JF,007IFEM07JFCP0KF8K03JFEP07JFCK01KFV07JFCP0KF8K01JF,007IFEM07JF8P07JFCK03JFCP03JFEK01JFEV07JF8P07JFCK01JF,007IFEM0KFQ03JFCK07JF8P01JFEK03JFCV0KFQ03JFCK01JF,007IFEM0KFQ01JFEK07JF8Q0KFK03JFCV0KFQ01JFEK01JF,007IFEL01JFER0JFEK0KFR07JFK07JF8U01JFER0JFEK01JF,007IFEL01JFCR0KFK0JFER07JF8J07JFV01JFCR0JFEK01JF,007IFEL01JFCR07JFJ01JFER03JF8J0KFV01JFCR07JFK01JF8,007IFEL03JF8R03JFJ01JFCR01JF8J0JFEV03JF8R03JFK01JF8,007IFEL03JFS03JF8I01JF8R01JFCJ0JFCV03JFS03JFK01JF8,003JFL03JFS01JF8I01JF8S0JFCJ0JFCV03JFS01JF8J01JF8,003JFL07JFS01JF8I03JF8S0JFCI01JFCV07JFS01JF8J01JFC,003JFL07IFES01JF8I03JFT0JFCI01JF8V07IFES01JF8K0JFC,003JF8K07IFET0JF8I03JFT07IFCI01JF8V07IFET0JF8K0JFC,001JF8K07IFET0JFCI03JFT07IFEI01JF8V07IFET0JF8K0JFE,001JFCK07IFCT0JFCI03IFET07IFEI01JFW07IFCT0JFCK0JFE,001JFCK0JFCT0JFCI07IFET07IFEI03JFW0JFCT0JFCK07JF,I0JFEK0JFCT07IFCI07IFET03IFEI03JFW0JFCT07IFCK07JF,I0JFEK0JFCT07IFCI07IFET03IFEI03JFW0JFCT07IFCK07JF8,I0KFK0JFCT07IFCI07IFET03IFEI03JFW0JFCT07IFCK03JF8,I07JFK0JFCT07IFCI07IFET03IFEI03IFEW0JFCT07IFCK03JFC,I07JF8J0JF8T07IFCI07IFCT03IFEI03IFEW0JF8T07IFCK01JFC,I03JF8J0JF8T07IFCI07IFCT03IFEI03IFEW0JF8T07IFCK01JFE,I03JFCJ0JF8T07IFCI07IFCT03IFEI03IFEW0JF8T07IFCL0JFE,I01JFCJ0JFCT07IFCI07IFET03IFEI03JFW0JFCT07IFCL0KF,I01JFCJ0JFCT07IFCI07IFET03IFEI03JFW0JFCT07IFCL07JF,J0JFEJ0JFCT07IFCI07IFET03IFEI03JFW0JFCT07IFCL07JF,J0JFEJ0JFCT07IFCI07IFET03IFEI03JFW0JFCT07IFCL03JF8,J07IFEJ0JFCT0JFCI07IFET07IFEI03JFW0JFCT0JFCL03JF8,J07JFJ07IFCT0JFCI03IFET07IFEI01JFW07IFCT0JFCL03JF8,J07JFJ07IFET0JFCI03JFT07IFEI01JF8V07IFET0JFCL01JFC,J03JFJ07IFET0JF8I03JFT07IFCI01JF8V07IFET0JFCL01JFC,J03JF8I07IFES01JF8I03JFT0JFCI01JF8V07IFES01JFCM0JFC,J01JF8I07JFS01JF8I03JF8S0JFCI01JFCV07JFS01JFCM0JFE,J01JF8I03JFS03JF8I01JF8R01JFCJ0JFCV03JFS03JFCM07IFE,K0JFCI03JF8R03JFJ01JFCR01JF8J0JFEV03JF8R03JFCM07IFE,K0JFCI03JF8R07JFJ01JFCR03JF8J0JFEV03JF8R07JFCM07IFE,K0JFCI01JFCR07JFK0JFER03JF8J07JFV01JFCR07JFCM03IFE,K0JFCI01JFCR0JFEK0JFER07JFK07JFV01JFCR0KFCM03JF,K07IFCI01JFEQ01JFEK0KFR0KFK07JF8U01JFEQ01KFCM03JF,K07IFCJ0KFQ01JFEK07JF8Q0KFK03JFCV0KFQ01KFEM03JF,K07IFCJ0KF8P03JFCK07JFCP01JFEK03JFEV0KF8P03KFEM03JF,K07IFCJ07JFCP07JFCK03JFCP03JFEK01JFEV07JFCP07KFEM03JF,K07IFCJ07JFCP0KF8K03JFEP07JFCK01KFV07JFCP0LFEM03JF,K07IFCJ03KFO01KF8K01KF8O0KFCL0KFCU03KFO01LFEM03JF,K07IFCJ03KF8N03KFL01KFCN01KF8L0KFEU03KF8N03LFEM03JF,K0JFCJ01KFCN0LFM0KFEN07KF8L07KFN03L01KFCN0MFEM03JF,K0JFCJ01LFM01KFEM0LF8M0LFM07KFCM07M0LFM01MFEM03IFE,K0JFCK0LF8L07KFCM07KFCL03KFEM03KFEL01F8L0LF8L07NFM07IFE,J01JFCK07KFEK01LFCM03LFL0LFEM01LF8K07F8L07KFEK01OFM07IFE,J01JF8K03LFCJ07LF8M01LFEJ03LFCN0MFJ01FFCL03LFCJ07OFM07IFE,J01JF8K03MFC007MFN01MFE003MF8N0NF001IFCL03MFC007PF8L0JFE,J03JF8K01WFEO0XFO07SFEL01gGF8L0JFC,J07JFM0WFCO07VFEO03SFEM0gGF8K01JFC,J07JFM07VF8O03VFCO01TFM07gFCK03JFC,J0KFM03VFP01VF8P0TF8L03gFCK07JF8,I01JFEM01UFEQ0VFQ07SF8L01UFE7IFEK0KF8,I03JFEN0UFCQ07TFEQ03SFCM0UFC3IFCJ01KF,I07JFCN07TF8Q03TFCQ01SFCM07TF83IFK03KF,001KFCN03TFR01TF8R0SFEM03TF01FFEK07JFE,003KF8O0SFES07SFS03SFN0SFE01FFCJ01KFE,007KF8O07RF8S03RFCS01RFEN07RF800FF8J01KFC,003KFP01RFU0RF8T07QFCN01RFI0FFK01KFC,003JFEQ07PFCU03PFEU01QFP07PFCI07EL0KF8,001JFEQ03PFV01PF8V0PFCP03PFJ078L0KF,001JFCR07NFCW03NFEW01OFR07NFCJ03M07IFE,I0JF8R01MFEY0NFY07MF8R01MFES07IFE,I0JFT03LFg01LF8g0LFCT03LFT03IFC,I07FFEU03JFgH01JF8gH0JFCV03JFU03IF8,I07FFCkQ01IF,I03FF8kQ01FFE,I03FFkS0FFC,I01FEkS0FF,I01F8kS07E,J0FkT078,J0CkT03,,::::^FS
//
//                        ^CWJ,E:FUCK.TTF
//                        ^SEE:GB18030.DAT^CI26
//
//                        ^FO50,220^AJN,40,40^FD品名:  %s^FS
//                        ^FO50,320^AJN,40,40^FD型号:  %s^FS
//                        ^FO50,420^AJN,40,40^FD颜色:  %s^FS
//                        ^FO50,520^AJN,40,40^FD料号:  %s^FS
//
//                        ^FO50,600^GB1100,3,3^FS
//                        ^FO50,650^AJN,40,40^FD生产日期:  %s^FS
//                        ^FO50,750^AJN,40,40^FDQTY: %s    DIM: %s^FS
//                        ^FO50,850^AJN,40,40^FDN.W: %s    G.W: %s^FS
//                        ^FO150,920^BY4^BCN,100,N,N,N^FD%s^FS
//                        ^FO150,1050^AJ,50^FB1000,1,0,C^FD%s^FS
//
//                        ^XZ
//
//                        ^XA
//                        ^JMA^LL1260^PW1260^MD10^PR2^PON^LRN^LH0,0
//                        ^SEE:GB18030.DAT^CI26                        
//                        ^FO130,90
//                        ^BQN,2,6
//                        ^FH^FDLM,B4000
//                        中箱箱号：%s_0D_0A
//                        品名：%s_0D_0A
//                        型号：%s_0D_0A
//                        料号：%s_0D_0A
//                        装箱数量：%s_0D_0A
//                        产品SN号：_0D_0A
//                        %s
//                        ^FS
//                        ^XZ
//                        """;
//
//                    zplCode = String.format(zplCode,productName,productType,productColor,productPartNumber,createTimeStr,QTY,DIM,NW,GW,barCodeStr,barCodeStr,barCodeStr,productName,productType,
//                            productPartNumber,QTY,newProductSNs);
//
//            }
//            if(version.equals("xiaomi")){
//                String DPIstr = MainUtils.getDict("system", "DPI");
//                double DPI = 300;
//                if(DPIstr!=null && !DPIstr.isEmpty()){
//                    DPI = Double.valueOf(DPIstr);
//                }
//                String jsonstr = JSON.toJSONString(cartoonBoxSN);
//                String zpl1 = ZplPrinter.printFile("zpl/中箱标签.zpl", jsonstr, DPI);
//                Map<String,String> map = new HashMap<String,String>();
//                map.put("boxSN", cartoonBoxSN.getSNCode());
//                String[] prods = productSNs.split(",");
//                for(int i=1; i<=prods.length; i++){
//                    map.put("productSN"+i, prods[i-1]);
//                }
//                String tmpJson = JSON.toJSONString(map);
//                String zpl2 = ZplPrinter.printFile("zpl/中箱唯一码标签.zpl", tmpJson, DPI);
//                String zpl3 = ZplPrinter.printFile("zpl/中箱料号标签.zpl", jsonstr, DPI);
//                zplCode = zpl1 + zpl2 + zpl3;
//            }
//            return zplCode;
//    }
//    /** 条形码高度 */
//    private static final int HEIGHT = 40;
//
//    /** 加文字 条形码 */
//    private static final int WORDHEIGHT = 75;
//
//    /**
//     * 设置 条形码参数
//     */
//    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
//        private static final long serialVersionUID = 1L;
//        {
//            // 设置编码方式
//            put(EncodeHintType.CHARACTER_SET, "utf-8");
//        }
//    };
//
//    public static BufferedImage getBarCode(String vaNumber, int width){
//        Code128Writer writer = new Code128Writer();
//		// 编码内容, 编码类型, 宽度, 高度, 设置参数
//		BitMatrix bitMatrix = writer.encode(vaNumber, BarcodeFormat.CODE_128, width, HEIGHT, hints);
//		return MatrixToImageWriter.toBufferedImage(bitMatrix);
//    }
//
//    public static BufferedImage getBarCode(String vaNumber, int width, int height){
//        Code128Writer writer = new Code128Writer();
//		// 编码内容, 编码类型, 宽度, 高度, 设置参数
//		BitMatrix bitMatrix = writer.encode(vaNumber, BarcodeFormat.CODE_128, width, height, hints);
//		return MatrixToImageWriter.toBufferedImage(bitMatrix);
//    }
//
//
//    public static BufferedImage emptyQRImage() throws Exception{
//        double picWidth = 10.5;
//        double picHeight = 9.5;
//        int width = (int)(picWidth * factor);
//        int height = (int)(picHeight * factor);
//
//        //得到图片缓冲区
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//
//        //得到它的绘制环境(这张图片的笔)
//        Graphics2D g2 = (Graphics2D) bi.getGraphics();
//        g2.fillRect(0, 0, width, height);
//        //g2.setColor(Color.WHITE);
//        g2.setFont(new Font("黑体", Font.BOLD, 18));
//        g2.setColor(Color.BLACK);
//        drawString(g2, "无预览", width/2, height/2);
//        return bi;
//    }
//
//    public static Pair<String,Integer> formatStr(String text, Graphics2D g2, int maxLineWidth){
//        // num 需要换行的字数
//        String newText = "";
//        boolean enter = false;
//        String oneline = "";
//        int lineCount = 1;
//        for (int i=0;i<text.length();i++){
//            char word = text.charAt(i);
//            oneline += word;
//            int lineWidth = g2.getFontMetrics().stringWidth(oneline);
//            if(lineWidth>=maxLineWidth){
//                newText = newText + "\n        ";
//                enter = true;
//                oneline = "";
//                lineCount += 1;
//            }
//            if (enter && word == ' '){
//                Log.info("catch enter");
//                continue;
//            }
//            newText = newText + word;
//            enter = false;
//        }
//        newText = newText + "\n";
//        return new Pair<String,Integer>(newText,lineCount);
//    }
//    public static Pair<String,Integer> infoUp(Graphics2D g2, int maxLineWidth, String nameStr,String typeStr,String colorStr,String partStr){
//        int lineCount = 0;
//        Pair<String,Integer> productName = formatStr(nameStr,g2, maxLineWidth);
//        Pair<String,Integer> productType = formatStr(typeStr,g2, maxLineWidth);
//        Pair<String,Integer> productColor = formatStr(colorStr,g2, maxLineWidth);
//        Pair<String,Integer> productPart = formatStr(partStr,g2, maxLineWidth);
//        String newText = "品名: " + productName.getKey()
//            + "型号: " + productType.getKey() 
//            + "颜色: " + productColor.getKey()
//            + "料号: " + productPart.getKey();
//        lineCount = productName.getValue() + productType.getValue() + productColor.getValue() + productPart.getValue();
//        return new Pair<String,Integer>(newText,lineCount);
//    }
//    public static String infoDown(String dateStr,String QTYStr,String dimStr,String nwStr,String gwStr){
//        String text = "生产日期: " + dateStr + "\n"
//            + "QTY: " + QTYStr + "  " 
//            + "DIM: " + dimStr + "\n"
//            + "N.W: " + nwStr + "  "
//            + "G.W: " + gwStr + "\n";
//        return text;
//    }
//    public static String fillBlank(int total,int baseNum){
//        // 总长 15
//        String text = "";
//        for (int i=0; i<(total-baseNum);i++){
//            text += " ";
//        }
//        return text;
//    }
//
//    public static void drawString(Graphics g, String text, int x, int y) {
//    for (String line : text.split("\n"))
//        g.drawString(line, x, y += g.getFontMetrics().getHeight());
//    }
//
//    public static void image2base64(String path) throws Exception{
//        byte[] fileContent = FileUtils.readFileToByteArray(new File(path));
//        String encodedString = Base64.getEncoder().encodeToString(fileContent);
//        Log.info(encodedString);
//    }
//    public static BufferedImage base64ToImage(String encodedString) throws Exception{
//        byte[] decodedBytes = Base64.getDecoder().decode(encodedString); 
//        InputStream is = new ByteArrayInputStream(decodedBytes);
//        BufferedImage newBi = ImageIO.read(is);
//        return newBi;
//    }
//
//
//    /**
//     * 设置 Graphics2D 属性  （抗锯齿）
//     *
//     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
//     */
//    private static void setGraphics2D(Graphics2D g2d) {
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
//        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
//        g2d.setStroke(s);
//    }
//
//    /**
//     * 设置背景为白色
//     *
//     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
//     */
//    private static void setColorWhite(Graphics2D g2d) {
//        g2d.setColor(Color.WHITE);
//        //填充整个屏幕
//        g2d.fillRect(0, 0, 600, 600);
//        //设置笔刷
//        g2d.setColor(Color.BLACK);
//    }
//
//    /**
//     * 设置边框
//     *
//     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
//     */
//    private static void setDrawRect(Graphics2D g2d, int width, int height) {
//        //设置笔刷
//        g2d.setColor(Color.DARK_GRAY);
//        g2d.drawRect(0, 0, width, height);
//    }
//
//    /**
//     * 设置边框虚线点
//     *
//     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
//     */
//    private static void setDrawRectDottedLine(Graphics2D g2d) {
//        //设置笔刷
//        g2d.setColor(Color.BLUE);
//        BasicStroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_ROUND,
//            BasicStroke.JOIN_ROUND, 0.5f, new float[]{1, 4}, 0.5f);
//        g2d.setStroke(stroke);
//        g2d.drawRect(0, 0, 435, 230);
//    }
//    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) throws IOException {
//            int orgWidth = originalImage.getWidth();
//            int orgHeight = originalImage.getHeight();
//            int targetHeight = (int)((double)targetWidth * ((double)orgHeight/(double)orgWidth));
//            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
//            Graphics2D graphics2D = resizedImage.createGraphics();
//            graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
//            //graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//            graphics2D.dispose();
//            return resizedImage;
//    }
//
//}

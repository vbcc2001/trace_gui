//package com.dlsc.workbenchfx.demo;
// 
// 
//import javax.imageio.ImageIO;
//
//import com.alibaba.fastjson2.JSON;
//import com.dlsc.workbenchfx.demo.modules.palletSN.model.PalletSN;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//
//import org.apache.commons.io.FileUtils;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
// 
///**
// * @author
// */
//public class PalletBarCodeUtils {
// 
//    /**
//     *  热敏打印机纸张大约宽度（4cm*6cm）, 1 cm = 37.5 pixels
//     */
//    /**
//     *  热敏打印机纸张大约高度（4cm*6cm）
//     */
//    private static int factor = 38;
//    /**
//     *
//     * @param code 编码
//     * @param path 二维码存放路劲
//     * @return 最新图片生成路径
//     */
//    public static BufferedImage palletBarCode(String fromArea, String toArea, String barCodeStr) throws Exception{
//        return GoogleBarCodeUtils2.generateBarCode(barCodeStr,fromArea,toArea);
//    }
//
//    public static String palletBarCode(String cartoonBoxSNs, PalletSN palletSN, String version) throws Exception{
////        BufferedImage bufferedImage = null;
//        String zplstr = null;
//        if (version.equals("sushi")){
//            zplstr = """
//                ^XA
//                ^JMA^LL1200^PW1200^MD10^PR2^PON^LRN^LH0,0
//                ^CI26
//                ^FO200,700^BY5^BCN,200,Y,N,N^FD%s^FS
//                ^CWJ,E:FUCK.TTF
//                ^SEE:GB18030.DAT^CI26
//                ^FO250,100^AJN,100,100^FD出货地: %s^FS
//                %s
//                ^XZ
//                """;
//            zplstr = String.format(zplstr,palletSN.getSNCode(),palletSN.getFromArea(),MultiLineString("目的地: "+palletSN.getToArea(),250,250,100,1200));
//
//        }
//        if (version.equals("xiaomi")){
//                String DPIstr = MainUtils.getDict("system", "DPI");
//                double DPI = 300;
//                if(DPIstr!=null && !DPIstr.isEmpty()){
//                    DPI = Double.valueOf(DPIstr);
//                }
//                String jsonstr = JSON.toJSONString(palletSN);
//
//                String zpl1 = ZplPrinter.printFile("zpl/栈板唯一码标签.zpl", jsonstr, DPI);
//                String zpl2 = ZplPrinter.printFile("zpl/栈板标签.zpl", jsonstr, DPI);
//                zplstr = zpl1 + zpl2;
//        }
//        return zplstr;
//    }
//
//    public static String MultiLineString(String str, int startX, int startY, int fontSize, int labelWidth){
//                //^FO250,250^AJN,100,100^FD目的地: %s^FS
//        String zplstr = "";
//        int spaceLeft = labelWidth - startX;
//        int lineCount = str.length() * fontSize / spaceLeft ;
//        if(spaceLeft*lineCount < str.length()*fontSize){
//            lineCount += 1;
//        }
//        int num = spaceLeft / fontSize;
//
//        int Y = startY - fontSize;
//        int begin = 0;
//        int end = num;
//        for (int i=0; i<lineCount; i++){
//            if(end>str.length()){
//                end = str.length();
//            }
//            String substr = str.substring(begin,end);
//            Y = Y + fontSize + 5;
//            String tmpstr = """
//                ^FO%s,%s^AJN,%s,%s^FD%s^FS
//                """;
//            tmpstr = String.format(tmpstr,String.valueOf(startX),String.valueOf(Y),String.valueOf(fontSize),String.valueOf(fontSize),substr);
//            Log.info(tmpstr);
//            zplstr += tmpstr;
//            begin = end;
//            end = end + num;
//        }
//        return zplstr;
//    }
//
//
//    public static BufferedImage emptyQRImage() throws Exception{
//        double picWidth = 12;
//        double picHeight = 11;
//        int width = (int)picWidth * factor;
//        int height = (int)picHeight * factor;
//        //得到图片缓冲区
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//
//        //得到它的绘制环境(这张图片的笔)
//        Graphics2D g2 = (Graphics2D) bi.getGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        g2.fillRect(0, 0, width, height);
//        //g2.setColor(Color.WHITE);
//        g2.setFont(new Font("黑体", Font.BOLD, 18));
//        g2.setColor(Color.BLACK);
//        drawString(g2, "无预览", width/2, height/2);
//        return bi;
//    }
//
//    public static String formatStr(String text, int num){
//        // num 需要换行的字数
//        String newText = "";
//        boolean enter = false;
//        for (int i=0;i<text.length();i++){
//            char word = text.charAt(i);
//            if((i+1+4) % num == 0){
//                newText = newText + "\n        ";
//                enter = true;
//            }
//            if (enter && word == ' '){
//                Log.info("catch enter");
//                continue;
//            }
//            newText = newText + word;
//            enter = false;
//        }
//        newText = newText + "\n";
//        return newText;
//    }
//    public static String infoUp(String nameStr,String typeStr,String colorStr,String partStr){
//        String newText = "品名: " + formatStr(nameStr,13)
//            + "型号: " + formatStr(typeStr,13)
//            + "颜色: " + formatStr(colorStr,13)
//            + "料号: " + formatStr(partStr,16);
//        return newText;
//    }
//    public static String infoDown(String dateStr,String QTYStr,String dimStr,String nwStr,String gwStr){
//        String text = "生产日期: " + dateStr + "\n"
//            + "QTY: " + QTYStr + fillBlank(13,QTYStr.length()+4) 
//            + "DIM: " + dimStr + "\n"
//            + "N.W: " + nwStr + fillBlank(13, nwStr.length()+4)
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
//}

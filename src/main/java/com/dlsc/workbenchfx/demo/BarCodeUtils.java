//package com.dlsc.workbenchfx.demo;
//
//import org.apache.commons.lang.ObjectUtils;
//import org.krysalis.barcode4j.HumanReadablePlacement;
//import org.krysalis.barcode4j.impl.code128.Code128Bean;
//import org.krysalis.barcode4j.impl.code128.Code128Constants;
//import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
//import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
//import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;
//import org.krysalis.barcode4j.tools.UnitConv;
//
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//
///**
// * 条形码工具类
// *
// * @author 明明如月
// * @date 2018/08/13
// */
//public class BarCodeUtils {
//
//
//    /**
//     * 生成code128条形码
//     *
//     * @param height        条形码的高度
//     * @param width         条形码的宽度
//     * @param message       要生成的文本
//     * @param withQuietZone 是否两边留白
//     * @param hideText      隐藏可读文本
//     * @return 图片对应的字节码
//     */
//    public static byte[] generateBarCode128(String message, Double height, Double width, boolean withQuietZone, boolean hideText) {
//        Code128Bean bean = new Code128Bean();
//        // 分辨率
//        int dpi =300;
//        bean.setCodeset(Code128Constants.CODESET_B);
//        // 设置两侧是否留白
//        //bean.doQuietZone(withQuietZone);
//        bean.doQuietZone(true);
//
//        // 设置条形码高度和宽度
//        bean.setBarHeight((double) ObjectUtils.defaultIfNull(height, 12.0D));
//        bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
//        if (width != null) {
//            bean.setModuleWidth(width);
//        }
//        bean.setFontSize(1.0D);
//        bean.setFontName("OCR-B");
//        // 设置文本位置（包括是否显示）
//        if (hideText) {
//            bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
//        }
//        // 设置图片类型
//        String format = "image/png";
//
//        ByteArrayOutputStream ous = new ByteArrayOutputStream();
//        BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
//                BufferedImage.TYPE_BYTE_BINARY, false, 0);
//
//
//        // 生产条形码
//        bean.generateBarcode(canvas, message);
//        try {
//            canvas.finish();
//        } catch (IOException e) {
//            //ByteArrayOutputStream won't happen
//        }
//
//        return ous.toByteArray();
//    }
//    public static BufferedImage generateEAN13BarcodeImage(String barcodeText) {
//
//        EAN13Bean barcodeGenerator = new EAN13Bean();
//        BitmapCanvasProvider canvas = 
//            new BitmapCanvasProvider(160, BufferedImage.TYPE_BYTE_BINARY, false, 0);
//
//        barcodeGenerator.generateBarcode(canvas, barcodeText);
//        return canvas.getBufferedImage();
//    }
//
//    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
//        QRCodeWriter barcodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = 
//            barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
//
//        return MatrixToImageWriter.toBufferedImage(bitMatrix);
//    }
//
//    public static String generateLaserImage(String barcodeText, String version){
//        String zplCode  = null;
//        if (version.equals("sushi")){
//            zplCode = """ 
//                ^XA
//                ^CWJ,E:FUCK.TTF
//                ^SEE:GB18030.DAT^CI26
//                ^JMA^LL448^PW448^MD10^PR2^PON^LRN^LH0,0
//                ^FO10,15^BY2^BCN,80,N,N,N^FD%s^FS
//                ^FO10,102^AJN,30^FB438,1,0,C^FD%s\\&^FS
//                ^XZ
//                """;
//            zplCode = String.format(zplCode,barcodeText,barcodeText);
//        }
//        if (version.equals("xiaomi")){
//            ProductLaser productLaser = null;
//            if (!barcodeText.equals("")){
//                int pageIndex = 0;
//                int pageCount = 1;
//                String uri = String.format("/productLaser/getByCode?SNCode=%s&pageIndex=%d&pageSize=%d",barcodeText,pageIndex,pageCount);
//                String text = MainUtils.post("{}",
//                        GlobalConfig.getInstance().getServerUrl()+uri);
//                Log.info(uri);
//                Log.info(text);
//                JSONObject json = JSON.parseObject(text);
//                JSONObject data = json.getJSONObject("data");
//                pageCount = data.getIntValue("pageCount");
//                JSONArray content = data.getJSONArray("content");
//                for (int i=0; i< content.size(); i++){
//                    productLaser = JSON.parseObject(content.getString(i), ProductLaser.class);
//                }
//            }
//            String snCode = productLaser.getSNCode();//"43563AFAJKF2VS00001";
//            String code69 = productLaser.getCode69();//"6941812701577";
//            String productName = productLaser.getProductName();//"米家电动剃须刀 S101";
//            String productColor = productLaser.getProductColor();//"暮光蓝";
//            String SKU = productLaser.getSKU();//"BHR6726CN";
//            String[] createDate = productLaser.getCreateDate().split(" ")[0].split("-");//"2022.06.01";
//            String datestr = createDate[0]+"."+createDate[1];
//            zplCode = """
//                ^XA
//                %s
//                %s
//                ^FO10,10^AJN,20,20^FD%s^FS
//                ^FO380,10^AJN,20,20^FD颜色:%s^FS
//                ^FO8,35^BY2^BCN,60,N,N,N^FD%s^FS
//                ^FO10,110^AJN,20,20^FDSN:%s^FS
//                ^FO350,110^AJN,20,20^FDSKU:%s^FS
//                ^FO60,150^BY4^BEN,100,Y,N^FD%s^FS
//                ^FO10,300^AJN,20,20^FD生产日期:%s^FS
//                ^FO380,300^GB100,25,2^FS
//                ^FO380,305^AJN,15,15^FD合格证 已检验^FS
//                ^XZ
//                """;
//        zplCode = String.format(zplCode,ZplPrinter.setFont("FUCK.TTF"),
//                ZplPrinter.labelSize(42),
//                productName,
//                productColor,
//                snCode,
//                snCode,
//                SKU,
//                code69,
//                datestr
//                );
//
//        }
//        return zplCode;
//    }
//}

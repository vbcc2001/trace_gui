package com.dlsc.workbenchfx.demo;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterName;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.zpl.ZPLLabel;
import com.dlsc.workbenchfx.demo.api.CartoonBoxAPI;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;
import com.dlsc.workbenchfx.demo.zplTemplate.TPInterface;


public class ZplPrinter {
    private String printerURI = null;//打印机完整路径
    private PrintService printService = null;//打印机服务  
    private boolean mock = false;
    private boolean setTransPose = false;

    private int dpi = 300;
    private String version = null;    

    /**
     * 构造方法
     * @param printerURI 打印机路径
     */
    public ZplPrinter(String printerURI, String printerDPI){
        version = LocalCache.getCache().getClientType();
        this.printerURI = printerURI;
        this.dpi = Integer.valueOf(printerDPI);
        if (printerURI.equals("mock")){
            mock = true;
            return;
        }
        //初始化打印机
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null,null);
        if (services != null && services.length > 0) {
            for (PrintService service : services) {
                if (printerURI.equals(service.getName())) {
                    printService = service;
                    break;
                }
            }
        }
        if (printService == null) {
            Log.info("没有找到打印机：["+printerURI+"]");
            //循环出所有的打印机
            if (services != null && services.length > 0) {
                Log.info("可用的打印机列表：");
                for (PrintService service : services) {
                    Log.info("["+service.getName()+"]");
                }
            }
        }else{
            Log.info("找到打印机：["+printerURI+"]");
            Log.info("打印机名称：["+printService.getAttribute(PrinterName.class).getValue()+"]");
        }
    }
    /**
     * 打印
     * @param zpl 完整的ZPL
     * @param PORTRAIT = True 打竖着
     */
    public boolean print(String zpl,int width, int height, boolean PORTRAIT){
        if(printService==null){
            Log.info("打印出错：没有找到打印机：["+printerURI+"]");
            return false;
        }
        DocPrintJob job = printService.createPrintJob();
        DocAttributeSet das = new HashDocAttributeSet();
        if(PORTRAIT){
            das.add(OrientationRequested.PORTRAIT);
        }else{
            das.add(OrientationRequested.LANDSCAPE);
        }
        das.add(new MediaPrintableArea(0,0,width,height,MediaPrintableArea.MM));
        byte[] by = zpl.getBytes();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(by, flavor, das);
        try {
            job.print(doc, null);
            Log.info("已打印");
            return true;
        } catch (PrintException e) {
            Log.error("",e);
            return false;
        }
    }

    public boolean print(ZPLLabel label){
        String zplModeStr = MainUtils.getLocalCache("system", "zplMode");
        if (zplModeStr!=null && zplModeStr.equals("false")){
            return printPNG(label);
        }else{
            return printZPL(label.toZPL());
        }

    }

    public boolean printPNG(ZPLLabel label){
        BufferedImage png = label.toPNG();
        float labelWidth = label.getConfig().getLabelMMWidth();
        float labelHeight = label.getConfig().getLabelMMHeight();
        
        if (label.getConfig().isPortrait()){
            float tmp = labelWidth;
            labelWidth = labelHeight;
            labelHeight = tmp;
        }
        
        if(printService==null){
            Log.info("打印出错：没有找到打印机：["+printerURI+"]");
            return false;
        }

        DocPrintJob job = printService.createPrintJob();
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(OrientationRequested.PORTRAIT);
        pras.add(new Copies(1));
        pras.add(PrintQuality.HIGH);

        DocAttributeSet das = new HashDocAttributeSet();
        das.add(new MediaPrintableArea(0, 0, labelWidth, labelHeight, MediaPrintableArea.MM));
        DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;

        try {
            Doc doc = new SimpleDoc(bufferedImageToInputStream(png), flavor, das);
            job.print(doc, pras);
            Log.info("已打印");
            return true;
        } catch (Exception e) {
            Log.error("",e);
            return false;
        } 
    }

    public static InputStream bufferedImageToInputStream(BufferedImage image){
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try {
		    ImageIO.write(image, "png", os);
		    InputStream input = new ByteArrayInputStream(os.toByteArray());
		    return input;
	    } catch (IOException e) {
		    Log.error("",e);
	    }
	    return null;
    }


    public boolean printZPL(String zpl){        
        Log.info(zpl);
        if(printService==null){
            Log.info("打印出错：没有找到打印机：["+printerURI+"]");
            return false;
        }
        DocPrintJob job = printService.createPrintJob();

        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        try {
            byte[] by = zpl.getBytes("GB18030");
            Doc doc = new SimpleDoc(by, flavor, null);
            job.print(doc, null);
            Log.info("已打印");
            return true;
        } catch (Exception e) {
            Log.error("",e);
            return false;
        } 
    }

    /**
     *素士+小米彩盒标签
     * */
    public void printColorBox(String printerTemplate,String SNCode,String SKU,String color,String createDate,String productName,String code69) throws Exception{


        if(version.equals("sushi")){
            print(DongGuanYinFan.ColorBox(mock, dpi, SNCode));
        }else{
        	ZPLLabel label = TPInterface.getService(printerTemplate).colorBox(mock, dpi, SNCode, SKU, color, createDate, productName, code69);
            print(label);
        }
    }
    /**
     *小米中箱唯一码标签
     * @throws BizException 业务处理异常
     * */
    public void printBoxUniqueCode(String printerTemplate,String boxCode,String productName,String color, String productId, String[] productCodes) throws BizException {
        String transPose = MainUtils.getDict("cartoonBox", "printerTransPose", version);
        if(transPose!=null&&transPose.equals("转置")){
            setTransPose = true;
        }
        ZPLLabel label=TPInterface.getService(printerTemplate).boxCode( setTransPose, mock,  dpi,  boxCode,  productCodes, productName, color, productId);
        print(label);
    }

    /**
     * 素士中箱打印
     * */
    public void printSuShiBox(String[] productCodes, CartoonBoxSN cartoonBox){
        String transPose = MainUtils.getDict("cartoonBox", "printerTransPose", version);
        if(transPose!=null&&transPose.equals("转置")){
            setTransPose = true;
        }

        String productCodesString = String.join(",",productCodes);

        if(productCodes.length>39){
            print(DongGuanYinFan.box(mock, dpi, productCodesString, cartoonBox, DongGuanYinFan.Level.ONLY_BOX));
            print(DongGuanYinFan.box(mock, dpi, productCodesString, cartoonBox, DongGuanYinFan.Level.ONLY_QRCODE));
        }else{
            print(DongGuanYinFan.box(mock, dpi, productCodesString, cartoonBox, DongGuanYinFan.Level.BOTH));
        }


    }

    /**
     * 小米栈板唯一码标签
     * */
    public boolean printPalletUniqueCode(String printerTemplate,String PO,String SKU,String id,String createDate,String productName,
                                         String productColor,String pcs,String grossWeight,String palletNumber){
        String transPose = MainUtils.getDict("pallet", "printerTransPose", version);
        if(transPose!=null&&transPose.equals("转置")){
            setTransPose = true;
        }

        String printerNote = MainUtils.getDict("pallet", "printerNote", version);
        String[] boxSNs = null;
        String[] productLaserSNs = null;
        if(printerNote!=null&&printerNote.equals("带出")){
            List<CartoonBoxSN> boxs ;
            try{
                boxs= CartoonBoxAPI.getCartoonBoxByPallet(palletNumber);
            }catch(RequestException e){
                Log.error("",e);
                MainUtils.showSimpleError("网络异常:"+e.getMessage());
                return false;
            }
            List<String> list = new ArrayList<String>();
            for (CartoonBoxSN box:boxs){
                list.add(box.getNickName());
            }
            if(!list.isEmpty()){
                boxSNs = new String[list.size()];
                boxSNs = list.toArray(boxSNs);
            }
        }
        //TODO 编译错误，先注释
        if(boxSNs!=null && boxSNs.length>0){
            List<ProductLaser> productLaserSNList; 
            try{
                productLaserSNList = ProductLaserAPI.getByPalletNickName(palletNumber);
            }catch(RequestException e){
                MainUtils.showSimpleError("网络异常:"+e.getMessage());
                Log.error("",e);
                return false;
            }
            if(productLaserSNList.size()>0){
                List<String> productLasers = productLaserSNList.stream().map( i -> i.getSNCode()).distinct().collect(Collectors.toList());

                productLaserSNs = new String[productLasers.size()];
                productLaserSNs = productLasers.toArray(productLaserSNs);
            }
        }
        try{
            ZPLLabel label = TPInterface.getService(printerTemplate).palletMark(setTransPose, mock, dpi, PO, SKU, id, createDate,
                    productName, productColor, pcs, grossWeight, palletNumber, boxSNs, productLaserSNs);
            print(label);
            return true;
        }catch(Exception e){//合并v2代码，去掉写死的模板代码 by lgh 2023/02/25
            Log.error("",e);
            MainUtils.showSimpleError("模板不对! =>"+e);
            return false;
        }
    }

    /**
     *素士栈板标签
     * */
    public boolean printSuShiPallet(String id,String from,String to) {
        String transPose = MainUtils.getDict("pallet", "printerTransPose", version);
        if(transPose!=null&&transPose.equals("转置")){
            setTransPose = true;
        }
        print(DongGuanYinFan.Pallet(mock, dpi, id,from,to));
        return true;
    }


    /**
     *小米目的仓标签
     * */
    public void printDstHouse(String printerTemplate, String dstCity, String dstArea, String productId, String dateStr, String palletSN){
        String transPose = MainUtils.getDict("palletPostView", "printerTransPose", version);
        if(transPose!=null&&transPose.equals("转置")){
            setTransPose = true;
        }
        try{
        	ZPLLabel label =TPInterface.getService(printerTemplate).warehouse(setTransPose, mock, dpi, dstCity, dstArea, productId, dateStr, palletSN);
            print(label);
        }catch(Exception e){
            Log.error("",e);
        }
    }

    /**
     *小米中箱物流标签
     * */
    public void printBoxPost(String printerTemplate,String productId, String color, String SKU, String PO, String createDate, String supplier, String prodArea, String salesRegion, String productName,String productSpec,String pkgSpec, String inspection, String boxGrossWeight, String boxNetWeight, String boxSize, String volume, String dstCity, String dstArea){
        String transPose = MainUtils.getDict("boxPostView", "printerTransPose", version);
        if(transPose!=null&&transPose.equals("转置")){
            setTransPose = true;
        }

        try{
        	ZPLLabel label =TPInterface.getService(printerTemplate).box(setTransPose,mock, dpi, productId, color, SKU, PO, createDate, supplier,
                    prodArea, salesRegion, productName, productSpec, pkgSpec, inspection,
                    boxGrossWeight, boxNetWeight, boxSize, volume, dstCity, dstArea);           
            print(label);

        }catch(Exception e){
            Log.error("",e);
        }

    }
    public static void main(String[] args) throws Exception{
        new ZplPrinter("ZDesigner ZD420-300dpi ZPL","300");
        BufferedImage textImage = Text2Image.createTextImage();
        String zpl = Image2Zpl.image2Zpl2(textImage);
        String str = zpl;
        BufferedWriter writer = new BufferedWriter(new FileWriter("test_Text2Image.txt"));
        writer.write(str);
        writer.close();
    }
}

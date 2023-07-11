package com.dlsc.workbenchfx.demo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;

//import com.spire.pdf.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.cg.core.util.Log;

import java.awt.print.*;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

public class PrintPDF {
    static public void print() throws Exception {
        //1.得到一个文件的输入流
        FileInputStream fiStream = null;
        try {
            fiStream = new FileInputStream("./colorBoxBarCodes.pdf");
        } catch (FileNotFoundException e) {
            Log.error("",e);
        }
        if (fiStream == null) {
            Log.info("file not found!");
            return;
        }

        //这是要打印文件的格式，如果是PDF文档要设为自动识别
        DocFlavor fileFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        //2.得到要打印的文档类DOC
        Doc myDoc = new SimpleDoc(fiStream, fileFormat, null);  
        //3.生成一个打印属性设置对象
        PrintRequestAttributeSet aset = 
            new HashPrintRequestAttributeSet();
        aset.add(new Copies(1));//Copies-打印份数5份
        //aset.add(MediaSizeName.ISO_A4);//A4纸张
        //aset.add(Sides.DUPLEX);//双面打印
        //4.关键一步，得到当前机器上所有已经安装的打印机
        //传入的参数是文档格式跟打印属性，只有支持这个格式与属性的打印机才会被找到
        PrintService[] services = 
            PrintServiceLookup.lookupPrintServices(fileFormat, aset);
        Log.info(services.length);
        for (int i=0; i<services.length; i++){
            //5.用打印服务生成一个文档打印任务，这里用的是第一个服务
            String serviceName = services[i].getName();//可以得到打印机名称，可用名称进行比较得到自己想要的打印机
            Log.info(serviceName);
            Log.info("fuck: "+i);
            DocPrintJob job = services[i].createPrintJob();
            try {
                //6.最后一步，执行打印文档任务，传入的参数是Doc文档类，与属性(5份，双面,A4)
                job.print(myDoc, aset);//成功后电脑会提示已有文档添加到打印队列
            } catch (PrintException pe) {}
            break;
        }
    }
//    public static void print2() {
//        //加载文档
//        PdfDocument pdf = new PdfDocument();
//        pdf.loadFromFile("colorBoxBarCodes.pdf");
//
//        PrinterJob loPrinterJob = PrinterJob.getPrinterJob();
//        PageFormat loPageFormat  = loPrinterJob.defaultPage();
//        Paper loPaper = loPageFormat.getPaper();
//
//        //删除默认页边距
//        loPaper.setImageableArea(0,0,loPageFormat.getWidth(),loPageFormat.getHeight());
//
//        //设置打印份数
//        loPrinterJob.setCopies(2);
//
//        loPageFormat.setPaper(loPaper);
//        loPrinterJob.setPrintable(pdf,loPageFormat);
//        try {
//            loPrinterJob.print();
//        } catch (PrinterException e) {
//            Log.error("",e);
//        }
//    }
    public static void defaultPrintPDF(String filePath, String printerName) throws Exception{
	    PDDocument document = PDDocument.load(new File(filePath));
	    PDFRenderer pdfRenderer = new PDFRenderer(document);
	    BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
	    //String filePath = "./colorBoxBarCodes.pdf";
	    Log.info("打印工具类入參：filePath==================="+filePath);
	    //File file = new File(filePath); // 获取选择的文件
        // 构建打印请求属性集
        HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        // 设置打印格式，因为未确定类型，所以选择autosense
        DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
	//DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Log.info("打印文件类型为：==================="+flavor);
        //pras.add(MediaName.ISO_A4_TRANSPARENT);//A4纸张
        //遍历
        PrintService printServices[] = PrintServiceLookup.lookupPrintServices(null, pras); 


        Log.info("目标打印机为：==================="+printerName);
	//Log.info("打印机数量: "+printServices.length);
        for (PrintService printService : printServices) {
            Log.info("当前打印机为：==================="+printService.getName());
	    Log.info("支持的flavor: "+printService.getSupportedDocFlavors());
	    //for(DocFlavor f:printService.getSupportedDocFlavors()) {
	    //        Log.info("name:"+f.getClass().getName());
	    //        Log.info("mediaType:"+f.getMediaType());
	    //        Log.info("mime:"+f.getMimeType());
	    //}
	    if (printService.getName().indexOf(printerName)>-1){
                Log.info("选择的打印机为：==================="+printService);
                try {
                    DocPrintJob job = printService.createPrintJob(); // 创建打印作业
                    //FileInputStream fis = new FileInputStream(file); // 构造待打印的文件流
                    DocAttributeSet das = new HashDocAttributeSet();
                    //Doc doc = new SimpleDoc(fis, flavor, das);
		    Doc doc = new SimpleDoc(bufferedImageToInputStream(bim), flavor, das);
                    job.print(doc, pras);
		    Log.info("打印结束!========");
		    break;
                } catch (Exception e) {
                    Log.error("",e);
                    throw new Exception();
                }
		//Log.info("打印结束!========");
            }
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
}


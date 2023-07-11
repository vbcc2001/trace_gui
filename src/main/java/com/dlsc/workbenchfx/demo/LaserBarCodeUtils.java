package com.dlsc.workbenchfx.demo;
import com.cg.core.util.Log;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class LaserBarCodeUtils {
    /**
     * 条形码宽度
     */
    private static int WIDTH = 250;

    /**
     * 条形码高度
     */
    private static int HEIGHT = 30;

    /**
     * 加文字 条形码
     */
    private static final int WORDHEIGHT = 50;
    /**
     * 右上角文字
     */
    private static final String RIGHT_UP_WORDS = "";
    /**
     * 条形码右下角第一段文字
     */
    private static final String RIGHT_DOWN_FIRST_WORDS = "出货地: %s";
    private static final String RIGHT_DOWN_SECOND_WORDS = "目的地: %s";
    /**
     * 条形码左下角第一段文字
     */
    private static final String LEFT_DOWN_FIRST_WORDS = "";
    private static final String LEFT_DOWN_SECOND_WORDS = "";

    /**
     * 设置 条形码参数
     */
    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
        private static final long serialVersionUID = 1L;

        {
            // 设置编码方式
            put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别 这里选用最高级H级别
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            put(EncodeHintType.MARGIN, 0);

        }
    };

    /**
     * 生成 图片缓冲
     *
     * @param vaNumber VA 码
     * @return 返回BufferedImage
     * @author myc
     */
    public static BufferedImage getBarCode(String vaNumber){
        if (vaNumber.isEmpty()) return null;
        Code128Writer writer = new Code128Writer();
        // 编码内容, 编码类型, 宽度, 高度, 设置参数
        BitMatrix bitMatrix = writer.encode(vaNumber, BarcodeFormat.CODE_128, WIDTH, HEIGHT, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 把带logo的二维码下面加上文字
     *
     * @param image   条形码图片
     * @param equipNo 设备编号
     * @return 返回BufferedImage
     * @author myc
     */
    public static BufferedImage insertWords(BufferedImage image,
            String equipNo) {
        //设置边框
        //setDrawRect(g2d,WIDTH-10,WORDHEIGHT-10);
        // 设置虚线边框
        //setDrawRectDottedLine(g2d,WIDTH-5,WORDHEIGHT-5);
        // 新的图片，把带logo的二维码下面加上文字
        if (StringUtils.isNotEmpty(equipNo)) {

            // 画条形码到新的面板
            Log.info("barcode width: "+image.getWidth()+" total width: "+WIDTH);
            if(image.getWidth()>=WIDTH){
                WIDTH = image.getWidth() + 10;
            }
            BufferedImage outImage = new BufferedImage(WIDTH, WORDHEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outImage.createGraphics();
            // 抗锯齿
            setGraphics2D(g2d);
            // 设置白色
            setColorWhite(g2d);

            int startX = (WIDTH-image.getWidth())/2;
            g2d.drawImage(image, startX, 5, image.getWidth(), image.getHeight(), null);



            // 画文字到新的面板
            Color color = new Color(0, 0, 0);
            g2d.setColor(color);
            // 字体、字型、字号
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            //文字长度
            String str = equipNo.trim();
            int strWidth = g2d.getFontMetrics().stringWidth(str);
            //总长度减去文字长度的一半  （居中显示）
            //            int wordStartX=(WIDTH - strWidth) / 2;
            int wordStartX = (WIDTH - strWidth) / 2;
            //height + (outImage.getHeight() - height) / 2 + 12
            int wordStartY = HEIGHT + 15;
            //右上角文字长度
            int rightUpWordsWidth = WIDTH - g2d.getFontMetrics().stringWidth(RIGHT_UP_WORDS);
            //左上角文字长度
            LocalDate localDate = LocalDate.now();//For reference
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedString = localDate.format(formatter);
            String printDate = "打印日期 " + formattedString;
            int leftUpWordsWidth = WIDTH - g2d.getFontMetrics().stringWidth(printDate);
            //左下角第一 文字长度
            int leftDownFirstWordsWidth = WIDTH - 20 - g2d.getFontMetrics().stringWidth(LEFT_DOWN_FIRST_WORDS);

            // 画文字-上部分
            //g2d.drawString(RIGHT_UP_WORDS, 20, 30);
            //g2d.drawString(printDate, leftUpWordsWidth - 20, 30);

            //文字-下部分
            g2d.drawString(str, wordStartX, wordStartY);
            //g2d.drawString(String.format(RIGHT_DOWN_FIRST_WORDS,fromArea), 20, wordStartY + 56);
            //g2d.drawString(String.format(RIGHT_DOWN_SECOND_WORDS,toArea), 20, wordStartY + 76);
            //g2d.drawString(LEFT_DOWN_FIRST_WORDS, leftDownFirstWordsWidth, wordStartY + 56);
            //g2d.drawString(LEFT_DOWN_SECOND_WORDS, leftDownFirstWordsWidth, wordStartY + 76);
            g2d.dispose();
            outImage.flush();
            return outImage;
        }else{
            BufferedImage outImage = new BufferedImage(WIDTH, WORDHEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outImage.createGraphics();
            // 抗锯齿
            setGraphics2D(g2d);
            // 设置白色
            setColorWhite(g2d);

            //Log.info("barcode width: "+image.getWidth()+" total width: "+WIDTH);
            String msg = "无预览";
            Color color = new Color(0, 0, 0);
            g2d.setColor(color);
            // 字体、字型、字号
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            //文字长度
            int strWidth = g2d.getFontMetrics().stringWidth(msg);
            int wordStartX = (WIDTH - strWidth) / 2;
            int wordStartY = (WORDHEIGHT) / 2 + 5;
            g2d.drawString(msg, wordStartX, wordStartY);
            return outImage;
        }
    }

    /**
     * 设置 Graphics2D 属性  （抗锯齿）
     *
     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
     */
    private static void setGraphics2D(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(s);
    }

    /**
     * 设置背景为白色
     *
     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
     */
    private static void setColorWhite(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        //填充整个屏幕
        g2d.fillRect(0, 0, 600, 600);
        //设置笔刷
        g2d.setColor(Color.BLACK);
    }

    /**
     * 设置边框
     *
     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
     */
    private static void setDrawRect(Graphics2D g2d, int width, int height) {
        //设置笔刷
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(0, 0, width, height);
    }

    /**
     * 设置边框虚线点
     *
     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
     */
    private static void setDrawRectDottedLine(Graphics2D g2d, int width, int height) {
        //设置笔刷
        g2d.setColor(Color.BLUE);
        BasicStroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 0.5f, new float[]{1, 4}, 0.5f);
        g2d.setStroke(stroke);
        g2d.drawRect(0, 0, width, height);
    }

    public static BufferedImage generateBarCode(String msg){
        return insertWords(getBarCode(msg), msg);
    }

    public static BufferedImage generateBarCode_S101(ProductLaser productLaser){
        if(productLaser==null){
            BufferedImage outImage = new BufferedImage(WIDTH, WORDHEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outImage.createGraphics();
            // 抗锯齿
            setGraphics2D(g2d);
            // 设置白色
            setColorWhite(g2d);

            //Log.info("barcode width: "+image.getWidth()+" total width: "+WIDTH);
            String msg = "无预览";
            Color color = new Color(0, 0, 0);
            g2d.setColor(color);
            // 字体、字型、字号
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            //文字长度
            int strWidth = g2d.getFontMetrics().stringWidth(msg);
            int wordStartX = (WIDTH - strWidth) / 2;
            int wordStartY = (WORDHEIGHT) / 2 + 5;
            g2d.drawString(msg, wordStartX, wordStartY);
            return outImage;
        }

        String snCode = productLaser.getSNCode();//"43563AFAJKF2VS00001";
        String code69 = productLaser.getCode69();//"6941812701577";
        String productName = productLaser.getProductName();//"米家电动剃须刀 S101";
        String productColor = productLaser.getProductColor();//"暮光蓝";
        String SKU = productLaser.getSKU();//"BHR6726CN";
        String createDate = productLaser.getCreateDate();//"2022.06.01";
        // 宽 42 ，高 28
        double factor = 8;
        double total_width = 42 * factor;
        double total_height = 28 * factor;
        Code128Writer writer = new Code128Writer();
        // 编码内容, 编码类型, 宽度, 高度, 设置参数
        BitMatrix bitMatrix = writer.encode(snCode, BarcodeFormat.CODE_128, (int)(total_width*0.8), (int)(total_height*0.2), hints);
        BufferedImage snCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        EAN13Writer barcodeWriter = new EAN13Writer();
	Log.info(code69);
        BitMatrix bitMatrix_en13 = barcodeWriter.encode(code69, BarcodeFormat.EAN_13, (int)(total_width*0.8), (int)(total_height*0.3));
        BufferedImage en13Image = MatrixToImageWriter.toBufferedImage(bitMatrix_en13);
        if (StringUtils.isNotEmpty(snCode)) {
            // 画条形码到新的面板
            if(snCodeImage.getWidth()>=total_width){
                total_width = snCodeImage.getWidth() + 10;
                total_height = total_width * 28.0 / 42.0;
            }
            BufferedImage outImage = new BufferedImage((int)total_width, (int)total_height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outImage.createGraphics();
            // 抗锯齿
            setGraphics2D(g2d);
            // 设置白色
            setColorWhite(g2d);

            Color color = new Color(0, 0, 0);
            g2d.setColor(color);
            // 字体、字型、字号
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, (int)(total_width*0.04)));
            //文字长度
            int productNameStartX = (int)(total_width*0.04);
            int productNameStartY = (int)(total_width*0.06);
            g2d.drawString(productName, productNameStartX, productNameStartY);

            productColor = "颜色: "+productColor;
            int productColorWidth = g2d.getFontMetrics().stringWidth(productColor);
            int productColorStartX = (int)(total_width*0.96 - (double)productColorWidth);
            g2d.drawString(productColor, productColorStartX, productNameStartY);
            
            int snCodeImageStartX = (int)((total_width-snCodeImage.getWidth())/2);
            int snCodeImageStartY = (int)(productNameStartY + total_width*0.04);
            g2d.drawImage(snCodeImage, snCodeImageStartX, snCodeImageStartY, snCodeImage.getWidth(), snCodeImage.getHeight(), null);

            int snCodeStartX = (int)(total_width*0.04);
            int snCodeStartY = (int)(snCodeImageStartY + snCodeImage.getHeight()+ total_width*0.06);
            snCode = "SN: "+snCode;
            g2d.drawString(snCode, snCodeStartX, snCodeStartY);

            SKU = "SKU: "+SKU;
            int SKUWidth = g2d.getFontMetrics().stringWidth(SKU);
            int SKUStartX = (int)(total_width*0.96 - (double)SKUWidth);
            g2d.drawString(SKU, SKUStartX, snCodeStartY);

            int en13ImageStartX = (int)((total_width-en13Image.getWidth())/2);
            int en13ImageStartY = (int)(snCodeStartY + total_width*0.04);
            g2d.drawImage(en13Image, en13ImageStartX, en13ImageStartY, en13Image.getWidth(), en13Image.getHeight(), null);

            int code69Width = g2d.getFontMetrics().stringWidth(code69);
            int code69StartX = (int)((total_width-code69Width)/2);
            int code69StartY = (int)(en13ImageStartY + en13Image.getHeight()+ total_width*0.06);
            g2d.drawString(code69, code69StartX, code69StartY);

            createDate = "生产日期: "+createDate;
            int createDateStartX = (int)(total_width*0.04);
            int createDateStartY = (int)(code69StartY + total_width*0.05);
            g2d.drawString(createDate, createDateStartX, createDateStartY);


            //g2d.drawString(String.format(RIGHT_DOWN_FIRST_WORDS,fromArea), 20, wordStartY + 56);
            //g2d.drawString(String.format(RIGHT_DOWN_SECOND_WORDS,toArea), 20, wordStartY + 76);
            //g2d.drawString(LEFT_DOWN_FIRST_WORDS, leftDownFirstWordsWidth, wordStartY + 56);
            //g2d.drawString(LEFT_DOWN_SECOND_WORDS, leftDownFirstWordsWidth, wordStartY + 76);
            g2d.dispose();
            outImage.flush();
            return outImage;
        }else{
            BufferedImage outImage = new BufferedImage(WIDTH, WORDHEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outImage.createGraphics();
            // 抗锯齿
            setGraphics2D(g2d);
            // 设置白色
            setColorWhite(g2d);

            //Log.info("barcode width: "+image.getWidth()+" total width: "+WIDTH);
            String msg = "无预览";
            Color color = new Color(0, 0, 0);
            g2d.setColor(color);
            // 字体、字型、字号
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 18));
            //文字长度
            int strWidth = g2d.getFontMetrics().stringWidth(msg);
            int wordStartX = (WIDTH - strWidth) / 2;
            int wordStartY = (WORDHEIGHT) / 2 + 5;
            g2d.drawString(msg, wordStartX, wordStartY);
            return outImage;
        }
    }

}

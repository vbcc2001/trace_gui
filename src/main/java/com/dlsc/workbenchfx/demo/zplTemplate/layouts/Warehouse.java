package com.dlsc.workbenchfx.demo.zplTemplate.layouts;

import com.cg.core.util.FontName;
import com.cg.core.util.Log;
import com.cg.core.zpl.FontSettings;
import com.cg.core.zpl.ZPLConfig;
import com.cg.core.zpl.ZPLLabel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/** 目的仓打印样式 **/
public class Warehouse {

 /**
  * 目的仓标签打印<br>
  * 带产品ID及日期信息<br>
  */
 public static ZPLLabel layout01_100_70(boolean setTransPose, boolean isMock, int dpi, String city, String town, String id, String createDate, String SNCode) {// 目的仓
  final String cnFontName="黑体";
  final String enFontName="Arial";
  final float fontHeight=3.4f;
  ZPLConfig config = new ZPLConfig(dpi, 100f, 70f, 100);// 标签宽度100*70mm,左右边距2mm
  config.setDarkness(10);
  config.setDefaultFont(new FontSettings(FontName.HEITI, fontHeight));
  config.setPortrait(setTransPose);//是否纵向打印
  final ZPLLabel label = new ZPLLabel(config);
  InputStream in = Warehouse.class.getResourceAsStream("/zpl/xiaomi.png");
  label.addImage(in, 10.7f, 7.4f, 9.9f, 11.2f);
  //label.addText("目的仓:", 10.6f, 24.6f, 0, "J", 7.4f);
  label.addText("目的仓:", 10.6f, 24.6f, 0, 7.4f, 7.4f, cnFontName);
  //label.addText(city, 10.6f, 35.8f, 0, "J", 11.9f);
  label.addText(city, 10.6f, 35.8f, 0, 11.9f, 11.9f, cnFontName);
  if (town != null && !town.isEmpty()) {
   label.addText("(" + town + ")", 40.6f, 35.8f, 0, 11.9f, 11.9f, cnFontName);
  }
  label.addText("ID: " + id, 10.6f, 52.5f, 24.6f, 3, 3, enFontName);
  label.addCode128(id, id, 10.6f, 55.5f, 24.6f, 6.4f);
  label.addText("Production Date: " + createDate, 40.6f, 52.5f, 56.1f, 3, 3, enFontName);
  label.addCode128(createDate, createDate, 56.3f, 55.5f, 24.6f, 6.4f);
  int qrWidth=label.toPix(20f);
  try {
   BufferedImage src=toQRcode(SNCode, qrWidth, qrWidth);
   label.addImage(src, 70, 6.2f, 20, 20);//生成的二维码有约1.2mm的留白，top需要上移1.2mm
  } catch (Exception e) {
   Log.error("",e);
  }
  return label;
 }


 /**
  * 目的仓标签打印<br>
  * 不带产品ID及日期信息<br>
  */
 public static ZPLLabel layout02_100_70(boolean setTransPose, boolean isMock, int dpi, String city, String town, String id, String SNCode) {// 目的仓
  var cnFontName = new FontSettings(FontName.HEITI, 2.7f); //黑体
  final float fontHeight=3.4f;
  ZPLConfig config = new ZPLConfig(dpi, 100f, 70f, 100);// 标签宽度100*70mm,左右边距2mm
  config.setDarkness(10);
  config.setDefaultFont(new FontSettings(FontName.HEITI, fontHeight));
  config.setPortrait(setTransPose);//是否纵向打印
  final ZPLLabel label = new ZPLLabel(config);
  InputStream in = Warehouse.class.getResourceAsStream("/zpl/xiaomi.png");
  label.addImage(in, 10.7f, 10.7f, 11.9f, 13.2f);
  label.addText("目的仓:", 10.2f, 32f, 0, 10f, 10f, cnFontName.getFontName());
  if (town != null && !town.isEmpty()) {
   city = city + "（"+town+"）";
  }
  label.addText(city, 10.6f, 45.8f, 0, 16f, 13f,  cnFontName.getFontName());
  int qrWidth=label.toPix(20f);
  try {
   BufferedImage src=toQRcode(SNCode, qrWidth, qrWidth);
   label.addImage(src, 63, 9.7f, 30, 30);//生成的二维码有约1.2mm的留白，top需要上移1.2mm
  } catch (Exception e) {
   Log.error("",e);
  }
  return label;
 }

 /**
  * 生成二维码
  *
  * @param content     扫码内容
  * @param width 二维码宽度(px)
  * @param height 二维码高度(px)
  * @return
  * @throws Exception
  */
 protected static BufferedImage toQRcode(String content, int width, int height) throws Exception {
  /** 定义Map集合封装二维码配置信息 */
  Map<EncodeHintType, Object> hints = new HashMap<>();
  /** 设置二维码图片的内容编码 */
  hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
  /** 设置二维码图片的上、下、左、右间隙 */
  hints.put(EncodeHintType.MARGIN, 1);
  /** 设置二维码的纠错级别 */
  hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
  /**
   * 创建二维码字节转换对象 第一个参数：二维码图片中的内容 第二个参数：二维码格式器 第三个参数：生成二维码图片的宽度 第四个参数：生成二维码图片的高度
   * 第五个参数：生成二维码需要配置信息
   */
  BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,
          hints);

  /** 获取二维码图片真正的宽度 */
  int matrix_width = matrix.getWidth();
  /** 获取二维码图片真正的高度 */
  int matrix_height = matrix.getHeight();
  // TODO 后续需要实现动态扩容和独立标签打印
  /** 定义一张空白的缓冲流图片 */
  BufferedImage image = new BufferedImage(matrix_width, matrix_height, BufferedImage.TYPE_INT_RGB);
  /** 把二维码字节转换对象 转化 到缓冲流图片上 */
  for (int x = 0; x < matrix_width; x++) {
   for (int y = 0; y < matrix_height; y++) {
    /** 通过x、y坐标获取一点的颜色 true: 黑色 false: 白色 */
    int rgb = matrix.get(x, y) ? 0 : 0xFFFFFF;
    image.setRGB(x, y, rgb);
   }
  }
  return image;
 }

}

package com.dlsc.workbenchfx.demo.zplTemplate;

import com.cg.core.zpl.ZPLLabel;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.ColorBox;

import org.apache.poi.ss.formula.eval.NotImplementedException;
/** 小米打印模板服务基础实现类 */
public abstract class TPService implements TPInterface{
    @Override
    public ZPLLabel colorBox(boolean isMock, int dpi, String SNCode, String SKU, String color, String createDate, String productName, String code69) {
        return ColorBox.layout01_42_28(isMock,dpi, SNCode,SKU, color, createDate, productName, code69);
    }
    @Override
    public ZPLLabel box(boolean isPortrait, boolean isMock, int dpi, String productId, String color, String SKU, String PO,
                        String createDate, String supplier, String prodArea, String salesRegion, String productName, String productSpec, String pkgSpec,
                        String inspection, String boxGrossWeight, String boxNetWeight, String boxSize, String volume, String dstCity, String dstArea) {
        throw new NotImplementedException("中箱物流模板还没实现");
    }

    @Override
    public ZPLLabel warehouse(boolean setTransPose, boolean isMock, int dpi, String city, String town, String id, String createDate, String SNCode) {
        throw new NotImplementedException("目的仓模板还没实现");
    }

    @Override
    public ZPLLabel boxCode(boolean setTransPose, boolean isMock, int dpi, String boxCode, String[] productSNs, String productName, String color,String productId) {
        throw new NotImplementedException("中箱唯一码模板还没实现");
    }

    @Override
    public ZPLLabel palletMark(boolean setTransPose, boolean isMock,int dpi,String PO,String SKU,String id,String createDate,String productName
            ,String productColor,String pcs,String grossWeight,String palletNumber, String[] boxSNs, String[] productLaserSNs) {
        throw new NotImplementedException("栈板唯一码模板还没实现");
    }
}

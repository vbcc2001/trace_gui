package com.dlsc.workbenchfx.demo.zplTemplate;

import com.cg.core.zpl.ZPLLabel;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.Box;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.BoxCode;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.PalletMark;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.Warehouse;

/**
 * 小米产品ID41431标签打样模板<br>
 * 41431 米家声波电动牙刷T501C  MES607 白色
 * 由东莞因范在2023/03/07提出需求
 */
@PrintTemplate(value = {"TP41431"})
public class TP41431 extends TPService{
    @Override
    public ZPLLabel box(boolean isPortrait, boolean isMock, int dpi, String productId, String color, String SKU,
                        String PO, String createDate, String supplier, String prodArea, String salesRegion, String productName,
                        String productSpec, String pkgSpec, String inspection, String boxGrossWeight, String boxNetWeight,
                        String boxSize, String volume, String dstCity, String dstArea) {
        return Box.layout03_100_70(isPortrait,isMock,dpi,productId,color,SKU,PO,createDate,supplier,prodArea,salesRegion,
                productName,productSpec,pkgSpec,inspection,boxGrossWeight,boxNetWeight,boxSize,volume,dstCity,dstArea);
    }
    @Override
    public ZPLLabel warehouse(boolean setTransPose, boolean isMock, int dpi, String city, String town, String id, String createDate, String SNCode) {
        return Warehouse.layout02_100_70(setTransPose,isMock,dpi,city,town,id,SNCode);
    }
    @Override
    public ZPLLabel boxCode(boolean setTransPose, boolean isMock, int dpi, String boxCode, String[] productSNs, String productName, String color,String productId) {
        return BoxCode.layout05_70_100(setTransPose,dpi,boxCode,productName,color,productId,productSNs);
    }
    @Override
    public ZPLLabel palletMark(boolean setTransPose, boolean isMock, int dpi, String PO, String SKU, String id, String createDate,
                               String productName, String productColor, String pcs, String grossWeight, String palletNumber,
                               String[] boxSNs, String[] productLaserSNs) {
        int rows = 9;
        int columns = 4;
        //A4尺寸,带出中箱SN码
        return PalletMark.layout02_297_210(setTransPose, isMock, dpi, PO, SKU, id, createDate, productName, productColor, pcs, grossWeight, palletNumber,boxSNs,rows,columns);
    }
}
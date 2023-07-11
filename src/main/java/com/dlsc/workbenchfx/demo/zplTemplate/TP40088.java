package com.dlsc.workbenchfx.demo.zplTemplate;

import com.cg.core.zpl.ZPLLabel;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.Box;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.BoxCode;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.PalletMark;
import com.dlsc.workbenchfx.demo.zplTemplate.layouts.Warehouse;

/**
 * 小米产品ID40088标签打样模板<br>
 * 40088 米家便携式冲牙器 MEO702 白色 标准版<br>
 * 40089 米家便携式冲牙器 MEO702 粉色 标准版<br>
 * 由江西乐丰提出需求<br>
 */
@PrintTemplate(value = {"TP40088","TP40089"})
public class TP40088 extends TPService {
    @Override
    public ZPLLabel box(boolean isPortrait, boolean isMock, int dpi, String productId, String color, String SKU,
                        String PO, String createDate, String supplier, String prodArea, String salesRegion, String productName,
                        String productSpec, String pkgSpec, String inspection, String boxGrossWeight, String boxNetWeight,
                        String boxSize, String volume, String dstCity, String dstArea) {
        return Box.layout02_117_60(isPortrait,isMock,dpi,productId,color,SKU,PO,createDate,supplier,prodArea,salesRegion,
                productName,productSpec,pkgSpec,inspection,boxGrossWeight,boxNetWeight,boxSize,volume,dstCity,dstArea);
    }
    @Override
    public ZPLLabel warehouse(boolean setTransPose, boolean isMock, int dpi, String city, String town, String id, String createDate, String SNCode) {
        return Warehouse.layout02_100_70(setTransPose,isMock,dpi,city,town,id,SNCode);
    }
    @Override
    public ZPLLabel boxCode(boolean setTransPose, boolean isMock, int dpi, String boxCode, String[] productSNs, String productName, String color,String productId) {
        return BoxCode.layout03_110_60(setTransPose,dpi,boxCode,productName,color,productSNs);
    }
    @Override
    public ZPLLabel palletMark(boolean setTransPose, boolean isMock, int dpi, String PO, String SKU, String id, String createDate,
                               String productName, String productColor, String pcs, String grossWeight, String palletNumber,
                               String[] boxSNs, String[] productLaserSNs) {
        //117*60尺寸,不带中箱SN码
        return PalletMark.layout03_117_60(setTransPose, isMock, dpi, PO, SKU, id, createDate, productName, productColor, pcs, grossWeight, palletNumber);
    }
}

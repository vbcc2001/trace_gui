package com.dlsc.workbenchfx.demo.zplTemplate;

import com.cg.core.module.BizException;
import com.cg.core.zpl.ZPLLabel;
/** 小米打印模板服务接口 */
public interface TPInterface {

    /**
     *
     * @param ptName 模板名称
     * @return 模板服务
     * @throws BizException 找不到模板服务
     */
    static TPInterface getService(String ptName) throws BizException {
        return ClazzUtils.getPrintTemplateByName(ptName).getService();
    }
    /** 彩盒标签打印 */
    ZPLLabel colorBox(boolean isMock, int dpi, String SNCode, String SKU, String color,String createDate, String productName, String code69);
    /** 中箱物流标签打印 */
    ZPLLabel box(boolean isPortrait, boolean isMock, int dpi, String productId, String color, String SKU, String PO,
                       String createDate, String supplier, String prodArea, String salesRegion, String productName, String productSpec, String pkgSpec,
                       String inspection, String boxGrossWeight, String boxNetWeight, String boxSize, String volume, String dstCity, String dstArea);
    /** 目的仓标签打印 */
    ZPLLabel warehouse(boolean setTransPose, boolean isMock, int dpi, String city, String town, String id, String createDate,
                       String SNCode);
    /** 中箱唯一码标签打印 */
    ZPLLabel boxCode(boolean setTransPose, boolean isMock, int dpi, String boxCode, String[] productSNs,String productName,String color,String productId);
    /** 栈板唯一码标签打印 */
    ZPLLabel palletMark(boolean setTransPose, boolean isMock,int dpi,String PO,String SKU,String id,String createDate,String productName
            ,String productColor,String pcs,String grossWeight,String palletNumber, String[] boxSNs, String[] productLaserSNs);
}

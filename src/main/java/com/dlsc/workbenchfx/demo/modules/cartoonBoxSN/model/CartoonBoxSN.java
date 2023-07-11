package com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model;

import java.util.List;

import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;


public class CartoonBoxSN extends ProductInfo {
	private List<String> laserSNCodes;
    public String getBoxNetWeigth() {
        return boxNetWeight;
    }


    public void setBoxNetWeigth(String boxNetWeigth) {
        this.boxNetWeight = boxNetWeigth;
    }


    public String getBoxGrossWeight() {
        return boxGrossWeight;
    }


    public void setBoxGrossWeight(String boxGrossWeight) {
        this.boxGrossWeight = boxGrossWeight;
    }

    public CartoonBoxSN(){};

    public CartoonBoxSN(String productPartNumber, String productName, String productType, String productColor,
            String createDate, String factoryCode, String sNCode, String streamCode, String status, String creator,
            String quantity, String boxLength, String boxWidth, String boxHeight, String boxNetWeight,
            String boxGrossWeight) {
        super(productPartNumber, productName, productType, productColor);
        this.createDate = createDate;
        this.factoryCode = factoryCode;
        SNCode = sNCode;
        this.streamCode = streamCode;
        this.status = status;
        this.creator = creator;
        this.quantity = quantity;
        this.boxLength = boxLength;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.boxNetWeight = boxNetWeight;
        this.boxGrossWeight = boxGrossWeight;
    }


    public String getCreateDate() {
        return createDate;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public String getSNCode() {
        return SNCode;
    }

    public String getStreamCode() {
        return streamCode;
    }

    public String getStatus() {
        return status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreateTime(String createDate) {
        this.createDate = createDate;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public void setSNCode(String sNCode) {
        SNCode = sNCode;
    }

    public void setStreamCode(String streamCode) {
        this.streamCode = streamCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBoxLength() {
        return boxLength;
    }

    public void setBoxLength(String boxLength) {
        this.boxLength = boxLength;
    }

    public String getBoxWidth() {
        return boxWidth;
    }

    public void setBoxWidth(String boxWidth) {
        this.boxWidth = boxWidth;
    }

    public String getBoxHeight() {
        return boxHeight;
    }

    public void setBoxHeight(String boxHeight) {
        this.boxHeight = boxHeight;
    }


    public String createDate;
    public String factoryCode;
    public String SNCode;
    public String nickName;
    public String streamCode;
    public String status;//="正常使用"; // 退回返修
    public String creator;//="";
    public String quantity; // 产品数量
    public String boxLength;
    public String boxWidth;
    public String boxHeight;
    public String boxNetWeight; //净重
    public String boxGrossWeight; //毛重
    public String supplier; //供应商
    public String prodPlace; //生产地
    public String salesArea; //销售区域
    public String pkgSpec; // 包装规格
    public String PO; // 采购订单号
    public String inspection; // 检验员/检验批次
    public String volume; // 体积
    public String dstCity; // 目的城市
    public String dstStoreHouse; // 目的仓
    public String palletCode;
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public String getNickName() {
        return nickName;
    }


    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getBoxNetWeight() {
        return boxNetWeight;
    }


    public void setBoxNetWeight(String boxNetWeight) {
        this.boxNetWeight = boxNetWeight;
    }


    public String getSupplier() {
        return supplier;
    }


    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }


    public String getProdPlace() {
        return prodPlace;
    }


    public void setProdPlace(String prodPlace) {
        this.prodPlace = prodPlace;
    }


    public String getSalesArea() {
        return salesArea;
    }


    public void setSalesArea(String salesArea) {
        this.salesArea = salesArea;
    }



    public String getPO() {
        return PO;
    }


    public void setPO(String pO) {
        PO = pO;
    }




    public String getInspection() {
        return inspection;
    }


    public void setInspection(String inspection) {
        inspection = inspection;
    }


    public String getVolume() {
        return volume;
    }


    public void setVolume(String volume) {
        this.volume = volume;
    }


    public String getDstCity() {
        return dstCity;
    }


    public void setDstCity(String dstCity) {
        this.dstCity = dstCity;
    }


    public String getDstStoreHouse() {
        return dstStoreHouse;
    }


    public void setDstStoreHouse(String dstStoreHouse) {
        this.dstStoreHouse = dstStoreHouse;
    }


	public String getPkgSpec() {
		return pkgSpec;
	}


	public void setPkgSpec(String pkgSpec) {
		this.pkgSpec = pkgSpec;
	}


	public String getPalletCode() {
		return palletCode;
	}


	public void setPalletCode(String palletCode) {
		this.palletCode = palletCode;
	}


	public List<String> getLaserSNCodes() {
		return laserSNCodes;
	}


	public void setLaserSNCodes(List<String> laserSNCodes) {
		this.laserSNCodes = laserSNCodes;
	}
}

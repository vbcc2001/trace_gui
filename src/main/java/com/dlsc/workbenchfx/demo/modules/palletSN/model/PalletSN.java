package com.dlsc.workbenchfx.demo.modules.palletSN.model;

import java.util.List;

import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;


public class PalletSN extends ProductInfo {
	private List<String> boxNickNames;
    public PalletSN(String productPartNumber, String productName, String productType, String productColor,
            String createDate, String factoryCode, String workshopCode, String sNCode, String streamCode,
            String creator, String quantity, String fromArea, String toArea) {
        super(productPartNumber, productName, productType, productColor);
        this.createDate = createDate;
        this.factoryCode = factoryCode;
        this.workshopCode = workshopCode;
        SNCode = sNCode;
        this.streamCode = streamCode;
        this.creator = creator;
        this.quantity = quantity;
        this.fromArea = fromArea;
        this.toArea = toArea;
    }

    public PalletSN(){}

    public String getFromArea() {
        return fromArea;
    }
    public void setFromArea(String fromArea) {
        this.fromArea = fromArea;
    }
    public String getToArea() {
        return toArea;
    }
    public void setToArea(String toArea) {
        this.toArea = toArea;
    }
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getFactoryCode() {
        return factoryCode;
    }
    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }
    public String getWorkshopCode() {
        return workshopCode;
    }
    public void setWorkshopCode(String workshopCode) {
        this.workshopCode = workshopCode;
    }
    public String getSNCode() {
        return SNCode;
    }
    public void setSNCode(String sNCode) {
        SNCode = sNCode;
    }
    public String getStreamCode() {
        return streamCode;
    }
    public void setStreamCode(String streamCode) {
        this.streamCode = streamCode;
    }
    public String getCreator() {
        return creator;
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
    public String createDate;
    public String factoryCode;
    public String workshopCode;
    public String SNCode;
    public String nickName;
    public String streamCode;
    public String creator;
    public String quantity; // 装板数量
    public String fromArea; // 出货地
    public String toArea; // 目的地
    public String PO; // 采购订单号
    public String grossWeight; // 托盘毛重
	public String getPO() {
		return PO;
	}
	public void setPO(String pO) {
		PO = pO;
	}
	public String getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}
	public String getNickName() {
		return nickName;
	}
	public PalletSN setNickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	public List<String> getBoxNickNames() {
		return boxNickNames;
	}

	public void setBoxNickNames(List<String> boxNickNames) {
		this.boxNickNames = boxNickNames;
	}	
}

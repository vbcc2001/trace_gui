package com.dlsc.workbenchfx.demo.modules.shipping.model;

public class Shipping {

    public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductPartNumber() {
        return productPartNumber;
    }
    public void setProductPartNumber(String productPartNumber) {
        this.productPartNumber = productPartNumber;
    }
    public String[] getProductSN() {
        return productSN.split(",");
    }
    public void setProductSN(String productSN) {
        this.productSN = productSN;
    }
    public String getCartoonBoxSN() {
        return cartoonBoxSN;
    }
    public void setCartoonBoxSN(String cartoonBoxSN) {
        this.cartoonBoxSN = cartoonBoxSN;
    }
    public String getPalletSN() {
        return palletSN;
    }
    public void setPalletSN(String palletSN) {
        this.palletSN = palletSN;
    }
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getShippingDate() {
        return shippingDate;
    }
    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }
    public String getShippingCode() {
        return shippingCode==null?"":shippingCode;
    }
    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }
    public Shipping(String productPartNumber, String productSN, String cartoonBoxSN, String palletSN, String createDate,
            String shippingDate, String shippingCode) {
        this.productPartNumber = productPartNumber;
        this.productSN = productSN;
        this.cartoonBoxSN = cartoonBoxSN;
        this.palletSN = palletSN;
        this.createDate = createDate;
        this.shippingDate = shippingDate;
        this.shippingCode = shippingCode;
    }
    public String productPartNumber;
    public String productType;
    public String productSN;
    public String cartoonBoxSN;
    public String palletSN;
    public String createDate;
    public String shippingDate;
    public String shippingCode;
    public String SKU;
	public String getSKU() {
		return SKU;
	}
	public void setSKU(String sKU) {
		SKU = sKU;
	}
}

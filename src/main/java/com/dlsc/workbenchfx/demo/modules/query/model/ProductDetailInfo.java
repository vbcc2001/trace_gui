package com.dlsc.workbenchfx.demo.modules.query.model;

import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;


public class ProductDetailInfo extends ProductLaser {          
    private String boxDate;//装箱时间
    private String palletCode;//栈板编码
    private String palletDate;//装栈时间    
	public String getBoxDate() {
		return boxDate;
	}
	public void setBoxDate(String boxDate) {
		this.boxDate = boxDate;
	}
	public String getPalletCode() {
		return palletCode;
	}
	public void setPalletCode(String palletCode) {
		this.palletCode = palletCode;
	}
	public String getPalletDate() {
		return palletDate;
	}
	public void setPalletDate(String palletDate) {
		this.palletDate = palletDate;
	}	
}

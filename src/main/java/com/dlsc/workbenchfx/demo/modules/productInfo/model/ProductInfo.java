package com.dlsc.workbenchfx.demo.modules.productInfo.model;

public class ProductInfo {

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer id; // 入库唯一标识符
	/***********以下为产品模板的通用属性*********************/
	public String productType; // 产品型号
	public String productName; // SKU名称/产品名称
	public String productColor; // 产品颜色(中文)

	/***********以下为小米产品模板的独有属性*********************/
	public String code69; // 69码,小米独有
	public String productId; // 商品id
	public String SKU; // SKU	

	/***********以下为素士产品模板的独有属性*********************/
	public String productPartNumber; // 产品料号,素士独有
	public String factoryCode; // 工厂代码
	public String boxQuantity; // 装箱数量
	public String boxNetWeight; // 中箱净重
	public String boxGrossWeight; // 中箱毛重
	public String boxLength; // 箱子长
	public String boxWidth; // 箱子宽
	public String boxHeight; // 箱子高
	
	/***********以下不属于产品模板的属性,是由模板衍生出来的产品属性*********************/
	public String projectSPU; // 项目SPU
	public String length; // 长
	public String width; // 宽
	public String height; // 高

    public String printerTemplate; //打印模板

	public String getProductPartNumber() {
		return productPartNumber;
	}

	public void setProductPartNumber(String productPartNumber) {
		this.productPartNumber = productPartNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductColor() {
		return productColor;
	}

	public void setProductColor(String productColor) {
		this.productColor = productColor;
	}

	public String getProjectSPU() {
		return projectSPU;
	}

	public void setProjectSPU(String projectSPU) {
		this.projectSPU = projectSPU;
	}

	public String getCode69() {
		return code69;
	}

	public void setCode69(String code69) {
		this.code69 = code69;
	}

	public String getSKU() {
		return SKU;
	}

	public void setSKU(String sKU) {
		SKU = sKU;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * 
	 * @return 打印模板,永不为空(若为空则默认模板名为TP+模板商品ID)
	 */
	public String getPrinterTemplate() {
//		return printerTemplate==null||printerTemplate.isBlank()?"TP"+getProductId():printerTemplate;
		return printerTemplate;
	}
	public void setPrinterTemplate(String printerTemplate) {
		this.printerTemplate = printerTemplate;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getBoxQuantity() {
		return boxQuantity;
	}

	public void setBoxQuantity(String boxQuantity) {
		this.boxQuantity = boxQuantity;
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

	public String getBoxNetWeight() {
		return boxNetWeight;
	}

	public void setBoxNetWeight(String boxNetWeight) {
		this.boxNetWeight = boxNetWeight;
	}

	public String getBoxGrossWeight() {
		return boxGrossWeight;
	}

	public void setBoxGrossWeight(String boxGrossWeight) {
		this.boxGrossWeight = boxGrossWeight;
	}

	public ProductInfo() {
		super();
	}
	public ProductInfo(String productPartNumber, String productName, String productType, //
            String productColor) {
        this.productPartNumber = productPartNumber;
        this.productName = productName;
        this.productType = productType;
        this.productColor = productColor;
   }
}

package com.dlsc.workbenchfx.demo.modules.query.model;

import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;


public class Query extends ProductInfo implements Cloneable{
    public String createDate;
    public String factoryCode;
    public String SNCode;
    public String used="否";
    public String streamCode;
    public String status="N"; // 退回返修R
    public String creator="";

    public String ecologicalChain; //生态链公司
    public String reserved = "F"; //预留位置
    public String weight; //重量

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public Query(String productPartNumber, 
            String productName, 
            String productType, //
            String productColor, 
            String createDate, 
            String factoryCode, 
            String streamCode, 
            String creator,
            String SNCode) {
        super(productPartNumber, productName, productType, productColor);
        this.createDate = createDate;
        this.factoryCode = factoryCode;
        this.SNCode = SNCode;
        this.streamCode = streamCode;
        this.creator = creator;
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

    public String getUsed() {
        return used;
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

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public void setSNCode(String sNCode) {
        SNCode = sNCode;
    }

    public void setUsed(String used) {
        this.used = used;
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

    public String getEcologicalChain() {
        return ecologicalChain;
    }

    public void setEcologicalChain(String ecologicalChain) {
        this.ecologicalChain = ecologicalChain;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
}

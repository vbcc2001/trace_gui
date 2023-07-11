package com.dlsc.workbenchfx.demo.modules.weighBox.model;

import java.util.function.Function;

public class WeighRecord{
    private final Function<String,String> testWeight;
    public WeighRecord(Function<String,String> testWeight){
    	this.testWeight=testWeight;
    }
    public String ID;
    public String createTime;
    public String weight;
    public String SNCode;
    public String result;
    private String bankFlag;//入库标记, 是/否
    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        ID = iD;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getSNCode() {
        return SNCode;
    }
    public void setSNCode(String sNCode) {
        SNCode = sNCode;
    }

    public String getResult() {
        return testWeight.apply(getWeight());
    }
	public String getBankFlag() {
		return bankFlag;
	}
	public void setBankFlag(String bankFlag) {
		this.bankFlag = bankFlag;
	}
}

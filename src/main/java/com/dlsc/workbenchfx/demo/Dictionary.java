package com.dlsc.workbenchfx.demo;

public class Dictionary{	
	private String dicType;//字典分组
	private String code;//字典编码
	private String text;//字典值,不允许为空
	private String desc;//字典描述,默认为空	
    public String getDicType() {
        return dicType;
    }
    public void setDicType(String dicType) {
        this.dicType = dicType;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}

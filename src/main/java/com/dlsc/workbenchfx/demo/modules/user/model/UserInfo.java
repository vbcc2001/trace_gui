package com.dlsc.workbenchfx.demo.modules.user.model;

public class UserInfo {

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
    public UserInfo(){}
	public Integer id;
    public String productPartNumber; //产品料号
    public String productName; //产品名字
    public String productType; //产品类型
    public String productColor; // 产品颜色
    public String projectSPU; //项目SPU
    public String code69; // 69码
    public String SKU; // SKU
    public String productId; // 商品id 
    public String length; // 长
    public String width; // 宽
    public String height; // 高

    public String userName; //用户名
    public String userPasswd; //用户密码
    public String product = "否"; //彩盒权限默认否,下面的字段类似
    public String laser = "否"; //彩盒权限默认否,下面的字段类似
    public String colorBox = "否"; //彩盒权限默认否,下面的字段类似
    public String weight = "否"; //彩盒权限默认否,下面的字段类似
    public String cartoonBox = "否"; //彩盒权限默认否,下面的字段类似
    public String boxPost = "否"; //彩盒权限默认否,下面的字段类似
    public String pallet = "否"; //彩盒权限默认否,下面的字段类似
    public String dstHouse = "否"; //彩盒权限默认否,下面的字段类似
    public String shipping = "否"; //彩盒权限默认否,下面的字段类似
    public String query = "否"; //彩盒权限默认否,下面的字段类似
    public String mygroup = "生产单位"; //彩盒权限默认否,下面的字段类似
    private boolean frozen;//用户是否已冻结
    public UserInfo(String productPartNumber, String productName, String productType, //
            String productColor) {
        this.productPartNumber = productPartNumber;
        this.productName = productName;
        this.productType = productType;
        this.productColor = productColor;
            }
    public String getProductPartNumber(){
        return productPartNumber;
    }
    public void setProductPartNumber(String productPartNumber){
        this.productPartNumber = productPartNumber;
    }

    public String getProductName(){
        return productName;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }

    public String getProductType(){
        return productType;
    }
    public void setProductType(String productType){
        this.productType = productType;
    }

    public String getProductColor(){
        return productColor;
    }
    public void setProductColor(String productColor){
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
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPasswd() {
        return userPasswd;
    }
    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }
    public String getColorBox() {
        return colorBox;
    }
    public void setColorBox(String colorBox) {
        this.colorBox = colorBox;
    }
    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getCartoonBox() {
        return cartoonBox;
    }
    public void setCartoonBox(String cartoonBox) {
        this.cartoonBox = cartoonBox;
    }
    public String getPallet() {
        return pallet;
    }
    public void setPallet(String pallet) {
        this.pallet = pallet;
    }
    public String getShipping() {
        return shipping;
    }
    public void setShipping(String shipping) {
        this.shipping = shipping;
    }	
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }
    public String getLaser() {
        return laser;
    }
    public void setLaser(String laser) {
        this.laser = laser;
    }
    public String getBoxPost() {
        return boxPost;
    }
    public void setBoxPost(String boxPost) {
        this.boxPost = boxPost;
    }
    public String getDstHouse() {
        return dstHouse;
    }
    public void setDstHouse(String dstHouse) {
        this.dstHouse = dstHouse;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
	public String getMygroup() {
		return mygroup;
	}
	public void setMygroup(String mygroup) {
		this.mygroup = mygroup;
	}
	public boolean isFrozen() {
		return frozen;
	}
	public void setFrozen(boolean isFrozen) {
		this.frozen = isFrozen;
	}
}

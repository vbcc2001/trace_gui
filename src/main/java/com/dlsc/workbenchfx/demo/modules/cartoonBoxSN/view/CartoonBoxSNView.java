package com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.view;

//import javafx.scene.Group;
//import com.dlsc.workbenchfx.demo.CartoonBoxQRCodeUtils;
import com.dlsc.workbenchfx.demo.LettersOrNumbersValidator;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;
import com.dlsc.workbenchfx.demo.modules.common.view.BaseView;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
import javafx.beans.property.*;

import java.time.*;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.api.CartoonBoxAPI;
import com.dlsc.workbenchfx.demo.api.ShippingAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;
import com.dlsc.workbenchfx.demo.modules.shipping.model.Shipping;

import java.util.*;
import java.util.stream.Collectors;

import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.util.ColSpan;

import com.dlsc.formsfx.model.structure.Field;
import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.MathUtil;



public class CartoonBoxSNView extends BaseView<CartoonBoxSN,ProductLaser> {




    public CartoonBoxSNView(String creator) {
        this.creator = creator;

    }

    @Override
    public Form getForm(String version){
        Form form = null;
        SingleSelectionField<String> productListField = Field.ofSingleSelectionType(productList, 0);
        //MainUtils.recoverSelection(productListField, "cartoonBox", "productName",version);

        if (productList.isEmpty()){
            productListField.tooltip("先去\"产品设置\"填好产品信息!");
        }
        productListField.setBindingMode(BindingMode.CONTINUOUS);

            IntegerProperty quantity = new SimpleIntegerProperty(0);
            StringProperty productPartNumber = new SimpleStringProperty("");
            StringProperty productType = new SimpleStringProperty("");
            StringProperty productColor = new SimpleStringProperty("");
            StringProperty SKU = new SimpleStringProperty("");
            StringProperty factoryCode = new SimpleStringProperty("");

            SimpleDoubleProperty boxLength = new SimpleDoubleProperty(0);
            SimpleDoubleProperty boxWidth = new SimpleDoubleProperty(0);
            SimpleDoubleProperty boxHeight = new SimpleDoubleProperty(0);
            SimpleDoubleProperty boxNetWeight = new SimpleDoubleProperty(0);
            SimpleDoubleProperty boxGrossWeight = new SimpleDoubleProperty(0);
            ObjectProperty<LocalDate> selectedDate  = new SimpleObjectProperty<LocalDate>(LocalDate.now());

        if(version.equals("sushi")){
            productListField.changedProperty().addListener((observable, oldValue, newValue)->{
                String _productName = productListField.getSelection();
                String[] items = _productName.split(":");
                Integer index = MathUtil.toInteger(items[0]);
                ProductInfo productInfo = productInfoList.get(index);
                currentProductInfo = productInfo;
                productPartNumber.setValue(productInfo.getProductPartNumber());
                productType.setValue(productInfo.getProductType());
                productColor.setValue(productInfo.getProductColor());

                factoryCode.setValue(productInfo.getFactoryCode());
                quantity.setValue(MathUtil.toInteger(productInfo.getBoxQuantity()));
                boxLength.setValue(MathUtil.toDouble(productInfo.getBoxLength()));
                boxWidth.setValue(MathUtil.toDouble(productInfo.getBoxWidth()));
                boxHeight.setValue(MathUtil.toDouble(productInfo.getBoxHeight()));
                boxNetWeight.setValue(MathUtil.toDouble(productInfo.getBoxNetWeight()));
                boxGrossWeight.setValue(MathUtil.toDouble(productInfo.getBoxGrossWeight()));

                productListField.persist();
            });

            String productName = productListField.getSelection();

            if (productName!=null){
                String[] items = productName.split(":");
                Integer index = Integer.valueOf(items[0]);
                ProductInfo productInfo = productInfoList.get(index);
                currentProductInfo = productInfo;
                productPartNumber.setValue(productInfo.getProductPartNumber());
                productType.setValue(productInfo.getProductType());
                productColor.setValue(productInfo.getProductColor());

                factoryCode.setValue(productInfo.getFactoryCode());
                quantity.setValue(MathUtil.toInteger(productInfo.getBoxQuantity()));
                boxLength.setValue(MathUtil.toDouble(productInfo.getBoxLength()));
                boxWidth.setValue(MathUtil.toDouble(productInfo.getBoxWidth()));
                boxHeight.setValue(MathUtil.toDouble(productInfo.getBoxHeight()));
                boxNetWeight.setValue(MathUtil.toDouble(productInfo.getBoxNetWeight()));
                boxGrossWeight.setValue(MathUtil.toDouble(productInfo.getBoxGrossWeight()));                
            }

            form = Form.of(
                    Group.of(
                        productListField
                        .label("产品名称")
                        .id("productName")
                        .span(ColSpan.HALF)
                        .styleClass("field"),
                        Field.ofStringType(productPartNumber)
                        .editable(false)
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .id("productPartNumber")
                        .label("产品料号"),
                        Field.ofStringType(productType)
                        .styleClass("field")
                        .editable(false)
                        .span(ColSpan.HALF)
                        .id("productType")
                        .label("产品型号"),
                        Field.ofStringType(productColor)
                        .styleClass("field")
                        .editable(false)
                        .span(ColSpan.HALF)
                        .id("productColor")
                        .label("产品颜色"),
                        //factoryCodeField
                        Field.ofStringType(factoryCode)
                            .label("工厂代码")
                            .id("factoryCode")
                            .styleClass("field")
                            .span(ColSpan.HALF)
                            .validate(StringLengthValidator.atLeast(2, "product_error_message"))
                            .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                            .required("必填"),
                        Field.ofIntegerType(quantity)
                            .styleClass("field")
                            .span(ColSpan.HALF)
                            .label("装载数量")
                            //.id("boxQuantity")
                            .id("quantity")
                            .validate(IntegerRangeValidator.atLeast(1, "必须为正整数"))
                            .required("必填"),
                        Field.ofDoubleType(boxLength)
                            .styleClass("field")
                            .label("长(mm)")
                            .id("boxLength")
                            .span(ColSpan.THIRD)
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填"),
                        Field.ofDoubleType(boxWidth)
                            .label("宽(mm)")
                            .id("boxWidth")
                            .styleClass("field")
                            .span(ColSpan.THIRD)
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填")
                            .span(ColSpan.THIRD),
                        Field.ofDoubleType(boxHeight)
                            .styleClass("field")
                            .label("高(mm)")
                            .id("boxHeight")
                            .span(ColSpan.THIRD)
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填")
                            .span(ColSpan.THIRD),
                        Field.ofDoubleType(boxNetWeight)
                            .styleClass("field")
                            .label("净重(kg)")
                            .id("boxNetWeight")
                            .span(ColSpan.THIRD)
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填")
                            .span(ColSpan.HALF),
                        Field.ofDoubleType(boxGrossWeight)
                            .styleClass("field")
                            .label("毛重(kg)")
                            .id("boxGrossWeight")
                            .span(ColSpan.HALF)
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填")
                            .span(ColSpan.HALF),
                        Field.ofDate(selectedDate)
                            .styleClass("field")
                            .label("日期")
                            .id("createDate")
                            //.required("required_error_message")
                            //.placeholder("independent_since_placeholder")
                            )
                            );

        }
        if(version.equals("xiaomi")){

            productListField.changedProperty().addListener((observable, oldValue, newValue)->{
                String _productName = productListField.getSelection();

                String[] items = _productName.split(":");
                Integer index = Integer.valueOf(items[0]);
                ProductInfo productInfo = productInfoList.get(index);
                currentProductInfo = productInfo;
                productPartNumber.setValue(productInfo.getProductPartNumber());
                SKU.setValue(productInfo.getSKU());
                productColor.setValue(productInfo.getProductColor());

                productListField.persist();
            });


            String productName = productListField.getSelection();
            if (productName!=null){
                String[] items = productName.split(":");
                Integer index = Integer.valueOf(items[0]);
                ProductInfo productInfo = productInfoList.get(index);
                currentProductInfo = productInfo;
                productPartNumber.setValue(productInfo.getProductPartNumber());
                SKU.setValue(productInfo.getSKU());
                productColor.setValue(productInfo.getProductColor());
            }

            String _factoryCode = MainUtils.getDict("cartoonBoxSN", "factoryCode",version);
            Log.info(version);
            Log.info(_factoryCode);
            if (_factoryCode!=null && !_factoryCode.isEmpty()){
                factoryCode.setValue(_factoryCode);
            }
            form = Form.of(
                    Group.of(
                        productListField
                        .label("产品名称")
                        .span(ColSpan.HALF)
                        .id("productName")
                        .styleClass("field"),
                        Field.ofStringType(SKU)
                        .styleClass("field")
                        .editable(false)
                        .span(ColSpan.HALF)
                        .id("SKU")
                        .label("SKU"),
                        Field.ofStringType(factoryCode)
                        .id("factoryCode")
                        .label("工厂代码")
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .validate(LettersOrNumbersValidator.exactly(3,"只能是3位字母跟数字"))
                        .required("必填"),
                        Field.ofIntegerType(quantity)
                        .id("quantity")
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .label("装载数量")
                        .validate(IntegerRangeValidator.atLeast(1, "必须为正整数"))
                        .required("必填"),

                        Field.ofDate(selectedDate)
                            .id("createDate")
                            .styleClass("field")
                            .label("日期")
                            )

                            );

        }

        return form;
    }


	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "cartoonBox";
	}

	@Override
	public CartoonBoxSN getContainer(String snCode) throws RequestException {
		// TODO Auto-generated method stub
		return CartoonBoxAPI.byNickName(snCode);
	}

	@Override
	public String getProductName(CartoonBoxSN c) {
		// TODO Auto-generated method stub
		return c.getProductName();
	}

	@Override
	public ProductLaser getItem(String snCode) throws RequestException, BizException {
		ProductLaser laser=ProductLaserAPI.getProductLaser(snCode);
		final String boxCode=laser.getCartoonBoxCode();		
		if(boxCode!=null&&!boxCode.isBlank())
			throw new BizException("产品"+snCode+"已在中箱"+boxCode);		
		return laser;
	}

	@Override
	public String getProductId(CartoonBoxSN c) {
		// TODO Auto-generated method stub
		return c.getProductId();
	}

	@Override
	public String getItemSKU(ProductLaser item) {
		// TODO Auto-generated method stub
		return item.getSKU();
	}

	@Override
	public String getItemFactoryCode(ProductLaser item) {
		// TODO Auto-generated method stub
		return item.getFactoryCode();
	}

	@Override
	public String getContainerSKU(CartoonBoxSN c) {
		// TODO Auto-generated method stub
		return c.getSKU();
	}

	@Override
	public String getContainerFactoryCode(CartoonBoxSN c) {
		// TODO Auto-generated method stub
		return c.getFactoryCode();
	}

	@Override
	public String getContainerNickName(CartoonBoxSN c) {
		// TODO Auto-generated method stub
		return c.getNickName();
	}

	@Override
	public boolean productPrinted(ProductLaser item) {
		// TODO Auto-generated method stub
		String used = item.getUsed();
        if(used!=null && used.equals("是")){
            return true;
        }
        return false;
	}

	@Override
	public boolean productWeighted(ProductLaser item) {
		// TODO Auto-generated method stub
		String weight = item.getWeight();
        if(weight!=null && !weight.isEmpty()){
            return true;
        }
        return false;
	}


	@Override
	public boolean storage(CartoonBoxSN c, List<String> itemCodes) throws BizException, RequestException {
		// TODO Auto-generated method stub
        return CartoonBoxAPI.inputLaser(c, itemCodes);
	}

	@Override
    public boolean doXiaomiPrint(CartoonBoxSN c) {
        // TODO Auto-generated method stub
        List<ProductLaser> list;
        try{
            list = ProductLaserAPI.getByBoxCode(c.getNickName());
        }catch(RequestException e){
            MainUtils.showSimpleError("网络异常");
            Log.error("",e);
            return false;
        }
        List<String> productSNs = list.stream().map(i -> i.getSNCode()).collect(Collectors.toList());
        String[] productCodes = new String[productSNs.size()];
        productCodes = productSNs.toArray(productCodes);
        try{                                                                                                                                                                                                 
            printer.printBoxUniqueCode(xiaomiPrinterTemplate, c.getNickName(), c.getProductName(), c.getProductColor(),c.getProductId(), productCodes);
        }catch (BizException e) {
            Log.error("打印异常",e);
            MainUtils.showSimpleError("打印异常:"+e.getMessage());
            return false;
        }catch (Exception e){
            Log.error("打印异常",e);
            MainUtils.showSimpleError("打印出错:"+e.getMessage());
            return false;
        }
        return true;
    }

    @Override
	public boolean doSuShiPrint(CartoonBoxSN c) {
		// TODO Auto-generated method stub
        List<ProductLaser> list;
        try{
            list = ProductLaserAPI.getByBoxCode(c.getNickName());
        }catch(RequestException e){
            MainUtils.showSimpleError("网络异常");
            Log.error("",e);
            return false;
        }
        List<String> productSNs = list.stream().map(i -> i.getSNCode()).collect(Collectors.toList());
        String[] productCodes = new String[productSNs.size()];
        productCodes = productSNs.toArray(productCodes);

        printer.printSuShiBox(productCodes, c);
		return false;
	}

	@Override
    public String calPartSNCode(CartoonBoxSN c, String version) {
        // TODO Auto-generated method stub
        String snCode = null;           
        if(version.equals("sushi")){                                   
            //根据 C+产品型号+颜色代码+工厂代码+日期代码 + N                      
            String[] day = {"1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V"};
            String[] month = {"E","F","G","H","I","J","K","L","M","N","O","P"};
            String[] year = {"M","N","O","P","Q","R","S","T","U","V","W"};

            String dateStr = c.getCreateDate().split(" ")[0];                 
            LocalDate date = LocalDate.parse(dateStr);
            String dayCode = day[date.getDayOfMonth()-1];          
            String monthCode = month[date.getMonthValue()-1];
            String yearCode = year[date.getYear()-2020];                          
            String dateCode = yearCode + monthCode + dayCode;
            String colorCode = GlobalConfig.getInstance().getColorCode(c.getProductColor());
            snCode = "C" + MainUtils.padLeftZeros(c.getProductType(),5) + colorCode + c.getFactoryCode() + dateCode + "N";
        }                                                                
        if(version.equals("xiaomi")){                  
            //根据 MZXH + SKU + 年份 + 月份 
            String[] month = {"1","2","3","4","5","6","7","8","9","A","B","C"};
            String dateStr = c.getCreateDate().split(" ")[0];
            LocalDate date = LocalDate.parse(dateStr);
            String monthCode = month[date.getMonthValue()-1];    
            String yearCode = String.valueOf(date.getYear()).substring(3);    
            String dateCode = yearCode + monthCode;
            snCode = "MZXH" + c.getSKU() + dateCode + c.getFactoryCode();
        }
        return snCode;
    }

	@Override
	public void setContainerCreator(CartoonBoxSN c, String creator) {
		// TODO Auto-generated method stub
	    c.setCreator(creator);	
	}

	@Override
	public void setContainerStreamCode(CartoonBoxSN c, String streamCode) {
		// TODO Auto-generated method stub
	    c.setStreamCode(streamCode);	
	}

	@Override
	public void setContainerSNCode(CartoonBoxSN c, String snCode) {
		// TODO Auto-generated method stub
	    c.setSNCode(snCode);	
	}

	@Override
	public void setContainerNickName(CartoonBoxSN c, String nickName) {
		// TODO Auto-generated method stub
	    c.setNickName(nickName);	
	}

	@Override
	public void setContainerFactoryCode(CartoonBoxSN c, String factoryCode) {
		// TODO Auto-generated method stub
        c.setFactoryCode(factoryCode);
	}

	@Override
	public boolean containerDelAble(CartoonBoxSN c) {
        String nickName = c.getNickName();
		// TODO Auto-generated method stub
        // 中箱在栈板里
        String palletCode = c.getPalletCode();
        if (palletCode!=null && !palletCode.isEmpty()){
            MainUtils.showSimpleError("中箱在栈板里:"+nickName);
            return false;
        }
        // 中箱出货
        Shipping shipping;
        try{
            shipping = ShippingAPI.byBox(nickName);
        }catch(RequestException e){
            MainUtils.showSimpleError("网络异常:"+nickName);
            return false;
        }
        if(shipping!=null && !shipping.getShippingCode().isEmpty()){
            MainUtils.showSimpleError("中箱已经出货:"+nickName);
            return false;
        }
		return true;
	}

	@Override
	public boolean clearContainer(CartoonBoxSN c) throws RequestException {
		// TODO Auto-generated method stub
        int num = ProductLaserAPI.clearBoxCode(c.getNickName());
        if (num>0) return true;
		return false;
	}

	@Override
	public boolean delContainer(CartoonBoxSN c) throws RequestException {
		// TODO Auto-generated method stub
        int num = CartoonBoxAPI.delBySNCode(c.getSNCode());
        if(num==1) return true;
		return false;
	}

	@Override
	public boolean isInContainer(ProductLaser item) {
		// TODO Auto-generated method stub
        String boxCode = item.getCartoonBoxCode();
        if(boxCode==null || boxCode.isEmpty()) {
            return false;
        }
        return true;
	}

}

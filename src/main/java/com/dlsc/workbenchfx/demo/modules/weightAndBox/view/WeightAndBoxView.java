package com.dlsc.workbenchfx.demo.modules.weightAndBox.view;

//import javafx.scene.Group;
//import com.dlsc.workbenchfx.demo.CartoonBoxQRCodeUtils;
import com.dlsc.workbenchfx.demo.LettersOrNumbersValidator;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;
import com.dlsc.workbenchfx.demo.modules.common.view.BaseView;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.time.*;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.WeightDemo;
import com.dlsc.workbenchfx.demo.api.CartoonBoxAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;

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



public class WeightAndBoxView extends BaseView<CartoonBoxSN,ProductLaser> {

    Thread thread;



    public WeightAndBoxView(String creator) {
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
                            .span(ColSpan.THIRD)
                            .id("createDate"),
                        Field.ofIntegerType(0)
                            .styleClass("field")
                            .span(ColSpan.THIRD)
                            .label("下限")
                            .validate(IntegerRangeValidator.atLeast(1, "必须为正整数"))
                            .id("lowerLimit"),
                        Field.ofIntegerType(0)
                            .styleClass("field")
                            .label("下限")
                            .span(ColSpan.THIRD)
                            .validate(IntegerRangeValidator.atLeast(1, "必须为正整数"))
                            .id("lowerLimit")

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
                            .styleClass("field")
                            .label("日期")
                            .span(ColSpan.THIRD)
                            .id("createDate"),
                        Field.ofIntegerType(0)
                            .styleClass("field")
                            .span(ColSpan.THIRD)
                            .validate(IntegerRangeValidator.atLeast(0, "必须为整数"))
                            .label("称重下限(克)")
                            .id("lowerLimit"),
                        Field.ofIntegerType(1)
                            .styleClass("field")
                            .label("称重上限(克)")
                            .validate(IntegerRangeValidator.atLeast(1, "必须为正整数"))
                            .span(ColSpan.THIRD)
                            .id("lowerLimit")
                            )
                            );

        }

        return form;
    }


	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "weightAndBox";
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
	public ProductLaser getItem(String snCode) throws RequestException {
		// TODO Auto-generated method stub
		return ProductLaserAPI.getProductLaser(snCode);
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
            printer.printBoxUniqueCode(xiaomiPrinterTemplate, c.getSNCode(), c.getProductName(), c.getProductColor(),c.getProductId(), productCodes);
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
		// TODO Auto-generated method stub
        // 中箱不在栈板里
        String palletCode = c.getPalletCode();
        if (palletCode!=null && !palletCode.isEmpty()){
            MainUtils.showSimpleError("中箱在栈板里");
            return false;
        }
        // 中箱没有出货
        List<ProductLaser> list;
        try{
            list = ProductLaserAPI.getByBoxCode(c.getNickName());
        }catch(RequestException e){
            MainUtils.showSimpleError("网络异常");
            return false;
        }
        if (list!=null){
            for (int i=0; i<list.size(); i++){
                String shippingCode = list.get(i).getShippingCode();
                if (shippingCode!=null || !shippingCode.isEmpty()){
                    MainUtils.showSimpleError("中箱已经出货,不能删除");
                    return false;
                }
            }
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
    public void weightAndPackAndSubmit(String snCode){
        final String portName = MainUtils.getLocalCache("system", "weighPort");
        Log.info("称重机串口:"+portName);
        if(portName==null || portName.isEmpty()){
            MainUtils.showSimpleError("电子称端口还没配置!请去全局设置配置");                        
            lblMessage.setText("电子称端口还没配置!请去全局设置配置");
            return;
        }
        final String unit = MainUtils.getLocalCache("system", "unit");//测量单位
        if (unit==null || unit.isEmpty()){
            MainUtils.showSimpleError("请去“全局”设置电子秤读数单位!");                        
            lblMessage.setText("请去“全局”设置电子秤读数单位!");
            return;
        }
        final Float minValue=MainUtils.getDictFloat("system", "wtMinValue");//电子秤最小读数
        final Integer N=MainUtils.getDictInt("system", "weightN");//稳定因子
        final WeightDemo demo=new WeightDemo(portName, minValue,N);
        txtItem.setEditable(false);//禁止录入SN码                 
        lblMessage.setText("读数中,请先放置产品 ...");
        Runnable runnable =
            () -> {
                try {
                    Log.debug("称重开始=========");                            
                    Float weightValue=null;
                    try {
                        weightValue=demo.getAvgValue();
                    }catch (Exception _e) {//读数失败，超时，连接异常等
                        Platform.runLater(()->{
                            showErr("failed:"+_e.getMessage());	                            		
                        });
                        return;//异常解锁返回
                    }
                    if(weightValue==null) {//称重被打断(主动关闭窗口等)                            	
                        return;
                    }
                    final String showUnit="g";//重量显示单位
                    weightValue=MainUtils.weightToShow(weightValue, unit, showUnit);	                            
                    Log.debug("称重结束=========");   	                            
                    packAndSubmit(snCode);
                }catch (Throwable t) {
                    Log.error("称重异常", t);
                }finally {
                    txtItem.setEditable(true);//放开SN码录入限制
                }
            };
        if(thread!=null) {
            stopWeightTask();
        }
        thread = new Thread(runnable);
        thread.setDaemon(true);//系统关闭时自动销毁线程
        thread.start();//启动称重线程
    }
    void showErr(String msg) {
    	Platform.runLater(()->{
        	lblMessage.setTextFill(Color.RED);
            lblMessage.setText(msg);
    	});
    }
    //void weightProcess(ProductLaser laser, Float value){    
    //	String weightValue=new DecimalFormat("0").format(value);
    //	Log.debug("正在更新产品重量:"+laser.getSNCode()+";重量:"+weightValue+"g");
    //	WeighRecord wr=addTable(laser.getSNCode(), weightValue); //刷新列表 
    //	refreshTableView();
    //	String rs=wr.getResult();
    //	if("通过".equals(rs)) {
    //		showOK("passed !"+" 重量: "+weightValue+"g");    		
    //	}else {
    //		showErr("failed:"+rs+";重量:"+weightValue+"g");
    //		return;
    //	}
    //	try {
	//		ProductLaserAPI.updateWeight(laser.getId(), weightValue);
	//		txtUserName.setUserData(null);//清空数据,确保一个产品重量只更新一次
	//		Log.debug("产品重量更新成功");
	//	} catch (BizException e) {
	//		Log.debug("产品重量更新失败:"+e.getMessage());
	//		showErr("failed:产品重量更新失败",e.getMessage());
	//	}    
    //}
    public void stopWeightTask() {
    	try {
	        if(thread==null||thread.isInterrupted())return;        	
	        Log.warn("称重线程未终止,将强制中断");        
        	thread.interrupt();
        }catch (Throwable t) {
        	Log.warn("中断称重线程失败");
		}finally {
			Platform.runLater(()->{
				txtItem.clear();
				lblMessage.setText("");
			});
		}        
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

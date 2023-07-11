package com.dlsc.workbenchfx.demo.modules.palletSN.view;

import com.cg.core.util.Log;
import javafx.scene.Node;
//import javafx.scene.Group;



//import com.dlsc.workbenchfx.demo.CartoonBoxQRCodeUtils;
import com.dlsc.workbenchfx.demo.LettersOrNumbersValidator;


import com.alibaba.fastjson2.JSON;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
//import com.dlsc.formsfx.model.structure.SelectionField;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;
import com.dlsc.workbenchfx.demo.modules.common.view.BaseView;
import com.dlsc.workbenchfx.demo.modules.palletSN.model.PalletSN;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
//import javafx.scene.control.RadioButton;
import javafx.beans.property.*;

import java.time.*;

import com.dlsc.workbenchfx.demo.MainUtils;
//import com.dlsc.workbenchfx.demo.PrintPDF;
import com.dlsc.workbenchfx.demo.api.CartoonBoxAPI;
import com.dlsc.workbenchfx.demo.api.PalletAPI;
import com.dlsc.workbenchfx.demo.api.ShippingAPI;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;
import com.dlsc.workbenchfx.demo.modules.shipping.model.Shipping;

import java.util.*;

import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.util.ColSpan;

import com.dlsc.formsfx.model.structure.Field;
import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;








public class PalletSNView extends BaseView<PalletSN,CartoonBoxSN> {


    public PalletSNView(String creator) {
        this.creator = creator;
    }


    @Override
    public Form getForm(String version){
        Form formInstance = null;
            SingleSelectionField<String> productListField = Field.ofSingleSelectionType(productList, 0).label("产品列表");
            if (productList.isEmpty()){
                productListField.tooltip("先去\"产品设置\"填好产品信息!");
            }

            productListField.setBindingMode(BindingMode.CONTINUOUS);
            StringProperty factoryCode = new SimpleStringProperty("");
            SimpleStringProperty workshopCode = new SimpleStringProperty("");
            SimpleStringProperty from = new SimpleStringProperty("");
            SimpleStringProperty to = new SimpleStringProperty("");
            SimpleIntegerProperty quantity = new SimpleIntegerProperty(0);


            ObjectProperty<LocalDate> selectedDate  = new SimpleObjectProperty<LocalDate>(LocalDate.now());

            StringProperty productPartNumber = new SimpleStringProperty("");
            //StringProperty productName = new SimpleStringProperty("");
            StringProperty productType = new SimpleStringProperty("");
            StringProperty productColor = new SimpleStringProperty("");
            StringProperty PO = new SimpleStringProperty("");
            SimpleDoubleProperty grossWeight = new SimpleDoubleProperty(0);
            SimpleStringProperty SKU = new SimpleStringProperty("");



        if (version.equals("sushi")){
            productListField.changedProperty().addListener((observable, oldValue, newValue)->{
                String _productName = productListField.getSelection();
                String[] items = _productName.split(":");
                Integer index = Integer.valueOf(items[0]);
                ProductInfo productInfo = productInfoList.get(index);

                currentProductInfo = productInfo;
                productPartNumber.setValue(productInfo.getProductPartNumber());
                productType.setValue(productInfo.getProductType());
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
                productType.setValue(productInfo.getProductType());
                productColor.setValue(productInfo.getProductColor());
            }





            formInstance = Form.of(
                    Group.of(
                        productListField
                        .label("产品名称")
                        .id("productName")
                        .span(ColSpan.HALF)
                        .styleClass("field"),
                        Field.ofStringType(productPartNumber)
                        .id("productPartNumber")
                        .editable(false)
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .label("产品料号"),
                        Field.ofStringType(productType)
                        .id("productType")
                        .styleClass("field")
                        .editable(false)
                        .span(ColSpan.HALF)
                        .label("产品型号"),
                        Field.ofStringType(productColor)
                        .id("productColor")
                        .styleClass("field")
                        .editable(false)
                        .span(ColSpan.HALF)
                        .label("产品颜色"),
                            Field.ofStringType(factoryCode).label("工厂代码")
                                .id("factoryCode")
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .validate(StringLengthValidator.atLeast(2, "最多2个"))
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .required("必填"),
                        Field.ofStringType(workshopCode)
                            .id("workshopCode")
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .label("车间代码")
                        .validate(StringLengthValidator.upTo(2, "最多2个"))
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .required("必填"),
                        Field.ofStringType(from)
                            .id("fromArea")
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .label("出货地")
                        .required("必填"),
                        Field.ofStringType(to)
                            .id("toArea")
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .label("目的地")
                        .required("必填"),
                        Field.ofIntegerType(quantity)
                            .id("quantity")
                        .styleClass("field")
                            .span(ColSpan.HALF)
                            .label("装板数量")
                            .validate(IntegerRangeValidator.atLeast(1, "必须大于0")),
                        Field.ofDate(selectedDate)
                            .id("createDate")
                        .styleClass("field")
                            .label("日期")
                            .span(ColSpan.HALF)
                            )
                            );


        }
        if (version.equals("xiaomi")){
            productListField.setBindingMode(BindingMode.CONTINUOUS);
            StringProperty productId = new SimpleStringProperty();

            productListField.changedProperty().addListener((observable, oldValue, newValue)->{
                String _productName = productListField.getSelection();
                String[] items = _productName.split(":");
                Integer index = Integer.valueOf(items[0]);
                ProductInfo productInfo = productInfoList.get(index);

                currentProductInfo = productInfo;
                productPartNumber.setValue(productInfo.getProductPartNumber());
                productType.setValue(productInfo.getProductType());
                productColor.setValue(productInfo.getProductColor());
                SKU.setValue(productInfo.getSKU());
                productId.setValue(productInfo.getProductId());
                productListField.persist();
            });


            String productName = productListField.getSelection();
            if (productName!=null){
                String[] items = productName.split(":");
                Integer index = Integer.valueOf(items[0]);
                ProductInfo productInfo = productInfoList.get(index);

                System.out.println(JSON.toJSONString(productInfo));

                currentProductInfo = productInfo;
                productPartNumber.setValue(productInfo.getProductPartNumber());
                productType.setValue(productInfo.getProductType());
                productColor.setValue(productInfo.getProductColor());
                productId.setValue(productInfo.getProductId());
                SKU.setValue(productInfo.getSKU());
            }


            String _factoryCode = MainUtils.getDict("palletSN", "factoryCode",version);
            Log.info(version);
            Log.info(_factoryCode);
            if (_factoryCode!=null && !_factoryCode.isEmpty()){
                factoryCode.setValue(_factoryCode);
            }
            formInstance = Form.of(
                    Group.of(
                        productListField
                        .label("产品名称")
                        .id("productName")
                        .span(ColSpan.THIRD)
                        .styleClass("field"),
                        Field.ofStringType(SKU)
                        .id("SKU")
                        .editable(false)
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        .label("SKU"),
                        Field.ofStringType(productId)
                        .id("productId")
                        .editable(false)
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        .label("商品 id"),
                        //Field.ofStringType(productType)
                        //.styleClass("field")
                        //.editable(false)
                        //.span(ColSpan.HALF)
                        //.label("产品型号"),
                        //Field.ofStringType(productColor)
                        //.styleClass("field")
                        //.editable(false)
                        //.span(ColSpan.HALF)
                        //.label("产品颜色"),
                            Field.ofStringType(factoryCode).label("工厂代码")
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .validate(StringLengthValidator.exactly(3, "3位"))
                                .id("factoryCode")
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        //.validate(StringLengthValidator.atLeast(2, "最多2个"))
                        .required("必填"),
                        Field.ofStringType(workshopCode)
                            .id("workshopCode")
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        .label("生产线")
                        .validate(StringLengthValidator.upTo(2, "最多2个"))
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .validate(StringLengthValidator.exactly(1, "1位"))
                        .required("必填"),
                        //Field.ofStringType(from)
                        //.styleClass("field")
                        //.span(ColSpan.HALF)
                        //.label("出货地")
                        //.required("必填"),
                        //Field.ofStringType(to)
                        //.styleClass("field")
                        //.span(ColSpan.HALF)
                        //.label("目的地")
                        //.required("必填"),
                        Field.ofStringType(PO)
                            .id("PO")
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        .required("必填")
                        .label("采购订单号"),
                        Field.ofDoubleType(grossWeight)
                            .id("grossWeight")
                        .styleClass("field")
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                        .span(ColSpan.THIRD)
                        .label("托盘毛重(KG)"),

                        Field.ofIntegerType(quantity)
                            .id("quantity")
                        .styleClass("field")
                            .span(ColSpan.THIRD)
                            .label("装托数量")
                            .validate(IntegerRangeValidator.atLeast(1, "必须大于0")),

                        Field.ofDate(selectedDate)
                        .styleClass("field")
                        .id("createDate")
                            .label("日期")
                            .span(ColSpan.THIRD)
                            )
                            );


        }

        return formInstance;
    }

    public static int pcsPerPallet(String palletNickName) throws RequestException{
        List<CartoonBoxSN>  boxs;
        boxs = CartoonBoxAPI.getCartoonBoxByPallet(palletNickName);
        if(boxs==null || boxs.isEmpty()){
            return 0;
        }
        int count = 0;
        for(int i=0; i<boxs.size(); i++){
            List<ProductLaser> lasers = ProductLaserAPI.getByBoxCode(boxs.get(i).getNickName());
            if(lasers != null && !lasers.isEmpty()){
                count += lasers.size();
            }
        }
        return count;
    }

    @Override
    public PalletSN getContainer(String nickName) throws RequestException {

        PalletSN probe = new PalletSN();
        probe.setNickName(nickName);
        List<PalletSN> list = PalletAPI.getPallet(probe);
        //注意nickName是唯一的,所以只能查出1个
        if (list.size()!=1){
            return null;
        }
        return list.get(0);
    }


    @Override
    public String getProductName(PalletSN c) {
        // TODO Auto-generated method stub
        return c.getProductName();
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return "pallet";
    }


	@Override
	public CartoonBoxSN getItem(String snCode) throws RequestException {
		// TODO Auto-generated method stub
        CartoonBoxSN probe = new CartoonBoxSN();
        probe.setNickName(snCode);
        List<CartoonBoxSN> list = CartoonBoxAPI.getCartoonBox(probe);
        //注意nickName是唯一的,所以只能查出1个
        if (list.size()!=1){
            return null;
        }
        return list.get(0);
	}


	@Override
	public String getProductId(PalletSN c) {
		// TODO Auto-generated method stub
		return c.getProductId();
	}


	@Override
	public String getItemSKU(CartoonBoxSN item) {
		// TODO Auto-generated method stub
		return item.getSKU();
	}


	@Override
	public String getItemFactoryCode(CartoonBoxSN item) {
		// TODO Auto-generated method stub
		return item.getFactoryCode();
	}


	@Override
	public String getContainerSKU(PalletSN c) {
		// TODO Auto-generated method stub
		return c.getSKU();
	}


	@Override
	public String getContainerFactoryCode(PalletSN c) {
		// TODO Auto-generated method stub
		return c.getFactoryCode();
	}


	@Override
	public boolean productPrinted(CartoonBoxSN item) {
		// TODO Auto-generated method stub
		String status = item.getStatus();
        if (status!=null && status.equals("是")){
            return true;
        }
        return false;
	}


	@Override
	public boolean storage(PalletSN c, List<String> itemCodes)throws BizException {
		// TODO Auto-generated method stub
        return PalletAPI.inputBox(c, itemCodes);
	}


	@Override
	public boolean doXiaomiPrint(PalletSN c) {
		// TODO Auto-generated method stub
		//return false;
        int pcs;
        try{
            pcs = pcsPerPallet(c.getNickName());
        }catch(RequestException e){
            MainUtils.showSimpleError("网络异常");
            return false;
        }
        return printer.printPalletUniqueCode(xiaomiPrinterTemplate, c.getPO(),
                c.getSKU(), c.getProductId(), 
                dateStr(c.getCreateDate()), c.getProductName(),c.getProductColor(),
                pcs +" PCS/PLT", c.getGrossWeight(), c.getNickName());
	}


	@Override
	public boolean doSuShiPrint(PalletSN c) {
		// TODO Auto-generated method stub
        return printer.printSuShiPallet(c.getNickName(),c.getFromArea(),c.getToArea());
	}


	@Override
	public String calPartSNCode(PalletSN c, String version) {
		// TODO Auto-generated method stub
        String snCode = null;
        //根据 P+工厂代码+车间代码+日期代码
        if (version.equals("sushi")){
            String[] day = {"1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V"};
            String[] month = {"E","F","G","H","I","J","K","L","M","N","O","P"};
            String[] year = {"M","N","O","P","Q","R","S","T","U","V","W"};
            String dateStr = c.getCreateDate().split(" ")[0];
            LocalDate date = LocalDate.parse(dateStr);
            String dayCode = day[date.getDayOfMonth()-1];
            String monthCode = month[date.getMonthValue()-1];
            String yearCode = year[date.getYear()-2020];
            String dateCode = yearCode + monthCode + dayCode;
            snCode = "P" + c.getFactoryCode() + c.getWorkshopCode() + dateCode;
        }
        //根据 P+SKU+工厂代码+日期代码+生产线+工厂代码
        if (version.equals("xiaomi")){
            String[] month = {"1","2","3","4","5","6","7","8","9","A","B","C"};
            String dateStr = c.getCreateDate().split(" ")[0];
            LocalDate date = LocalDate.parse(dateStr);
            String monthCode = month[date.getMonthValue()-1];
            String yearCode = String.valueOf(date.getYear()).substring(3);
            String dateCode = yearCode + monthCode;
            snCode = "P" + c.getSKU() + dateCode + c.getWorkshopCode() + c.getFactoryCode();
        }

        return snCode;
	}


	@Override
	public void setContainerCreator(PalletSN c, String creator) {
		// TODO Auto-generated method stub
	    c.setCreator(creator);	
	}


	@Override
	public void setContainerStreamCode(PalletSN c, String streamCode) {
		// TODO Auto-generated method stub
		
        c.setStreamCode(streamCode);
	}


	@Override
	public void setContainerSNCode(PalletSN c, String snCode) {
		// TODO Auto-generated method stub
	    
        c.setSNCode(snCode);
	}


	@Override
	public void setContainerNickName(PalletSN c, String nickName) {
		// TODO Auto-generated method stub
		
        c.setNickName(nickName);
	}


	@Override
	public String getContainerNickName(PalletSN c) {
		// TODO Auto-generated method stub
		return c.getNickName();
	}


	@Override
	public void setContainerFactoryCode(PalletSN c, String factoryCode) {
		// TODO Auto-generated method stub
		c.setFactoryCode(factoryCode);
	}


    boolean cartoonBoxOnShip(CartoonBoxSN c){
        // 中箱没有出货
        List<ProductLaser> list;
        try{
            list = ProductLaserAPI.getByBoxCode(c.getNickName());
        }catch(RequestException e){
            MainUtils.showSimpleError("网络异常");
            return true;
        }
        if (list!=null){
            for (int i=0; i<list.size(); i++){
                String shippingCode = list.get(i).getShippingCode();
                if (shippingCode!=null || !shippingCode.isEmpty()){
                    //MainUtils.showSimpleError("中箱已经出货");
                    return true;
                }
            }
        }
		return false;
    }

	@Override
	public boolean containerDelAble(PalletSN c) {
		// TODO Auto-generated method stub
        // 栈板出货
        String nickName = c.getNickName();
        Shipping shipping;
        try{
            shipping = ShippingAPI.byPallet(nickName);
        }catch(RequestException e){
            MainUtils.showSimpleError("网络异常:"+nickName);
            return false;
        }
        if(shipping!=null && shipping.getShippingCode()!=null&&!shipping.getShippingCode().isEmpty()){
            MainUtils.showSimpleError("栈板已经出货:"+nickName);
            return false;
        }

		return true;
	}


	@Override
	public boolean clearContainer(PalletSN c) throws RequestException {
		// TODO Auto-generated method stub
        int num = CartoonBoxAPI.clearPalletCode(c.getNickName());
        if (num>0) return true;
		return false;
	}


	@Override
	public boolean delContainer(PalletSN c) throws RequestException {
		// TODO Auto-generated method stub
        int num = PalletAPI.delBySNCode(c.getSNCode());
        if(num==1) return true;

		return false;
	}


	@Override
	public boolean productWeighted(CartoonBoxSN item) {
		// TODO Auto-generated method stub
		return true;
	}

    String dateStr(String date){
        String[] date_time = date.split(" ");
        String[] year_month_day = date_time[0].split("-");
        String dateCode = year_month_day[0] + year_month_day[1] + year_month_day[2];
        return dateCode;
    }


	@Override
	public boolean isInContainer(CartoonBoxSN item) {
		// TODO Auto-generated method stub
        String palletCode = item.getPalletCode();
        if(palletCode==null || palletCode.isEmpty()) {
            return false;
        }
        return true;
	}

    @Override
    public boolean boxOnShip(CartoonBoxSN item){
        // 中箱出货
        String nickName = item.getNickName();
        Shipping shipping;
        try{
            shipping = ShippingAPI.byBox(nickName);
        }catch(RequestException e){
            MainUtils.showSimpleError("网络异常:"+nickName);
            return true;
        }
        if(shipping!=null &&  shipping.getShippingCode()!= null && !shipping.getShippingCode().isEmpty()){
            MainUtils.showSimpleError("中箱已经出货:"+nickName);
            return true;
        }
        return false;
    }


}

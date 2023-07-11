package com.dlsc.workbenchfx.demo.global;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.cg.core.util.MathUtil;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.ModelUtil;
import com.dlsc.workbenchfx.demo.NumbersValidator;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 产品模板表单
 * @author LiangGuanHao
 *
 */
public class ProductInfoForm {
	private final GlobalConfig globalConfig=GlobalConfig.getInstance();
	private final SingleSelectionField<String> productNamesField=Field.ofSingleSelectionType(globalConfig.getProductNames(),0).id("productName").styleClass("field");
	/**
	 * 产品类型(素士)
	 */
	private final StringField productType=createStringField("productType");
	/**
	 * 产品颜色名
	 */
	private final StringField productColor=createStringField("productColor");
	/**
	 * 素士产品料号(类似小米商品ID)
	 */
	private final StringField productPartNumber=createStringField("productPartNumber");
	/**
	 * 小米商品ID
	 */
	private final StringField productId=createStringField("productId");
	/**
	 * 小米69码
	 */
	private final StringField code69=createStringField("code69");
	/**
	 * 小米SKU
	 */
	private final StringField SKU=createStringField("SKU");
	/**
	 * 代工厂编码
	 */
	private final StringField factoryCode=createStringField("factoryCode");
	/**
	 * 小米生态链公司编码
	 */
	private final StringField ecologicalChain=createStringField("ecologicalChain");
	/**
	 * 生成SN码的数量(镭雕)
	 */
	private final StringField productNumber=createStringField("quantity");
	/**
	 * 当前日期(具体作用描述不是很清晰)
	 */
	private final ObjectProperty<LocalDate> selectedDate=new SimpleObjectProperty<>(LocalDate.now());
	/**
	 * 当前选中的产品模板
	 */
	private ProductInfo selectedProductInfo;
	private final String bizType;//业务类型
	private Form formInstance=null;
	public ProductInfoForm(String bizType) {
		this.bizType=bizType;
		String _factoryCode = MainUtils.getDict("productLaser", "factoryCode",globalConfig.getClientType());
        if (_factoryCode!=null && !_factoryCode.isEmpty()){
        	setValue(factoryCode, _factoryCode);
        }

        String _quantity = MainUtils.getDict("productLaser", "quantity", globalConfig.getClientType());
        if (_quantity!=null && !_quantity.isEmpty()){
        	setValue(productNumber, _quantity);
        }

        String _ecologicalChain = MainUtils.getDict("productLaser", "ecologicalChain",globalConfig.getClientType());
        if (_ecologicalChain!=null && !_ecologicalChain.isEmpty()){
        	setValue(ecologicalChain, _ecologicalChain);
        }
	}
	private void setValue(StringField field,String value) {
		if(value==null||"null".equals(value))value="";
		field.userInputProperty().setValue(value);		
	}
	private StringField createStringField(String id) {
		return Field.ofStringType(new SimpleStringProperty()).id(id).styleClass("field");
	}
	private void flushSelectedInfo(final String productName) {
    	if(productName==null||productName.isBlank()) {
    		MainUtils.showSimpleError("请先配置产品模板");
    		return;
    	}
    	String[] items = productName.split(":");
        Integer index = Integer.valueOf(items[0]);
        this.selectedProductInfo =globalConfig.getAllProductInfos().get(index);//选中的产品模板       
        //更新通用属性
        
        setValue(productType,this.selectedProductInfo.getProductType());
        setValue(productColor,this.selectedProductInfo.getProductColor());
        //更新可选属性
        setValueIfExist(this.factoryCode, this.selectedProductInfo.getFactoryCode());
        setValueIfExist(this.productPartNumber, this.selectedProductInfo.getProductPartNumber());        
        setValueIfExist(this.code69, this.selectedProductInfo.getCode69());
        setValueIfExist(this.productId, this.selectedProductInfo.getProductId());
        setValueIfExist(this.SKU, this.selectedProductInfo.getSKU());      
    }
    private void setValueIfExist(StringField field,String value) {
    	if(value!=null&&!value.isBlank()&&!"null".equals(value)) {    		
    		field.userInputProperty().setValue(value);
    	}
    }
    /**
     * 镭雕
     */
    public final static String LASER="laser";
    /**
     * 彩盒
     */
    public final static String COLORBOX="colorBox";
    /**
     * 
     * @param version 客户端类型
     * @return 产品模板的form表单对象
     */
    public Form getFormInstanceByConfig(String version){      	
    	switch(bizType) {
    	case LASER:
    		return getFormForLaser(version);
    	case COLORBOX:
    		return getFormForColorBox(version);
    	default:
    		throw new UnsupportedOperationException("不支持的业务模块");
    	}
    }
	/**
     * 
     * @param version 客户端类型
     * @return 产品模板的form表单对象
     */
    private Form getFormForLaser(String version){                    
        productNamesField.setBindingMode(BindingMode.CONTINUOUS);
        if(productNamesField.getItems().isEmpty()) {
        	productNamesField.tooltip("先去\"产品设置\"填好产品信息!");
        }     
        flushSelectedInfo(productNamesField.getSelection());//刷新当前选中的模板信息
        productNamesField.changedProperty().addListener((observable, oldValue, newValue)->{
        	flushSelectedInfo(productNamesField.getSelection());//刷新当前选中的模板信息                 
        });
        
        if (version.equals("sushi")){            
            formInstance = Form.of(
                    Group.of(
                        productNamesField
                        .span(6)                        
                        .label("产品名称"),
                        productPartNumber.editable(false).span(6).required("必填").label("产品料号"),                       
                        productType.editable(false).span(6).required("必填").label("产品型号"),
                        productColor.editable(false)
                        .span(6)                        
                        .required("必填")                        
                        .label("产品颜色"),
                        factoryCode.span(4).label("工厂代码").editable(false).required("必填"),
                         productNumber
                        .span(4)
                        .label("产品数量")
                        .validate(NumbersValidator.justNumber("只能是正整数"))
                        .required("必填"),
                        Field.ofDate(selectedDate)
                        .span(4)
                        .styleClass("field")
                        .id("createDate")
                        .label("日期")
                        .editable(false)
                        )
                    );
        }
        if (version.equals("xiaomi")){
            formInstance = Form.of(
                    Group.of(
                        productNamesField.label("SKU 名称"),
                        productType.editable(false).span(4).label("产品型号"),
                        productColor.editable(false).span(4).required("必填").label("产品颜色"),
                        code69.editable(false).span(4).required("必填").label("69 码"),
                        productId.editable(false).span(4).required("必填").label("商品 id"),
                        SKU.editable(false).span(4).required("必填").label("SKU"),
                        ecologicalChain.span(4).required("必填").validate(StringLengthValidator.exactly(2, "必须2位")).label("生态链公司"),
                        factoryCode.label("工厂代码").span(4).validate(StringLengthValidator.exactly(3, "必须3位")).required("必填"),
                        productNumber.span(4).label("产品数量")
                        .validate(NumbersValidator.justNumber("只能是正整数"))
                        .required("必填"),
                        Field.ofDate(selectedDate)
                        .span(4)
                        .styleClass("field")
                        .id("createDate")
                        .label("日期").editable(false)
                        )
                    );

        }
        return formInstance;
    }
    private Form getFormForColorBox(String version){                   
        productNamesField.setBindingMode(BindingMode.CONTINUOUS);
        if(productNamesField.getItems().isEmpty()) {
        	productNamesField.tooltip("先去\"产品设置\"填好产品信息!");
        }     
        flushSelectedInfo(productNamesField.getSelection());//刷新当前选中的模板信息
        productNamesField.changedProperty().addListener((observable, oldValue, newValue)->{
        	flushSelectedInfo(productNamesField.getSelection());//刷新当前选中的模板信息                 
        });
        
        if (version.equals("sushi")){            
            formInstance = Form.of(
                    Group.of(
                        productNamesField
                        .span(6)                        
                        .label("产品名称"),
                        productPartNumber.editable(false).span(6).required("必填").label("产品料号"),                       
                        productType.editable(false).span(6).required("必填").label("产品型号"),
                        productColor.editable(false)
                        .span(6)                        
                        .required("必填")                        
                        .label("产品颜色"),
                        factoryCode.span(4).label("工厂代码").editable(false).required("必填"),
                        Field.ofDate(selectedDate)
                        .span(4)
                        .styleClass("field")
                        .id("createDate")
                        .label("日期")
                        .editable(false)
                        )
                    );
        }
        if (version.equals("xiaomi")){
            formInstance = Form.of(
                    Group.of(
                        productNamesField.label("SKU 名称"),
                        productType.editable(false).span(4).label("产品型号"),
                        productColor.editable(false).span(4).required("必填").label("产品颜色"),
                        code69.editable(false).span(4).required("必填").label("69 码"),
                        productId.editable(false).span(4).required("必填").label("商品 id"),
                        SKU.editable(false).span(4).required("必填").label("SKU"),
                        ecologicalChain.span(4).required("必填").validate(StringLengthValidator.exactly(2, "必须2位")).label("生态链公司"),
                        factoryCode.label("工厂代码").span(4).validate(StringLengthValidator.exactly(3, "必须3位")).required("必填"),
                        Field.ofDate(selectedDate)
                        .span(4)
                        .styleClass("field")
                        .id("createDate")
                        .label("日期").editable(false)
                        )
                    );

        }
        return formInstance;
    }
    public String getFactoryCode() {    	
    	return factoryCode.getUserInput();
    }
    public String getEcologicalChain() {    	
    	return ecologicalChain.getUserInput();
    }
    public void cacheInput() {
    	MainUtils.addOrUpdateLocalCache("productLaser", "factoryCode", factoryCode.getValue(),globalConfig.getClientType());
        MainUtils.addOrUpdateLocalCache("productLaser", "quantity", productNumber.getValue(),globalConfig.getClientType());
        MainUtils.addOrUpdateLocalCache("productLaser", "ecologicalChain", ecologicalChain.getValue(),globalConfig.getClientType());
    }
    public Integer getProductCount() {    	
    	return MathUtil.toInteger(productNumber.getUserInput());
    }
    public String getCreateDate() {
    	return selectedDate.get().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
    /**
     * 
     * @return 根据模板和客户端类型生成的laser对象,若未选择模板则返回null
     */
	public ProductLaser createLaser() {
		if(selectedProductInfo==null) return null;
		final String version = globalConfig.getClientType();
		final ProductLaser laser = new ProductLaser();// 产品模板
		ModelUtil.fillLaserValueFromInfo(laser, selectedProductInfo);
		if ("sushi".equals(version)) {// 小米
			laser.setStatus("N");
		} else if ("xiaomi".equals(version)) {
			laser.setFactoryCode(getFactoryCode());
			laser.setEcologicalChain(getEcologicalChain());
		}
		return laser;
	}
	public ProductInfo getSelectedProductInfo() {
		return selectedProductInfo;
	}
	public void persist() {
		if(formInstance!=null) {
			formInstance.persist();
		}
	}
}

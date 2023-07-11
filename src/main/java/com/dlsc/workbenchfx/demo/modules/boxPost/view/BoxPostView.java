
package com.dlsc.workbenchfx.demo.modules.boxPost.view;

import com.dlsc.workbenchfx.demo.NumbersValidator;
import com.dlsc.workbenchfx.demo.modules.post.view.PostView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.util.ColSpan;


public class BoxPostView extends PostView {

    @Override
    public Form getForm(){
        Form form = null;
        //MainUtils.recoverSelection(productListField, "cartoonBox", "productName",version);

        IntegerProperty quantity = new SimpleIntegerProperty(0);
        StringProperty productName = new SimpleStringProperty("");
        StringProperty productId = new SimpleStringProperty("");
        StringProperty productPartNumber = new SimpleStringProperty("");
        StringProperty productType = new SimpleStringProperty("");
        StringProperty productColor = new SimpleStringProperty("");
        StringProperty SKU = new SimpleStringProperty("");
        StringProperty factoryCode = new SimpleStringProperty("");
        StringProperty supplier = new SimpleStringProperty("");
        StringProperty prodPlace = new SimpleStringProperty("");
        StringProperty salesArea = new SimpleStringProperty("");
        StringProperty PO = new SimpleStringProperty("");
        StringProperty inspectors = new SimpleStringProperty("");
        StringProperty productSpec = new SimpleStringProperty("");
        StringProperty pkgSpec = new SimpleStringProperty("");
        StringProperty volume = new SimpleStringProperty("");
        StringProperty dstCity = new SimpleStringProperty("");
        StringProperty dstStoreHouse = new SimpleStringProperty("");
        StringProperty boxSize = new SimpleStringProperty("");



        StringProperty boxNetWeight = new SimpleStringProperty("");
        StringProperty boxGrossWeight = new SimpleStringProperty("");
        ObjectProperty<LocalDate> selectedDate  = new SimpleObjectProperty<LocalDate>(LocalDate.now());


        form = Form.of(
                Group.of(
                    Field.ofStringType(supplier)
                        .id("supplier")
                        .label("供应商")
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .required("必填"),
                    Field.ofStringType(prodPlace)
                        .id("prodArea")
                        .label("生产地")
                        .styleClass("field")
                        .span(ColSpan.HALF)
                        .required("必填"),
                    Field.ofStringType(productId)
                        .id("productId")
                        .label("产品ID")
                        .validate(NumbersValidator.justNumber("只能是数字"))
                        .validate(StringLengthValidator.exactly(5,"5位数字"))
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        .required("必填"),
                    Field.ofStringType(productColor)
                        .id("color")
                        .label("产品颜色")
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        .required("必填"),
                    Field.ofStringType(salesArea)
                        .id("salesRegion")
                        .label("销售区域")
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        .required("必填"),

                    Field.ofStringType(productName)
                    .label("产品名称")
                    .span(ColSpan.THIRD)
                    .id("productName")
                        .required("必填")
                    .styleClass("field"),
                    Field.ofStringType(SKU)
                    .styleClass("field")
                    .span(ColSpan.THIRD)
                    .id("SKU")
                        .required("必填")
                    .label("产品料号"),
                    Field.ofStringType(productSpec)
                        .id("productSpec")
                        .label("产品规格")
                        .styleClass("field")
                        .required("必填")
                        .span(ColSpan.THIRD),
                    Field.ofStringType(pkgSpec)
                        .id("pkgSpec")
                        .label("包装规格")
                        .styleClass("field")
                        .required("必填")
                        .span(ColSpan.HALF),
                    Field.ofStringType(PO)
                        .id("PO")
                        .styleClass("field")
                        .label("采购订单号")
                        .span(ColSpan.HALF)
                        .required("必填"),
                    Field.ofDate(selectedDate)
                        .id("createDate")
                        .styleClass("field")
                        .required("必填")
                        .span(ColSpan.HALF)
                        .label("生产日期"),
                    Field.ofStringType(inspectors)
                        .id("inspection")
                        .label("检验员/检验批次")
                        .styleClass("field")
                        .required("必填")
                        .span(ColSpan.HALF),
                    Field.ofStringType(boxGrossWeight)
                        .id("boxGrossWeight")
                        .styleClass("field")
                        .label("毛重(kg)")
                        .span(ColSpan.THIRD)
                        .required("必填")
                        .span(ColSpan.THIRD),
                    Field.ofStringType(boxNetWeight)
                        .id("boxNetWeight")
                        .styleClass("field")
                        .label("净重(kg)")
                        .span(ColSpan.THIRD)
                        .required("必填")
                        .span(ColSpan.THIRD),
                    Field.ofStringType(boxSize)
                        .id("boxSize")
                        .styleClass("field")
                        .label("中箱尺寸(cm*cm*cm)")
                        .span(ColSpan.THIRD)
                        .required("必填")
                        .span(ColSpan.THIRD),

                    Field.ofStringType(volume)
                        .id("volume")
                        .styleClass("field")
                        .label("体积(m^3)")
                        .span(ColSpan.THIRD)
                        .required("必填"),
                    Field.ofStringType(dstCity)
                        .id("dstCity")
                        .label("目的城市")
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        .required("必填"),
                    Field.ofStringType(dstStoreHouse)
                        .id("dstArea")
                        .label("目的仓")
                        .styleClass("field")
                        .span(ColSpan.THIRD)
                        .required("必填")

                        //.span(ColSpan.THIRD)
                        //.required("required_error_message")
                        //.placeholder("independent_since_placeholder")
                        )

                        );
        return form;

    }

    @Override
    public String getTitle(){
        return "box";
    }


}

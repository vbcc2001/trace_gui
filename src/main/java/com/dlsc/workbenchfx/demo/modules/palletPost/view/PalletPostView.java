
package com.dlsc.workbenchfx.demo.modules.palletPost.view;

import com.dlsc.workbenchfx.demo.NumbersValidator;
import com.dlsc.workbenchfx.demo.modules.post.view.PostView;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.validators.StringLengthValidator;


public class PalletPostView extends PostView {

    @Override
    public Form getForm(){
        Form form = null;
        //MainUtils.recoverSelection(productListField, "cartoonBox", "productName",version);

        SimpleObjectProperty<LocalDate> selectedDate  = new SimpleObjectProperty<LocalDate>(LocalDate.now());
        SimpleStringProperty dstCity  = new SimpleStringProperty("");
        SimpleStringProperty dstArea  = new SimpleStringProperty("");
        SimpleStringProperty productId  = new SimpleStringProperty("");

        form = Form.of(
                Group.of(
                    Field.ofStringType(dstCity)
                    .label("目的城市")
                    .required("必填")
                    .id("dstCity"),
                    Field.ofStringType(dstArea)
                    .label("目的仓")
                    .required("必填")
                    .id("dstArea"),
                    Field.ofStringType(productId)
                    .label("产品ID")
                    .required("必填")
                    .validate(NumbersValidator.justNumber("只能是数字"))
                    .validate(StringLengthValidator.exactly(5,"5位数字"))
                    .id("productId"),
                    Field.ofDate(selectedDate)
                    .id("createDate")
                    .label("日期")
                    )
                );
        return form;

    }

    @Override
    public String getTitle(){
        return "pallet";
    }

}

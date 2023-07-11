package com.dlsc.workbenchfx.demo.modules.shipping.view;

import com.cg.core.module.BizException;
import com.dlsc.workbenchfx.demo.modules.user.model.UserInfo;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.cg.core.gui.FormUtils;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;
import com.dlsc.workbenchfx.demo.modules.palletSN.model.PalletSN;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;
import com.dlsc.workbenchfx.demo.modules.query.model.ProductDetailInfo;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.dlsc.workbenchfx.demo.LettersOrNumbersValidator;
import com.dlsc.workbenchfx.demo.LocalCache;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.api.CartoonBoxAPI;
import com.dlsc.workbenchfx.demo.api.CommonAPI;
import com.dlsc.workbenchfx.demo.api.PalletAPI;
import com.dlsc.workbenchfx.demo.api.ShippingAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.shipping.model.Shipping;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import java.util.*;
import com.dlsc.formsfx.model.structure.StringField;




public class ShippingView extends BorderPane {
    List<String> headerName;
    List<String> headerId;
    public TableView<ProductDetailInfo> table;
    private GridPane controls;
    private VBox ImportExport;
    private Button buttonExport;
    //private Button buttonImport;
    private VBox bindingInfo;
    private Label countryLabel;
    private Pagination pagination;
    public ScrollPane scrollContent;

	public StringProperty factoryCode;// = new SimpleStringProperty("");
	public StringProperty productNumber;// = new SimpleStringProperty("");
	public ObjectProperty<LocalDate> selectedDateStart;//  = new SimpleObjectProperty<LocalDate>(LocalDate.now());
	public ObjectProperty<LocalDate> selectedDateEnd;//  = new SimpleObjectProperty<LocalDate>(LocalDate.now());
    Button buttonRefresh;
    public Integer count = 0;

	public StringProperty productPartNumber;
	public StringProperty productName;
	public StringProperty productType;
	public StringProperty productColor;

    public static String[] day = {"1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V"};
    public static String[] month = {"E","F","G","H","I","J","K","L","M","N","O","P"};
    public static String[] year = {"M","N","O","P","Q","R","S","T","U","V","W"};

    public static Integer pageCount=1;
    public static String partSNCode;    
    public StringField factoryCodeField;
    public String productSNs = null;   
    public ListView<String> listView ;//= new ListView<>();
    public Label hint;

    boolean firstOpen;
    TextField txtShippingCode=null;
    Button buttonSNCode = new Button("开始出货");
    /**
     * 下拉框:栈板/中箱
     */
    private ChoiceBox<String> cbProductSN;

    ToggleGroup group;
    Workbench workbench;

    public ShippingView(String creator) {
        
    }

    public void init(Workbench workbench){
        this.workbench = workbench;
        firstOpen = true;

        Log.info("出货！");

        getStyleClass().add("module-background");
        getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        getStyleClass().add("root-pane");

        //BorderPane scrollContent = new BorderPane();
        ScrollPane scrollContent = new ScrollPane();
        scrollContent.getStyleClass().add("scroll-pane");
        scrollContent.getStyleClass().add("bordered");


        setCenter(scrollContent);


        GridPane gridPane = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth( 20 );
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth( 80 );

        gridPane.getColumnConstraints().addAll( col1, col2);
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        //gridPane.setStyle("-fx-padding: 20 20 20 20;");

        VBox vInput = new VBox();
        vInput.setStyle("-fx-padding: 20 20 0 20;");
        gridPane.setStyle("-fx-padding: 20 20 20 20;");
        gridPane.getStyleClass().add("bordered_all");
        vInput.getChildren().add(gridPane);
        String st[] = { "栈板", "中箱" };
        cbProductSN = new ChoiceBox<>(FXCollections.observableArrayList(st));
        cbProductSN.setPrefWidth(100);
        cbProductSN.setValue("栈板");
        TextField txtProductSN = new TextField();
        txtProductSN.setEditable(false);
        Label lbShippingCode = new Label("出货单号");
        txtShippingCode = new TextField();

        hint = new Label();
        hint.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        hint.setTextFill(Color.RED);
        

        Label lbShippingDate = new Label("出货日期");
        DatePicker dpShippingDate = new DatePicker();
        dpShippingDate.setValue(LocalDate.now());
        dpShippingDate.setStyle("-fx-max-width:Infinity;");

        gridPane.add(cbProductSN, 0, 2);
        gridPane.add(txtProductSN, 1, 2);

        listView = new ListView<>();
        gridPane.add(listView,1,4);
        gridPane.add(hint,1,3);
        listView.setPrefHeight(100);
        MenuItem delItem = new MenuItem("删除");
        ContextMenu contextMenu = new ContextMenu(delItem);
        listView.setContextMenu(contextMenu);
        delItem.setOnAction(e->{
            int index = listView.getSelectionModel().getSelectedIndex();
            listView.getItems().remove(index);
        });

        gridPane.add(lbShippingCode, 0, 0);
        gridPane.add(txtShippingCode, 1, 0);
        gridPane.add(lbShippingDate, 0, 1);
        gridPane.add(dpShippingDate, 1, 1);       


        String version = LocalCache.getCache().getClientType();

        selectedDateStart  = new SimpleObjectProperty<LocalDate>(LocalDate.now());
        selectedDateEnd  = new SimpleObjectProperty<LocalDate>(LocalDate.now());

        if(version.equals("sushi")){
            headerName = Arrays.asList("物料编号","型号","产品SN","中箱SN","卡板SN","生产日期","出货日期","出货单号");
            headerId = Arrays.asList("productPartNumber","productType","SNCode","cartoonBoxCode","palletCode","createDate","shippingDate","shippingCode");
            table = MainUtils.createTable(headerName,headerId);
        }
        if(version.equals("xiaomi")){
            headerName = Arrays.asList("SKU","产品型号","产品SN码","中箱SN码","栈板码","创建日期","出货日期","出货单号");
            headerId = Arrays.asList("SKU","productType","SNCode","cartoonBoxCode","palletCode","createDate","shippingDate","shippingCode");
            table = MainUtils.createTable(headerName,headerId);
        }


        VBox root = new VBox();

        VBox tableBox = new VBox();
        //tableBox.prefHeight(900);
        tableBox.setStyle("-fx-padding: 20 20 0 20;");
        root.getStyleClass().add("bordered");

        Integer count;

        try{
            count = getShippingCount();
        }catch(RequestException e){
            MainUtils.showSimpleError("网络异常");
            count =0;
        }
        pagination = new Pagination(MainUtils.getPageCount(table, count), 0);


        VBox.setVgrow(pagination, Priority.ALWAYS );
        tableBox.getChildren().add(pagination);


        pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
        //tableBox.getChildren().add(table);

        root.getChildren().addAll(vInput,tableBox);
        VBox.setVgrow(tableBox, Priority.ALWAYS );
        VBox.setVgrow(root, Priority.ALWAYS );

        //scrollContent.setCenter(root);
        scrollContent.setFitToWidth(true);
        scrollContent.setContent(root);
        controls = new GridPane();

        countryLabel = new Label("帮助电话: "+MainUtils.helpPhone);
        bindingInfo = new VBox();
        bindingInfo.setPadding(new Insets(10));
        bindingInfo.getChildren().addAll(countryLabel);
        bindingInfo.setSpacing(10);
        bindingInfo.setPrefWidth(200);
        bindingInfo.getStyleClass().add("bordered");
        controls.add(bindingInfo, 0, 0);


        VBox snBox = new VBox();

        buttonSNCode.setMaxWidth(Double.MAX_VALUE);
        buttonSNCode.getStyleClass().add("save-button");

        buttonSNCode.setOnAction(event -> {
                if(buttonSNCode.getText().equals("开始出货")){
                    buttonSNCode.setText("结束出货");
                    txtProductSN.setEditable(true);
                    txtProductSN.setDisable(false);
                    txtShippingCode.setEditable(false);
                    txtShippingCode.setDisable(true);
                    dpShippingDate.setEditable(false);
                    dpShippingDate.setDisable(true);
                }else{
                    buttonSNCode.setText("开始出货");
                    txtProductSN.setEditable(false);
                    txtProductSN.setDisable(true);
                    txtShippingCode.setEditable(true);
                    txtShippingCode.setDisable(false);
                    dpShippingDate.setEditable(true);
                    dpShippingDate.setDisable(false);
                    txtProductSN.clear();
                    listView.getItems().clear();
                    MainUtils.showSimpleSuccess("结束出货");
                }
        });



        //Button buttonEmpty = new Button("清空输入");
        //buttonEmpty.setMaxWidth(Double.MAX_VALUE);
        //HBox.setHgrow(buttonEmpty, Priority.ALWAYS);


        buttonRefresh = new Button("查询");
        buttonRefresh.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buttonRefresh, Priority.ALWAYS);
        //
        Button btnLabelDel = new Button("作废单号");
        btnLabelDel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnLabelDel, Priority.ALWAYS);

        Button btnLabelCancel = new Button("撤销出货");
        btnLabelCancel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnLabelCancel, Priority.ALWAYS);


        snBox.setSpacing(10);
        snBox.setPrefWidth(200);
        snBox.getStyleClass().add("bordered");
        snBox.setPadding(new Insets(10));
        snBox.getChildren().addAll(buttonSNCode,btnLabelCancel,buttonRefresh);
        controls.add(snBox, 0, 1);


        ImportExport = new VBox();
        group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("        导出当前页");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        ChoiceBox choiceExportType = new ChoiceBox();
        choiceExportType.getStyleClass().add("choice-box");
        String[] exportType = {"按单号导出","按栈板号导出","按箱号导出", "按日期导出"};
        for(String type:exportType){
            choiceExportType.getItems().add(type);
        }
        choiceExportType.setPrefWidth(200);
        choiceExportType.setValue("按单号导出");


        RadioButton rb2 = new RadioButton("        导出全部");
        rb2.setToggleGroup(group);

        buttonExport = new Button("导出Excel");
        buttonExport.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buttonExport, Priority.ALWAYS);

        ImportExport.setPadding(new Insets(10));
        ImportExport.getChildren().addAll(choiceExportType,buttonExport);
        ImportExport.setSpacing(10);
        ImportExport.setPrefWidth(200);
        ImportExport.getStyleClass().add("bordered");
        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        controls.add(ImportExport, 0, 2);

        setLeft(controls);
        buttonSNCode.disableProperty().bind(Bindings.isEmpty(txtShippingCode.textProperty()));

        buttonExport.setOnAction(event -> {
            String value = (String)choiceExportType.getSelectionModel().getSelectedItem();
            Form form = createFormBySelected(value);

            ButtonType okay = MainUtils.okayButton();
            ButtonType cancel = MainUtils.cancelButton();
            FormRenderer renderer = new FormRenderer(form);
            MainUtils.searchAndSetControlsLabelWidth(renderer, 35);
            WorkbenchDialog dialog = WorkbenchDialog.builder(
                    "导出数据", renderer, okay, cancel)
                .blocking(true)
                //.maximized(true)
                .showButtonsBar(true)
                .onResult(buttonType -> {
                    if (buttonType.equals(okay)) {
                        form.persist();
                        String text = FormUtils.form2JsonStr(form);
                        ExportField exportField = JSON.parseObject(text, ExportField.class);
                        
                        Stage stage = new Stage();
                        String laserSql=null;
                        try{
                            if (value.equals("按单号导出")){
                                String shippingCode = exportField.shippingCode;
                                laserSql = "from product_laser as s where 1 and s.shipping_code = '%s'";
                                laserSql = String.format(laserSql,shippingCode);
                            }else if(value.equals("按栈板号导出")){
                                String palletCode = exportField.palletCode;
                                laserSql = "from product_laser as s where 1 and s.cartoon_box_code in (select nick_name from cartoon_box as c where 1 and c.pallet_code = '%s') and s.shipping_code is not null and s.shipping_code <> ''";
                                laserSql = String.format(laserSql,palletCode);
                            }else if(value.equals("按箱号导出")){
                                String boxCode = exportField.boxCode;
                                laserSql = "from product_laser as s where 1 and s.cartoon_box_code = '%s' and s.shipping_code is not null and s.shipping_code <> ''";
                                laserSql = String.format(laserSql,boxCode);
                            }else if(value.equals("按日期导出")){
                                String dateEnd = exportField.dateEnd;
                                String dateStart = exportField.dateStart;
                                dateEnd = dateEnd.split(" ")[0];
                                dateStart = dateStart.split(" ")[0];
                                laserSql = "from product_laser as s where 1 and s.create_date>='%s 00:00:00' and s.create_date<='%s 23:59:59' and s.shipping_code is not null and s.shipping_code <> ''";
                                laserSql = String.format(laserSql,dateStart,dateEnd);
                            }
                        }catch(Exception e){
                            Log.error("",e);
                            MainUtils.showSimpleError("导出失败:"+e.getMessage());
                            return;
                        }
                        MainUtils.exportAll(laserSql,stage,headerName,headerId);
                    }
                })
                .build();
                //dialog.setOnShown(event1 -> {
                //    dialog.getButton(okay).ifPresent(
                //            button -> {
                //                button.disableProperty().bind(form.persistableProperty().not());
                //            });
                //});
                workbench.showDialog(dialog);


        });

        buttonRefresh.setOnAction(event -> {
            refreshTable(0); 
        });

        //buttonEmpty.setOnAction(event -> {
        //    txtProductSN.setText("");
        //    txtShippingCode.setText("");
        //    listView.getItems().clear();
        //    txtPrudctSN.setEditable(false);
        //});

        txtProductSN.setOnKeyPressed(e->{ // 栈板或者中箱输入信息
            if(e.getCode() == KeyCode.ENTER){
                String snCode = txtProductSN.getText(); //其实这个sncode是nickname
                if(!snCode.equals("")){
                    txtProductSN.clear();
                    String lbl = (String)cbProductSN.getSelectionModel().getSelectedItem();
                    if(lbl.equals("栈板")){
                        PalletSN pallet = null;
                        try{
                            pallet = PalletAPI.getPalletByNickName(snCode);
                        }catch(RequestException _e){
                            String text = snCode + ":网络异常";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }
                        if(pallet==null) {
                            String text = snCode + ":非法栈板";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }
                        if (!validPallet(pallet)){
                            return;
                        }
                        Shipping shipping = null;
                        try{
                            shipping = getShippingByPallet(snCode);
                        }catch(RequestException _e){
                            Log.error("",_e);
                            String text = snCode + ":网络异常";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }

                        //if (shipping ==null){
                        //    String text = snCode + ":出货失败";
                        //    hint.setText(text);
                        //    MainUtils.showSimpleError(text);
                        //    return;
                        //}

                        if(shipping != null && (shipping.getShippingCode() != null) && (!shipping.getShippingCode().isEmpty())){
                            String text = snCode + ":已出货";
                            hint.setText(text);
                            MainUtils.showSimpleSuccess(text);
                            return;
                        }

                        String shippingCode = txtShippingCode.getText();
                        String shippingDate = dpShippingDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        int num;
                        try{
                            num = PalletAPI.bindShip(shippingCode, shippingDate, pallet);
                        }catch(RequestException _e){
                            Log.error("",_e);
                            String text = snCode + ":网络异常";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }
                        if(num==0){
                            String text = snCode + ":出货失败";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }
                    }else{
                        CartoonBoxSN box = null;
                        try{
                            box = CartoonBoxAPI.byNickName(snCode);
                        }catch(RequestException _e){
                            String text = snCode + ":网络异常";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }
                        if(box==null) {
                            String text = snCode + ":非法中箱码";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }
                        if (!validBox(box)){
                            return;
                        }
                        Shipping shipping = null;
                        try{
                            shipping = getShippingByBox(snCode);
                        }catch(RequestException _e){
                            Log.error("",_e);
                            String text = snCode + ":网络异常";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }

                        //if (shipping ==null){
                        //    String text = snCode + ":出货失败";
                        //    hint.setText(text);
                        //    MainUtils.showSimpleError(text);
                        //    return;
                        //}

                        if(shipping !=null && (shipping.getShippingCode() != null) && (!shipping.getShippingCode().isEmpty())){
                            String text = snCode + ":已出货";
                            hint.setText(text);
                            MainUtils.showSimpleSuccess(text);
                            return;
                        }

                        String shippingCode = txtShippingCode.getText();
                        String shippingDate = dpShippingDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        int num;
                        try{
                            num = CartoonBoxAPI.bindShip(shippingCode, shippingDate, box);
                        }catch(RequestException _e){
                            Log.error("",_e);
                            String text = snCode + ":网络异常";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }
                        if(num==0){
                            String text = snCode + ":出货失败";
                            hint.setText(text);
                            MainUtils.showSimpleError(text);
                            return;
                        }

                    }


                    if(!listView.getItems().contains(snCode)){
                        listView.getItems().add(0,snCode);
                        txtProductSN.clear();
                        hint.setText("");
                    }else{
                        String text = snCode + ":重复SN";
                        hint.setText(text);
                        MainUtils.showSimpleError(text);
                    }
                }
            }

        });

        btnLabelDel.setOnAction(e->{
            delLabel();
        });

        btnLabelCancel.setOnAction(e->{
            cancelOrder();
        });


    }

    void cancelOrder(){

        StringProperty itemCode = new SimpleStringProperty("");
        StringProperty userName = new SimpleStringProperty("");
        StringProperty pwd = new SimpleStringProperty("");
        ObservableList<String> cancelType = null;
        cancelType = FXCollections.observableArrayList(Arrays.asList("中箱","栈板"));
        SingleSelectionField<String> cancelTypeField = Field.ofSingleSelectionType(cancelType, 0);

        Form form = Form.of(
                Group.of(
                    cancelTypeField
                    .label("撤销类型"),
                    Field.ofStringType(itemCode)
                    .id("container")
                    .label("中箱/栈板码")
                    .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                    .required("必填"),
                    Field.ofStringType(userName)
                    .id("userName")
                    .label("工程用户")
                    .required("必填"),
                    Field.ofStringType(pwd)
                    .id("passwd")
                    .label("密码")
                    .required("必填")

                    ));
        //form.binding(BindingMode.CONTINUOUS);
        FormRenderer renderer = new FormRenderer(form);
        MainUtils.searchAndSetControlsLabelWidth(renderer, 40);
        ButtonType okayButton = MainUtils.okayButton();
        ButtonType cancelButton = MainUtils.cancelButton();
        WorkbenchDialog dialog = WorkbenchDialog.builder(
                "撤销出货", renderer, okayButton, cancelButton)
            .showButtonsBar(true)
            .onResult(buttonType -> {
                if (buttonType.equals(okayButton)) {
                    form.persist();
                    if(!userVerify(userName.getValue(), pwd.getValue())){
                        return;
                    }
                    
                    Shipping shipping;
                    String nickName = itemCode.getValue();
                    try{
                        if(cancelTypeField.getSelection().equals("中箱")){
                            CartoonBoxSN box = CartoonBoxAPI.byNickName(nickName);
                            if (box==null){
                                MainUtils.showSimpleError("中箱号不存在:"+nickName);
                                return;
                            }
                            shipping = ShippingAPI.byBox(nickName);
                            if(shipping==null || shipping.getShippingCode().isEmpty()){
                                MainUtils.showSimpleError("中箱号还没出货:"+nickName);
                                return;
                            }
                            int count = ProductLaserAPI.clearShipCodeByBoxNickName(nickName);
                            if(count==0){
                                MainUtils.showSimpleError("中箱号解除出货失败:"+nickName);
                                return;
                            }
                            MainUtils.showSimpleError("中箱号解除出货成功:"+nickName);

                        }else{
                            PalletSN pallet = PalletAPI.getPalletByNickName(nickName);
                            if (pallet==null){
                                MainUtils.showSimpleError("栈板号不存在:"+nickName);
                                return;
                            }
                            shipping = ShippingAPI.byPallet(nickName);
                            if(shipping==null || shipping.getShippingCode().isEmpty()){
                                MainUtils.showSimpleError("栈板号还没出货:"+nickName);
                                return;
                            }
                            int count = ProductLaserAPI.clearShipCodeByPalletNickName(nickName);
                            if(count==0){
                                MainUtils.showSimpleError("栈板号解除出货失败:"+nickName);
                                return;
                            }
                            MainUtils.showSimpleError("栈板号解除出货成功:"+nickName);
                        }
                    }catch(RequestException e){
                        Log.error("",e);
                        MainUtils.showSimpleError("网络异常:"+e.getMessage());
                        return;
                    }
                }
            })
        .build();
        dialog.setOnShown(event1 -> {
            dialog.getButton(okayButton).ifPresent(
                    button -> {
                        button.disableProperty().bind(form.persistableProperty().not());
                    });
        });
        workbench.showDialog(dialog);

    }


    void delLabel(){

        StringProperty shippingCode = new SimpleStringProperty("");
        StringProperty userName = new SimpleStringProperty("");
        StringProperty pwd = new SimpleStringProperty("");
        Form form = Form.of(
                Group.of(
                    Field.ofStringType(shippingCode)
                    .id("container")
                    .label("出货单号")
                    .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                    .required("必填"),
                    Field.ofStringType(userName)
                    .id("userName")
                    .label("工程用户")
                    .required("必填"),
                    Field.ofStringType(pwd)
                    .id("passwd")
                    .label("密码")
                    .required("必填")

                    ));
        //form.binding(BindingMode.CONTINUOUS);
        FormRenderer renderer = new FormRenderer(form);
        MainUtils.searchAndSetControlsLabelWidth(renderer, 35);
        ButtonType okayButton = MainUtils.okayButton();
        ButtonType cancelButton = MainUtils.cancelButton();
        WorkbenchDialog dialog = WorkbenchDialog.builder(
                "作废标签", renderer, okayButton, cancelButton)
            .showButtonsBar(true)
            .onResult(buttonType -> {
                if (buttonType.equals(okayButton)) {
                    form.persist();
                    if(!userVerify(userName.getValue(), pwd.getValue())){
                        return;
                    }

                    List<ProductLaser> lasers;
                    try{
                    lasers = getLaserByShippingCode(shippingCode.getValue());
                    }catch(RequestException _e){
                        MainUtils.showSimpleError("网络异常");
                        Log.error("",_e);
                        return;
                    }

                    if(lasers==null || lasers.isEmpty()){
                        MainUtils.showSimpleError("单号不存在");
                        return ;
                    }

                    boolean op = false;
                    try{
                        op = clearShippingCode(shippingCode.getValue());
                    }catch(RequestException _e){
                        MainUtils.showSimpleError("网络异常");
                        Log.error("",_e);
                        return;
                    }


                    if(!op){
                        MainUtils.showSimpleSuccess("删除出货单号失败!");  
                        return;
                    }
                    MainUtils.showSimpleSuccess("删除出货单号成功!");  
                }
            })
        .build();
        dialog.setOnShown(event1 -> {
            dialog.getButton(okayButton).ifPresent(
                    button -> {
                        button.disableProperty().bind(form.persistableProperty().not());
                    });
        });
        workbench.showDialog(dialog);

    }

    boolean userVerify(String userName, String pwd){
        try{
            UserInfo user = GlobalConfig.getInstance().findUser(userName, pwd);
            if (!user.getMygroup().equals("工程单位")){
                MainUtils.showSimpleError("非工程单位用户");
                return false;
            }
            return true;
        }catch(BizException e){
            MainUtils.showSimpleError("用户不存在");
            return false;
        }
    }


    public Node createPage(int pageIndex) {
        //Integer pageSize = MainUtils.getPageSize(table);
        //ObservableList<Shipping> list = getShippings(pageIndex,pageSize);
        //table.setItems(list);
        if (firstOpen){
            firstOpen = false;
            return new VBox(table);
        }
        refreshTable(pageIndex);
        return new VBox(table);
    }


    public Integer getPageCount(Integer count){
        return MainUtils.getPageCount(table, count);
    }

    public void refreshTable(int pageIndex){
        //刷新表格: 重新计算页数,定位到第一页。
        String shippingCode = txtShippingCode.getText();
        if(shippingCode==null||shippingCode.isEmpty()){
            MainUtils.showSimpleError("请先填出货单号");
            return;
        }
        String sql = "from product_laser as s where 1 and s.shipping_code='%s'";
        sql = String.format(sql, txtShippingCode.getText());
        int pageSize = MainUtils.getPageSize(table);
        //int pageIndex = 0;
        int offset = pageSize * pageIndex;
        List<ProductDetailInfo> list;
        int count;
        try{
            list= CommonAPI.getProductDetail(sql, pageSize, offset);
            count = CommonAPI.getProductDetailCount(sql);
        }catch(BizException e){
            Log.error("",e);
            MainUtils.showSimpleError("异常:"+e.getMessage());
            return;
        }
        pagination.setPageCount(getPageCount(count));
        pagination.setCurrentPageIndex(0);
        table.getItems().clear();
        table.getItems().addAll(list);
    }
    /**
     * 
     * 以下部分需要对接后端接口!
     * 
    */
    public static ObservableList<Shipping> getShippings(Integer pageIndex, Integer pageSize){
        String uri = String.format("/productLaser/getShippings?pageIndex=%d&pageSize=%d",pageIndex,pageSize);
        Log.debug(uri);
        String jsonStr = MainUtils.post("{}",GlobalConfig.getInstance().getServerUrl()+uri);

        Log.debug(jsonStr);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Integer code = jsonObject.getInteger("code");
        List<Shipping> shippings = new ArrayList<Shipping>();
        if (code==-1){
            return FXCollections.observableArrayList(shippings);
        }
        pageCount = jsonObject.getJSONObject("data").getInteger("pageCount");
        JSONArray array = jsonObject.getJSONObject("data").getJSONArray("content");
        for (int i=0; i<array.size(); i++){
            Shipping shipping = JSON.parseObject(array.getString(i), Shipping.class);
            shippings.add(shipping);
        }
        return FXCollections.observableArrayList(shippings);
    }
    Shipping getShippingByBox(String boxSN) throws RequestException{
        Shipping shipping;
        shipping = ShippingAPI.byBox(boxSN);
        return shipping;
    }
    Shipping getShippingByPallet(String palletSN) throws RequestException{
        Shipping shipping;
        shipping = ShippingAPI.byPallet(palletSN);
        return shipping;
    }

    List<ProductLaser> getLaserByShippingCode(String shippingCode) throws RequestException{
       ProductLaser laser = new ProductLaser(); 
       laser.setShippingCode(shippingCode);
       List<ProductLaser> list;
       list = ProductLaserAPI.getProductLasers(laser);
       return list;
    }

    boolean clearShippingCode(String shipCode) throws RequestException{
        int num = ProductLaserAPI.clearShipCode(shipCode);
        if (num==0) return false;
        return true;
    }




    public Integer getShippingCount() throws RequestException{
        // 根据部分SNCode查询总记录数
        Integer count = ProductLaserAPI.getShippingCount();
        return count;
    }
    public Form createFormBySelected(String version){
        Form form = null;
        if(version.equals("按单号导出")){
            form = Form.of(
                    Group.of(
                        Field.ofStringType("")
                        .id("shippingCode")
                        .label("单号")
                        .required("必填")
                        )
                    );
        }
        if(version.equals("按栈板号导出")){
            form = Form.of(
                    Group.of(
                        Field.ofStringType("")
                        .id("palletCode")
                        .label("栈板号")
                        .required("必填")
                        )
                    );
        }
        if(version.equals("按箱号导出")){
            form = Form.of(
                    Group.of(
                        Field.ofStringType("")
                        .id("boxCode")
                        .label("中箱号")
                        .required("必填")
                        )
                    );
        }
        if(version.equals("按日期导出")){
            form = Form.of(
                    Group.of(
                        Field.ofDate(selectedDateStart)
                        .label("开始日期")
                        .id("dateStart")
                        .required("必填"),
                        Field.ofDate(selectedDateEnd)
                        .label("结束日期")
                        .id("dateEnd")
                        .required("必填")
                        )
                    );
        }


        return form;
    }

    class ExportField{
        public String shippingCode;
        public String palletCode;
        public String boxCode;
        public String dateStart;
        public String dateEnd;
    }

    boolean validBox(CartoonBoxSN box){

        List<ProductLaser> products;
        try{
            products = ProductLaserAPI.getByBoxCode(box.getNickName());
        }catch(RequestException _e){
            String text = box.getNickName() + ":网络异常";
            hint.setText(text);
            MainUtils.showSimpleError(text);
            return false;
        }
        if(products==null || products.isEmpty()){
            String text = box.getNickName() + ":空箱子";
            hint.setText(text);
            MainUtils.showSimpleError(text);
            return false;
        }

        String palletCode = box.getPalletCode();
        if(palletCode!=null && !palletCode.isEmpty()){
            String text = box.getNickName() + ":已经装入栈板,禁止出货";
            hint.setText(text);
            MainUtils.showSimpleError(text);
            return false;
        }
        return true;

    }

    public boolean validPallet(PalletSN pallet){

        List<CartoonBoxSN> boxs;
        try{
            boxs = CartoonBoxAPI.getCartoonBoxByPallet(pallet.getNickName());
        }catch(RequestException _e){
            String text = pallet.getNickName()+":网络异常";
            hint.setText(text);
            MainUtils.showSimpleError(text);
            return false;
        }
        if(boxs==null || boxs.isEmpty()){
            String text = pallet.getNickName()+":空栈板";
            hint.setText(text);
            MainUtils.showSimpleError(text);
            return false;
        }

        return true;

    }



}

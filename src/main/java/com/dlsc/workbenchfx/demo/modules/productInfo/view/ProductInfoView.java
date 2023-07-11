package com.dlsc.workbenchfx.demo.modules.productInfo.view;


import java.util.Arrays;
import java.util.List;
import com.alibaba.fastjson2.JSON;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.Code69Validator;
import com.dlsc.workbenchfx.demo.LettersOrNumbersValidator;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.ModelUtil;
import com.dlsc.workbenchfx.demo.NumbersValidator;
import com.dlsc.workbenchfx.demo.api.DictionaryAPI;
import com.dlsc.workbenchfx.demo.api.ServerAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfoAPI;
import com.dlsc.workbenchfx.demo.zplTemplate.ClazzUtils;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.cg.core.gui.FormUtils;
import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;


public class ProductInfoView extends BorderPane {
	private final GlobalConfig globalConfig=GlobalConfig.getInstance();
	/**
	 * 左侧面板
	 */
    private GridPane controls;
    /**
     * 左侧操作按钮容器
     */
    private VBox crudContent;
    /**
     * 导出Excel和刷新按钮Wrapper
     */
    private VBox ImportExport;
    /**
     * 导出Excel按钮
     */
    private Button buttonExport;   
    /**
     * 增加模板按钮
     */
    private Button buttonAdd;
    /**
     * 删除模板按钮
     */
    private Button buttonDel;
    /**
     * 修改模板按钮
     */
    private Button buttonUpdate;
    /**
     * 帮助电话Wrapper
     */
    private VBox bindingInfo;
    /**
     * 帮助电话标签
     */
    private Label countryLabel;
    public Pagination pagination;
    private Stage primaryStage;
    public ObservableList<ProductInfo> productInfoList;   
    public String creator;
    /**
     * 右侧产品模板数据表格
     */
    public TableView<ProductInfo> table=null;
    /**
     * 客户端类型
     */
    private String version;    
    //public static List<String> colorCNList = new ArrayList<String>(Arrays.asList("白色","棕色","绿色","黄色","灰色","粉红色","蓝色","红色","黑色","紫色","银色"));



    public ProductInfoView(String creator) {
        this.creator = creator;
    }
    /**
     * 刷新表格列表
     */
    public void refreshTableView() {    	
    	if(pagination!=null) {
    		pagination.setPageCount(MainUtils.getPageCount(table, globalConfig.getAllProductInfos().size()));
    		pagination.setCurrentPageIndex(0);
    	}
    	if(table!=null)table.refresh();
    }
    public void init(Workbench workbench){
        controls = new GridPane();

        countryLabel = new Label("帮助电话: "+MainUtils.helpPhone);
        bindingInfo = new VBox();
        bindingInfo.setPadding(new Insets(10));
        bindingInfo.getChildren().addAll(countryLabel);
        bindingInfo.setSpacing(10);
        bindingInfo.setPrefWidth(200);
        bindingInfo.getStyleClass().add("bordered");
        controls.add(bindingInfo, 0, 0);
        
        crudContent = new VBox();
        buttonAdd = new Button("增加");
        buttonAdd.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buttonAdd, Priority.ALWAYS);      
        buttonDel = new Button("删除");
        buttonDel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buttonDel, Priority.ALWAYS);
        buttonUpdate = new Button("修改");
        buttonUpdate.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buttonUpdate, Priority.ALWAYS);
        crudContent.setPadding(new Insets(10));
        crudContent.getChildren().addAll(buttonAdd,buttonDel,buttonUpdate);
        crudContent.setSpacing(10);
        crudContent.setPrefWidth(200);
        crudContent.getStyleClass().add("bordered");
        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        controls.add(crudContent, 0, 1);


        ImportExport = new VBox();
        buttonExport = new Button("导出Excel");
        buttonExport.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buttonExport, Priority.ALWAYS);
        //buttonImport.setId("bevel-grey");
       
        ImportExport.setPadding(new Insets(10));
        //ImportExport.getChildren().addAll(buttonExport,buttonImport,buttonRefresh);
        ImportExport.getChildren().addAll(buttonExport);
        ImportExport.setSpacing(10);
        ImportExport.setPrefWidth(200);
        ImportExport.getStyleClass().add("bordered");
        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        controls.add(ImportExport, 0, 2);
        
        VBox vConfig = new VBox();//素士专用操作按钮wrapper
        Button btnColorConfig = new Button("新增颜色编码");
        btnColorConfig.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnColorConfig, Priority.ALWAYS);

        Button btnDateConfig = new Button("新增日期编码");
        btnDateConfig.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnDateConfig, Priority.ALWAYS);


        vConfig.setPadding(new Insets(10));
        vConfig.getChildren().addAll(btnColorConfig);
        vConfig.setSpacing(10);
        vConfig.setPrefWidth(200);
        vConfig.getStyleClass().add("bordered");

        version = globalConfig.getClientType();
        if(version.equals("sushi")){
            controls.add(vConfig, 0, 3);
        }
        try{
            MainUtils.setWorkbench(workbench);

            primaryStage = new Stage();
            Log.info("产品信息管理！");

            getStyleClass().add("module-background");
            getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            getStyleClass().add("root-pane");

            if (version.equals("sushi")){
                table = MainUtils.createTable(Arrays.asList("ID","产品料号","产品名称","产品型号","产品颜色","工厂代码","装箱数量","长(mm)","宽(mm)","高(mm)","净重(kg)","毛重(Kg)"),
                        Arrays.asList("id:i","productPartNumber","productName","productType","productColor","factoryCode","boxQuantity","boxLength","boxWidth","boxHeight","boxNetWeight","boxGrossWeight"));
            }else if(version.equals("xiaomi")){
                table = MainUtils.createTable(Arrays.asList("ID","产品型号","69码","商品 id","SKU","SKU 名称", "产品颜色","打印模板"),
                        Arrays.asList("id:i","productType","code69","productId", "SKU", "productName","productColor","printerTemplate"));
            }
            VBox tableBox = new VBox();
            tableBox.setStyle("-fx-padding: 20 20 0 20;");
            pagination = new Pagination(MainUtils.getPageCount(table, globalConfig.getAllProductInfos().size()), 0);
            //pagination = new Pagination(5, 0);
            VBox.setVgrow(pagination, Priority.ALWAYS );
            tableBox.getChildren().add(pagination);
            pagination.setPageFactory((Integer pageIndex) -> {
                return createPage(table,pageIndex);
            });
            setCenter(tableBox);
            setLeft(controls);
        }catch(Exception e){
            Log.warn("",e);
            getStyleClass().add("module-background");
            Label hint = new Label("服务器断开连接,请关闭本页面再打开");
            hint.setFont(Font.font("Tahoma",FontWeight.NORMAL,40));
            hint.setTextFill(Color.RED);
            setCenter(hint);
            controls.getChildren().clear();
            setLeft(controls);
        }
        //


        //this.workbench = workbench;
        ButtonType okay = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);//新增模板确认
        ButtonType cancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);//新增模板取消


        buttonAdd.setOnAction(event -> {

            Form form = createFormByVersion(version);

            FormRenderer renderer = new FormRenderer(form);
            MainUtils.searchAndSetControlsLabelWidth(renderer, 40);

            // Building the dialog with the CheckBox as content
            WorkbenchDialog dialog = WorkbenchDialog.builder(
                    "新增产品", renderer, okay, cancel)
                .blocking(true)
                //.maximized(true)
                .showButtonsBar(true)
                .onResult(buttonType -> {
                    if (buttonType.equals(okay)) {
                        form.persist();                        
                        String text = FormUtils.form2JsonStr(form);
                        ProductInfo productInfo = JSON.parseObject(text, ProductInfo.class);
                        if(!isUnique(table.getItems(),productInfo,version)){
                            if(version.equals("sushi")){
                                MainUtils.showSimpleError("产品料号重复!");
                            }else{
                                MainUtils.showSimpleError("产品id重复!");
                            }
                            return;
                        }
                    	try {
							List<ProductInfo> rsList=ProductInfoAPI.addAll(List.of(productInfo));
							globalConfig.addProductInfos(rsList);
							refreshTableView();
						} catch (RequestException e) {
							MainUtils.showSimpleError(ServerAPI.toBizException(null, e).getMessage());
							return;
						} catch (BizException e) {
							MainUtils.showSimpleError(e.getMessage());
							return;
						}                        
                    }
                })
                .build();
                dialog.setOnShown(event1 -> {
                    dialog.getButton(okay).ifPresent(
                            button -> {
                                button.disableProperty().bind(form.persistableProperty().not());
                            });
                });
                workbench.showDialog(dialog);
        });

        buttonDel.setOnAction(event -> {
            ProductInfo item = table.getSelectionModel().getSelectedItem();
            if (item == null){
                WorkbenchDialog dialog = WorkbenchDialog.builder(
                        "删除产品失败", "请您先选择需要删除的产品", okay)
                    .showButtonsBar(true)
                    .onResult(buttonType -> {
                    })
                .build();
                workbench.showDialog(dialog);
            }else{
                String delMessage = "";
                if (version.equals("sushi")){
                    delMessage = "产品料号: "+item.getProductPartNumber()+"\n产品名称: "+item.getProductName()+"\n产品型号: "+item.getProductType()+"\n产品颜色: "+item.getProductColor();
                }
                if(version.equals("xiaomi")){
                    delMessage = "产品型号: "+item.getProductType()+"\n69码: "+item.getCode69()+"\n商品id: "+item.getProductId()+"\nSKU: "+item.getSKU()+"\nSKU名称: "+item.getProductName()+
                        "\n产品颜色: "+item.getProductColor();
                }
                WorkbenchDialog dialog = WorkbenchDialog.builder(
                        "删除产品", delMessage, okay, cancel)
                    .showButtonsBar(true)
                    .onResult(buttonType -> {
                        if (buttonType.equals(okay)) {
                            ProductInfo productInfo = table.getSelectionModel().getSelectedItem();
                            try {
	                            if(ProductInfoAPI.delOne(productInfo.getId())) {
	                            	globalConfig.delProductInfo(productInfo.getId());
									refreshTableView();
	                            }else {
	                            	MainUtils.showSimpleError("删除失败");
									return;
	                            }
                            }catch (BizException e) {
                            	MainUtils.showSimpleError(e.getMessage());
								return;
							}
                        }
                    })
                .build();
                workbench.showDialog(dialog);
            }
        });

        buttonUpdate.setOnAction(event -> {
            // Building the dialog with the CheckBox as content
            ProductInfo item = table.getSelectionModel().getSelectedItem();
            if (item == null){
                WorkbenchDialog dialog = WorkbenchDialog.builder(
                        "更新产品失败", "请您先选择需要更新的产品", okay)
                    .showButtonsBar(true)
                    .onResult(buttonType -> {
                    })
                .build();
                workbench.showDialog(dialog);
            }else{

                Form formUpdate = updateFormByVersion(item,version);
                FormRenderer rendererUpdate = new FormRenderer(formUpdate);
                MainUtils.searchAndSetControlsLabelWidth(rendererUpdate, 35);

                WorkbenchDialog dialog = WorkbenchDialog.builder(
                        "修改产品", rendererUpdate, okay, cancel)
                    .showButtonsBar(true)
                    .onResult(buttonType -> {
                        if (buttonType.equals(okay)) {
                            formUpdate.persist();                           
                            //String text = MainUtils.form2JsonStr(formUpdate, dict);
                            String text = FormUtils.form2JsonStr(formUpdate);
                            ProductInfo productInfo = JSON.parseObject(text, ProductInfo.class);
                            productInfo.setId(item.getId());
                            //if(!isUnique(table.getItems(),productInfo,version)){
                            //    if(version.equals("sushi")){
                            //        MainUtils.showSimpleError("产品料号重复!");
                            //    }else{
                            //        MainUtils.showSimpleError("产品料号重复!");
                            //    }
                            //    return;
                            //}
                            ProductInfo rs=null;
							try {
								rs = ProductInfoAPI.update(productInfo);
							} catch (BizException e) {
								MainUtils.showSimpleError(e.getMessage());
								return;
							} catch (RequestException e) {
								MainUtils.showSimpleError(ServerAPI.toBizException(null, e).getMessage());
								return;
							}
                            if(rs==null) {
                            	Log.warn("update:"+productInfo+" return null");
                            }else {
                            	ProductInfo info=globalConfig.getProductInfo(item.getId());  
                            	ModelUtil.copyModel(rs, info);//刷新列表
                            	refreshTableView();
                            }                            
                        }
                    })
                .build();
                dialog.setOnShown(event1 -> {
                    dialog.getButton(okay).ifPresent(
                            button -> {
                                button.disableProperty().bind(formUpdate.validProperty().not());
                            });
                });
                workbench.showDialog(dialog);
            }
        });

        buttonExport.setOnAction(event -> {
            //Log.info(primaryStage);
            MainUtils.exportExcel(primaryStage, table);
        });

        btnColorConfig.setOnAction(event -> {
            StringProperty colorName = new SimpleStringProperty("");
            StringProperty colorCode = new SimpleStringProperty("");

            Form form = Form.of(
                    Group.of(
                        Field.ofStringType(colorName)
                        .label("颜色名称")
                        .required("必填"),
                        Field.ofStringType(colorCode)
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .label("颜色代码")
                        .required("必填")
                        ));

            FormRenderer renderer = new FormRenderer(form);
            MainUtils.searchAndSetControlsLabelWidth(renderer, 35);

            // Building the dialog with the CheckBox as content
            WorkbenchDialog dialog = WorkbenchDialog.builder(
                    "新增颜色编码", renderer, okay, cancel)
                .blocking(true)
                //.maximized(true)
                .showButtonsBar(true)
                .onResult(buttonType -> {
                    if (buttonType.equals(okay)) {
                        form.persist();
                        try {
                        	//TODO 这里可能有漏洞,比如WH->白色,某一天修改为WH->红色,根据SN码追溯的时候会出现颜色误判
							DictionaryAPI.addOrUpdate("color", colorName.getValue(), colorCode.getValue());	
							globalConfig.putColor(colorCode.getValue(), colorName.getValue());
							MainUtils.showSimpleSuccess("新增颜色成功");
						} catch (RequestException e) {
							MainUtils.showSimpleError(ServerAPI.toBizException(null, e).getMessage());
							return;
						}
                    }
                })
                .build();
                dialog.setOnShown(event1 -> {
                    dialog.getButton(okay).ifPresent(
                            button -> {
                                button.disableProperty().bind(form.persistableProperty().not());
                            });
                });
                workbench.showDialog(dialog);
        });

        btnDateConfig.setOnAction(event -> {
            StringProperty dateName = new SimpleStringProperty("");
            StringProperty dateCode = new SimpleStringProperty("");

            Form form = Form.of(
                    Group.of(
                        Field.ofStringType(dateName)
                        .label("日期名称")
                        .required("必填"),
                        Field.ofStringType(dateCode)
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .label("日期代码")
                        .required("必填")
                        ));

            FormRenderer renderer = new FormRenderer(form);
            MainUtils.searchAndSetControlsLabelWidth(renderer, 35);

            // Building the dialog with the CheckBox as content
            WorkbenchDialog dialog = WorkbenchDialog.builder(
                    "新增日期编码", renderer, okay, cancel)
                .blocking(true)
                //.maximized(true)
                .showButtonsBar(true)
                .onResult(buttonType -> {
                    if (buttonType.equals(okay)) {
                        form.persist();
                        if (MainUtils.addOrUpdateLocalCache("date",dateName.getValue(),dateCode.getValue())){
                            
                        }
                    }
                })
                .build();
                dialog.setOnShown(event1 -> {
                    dialog.getButton(okay).ifPresent(
                            button -> {
                                button.disableProperty().bind(form.persistableProperty().not());
                            });
                });
                workbench.showDialog(dialog);
        });

    } 

    public TableView<ProductInfo> createPage(TableView<ProductInfo> table,int pageIndex) {
        ObservableList<ProductInfo> list = globalConfig.getAllProductInfos();
        table.setItems(list);
        return table;
    }

    public Form createFormByVersion(String version){
        Form form = null;
        StringProperty code69 = new SimpleStringProperty("");
        StringProperty productName = new SimpleStringProperty("");       
        
        StringProperty printerTemplate = new SimpleStringProperty("");
        StringProperty productPartNumber = new SimpleStringProperty("");
        StringProperty productType = new SimpleStringProperty("");
        StringProperty productId = new SimpleStringProperty("");
        StringProperty SKU = new SimpleStringProperty("");
        DoubleProperty productLength = new SimpleDoubleProperty(0);
        DoubleProperty productWidth = new SimpleDoubleProperty(0);
        DoubleProperty productHeight = new SimpleDoubleProperty(0);
        IntegerProperty quantity = new SimpleIntegerProperty(0);
        StringProperty factoryCode = new SimpleStringProperty("");
        SimpleDoubleProperty boxNetWeight = new SimpleDoubleProperty(0);
        SimpleDoubleProperty boxGrossWeight = new SimpleDoubleProperty(0);     
        final List<String> printTemplates=ClazzUtils.getAllPrintTemplateName(version);
        if(version.equals("sushi")){
        	 final SimpleListProperty<String> colorList=new SimpleListProperty<>(FXCollections.observableArrayList(globalConfig.getAllColorNames()));
        	final SimpleObjectProperty<String> productColor=new SimpleObjectProperty<>(); //选择的颜色
        	final SingleSelectionField<String> colorSelectionField = Field.ofSingleSelectionType(colorList,productColor);
//            SingleSelectionField colorSelectionField = Field.ofSingleSelectionType(globalConfig.getAllColorElements(), 1);
            form = Form.of(
                    Group.of(
                        Field.ofStringType(productPartNumber)
                        .id("productPartNumber")
                        .label("产品料号")
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .required("必填"),
                        Field.ofStringType(productName)
                        .id("productName")
                        .label("产品名称")
                        .required("必填"),
                        Field.ofStringType(productType)
                        .id("productType")
                        .label("产品型号")
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .required("必填"),
                        colorSelectionField
                        //.id("productColor")
                        .label("产品颜色")
                        .id("productColor"),
                        Field.ofStringType(factoryCode)
                            .id("factoryCode")
                        .label("工厂代码")
                        .required("必填"),
                        Field.ofIntegerType(quantity)
                            .styleClass("field")
                            .label("装载数量")
                            .id("boxQuantity")
                            .validate(IntegerRangeValidator.atLeast(1, "必须为正整数"))
                            .required("必填"),
                        Field.ofDoubleType(boxNetWeight)
                            .styleClass("field")
                            .label("净重(kg)")
                            .id("boxNetWeight")
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填"),
                        Field.ofDoubleType(boxGrossWeight)
                            .styleClass("field")
                            .label("毛重(kg)")
                            .id("boxGrossWeight")
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填"),

                        Field.ofDoubleType(productLength)
                            .id("boxLength")
                            .label("产品长(mm)")
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填"),
                        Field.ofDoubleType(productWidth)
                            .id("boxWidth")
                            .label("产品宽(mm)")
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填"),
                        Field.ofDoubleType(productHeight)
                            .id("boxHeight")
                            .label("产品高(mm)")
                            .validate(DoubleRangeValidator.atLeast(0.0001, "必须为正实数"))
                            .required("必填")

                        ));
        }else if(version.equals("xiaomi")){
            SingleSelectionField tpSelectionField = Field.ofSingleSelectionType(printTemplates, 0);
            final StringProperty productColor=new SimpleStringProperty("");
            form = Form.of(
                    Group.of(
                        Field.ofStringType(productType)
                        .id("productType")
                        .label("产品型号")
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .required("必填"),
                        Field.ofStringType(code69)
                        .id("code69")
                        .label("69 码")
                        .validate(NumbersValidator.justNumber("只能是数字"))
                        .validate(StringLengthValidator.exactly(13,"长度必须是13"))
                        .validate(Code69Validator.verify("69码校验失败"))
                        .required("必填"),
                        Field.ofStringType(productId)
                        .id("productId")
                        .label("商品 id")
                        .validate(NumbersValidator.justNumber("只能是数字"))
                        .validate(StringLengthValidator.exactly(5,"5位数字"))
                        .required("必填"),
                        Field.ofStringType(SKU)
                            .id("SKU")
                        .label("SKU")
                        .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                        .required("必填"),
                        Field.ofStringType(productName)
                            .id("productName")
                        .label("SKU 名称")
                        .required("必填"),
                        Field.ofStringType(productColor)
                            .id("productColor")
                        .label("产品颜色")
                        .required("必填"),
                        tpSelectionField
                        .label("打印模板")
                        .id("printerTemplate") 
                        ));

        }
        return form;
    }


	public Form updateFormByVersion(ProductInfo item,String version){
        Form form = createFormByVersion(version);
        String text = JSON.toJSONString(globalConfig.getProductInfo(item.getId()));
        FormUtils.json2Form(form,text);

        return form;
    } 
    private boolean isUnique(List<ProductInfo> list, ProductInfo productInfo, String version){
        for(ProductInfo p:list){
            if(version.equals("sushi")){
                if(productInfo.getProductPartNumber().equals(p.getProductPartNumber())){
                    return false;
                }
            }else{
                if(productInfo.getProductId().equals(p.getProductId())){
                    return false;
                }
            }
        }
        return true;
    }
}



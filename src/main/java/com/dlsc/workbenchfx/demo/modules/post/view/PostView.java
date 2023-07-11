package com.dlsc.workbenchfx.demo.modules.post.view;

//import javafx.scene.Group;


import com.dlsc.workbenchfx.demo.modules.palletSN.view.PalletSNView;
import javafx.geometry.Insets;
import javafx.print.Printer;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.application.Platform;
import javafx.beans.property.*;

import com.dlsc.workbenchfx.demo.LocalCache;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.ZplPrinter;
import com.dlsc.workbenchfx.demo.api.PalletAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.scene.text.FontWeight;
import com.dlsc.workbenchfx.demo.modules.productInfo.view.ProductInfoView;
import com.cg.core.gui.FormUtils;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;




public class PostView extends BorderPane {  

    public TableView<ProductLaser> table;
    private GridPane controls;
    //private Button buttonImport;
    private VBox bindingInfo;
    private Label countryLabel;
    public ScrollPane scrollContent;

    //public StringProperty factoryCode;// = new SimpleStringProperty("");
    public Button buttonRefresh;
    public Integer count = 0;


    public static String[] day = {"1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V"};
    public static String[] month = {"E","F","G","H","I","J","K","L","M","N","O","P"};
    public static String[] year = {"M","N","O","P","Q","R","S","T","U","V","W"};

    public static Integer pageCount=1;
    public static Integer pageSize = 30;
    public static String partSNCode;
    public static String creator;
    public StringField factoryCodeField;
    public Stage primaryStage;
    private Form formInstance;
    private String currentCartoonBoxSN;
    private String currentBoxNickName;
    public String version;

    ChoiceBox choicePrinter;// = new ChoiceBox(FXCollections.observableArrayList(printers));
    private Workbench workbench;
    public SimpleIntegerProperty productNumber = null;


    public ListView<String> listView = new ListView<>();
    public TextField txtBoxSN = new TextField();
    public ObservableList<ProductInfo> productInfoList = null; //ProductInfoView.query();
    public ObservableList<String> snList = null;

    public CartoonBoxSN cartoonBoxSN = null;  
    public Label jobStatusProductSN = new Label("请先设置扫码枪的回车建.");

    public Button buttonSNCode = new Button("开始打印");
    public TextField txtProductSN = new TextField();
    //public GridPane gridPane = new GridPane();
    public Form formUpdate = null;
    public List<String> productList = null;//new ArrayList<String>();

    Thread thread;

    public void recoverTranspose(CheckBox cbTranspose,String name, String version){
        String printerTransPose = MainUtils.getDict(name, "printerTransPose", version);
        if(printerTransPose!=null && printerTransPose.equals("转置")){
            cbTranspose.setSelected(true);
        }else{
            cbTranspose.setSelected(false);
        }
    }

    public void saveTranspose(CheckBox cbTranspose, String name, String version){
        if(cbTranspose.isSelected()){
            MainUtils.addOrUpdateLocalCache(name, "printerTransPose", "转置", version);
        }else{
            MainUtils.addOrUpdateLocalCache(name, "printerTransPose", "默认", version);
        }
    }

    public void init(Workbench workbench){
        productInfoList = GlobalConfig.getInstance().getAllProductInfos(); 
        productList = new ArrayList<String>();

        this.workbench = workbench;
        scrollContent = new ScrollPane();

        getStyleClass().add("module-background");
        //getStylesheets().add(getClass().getResource("/colorBoxSN.css").toExternalForm());
        getStyleClass().add("root-pane");

        getStyleClass().add("module-background");
        getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        getStyleClass().add("root-pane");

        scrollContent.setFitToWidth(true);
        scrollContent.getStyleClass().add("scroll-pane");
        scrollContent.getStyleClass().add("bordered");

        setCenter(scrollContent);

        version = LocalCache.getCache().getClientType();

        //ObservableList<ProductInfo> productInfoList = ProductInfoView.query();
        for (int i=0; i<productInfoList.size();i++){
            ProductInfo productInfo = productInfoList.get(i);
            String item =  i + ": "+productInfo.getProductName();
            productList.add(item);
        }


        formInstance = getForm();
        MainUtils.recoverForm(getTitle()+"PostView", "form", formInstance, version);

        FormRenderer formRenderer = new FormRenderer(formInstance);
        //MainUtils.searchAndSetControlsLabelWidth(formRenderer, 10);

        VBox root = new VBox();
        VBox cartoonBox = new VBox();
        //cartoonBox.getStyleClass().add("bordered");


        scrollContent.setContent(root);


        //Adding GridPane
       GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.getStyleClass().add("bordered_all");

        cartoonBox.setStyle("-fx-padding: 0 20 0 20;");
        gridPane.setStyle("-fx-padding: 20 20 20 20;");
        //gridPane.setAlignment(Pos.CENTER);
        //gridPane.setHgap(80);
        gridPane.setVgap(10);

        //Implementing Nodes for GridPane
        //MainUtils.recoverTextField(txtBoxSN, "cartoonBox", "txtBoxSN",version);
        Label lbProductSN = new Label("栈板SN码:");
        txtProductSN = new TextField();
        txtProductSN.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        txtProductSN.disableProperty().bind(formInstance.validProperty().not());
        txtProductSN.setPrefHeight(50);
        txtProductSN.setEditable(false);

        //if(txtBoxSN.getText().isEmpty()){
        //    txtProductSN.setEditable(false);
        //}

        Label lbBoxSN = new Label("箱子SN码:");
        Label lblMessage = new Label();
        //final TextField txtBoxSN = new TextField();
        txtBoxSN.setEditable(false);
        txtBoxSN.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        lblMessage.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));


        if (getTitle().equals("pallet")){
            gridPane.add(lbProductSN, 0, 1);
            gridPane.add(txtProductSN, 1, 1);
        }
        //clipChildren(gridPane);
        //imgBox.setPadding(new Insets(0, 0, 0, 0));
        //VBox.setVgrow(imageView, Priority.ALWAYS);
        //imageView.fitHeightProperty().bind(gridPane.heightProperty().multiply(0.6D));
        //imgBox.getStyleClass().add("bordered_all");
        //gridPane.add(imgBox,1,4);


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth( 15 );
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth( 85 );

        //gridPane.setGridLinesVisible(true);
        gridPane.getColumnConstraints().addAll( col1, col2);
        //GridPane.setColumnSpan(imgBox, 3);
        //GridPane.setColumnSpan(lbProductSNStatus, 3 );
        //GridPane.setColumnSpan(lbProductList, 3 );

        //MainUtils.recoverPane("cartoonBox", "boxPane", gridPane, version);
        //jobStatusProductSN.setText("已装箱: "+listView.getItems().size()+" / "+String.valueOf(productNumber.getValue())+"个");
        VBox.setVgrow(gridPane, Priority.ALWAYS );
        cartoonBox.getChildren().add(gridPane);
        root.getChildren().addAll(formRenderer,cartoonBox);



        controls = new GridPane();

        countryLabel = new Label("帮助电话: "+MainUtils.helpPhone);

        CheckBox cbTranspose = new CheckBox("纵向打印");
        recoverTranspose(cbTranspose, getTitle()+"PostView", version);
        cbTranspose.setOnAction(e->{
            saveTranspose(cbTranspose,getTitle()+"PostView",version);
        });
        cbTranspose.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cbTranspose, Priority.ALWAYS);

        bindingInfo = new VBox();
        bindingInfo.setPadding(new Insets(10));
        bindingInfo.getChildren().addAll(countryLabel);
        bindingInfo.setSpacing(10);
        bindingInfo.setPrefWidth(200);
        bindingInfo.getStyleClass().add("bordered");

        
        controls.add(bindingInfo, 0, 0);


        //VBox snBox = new VBox();

        //Label l = new Label("选择打印机");
        //ObservableSet<Printer> printers = Printer.getAllPrinters();
        ////Log.info(printers);

        //// create a choiceBox
        //choicePrinter = new ChoiceBox(FXCollections.observableArrayList(printers));
        //if (printers.isEmpty()){
        //    choicePrinter.setTooltip(new Tooltip("请先安装打印机"));
        //}
        //choicePrinter.setPrefWidth(200);

        //snBox.setSpacing(10);
        //snBox.setPrefWidth(200);
        //snBox.getStyleClass().add("bordered");
        //snBox.setPadding(new Insets(10));
        //snBox.getChildren().addAll(l,choicePrinter);
        //controls.add(snBox, 0, 1);


        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        //controls.add(crudContent, 0, 3);

        //////////////////////////////////////////////////////////////////////////////

        VBox crudCurrent = new VBox();

        //buttonSNCode = new Button("开始装箱");
        buttonSNCode.setMaxWidth(Double.MAX_VALUE);
        buttonSNCode.getStyleClass().add("save-button");

        Label printLabel = new Label("打印张数:");
        TextField printCounter = new TextField("1");
        MainUtils.numericOnly(printCounter);

        crudCurrent.setPadding(new Insets(10));
        if(getTitle().equals("pallet")){
            crudCurrent.getChildren().addAll(buttonSNCode,cbTranspose);
        }else{
            crudCurrent.getChildren().addAll(buttonSNCode,cbTranspose,printLabel,printCounter);
        }
        crudCurrent.setSpacing(10);
        crudCurrent.setPrefWidth(200);
        crudCurrent.getStyleClass().add("bordered");

        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        controls.add(crudCurrent, 0, 2);


        setLeft(controls);
        

        // binds

        buttonSNCode.disableProperty().bind(formInstance.validProperty().not());
        buttonSNCode.setOnAction(event -> {
            if(getTitle().equals("pallet")){ // 目的仓页面只改变按钮状态,中箱物流页面直接打印
                if(buttonSNCode.getText().equals("开始打印")){
                    buttonSNCode.setText("结束打印");
                    txtProductSN.setEditable(true);
                    formInstance.getFields().forEach(s->s.editable(false));
                }else{
                    buttonSNCode.setText("开始打印");
                    txtProductSN.setEditable(false);
                    formInstance.getFields().forEach(s->s.editable(true));
                }
            }else{
                if(buttonSNCode.getText().equals("开始打印")){
                    buttonSNCode.setText("结束打印");
                    formInstance.getFields().forEach(s->s.editable(false));
                }else{
                    buttonSNCode.setText("开始打印");
                    formInstance.getFields().forEach(s->s.editable(true));
                    return;
                }


                MainUtils.saveForm(getTitle()+"PostView", "form", formInstance, version);
                String printerName = MainUtils.getPrinterName();
                if(printerName==null || printerName.isEmpty()){
                    MainUtils.showSimpleError("打印机没配置!");
                    return;
                }
                String printerDPI = MainUtils.getPrinterDPI();
                if(printerDPI==null || printerDPI.isEmpty()){
                    MainUtils.showSimpleError("打印机DPI没配置!");
                    return;
                }

                ZplPrinter p = new ZplPrinter(printerName,printerDPI);
                formInstance.persist();
                String text = FormUtils.form2JsonStr(formInstance);
                JSONObject json = JSON.parseObject(text);
                String printerTemplate;
                try{
                    printerTemplate = MainUtils.getXiaoMiTemplateByProductId(json.getString("productId"),productInfoList);
                }catch(Exception e){
                    Log.error("",e);
                    return;
                }
                String countStr = printCounter.getText();
                countStr = countStr.trim();
                int count;
                try{
                    count = Integer.valueOf(countStr);
                }catch(NumberFormatException e){
                    MainUtils.showSimpleError("打印张数数值错误:"+e.getMessage());
                    return;
                }
                Runnable runnable =
                    () -> { 
                        Log.debug("打印开始========="); 

                        for(int i=0; i<count; i++){
                            if (buttonSNCode.getText().equals("开始打印")){
                                Platform.runLater(new Runnable(){
                                    @Override
                                    public void run(){
                                        MainUtils.showSimpleSuccess("终止打印!");
                                    }
                                });
                                return;
                            }
                            try{
                                TimeUnit.SECONDS.sleep(3);
                            }catch(Exception e){
                                Log.error("",e);
                            }
                            p.printBoxPost(printerTemplate, json.getString("productId"), json.getString("color"), 
                                    json.getString("SKU"), json.getString("PO"), dateStr(json.getString("createDate")), json.getString("supplier"), 
                                    json.getString("prodArea"), json.getString("salesRegion"), json.getString("productName"),json.getString("productSpec"),json.getString("pkgSpec"),json.getString("inspection"),json.getString("boxGrossWeight"),json.getString("boxNetWeight"),json.getString("boxSize"),json.getString("volume"),json.getString("dstCity"),json.getString("dstArea")); 
                            Platform.runLater(new Runnable(){
                                @Override
                                public void run(){
                                    MainUtils.showSimpleSuccess("打印成功!");
                                }
                            });

                        }
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run(){
                                MainUtils.showSimpleSuccess("终止完成!");
                                buttonSNCode.setText("开始打印");
                            }
                        });

                    };

                thread = new Thread(runnable);
                thread.setDaemon(true);//系统关闭时自动销毁线程
                thread.start();//启动称重线程


            }
        });
        txtProductSN.setOnKeyPressed(e->{
        	if(!txtProductSN.isEditable()){
        		//MainUtils.showSimpleError("已停止装箱,禁止录入SN码");
        		return;
        	}		
        	if(e.getCode()!=KeyCode.ENTER)
        		return;
            MainUtils.saveForm(getTitle()+"PostView", "form", formInstance, version);
        	final String snCode = txtProductSN.getText();
            txtProductSN.clear();//清空文本输入
            if(snCode==null||snCode.isEmpty()) return;//空值
            // 服务器端栈板SN的验证
            try {
				if(PalletAPI.getPalletByNickName(snCode)==null) {
				    MainUtils.showSimpleError("栈板SN码服务器没有查到");
				    return;
				}
			} catch (RequestException e1) {
				//TODO 
				 MainUtils.showSimpleError("栈板SN码服务器没有查到");
				 return;
			}
            String printerName = MainUtils.getPrinterName();
            if(printerName==null || printerName.isEmpty()){
                MainUtils.showSimpleError("打印机没配置!");
                return;
            }
            String printerDPI = MainUtils.getPrinterDPI();
            if(printerDPI==null || printerDPI.isEmpty()){
                MainUtils.showSimpleError("打印机DPI没配置!");
                return;
            }

            //String printerTemplate = MainUtils.getPrinterTemplate();
            //if(printerTemplate==null || printerTemplate.isEmpty()){
            //    MainUtils.showSimpleError("打印模板没配置!");
            //    return;
            //}
            ZplPrinter p = new ZplPrinter(printerName,printerDPI);
            formInstance.persist();
            String text = FormUtils.form2JsonStr(formInstance);
            JSONObject json = JSON.parseObject(text);
            String printerTemplate; 
            try{
                printerTemplate = MainUtils.getXiaoMiTemplateByProductId(json.getString("productId"),productInfoList);
            }catch(Exception _e){
                return;
            }
            p.printDstHouse(printerTemplate, json.getString("dstCity"), json.getString("dstArea"), json.getString("productId"), dateStr(json.getString("createDate")), snCode);
            MainUtils.showSimpleSuccess("打印成功!");
        });
    }


    public String dateStr(String date){
        String[] date_time = date.split(" ");
        String[] year_month_day = date_time[0].split("-");
        String dateCode = year_month_day[0] + year_month_day[1] + year_month_day[2];
        return dateCode;
    }

    public Form getForm(){
        Form form = null;
        return form;
    }

    public String getTitle(){
        String title = "";
        return title;
    }

}

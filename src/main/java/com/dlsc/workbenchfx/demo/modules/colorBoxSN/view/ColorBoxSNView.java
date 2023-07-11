package com.dlsc.workbenchfx.demo.modules.colorBoxSN.view;

import com.dlsc.workbenchfx.demo.zplTemplate.TPInterface;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.demo.ZplPrinter;
import com.dlsc.workbenchfx.demo.api.ServerAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.global.ProductInfoForm;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;

import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;

import javafx.scene.text.FontWeight;




public class ColorBoxSNView extends BorderPane {
	private final GlobalConfig globalConfig=GlobalConfig.getInstance();
	private final ProductInfoForm form=new ProductInfoForm(ProductInfoForm.COLORBOX);    
	private final Label lblMessage = new Label();
	/**
	 * 产品SN码
	 */
	private final TextField txtUserName = new TextField();
    private GridPane controls;   
    //private Button buttonImport;
    private VBox bindingInfo;
    private Label countryLabel;   
    private VBox crudContent;
    /**
     * 补打按钮
     */
    private Button reprintButton;
    public Button buttonRefresh;
    public Button buttonSNCode;

    public static Integer pageCount=1;
    public static Integer pageSize = 30;         
    public Label jobStatus;    
    private String version;     

    public ColorBoxSNView(String creator) {
       
    }   

    public void init(Workbench workbench){
        MainUtils.setWorkbench(workbench);                 
        version = globalConfig.getClientType();


        getStyleClass().add("module-background");
        //getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/colorBoxSN.css").toExternalForm());
        getStyleClass().add("root-pane");

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10,50,50,50));
         
        //Adding HBox
        HBox hb = new HBox();
        hb.setPadding(new Insets(80,20,20,30));
        
        Form formInstance = form.getFormInstanceByConfig(version);
        FormRenderer formRenderer = new FormRenderer(formInstance);
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(50);
        gridPane.setVgap(5);

         
       //Implementing Nodes for GridPane
        Label lblUserName = new Label("产品SN码:");        
        txtUserName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        txtUserName.setPrefWidth(500);       
        
         
        //Adding Nodes to GridPane layout
        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtUserName, 1, 0);     
        gridPane.add(lblMessage, 1, 2);
         
        jobStatus = new Label("提示:扫描产品SN码之前请设置扫码枪的回车建!");
        Label lbStatus = new Label("打印状态: ");
        gridPane.add(lbStatus,0,3);
        gridPane.add(jobStatus,1,3);
        
                 
        //Reflection for gridPane
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPane.setEffect(r);
         
        //DropShadow effect 
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
         
        //Adding text and DropShadow effect to it
        Text text = new Text("打印彩盒条形码");
        text.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
        text.setEffect(dropShadow);
         
        //Adding text to HBox
        hb.getChildren().add(text);
                           
        //Add ID's to Nodes
        bp.setId("bp");
        gridPane.setId("root");
        //btnLogin.setId("btnLogin");
        text.setId("text");
                 
        //Add HBox and GridPane layout to BorderPane Layout
        bp.setTop(hb);
        //bp.setCenter(gridPane);

        //tableBox.setId("table");
        gridPane.setPrefHeight(300);

        VBox tableBox = new VBox(formRenderer);

        tableBox.getStyleClass().add("scroll-pane");
        tableBox.getStyleClass().add("bordered");
        VBox.setMargin(tableBox, new Insets(0, 0, 10, 0));

        VBox vbox = new VBox(tableBox,gridPane);

        bp.setCenter(vbox);

        setCenter(bp);

        txtUserName.setOnKeyPressed(e->{
        	workbench.hideDrawer();
            if(e.getCode() == KeyCode.ENTER){                
                final String snCode = txtUserName.getText();
                if(snCode!=null&&!snCode.isEmpty()){
                	printSN(snCode,version);                       
                	txtUserName.clear();                                
                }
            }
        });


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
        reprintButton = new Button("补打标签");
        reprintButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(reprintButton, Priority.ALWAYS);

        crudContent.setPadding(new Insets(10));
        crudContent.getChildren().addAll(reprintButton);
        crudContent.setSpacing(10);
        crudContent.setPrefWidth(200);
        crudContent.getStyleClass().add("bordered");
        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        controls.add(crudContent, 0, 1);

        reprintButton.setOnAction(e->{
            StringProperty colorBoxSN = new SimpleStringProperty("");
            Form form = Form.of(Group.of(Field.ofStringType(colorBoxSN).label("彩盒SN码").required("必填")));
            FormRenderer renderer = new FormRenderer(form);
            MainUtils.searchAndSetControlsLabelWidth(renderer, 35);
            ButtonType okayButton = MainUtils.okayButton();
            ButtonType cancelButton = MainUtils.cancelButton();
            var dialog = WorkbenchDialog.builder("补打标签", renderer, okayButton, cancelButton)
                    .showButtonsBar(true)
                    .onResult(buttonType -> {
                        if (buttonType.equals(okayButton)) {
                            form.persist();                            
                            printSN(colorBoxSN.getValue(),version,true);
                        }
                    })
                    .build();
            dialog.setOnShown(event1 -> {dialog.getButton(okayButton).ifPresent(button -> { button.disableProperty().bind(form.persistableProperty().not());});});
            workbench.showDialog(dialog);
        }); 

        setLeft(controls);   
    }
    private void printSN(String snCode,String version) {
    	printSN(snCode, version,false);
    }  
    /**
     * 验证彩盒标签合法性，并打印合法的彩盒标签 
     * @param snCode 产品SN
     * @param version 客户端类型
     * @param isRepair 是否标签补打
     * @return true/false
     */
    private void printSN(String snCode,String version,boolean isRepair) {      
        // ----------------验证SN合法性----------------
        if(form.getSelectedProductInfo()==null) {
        	MainUtils.showSimpleError("未选择产品信息");
			return;
        } 
        form.persist();
        ProductLaser tempLaser=null;
        if(!isRepair) {//非补打需要校验SN码
        	try {
        		ProductInfo info=form.getSelectedProductInfo();
        		info.setFactoryCode(form.getFactoryCode());
    			tempLaser = ProductLaser.fromSN(snCode, version, info);//根据SN码自动生成产品并校验
    			if("xiaomi".equals(version)) {
    				String selectEC=form.getEcologicalChain();
    				if(!tempLaser.getEcologicalChain().equals(selectEC)) {    					
    					MainUtils.showSimpleError("SN码包含的生态链公司代码"+tempLaser
    							.getEcologicalChain()+"与设置值"+selectEC+"不一致");
    	    			return;
    				}
    				String selectFC=form.getFactoryCode();
    				if(!tempLaser.getFactoryCode().equals(selectFC)) {
    					MainUtils.showSimpleError("SN码包含的工厂代码"+tempLaser
    							.getFactoryCode()+"与设置值"+selectFC+"不一致");
    	    			return;
    				}
    				
    			}
    		} catch (BizException e2) {
    			MainUtils.showSimpleError(e2.getMessage());
    			return;
    		} 
        }
        final String printerTemplate = form.getSelectedProductInfo().getPrinterTemplate();
        if("xiaomi".equals(version)) {
        	if(printerTemplate==null||printerTemplate.isBlank()) {
        		MainUtils.showSimpleError("请先到产品设置中选择打印模板");
        		return;
        	}
        	try {
        		TPInterface.getService(printerTemplate);
        	}catch (BizException e) {
        		MainUtils.showSimpleError(e.getMessage());
        		return;
			}
        }
        
        ProductLaser laser=null;
		try {
			laser = ProductLaserAPI.getProductLaser(snCode);
		} catch (RequestException e1) {
			MainUtils.showSimpleError(ServerAPI.toBizException(null, e1).getMessage());
			return;
		}//查询已入库的SN码 
        if(laser==null) {//校验通过的SN码未入库则自动入库
        	if(isRepair) {//补打必需已入库
        		MainUtils.showSimpleError("产品未入库，请先扫码入库");
        		return;
        	}
        	laser=tempLaser;
        	laser.setUsed("是");
        	laser.setCreator(globalConfig.getCurrentUser().getUserName());
        	if("sushi".equals(version)) {
        		laser.setStatus("N");
        	}        	
        	try {
				if(!ProductLaserAPI.addLasers(List.of(laser))) {
					MainUtils.showSimpleError("彩盒入库失败");
					return;
				}
			} catch (RequestException e) {
				MainUtils.showSimpleError(ServerAPI.toBizException(null, e).getMessage());
            	return;				
			}       	
        }else {//已入库
        	if(!isRepair&&"是".equals(laser.getUsed())) {
        		MainUtils.showSimpleError("禁止重复打印:"+snCode);
				return;
        	}
        }     
        try{
            String printerName = MainUtils.getPrinterName();
            String printerDPI = MainUtils.getPrinterDPI();
            if(printerName==null || printerName.isEmpty()){
                MainUtils.showSimpleError("打印机没配置!");
                return;
            } else if(printerDPI==null || printerDPI.isEmpty()){
                MainUtils.showSimpleError("打印机DPI没配置!");
                return;
            }else {
                ZplPrinter p = new ZplPrinter(printerName,printerDPI);
                try {
                    LocalDate localDate = LocalDate.now();//For reference
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedString = localDate.format(formatter);
                	p.printColorBox(printerTemplate, laser.getSNCode(), laser.getSKU(),laser.getProductColor(), yearDotMonth(formattedString), laser.getProductName(), laser.getCode69());
                	MainUtils.showSimpleSuccess(snCode+"打印成功");
                }catch (Throwable e) {
                	Log.error(snCode+"打印异常", e);
                	MainUtils.showSimpleSuccess(snCode+"打印异常");
                	return;
				}
               
            }
        }catch(Exception e){
        	Log.error("", e);
        	MainUtils.showSimpleError(snCode+"打印失败");
        }
    }   

    private String yearDotMonth(String date){
        String[] date_time = date.split(" ");
        String[] year_month_day = date_time[0].split("-");
        String dateCode = year_month_day[0] +"."+ year_month_day[1];
        return dateCode;
    }

}

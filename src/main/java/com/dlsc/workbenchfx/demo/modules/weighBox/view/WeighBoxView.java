package com.dlsc.workbenchfx.demo.modules.weighBox.view;

import com.dlsc.workbenchfx.demo.WeightDemo;
import com.dlsc.workbenchfx.demo.api.ServerAPI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.MathUtil;
import com.dlsc.workbenchfx.Workbench;

import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Function;

import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;
import com.dlsc.workbenchfx.demo.modules.weighBox.model.WeighRecord;

import javafx.scene.text.FontWeight;




public class WeighBoxView extends BorderPane {

    private TableView<WeighRecord> table;
    private GridPane controls;
    private VBox bindingInfo;
    private Label countryLabel;      

    private TextField txtUpBound ;
    private TextField txtDownBound ;
    //public ChoiceBox choicePorts = new ChoiceBox();
    //public ChoiceBox choiceBitRates = new ChoiceBox();
    private Thread thread;    
    /**
     * 消息提示框
     */
    private Label lblMessage = new Label();
    /**
     * SN码输入框
     */
    private TextField txtUserName = new TextField();
    /**
     * 重量手工输入框
     */
    private final TextField txtWeight = new TextField(); 
    /**
     * 是否允许手工输入重量
     */
    private final CheckBox cbWeight = new CheckBox("手工输入重量(g)");

    public WeighBoxView(String creator) {
             
    }

    public void init(Workbench workbench){
        Log.info("称重！");

        getStyleClass().add("module-background");
        //getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/colorBoxSN.css").toExternalForm());
        getStyleClass().add("root-pane");


        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10,50,50,50));
         
        //Adding HBox
        HBox hb = new HBox();
        hb.setPadding(new Insets(80,20,20,30));
         
        //Adding GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(50);
        gridPane.setVgap(5);

         
       //Implementing Nodes for GridPane
        Label lblUserName = new Label("产品SN码:");
        txtUserName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        txtUserName.setPrefWidth(500);
        lblMessage.setFont(Font.font("Tahoma",FontWeight.BOLD,40));
        lblMessage.setWrapText(true);
         
        //Adding Nodes to GridPane layout
        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtUserName, 1, 0);
        final Label hint = new Label("扫码枪设置回车进行称重");
        hint.setFont(Font.font("Tahoma",FontWeight.NORMAL,20));
        hint.setTextFill(Paint.valueOf("#666666"));
        gridPane.add(hint, 1, 1);
        
        txtWeight.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        txtWeight.setPrefWidth(500);
        this.flushConfig();
        gridPane.add(cbWeight, 0, 2);
        gridPane.add(txtWeight, 1, 2);
        Button buttonWeight = new Button("称重");
        buttonWeight.setMaxWidth(100);
        buttonWeight.setMaxHeight(100);
        //gridPane.add(buttonWeight, 1, 4);

        //gridPane.add(lblPassword, 0, 1);
        //gridPane.add(pf, 1, 1);
        //gridPane.add(btnLogin, 2, 1);
        gridPane.add(lblMessage, 1, 3);
        //lblMessage.setTextFill(Color.GREEN);
        //lblMessage.setText("重量合格!");              
                 
        //Reflection for gridPane
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPane.setEffect(r);
         
        //DropShadow effect 
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
         
        //Adding text and DropShadow effect to it
        Text text = new Text("产品称重");
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
        table = MainUtils.createTable(Arrays.asList(
                    "序号",
                    "产品SN码",
                    "产品重量(g)",
                    "结果",
                    "称重时间",
                    "是否已入库"
                    ),
                    Arrays.asList(
                        "ID",
                            "SNCode",
                            "weight",
                            "result",
                            "createTime",
                            "bankFlag"
                            )
                                );

        VBox tableBox = new VBox(table);        
        tableBox.setId("table");
        gridPane.setPrefHeight(300);
        //tableBox.setStyle("-fx-padding: 10 5 0 5;");
        //tableBox.setStyle("-fx-background-color:  linear-gradient(gray,DimGrey );");
        VBox vbox = new VBox(gridPane,tableBox);
        bp.setCenter(vbox);

        setCenter(bp);



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

        //Label lport = new Label("电子秤端口");
        //Label lbitrate = new Label("电子秤波特率");
        Label upBound = new Label("重量上限(单位:g)");
        Label downBound = new Label("重量下限(单位:g)");
        txtUpBound = new TextField();
        txtDownBound = new TextField();
        txtUpBound.setId("upBound");
        txtDownBound.setId("downBound");

        snBox.setSpacing(10);
        snBox.setPrefWidth(200);
        snBox.getStyleClass().add("bordered");
        snBox.setPadding(new Insets(10));
        snBox.getChildren().addAll(upBound,txtUpBound,downBound,txtDownBound);
        controls.add(snBox, 0, 1);       
        txtUpBound.setOnKeyReleased(e->{//松开上限
        	refreshTableView();
        });
        txtDownBound.setOnKeyPressed(e->{//松开下限
        	refreshTableView();
        });
        MainUtils.recoverPaneV2("productWeight", "leftPane", controls, "any");
        setLeft(controls);

        txtWeight.editableProperty().bind(cbWeight.selectedProperty());        
        txtWeight.setOnKeyReleased(e->{
            if(e.getCode() == KeyCode.ENTER){
            	final ProductLaser laser=(ProductLaser) txtUserName.getUserData();
            	if(laser==null)return;            
                final String weight = txtWeight.getText();
                if(weight==null||weight.isBlank())return;                
                cbWeight.selectedProperty().set(false);//禁止输入               
                try {
                	final Float wv=MathUtil.toFloat(weight);
                	if(wv==null) {
                		showErr("手工输入重量格式错误:"+weight);
                		return;
                	}
                	weightProcess(laser, wv);                	
                }finally {
                	cbWeight.selectedProperty().set(true);
					txtWeight.clear();
					txtUserName.clear();
					txtUserName.setEditable(true);
				}
            }
        });

        txtUserName.setOnKeyReleased(e->{
            if(e.getCode() == KeyCode.ENTER){            	
                if(!txtUserName.isEditable()){
                    MainUtils.showSimpleError("前一个产品称重中!");
                    return;
                }		              
                String snCode = txtUserName.getText();
                if(snCode==null||snCode.isBlank())return;
                txtUserName.setUserData(null);//置空数据缓存
                boolean clearSN=true;
                try {
                	Float max=MathUtil.toFloat(txtUpBound.getText());
                	if(max==null) {
                		showErr("重量上限未设置或者格式错误");
						return;
                	}
                	Float min=MathUtil.toFloat(txtDownBound.getText());
                	if(min==null) {
                		showErr("重量下限未设置或者格式错误");
						return;
                	}
                	MainUtils.savePaneV2("productWeight", "leftPane", controls, "any");//记录
					final ProductLaser laser=ProductLaserAPI.getProductLaser(snCode);
					if(laser==null) {
						showErr("SN码未入库:"+snCode);
						return;
					}
					if(!"是".equals(laser.getUsed())) {
						showErr("SN码未打印彩盒标签:"+snCode);
						return;
					}
					if(laser.getWeight()!=null&&!laser.getWeight().isBlank()) {
						showErr(snCode+"重复称重:"+laser.getWeight()+"g");
						return;
					}
					if(cbWeight.isSelected()) {
						txtUserName.setUserData(laser);
						txtUserName.setEditable(false);//防止重复扫码称重
						txtWeight.clear();
						clearSN=false;
						showTips("等待手工输入重量 ...");
						return;
					}else {
						cbWeight.selectedProperty().set(false);						
					}					
					final String portName = MainUtils.getLocalCache("system", "weighPort");
                    Log.info("称重机串口:"+portName);
                    if(portName==null || portName.isEmpty()){
                    	showErr("电子称端口还没配置!请去全局设置配置");                        
                        return;
                    }
                    final String unit = MainUtils.getLocalCache("system", "unit");//测量单位
                    if (unit==null || unit.isEmpty()){
                    	showErr("请去“全局”设置电子秤读数单位!");                        
                        return;
                    }
                    final Float minValue=MainUtils.getDictFloat("system", "wtMinValue");//电子秤最小读数
                    final Integer N=MainUtils.getDictInt("system", "weightN");//稳定因子
                    final WeightDemo demo=new WeightDemo(portName, minValue,N);
                    txtUserName.setEditable(false);//禁止录入SN码                 
                    showTips("读数中,请先放置产品 ...");
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
	                            weightProcess(laser, weightValue);
                        	}catch (Throwable t) {
								Log.error("称重异常", t);
							}finally {
								txtUserName.setEditable(true);//放开SN码录入限制
							}
                    };
                    if(thread!=null) {
                    	stopWeightTask();
                    }
                    thread = new Thread(runnable);
                    thread.setDaemon(true);//系统关闭时自动销毁线程
                    thread.start();//启动称重线程
				} catch (RequestException e1) {
					MainUtils.showSimpleError(ServerAPI.toBizException(null, e1).getMessage());
					showErr("网络异常，请稍后再试");
                    return;
				} finally {
					if(clearSN) {
						txtUserName.clear();
					}
				}                
            }
        });      
    }
    private void showErr(String msg) {
    	showErr(msg,null);
    }
    private void showErr(String msg,String popErr) {
    	Platform.runLater(()->{
        	lblMessage.setTextFill(Color.RED);
            lblMessage.setText(msg);
            if(popErr!=null&&!popErr.isBlank()) {
            	MainUtils.showSimpleError(popErr);
            }
    	});
    }
    private void showTips(String tips) {
    	Platform.runLater(()->{
        	lblMessage.setTextFill(Color.YELLOW);
            lblMessage.setText(tips);
    	});
    }
    private void showOK(String msg) {
    	Platform.runLater(()->{
        	lblMessage.setTextFill(Color.GREEN);
            lblMessage.setText(msg);
    	});
    }
    private final Function<String,String> testWeight=weight->{
    	final String maxText=txtUpBound.getText();
    	if(maxText==null||maxText.isBlank()) {
    		return "未设置重量上限";
    	}
    	final String minText=txtDownBound.getText();
    	if(minText==null||minText.isBlank()) {
    		return "未设置重量下限";
    	}
    	final Float max=MathUtil.toFloat(maxText);    	
    	if(max==null) 
    		return "重量上限格式错误:"+maxText;
    	final Float min=MathUtil.toFloat(minText);
    	if(min==null)
    		return "重量下限限格式错误:"+maxText;
    	if(max<min)
    		return "重量上限低于重量下限";
    	final Float target=MathUtil.toFloat(weight);
    	if(target>max) {
    		return "重量高于上限"+maxText;
    	}
    	if(target<min) {
    		return "重量低于下限"+minText;
    	}
    	return "通过";
    }; 
    private int currentRowId=1;
    private WeighRecord addTable(String snCode, String weight){
        WeighRecord weighRecord = new WeighRecord(testWeight);
        weighRecord.setID(currentRowId+"");
        currentRowId++;
        weighRecord.setSNCode(snCode);
        weighRecord.setWeight(weight);
        weighRecord.setBankFlag("否");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(format);
        weighRecord.setCreateTime(formatDateTime);
        table.getItems().add(0,weighRecord);
        int len=table.getItems().size();
        if(len>10) {
        	table.getItems().remove(len-1);
        }
        return weighRecord;
    }
    private void refreshTableView() {
    	if(table!=null)table.refresh();
    }
    private void weightProcess(ProductLaser laser, Float value){    
    	String weightValue=new DecimalFormat("0").format(value);
    	Log.debug("正在更新产品重量:"+laser.getSNCode()+";重量:"+weightValue+"g");
    	WeighRecord wr=addTable(laser.getSNCode(), weightValue); //刷新列表 
    	refreshTableView();
    	String rs=wr.getResult();
    	if("通过".equals(rs)) {
    		showOK("passed !"+" 重量: "+weightValue+"g");    		
    	}else {
    		showErr("failed:"+rs+";重量:"+weightValue+"g");
    		return;
    	}
    	try {
			ProductLaserAPI.updateWeight(laser.getId(), weightValue);
			txtUserName.setUserData(null);//清空数据,确保一个产品重量只更新一次
			Log.debug("产品重量更新成功");
			wr.setBankFlag("是");			
		} catch (BizException e) {
			Log.debug("产品重量更新失败:"+e.getMessage());
			showErr("failed:产品重量更新失败",e.getMessage());
		}    
    }
    public void stopWeightTask() {
    	try {
	        if(thread==null||thread.isInterrupted())return;        	
	        Log.warn("称重线程未终止,将强制中断");        
        	thread.interrupt();
        }catch (Throwable t) {
        	Log.warn("中断称重线程失败");
		}finally {
			Platform.runLater(()->{
				txtUserName.clear();
				txtWeight.clear();
				lblMessage.setText("");
			});
		}        
    }
    public void destroy() {
    	stopWeightTask();
    	currentRowId=1;
    }
    /**
     * 是否允许手工录入重量
     */   
    public void flushConfig() {
    	txtWeight.setVisible(false);
    	if("手工称重".equals(MainUtils.getLocalCache("system", "weightByHand"))){    		
            cbWeight.setVisible(true);
            txtWeight.setVisible(true);
        }else {
        	cbWeight.selectedProperty().set(false);
        	cbWeight.setVisible(false);
            txtWeight.setVisible(false);
        }
    }
}

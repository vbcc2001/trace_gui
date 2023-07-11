package com.dlsc.workbenchfx.demo;

import javafx.print.PrinterJob;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.cg.core.gui.FormUtils;
import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.api.CommonAPI;
import com.dlsc.workbenchfx.demo.api.DictionaryAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;
import com.dlsc.workbenchfx.demo.modules.query.model.ProductDetailInfo;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javafx.util.Duration;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Cell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import com.dlsc.workbenchfx.model.WorkbenchDialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.print.Printer;
import javafx.scene.control.ButtonBar;
//import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.dlsc.formsfx.model.structure.SingleSelectionField;

public class MainUtils{   
    //public static Logger logger = Log.getLogger("demo");

    public static void print(){
        //Log.info("hello");
    }
    /**
     * 将对象转字符串复制到粘贴板中
     * @param obj 复制对象
     */
    private static void clipString(Object obj) {
    	Map<DataFormat, Object> content=new HashMap<>();
    	String value="";
    	if(obj instanceof String) {
    		value=obj.toString();    		
    	}else if(obj!=null){
    		try {
    			value=JSON.toJSONString(obj);
    		}catch (Exception e) {
				value=obj.toString();
			}    		
    	}
    	content.put(DataFormat.PLAIN_TEXT, value);
    	Clipboard.getSystemClipboard().setContent(content);
    }
    public static <T>TableView<T> createTable(List<String> headers, List<String> columnName){
        TableView<T> table = new TableView<T>();
        int headSize = headers.size();
        if ((columnName.indexOf("id")>-1)||(columnName.indexOf("id:i")>-1)){
            headSize = headSize - 1;
        }
        table.setOnKeyPressed(e->{
        	if(e.isControlDown()&&e.getCode()==KeyCode.C) {
//        		Log.info("Ctrl+C:"+e.getTarget()+";"+e.getSource());
        		EventTarget target=e.getTarget();
        		if(target instanceof Cell<?>) {//复制单元格内容
        			Cell<?> cell=(Cell<?>) target;
        			Map<DataFormat, Object> content=new HashMap<>();
        			content.put(DataFormat.PLAIN_TEXT, cell.getText());
        			Clipboard.getSystemClipboard().setContent(content);
        		}else if(target instanceof TableView<?>) {//复制一行        			
        			clipString(table.getSelectionModel().getSelectedItem());
        		}
        	}
        });
        table.setOnMouseClicked(e->{
        	if(e.getClickCount()>=2) {        	
        		int rowIdx=table.getSelectionModel().getSelectedIndex();//从0开始
//        		Log.info("鼠标双击:"+rowIdx);
        		if(rowIdx<0)return;               	
        		EventTarget target=e.getTarget();
        		Cell<?> label=null;
        		if(target instanceof Cell<?>) {//空白单元格
        			label=(Cell<?>) target;        		        		
        		}else if(target instanceof Node){
        			Node node=(Node) target;
        			Node parent=node.getParent();
        			if(parent instanceof Cell<?>) {
        				label=(Cell<?>) parent;
        			}
        		}else {
        			Log.info("TableView未知target类型:"+target);
        		}
        		if(label!=null) {
//        			Log.info("label:"+label);
        			table.getSelectionModel().clearSelection(rowIdx);        			
        			label.updateSelected(true);
        			label.requestFocus();
        			e.consume();

        		}        		      
        	}
        });
        double percentage = 1D/(double)headSize;
        for(int i=0;i<headers.size();i++){
            String[] nameAndType = columnName.get(i).split(":");
            if(nameAndType.length==1){
                TableColumn<T,String> column = new TableColumn<T, String>(headers.get(i));
                column.setCellValueFactory(new PropertyValueFactory<>(nameAndType[0]));
                column.setId(nameAndType[0]);
                column.setCellFactory(TextFieldTableCell.<T>forTableColumn());
                column.prefWidthProperty().bind(table.widthProperty().multiply(percentage));
                table.getColumns().add(column);
            }else{
                if (nameAndType[1].equals("i")){
                    TableColumn<T,Integer> column = new TableColumn<T, Integer>(headers.get(i));
                    column.setCellValueFactory(new PropertyValueFactory<>(nameAndType[0]));
                    column.setId(nameAndType[0]);
                    column.prefWidthProperty().bind(table.widthProperty().multiply(percentage));
                    if (nameAndType[0].equals("id")){
                        column.setVisible(false);
                    }
                    table.getColumns().add(column);
                }
            }
        }
        return table;
    }

	public static <T> void exportExcel(Stage stage, TableView<T> table) {
		// Log.info("export Excel");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导出excel");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("xls", "*.xls", "*.xlsx"));
		File selectedFile = fileChooser.showSaveDialog(stage);
		if (selectedFile == null)
			return;
		try (FileOutputStream fileOut = new FileOutputStream(selectedFile);
				Workbook workbook = getWorkBook(selectedFile.getName());) {
			Sheet spreadsheet = workbook.createSheet("sample");
			Row row = spreadsheet.createRow(0);
			for (int j = 0; j < table.getColumns().size(); j++) {
				row.createCell(j).setCellValue(table.getColumns().get(j).getText());
			}
			for (int i = 0; i < table.getItems().size(); i++) {
				row = spreadsheet.createRow(i + 1);
				for (int j = 0; j < table.getColumns().size(); j++) {
					if (table.getColumns().get(j).getCellData(i) != null) {
						row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
					} else {
						row.createCell(j).setCellValue("");
					}
				}
			}
			workbook.write(fileOut);
			workbook.close();
			fileOut.close();
		} catch (Exception e) {
			Log.error("", e);
		}
	}
    private static Workbook getWorkBook(String fileName) {
    	if(fileName.endsWith(".xlsx"))
    		return new XSSFWorkbook();
    	return new HSSFWorkbook();
    }

	public static <T> void exportExcelAll(Stage stage, TableView<T> table, ObservableList<T> observableList)
			throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导出excel");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("xls", "*.xls", "*.xlsx"));
		File selectedFile = fileChooser.showSaveDialog(stage);
		if (selectedFile == null) return;
		try (FileOutputStream fileOut = new FileOutputStream(selectedFile);
				Workbook workbook = getWorkBook(selectedFile.getName());) {			
			Sheet spreadsheet = workbook.createSheet("sample");
			Row row = spreadsheet.createRow(0);
			for (int j = 0; j < table.getColumns().size(); j++) {
				row.createCell(j).setCellValue(table.getColumns().get(j).getText());
			}
			Class<?> clazz = observableList.get(0).getClass();
			final Map<String, PropertyDescriptor> map = ModelUtil.getPropMap(clazz);
			for (int i = 0; i < observableList.size(); i++) {
				row = spreadsheet.createRow(i + 1);
				for (int j = 0; j < table.getColumns().size(); j++) {
					String fieldName = table.getColumns().get(j).getId();
					PropertyDescriptor p = map.get(fieldName);
					if (p == null) {
						Log.warn(clazz.getSimpleName() + "缺少表格字段:" + fieldName);
						continue;
					}
					Object obj = observableList.get(i);
					Object rs = ModelUtil.readValue(p, obj);
					String fieldValue = rs == null ? "" : rs.toString();
					row.createCell(j).setCellValue(fieldValue);
				}
			}
			workbook.write(fileOut);
		} catch (Exception e) {
			Log.error("", e);
		}
	}


//    public static<T> void importExcel(Stage stage, Workbench workbench, TableView<T> table){
//    }
    public static void showErrorDialog(Workbench workbench, String title, String message){
        ButtonType okay = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        WorkbenchDialog dialog = WorkbenchDialog.builder(
                title, message, okay)
            .showButtonsBar(true)
            .onResult(buttonType -> {
            })
        .build();
        workbench.showDialog(dialog);
    }

    public static void showCopyAbleErrorDialog(Workbench workbench, String title, String message){
        TextField txt = new TextField(message);
        txt.setPrefWidth(200);
        ButtonType okay = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        WorkbenchDialog dialog = WorkbenchDialog.builder(
                title, txt, okay)
            .showButtonsBar(true)
            .onResult(buttonType -> {
            })
        .build();
        workbench.showDialog(dialog);
    }


    public static void showDialog(Workbench workbench, String title, String message){
        ButtonType okay = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        WorkbenchDialog dialog = WorkbenchDialog.builder(
                title, message, okay)
            .showButtonsBar(true)
            .onResult(buttonType -> {
            })
        .build();
        workbench.showDialog(dialog);
    }


//    public static void showInfoDialog(Workbench workbench, String title, String message){
//        ButtonType okay = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
//        WorkbenchDialog dialog = WorkbenchDialog.builder(
//                title, message, okay)
//            .showButtonsBar(true)
//            .onResult(buttonType -> {
//            })
//        .build();
//        workbench.showDialog(dialog);
//    }


    public static<T> Integer getPageSize(TableView<T> table){
        double height = table.getHeight();
        double cellHeiht = 25;
        if (table.getHeight() == 0){
            height = 540;   
        }
        Integer pageSize = (int)Math.round(height / cellHeiht);
        return pageSize;
    }

    public static<T> ObservableList<T> getPage(ObservableList<T> list, Integer pageNumber, Integer pageSize){
        Integer end = (pageNumber+1) * pageSize;
        Integer start = pageNumber * pageSize;
        if (end > list.size()){
            end = list.size();
        }
        ObservableList<T> subList = FXCollections.observableList(list.subList(start, end));
        return subList;
    }

    /**
     * Set label width by percentage
     * @param pane probably the FormRenderer
     * @param labelSize label size by percentage. For ex. if the label needs half the space of width use 50
     */
    public static void searchAndSetControlsLabelWidth(Pane pane, double labelSize) {
        if(pane instanceof GridPane){
            if(pane.getStyleClass().stream().anyMatch(s -> s.contains("simple-"))){
                GridPane gp = (GridPane) pane;
                //gp.setHgap(5);
                //Log.info("h gap: "+gp.getHgap());
                if (gp.getColumnConstraints().size() == 12) {
                    double rest = 100 - labelSize;
                    for (int i = 0; i < gp.getColumnConstraints().size(); i++) {
                        if (i < 3) {
                            gp.getColumnConstraints().get(i).setPercentWidth(labelSize / 2);
                        }
                        else {
                            gp.getColumnConstraints().get(i).setPercentWidth(rest/10);
                        }
                    }
                }
            }
        }
        for (Node child : pane.getChildren()) {
            if (child instanceof Pane) {
                searchAndSetControlsLabelWidth((Pane) child, labelSize);
            }
        }
    }

    public static <T>Integer getPageCount(TableView<T> table, Integer amount){
        Integer pageSize = MainUtils.getPageSize(table);
        Integer pageCount = amount / pageSize;
        if (pageCount==0) pageCount = 1;
        return pageCount;
    }
    
    public static ButtonType okayButton(){
        return new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
    }
    public static ButtonType cancelButton(){
        return new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
    }
    public static String helpPhone = "13751732794";


    public static String post(String postBody,String serviceUrl){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(serviceUrl))
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(postBody))
            .build();
        String res = "{}";
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()==200){
                res = response.body();
            }else{
                Log.info("status code: "+response.statusCode()+" url: "+serviceUrl);
            }
        }catch(Exception e){
            Log.error("",e);
            return "exception";
        }
        return res;
    }
    public static String get(String uri,int timeout){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .timeout(java.time.Duration.ofSeconds(timeout))
            .build();
        String res = "{}";
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()==200){
                res = response.body();
            }
            if (response.statusCode()==404){
                res = "404";
            }

        }catch(Exception e){

            return "exception";
        }
        return res;
    }

    public static String get(String uri){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .timeout(java.time.Duration.ofSeconds(5))
            .build();
        String res = "{}";
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()==200){
                res = response.body();
            }
            if (response.statusCode()==404){
                res = "404";
            }

        }catch(HttpConnectTimeoutException e){
            return "timeout";

        }catch(Exception e){
        	Log.error("",e);
            return "exception";
        }
        return res;
    }
    public static String getPrinterName(){
        String name = getLocalCache("system","printer");
        return name;
    }
    public static String getPrinterDPI(){
        String name = getLocalCache("system","printerDPI");
        return name;
    }


    /**
     * {"code":-1,"data":{"code":400,"msg":"数据违反完整性约束","detailMsg":"see log $1659167359849"}}
     */
    public static void showServerError(String serverError){
        try{
            //JSONObject jsonObject = JSON.parseObject(serverError);
            //JSONObject data = jsonObject.getJSONObject("data");
            //String code = data.getString("code");
            //String msg = data.getString("msg");
            //String detailMsg = data.getString("detailMsg");

            //String content = String.format("代码: %s\n内容: %s\n详情: %s",code,msg,detailMsg);           
            Notifications.create()
                .title("操作失败")
                .text(serverError)
                .showError();

        }catch(Exception e){
            Log.error("",e);
        }
    }
    public static void showWarning(String errMsg) {
    	if(workbench==null||workbench.isDisabled()) {
    		Log.error(errMsg);
    		return;
    	}
    	Label drawer=new Label(errMsg);
        drawer.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
    	drawer.setStyle("-fx-text-fill:red;");    	             
    	drawer.setAlignment(Pos.CENTER);
        drawer.setOpacity(0.8);
        drawer.setPrefWidth(300);
    	TitledPane tp=new TitledPane("操作警告", drawer);
    	tp.setStyle("-fx-text-fill:red;-fx-font-size:3em;");
    	tp.setTextAlignment(TextAlignment.CENTER);
    	workbench.showDrawer(drawer, Side.TOP,25);
    }
    public static Workbench workbench;
//    private static  NotificationPane notificationPane = new NotificationPane();   

    public static void setWorkbench(Workbench workbench){
        MainUtils.workbench = workbench;
    }

    /**
     *  notificationPane
     */
    public static void showNotificationPane(String errMsg){
        try{
            NotificationPane notificationPane = new NotificationPane();
            notificationPane.setShowFromTop(true);
            notificationPane.setText("Do you want to save your password?");
            notificationPane.show();
            //notificationPane.getActions().addAll(new Action("Sync", ae -> {
            //    // do sync

            //    // then hide...
            //    notificationPane.hide();
            //}));
        }catch(Exception e){
            Log.error("",e);
        }
    }

    /**
     *  errMsg
     */
    public static void showSimpleError(String errMsg){
        //showNotificationPane("");
        try{
            Notifications.create()
                .owner(workbench)
                .title("操作失败")
                .text(errMsg)
                .hideAfter(Duration.seconds(5))
                .showError();

        }catch(Exception e){
            Log.error("",e);
        }
    }

    /**
     *  longErrMsg
     */
    public static void showLongSimpleError(String errMsg){
        //showNotificationPane("");

        //GlobalConfig.getInstance().getServerUrl() = "http://localhost:8080";//失败后地址回退到本地
        Stage stage = new Stage();

        VBox vbox = new VBox();

        GridPane gp = new GridPane();
        gp.setPadding( new Insets(10) );
        gp.setHgap( 4 );
        gp.setVgap( 8 );

        Label lblProblem = new Label(errMsg);
        lblProblem.setWrapText(true);
        lblProblem.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
        lblProblem.setTextFill(Color.RED);
        lblProblem.setPrefWidth(580);
        gp.add( lblProblem,  0, 1);  // empty item at 0,0
        
        vbox.getChildren().addAll( gp);
        Scene errScene = new Scene(vbox, 600, 300);
        stage.setScene(errScene);
        stage.setTitle("服务器地址配错!");
        stage.setAlwaysOnTop(true);
        stage.show();
    }


    /**
     *  successMsg
     */
    public static void showSimpleSuccess(String successMsg){
        try{
            Notifications.create()
                .owner(workbench)
                .title("操作成功")
                .text(successMsg)
                .hideAfter(Duration.seconds(1))
                .showInformation();

        }catch(Exception e){
            Log.error("",e);
        }
    }
    public static String getLocalCache(String type, String key){
    	return LocalCache.getCache().getDict(type, key);
    }    

    public static String getRemoteDictByUrl(String host, String type, String key){
        // 双key 获取value
        String value = null;
        String url = host +"/dic/get?dicType="+type+"&dicCode="+key;
        Log.info(url);
        String res = MainUtils.get(url,2);
        Log.info("getDict:"+res);
        JSONObject json = JSON.parseObject(res);
        int code = json.getIntValue("code");
        if (code==0){
            value = json.getString("data");
        }else{
            MainUtils.showServerError(res);
        }
        return value;
    }


    public static String getDict(String type, String key, String version){
        return getLocalCache(version+":"+type,key);
    }

    public static Integer getDictInt(String type,String key) {
    	String value=getLocalCache(type, key);
    	if(value==null||value.isEmpty())
    		return null;
    	try {
    		return Integer.parseInt(value);
    	}catch (Exception e) {
			return null;
		}
    }
    public static Float getDictFloat(String type,String key) {
    	String value=getLocalCache(type, key);
    	if(value==null||value.isEmpty())
    		return null;
    	try {
    		return Float.parseFloat(value);
    	}catch (Exception e) {
			return null;
		}
    } 

    /**
     * 
     * @param type dicType
     * @param key dicCode
     * @param value dicValue
     * @return 是否成功写入本地缓存
     */
    public static boolean addOrUpdateLocalCache(String type, String key, String value){
        return LocalCache.getCache().addOrUpdateDict(type, key, value);

    }


    public static boolean addOrUpdateRemoteDictionary(String type, String key, String value){
    	try {
			return DictionaryAPI.addOrUpdate(type, key, value);
		} catch (RequestException e) {
			Log.error("保存字典失败:"+type+"->"+key+"->"+value, e);
			return false;
		}   
    }

    public static boolean addOrUpdateLocalCache(String type, String key, String value, String version){
        return addOrUpdateLocalCache(version+":"+type,key,value);
    }


    public static boolean printImage(Image image,Label jobStatus, ChoiceBox choicePrinter) 
    {
        ImageView node = new ImageView(image);
        jobStatus.textProperty().unbind();
        jobStatus.setText("正在创建打印任务.....");
        PrinterJob job = PrinterJob.createPrinterJob();
        Printer printer = (Printer)choicePrinter.getSelectionModel().getSelectedItem();
        //Log.info("job: "+job);
        //Log.info("printer : "+printer);
        if (job == null){
            jobStatus.textProperty().unbind();
            jobStatus.setText("打印失败,请选择正确的打印机,并且检查打印机是否正确安装");
            return false;
        }
        job.setPrinter(printer);
        //jobStatus.textProperty().bind(job.jobStatusProperty().asString());
        boolean printed = job.printPage(node);
        if (printed) 
        {
            // End the printer job
            job.endJob();
            jobStatus.textProperty().unbind();
            jobStatus.setText("打印成功 !");
            return true;
        } 
        else
        {
            // Write Error Message
            jobStatus.textProperty().unbind();
            jobStatus.setText("打印失败,请选择正确的打印机,并且检查打印机是否正确安装");
            return false;
        }
    }

//    public static String form2JsonStr(Form formInstance, Map<String,Object> dict){
//        List<com.dlsc.formsfx.model.structure.Field> fields = formInstance.getFields();
//        Map<String,String> kv = new HashMap<String,String>();
//        //Log.info(dict);
//        for (int i=0; i<fields.size();i++){
//            String key = (String)dict.get("\""+fields.get(i).getLabel()+"\""); 
//            //Log.info(fields.get(i).getLabel());
//            //Log.info(key);
//            if(fields.get(i).getClass().toString().indexOf("StringField")>0){
//                String value = ((StringField)(fields.get(i))).getValue();
//                kv.put(key,value);
//            }
//            else if(fields.get(i).getClass().toString().indexOf("DateField")>0){
//                LocalDateTime now = LocalDateTime.now();
//                int hour = now.getHour();
//                int minute = now.getMinute();
//                int second = now.getSecond();
//                LocalDate value = ((DateField)(fields.get(i))).getValue();
//                kv.put(key,value.toString()+String.format(" %02d:%02d:%02d",hour,minute,second));
//            }
//            else if(fields.get(i).getClass().toString().indexOf("IntegerField")>0){
//                Integer value = ((IntegerField)(fields.get(i))).getValue();
//                kv.put(key,value.toString());
//            }
//            else if(fields.get(i).getClass().toString().indexOf("DoubleField")>0){
//                Double value = ((DoubleField)(fields.get(i))).getValue();
//                kv.put(key,value.toString());
//            }
//            else if(fields.get(i).getClass().toString().indexOf("SingleSelectionField")>0){
//                String value = ((SingleSelectionField)(fields.get(i))).getSelection().toString();
//                kv.put(key,value);
//            }
//
//        }
//        String text = JSON.toJSONString(kv);
//        return text;
//    }
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) throws IOException {
            int orgWidth = originalImage.getWidth();
            int orgHeight = originalImage.getHeight();
            int targetHeight = (int)((double)targetWidth * ((double)orgHeight/(double)orgWidth));
            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            //graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.dispose();
            return resizedImage;
    }

    public static String HomeDIR = FileUtils.getUserDirectoryPath();

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
    /**
     * 
     * @param value 称重值
     * @param unit 测量单位
     * @param showUnit 显示单位
     * @return 按显示单位将称重值转换
     */
    public static Float weightToShow(Float value,String unit,String showUnit) {
    	if(unit.length()>showUnit.length()) {//kg->g
    		return value*1000;
    	}else if(unit.length()<showUnit.length()) {//g->kg
    		return value/1000;
    	}else {
    		return value;
    	}
    	
    }
    /**
     * 
     * @param value 重量值
     * @param showUnit 重量单位
     * @return 将重量值和单位拼接为字符串,kg保留3位小数,g不含小数
     */
    public static String toShowWeight(Float value,String showUnit) {
    	if(showUnit.length()==1) {//g
    		DecimalFormat format=new DecimalFormat("0");
			return format.format(value)+showUnit;
    	}else {//kg
    		DecimalFormat format=new DecimalFormat("0.000");
			return format.format(value)+showUnit;
    	}
    	
    }

    public static String float2IntOrFloat(String value){
        String[] parts = value.split("\\.");
        if (Integer.valueOf(parts[1])==0){
            return parts[0];
        }
        return value;
    }

    public static<T> void recoverElement(Property<T> element, String pageName, String elementName, String type, String version){
        String value = getDict(pageName,elementName,version);
        if (value==null || value.isEmpty()){
            return;
        }
        if(type.equals("str")){
            T _value = (T)(value);
            element.setValue(_value);
            return;
        }

        if(type.equals("int")){
            T _value = (T)Integer.valueOf(value);
            element.setValue(_value);
            return;
        }

        if(type.equals("double")){
            T _value = (T)Double.valueOf(value);
            element.setValue(_value);
            return;
        }

        if(type.equals("date")){
            LocalDate localDate = LocalDate.parse(value);
            T _value = (T)localDate;
            element.setValue(_value);
            return;
        }

    }

    public static void recoverSelection(SingleSelectionField<String> element, String pageName, String elementName, String version){
        String value = getDict(pageName,elementName,version);
        if (value==null || value.isEmpty()){
            return;
        }
        element.select(Integer.valueOf(value));
    }
    public static<T> void saveElement(Property<T> element, String pageName, String elementName, String version){
        addOrUpdateLocalCache(pageName, elementName, element.getValue().toString(), version);
    }
    public static<T> void saveSelection(SingleSelectionField<T> element, String pageName, String elementName, String version){
        addOrUpdateLocalCache(pageName, elementName, String.valueOf(element.getItems().indexOf(element.getSelection().toString())), version);
    }
    public static void saveListView(ObservableList<String> element, String pageName, String elementName, String version){
        addOrUpdateLocalCache(pageName, elementName, LocalDate.now().toString()+":"+String.join(",",element), version);
    }
    public static void recoverListView(ObservableList<String> element, String pageName, String elementName, String version){
         String value = getDict(pageName,elementName, version);
        if (value==null || value.isEmpty()){
            return;
        }
        String[] values = value.split(":");
        if(LocalDate.parse(values[0]).getDayOfMonth() != LocalDate.now().getDayOfMonth()){
            //昨天的数据不恢复
            return;
        }
        if(values.length==1){
            return;//没数据
        }

        for (String _value:values[1].split(",")){
            element.add(_value);
        }
    }
    public static void recoverTextField(TextField element, String pageName, String elementName, String version){
        String value = getDict(pageName,elementName,version);
        if (value==null || value.isEmpty()){
            return;
        }
        String[] values = value.split(":");
        if(LocalDate.parse(values[0]).getDayOfMonth() != LocalDate.now().getDayOfMonth()){
            //昨天的数据不恢复
            return;
        }
        element.setText(values[1]);
    }
    public static void saveTextField(TextField element, String pageName, String elementName, String version){
        addOrUpdateLocalCache(pageName, elementName, LocalDate.now().toString()+":"+element.getText(), version);
    }

    public static void saveForm(String pageName,String formName, Form formInstance, String version){
        String jsonstr = FormUtils.form2JsonStr(formInstance);
        jsonstr = Base64.getEncoder().encodeToString(jsonstr.getBytes());
        addOrUpdateLocalCache(pageName, formName, jsonstr, version);
    }
    public static void recoverForm(String pageName,String formName, Form formInstance, String version){
        String jsonstr = getDict(pageName,formName,version);
        if(jsonstr==null)return;
        byte[] decodedBytes = Base64.getDecoder().decode(jsonstr);
        jsonstr = new String(decodedBytes);
        FormUtils.json2Form(formInstance,jsonstr);
    }
    public static void savePane(String pageName,String paneName, Pane pane, String version){
        String jsonstr = FormUtils.pane2JsonStr(pane);
        Log.debug("savePane: "+jsonstr);
        jsonstr = Base64.getEncoder().encodeToString(jsonstr.getBytes());
        addOrUpdateLocalCache(pageName, paneName, jsonstr, version);
    }

    public static void savePaneV2(String pageName,String paneName, Pane pane, String version){
        String jsonstr = FormUtils.node2Json(pane);
        Log.debug("savePane: "+jsonstr);
        jsonstr = Base64.getEncoder().encodeToString(jsonstr.getBytes());
        addOrUpdateLocalCache(pageName, paneName, jsonstr, version);
    }

    public static void recoverPane(String pageName,String paneName, Pane pane, String version){
        String jsonstr = getDict(pageName,paneName,version);
        if(jsonstr==null)return;
        byte[] decodedBytes = Base64.getDecoder().decode(jsonstr);
        jsonstr = new String(decodedBytes);
        //Log.info(jsonstr);
        FormUtils.json2Pane(pane,jsonstr);
    }

    public static void recoverPaneV2(String pageName,String paneName, Pane pane, String version){
        String jsonstr = getDict(pageName,paneName,version);
        if(jsonstr==null)return;
        byte[] decodedBytes = Base64.getDecoder().decode(jsonstr);
        jsonstr = new String(decodedBytes);
        FormUtils.json2Node(pane,jsonstr);
    }

    public static String getBasePath(Class<?> clz) {
        String path=clz.getResource("/").getFile();
        Pattern pt=Pattern.compile("file:/(.*)/.*\\.jar!/.*");
        Matcher	 m=pt.matcher(path);
        while(m.find()) {
            return m.group(1);
        }
        return path;
    }
    public static String changeBase(long n, long base, Map<Integer,String>symbol){
        String ans = "";
        long res = n % base;
        long q = n / base;

        ans = symbol.get((int)res) + ans; 
        n = q;
        while(n!=0){
            res = n % base;
            q = n / base;
            n = q;
            ans = symbol.get((int)res) + ans;
        }
        return ans;
    }

    /**
     * 获取英文或者数字字符串最后n位
     * */

    public static String getStringLastN(String str, int n){
        //获取最后n位并且去掉左边的0
        String substr =  str.substring(str.length() - n);
        substr = substr.replaceFirst("^0+(?!$)", "");
        return substr;
    }

    /**
     *
     *从 形如 00XXX 的 N进制字符串 转10进制的字符串
     * */

    public static String changeBaseTo10(String str, int n, Map<Integer,String>symbol){
        long out = 0;
        Map<String, Integer> newSymbol = new HashMap<>();
        for(Map.Entry<Integer, String> entry : symbol.entrySet()){
            newSymbol.put(entry.getValue(), entry.getKey());
        }
        for(int i=str.length()-1; i>=0; i--){
            String ch = str.substring(i,i+1);
            if (ch.equals("0")) break;
            out += Long.valueOf(newSymbol.get(ch)) * Math.pow(n,i-str.length()+1);
        }
        return String.valueOf(out);
    }

    /**
     *
     *Ean-13码规则：第十三位数字是前十二位数字经过计算得到的校验码。

     例如：690123456789

     计算其校验码的过程为：

     ① 前十二位的奇数位和6+0+2+4+6+8=26

     ② 前十二位的偶数位和9+1+3+5+7+9=34

     ③ 将奇数和与偶数和的三倍相加26+34*3=128

     ④ 取结果的个位数：128的个位数为8

     ⑤ 用10减去这个个位数10-8=2

     所以校验码为2

     （注：如果取结果的个位数为0，那么校验码不是为10（10-0=10），而是0）
     * */
    public static int code69Verify(int[] V){
        int oddSum = V[0] + V[2] + V[4] + V[6] + V[8] + V[10];
        int evenSum = V[1] + V[3] + V[5] + V[7] + V[9] + V[11]; 
        int sum = oddSum + evenSum * 3;
        int ones = sum % 10; 
        if (ones > 0){
            return 10 - ones;
        }
        return ones;
    } 

    /**
     *当前日期到指定日期的间隔天数
     * */
    public static long localDateCompare(LocalDate endDate){
        return LocalDate.now().until(endDate, ChronoUnit.DAYS);
    }


    /**
     *小米版本根据SNCode找出对应的productInfo
     * @throws BizException 
     *
     * */
	public static ProductInfo getXiaoMiProductInfoBySNcode(String snCode, List<ProductInfo> productList)
			throws BizException {
		ProductLaser laser = null;
		try {
			laser = ProductLaserAPI.getProductLaser(snCode);
		} catch (RequestException e) {
			throw new BizException("网络异常，稍后再试");
		}
		if (laser == null) {
			throw new BizException("产品SN码不存在");
		}

		for (ProductInfo productInfo : GlobalConfig.getInstance().getAllProductInfos()) {
			if (laser.getProductId().equals(productInfo.getProductId())) {
				return productInfo;
			}
		}
		throw new BizException("找不到" + laser.getProductId() + "的产品配置");
	}

    /**
     *小米版本根据productId找出对应的productInfo
     *
     * */
    public static ProductInfo getXiaoMiProductInfoById(String productId, List<ProductInfo> productList){
        List<ProductInfo> list = productList;
        for (int i=0; i<list.size(); i++){
            if(list.get(i).getProductId().equals(productId)){
                return list.get(i);
            }
        }
        MainUtils.showSimpleError("产品配置信息不存在");
        return null;
    }


    /**
     * 小米版本根据产品SN码获取小米打印模板名
     * */
    public static String getXiaoMiTemplate(String snCode, List<ProductInfo> productList){
//        ProductInfo productInfo = getXiaoMiProductInfoBySNcode(snCode, productList);
//        String printerTemplate = productInfo.getPrinterTemplate();
//        if (printerTemplate==null || printerTemplate.isEmpty()){
//            printerTemplate = "TP"+productInfo.getProductId();
//        }
//        return printerTemplate;
    	return null;
    }

    /**
     * 小米版本根据产品id码获取小米打印模板名
     * */
    public static String getXiaoMiTemplateByProductId(String productId, List<ProductInfo> productList) throws Exception{
        ProductInfo productInfo = getXiaoMiProductInfoById(productId, productList);
        String printerTemplate = productInfo.getPrinterTemplate();
        if (printerTemplate==null || printerTemplate.isEmpty()){
            throw new Exception(productId+"打印模板没设置");
        }
        return printerTemplate;
    }


    /**
     * textField 设置成只接受integer
     * */
    public static void numericOnly(final TextField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    /**
     * 将laserSql对应产品+中箱+栈板+出货信息(ProductDetailInfo)导出到Excel
     * @param laserSql 产品查询语句
     * @param primaryStage 窗口Stage
     * @param headName 表头名(中文)
     * @param fieldNameList 对应ProdetailInfo的字段名(英文)
     */
    public static void exportAll(final String laserSql,final Stage primaryStage,List<String> headName,List<String> fieldNameList) {
    	Integer count=null;
    	try {
			count=CommonAPI.getProductDetailCount(laserSql);
		} catch (BizException e) {
			MainUtils.showSimpleError(e.getMessage());
			return;
		}
    	if(count==null) {
    		MainUtils.showSimpleError("查询结果为空");
    		return;
    	}
    	FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导出Excel");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("xlsx","*.xlsx"));
		File selectedFile = fileChooser.showSaveDialog(primaryStage);
		if (selectedFile == null) return;
		Log.info("导出"+count+"条数据到:"+selectedFile.getPath());
		Log.info("临时文件目录:"+System.getProperty("java.io.tmpdir")+"poifiles");
		try (FileOutputStream fileOut = new FileOutputStream(selectedFile);
				SXSSFWorkbook workbook = new SXSSFWorkbook(-1);) {
			try {
				final int pageSize=10000;//每次最多查询pageSize条数据
				final int maxRowPerSheet=60000;
				final int maxQueryCountPerSheet=(maxRowPerSheet-1)/pageSize+1;//每张sheet查询次数
				int pageIndex=0;
				int sheetIndex=1;
				final Map<String, PropertyDescriptor> map = ModelUtil.getPropMap(ProductDetailInfo.class);
				sheetLoop:while(true) {
					final SXSSFSheet spreadsheet = workbook.createSheet("表"+sheetIndex);
					Row row = spreadsheet.createRow(0);
					for (int j = 0; j < headName.size(); j++) {
						row.createCell(j).setCellValue(headName.get(j));
					}
					int rowIdx=1;//sheet表行
					for(int j=0;j<maxQueryCountPerSheet;j++) {
						int offset=pageSize*pageIndex;
						List<ProductDetailInfo> list=CommonAPI.getProductDetail(laserSql, pageSize, offset);					
						pageIndex++;
						if(list==null||list.isEmpty()) {//已查完所有数据
							break sheetLoop;
						}					
						for(ProductDetailInfo info:list) {						
							row = spreadsheet.createRow(rowIdx++);
							for (int i = 0; i < fieldNameList.size(); i++) {
								String fieldName = fieldNameList.get(i);
								PropertyDescriptor p = map.get(fieldName);
								if (p == null) {
									Log.warn("通用查询缺少表格字段:" + fieldName);
									continue;
								}						
								Object rs = ModelUtil.readValue(p, info);
								String fieldValue = rs == null ? "" : rs.toString();
								row.createCell(i).setCellValue(fieldValue);
							}						
						}
						spreadsheet.flushRows();//将所有行数据缓存到临时文件并释放内存					
					}
					sheetIndex++;//新建一张sheet
				}
				MainUtils.showSimpleSuccess("导出完毕");
			}catch (BizException e) {
				Log.error("通用查询导出异常", e);
				MainUtils.showSimpleError(e.getMessage());
				return;
			}finally {
				workbook.write(fileOut);
				workbook.dispose();	
			}			
		} catch (Throwable e) {
			Log.error("通用查询导出异常", e);
			MainUtils.showSimpleError("导出异常");
			return;
		}
    }
}

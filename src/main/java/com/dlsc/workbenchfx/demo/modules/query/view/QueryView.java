package com.dlsc.workbenchfx.demo.modules.query.view;

import com.cg.core.util.Log;
import com.cg.core.util.MathUtil;
import com.dlsc.workbenchfx.demo.modules.query.model.ProductDetailInfo;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.cg.core.gui.gridForm.GridField;
import com.cg.core.gui.gridForm.GridForm;
import com.cg.core.gui.gridForm.GridRow;
import com.cg.core.module.BizException;
import com.dlsc.workbenchfx.Workbench;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.ModelUtil;
import com.dlsc.workbenchfx.demo.api.CommonAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;

public class QueryView extends BorderPane {
	private GlobalConfig globalConfig=GlobalConfig.getInstance();
	private String version=globalConfig.getClientType();
    private TableView<ProductDetailInfo> table;
    private GridPane controls;
    private VBox ImportExport;
    private Button buttonExport;
    //private Button buttonImport;
    private VBox bindingInfo;
    private Label countryLabel;
    private Pagination pagination;
    private Stage primaryStage;
    /**
     * 查询请求响应超时时间(ms)
     */
    private final int queryRespTimeout=5000;
    /**
     * 查询按钮
     */
    private Button buttonQuery;        
    private final Integer pageSize = 16;
   

    ToggleGroup group;

    /**
     * 彩盒SN前缀
     */
    private TextField laserSNPrefix = createTextField("彩盒SN前缀"); //产品SN
    private TextField createTextField(String tip) {
    	TextField field=new TextField();
    	field.setPromptText(tip);    	
    	return field;
    }
    /**
     * 中箱SN前缀
     */
    private TextField boxSNPrefix = createTextField("中箱SN前缀"); //中箱标签
    /**
     * 栈板SN前缀
     */
    private TextField palletSNPrefix = createTextField("栈板SN前缀"); //栈板标签
    /**
     * SKU(小米)/产品料号(素士productPartNumber),小米的商品ID可以扫码录入SN码中查询
     */
    private TextField sku = createTextField(isXiaoMi()?"完整的SKU":"完整的产品料号"); //SKU/料号
    /**
     * 产品类型(素士productType)
     */
    private TextField productType = createTextField("完整的产品类型编码"); //产品类型
    /**
     * 出货订单号(shippingCode)
     */
    private TextField shippingCode = createTextField("完整的出货订单号"); //订单号
    private DatePicker createDatePicker(String tip) {
        String pattern = "yyyy-MM-dd";
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        DatePicker picker=new DatePicker();
        picker.setConverter(converter);
        picker.setPromptText(tip);    	
        return picker;
    }
    /**
     * 彩盒打印日期(起始)
     */
    private DatePicker laserCreateDateStart = createDatePicker("彩盒打印日期"); //生产开始日期
    /**
     * 彩盒打印日期(截止)
     */
    private DatePicker laserCreateDateEnd = createDatePicker("彩盒打印日期");  //生产截至日期
    /**
     * 中箱打印开始日期
     */
    private DatePicker boxCreateDateStart = createDatePicker("中箱打印日期");
    /**
     * 中箱打印截止日期
     */
    private DatePicker boxCreateDateEnd = createDatePicker("中箱打印日期");
    /**
     * 栈板打印开始日期
     */
    private DatePicker palletCreateDateStart = createDatePicker("栈板打印日期");
    /**
     * 栈板打印截止日期
     */
    private DatePicker palletCreateDateEnd = createDatePicker("栈板打印日期");
    /**
     * 彩盒打印是否已入库
     */
    private CheckBox isLaserUsed = new CheckBox("经过彩盒");
    /**
     * 称重数据是否已入库
     */
    private CheckBox isWeighted = new CheckBox("经过称重");
    /**
     * 中箱打印是否已入库
     */
    private CheckBox isBoxUsed = new CheckBox("经过中箱");
    /**
     * 栈板打印是否已入库
     */
    private CheckBox isPalletUsed = new CheckBox("经过栈板");
    /**
     * 出货订单是否已入库
     */
    private CheckBox isShipping = new CheckBox("经过出货");
    /**
     * 表格分页数据容器
     */
    private final ObservableList<ProductDetailInfo> tableItems=FXCollections.observableArrayList();   
    private final List<String> selectFields;
    private final List<String> selectFieldNames;    
    public QueryView() {
		if(isXiaoMi()) {
			selectFieldNames=Arrays.asList("产品SN码",
                    "SKU",
                    "69码",
                    "产品名称",
                    "产品型号",
                    "产品颜色",
                    "商品 id",
                    "生态链公司",
                    "工厂代码",
                    "生产时间",
                    "打印彩盒",
                    "称重(g)",
                    "中箱标签",
                    "装箱时间",
                    "栈板标签",
                    "装栈时间",
                    "出货单号",
                    "出货时间",                    
                    "创建者");
			selectFields=Arrays.asList(
                    "SNCode",
                    "SKU",
                    "code69",
                    "productName",
                    "productType",
                    "productColor",
                    "productId",
                    "ecologicalChain",
                    "factoryCode",
                    "createDate",
                    "used",
                    "weight",
                    "cartoonBoxCode",
                    "boxDate",
                    "palletCode",
                    "palletDate",
                    "shippingCode",
                    "shippingDate",                    
                    "creator");
		}else {
			selectFieldNames=Arrays.asList(
                    "产品SN码",
                    "产品料号",
                    "产品名称",
                    "产品型号",
                    "产品颜色",
                    "工厂代码",
                    "生产时间",
                    "打印彩盒",
                    "称重(g)",
                    "中箱标签",
                    "装箱时间",
                    "栈板标签",
                    "装栈时间",
                    "出货单号",
                    "出货时间",
                    "创建者",
                    "状态");
			selectFields=Arrays.asList(
                    "SNCode",
                    "productPartNumber",
                    "productName",
                    "productType",
                    "productColor",
                    "factoryCode",
                    "createDate",
                    "used",
                    "weight",
                    "cartoonBoxCode",
                    "boxDate",
                    "palletCode",
                    "palletDate",
                    "shippingCode",
                    "shippingDate",
                    "creator",
                    "status");
			
		}
	}    
    public void init(Workbench workbench){
        MainUtils.setWorkbench(workbench);
        getStyleClass().add("module-background"); 
        final String sheetUrl=getClass().getResource("/style.css").toExternalForm();
        getStylesheets().add(sheetUrl);
        getStyleClass().add("root-pane");
        ScrollPane scrollContent = new ScrollPane();
        scrollContent.setFitToWidth(true);
        scrollContent.getStyleClass().add("scroll-pane");
        scrollContent.getStyleClass().add("bordered");
        setCenter(scrollContent);

        VBox vInput = getGridPane();               
        // 分页界面
        pagination = new Pagination(1,0);       
        // 表格界面
        VBox tableBox = new VBox();
        tableBox.setStyle("-fx-padding: 5 5 0 5;");
           
        table = MainUtils.createTable(selectFieldNames,selectFields);                
        table.setItems(tableItems);  
        tableBox.getChildren().addAll(pagination,table);
        doQuery(true);
        VBox.setVgrow(pagination, Priority.ALWAYS );
        VBox.setVgrow(tableBox, Priority.ALWAYS );
        

        // 主界面
        VBox root = new VBox();
        root.getStyleClass().add("bordered");
        root.getChildren().addAll(vInput,tableBox);

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



        buttonQuery = new Button("查询");
        buttonQuery.setMaxWidth(Double.MAX_VALUE);
        buttonQuery.getStyleClass().add("save-button");

        VBox snBox = new VBox();
        snBox.setSpacing(10);
        snBox.setPrefWidth(200);
        snBox.getStyleClass().add("bordered");
        snBox.setPadding(new Insets(10));
        snBox.getChildren().addAll(buttonQuery);//,buttonRefresh);
        controls.add(snBox, 0, 1);


        ImportExport = new VBox();
        group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("        导出当前页");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        RadioButton rb2 = new RadioButton("        导出全部");
        rb2.setToggleGroup(group);

        buttonExport = new Button("导出Excel");
        buttonExport.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buttonExport, Priority.ALWAYS);

        ImportExport.setPadding(new Insets(10));
        ImportExport.getChildren().addAll(buttonExport,rb1,rb2);
        ImportExport.setSpacing(10);
        ImportExport.setPrefWidth(200);
        ImportExport.getStyleClass().add("bordered");
        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        controls.add(ImportExport, 0, 2);

        setLeft(controls);
        pagination.setPageFactory(this::createPage);
        buttonQuery.setOnAction(event -> doQuery(false));

        buttonExport.setOnAction(event -> {
            RadioButton rb = (RadioButton)group.getSelectedToggle();
            //Log.info("select: "+rb.getText());
            if (rb.getText().trim().equals("导出当前页")){
                MainUtils.exportExcel(primaryStage, table);
            }else{
               exportAll();            	
            }
        });      
    }
    private void doQuery(boolean isInit) {    	
    	final String laserSql=createLaserSql();
    	table.getItems().clear();//清空分页数据
    	this.lastLaserSql=null;//新的一轮查询
		try {
			int laserCount = CommonAPI.getProductDetailCount(laserSql,queryRespTimeout);
			Log.info("查询总数:"+laserCount);			
			MainUtils.showSimpleSuccess("查询结果共"+laserCount+"条");				
//			this.pageSize=MainUtils.getPageSize(table);//分页大小
			final int pageCount=(laserCount-1)/pageSize+1;//总页数,从1开始
			if(isInit) {
				if(pageCount==1) {
					Log.info("刷新分页首页");
					flushTable(0);//已有分页,直接刷新分页数据
				}else {
					Log.info("重新分页"+pageCount);
					pagination.setPageCount(pageCount);//重新创建分页
				}				
			}else {
				if(pageCount==pagination.getPageCount()) {//总页数不变
					if(pagination.getCurrentPageIndex()==0) {
						Log.info("刷新分页首页");
						flushTable(0);//已有分页，刷新分页数据
					}else {
						Log.info("跳到分页首页");
						pagination.setCurrentPageIndex(0);//跳回到第一页,页码变更自动触发分页查询	
					}				
				}else {//总页数不一样
					Log.info("重新分页"+pageCount);
					pagination.setPageCount(pageCount);//重置分页总数触发分页查询								
				}
			}										
		} catch (BizException e) {
			MainUtils.showSimpleError(e.getMessage());
			return;
		}
    }
    private void exportAll() {//导出全部只支持xlsx格式(Excel2007+)
    	String laserSql=createLaserSql();

        final Integer FormalEdition = 365;

        Integer days = 1; //兼容有证书但是没有写入缓存的客户端,默认这些客户端全部是试用授权.
        try{
            days= MainUtils.getDictInt("certificate", "days");
        }catch(Exception e){
            Log.error("",e);
            days = 1;
        }
        if (days==null) days = 1; //没有数据就默认是试用授权

        if(days<FormalEdition){
            MainUtils.showWarning("目前是试用版,只能导出200条请购买正版授权");
        }

    	Integer count=null;
    	try {
			count=CommonAPI.getProductDetailCount(laserSql,queryRespTimeout);
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
				int pageSize=10000;//每次最多查询pageSize条数据

				int maxRowPerSheet=60000;
                if(days<FormalEdition){
                    pageSize = 200;
                    maxRowPerSheet=200;
                }

				final int maxQueryCountPerSheet=(maxRowPerSheet-1)/pageSize+1;//每张sheet查询次数
				int pageIndex=0;
				int sheetIndex=1;
				final Map<String, PropertyDescriptor> map = ModelUtil.getPropMap(ProductDetailInfo.class);
				sheetLoop:while(true) {
					final SXSSFSheet spreadsheet = workbook.createSheet("表"+sheetIndex);
					Row row = spreadsheet.createRow(0);
					for (int j = 0; j < table.getColumns().size(); j++) {
						row.createCell(j).setCellValue(table.getColumns().get(j).getText());
					}
					int rowIdx=1;//sheet表行
					for(int j=0;j<maxQueryCountPerSheet;j++) {
						int offset=pageSize*pageIndex;
						List<ProductDetailInfo> list=CommonAPI.getProductDetail(laserSql, pageSize, offset,queryRespTimeout);					
						pageIndex++;
						if(list==null||list.isEmpty()) {//已查完所有数据
							break sheetLoop;
						}					
						for(ProductDetailInfo info:list) {						
							row = spreadsheet.createRow(rowIdx++);
							for (int i = 0; i < table.getColumns().size(); i++) {
								String fieldName = table.getColumns().get(i).getId();
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
                    if(days<FormalEdition) break sheetLoop;

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
    private String getDateText(DatePicker picker) {
    	String text=picker.getEditor().getText();
    	if(text==null||text.isBlank())return "";
        return text;
    	//String[] arr=text.split("/");
    	//if(arr.length!=3) {
    	//	MainUtils.showSimpleError("日期格式错误:"+text);
    	//	return "";
    	//}
    	//Integer year=MathUtil.toInteger(arr[0]);
    	//if(year==null) {
    	//	MainUtils.showSimpleError("日期格式错误:"+text);
    	//	return "";
    	//}
    	//Integer month=MathUtil.toInteger(arr[1]);
    	//if(month==null) {
    	//	MainUtils.showSimpleError("日期格式错误:"+text);
    	//	return "";
    	//}
    	//Integer day=MathUtil.toInteger(arr[2]);
    	//if(day==null) {
    	//	MainUtils.showSimpleError("日期格式错误:"+text);
    	//	return "";
    	//}
    	//return String.format("%04d-%02d-%02d", year,month,day);	    	
    }
    private void fillDate(StringBuilder laserSql,DatePicker picker,boolean isStart,String alias) {
    	String text=getDateText(picker);
    	if(text.isEmpty())return;
    	if(isStart) {
    		laserSql.append(" and ").append(alias).append(".create_date>='").append(text).append(" 00:00:00'");
    	}else {
    		laserSql.append(" and ").append(alias).append(".create_date<='").append(text).append(" 23:59:59'");
    	}
    }
    /**
     * 
     * @return 产品查询条件
     */
    private String createLaserSql() {
    	StringBuilder laserSql=new StringBuilder(64);
    	final boolean isXiaoMi=isXiaoMi();
    	//1.计算产品查询条件
    	laserSql.append("from product_laser as s where 1");
    	if(!isEmptyValue(laserSNPrefix.getText())) {
    		laserSql.append(" and s.SNCode like '").append(laserSNPrefix.getText().trim()).append("%'");
    	}
    	if(!isEmptyValue(sku.getText())) {
    		if(isXiaoMi) {//小米SKU
    			laserSql.append(" and s.sku='").append(sku.getText().trim()).append("'");
    		}else {//素士product_part_number
    			laserSql.append(" and s.product_part_number='").append(sku.getText().trim()).append("'");
    		}
    	}
    	if(!isEmptyValue(productType.getText())) {//产品类型
    		laserSql.append(" and s.product_type='").append(productType.getText().trim()).append("'");
    	}
    	fillDate(laserSql, laserCreateDateStart, true,"s");
    	fillDate(laserSql, laserCreateDateEnd, false,"s");    	
    	if(isLaserUsed.isSelected()) {//彩盒已打印
    		laserSql.append(" and s.used='是'");
    	}
    	if(isWeighted.isSelected()) {//彩盒已称重
    		laserSql.append(" and s.weight is not null and s.weight<>''");
    	}
    	final String boxSN=boxSNPrefix.getText();
    	final String palletSN=palletSNPrefix.getText();
    	final String boxStartDate=getDateText(boxCreateDateStart);
    	final String boxEndDate=getDateText(boxCreateDateEnd);
    	final String palletStartDate=getDateText(palletCreateDateStart);
    	final String palletEndDate=getDateText(palletCreateDateEnd);
    	final String shippingCodeText=shippingCode.getText();//出货单号
    	boolean queryShipping=isShipping.isSelected()||!isEmptyValue(shippingCodeText);
    	boolean queryPallet=isPalletUsed.isSelected()||!isEmptyValue(palletSN)||!isEmptyValue(palletStartDate)||!isEmptyValue(palletEndDate);
    	boolean queryBox=queryShipping||queryPallet||isBoxUsed.isSelected()||!isEmptyValue(boxSN)||!isEmptyValue(boxStartDate)||!isEmptyValue(boxEndDate);
    	if(queryBox) {//中箱没有used字段，中箱装箱完成等价于中箱标签已打印
//    		laserSql.append(" and s.cartoon_box_code is not null and s.cartoon_box_code <>''");
    		if(!isEmptyValue(boxSN)) {
    			laserSql.append(" and s.cartoon_box_code like '").append(boxSN).append("%'");
    		}
    		laserSql.append(" and s.cartoon_box_code in (select nick_name from cartoon_box as c where 1");    		
    		fillDate(laserSql, boxCreateDateStart, true,"c");
    		fillDate(laserSql, boxCreateDateEnd, false,"c");    		
    		if(queryPallet) {//栈板没有used字段，栈板装栈完成等价于栈板标签已打印
//        		laserSql.append(" and s.cartoon_box_code in(select nick_name from cartoon_box where pallet_code is not null and pallet_code<>'') ");
    			if(!isEmptyValue(palletSN)) {
        			laserSql.append(" and c.pallet_code like '").append(palletSN).append("%'");
        		}
    			laserSql.append(" and c.pallet_code in (select nick_name from pallet as p where 1");
        		
        		fillDate(laserSql, palletCreateDateStart, true,"p");
        		fillDate(laserSql, palletCreateDateEnd, false,"p");
        		laserSql.append(")");
        	}
    		laserSql.append(")");
    	}    
    	if(queryShipping) {//已出货    		
    		if(isEmptyValue(shippingCodeText)) {//经过出货但未指定出货单号
    			laserSql.append(" and s.shipping_code is not null and s.shipping_code <>'' ");
    		}else {//经过出货且指定出货单号
    			laserSql.append(" and s.shipping_code='").append(shippingCodeText).append("'");
    		}
    	}
    	return laserSql.toString();
    }
    private String lastLaserSql=null;
    /**
     * 
     * @param index 分页页码,从0开始
     */
    private void flushTable(int index) {
    	table.getItems().clear();
        final String laserSql=createLaserSql();
        if(lastLaserSql!=null&&!lastLaserSql.equals(laserSql)) {
        	Log.debug("查询条件已更新，正在重新分页");
        	MainUtils.showSimpleSuccess("查询条件已更新，正在重新分页");
        	doQuery(false);
        	return;
        }
        this.lastLaserSql=laserSql;
        try {     			
//			this.pageSize=MainUtils.getPageSize(table);	
			final int pageIndex=this.pageSize*index;
			List<ProductDetailInfo> list=CommonAPI.getProductDetail(laserSql, this.pageSize, pageIndex,queryRespTimeout);	
			Log.info("分页大小:"+list.size());
			if(list!=null&&!list.isEmpty()) {				
				table.getItems().addAll(list);				
			}			
		} catch (BizException e1) {
			MainUtils.showSimpleError(e1.getMessage());
			return;
		}       
    }
    private boolean isEmptyValue(String value) {
    	return value==null||value.isBlank();
    }
    public VBox createPage(int pageIndex) {
    	Log.info("分压器自动刷新分页"+(pageIndex+1));
    	flushTable(pageIndex);  	 	
    	return new VBox(table);
    }    

    private VBox getGridPane(){
        laserCreateDateStart.setPrefWidth(200);
        laserCreateDateStart.setMinWidth(130);
        laserCreateDateEnd.setPrefWidth(200);
        laserCreateDateEnd.setMinWidth(130);
        boxCreateDateStart.setPrefWidth(200);
        boxCreateDateStart.setMinWidth(130);
        boxCreateDateEnd.setPrefWidth(200);
        boxCreateDateEnd.setMinWidth(130);
        palletCreateDateStart.setPrefWidth(200);
        palletCreateDateStart.setMinWidth(130);
        palletCreateDateEnd.setPrefWidth(200);
        palletCreateDateEnd.setMinWidth(130);

        Label labelProductSN = new Label("产品SN码");
        labelProductSN.setMinWidth(70);

        VBox vbox = GridForm.of(
                GridRow.of(
                    GridField.of(labelProductSN).span(8),
                    GridField.of(laserSNPrefix).span(22),
                    GridField.of(new Label("")).span(3),
                    GridField.of(new Label("中箱标签")).span(8),
                    GridField.of(boxSNPrefix).span(22),
                    GridField.of(new Label("")).span(3),
                    GridField.of(new Label("栈板标签")).span(8),
                    GridField.of(palletSNPrefix).span(22)
                    ),
                GridRow.of(
                        GridField.of(new Label("SKU/料号")).span(8),
                        GridField.of(sku).span(22),
                        GridField.of(new Label("")).span(3),
                        GridField.of(new Label("产品型号")).span(8),
                        GridField.of(productType).span(22),
                        GridField.of(new Label("")).span(3),
                        GridField.of(new Label("订单号")).span(8),
                        GridField.of(shippingCode).span(22)
                ) ,
                GridRow.of(
                        GridField.of(new Label("生产工序")).span(5),
                        GridField.of(new Label("")).span(3),
                        GridField.of(isLaserUsed).span(8),
                        GridField.of(isWeighted).span(8),
                        GridField.of(isBoxUsed).span(8),
                        GridField.of(isPalletUsed).span(8),
                        GridField.of(isShipping).span(8)
                    ),
                GridRow.of(
                        GridField.of(new Label("生产日期")).span(5),
                        GridField.of(new Label("")).span(1),
                        GridField.of(laserCreateDateStart).span(11),
                        GridField.of(new Label("")).span(1),
                        GridField.of(new Label("至")).span(2),
                        GridField.of(laserCreateDateEnd).span(11),
                        GridField.of(new Label("")).span(3),
                        GridField.of(new Label("装箱日期")).span(5),
                        GridField.of(new Label("")).span(1),
                        GridField.of(boxCreateDateStart).span(11),
                        GridField.of(new Label("")).span(1),
                        GridField.of(new Label("至")).span(2),
                        GridField.of(boxCreateDateEnd).span(11),
                        GridField.of(new Label("")).span(3),
                        GridField.of(new Label("装板日期")).span(5),
                        GridField.of(new Label("")).span(1),
                        GridField.of(palletCreateDateStart).span(11),
                        GridField.of(new Label("")).span(1),
                        GridField.of(new Label("至")).span(2),
                        GridField.of(palletCreateDateEnd).span(11)
                    )
                ); 

        vbox.getStyleClass().add("bordered_all");

        VBox vInput = new VBox();
        vInput.setStyle("-fx-padding: 5 5 5 5;");
        vInput.getChildren().add(vbox);
        vbox.setStyle("-fx-padding: 5 5 5 5;");

        return vInput;

    }
    private boolean isXiaoMi() {
    	return "xiaomi".equals(version);
    }
}

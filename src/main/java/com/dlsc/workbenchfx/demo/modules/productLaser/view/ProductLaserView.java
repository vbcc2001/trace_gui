package com.dlsc.workbenchfx.demo.modules.productLaser.view;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.demo.api.CounterAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.global.ProductInfoForm;

import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.cg.core.module.BizException;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.workbenchfx.Workbench;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Pagination;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.ModelUtil;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;




public class ProductLaserView extends BorderPane {
	private final GlobalConfig globalConfig=GlobalConfig.getInstance();
    public TableView<ProductLaser> table;
    private final ProductInfoForm productInfoForm=new ProductInfoForm(ProductInfoForm.LASER);
    private GridPane controls;
    private VBox ImportExport;
    private Button buttonExport;
    //private Button buttonImport;
    private VBox bindingInfo;
    private Label countryLabel;
    private Pagination pagination;
    private Stage primaryStage;      
    /**
     * 生成SN码按钮
     */
	public Button buttonSNCode;    
    private final Integer pageSize = 16;   
    public String version;
    /**
     * 总数据
     */
    private final List<ProductLaser> allData=new ArrayList<>();
    /**
     * 分页数据
     */
    private final ObservableList<ProductLaser> dataList=FXCollections.observableArrayList();
    ToggleGroup group;

    public ProductLaserView(String creator) {
        
    }

    public void init(Workbench workbench){
        MainUtils.setWorkbench(workbench);

        getStyleClass().add("module-background");
        getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        getStyleClass().add("root-pane");

        //BorderPane scrollContent = new BorderPane();
        ScrollPane scrollContent = new ScrollPane();
        scrollContent.setFitToWidth(true);
        //scrollContent.setFitToHeight(true);
        scrollContent.getStyleClass().add("scroll-pane");
        scrollContent.getStyleClass().add("bordered");
        
        setCenter(scrollContent);
        
        version = globalConfig.getClientType();

        Form formInstance = productInfoForm.getFormInstanceByConfig(version);        
        FormRenderer formRenderer = new FormRenderer(formInstance);



        if (version.equals("sushi")){
        table = MainUtils.createTable(Arrays.asList("产品SN码","产品料号","产品名称","产品型号","产品颜色","工厂代码","创建者","创建时间","状态"),
                Arrays.asList("SNCode","productPartNumber","productName","productType","productColor","factoryCode","creator","createDate","status"));
        }

        if (version.equals("xiaomi")){
        table = MainUtils.createTable(Arrays.asList("产品SN码","产品颜色","商品 id","生态链公司","工厂代码","创建时间","创建者"),
                Arrays.asList("SNCode","productColor","productId","ecologicalChain","factoryCode","createDate","creator"));
        }               
        VBox root = new VBox();

        VBox tableBox = new VBox();
        //tableBox.prefHeight(900);
        tableBox.setStyle("-fx-padding: 0 20 0 20;");
        root.getStyleClass().add("bordered");     
        pagination = new Pagination(1,0);
        tableBox.getChildren().add(pagination);


        VBox.setVgrow(pagination, Priority.ALWAYS );
        VBox.setVgrow(tableBox, Priority.ALWAYS );


        pagination.setPageFactory(this::createPage);
        tableBox.getChildren().add(table);     
        table.setItems(dataList);
        root.getChildren().addAll(formRenderer,tableBox);

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

        buttonSNCode = new Button("生成SN码");
        buttonSNCode.setMaxWidth(Double.MAX_VALUE);
        buttonSNCode.getStyleClass().add("save-button");   

        snBox.setSpacing(10);
        snBox.setPrefWidth(200);
        snBox.getStyleClass().add("bordered");
        snBox.setPadding(new Insets(10));
        snBox.getChildren().addAll(buttonSNCode);
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

        buttonSNCode.setOnAction(event -> {
            formInstance.persist();
            //检验工厂代码格式
            allData.clear();            
            final ProductLaser laser=productInfoForm.createLaser();
            if(laser==null) {
            	MainUtils.showSimpleError("请先选择产品模板");
            	return;
            }
            String partSNCode=null;
        	try {
				partSNCode=laser.toSNPrefix(version);
				Log.info("产品前缀:"+partSNCode);
			} catch (BizException e) {
				 MainUtils.showSimpleError(e.getMessage());
                 return;
			}
            productInfoForm.cacheInput();//缓存录入结果
            Integer count=productInfoForm.getProductCount();
            if(count==null||count<0) {
            	MainUtils.showSimpleError("请先设置产品数量");
            	return;
            }
            try {
            	List<Integer> streamCodeList=CounterAPI.getNum(partSNCode, 0, true, count);//产品日流水
            	final int end=streamCodeList.get(1);
            	String endStreamCode=String.format("%05d", end);
            	if(endStreamCode.length()>5) {
            		MainUtils.showSimpleError("产品日流水已溢出");
                	return;
            	}
            	int start=streamCodeList.get(0);
            	final String createDate=productInfoForm.getCreateDate();
            	for(int i=start;i<=end;i++) {  
            		String streamCode=String.format("%05d", i);
            		ProductLaser p=ModelUtil.copyModel(laser, new ProductLaser());
            		p.setStreamCode(streamCode);
            		p.setSNCode(partSNCode+streamCode);
            		p.setCreator(globalConfig.getCurrentUser().getUserName());
            		p.setCreateDate(createDate);
            		allData.add(p);            		
            	}
            }catch (BizException e) {
            	MainUtils.showSimpleError(e.getMessage());
            	return;
			}
            refreshTableView();
        });

        buttonExport.setOnAction(event -> {
            RadioButton rb = (RadioButton)group.getSelectedToggle();
            //Log.info("select: "+rb.getText());
            if (rb.getText().trim().equals("导出当前页")){
                MainUtils.exportExcel(primaryStage, table);
            }else{//导出全部数据               
                try{
                    formInstance.persist();                                                
                    ObservableList<ProductLaser> list = FXCollections.observableArrayList();
                    list.addAll(allData);                    
                    MainUtils.exportExcelAll(primaryStage, table, list);
                }catch(Exception e){
                    Log.error("",e);
                }
            }
        });
    }

    private Node createPage(int pageIndex) {    	
    	return new VBox(updateTbale(pageIndex));
    }
    private TableView<ProductLaser> updateTbale(int pageIndex) {
    	dataList.clear();
    	int pageCount=MainUtils.getPageCount(table, allData.size());    	
    	pagination.setPageCount(pageCount);
		pagination.setCurrentPageIndex(pageIndex);		
		int start=pageSize*pageIndex;
		int end=start+pageSize;
		if(end>allData.size())end=allData.size();
		for(int i=start;i<end;i++) {
			dataList.add(allData.get(i));
		}		
    	table.setItems(dataList);
    	return table;
    }
    private void refreshTableView() {    	
    	updateTbale(0).refresh();
    }
}

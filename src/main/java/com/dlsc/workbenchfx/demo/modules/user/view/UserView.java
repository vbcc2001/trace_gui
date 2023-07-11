package com.dlsc.workbenchfx.demo.modules.user.view;


import java.util.Arrays;
import com.alibaba.fastjson2.JSON;
import com.dlsc.formsfx.model.structure.BooleanField;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.ModelUtil;
import com.dlsc.workbenchfx.demo.api.ServerAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.user.model.UserInfo;
import com.dlsc.workbenchfx.demo.modules.user.model.UserInfoAPI;
import com.dlsc.workbenchfx.model.WorkbenchDialog;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.cg.core.gui.FormUtils;
import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;


public class UserView extends BorderPane {
	private final GlobalConfig globalConfig=GlobalConfig.getInstance();
    private GridPane controls;
    private VBox crudContent;
    private Button buttonAdd;
    private Button buttonDel;
    private Button buttonUpdate;
    private VBox bindingInfo;
    private Label countryLabel;
    public Pagination pagination;    
    public TableView<UserInfo> table=null;    
    public String version;

    public void init(Workbench workbench){
        MainUtils.setWorkbench(workbench);
        version = globalConfig.getClientType();

        getStyleClass().add("module-background");
        getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        getStyleClass().add("root-pane");

        table = MainUtils.createTable(Arrays.asList("用户名","用户密码","所在组","产品权限","镭射权限","彩盒权限","称重权限","中箱权限","栈板权限","出货权限","是否冻结"),
                Arrays.asList("userName","userPasswd","mygroup","product","laser","colorBox","weight","cartoonBox","pallet","shipping","frozen"));
        ObservableList<TableColumn<UserInfo, ?>> columns=table.getColumns();
        @SuppressWarnings("unchecked")
		TableColumn<UserInfo, String> col_frozen=(TableColumn<UserInfo, String>) columns.get(columns.size()-1);
        col_frozen.setCellValueFactory(param->{
        	UserInfo value=param.getValue();
        	return new ReadOnlyObjectWrapper<>(value.isFrozen()?"是":"否");        	
        });       
        VBox tableBox = new VBox();
        tableBox.setStyle("-fx-padding: 20 20 0 20;");
        pagination = new Pagination(MainUtils.getPageCount(table, globalConfig.getAllUsers().size()), 0);
        //pagination = new Pagination(5, 0);
        VBox.setVgrow(pagination, Priority.ALWAYS );
        tableBox.getChildren().add(pagination);
        pagination.setPageFactory((Integer pageIndex) -> {
            return createPage(table,pageIndex);
        });
        setCenter(tableBox);
        //
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
        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        //controls.add(ImportExport, 0, 2);
        setLeft(controls);


        //this.workbench = workbench;
        ButtonType okay = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);


        buttonAdd.setOnAction(event -> {

            Form form = createFormByVersion();

            FormRenderer renderer = new FormRenderer(form);
            MainUtils.searchAndSetControlsLabelWidth(renderer, 40);

            // Building the dialog with the CheckBox as content
            WorkbenchDialog dialog = WorkbenchDialog.builder(
                    "新增用户", renderer, okay, cancel)
                .blocking(true)
                //.maximized(true)
                .showButtonsBar(true)
                .onResult(buttonType -> {
                    if (buttonType.equals(okay)) {
                        form.persist();                       
                        String text = FormUtils.form2JsonStr(form);
                        UserInfo user = JSON.parseObject(text, UserInfo.class);
                        try {
							UserInfo rs=UserInfoAPI.addUser(user);
							globalConfig.addUser(rs);//刷新内存
							refreshTableView();//刷新列表
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

        buttonDel.setOnAction(event -> {
            UserInfo item = table.getSelectionModel().getSelectedItem();
            if (item == null){
                WorkbenchDialog dialog = WorkbenchDialog.builder(
                        "删除用户失败", "请您先选择需要删除的用户", okay)
                    .showButtonsBar(true)
                    .onResult(buttonType -> {
                    })
                .build();
                workbench.showDialog(dialog);
            }else if(item.isFrozen()) {
            	MainUtils.showSimpleSuccess("用户"+item.getUserName()+"已删除,不要重复删除");
            	return;
            }else{
                String delMessage = "";
                delMessage = "用户: "+item.getUserName();
                WorkbenchDialog dialog = WorkbenchDialog.builder(
                        "删除用户", delMessage, okay, cancel)
                    .showButtonsBar(true)
                    .onResult(buttonType -> {
                        if (buttonType.equals(okay)) {
                            UserInfo userInfo = table.getSelectionModel().getSelectedItem();
                            userInfo.setFrozen(true);
                            UserInfo rs=null;
                            try {
                            	rs=UserInfoAPI.updateUser(userInfo);                            	
							} catch (RequestException e) {
								MainUtils.showSimpleError(ServerAPI.toBizException(null, e).getMessage());
								return;
							} catch (BizException e) {
								MainUtils.showSimpleError(e.getMessage());
								return;
							}
                            if(rs==null) {
                            	Log.warn("frozen:"+userInfo+" return null");
                            }else {
                            	ModelUtil.copyModel(rs, userInfo);//刷新列表
                            	refreshTableView();
                            }
                        }
                    })
                .build();
                workbench.showDialog(dialog);
            }
        });

        buttonUpdate.setOnAction(event -> {
            // Building the dialog with the CheckBox as content
            UserInfo item = table.getSelectionModel().getSelectedItem();
            if (item == null){
                WorkbenchDialog dialog = WorkbenchDialog.builder(
                        "更新用户失败", "请您先选择需要更新的用户", okay)
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
                        "修改用户", rendererUpdate, okay, cancel)
                    .showButtonsBar(true)
                    .onResult(buttonType -> {
                        if (buttonType.equals(okay)) {
                            formUpdate.persist();                           
                            //String text = MainUtils.form2JsonStr(formUpdate, dict);
                            String text = FormUtils.form2JsonStr(formUpdate);
                            UserInfo userInfo = JSON.parseObject(text,UserInfo.class);
                            userInfo.setId(item.getId());
                            UserInfo rs=null;
                            try {
                            	rs=UserInfoAPI.updateUser(userInfo);                            	
							} catch (RequestException e) {
								MainUtils.showSimpleError(ServerAPI.toBizException(null, e).getMessage());
								return;
							} catch (BizException e) {
								MainUtils.showSimpleError(e.getMessage());
								return;
							}
                            if(rs==null) {
                            	Log.warn("update:"+userInfo+" return null");
                            }else {
                            	UserInfo origin=globalConfig.getUser(item.getUserName());
                            	ModelUtil.copyModel(rs, origin);//刷新列表
                            	refreshTableView();
                            }
                        }
                    })
                .build();
                dialog.setOnShown(event1 -> {
                    dialog.getButton(okay).ifPresent(
                            button -> {
                                button.disableProperty().bind(formUpdate.persistableProperty().not());
                            });
                });
                workbench.showDialog(dialog);
            }
        });
    } 

    public TableView<UserInfo> createPage(TableView<UserInfo> table,int pageIndex) {
        table.setItems(globalConfig.getAllUsers());
        return table;
    }

    public Form createFormByVersion(){
        Form form = null;
        StringProperty userName = new SimpleStringProperty("");
        StringProperty userPasswd = new SimpleStringProperty("");
        SingleSelectionField<String> group = Field.ofSingleSelectionType(Arrays.asList("生产单位","工程单位"), 0);

        SingleSelectionField<String> product = Field.ofSingleSelectionType(Arrays.asList("是","否"), 1);
        SingleSelectionField<String> laser = Field.ofSingleSelectionType(Arrays.asList("是","否"), 1);
        SingleSelectionField<String> colorBox = Field.ofSingleSelectionType(Arrays.asList("是","否"), 1);
        SingleSelectionField<String> weight = Field.ofSingleSelectionType(Arrays.asList("是","否"), 1);
        SingleSelectionField<String> cartoonBox = Field.ofSingleSelectionType(Arrays.asList("是","否"), 1);
        SingleSelectionField<String> pallet = Field.ofSingleSelectionType(Arrays.asList("是","否"), 1);
        SingleSelectionField<String> shipping = Field.ofSingleSelectionType(Arrays.asList("是","否"), 1);
        BooleanField isFrozen = BooleanField.ofBooleanType(new SimpleBooleanProperty(false));

        form = Form.of(
                Group.of(
                    Field.ofStringType(userName)
                    .id("userName")
                    .label("用户名")
                    .required("必填"),
                    Field.ofStringType(userPasswd)
                    .id("userPasswd")
                    .label("密码")
                    .required("必填"),
                    group
                    .label("所在组")
                    .id("mygroup"),

                    product
                    .label("产品权限")
                    .id("product"),
                    laser
                    .label("镭射权限")
                    .id("laser"),

                    colorBox
                    .label("彩盒权限")
                    .id("colorBox"),
                    weight
                    .label("称重权限")
                    .id("weight"),
                    cartoonBox
                    .label("中箱权限")
                    .id("cartoonBox"),
                    pallet
                    .label("栈板权限")
                    .id("pallet"),
                    shipping
                    .label("出货权限")
                    .id("shipping"),
                    isFrozen.label("是否冻结")
                    .id("frozen")
                    ));
        return form;
    }


	public Form updateFormByVersion(UserInfo item,String version){
        Form form = createFormByVersion();
        String text = JSON.toJSONString(globalConfig.getUser(item.getUserName()));
        FormUtils.json2Form(form,text);

        return form;
    }

    /**
     * 刷新表格列表
     */
    public void refreshTableView() {
    	if(table!=null)table.refresh();
    	if(pagination!=null) {
    		pagination.setPageCount(MainUtils.getPageCount(table, globalConfig.getAllUsers().size()));
    		pagination.setCurrentPageIndex(0);
    	}
    }   
}



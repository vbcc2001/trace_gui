package com.dlsc.workbenchfx.demo;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.module.SimpleElement;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.preferences.Preferences;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import com.dlsc.workbenchfx.view.controls.module.Tile;

import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import com.dlsc.workbenchfx.demo.modules.productInfo.ProductInfoModule;
import com.dlsc.workbenchfx.demo.modules.productLaser.ProductLaserModule;
import com.dlsc.workbenchfx.demo.modules.query.QueryModule;
import com.dlsc.workbenchfx.demo.modules.shipping.ShippingModule;
import com.dlsc.workbenchfx.demo.modules.user.UserModule;
import com.dlsc.workbenchfx.demo.modules.user.model.UserInfo;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.CartoonBoxSNModule;
import com.dlsc.workbenchfx.demo.modules.colorBoxSN.ColorBoxSNModule;
import com.dlsc.workbenchfx.demo.modules.palletPost.PalletPostModule;
import com.dlsc.workbenchfx.demo.modules.palletSN.PalletSNModule;
import com.dlsc.workbenchfx.demo.modules.boxPost.BoxPostModule;
import com.dlsc.workbenchfx.demo.modules.weighBox.WeighBoxModule;
import com.dlsc.workbenchfx.demo.modules.weightAndBox.WeightAndBoxModule;
import com.dlsc.workbenchfx.demo.modules.preferences.PreferencesModule;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class ExtendedDemo extends Application {  
    public static ToolbarItem nightly = null;
	private static String homeDir;
	private static String basePath;
    static {
    	String homeDir=getHomeDir();    	
    	String logHome=isClientMod()?homeDir:homeDir+File.separator+"target";    	
        System.setProperty("trace_home", logHome);
    }
    public static String getBasePath(Class<?> clz) {
    	if(basePath==null) {
    		basePath=clz.getResource("").getFile();
            Pattern pt=Pattern.compile("file:/(.*)/.*\\.jar!/.*");
            Matcher	 m=pt.matcher(basePath);
            while(m.find()) {
                return basePath=m.group(1);
            }
    	}
        return basePath;
    }
    public static boolean isClientMod() {
    	return "1".equals(System.getProperty("client"));
    }
    public static String getHomeDir() {
    	if(homeDir==null) {
    		try {
	    		String basePath = getBasePath(ExtendedDemo.class);
	    		System.out.println("basePath:"+basePath);
	    		File root=new File(basePath);
	    		if(!root.exists()) {
	    			homeDir=basePath;
	    		}else {
	    			if(isClientMod()) {//traceSys/app
	    				homeDir=root.getParent();
	    			}else {//trace-gui/target/classes
	    				homeDir=root.getParentFile().getParent();
	    			}
	    		}
	    		//fix: windows系统不支持new URI("file:C:/xx")格式
//	    		Path path = Paths.get(new URI("file://" + basePath));
//	    		if(isClientMod()) {
//	    			path = path.resolve("../");
//	    		}else {
//	    			path = path.resolve("../../");
//	    		}
//	    		basePath = path.normalize().toString();
//	    		homeDir=basePath;
    		}catch (Exception e) {
				Log.error("",e);
			}
    	}
		return homeDir;
	}

    private Workbench workbench;
    public String dir = FileUtils.getUserDirectoryPath();
    /**
     * 全局缓存,单例模式
     */
    private final GlobalConfig globalConfig=GlobalConfig.getInstance();  

    public static void main(String[] args) throws FileNotFoundException {
        launch(args);    
    }

    private long exPiredDays = 0;

    private Integer userInfoVerify(String decryptedString,String serialNumber){
        // 0 正确
        // 1 过期
        // 2 错误
        String deviceString = decryptedString.substring(0, 32);
        String usefulStr = decryptedString.substring(32);
        String endDate = usefulStr.substring(0, 8);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate localEndDate = LocalDate.parse(endDate, formatter);
        long days = MainUtils.localDateCompare(localEndDate);
        Log.info("剩余授权天数: "+days);
        if(newCertificate){
            MainUtils.addOrUpdateLocalCache("certificate", "days", ""+days);
        }
        exPiredDays = days;
        if (days<=0){
            return 1;
        }
        String guessedCpuId = usefulStr.substring(8);
        //Log.info("guessedCpuId:"+guessedCpuId);
        //Log.info("deviceString:"+deviceString);
        String guessedSerialNum = deviceString + guessedCpuId;
        if (!guessedSerialNum.equals(serialNumber)){
            return 2;
        }
        return 0;
    }

    private boolean isGenuine(String decryptedString,String serialNumber){
        // 0 正确
        // 1 过期
        // 2 错误
//        String deviceString = decryptedString.substring(0, 32);
        String usefulStr = decryptedString.substring(32);
        String endDate = usefulStr.substring(0, 8);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate localEndDate = LocalDate.parse(endDate, formatter);
        long days = MainUtils.localDateCompare(localEndDate);
        Log.info("授权天数: "+days);
        if (days>365){ //超过1年的客户约定是正版付费用户
            return true;
        }
        return false;
    }
    
    @Override
	public void stop() throws Exception {
		Log.info("正在退出系统");
		System.exit(0);
	}
	public void start(Stage primaryStage) {		
    	Thread.currentThread().setUncaughtExceptionHandler((thread,throwable)->{ 
    		Log.error("global exception", throwable); 
    	});
    	App.getApp().setController(this, primaryStage);
        String cpuId = SystemInfoUtils.getComputerInfo();
        File f = new File(dir + "/crt.txt");
        File deviceIdObj = new File(dir+"/deviceId.txt");
        if(f.exists() && !f.isDirectory() && deviceIdObj.exists() && !deviceIdObj.isDirectory()) { 
            Path fileName = Path.of(dir+"/crt.txt");
            try{
                String userInfoEncryption = Files.readString(fileName);
                String deviceStr = Files.readString(Path.of(deviceIdObj.getPath()));
                String serialNumber = deviceStr + cpuId;
                String decryptedString = RSAUtil.decrypt(userInfoEncryption, RSAUtil.privateKey);
                //Log.info("解密信息: "+decryptedString);
                if (userInfoVerify(decryptedString, serialNumber)==1){
                    Certificate(primaryStage,"授权码过期!",false);
                }else if (userInfoVerify(decryptedString, serialNumber)==2){
                    Certificate(primaryStage,"授权码非法!",false);
                }else{//已授权                	
                	checkConfig(primaryStage,globalConfig.getClientType(),globalConfig.getServerUrl());
                }
            }catch(Exception exception){
            	Log.error("crt error",exception);
                Certificate(primaryStage,"授权码异常!",false);
            }
        }else{
            Certificate(primaryStage,null,false);
        }

    }   
    /**
     * 检查初始配置并自动跳到登录页面或者初始配置引导页面
     * @param ct 客户端类型
     * @param serverUrl 服务地址
     * @param isLogin 是登录或者切换系统
     */
    void checkConfig(Stage primaryStage,String ct,String serverUrl) {//检查初始配置  
    	String errMsg=globalConfig.checkClientTypeAndServer(ct, serverUrl,true);
    	if(errMsg==null) {
    		globalConfig.setServerUrl(serverUrl);
    		globalConfig.setClientType(ct);    		
    		login(primaryStage);
    	}else {
    		showBootstrapDialog(primaryStage,ct,serverUrl,errMsg);
    	}    	
    }  
    private void showBootstrapDialog(Stage primaryStage,String clientType,String serverUrl,String errMsg) {//显示配置引导框
    	primaryStage.hide();
    	primaryStage.setTitle("服务器配置");    		
		GridPane mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setHgap(10);         
        mainPane.getRowConstraints().add(new RowConstraints(50));
        RowConstraints otherRows=new RowConstraints(40);
        for(int i=1;i<6;i++) {        	
        	mainPane.getRowConstraints().add(otherRows);  
        }                
        final Text titleText = new Text("");	        
        titleText.setFont(Font.font("SimSun", FontWeight.BOLD, FontPosture.REGULAR, 28));
        mainPane.add(titleText, 0, 0, 2, 1);        	        
        Label ctLabel = new Label("服务器版本:");
        ctLabel.setFont(Font.font("SimSun", FontWeight.BOLD, FontPosture.REGULAR, 14));
        mainPane.add(ctLabel, 0, 1,1,1);
        ChoiceBox<SimpleElement> ct=new ChoiceBox<>();
        //ct.setPrefWidth(220);
        ct.getItems().addAll(globalConfig.getAllClientType());
        if(globalConfig.getClientName(clientType)!=null) {
        	for(SimpleElement se:ct.getItems()) {
        		if(se.getId().equals(clientType)) {
        			ct.getSelectionModel().select(se);
        			break;
        		}
        	}
        }
        mainPane.add(ct, 1, 1,1,1);       
        
        Label serverLabel = new Label("服务器地址:");
        serverLabel.setFont(Font.font("SimSun", FontWeight.BOLD, FontPosture.REGULAR, 14));
        mainPane.add(serverLabel, 0, 2,1,1);
        TextField serverInput = new TextField();
        if(serverUrl!=null)serverInput.setText(serverUrl);
        //serverInput.setPrefWidth(220);
        mainPane.add(serverInput, 1, 2,1,1);
        final Text errInput = new Text();
        if(errMsg==null||errMsg.isBlank()) {
    		titleText.setText("首次使用，请先录入服务器地址！");
        	titleText.setFill(Color.BLACK);
    	}else {
    		titleText.setText("连接失败，请重录服务器地址！");
            titleText.setFill(Color.BLACK);
            errInput.setText(errMsg);
    	}	        
        //errInput.getStyleClass().add("errInput");
        errInput.setFont(Font.font("SimSun", FontWeight.LIGHT, FontPosture.REGULAR, 14));
        errInput.setFill(Color.RED);
        mainPane.add(errInput, 0, 3,2,1);
        
        Button okBtn = new Button("确定");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(okBtn);
                  
        mainPane.add(hbBtn, 0,4,2,1);   
        Scene bootstrapScene = new Scene(mainPane,600,400);
        bootstrapScene.getStylesheets().add(getClass().getResource("/css/bootstrap.css").toExternalForm());  
        ct.setOnAction(e->{
        	errInput.setText("");
        	serverInput.requestFocus();
        });
        serverInput.setOnAction(e->{
        	errInput.setText("");
        	okBtn.requestFocus();
        });
        okBtn.setOnAction(e->{
        	SimpleElement selectClient=ct.getSelectionModel().getSelectedItem();
        	if(selectClient==null) {
        		errInput.setText("请先选择系统版本");
        		ct.requestFocus();
        		return;
        	}        
        	String url=serverInput.getText();
        	if(url==null||url.isBlank()) {
        		errInput.setText("请先录入服务器地址");
        		serverInput.requestFocus();
        		return;
        	}
        	String ctCode=selectClient.getId();
        	checkConfig(primaryStage,ctCode, url);        	      	  
        });	    
        primaryStage.setScene(bootstrapScene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.centerOnScreen();
    	primaryStage.show();   	
    }
    private void login(Stage primaryStage){
    	primaryStage.hide();
        String ctId=globalConfig.getClientType();
        String ctName=globalConfig.getClientName(ctId);
        primaryStage.setTitle("登陆"+ctName+"追溯系统");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("欢迎使用"+ctName+"追溯系统!");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
        Label userName = new Label("用户名:");
        grid.add(userName, 0, 1);

        TextField userNameInput = new TextField();
        grid.add(userNameInput, 1, 1);       

        Label pw = new Label("密码:");
        grid.add(pw, 0, 2);

        //PasswordField pwBox = new PasswordField();
        TextField pwdInput = new PasswordField();
        grid.add(pwdInput, 1, 2);        
        Button loginBtn = new Button("登录");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(loginBtn); 
        grid.add(hbBtn, 1, 4);

        final Text errMsgInput = new Text();
        grid.add(errMsgInput, 1, 6);        
        final Consumer<String> errMsgConsumer=errMsg->{//错误信息回显
        	errMsgInput.setFill(Color.RED);
        	errMsgInput.setText(errMsg);
        	errMsgInput.setUserData(Boolean.TRUE);
        };
        final ChangeListener<String> inputChangeListener=(origin,oldVal,newVal)->{
        	if(newVal==null||newVal.isEmpty())
        		return;
        	if(errMsgInput.getUserData()==Boolean.TRUE) {//擦除错误信息
        		errMsgInput.setUserData(null);
        		errMsgInput.setText("");
        	}
        };
        globalConfig.clearUserInfo();//防止用户数据跨系统干扰
        userNameInput.textProperty().addListener(inputChangeListener);
        pwdInput.textProperty().addListener(inputChangeListener);
        grid.addEventHandler(ActionEvent.ACTION, e->{//登录逻辑        
        	if(e.isConsumed()) {
        		return;//防止重复响应事件
        	}
        	e.consume();
        	String user = userNameInput.getText();
        	if(user==null||user.isBlank()) {
        		errMsgConsumer.accept("用户名不能为空");
            	userNameInput.requestFocus();
            	return;
        	}
            String passwd = pwdInput.getText();
            if(passwd==null||passwd.isBlank()) {
            	errMsgConsumer.accept("密码不能为空");            	
            	pwdInput.requestFocus();
            	return;
            }
            try {
            	UserInfo info=globalConfig.findUser(user, passwd);
            	globalConfig.setCurrentUser(info);
            	globalConfig.persistInitConfig();            	
            	String nickName=info.getUserName();
            	Log.info(info.getUserName()+"登录成功");
            	//TODO 分系统后用户本地缓存应该按客户端类型分开控制
            	MainUtils.addOrUpdateLocalCache("system", "userName", user);
                MainUtils.addOrUpdateLocalCache("system", "nickName", nickName);
                //首次登录成功后，初始化默认配置             
                Log.info("initCompleted=" + MainUtils.getLocalCache("system","initCompleted"));
                if(MainUtils.getLocalCache("system","initCompleted")==null){
                    //应用设置缓存 (彩盒、称重)
                    MainUtils.addOrUpdateLocalCache("system", "colorBoxInputPermission","禁止录入");
                    MainUtils.addOrUpdateLocalCache("system", "weightPassed", "必须称重");
                    MainUtils.addOrUpdateLocalCache("system", "weightByHand", "自动称重");
                    //打印机设置（型号、DPI、浓度、是否斑马打印机），打印模板不初始化，用户手动选择
                    MainUtils.addOrUpdateLocalCache("system", "printer", "mock");
                    MainUtils.addOrUpdateLocalCache("system", "printerDPI", "300");
                    MainUtils.addOrUpdateLocalCache("system", "printerMD", "0");
                    //MainUtils.addOrUpdateDictionary("system", "printerTemplate", null);
                    MainUtils.addOrUpdateLocalCache("system", "zplMode", "true");
                    //服务器设置缓存不用初始化
                    //称重机设置缓存(端口、单位、最小度数、稳定因子)
                    MainUtils.addOrUpdateLocalCache("system", "weighPort", "COM1");
                    MainUtils.addOrUpdateLocalCache("system", "unit", "kg");
                    MainUtils.addOrUpdateLocalCache("system", "wtMinValue", "0.005");
                    MainUtils.addOrUpdateLocalCache("system", "weightN", "3");
                    //开发设置缓存（版本切换）
                    MainUtils.addOrUpdateLocalCache("system", "nightly", "正式版");
                    //默认配置初始化完成标志
                    MainUtils.addOrUpdateLocalCache("system", "initCompleted", "true");
                }
            }catch (BizException e1) {
            	errMsgConsumer.accept(e1.getMessage());
            	return;
			}
            try {
	        	 mainApp(primaryStage,ctName+"追溯系统",user,passwd);	        	 
            }catch (RequestException e2) {            	
            	errMsgConsumer.accept(e2.getMessage());
            	Log.error("初始化主界面异常",e2.getCause());
			}catch (Throwable t) {
				errMsgConsumer.accept(t.getMessage());
				Log.error("初始化主界面异常",t);
			}          
        });
        Scene loginScene = new Scene(grid, 600, 400);
        primaryStage.setScene(loginScene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.centerOnScreen();        
        UserInfo currentUser=globalConfig.getCurrentUser();
        if(currentUser!=null) {//切换版本
        	userNameInput.setText(currentUser.getUserName());
        	pwdInput.setText(currentUser.getUserPasswd());
        	grid.fireEvent(new ActionEvent());
        }else {
        	//记住用户名功能,将密码输入框设置焦点。
            if(MainUtils.getLocalCache("system","userName")!=null){
                userNameInput.setText(MainUtils.getLocalCache("system","userName").trim());
                pwdInput.requestFocus();                
            }           
        } 
        primaryStage.show();        
    }
    private void mainApp(Stage primaryStage,String title,String user, String passwd) throws RequestException{
    	primaryStage.hide();
        Scene myScene = new Scene(initWorkbench(primaryStage,user,passwd));
        CSSFX.start(myScene);
        primaryStage.setTitle(title);
        primaryStage.setScene(myScene);
        Screen screen=Screen.getPrimary();//主屏幕
        Rectangle2D rect=screen.getBounds();
        double maxX=rect.getWidth();//*screen.getOutputScaleX();//基础分辨率*缩放率
        double maxY=rect.getHeight();//*screen.getOutputScaleY();
        //客户端貌似会自动计算屏幕缩放率，所以客户端宽高根据屏幕基础分辨率进行计算
        primaryStage.setWidth(maxX*0.97);
        primaryStage.setHeight(maxY*0.9);
        //stage.setFullScreen(true);
        //stage.setFullScreenExitHint("输入ESC可以退出全屏");
        primaryStage.centerOnScreen();
        primaryStage.show();        
    }

    boolean newCertificate = false;//判断是否刚授权证书,默认不是新的

    public void Certificate(Stage stage, String errMessage, boolean menu){


        VBox vbox = new VBox();

        GridPane gp = new GridPane();
        String tip = "请把如下序列号发给您的软件供应商获取授权码";
        if (errMessage!=null){
            tip = errMessage + "," + tip;
        }
        Label lblTitle = new Label(tip);
        if (errMessage!=null){
            lblTitle.setTextFill(Color.color(1,0,0));
        }

        Label lblProblem = new Label("序列号");
        TextField tfProblem = new TextField();
        File deviceIdObj = new File(dir+"/deviceId.txt");
        String deviceString = null;
        try{
            if(deviceIdObj.exists()){
                deviceString = Files.readString(Path.of(deviceIdObj.getPath()));
            }else{
                deviceString = RandomString.getAlphaNumericString(32); // 32位
                deviceIdObj.createNewFile();
                Files.writeString(Path.of(deviceIdObj.getPath()), deviceString);
            }
        }catch(Exception e){}

        String cpuId = SystemInfoUtils.getComputerInfo();
        String serialNumber = deviceString + cpuId;//明文序列号
        String serialNumberEncrypted = AESUtil.encrypt(serialNumber);//aes加密后的序列号
        //Log.info("系统序列号: "+serialNumber);

        tfProblem.setText(serialNumberEncrypted);
        tfProblem.setEditable(false);

        Label lblDescription = new Label("授权码");
        TextArea taDescription = new TextArea();

        
        gp.setPadding( new Insets(10) );
        gp.setHgap( 4 );
        gp.setVgap( 8 );

        gp.add( lblTitle,       1, 1);  // empty item at 0,0
        //gp.add( lblEmail,       0, 2); gp.add(tfEmail,        1, 2);
        //gp.add( lblPriority,    0, 3); gp.add( cbPriority,    1, 3);
        gp.add( lblProblem,     0, 4); gp.add( tfProblem,     1, 4);
        gp.add( lblDescription, 0, 5); gp.add( taDescription, 1, 5);


        Separator sep = new Separator(); // hr

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding( new Insets(10) );

        Button saveButton = new Button("验证");
        Button cancelButton = new Button("取消");

        ButtonBar.setButtonData(saveButton, ButtonBar.ButtonData.OK_DONE);
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);

        buttonBar.getButtons().addAll(saveButton, cancelButton);

        vbox.getChildren().addAll( gp, sep, buttonBar );

        Scene scene = new Scene(vbox);

        stage.setTitle("软件授权");
        stage.setScene(scene);
        stage.setWidth( 736 );
        stage.setHeight( 414  );
        stage.show();

        cancelButton.setOnAction(e->{
            stage.close();
        });

        saveButton.disableProperty().bind(Bindings.isEmpty(taDescription.textProperty()));
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //actiontarget.setFill(Color.FIREBRICK);
                //actiontarget.setText("Sign in button pressed");
                String userInfoEncryption = taDescription.getText();
                newCertificate = true; // 刚从textField获取的一定是新证书
                //Log.info("加密用户信息 : "+userInfoEncryption);
                
                try{
                    String decryptedString = RSAUtil.decrypt(userInfoEncryption, RSAUtil.privateKey);
                    //Log.info("解密信息2: "+decryptedString);
                    if (userInfoVerify(decryptedString, serialNumber)==1){
                        Certificate(stage,"授权码过期!",menu);
                    }
                    else if(userInfoVerify(decryptedString, serialNumber)==2){
                        Certificate(stage,"授权码错误!",menu);
                    }else{
                        String text = userInfoEncryption;
                        File myObj = new File(dir+"/crt.txt");
                        myObj.createNewFile();
                        Path fileName = Path.of(dir+"/crt.txt");
                        Files.writeString(fileName, text);                        
                        if(!menu){                        	
                            checkConfig(stage,globalConfig.getClientType(),globalConfig.getServerUrl());
                        }else{
                        	stage.close();
                            MainUtils.showDialog(workbench, "授权成功", "恭喜您成为授权正式版用户");
                        }
                    }
                }catch(Exception exception){
                    Log.error("", exception);
                    if(menu) {
                    	stage.close();
                    }                   
                    Stage stage = new Stage();
                    Certificate(stage,"授权码错误!!",menu);
                }
                
            }
        });


    }   
    private Workbench initWorkbench(Stage primaryStage,String user, String passwd) throws RequestException {               
        UserInfo userInfo = globalConfig.getCurrentUser();
        globalConfig.flushServerConfig(null, null);
        String versionNumber = TomlConfig.build().getVersion().getNumber();        
        Menu patch = new Menu("升级更新", createIcon(MaterialDesign.MDI_DOMAIN));
        MenuItem update = new MenuItem("客户端升级");
        patch.getItems().add(update);
        MenuItem server_update = new MenuItem("服务器升级");
        if (user.equals("admin")){
            // patch.getItems().add(server_update);
        }
        Menu certificate = new Menu("证书", createIcon(MaterialDesign.MDI_DOMAIN));
        MenuItem authorized = new MenuItem("正版授权");
        certificate.getItems().addAll(authorized);

        Menu other = new Menu("其它", createIcon(MaterialDesign.MDI_DOMAIN));
        MenuItem mergeWeightAndBox = new MenuItem("合并称重与中箱工序");
        MenuItem splitWeightAndBox = new MenuItem("拆分称重与中箱工序");
        other.getItems().addAll(mergeWeightAndBox,splitWeightAndBox);


        //MenuItem item2 = new MenuItem("打印", new FontIcon(MaterialDesign.MDI_PRINTER));
        //MenuItem item3 = new MenuItem("设置", new FontIcon(MaterialDesign.MDI_SETTINGS));

        nightly = new ToolbarItem("");

        String nightlyValue  = MainUtils.getLocalCache("system","nightly");
        if(nightlyValue==null || nightlyValue.equals("先行版")){
            nightly.setText("先行版");
        }
        final String ctId=globalConfig.getClientType();
        final String ctName=globalConfig.getClientName(ctId);     
        ToolbarItem showVersion = new ToolbarItem(ctName+"追溯系统（"+versionNumber+"）", new FontIcon(MaterialDesign.MDI_SETTINGS));
        final String serverVersion = globalConfig.getServerVer().getServer();       
        ToolbarItem serverVersionInfo = new ToolbarItem("服务器正常 ◕‿◕（"+serverVersion+"）",new FontIcon(MaterialDesign.MDI_DNS));
        ToolbarItem showUser = new ToolbarItem(""+user, new FontIcon(MaterialDesign.MDI_ACCOUNT));
        ToolbarItem showExpired = new ToolbarItem("授权还剩"+exPiredDays+"天", new FontIcon(MaterialDesign.MDI_CLOCK));
        if (exPiredDays > 3650){
            showExpired.setText("");
        }        
        RestUtil.setRequestExceptionHandler(re->{//每次请求后自动更新服务器状态
        	try {
    			Platform.runLater(()->{
    				try {
    					if(re==null) {
    						serverVersionInfo.setText("服务器正常 ◕‿◕（"+globalConfig.getServerVer().getServer()+"）"); 
    					}else {
    						if(re.isConnectTimeout()) {
    		            		serverVersionInfo.setText("服务器连接超时 ◔⁔◔（"+globalConfig.getServerVer().getServer()+"）");
    		            	}else if(re.isConnectRefuse()) {
    		            		serverVersionInfo.setText("服务器拒绝连接 ◔⁔◔（"+globalConfig.getServerVer().getServer()+"）");
    		            	}else {
    		            		Log.error("服务器异常", re);
    		            		serverVersionInfo.setText("服务器异常 ◔⁔◔（"+globalConfig.getServerVer().getServer()+"）");
    		            	}
    					}
    	        	}catch (Throwable t) {
    	        		Log.error("更新服务器状态失败", t);
    				}    				
    			});
    		}catch (Throwable t) {
				Log.error("更新服务器状态失败", t);
			}
        });
        ToolbarItem logoutButton = new ToolbarItem(
                "退出",
                new FontIcon(MaterialDesign.MDI_EXIT_TO_APP),
                event -> Platform.exit()
                );
//        /** -------------- 服务器状态刷新 start ------------- */
//        //服务器状态监听器
//        EventHandler<ActionEvent> eventHandler = e->{
//            try{
//            	String newVer=ServerAPI.fetchVersion();
//            	serverVersionInfo.setText("服务器正常 ◕‿◕（"+"V"+newVer+"）");                
//            }catch(RequestException t){
//            	if(t.isConnectTimeout()) {
//            		 serverVersionInfo.setText("服务器 ◔⁔◔（"+"V"+ServerAPI.getLastVersion()+"）");
//            	}else if(t.isConnectRefuse()) {
//            		 serverVersionInfo.setText("服务器拒绝连接 ◔⁔◔（"+"V"+ServerAPI.getLastVersion()+"）");
//            	}else {
//            		 Log.error("服务器异常", t);
//            		 serverVersionInfo.setText("服务异常 ◔⁔◔（"+"V"+ServerAPI.getLastVersion()+"）");
//            	}             
//            }
//        };
//        //服务器心跳间隔由60s增加到120s by LiangGuanHao on 2023/3/11
//        Timeline animation = new Timeline(new KeyFrame(Duration.seconds(120), eventHandler));
//        animation.setCycleCount(Timeline.INDEFINITE);
//        animation.play();
//        /** -------------- 服务器状态刷新 end ------------- */


        String creator = user;
        // WorkbenchFX      

        List<String> moduleNames = TomlConfig.build().getIndex().getModuleNames();
        WorkbenchModule[] indexModules = new WorkbenchModule[moduleNames.size()];
        Preferences preferences = new Preferences();
        WorkbenchModule weightAndBoxModule = new WeightAndBoxModule(creator);
        for (int i=0; i<moduleNames.size();i++){
            if (moduleNames.get(i).equals("productInfo")){
                indexModules[i] = new ProductInfoModule(creator);
            }else if(moduleNames.get(i).equals("productLaser")){
                indexModules[i] = new ProductLaserModule(creator);
            }else if(moduleNames.get(i).equals("colorBoxPrint")){
                indexModules[i] = new ColorBoxSNModule(creator);
            }else if(moduleNames.get(i).equals("weighBox")){
                indexModules[i] = new WeighBoxModule(creator);
            }else if(moduleNames.get(i).equals("cartoonBoxPrint")){
                indexModules[i] = new CartoonBoxSNModule(creator);
            }else if(moduleNames.get(i).equals("palletPrint")){
                indexModules[i] = new PalletSNModule(creator);
            }else if(moduleNames.get(i).equals("shipping")){
                indexModules[i] = new ShippingModule(creator);
            }else if(moduleNames.get(i).equals("preferences")){
                indexModules[i] = new PreferencesModule(preferences);
            }else if(moduleNames.get(i).equals("boxPost")){
                indexModules[i] = new BoxPostModule();
            }else if(moduleNames.get(i).equals("palletPost")){
                indexModules[i] = new PalletPostModule();
            }else if(moduleNames.get(i).equals("query")){
                indexModules[i] = new QueryModule();
            }else if(moduleNames.get(i).equals("user")){
                indexModules[i] = new UserModule();
            }


        }

        
        Menu menuVersion = new Menu("版本", createIcon(MaterialDesign.MDI_DOMAIN));       
        for(SimpleElement clientType:globalConfig.getAllClientType()) {//动态切换客户端类型
        	final MenuItem item=new MenuItem(clientType.getName()+"产品追溯系统");
        	menuVersion.getItems().add(item);
        	item.setOnAction(event->{        		
        		String serverUrl=LocalCache.getCache().getServer(clientType.getId());
        		if(serverUrl==null||serverUrl.isBlank()) {
        			MainUtils.showErrorDialog(workbench, "切换失败", "请去全局设置页面配置"+clientType.getName()+"数据库地址!");
                    return;
        		}        		
        		checkConfig(primaryStage,clientType.getId(), serverUrl);        		
        	});  
        } //切换产品类型菜单 end
        
        workbench = Workbench.builder(
                        //new ProductInfoModule(creator),
                        //new ProductLaserModule(creator),
                        //new ColorBoxSNModule(creator),
                        //new CartoonBoxSNModule(creator),
                        //new PalletSNModule(creator),
                        //new ShippingModule(creator)
                        indexModules
                )
                .toolbarLeft(new ToolbarItem("菜单"))
                .toolbarRight(nightly,showVersion,serverVersionInfo,showUser,showExpired,logoutButton)
                //.navigationDrawerItems(menuVersion,patch,certificate,other)
                .navigationDrawerItems(menuVersion,patch,certificate)
                .modulesPerPage(16)
                .tileFactory(wb->{
                	Tile tile=new Tile(wb);
                	tile.moduleProperty().addListener((observable,oldValue,newValue)->{
                		if(newValue!=null) {
                			final String tileName=tile.getName();

                			if("产品设置".equals(tileName)) {
                                if("否".equals(userInfo.getProduct())){
                                    tile.getStyleClass().add("tile-unactive");
                                }
                			}               	
                			if("产品镭射".equals(tileName)) {
                                if("否".equals(userInfo.getLaser())){
                                    tile.getStyleClass().add("tile-unactive");
                                }
                			}            

                			if("彩盒SN".equals(tileName)) {
                                if("否".equals(userInfo.getColorBox())){
                                    tile.getStyleClass().add("tile-unactive");
                                }
                			}               	

                			if("称重".equals(tileName)) {
                                if("否".equals(userInfo.getWeight())){
                                    tile.getStyleClass().add("tile-unactive");
                                }
                			}               	

                			if("中箱装箱".equals(tileName)||"中箱物流".equals(tileName)) {
                                if("否".equals(userInfo.getCartoonBox())){
                                    tile.getStyleClass().add("tile-unactive");
                                }
                			}               	

                			if("栈板管理".equals(tileName)||"目的仓".equals(tileName)) {
                                if("否".equals(userInfo.getPallet())){
                                    tile.getStyleClass().add("tile-unactive");
                                }
                			}               	

                			if("出货".equals(tileName)) {
                                if("否".equals(userInfo.getShipping())){
                                    tile.getStyleClass().add("tile-unactive");
                                }
                			}               	

                			if("用户管理".equals(tileName)) {
                                if(!"admin".equals(userInfo.getUserName())){
                                    tile.getStyleClass().add("tile-unactive");
                                }
                			}            

                			if("全局配置".equals(tileName)) {
                                if(!"admin".equals(userInfo.getUserName()) && !"super-admin".equals(userInfo.getUserName())){
                                    tile.getStyleClass().add("tile-unactive");
                                }
                			}           
                		}
                	});
                	tile.setOnMouseClicked(e->{                		       
                        final String tileName=tile.getName();
                        if("产品设置".equals(tileName)) {
                            if("否".equals(userInfo.getProduct())){
                                return;
                            }
                        }         

                        if("产品镭射".equals(tileName)) {
                            if("否".equals(userInfo.getLaser())){
                                return;
                            }
                        }      
                        if("彩盒SN".equals(tileName)) {
                            if("否".equals(userInfo.getColorBox())){
                                return;
                            }
                        }               	

                        if("称重".equals(tileName)) {
                            if("否".equals(userInfo.getWeight())){
                                return;
                            }
                        }               	

                        if("中箱装箱".equals(tileName)||"中箱物流".equals(tileName)) {
                            if("否".equals(userInfo.getCartoonBox())){
                                return;
                            }
                        }               	

                        if("栈板管理".equals(tileName)||"目的仓".equals(tileName)) {
                            if("否".equals(userInfo.getPallet())){
                                return;
                            }
                        }               	

                        if("出货".equals(tileName)) {
                            if("否".equals(userInfo.getShipping())){
                                return;
                            }
                        }               	

                        if("用户管理".equals(tileName)) {
                            if(!"admin".equals(userInfo.getUserName())){
                                return;
                            }
                        }            

                        if("全局配置".equals(tileName)) {
                            if(!"admin".equals(userInfo.getUserName()) && !"super-admin".equals(userInfo.getUserName()) ){
                                return;
                            }
                        }           

                        tile.open();

                    });                
                    return tile;
                })
        .build();
        if(ctId.equals("sushi")){
            workbench.getModules().remove(indexModules[5]);
            workbench.getModules().remove(indexModules[7]);
        }

        //合并工序
        mergeWeightAndBox.setOnAction(e->{
            workbench.getModules().remove(indexModules[3]);
            workbench.getModules().remove(indexModules[4]);
            workbench.getModules().add(3,weightAndBoxModule);
            

        });
        //拆分工序
        splitWeightAndBox.setOnAction(e->{
            workbench.getModules().remove(weightAndBoxModule);
            workbench.getModules().add(3,indexModules[3]);
            workbench.getModules().add(4,indexModules[4]);
        });


        update.setOnAction(event -> {
            Runnable runnable =
                () -> { 
                    if(nightlyValue.equals("先行版")){
                        Helper.updatePatch(true);
                    }else{
                        Helper.updatePatch(false);
                    }
                    Platform.runLater(finishUpdate);
                };
            Thread thread = new Thread(runnable);
            //thread.setDaemon(true);//系统关闭时自动销毁线程
            thread.start();
            WorkbenchDialog dialog = WorkbenchDialog.builder(
                    "客户端升级中...", "升级过程请勿关闭程序或者电脑,升级完毕再操作本应用")
                .blocking(true)
                .showButtonsBar(false)
                .build();
            workbench.showDialog(dialog);


        });

        if (server_update!=null) server_update.setOnAction(event -> {
            Runnable runnable =
                () -> { 
                    if(nightlyValue.equals("先行版")){
                        Helper.updateServer(true);
                    }else{
                        Helper.updateServer(false);
                    }
                    //Platform.runLater(finishUpdate);
                };


            ButtonType okay = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);

            WorkbenchDialog confirm = WorkbenchDialog.builder(
                    "操作确认", "服务器会导致当前生产中断,请您谨慎确认,在供应商的陪同下操作!",okay,cancel)
                .blocking(true)
                .showButtonsBar(true)
                .onResult(buttonType -> {
                    if (buttonType.equals(okay)) {
                        WorkbenchDialog dialog = WorkbenchDialog.builder(
                                "服务器升级中...", "服务器升级过程中,暂时停止任何操作请等待服务器状态OKAY")
                            .blocking(true)
                            .showButtonsBar(false)
                            .build();
                        workbench.showDialog(dialog);
                        Thread thread = new Thread(runnable);
                        //thread.setDaemon(true);//系统关闭时自动销毁线程
                        thread.start();
                    }
                })
            .build();
            workbench.showDialog(confirm);
        });



        authorized.setOnAction(event -> {
            String cpuId = SystemInfoUtils.getComputerInfo();
            File f = new File(dir + "/crt.txt");
            File deviceIdObj = new File(dir+"/deviceId.txt");
            if(f.exists() && !f.isDirectory() && deviceIdObj.exists() && !deviceIdObj.isDirectory()) { 
                Path fileName = Path.of(dir+"/crt.txt");
                try{
                    String userInfoEncryption = Files.readString(fileName);
                    String deviceStr = Files.readString(Path.of(deviceIdObj.getPath()));
                    String serialNumber = deviceStr + cpuId;
                    String decryptedString = RSAUtil.decrypt(userInfoEncryption, RSAUtil.privateKey);
                    //Log.info("解密信息: "+decryptedString);
                    if (isGenuine(decryptedString, serialNumber)){
                        MainUtils.showDialog(workbench,"已授权","您已经是正版用户");
                        return;
                    }
                }catch(Exception e){
                    Log.error("",e);
                }
            }

            Stage stage = new Stage();
            Certificate(stage,"正版授权",true);
        });       

        //item2.setOnAction(event -> workbench.hideNavigationDrawer());
        //item3.setOnAction(event -> workbench.hideNavigationDrawer());

        // This sets the custom style. Comment this out to have a look at the default styles. 
        workbench.getStylesheets().add(ExtendedDemo.class.getResource("customTheme.css").toExternalForm());        
        return workbench;
    }
    /**
     * 更新结束处理
     */
    private final Runnable finishUpdate=()->{
    	ButtonType okay = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        WorkbenchDialog dialog = WorkbenchDialog.builder(
                "更新完毕", "更新完毕,请关闭后再打开", okay)
            .showButtonsBar(true)
            .onResult(buttonType -> {
                Platform.exit();
            })
        .build();
        workbench.showDialog(dialog);
    };
	private Node createIcon(MaterialDesign mdiDomain) {
		return null;
	}

}

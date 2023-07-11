package com.dlsc.workbenchfx.demo.modules.preferences;

import com.cg.core.module.SimpleElement;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.IntegerField;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.formsfx.view.controls.IntegerSliderControl;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.dlsc.preferencesfx.view.PreferencesFxView;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.App;
import com.dlsc.workbenchfx.demo.ExtendedDemo;
import com.dlsc.workbenchfx.demo.LocalCache;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.fazecast.jSerialComm.SerialPort;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.scene.control.ButtonType;

import org.apache.commons.lang.StringUtils;

/**
 * Model object for Preferences.
 */
public class Preferences {
	private final LocalCache localCache=LocalCache.getCache();
	private final GlobalConfig globalConfig=GlobalConfig.getInstance();
  public PreferencesFx preferencesFx;

  public Workbench workbench = null;

  // General
  StringProperty welcomeText = new SimpleStringProperty("Hello World");
  IntegerProperty brightness = new SimpleIntegerProperty(50);
  BooleanProperty nightMode = new SimpleBooleanProperty(true);
  BooleanProperty zplMode = new SimpleBooleanProperty(true);

  // Screen
  DoubleProperty scaling = new SimpleDoubleProperty(1);
  /**
   * 服务器地址存储器,key为客户端类型编码,value为服务器地址
   */
  private final Map<String, StringProperty> serverUrls=new HashMap<>(); 
  StringProperty minValue = new SimpleStringProperty("0.005");
  StringProperty weightN = new SimpleStringProperty("3");
  IntegerProperty DPI = new SimpleIntegerProperty(300);
  /**
   * 打印浓度
   */
  private IntegerProperty printerMD = new SimpleIntegerProperty(0);  

   ObjectProperty<File> fileProperty1 = new SimpleObjectProperty<File>();
   ObjectProperty<File> fileProperty2 = new SimpleObjectProperty<File>();
   ObjectProperty<File> fileProperty3 = new SimpleObjectProperty<File>();

  ObservableList<String> colorBoxInputPermissionItems = null;
  ObservableList<String> weightPassed = null;
  ObservableList<String> weightByHand = null;
  ObservableList<String> nightly = null;
  /**
   * 可选打印机名称列表(printerName)
   */
  ObservableList<String> printerItems = null;  
  ObservableList<String> weighItems = null;
  ObservableList<String> unitItems = null;
  /**
   * 已选中的打印机名称
   */
  ObjectProperty<String> printerSelection = null;  
  ObjectProperty<String> weighSelection = null;
  ObjectProperty<String> nightlySelection = null;
  ObjectProperty<String> unitSelection = null;
  ObjectProperty<String> colorBoxInputPermissionSelection = null;
  ObjectProperty<String> weightPassedSelection = null;
  ObjectProperty<String> weightByHandSelection = null;

  ListProperty<String> orientationItems = new SimpleListProperty<>(
      FXCollections.observableArrayList(Arrays.asList("Vertical", "Horizontal"))
  );
  ObjectProperty<String> orientationSelection = new SimpleObjectProperty<>("Vertical");

  IntegerProperty fontSize = new SimpleIntegerProperty(12);
  DoubleProperty lineSpacing = new SimpleDoubleProperty(1.5);

  // Favorites
  ListProperty<String> fontItems = new SimpleListProperty<>(
      FXCollections.observableArrayList(Arrays.asList(
              "test"
          )
      )
  );
  ListProperty<String> favoritesSelection = null;//new SimpleListProperty<>(
  //    FXCollections.observableArrayList(Arrays.asList(
  //        "Eboda Phot-O-Shop", "Mikesoft Text"))
  //);

  // Custom Control
  IntegerProperty customControlProperty = new SimpleIntegerProperty(42);
  IntegerField customControl = setupCustomControl();

  public Preferences() {


      ObservableSet<Printer> printers = Printer.getAllPrinters();
      List<String> printerNames = new ArrayList<String>();
      printerNames.add("mock");
      ObservableList<Printer> printerList = FXCollections.observableArrayList(printers);
      for (int i=0; i<printers.size();i++){ 
          String name = printerList.get(i).getName();
          printerNames.add(name);
      }
      printerItems = FXCollections.observableArrayList(printerNames);
      String printerName = MainUtils.getLocalCache("system", "printer");
      if (printerName==null || printerName.isEmpty()){
          printerName = printerItems.get(0);
      }
      printerSelection = new SimpleObjectProperty<>(printerName);                  
      List<String> fonts = new ArrayList<String>();
      for (int i=0; i< fonts.size(); i++){
          fontItems.add(fonts.get(i));
      }

      SerialPort[] serialPorts = SerialPort.getCommPorts();//查找所有串口
      List<String> weighNames = new ArrayList<String>();
      //weighNames.add("占位符");
      for(SerialPort port:serialPorts){
          weighNames.add(port.getSystemPortName());
      }
      //称重端口
      weighItems = FXCollections.observableArrayList(weighNames);
      String weighName = MainUtils.getLocalCache("system", "weighPort");//电子秤串口名称
      if(StringUtils.isNotEmpty(weighName)){
          weighSelection = new SimpleObjectProperty<>(weighName);
      }else{
          weighSelection = new SimpleObjectProperty<>("");
      }

      //称重单位
      unitItems = FXCollections.observableArrayList(Arrays.asList("kg","g"));
      String unitStr = MainUtils.getLocalCache("system", "unit");
      if(StringUtils.isNotEmpty(unitStr)){
          unitSelection = new SimpleObjectProperty<>(unitStr);
      }else{
          unitSelection = new SimpleObjectProperty<>("kg");
      }
      //称重最小度数
      String wtMinValueStr = MainUtils.getLocalCache("system", "wtMinValue");
      if(StringUtils.isNotEmpty(wtMinValueStr)){
          minValue = new SimpleStringProperty(wtMinValueStr);
      }
      //称重稳定因子
      String weightNStr = MainUtils.getLocalCache("system", "weightN");
      if(StringUtils.isNotEmpty(weightNStr)){
          weightN = new SimpleStringProperty(weightNStr);
      }

      colorBoxInputPermissionItems = FXCollections.observableArrayList(Arrays.asList("禁止录入","允许录入"));
      String colorBoxInputPermission = MainUtils.getLocalCache("system", "colorBoxInputPermission");
      colorBoxInputPermissionSelection = new SimpleObjectProperty<>(colorBoxInputPermission);

      weightPassed = FXCollections.observableArrayList(Arrays.asList("必须称重","可选称重"));
      String weightPassedStr = MainUtils.getLocalCache("system", "weightPassed");
      weightPassedSelection = new SimpleObjectProperty<>(weightPassedStr);

      weightByHand = FXCollections.observableArrayList(Arrays.asList("手工称重","自动称重"));
      String weightByHandStr = MainUtils.getLocalCache("system", "weightByHand");
      weightByHandSelection = new SimpleObjectProperty<>(weightByHandStr);


      nightly = FXCollections.observableArrayList(Arrays.asList("先行版","正式版"));
      String nightlyStr = MainUtils.getLocalCache("system", "nightly");
      nightlySelection = new SimpleObjectProperty<>(nightlyStr);




      String printerDPI = MainUtils.getLocalCache("system", "printerDPI");
      if(printerDPI!=null && !printerDPI.isEmpty()){
          DPI.setValue(Integer.valueOf(printerDPI));
      }

      String zplModeStr = MainUtils.getLocalCache("system", "zplMode");
      if(zplModeStr!=null && !zplModeStr.isEmpty()){
            zplMode.set(Boolean.valueOf(zplModeStr));
      }


      String printerMDstr = MainUtils.getLocalCache("system", "printerMD");
      if(printerMDstr!=null && !printerMDstr.isEmpty()){
          printerMD.setValue(Integer.valueOf(printerMDstr));
      }     
      globalConfig.getAllClientType().forEach(se->{//初始化服务器配置数据
    	  String urlCache=localCache.getServer(se.getId());
    	  serverUrls.put(se.getId(), new SimpleStringProperty(urlCache==null?"":urlCache));
      });
      preferencesFx = createPreferences();
      preferencesFx.saveSettings();

  }

  private IntegerField setupCustomControl() {
    return Field.ofIntegerType(customControlProperty).render(
        new IntegerSliderControl(0, 42));
  }

  private PreferencesFx createPreferences() {
      var categoryList = new ArrayList<Category>();
      categoryList.add(
          Category.of("应用设置",
              Group.of("彩盒页面",
                      Setting.of("允许录入产品SN", colorBoxInputPermissionItems, colorBoxInputPermissionSelection)
              ),
              Group.of("称重工序",
                      Setting.of("必须称重", weightPassed, weightPassedSelection),
                      Setting.of("称重模式", weightByHand, weightByHandSelection)
          ))
      );
      categoryList.add(
          Category.of("打印机设置",
                  Group.of("默认打印机",
                          Setting.of("型号", printerItems, printerSelection),
                          Setting.of("DPI", DPI),
                          Setting.of("浓度(+-)", printerMD),                         
                          Setting.of("斑马打印机", zplMode)
                  )
                  //Group.of("加载字体",
                  //    Setting.of("上传字体", fileProperty1, false)
                  //    )
          )
      );
      final List<SimpleElement> allClientTypes=globalConfig.getAllClientType();
      @SuppressWarnings("rawtypes")
      final Setting[] settings=new Setting[allClientTypes.size()];
      for(int i=0;i<allClientTypes.size();i++) {
    	  final SimpleElement se=allClientTypes.get(i);
    	  settings[i]=Setting.of(se.getName()+"服务地址", serverUrls.get(se.getId()));
      }
      categoryList.add(Category.of("服务器配置",settings));
      categoryList.add(
          Category.of("称重机设置",
                  Group.of("称重端口",
                          Setting.of("端口", weighItems, weighSelection)
                  ),
                  Group.of("称重参数",
                          Setting.of("单位", unitItems, unitSelection),
                          Setting.of("最小读数", minValue),
                          Setting.of("稳定读数因子", weightN)
                  )
          )
      );
      if("super-admin".equals(MainUtils.getLocalCache("system","userName"))){
          categoryList.add(
                  Category.of("开发设置",
                          Group.of("版本切换",
                                  Setting.of("先行版", nightly, nightlySelection)
                          )
                  )
          );
      }
      return PreferencesFx.of(PreferencesModule.class,categoryList.stream().toArray(Category[]::new))
                          .persistWindowState(false)
                          .saveSettings(false)
                          .debugHistoryMode(false)
                          .buttonsVisibility(true);
  }

  void save() {
      //Log.info("hello save me 3");
      //Log.info(weighSelection.getValue());
      MainUtils.addOrUpdateLocalCache("system", "printer", printerSelection.getValue());
      MainUtils.addOrUpdateLocalCache("system", "weighPort", weighSelection.getValue());
      MainUtils.addOrUpdateLocalCache("system", "unit", unitSelection.getValue());
      MainUtils.addOrUpdateLocalCache("system", "wtMinValue", minValue.getValue());
      MainUtils.addOrUpdateLocalCache("system", "weightN", weightN.getValue());
      MainUtils.addOrUpdateLocalCache("system", "printerDPI", String.valueOf(DPI.getValue()));
      MainUtils.addOrUpdateLocalCache("system", "printerMD", String.valueOf(printerMD.getValue()));
      MainUtils.addOrUpdateLocalCache("system", "colorBoxInputPermission", colorBoxInputPermissionSelection.getValue());
      MainUtils.addOrUpdateLocalCache("system", "weightPassed", weightPassedSelection.getValue());
      MainUtils.addOrUpdateLocalCache("system", "weightByHand", weightByHandSelection.getValue());
      MainUtils.addOrUpdateLocalCache("system", "nightly", nightlySelection.getValue());
      MainUtils.addOrUpdateLocalCache("system", "zplMode", String.valueOf(zplMode.getValue()));              
      if(nightlySelection.getValue()!=null && nightlySelection.getValue().equals("先行版")){
          ExtendedDemo.nightly.setText("先行版");
      }
      Iterator<Entry<String, StringProperty>> it=serverUrls.entrySet().iterator();      
      String restartServerUrl=null;
      while(it.hasNext()) {
    	  Entry<String, StringProperty> entry=it.next();    	  
    	  final String url=entry.getValue().getValue();
    	  if(url==null||url.isBlank())break;
    	  final String ct=entry.getKey();
    	  localCache.saveServerUrl(ct, url);//缓存到本地 
    	  if(ct.equals(globalConfig.getClientType())&&!url.equals(globalConfig.getServerUrl())) {
    		  //当前服务地址变更，需要重启生效
    		  restartServerUrl=url;
    	  }
      }   
      MainUtils.showSimpleSuccess("保存成功!");
      if(restartServerUrl!=null) {//当前服务地址变更，提示是否需要重启
    	  final String newServerUrl=restartServerUrl;
    	  workbench.showConfirmationDialog("服务地址变更", "检测到当前("+globalConfig.getClientName(globalConfig.getClientType())+")服务地址变更,是否立即切换到最新地址?", bt->{    			  
			  if(bt==ButtonType.YES) {
				  App.getApp().restart(globalConfig.getClientType(), newServerUrl);
			  }
		  });
      }
     
   
      
  }
  public void discardChanges() {
    preferencesFx.discardChanges();
  }

  public PreferencesFxView getPreferencesFxView() {
    return preferencesFx.getView();
  }

  public BooleanProperty nightModeProperty() {
    return nightMode;
  }

  public boolean isNightMode() {
    return nightMode.get();
  }
}

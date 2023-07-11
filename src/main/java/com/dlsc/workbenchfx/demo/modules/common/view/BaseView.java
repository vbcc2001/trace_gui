package com.dlsc.workbenchfx.demo.modules.common.view;

import javafx.collections.ObservableList;

//import javafx.scene.Group;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.cg.core.util.Log;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;
import com.dlsc.workbenchfx.demo.modules.palletSN.model.PalletSN;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import com.dlsc.workbenchfx.demo.LettersOrNumbersValidator;
import com.dlsc.workbenchfx.demo.LocalCache;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.ZplPrinter;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.user.model.UserInfo;
import com.dlsc.workbenchfx.model.WorkbenchDialog;

import javafx.scene.text.FontWeight;
import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import org.apache.commons.beanutils.BeanUtils;
import com.cg.core.gui.FormUtils;
import javafx.beans.property.*;
import javafx.scene.layout.HBox;
import com.dlsc.workbenchfx.demo.api.CartoonBoxAPI;
import com.dlsc.workbenchfx.demo.api.CounterAPI;
import com.dlsc.workbenchfx.demo.api.ServerAPI;


/**
 *
 * 2个基本模板类型C代表容器Container,I代表物品Item
 *
 * */
public abstract class BaseView<C,I> extends BorderPane{

    //public CheckBox cbTranspose = null;

    public void recoverTranspose(CheckBox cbTranspose, String name, String version){
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


    public void recoverNote(CheckBox cbNote, String name, String version){
        String printerNote = MainUtils.getDict(name, "printerNote", version);
        if(printerNote!=null && printerNote.equals("带出")){
            cbNote.setSelected(true);
        }else{
            cbNote.setSelected(false);
        }
    }

    public void saveNote(CheckBox cbNote, String name, String version){
        if(cbNote.isSelected()){
            MainUtils.addOrUpdateLocalCache(name, "printerNote", "带出", version);
        }else{
            MainUtils.addOrUpdateLocalCache(name, "printerNote", "空白", version);
        }
    }



    //public void adjPackage(Workbench workbench, String name){
    //        GridPane gridPane1 = new GridPane();
    //        gridPane1.setHgap(10);
    //        gridPane1.setVgap(5);

    //        Label lbBoxSN1 = new Label(name+"SN码");
    //        gridPane1.add(lbBoxSN1, 0, 0);
    //        TextField txtBoxSNAdj = new TextField();
    //        txtBoxSNAdj.setPrefWidth(300);
    //        gridPane1.add(txtBoxSNAdj, 1, 0);

    //        ButtonType okay = MainUtils.okayButton();
    //        ButtonType cancel = MainUtils.cancelButton();

    //        WorkbenchDialog dialog = WorkbenchDialog.builder(
    //                name+"调整", gridPane1, okay, cancel)
    //            //.blocking(false)
    //            //.maximized(true)
    //            .showButtonsBar(true)
    //            .onResult(buttonType -> {
    //                if (buttonType.equals(okay)) {
    //                    ButtonType okay2 = MainUtils.okayButton();
    //                    ButtonType cancel2 = MainUtils.cancelButton();

    //                    //CartoonBoxSN cartoonBoxSN = getCartoonBox(txtBoxSNAdj.getText());
    //                    //if (cartoonBoxSN==null){
    //                    //    MainUtils.showSimpleError("非法中箱SN码!");
    //                    //    return;
    //                    //}
    //                    GridPane gridPane = adjPane(txtBoxSNAdj.getText());                       
    //                    //MainUtils.searchAndSetControlsLabelWidth(rendererUpdate, 35);


    //                    WorkbenchDialog dialog2 = WorkbenchDialog.builder(
    //                            name+"调整", gridPane, okay2, cancel2)
    //                        .showButtonsBar(true)
    //                        .blocking(true)
    //                        .onResult(buttonType2 -> {
    //                            if (buttonType2.equals(okay2)) {
    //                            }
    //                        })
    //                    .build();
    //                    dialog2.setOnShown(event1 -> {
    //                        dialog2.getButton(okay).ifPresent(
    //                                button -> {
    //                                });
    //                    });
    //                    workbench.showDialog(dialog2);
    //                }

    //            }).build();

    //        workbench.showDialog(dialog);

    //}

    //private GridPane adjPane(String SNCode){
    //   GridPane gridPane = new GridPane();

    //    gridPane.setPadding(new Insets(20,20,20,20));
    //    gridPane.getStyleClass().add("bordered_all");

    //    //gridPane.setStyle("-fx-padding: 20 20 20 20;");
    //    //gridPane.setAlignment(Pos.CENTER);
    //    //gridPane.setHgap(80);
    //    gridPane.setVgap(10);

    //    //Implementing Nodes for GridPane
    //    //MainUtils.recoverTextField(txtBoxSN, "cartoonBox", "txtBoxSN",version);
    //    Label lbProductSN = new Label("产品SN码:");
    //    TextField txtProductSN = new TextField();
    //    TextField txtBoxSN = new TextField(SNCode);
    //    txtBoxSN.setEditable(false);
    //    txtProductSN.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

    //    //if(txtBoxSN.getText().isEmpty()){
    //    //    txtProductSN.setEditable(false);
    //    //}

    //    Label lbBoxSN = new Label("箱子SN码:");
    //    Label lblMessage = new Label();
    //    //final TextField txtBoxSN = new TextField();
    //    //txtBoxSN.setEditable(false);
    //    txtBoxSN.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    //    lblMessage.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));


    //    gridPane.add(lbProductSN, 0, 1);
    //    gridPane.add(txtProductSN, 1, 1);
    //    gridPane.add(lbBoxSN, 0, 0);
    //    gridPane.add(txtBoxSN, 1, 0);


    //    Label jobStatusProductSN = new Label("请先设置扫码枪的回车建.");
    //    Label lbProductSNStatus = new Label("产品SN列表: ");
    //    gridPane.add(lbProductSNStatus,0,2);
    //    //lblMessage.setText("测试！");
    //    GridPane stack_pane = new GridPane();
    //    jobStatusProductSN.setPrefWidth(200);
    //    stack_pane.add(jobStatusProductSN,0,0);
    //    stack_pane.add(lblMessage,1,0);
    //    gridPane.add(stack_pane,1,2);
    //    

    //    Label lbProductList = new Label("产品列表: ");
    //    gridPane.add(lbProductList,0,3);
    //    ListView<String> listView = new ListView<>();
    //    //listView.prefWidthProperty().bind(gridPane.widthProperty().multiply(0.3D));
    //    //listView.prefHeightProperty().bind(gridPane.heightProperty().multiply(0.6D));
    //    listView.setId("productSNs");
    //    txtBoxSN.setId("boxSN");

    //    gridPane.add(listView,1,3);
    //    return gridPane;

    //}

    //public boolean isBoxInPallet(String nickName){
    //    //判断中箱是否绑定了栈板
    //    String uri = String.format("/cartoonBox/get?");
    //    String postBody = """
    //    {
    //        "nickName":"%s"
    //    }
    //        """;
    //    postBody = String.format(postBody,nickName);
    //    String text = MainUtils.post(postBody,GlobalConfig.getInstance().getServerUrl()+uri);
    //    JSONObject jsonObject = JSON.parseObject(text);
    //    JSONArray data = jsonObject.getJSONArray("data");
    //    CartoonBoxSN cartoonBoxSN = null;
    //    if (data.size()==0){
    //        return false;
    //    }
    //    for (int i=0;i<data.size();i++){
    //        cartoonBoxSN = JSON.parseObject(data.getString(i), CartoonBoxSN.class);
    //        break;
    //    }
    //    if(cartoonBoxSN.getPalletCode()==null || cartoonBoxSN.getPalletCode().isEmpty()){
    //        return false;
    //    }
    //    return true;

    //}

    //public boolean isBoxShipping(String boxNickName){
    //    //判断中箱是否绑定了出货
    //    String uri = String.format("/shipping/byBox?SNCode="+boxNickName);
    //    String text = MainUtils.post("{}",GlobalConfig.getInstance().getServerUrl()+uri);
    //    //Log.info(text);
    //    JSONObject json = JSON.parseObject(text);
    //    String data = json.getString("data");
    //    Integer code = json.getInteger("code");
    //    if (code == -1) return false;
    //    Log.info(data);
    //    return true;
    //}

    //public boolean isBoxinChain(String boxNickName){
    //    boolean inPallet = isBoxInPallet(boxNickName); 
    //    boolean inShipping = isBoxShipping(boxNickName);
    //    Log.info(inPallet);
    //    Log.info(inShipping);
    //    return inPallet || inShipping;
    //}

    //public Shipping getShippingByPallet(String palletNickName){
    //    String uri = String.format("/shipping/byPallet?SNCode="+palletNickName);
    //    String text = MainUtils.post("{}",GlobalConfig.getInstance().getServerUrl()+uri);
    //    Log.info(text);
    //    JSONObject json = JSON.parseObject(text);
    //    String data = json.getString("data");
    //    Integer code = json.getInteger("code");
    //    if (code == -1) return null;
    //    Shipping shipping = JSON.parseObject(data, Shipping.class);
    //    return shipping;
    //}
    //public static String dateStr(String date){
    //    String[] date_time = date.split(" ");
    //    String[] year_month_day = date_time[0].split("-");
    //    String dateCode = year_month_day[0] + year_month_day[1] + year_month_day[2];
    //    return dateCode;
    //}



    public abstract Form getForm(String version);

    public abstract String getTitle();

    public abstract C getContainer(String snCode) throws RequestException;
    public abstract I getItem(String snCode) throws RequestException, BizException;

    public abstract String getProductName(C c);
    public abstract String getProductId(C c);


    public abstract String getItemSKU(I item);
    public abstract String getItemFactoryCode(I item);


    public abstract String getContainerSKU(C c);
    public abstract String getContainerFactoryCode(C c);
    public abstract void setContainerFactoryCode(C c, String factoryCode);
    public abstract String getContainerNickName(C c);


    String getProductLasers(String cartoonBoxSNNickName) throws RequestException{
        //注意获取产品都是通过中箱的nickname
        List<ProductLaser> list = ProductLaserAPI.getByBoxCode(cartoonBoxSNNickName);
        String res = "";
        for (int i=0;i<list.size();i++){
            res += list.get(i).getSNCode();
            if (i<list.size()-1) res += ",";
        }
        return res;
    }

    boolean isBoxEmpty(String snCode){
        String productLasers = null;
        try{
            productLasers = getProductLasers(snCode);
        }catch(RequestException e){
            Log.error("",e);
            String hints = snCode + ":" + "网络异常";
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(hints);
            MainUtils.showSimpleError(hints);
            return true;
        }
        if(productLasers==null || productLasers.isEmpty()){
            String hints = snCode + ":" + "空箱子";
            MainUtils.showSimpleError(hints);
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(hints);
            return true;
        }
        return false;
    }

    void remember(){
        //String recordSN = MainUtils.getDict(getTitle(), getTitle()+"SN", version);
        MainUtils.recoverForm(getTitle(), "Form", formInstance, version);
        //if(recordSN!=null && !recordSN.isEmpty()){
        //    C lastSN = null;
        //    try{
        //        lastSN = getContainer(recordSN);
        //    }catch(RequestException e){
        //        MainUtils.showSimpleError("网络异常,无法获取记忆容器信息");
        //        return;
        //    }
        //    if(lastSN!=null) {
        //    	for(ProductInfo productInfo:productInfoList){
        //            if(productInfo.getProductName()==null) continue;
        //            if(getProductName(lastSN).indexOf(productInfo.getProductName())>=0){
        //                try{
        //                    BeanUtils.copyProperties(lastSN, productInfo);
        //                    currentProductInfo = productInfo;
        //                    currentContainer = lastSN;
        //                    break;
        //                }catch(Exception e){
        //                    Log.error("",e);
        //                }
        //            }
        //        }
        //        String lastSNJson = JSON.toJSONString(lastSN);
        //        try{
        //        FormUtils.json2Form(formInstance, lastSNJson);
        //        }catch(Exception e){}
        //    }else {
        //    	Log.info("无效容器记忆:"+recordSN);
        //    } 
        //}

    }

    //小米特有:匹配item跟container的SKU
    boolean itemMatchSKU(I item){
        // 获取容器的SKU以及物品的SKU然后匹配
        String itemSKU = getItemSKU(item);
        String containerSKU = getContainerSKU(currentContainer);
        return itemSKU.equals(containerSKU);
    }

    //通用:工厂代码匹配
    boolean itemMatchFactoryCode(I item){
        // 获取容器的工厂代码以及物品的工厂代码然后匹配
        String itemFactoryCode = getItemFactoryCode(item);
        String containerFactoryCode = getContainerFactoryCode(currentContainer);
        return itemFactoryCode.equals(containerFactoryCode);
    }

    CartoonBoxSN getCartoonBox(String nickName) throws RequestException{
        CartoonBoxSN probe = new CartoonBoxSN();
        probe.setNickName(nickName);
        List<CartoonBoxSN> list = CartoonBoxAPI.getCartoonBox(probe);
        //注意nickName是唯一的,所以只能查出1个
        if (list.size()!=1){
            return null;
        }
        return list.get(0);
    }

    //栈板特有:已经出货的中箱不许装栈板
    public boolean boxOnShip(I item){
        return false;
    };


    public abstract boolean isInContainer(I item);


    
    //中箱页面特有:彩盒已经打印
    public abstract boolean productPrinted(I item);

    //中箱页面特有:经过称重工序
    public abstract boolean productWeighted(I item);


    //


    /**
     * 装入容器的物品是否合法
     * */
    boolean itemValid(String snCode){
        //通用:查询物品是否存在
        I item = null;
        try{
            item = getItem(snCode);
        }catch (BizException e) {
			MainUtils.showSimpleError(e.getMessage());
			return false;
		}
        catch(RequestException e){
            Log.error("",e);
            String hints = snCode+":"+"网络异常";
            MainUtils.showSimpleError(hints);
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(hints);
            return false;
        }
        //lblMessage.setText("");
        if (item==null){
            String hints = snCode + ":" + (getTitle().equals("pallet")?"非法箱子SN码":"产品没经过彩盒工序");
            MainUtils.showSimpleError(hints);
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(hints);
            return false;
        }
        //栈板管理页面特有:判断是否空
        if(getTitle().equals("pallet") && isBoxEmpty(snCode)){
            return false;
        }

        //栈板管理页面特有:判断中箱是否已经出货
        if(getTitle().equals("pallet") && boxOnShip(item)){
            return false;
        }


        //通用:判断已经装入了容器
        if(isInContainer(item)){
            String hints = snCode + ":" + "已装入" + (getTitle().equals("pallet")?"栈板":"中箱");
            MainUtils.showSimpleError(hints);
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(hints);
            return false;
        }

        //小米特有:SKU
        if(version.equals("xiaomi") && !itemMatchSKU(item)){
            String hints = snCode+":"+(getTitle().equals("pallet")?"箱子":"产品")+"SKU不对";
            MainUtils.showSimpleError(hints);
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(hints);
            return false;
        }

        //通用:判断与工厂代码匹配
        if(!itemMatchFactoryCode(item)){
            String hints = snCode + ":" + (getTitle().equals("pallet")?"箱子":"产品") + "工厂代码不对";
            MainUtils.showSimpleError(hints);
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(hints);
            return false;
        }

        //中箱管理页面:产品是否打印彩盒
        if(!getTitle().equals("pallet") && !productPrinted(item)){
            String hints = snCode + ":" + "产品没有经过彩盒工序";
            MainUtils.showSimpleError(hints);
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(hints);
            return false;
        }

        //中箱管理页面:产品是否称重
        if(getTitle().equals("cartoonBox") && !productWeighted(item)){
            String hints = snCode + ":" + "产品没有经过称重工序";
            MainUtils.showSimpleError(hints);
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText(hints);
            return false;
        }


        return true;
    }

    //public abstract boolean itemValid(String snCode);

    public abstract boolean storage(C c, List<String> itemCodes) throws BizException,RequestException;

    public abstract boolean doXiaomiPrint(C c);
    public abstract boolean doSuShiPrint(C c);

    public abstract String calPartSNCode(C c,String version);
    public abstract void setContainerCreator(C c, String creator);
    public abstract void setContainerStreamCode(C c, String streamCode);
    public abstract void setContainerSNCode(C c, String snCode);
    public abstract void setContainerNickName(C c, String nickName);


    public boolean newContainer(){
            formInstance.persist();          
            String text = FormUtils.form2JsonStr(formInstance);

            Log.info(text);
            //Log.info("text:======"+text);
            currentContainer = JSON.parseObject(text, getTitle().equals("pallet")?PalletSN.class:CartoonBoxSN.class);
            try{
                //把产品信息拷贝到容器,修复被覆盖的工厂代码
                String factoryCode = getContainerFactoryCode(currentContainer);
                BeanUtils.copyProperties(currentContainer,currentProductInfo);
                setContainerFactoryCode(currentContainer,factoryCode);
            }catch(Exception e){
                Log.error("",e);
            }

            String partSNCode = calPartSNCode(currentContainer,version);

            //Pair<Integer,Integer> pair= MainUtils.getStream(partSNCode,"0", "2", 1);
            List<Integer> pair;
            try{
                pair = CounterAPI.getNum(partSNCode, 2, false, 1);
            }catch(BizException e){
                Log.error("",e);
                MainUtils.showSimpleError("申请流水失败:"+e.getMessage());
                String buttonStartName = getTitle().equals("pallet")?"开始装板":"开始装箱";
                buttonSNCode.setText(buttonStartName);
                return false;
            }
            if (pair==null) MainUtils.showSimpleError("获取流水号失败!");
            long start = pair.get(0);

            //小米中箱流水号6位,小米栈板流水号6位
            //素士中箱流水号4位,素士栈板流水号3位
            String streamCode = null;
            if(version.equals("sushi")){
                streamCode = getTitle().equals("pallet")?String.format("%03d",start):String.format("%04d",start);
            }else if(version.equals("xiaomi")){
                streamCode = String.format("%06d",start);
            }else{
                MainUtils.showSimpleError("版本号错误导致流水号错误!");
                return false;
            }

            String sNCode = partSNCode + streamCode;
            setContainerCreator(currentContainer,creator);
            setContainerStreamCode(currentContainer,streamCode);
            setContainerSNCode(currentContainer,sNCode);
            String sNCodeNickName = sNCode;
            if (version.equals("xiaomi")){
                sNCodeNickName  = partSNCode.substring(0,partSNCode.length()-getContainerFactoryCode(currentContainer).length()) 
                    + streamCode + getContainerFactoryCode(currentContainer);
            }
            setContainerNickName(currentContainer,sNCodeNickName);


            txtContainer.setText(sNCodeNickName);
            txtContainer.setEditable(false);
            listView.getItems().clear();
            //buttonSNCode.disableProperty().unbind();
            jobStatus.setText("已装板: "+listView.getItems().size()+" / "+String.valueOf(quantity.getValue())+"个");
            txtItem.setEditable(true);
            MainUtils.saveForm(getTitle(),"Form", formInstance, version); // 保存表单
            //MainUtils.addOrUpdateLocalCache(getTitle(), getTitle()+"SN", sNCodeNickName, version);
            return true;
    }


    /**
     *
     * 打印标签
     * */
    public boolean printLabel(C c, String version){
        String printerName = MainUtils.getPrinterName();
        if(printerName==null || printerName.isEmpty()){
            MainUtils.showSimpleError("打印机没配置!");
            return false;
        }
        String printerDPI = MainUtils.getPrinterDPI();
        if(printerDPI==null || printerDPI.isEmpty()){
            MainUtils.showSimpleError("打印机DPI没配置!");
            return false;
        }

        printer = new ZplPrinter(printerName,printerDPI);
        if(version.equals("xiaomi")){
            try{
                xiaomiPrinterTemplate = MainUtils.getXiaoMiTemplateByProductId(getProductId(c), productInfoList);
            }catch(Exception e){
                Log.error("",e);
                MainUtils.showSimpleError("打印模板设置!");
            }
            return doXiaomiPrint(c);
        }else{
            return doSuShiPrint(c);
        }

    };

    /**
     * 新装箱流程
     * */
    public void newPack(){
        String snCode = txtItem.getText();
        if(snCode !=null && !snCode.isEmpty()){
            txtItem.clear();

            if (listView.getItems().contains(snCode)){
                MainUtils.showSimpleError("重复SN码");
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText("重复箱子SN码");
                return;
            }
            if (listView.getItems().size()>=quantity.getValue()){
                MainUtils.showSimpleError("装满了");
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText("装满了!");
                return;
            }

            if(!itemValid(snCode)){
                //MainUtils.showSimpleError("校验不通过:"+snCode);
                //lblMessage.setTextFill(Color.RED);
                //lblMessage.setText("校验不通过"+snCode);
                return;
            }
            
            if (getTitle().equals("weightAndBox")){
                weightAndPackAndSubmit(snCode);
                return;
            }
            packAndSubmit(snCode);
        }
    }

    public void weightAndPackAndSubmit(String snCode){};

    public void packAndSubmit(String snCode){
        listView.getItems().add(0,snCode);
        jobStatus.setText("已装: "+listView.getItems().size()+" / "+String.valueOf(quantity.getValue())+"个");
        //MainUtils.savePane(getTitle(), getTitle()+"Pane", gridPane, version);
        if(listView.getItems().size()==quantity.getValue()){
            //jobStatus.setText("已装: "+listView.getItems().size()+" / "+String.valueOf(quantity.getValue())+"个");
            List<String> itemCodes = listView.getItems();
            boolean op = false;
            try{
                op = storage(currentContainer, itemCodes);
            }catch (RequestException e) {
            	listView.getItems().remove(0);
                jobStatus.setText("已装: "+listView.getItems().size()+" / "+String.valueOf(quantity.getValue())+"个");
				if(e.isConnectTimeout()) {
					MainUtils.showSimpleError("网络异常,请重新扫码");
					return;
				}else {					
					MainUtils.showSimpleError(ServerAPI.toBizException(null, e).getMessage());
					return;
				}            	
			}catch(BizException e){
                Log.error("",e);                
                MainUtils.showSimpleError(e.getMessage());
                listView.getItems().remove(0);
                jobStatus.setText("已装: "+listView.getItems().size()+" / "+String.valueOf(quantity.getValue())+"个");
                return;
            }
            if (!op) {
                MainUtils.showSimpleError("SN录入失败!请重新扫最后一个");
                //按照观浩的意见失败后,listView减少最后一个item
                listView.getItems().remove(0);
                jobStatus.setText("已装: "+listView.getItems().size()+" / "+String.valueOf(quantity.getValue())+"个");
                return;
            }
            //MainUtils.savePane("pallet", "palletPane", gridPane, version);
            MainUtils.showSimpleSuccess("装箱成功");
            if (printLabel(currentContainer,version)){
                MainUtils.showSimpleSuccess("打印成功");
            }else{
                MainUtils.showCopyAbleErrorDialog(workbench, "打印失败,请补打:",getContainerNickName(currentContainer));
            }
            listView.getItems().clear();
            jobStatus.setText("已装: "+listView.getItems().size()+" / "+String.valueOf(quantity.getValue())+"个");
            //MainUtils.addOrUpdateLocalCache(getTitle(), getTitle()+"SN", getContainerNickName(currentContainer), version);
            newContainer();
        }

    }

    /**
     * 补打标签流程
     * */

    void rePrintLabel(){

        Form form = Form.of(
                Group.of(
                    Field.ofStringType("")
                    .id("container")
                    .label("栈板SN码")
                    .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                    .required("必填")
                    ));


        FormRenderer renderer = new FormRenderer(form);
        MainUtils.searchAndSetControlsLabelWidth(renderer, 35);
        ButtonType okayButton = MainUtils.okayButton();
        ButtonType cancelButton = MainUtils.cancelButton();

        WorkbenchDialog dialog = WorkbenchDialog.builder(
                "补打标签", renderer, okayButton, cancelButton)
            .showButtonsBar(true)
            .onResult(buttonType -> {
                if (buttonType.equals(okayButton)) {
                    form.persist();
                    C container = null;
                    String containerId = FormUtils.getDataField(form, "container").getValue().toString();
                    Log.info("containId :"+containerId);
                    try{
                        container = getContainer(containerId);
                    }catch(RequestException e){
                        MainUtils.showSimpleError("网络异常!");
                        Log.error("",e);
                        return;
                    }
                    if (container==null){
                        String containerName = getTitle().equals("pallet")?"栈板":"中箱";
                        MainUtils.showSimpleError(containerName+"不存在!");
                        return ;
                    }
                    if (printLabel(container,version)){
                        MainUtils.showSimpleSuccess("打印成功");
                    }else{
                        MainUtils.showCopyAbleErrorDialog(workbench, "打印失败,请补打:",getContainerNickName(container));
                    }
                }
            })
        .build();
        dialog.setOnShown(event1 -> {
            dialog.getButton(okayButton).ifPresent(
                    button -> {
                        button.disableProperty().bind(form.persistableProperty().not());
                    });
        });
        workbench.showDialog(dialog);

    }

    boolean userVerify(String userName, String pwd){
        try{
            UserInfo user = GlobalConfig.getInstance().findUser(userName, pwd);
            if (!user.getMygroup().equals("工程单位")){
                MainUtils.showSimpleError("非工程单位用户");
                return false;
            }
            return true;
        }catch(BizException e){
            MainUtils.showSimpleError("异常:"+e.getMessage());
            Log.error("",e);
            return false;
        }
    }

    /**
     * 容器可以删除? 也就是栈板或者中箱能是否允许删除
     * */
    public abstract boolean containerDelAble(C c);
    public abstract boolean clearContainer(C c) throws RequestException;
    public abstract boolean delContainer(C c) throws RequestException;

    /**
     *
     * 删除标签流程
     * */

    void delLabel(){

        String name = getTitle().equals("pallet")?"栈板":"中箱";
        StringProperty nickName = new SimpleStringProperty("");
        StringProperty userName = new SimpleStringProperty("");
        StringProperty pwd = new SimpleStringProperty("");
        Form form = Form.of(
                Group.of(
                    Field.ofStringType(nickName)
                    .id("container")
                    .label(name+"SN码")
                    .validate(LettersOrNumbersValidator.justLettersOrNumber("只能是字母跟数字"))
                    .required("必填"),
                    Field.ofStringType(userName)
                    .id("userName")
                    .label("工程用户")
                    .required("必填"),
                    Field.ofStringType(pwd)
                    .id("passwd")
                    .label("密码")
                    .required("必填")

                    ));
        //form.binding(BindingMode.CONTINUOUS);
        FormRenderer renderer = new FormRenderer(form);
        MainUtils.searchAndSetControlsLabelWidth(renderer, 35);
        ButtonType okayButton = MainUtils.okayButton();
        ButtonType cancelButton = MainUtils.cancelButton();
        WorkbenchDialog dialog = WorkbenchDialog.builder(
                "作废标签", renderer, okayButton, cancelButton)
            .showButtonsBar(true)
            .onResult(buttonType -> {
                if (buttonType.equals(okayButton)) {
                    form.persist();
                    if(!userVerify(userName.getValue(), pwd.getValue())){
                        return;
                    }
                    C container;
                    try{
                        container = getContainer(nickName.getValue());
                    }catch(RequestException e){
                        Log.error("",e);
                        MainUtils.showSimpleError("网络异常，稍后再试");
                        return;
                    }
                    if(container==null) {
                    	MainUtils.showSimpleError(nickName.getValue()+"不存在");
                        return;
                    }
                    if(!containerDelAble(container)){
                        return;
                    }

                    //清空容器
                    boolean clear = false ;
                    try{
                        clear = clearContainer(container);
                    }catch(RequestException _e){
                        MainUtils.showSimpleError("网络异常");
                        Log.error("",_e);
                        return;
                    }
                    if(!clear){
                        MainUtils.showSimpleError("清空"+name+"失败!");
                        return;
                    }
                    //删除容器
                    boolean del = false;
                    try{
                        del = delContainer(container);
                    }catch(RequestException _e){
                        MainUtils.showSimpleError("网络异常");
                        Log.error("",_e);
                        return;
                    }
                    if(!del){
                        MainUtils.showSimpleError("删除"+name+"失败!");  
                        return;
                    }
                    MainUtils.showSimpleSuccess("删除"+name+"成功!");  
                }
            })
        .build();
        dialog.setOnShown(event1 -> {
            dialog.getButton(okayButton).ifPresent(
                    button -> {
                        button.disableProperty().bind(form.persistableProperty().not());
                    });
        });
        workbench.showDialog(dialog);

    }

    public String version;
    public Workbench workbench;
    public ObservableList<ProductInfo> productInfoList;
    public List<String> productList;
    public ScrollPane scrollContent;
    public Form formInstance;
    public SimpleIntegerProperty quantity;
    public TextField txtItem;
    public TextField txtContainer;
    public Label jobStatus;
    public ListView<String> listView;
    public GridPane controls;
    public Button buttonSNCode;
    public Label lblMessage;
    public GridPane gridPane;
    public ProductInfo currentProductInfo;
    public C currentContainer;
    public String xiaomiPrinterTemplate;
    public ZplPrinter printer;
    public String factoryCode;
    public String creator;
    
    public void init(Workbench workbench){

        this.workbench = workbench;


        productInfoList = GlobalConfig.getInstance().getAllProductInfos();
        productList = new ArrayList<String>();
        //Log.info(productInfoList);
        for (int i=0; i<productInfoList.size();i++){
            ProductInfo productInfo = productInfoList.get(i);
            String item =  i + ": "+ productInfo.getProductId() + "  " + productInfo.getProductName();
            productList.add(item);
        }

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

        formInstance = getForm(version);
        FormRenderer formRenderer = new FormRenderer(formInstance);

        remember(); //恢复表单记忆


        quantity = (SimpleIntegerProperty)FormUtils.getDataField(formInstance, "quantity");


        VBox root = new VBox();
        VBox cartoonBox = new VBox();
        //cartoonBox.getStyleClass().add("bordered");


        scrollContent.setContent(root);


        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20,20,20,20));
        gridPane.getStyleClass().add("bordered_all");
        cartoonBox.setStyle("-fx-padding: 0 20 0 20;");
        gridPane.setStyle("-fx-padding: 20 20 20 20;");
        gridPane.setVgap(10);

        Label lbItem = new Label(getTitle().equals("pallet")?"箱子SN码:":"产品SN码");
        txtItem = new TextField();
        txtItem.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Label lbContainer = new Label(getTitle().equals("pallet")?"栈板SN码:":"箱子SN码");
        txtContainer = new TextField();
        txtContainer.setEditable(false);
        txtContainer.prefHeightProperty().bind(txtItem.heightProperty());
        txtContainer.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));


        gridPane.add(lbItem, 0, 1);
        gridPane.add(txtItem, 1, 1);
        gridPane.add(lbContainer, 0, 0);
        gridPane.add(txtContainer, 1, 0);


        Label lbItemStatus = new Label(getTitle().equals("pallet")?"箱子SN列表: ":"产品SN列表: ");

        lblMessage = new Label();
        lblMessage.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(lbItemStatus,0,2);

        GridPane stack_pane = new GridPane();
        jobStatus = new Label("请先设置扫码枪的回车建.");
        jobStatus.setPrefWidth(200);
        stack_pane.add(jobStatus,0,0);
        stack_pane.add(lblMessage,1,0);

        gridPane.add(stack_pane,1,2);

        Label lbItemList = new Label(getTitle().equals("pallet")?"箱子列表: ":"产品列表: ");
        gridPane.add(lbItemList,0,3);



        listView = new ListView<>();
        gridPane.add(listView,1,3);



        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth( 15 );
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth( 85 );


        gridPane.getColumnConstraints().addAll( col1, col2);
        listView.setId(getTitle().equals("pallet")?"boxs":"products");
        txtContainer.setId(getTitle().equals("pallet")?"pallet":"box");

        //MainUtils.recoverPane("pallet", "palletPane", gridPane, version);
        jobStatus.setText("已装箱: "+listView.getItems().size()+" / "+String.valueOf(quantity.getValue())+"个");

        VBox.setVgrow(gridPane, Priority.ALWAYS );
        cartoonBox.getChildren().add(gridPane);
        root.getChildren().addAll(formRenderer,cartoonBox);

        // 左边拦

        controls = new GridPane();

        Label countryLabel = new Label("帮助电话: "+MainUtils.helpPhone);
        VBox bindingInfo = new VBox();
        bindingInfo.setPadding(new Insets(10));
        bindingInfo.getChildren().addAll(countryLabel);
        bindingInfo.setSpacing(10);
        bindingInfo.setPrefWidth(200);
        bindingInfo.getStyleClass().add("bordered");
        controls.add(bindingInfo, 0, 0);

        CheckBox cbTranspose = new CheckBox("纵向打印");
        recoverTranspose(cbTranspose, "pallet", version);
        cbTranspose.setOnAction(e->{
            saveTranspose(cbTranspose,"pallet",version);
        });

        cbTranspose.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cbTranspose, Priority.ALWAYS);

        CheckBox cbNote = new CheckBox("带出中箱");
        recoverNote(cbNote, "pallet", version);
        cbNote.setOnAction(e->{
            saveNote(cbNote,"pallet",version);
        });

        cbNote.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cbNote, Priority.ALWAYS);


        //////////////////////////////////////////////////////////////////////////////

        VBox crudContent = new VBox();

        Button btnLabelReprint = new Button("补打标签");
        btnLabelReprint.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnLabelReprint, Priority.ALWAYS);

        Button btnLabelDel = new Button("作废标签");
        btnLabelDel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnLabelDel, Priority.ALWAYS);

        crudContent.setPadding(new Insets(10));
        if(version.equals("xiaomi")){
            crudContent.getChildren().addAll(btnLabelReprint,btnLabelDel,cbTranspose);//,btnPackingAdjustment);
            if(getTitle().equals("pallet")){
                crudContent.getChildren().add(cbNote);
            }
        }else{
            crudContent.getChildren().addAll(btnLabelReprint,btnLabelDel,cbTranspose);//,btnPackingAdjustment);
        }
        crudContent.setSpacing(10);
        crudContent.setPrefWidth(200);
        crudContent.getStyleClass().add("bordered");

        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        controls.add(crudContent, 0, 3);

        //////////////////////////////////////////////////////////////////////////////

        VBox crudCurrent = new VBox();
        Button buttonSNCode = new Button(getTitle().equals("pallet")?"开始装板":"开始装箱");
        buttonSNCode.setMaxWidth(Double.MAX_VALUE);
        buttonSNCode.getStyleClass().add("save-button");

        Button btnCurrentLabelEmpty = new Button("清空当前栈板");
        btnCurrentLabelEmpty.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnCurrentLabelEmpty, Priority.ALWAYS);

        crudCurrent.setPadding(new Insets(10));
        crudCurrent.getChildren().addAll(buttonSNCode);//,btnCurrentLabelEmpty,btnCurrentLabelReprint,btnCurrentLabelDel,btnCurrentLabelForceDone);
        crudCurrent.setSpacing(10);
        crudCurrent.setPrefWidth(200);
        crudCurrent.getStyleClass().add("bordered");

        controls.setPrefWidth(200);
        controls.getStyleClass().add("controls");
        controls.add(crudCurrent, 0, 2);


        setLeft(controls);

        // binds

        buttonSNCode.disableProperty().bind(formInstance.validProperty().not());

        String buttonStartName = getTitle().equals("pallet")?"开始装板":"开始装箱";
        String buttonEndName = getTitle().equals("pallet")?"结束装板":"结束装箱";

        if(listView.getItems().isEmpty()){ //如果物品列表是空的?
            txtItem.clear();
            buttonSNCode.setText(buttonStartName);
        }

        if(buttonSNCode.getText().equals(buttonEndName)){//之前的打包中断
            formInstance.getFields().forEach(s->s.editable(false));
            txtItem.setEditable(true);
        }
        buttonSNCode.setOnAction(event -> {
            if(buttonSNCode.getText().equals(buttonStartName)){
                formInstance.persist();
                MainUtils.addOrUpdateLocalCache("palletSN", "factoryCode", factoryCode,version);
                buttonSNCode.setText(buttonEndName);
                formInstance.getFields().forEach(s->s.editable(false));
                txtItem.setEditable(true);
                listView.getItems().clear();
                newContainer();
            }else{
                buttonSNCode.setText(buttonStartName);
                formInstance.getFields().forEach(s->s.editable(true));
                txtItem.clear();
                txtItem.setEditable(false);
                listView.getItems().clear();
                txtContainer.clear();
            }
        });

        txtItem.setOnKeyPressed(e->{
        	if(!txtItem.isEditable()){
        		return;
        	}		
            if(e.getCode() != KeyCode.ENTER){
                return;
            }
            newPack();
        });


        btnLabelReprint.setOnAction(e->{
            rePrintLabel();
        });


        btnLabelDel.setOnAction(e->{
            delLabel();
        });


        /**
        btnUpdate.setOnAction(event -> {
            // Building the dialog with the CheckBox as content

            GridPane gridPane1 = new GridPane();
            gridPane1.setHgap(10);
            gridPane1.setVgap(5);

            Label lbBoxSN1 = new Label("栈板SN码");
            gridPane1.add(lbBoxSN1, 0, 0);
            TextField txtBoxSNAdj = new TextField();
            txtBoxSNAdj.setPrefWidth(300);
            gridPane1.add(txtBoxSNAdj, 1, 0);

            ButtonType okay = MainUtils.okayButton();
            ButtonType cancel = MainUtils.cancelButton();

            WorkbenchDialog dialog = WorkbenchDialog.builder(
                    "标签修改", gridPane1, okay, cancel)
                //.blocking(false)
                //.maximized(true)
                .showButtonsBar(true)
                .onResult(buttonType -> {
                    if (buttonType.equals(okay)) {
                        ButtonType okay2 = MainUtils.okayButton();
                        ButtonType cancel2 = MainUtils.cancelButton();

                        PalletSN _palletSN = null;
                        try{
                            _palletSN = getPalletSN(txtBoxSNAdj.getText());
                        }catch(RequestException _e){
                            MainUtils.showSimpleError("网络异常,栈板无法查询!");
                            Log.error("",_e);
                            return;
                        }
                        if (_palletSN==null){
                            MainUtils.showSimpleError("非法栈板SN码!");
                            return;
                        }
                        formUpdate = updateFormByVersion(_palletSN,version);
                        FormRenderer rendererUpdate = new FormRenderer(formUpdate);
                        //MainUtils.searchAndSetControlsLabelWidth(rendererUpdate, 35);


                        WorkbenchDialog dialog2 = WorkbenchDialog.builder(
                                "修改栈板", rendererUpdate, okay2, cancel2)
                            .showButtonsBar(true)
                            .onResult(buttonType2 -> {
                                if (buttonType2.equals(okay2)) {
                                    formUpdate.persist();
                                    String text = FormUtils.form2JsonStr(formUpdate);
                                    PalletSN _pallet= JSON.parseObject(text, PalletSN.class);
                                    PalletSN __palletSN = null;
                                    try{
                                        __palletSN = getPalletSN(txtBoxSNAdj.getText());
                                    }catch(RequestException _e){
                                        MainUtils.showSimpleError("网络异常,栈板无法查询!");
                                        Log.error("",_e);
                                        return;
                                    }
                                    if (__palletSN==null){
                                        MainUtils.showSimpleError("非法栈板SN码!");
                                        return;
                                    }
                                    _pallet.setSNCode(__palletSN.getSNCode());
                                    if (updatePalletSN(_pallet)){
                                        MainUtils.showSimpleSuccess("更新成功!");
                                    }else{
                                        MainUtils.showSimpleError("更新失败!");
                                    }
                                }
                            })
                        .build();
                        dialog2.setOnShown(event1 -> {
                            dialog2.getButton(okay).ifPresent(
                                    button -> {
                                        button.disableProperty().bind(formUpdate.persistableProperty().not());
                                    });
                        });
                        workbench.showDialog(dialog2);
                    }

                }).build();

            workbench.showDialog(dialog);

        });

        btnPackingAdjustment.setOnAction(e->{
            adjPackage(workbench,"栈板");
        });

        **/




    }




}

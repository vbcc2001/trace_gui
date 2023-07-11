package com.dlsc.workbenchfx.demo.modules.productLaser;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.MainUtils;
import com.dlsc.workbenchfx.demo.modules.productLaser.view.ProductLaserView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.fontawesome.FontAwesome;
/**
 * @author Dieter Holz
 */
public class ProductLaserModule extends WorkbenchModule {

    private ProductLaserView rootPane;

    public ProductLaserModule(String creator) {
        super("产品镭射", FontAwesome.BARCODE);
        rootPane = new ProductLaserView(creator);
    }
    @Override
    public void init(Workbench workbench) {
    	if(this.getWorkbench()==null) {
    		super.init(workbench);
            rootPane.init(workbench);
    	}        
    }

    @Override
    public Node activate() {
//    	MainUtils.showErrorDialog(MainUtils.workbench, "测试", "123456");
        return rootPane;
    }

}



package com.dlsc.workbenchfx.demo.modules.productInfo;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.productInfo.view.ProductInfoView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.fontawesome.FontAwesome;

/**
 * @author Dieter Holz
 */
public class ProductInfoModule extends WorkbenchModule {


    private ProductInfoView rootPane;   

    public ProductInfoModule(String creator) {
        super("产品设置",FontAwesome.PRODUCT_HUNT);
        rootPane = new ProductInfoView(creator);
        Log.info("construct");
    }
    @Override
    public void init(Workbench workbench) {   
    	if(this.getWorkbench()==null) {
    		super.init(workbench);        
            rootPane.init(workbench);    
    	}          
    }

    @Override
    public Node activate() {//选中页签
    	rootPane.refreshTableView();
        return rootPane;
    }
	@Override
	public void deactivate() {//切换页签
		
	}
	@Override
	public boolean destroy() {//关闭页签
		return true;
	}
    
    
}

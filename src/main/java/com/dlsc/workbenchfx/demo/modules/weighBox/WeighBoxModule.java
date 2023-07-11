package com.dlsc.workbenchfx.demo.modules.weighBox;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.weighBox.view.WeighBoxView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.fontawesome.FontAwesome;
/**
 * @author Dieter Holz
 */
public class WeighBoxModule extends WorkbenchModule {

    private WeighBoxView rootPane;    

    public WeighBoxModule(String creator) {
        super("称重", FontAwesome.CHECK);
        rootPane = new WeighBoxView(creator);
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
    public Node activate() {
    	Log.info("当前模块切到称重模块");
    	rootPane.flushConfig();
        return rootPane;
    }

    @Override
    public void deactivate() {
    	Log.info("称重模块切到后台");    	
    }

    @Override
    public boolean destroy() {
        rootPane.destroy();
        return true;
    }

}



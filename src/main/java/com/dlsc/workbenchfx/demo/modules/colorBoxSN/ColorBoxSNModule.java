package com.dlsc.workbenchfx.demo.modules.colorBoxSN;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.colorBoxSN.view.ColorBoxSNView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.fontawesome.FontAwesome;
/**
 * @author Dieter Holz
 */
public class ColorBoxSNModule extends WorkbenchModule {

    private ColorBoxSNView rootPane;   

    public ColorBoxSNModule(String creator) {
        super("彩盒SN", FontAwesome.INBOX);
        rootPane = new ColorBoxSNView(creator);      
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
        return rootPane;
    }

}



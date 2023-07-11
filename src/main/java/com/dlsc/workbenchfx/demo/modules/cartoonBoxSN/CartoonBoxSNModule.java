package com.dlsc.workbenchfx.demo.modules.cartoonBoxSN;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.view.CartoonBoxSNView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
/**
 * @author Dieter Holz
 */
public class CartoonBoxSNModule extends WorkbenchModule {

    private CartoonBoxSNView rootPane;
    public CartoonBoxSNModule(String creator) {
        super("中箱装箱", MaterialDesign.MDI_BOX);
        rootPane = new CartoonBoxSNView(creator);
        Log.info("construct");
    }
    @Override
    public void init(Workbench workbench) {
        super.init(workbench);
        rootPane.init(workbench);
        Log.info("init");
    }

    @Override
    public Node activate() {
        return rootPane;
    }

}



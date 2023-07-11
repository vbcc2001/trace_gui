package com.dlsc.workbenchfx.demo.modules.palletSN;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.palletSN.view.PalletSNView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
/**
 * @author Dieter Holz
 */
public class PalletSNModule extends WorkbenchModule {

    private PalletSNView rootPane;
    public PalletSNModule(String creator) {
        super("栈板管理", MaterialDesign.MDI_POUND_BOX);
        rootPane = new PalletSNView(creator);
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



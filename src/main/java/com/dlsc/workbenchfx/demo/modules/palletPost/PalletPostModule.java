package com.dlsc.workbenchfx.demo.modules.palletPost;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.palletPost.view.PalletPostView;
import com.dlsc.workbenchfx.model.WorkbenchModule;

import javafx.scene.Node;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
/**
 * @author Dieter Holz
 */
public class PalletPostModule extends WorkbenchModule {
    private PalletPostView rootPane = null;

    public PalletPostModule() {
        super("目的仓",MaterialDesign.MDI_HOME);
        rootPane = new PalletPostView();

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



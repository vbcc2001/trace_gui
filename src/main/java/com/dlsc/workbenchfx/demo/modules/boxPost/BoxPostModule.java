package com.dlsc.workbenchfx.demo.modules.boxPost;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.boxPost.view.BoxPostView;
import com.dlsc.workbenchfx.model.WorkbenchModule;

import javafx.scene.Node;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
/**
 * @author Dieter Holz
 */
public class BoxPostModule extends WorkbenchModule {
    private BoxPostView rootPane = null;

    public BoxPostModule() {
        super("中箱物流",MaterialDesign.MDI_CAR);
        rootPane = new BoxPostView();

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



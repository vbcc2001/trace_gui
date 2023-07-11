package com.dlsc.workbenchfx.demo.modules.weightAndBox;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.weightAndBox.view.WeightAndBoxView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
/**
 * @author Dieter Holz
 */
public class WeightAndBoxModule extends WorkbenchModule {

    private WeightAndBoxView rootPane;
    public WeightAndBoxModule(String creator) {
        super("中箱装箱(含称重)", MaterialDesign.MDI_BOX);
        rootPane = new WeightAndBoxView(creator);
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



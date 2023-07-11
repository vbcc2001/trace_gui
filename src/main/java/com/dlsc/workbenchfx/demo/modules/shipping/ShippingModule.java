package com.dlsc.workbenchfx.demo.modules.shipping;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.shipping.view.ShippingView;
import com.dlsc.workbenchfx.demo.modules.product.model.DemoModel;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome.FontAwesome;

/**
 * @author Dieter Holz
 */
public class ShippingModule extends WorkbenchModule {


    private ShippingView rootPane;
    private Workbench workbench;

    public ShippingModule(String creator) {
        super("出货",FontAwesome.SHIP);
        rootPane = new ShippingView(creator);
        Log.info("construct");
    }
    @Override
    public void init(Workbench workbench) {
        super.init(workbench);
        this.workbench = workbench;
        rootPane.init(workbench);
        Log.info("init");
    }

    @Override
    public Node activate() {
        return rootPane;
    }

}

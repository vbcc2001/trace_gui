package com.dlsc.workbenchfx.demo.modules.product;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.product.view.RootPane;
import com.dlsc.workbenchfx.demo.modules.product.model.DemoModel;
import com.dlsc.workbenchfx.model.WorkbenchDialog;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.fontawesome.FontAwesome;

/**
 * @author Dieter Holz
 */
public class ProductModule extends WorkbenchModule {


    private RootPane rootPane;

    public ProductModule() {
        super("demo", MaterialDesign.MDI_ACCOUNT);
        DemoModel model = new DemoModel();
        rootPane = new RootPane(model);
    }

    @Override
    public Node activate() {
        return rootPane;
    }

}

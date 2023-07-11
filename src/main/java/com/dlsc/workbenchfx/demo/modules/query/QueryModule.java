package com.dlsc.workbenchfx.demo.modules.query;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.productLaser.view.ProductLaserView;
import com.dlsc.workbenchfx.demo.modules.query.view.QueryView;
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
public class QueryModule extends WorkbenchModule {

    private QueryView rootPane;
    private Workbench workbench;

    public QueryModule() {
        super("通用查询", FontAwesome.BARCODE);
        rootPane = new QueryView();
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



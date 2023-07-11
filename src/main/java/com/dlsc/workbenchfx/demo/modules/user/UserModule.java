package com.dlsc.workbenchfx.demo.modules.user;

import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.demo.modules.user.view.UserView;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import javafx.scene.Node;
import org.kordamp.ikonli.fontawesome.FontAwesome;

/**
 * @author Dieter Holz
 */
public class UserModule extends WorkbenchModule {
    private UserView rootPane;

    public UserModule() {
        super("用户管理",FontAwesome.USER);
        rootPane = new UserView();       
    }
    @Override
    public void init(Workbench workbench) {
        super.init(workbench);        
        rootPane.init(workbench);        
    }

    @Override
    public Node activate() {
    	rootPane.refreshTableView();
        return rootPane;
    }

}

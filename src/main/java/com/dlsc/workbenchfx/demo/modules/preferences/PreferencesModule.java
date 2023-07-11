package com.dlsc.workbenchfx.demo.modules.preferences;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.Workbench;
import com.dlsc.workbenchfx.model.WorkbenchModule;
import com.dlsc.workbenchfx.view.controls.ToolbarItem;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

public class PreferencesModule extends WorkbenchModule {

  Preferences preferences;

  public PreferencesModule(Preferences preferences) {
    super("全局配置", FontAwesome.GEAR);
    this.preferences = preferences;

    ToolbarItem save = new ToolbarItem(new FontIcon(MaterialDesign.MDI_CONTENT_SAVE),
        event -> preferences.save());
    ToolbarItem discardChanges =
        new ToolbarItem(new FontIcon(MaterialDesign.MDI_DELETE),
            event -> getWorkbench().showConfirmationDialog("取消修改",
                "你确定取消修改吗?",
                buttonType -> {
                  if (ButtonType.YES.equals(buttonType)) {
                    preferences.discardChanges();
                  }
                })
        );
    getToolbarControlsLeft().addAll(save, discardChanges);
  }

  @Override
  public Node activate() {
    return preferences.getPreferencesFxView();
  }

  @Override
  public void init(Workbench workbench) {
      super.init(workbench);
      preferences.workbench = workbench;
      Log.info("init");
  }


  @Override
  public boolean destroy() {
    //preferences.save();
    return true;
  }
}

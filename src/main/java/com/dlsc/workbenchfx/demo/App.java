package com.dlsc.workbenchfx.demo;

import javafx.stage.Stage;

public class App {
	private static App app;
	private App() {
		
	}
	public final static App getApp() {
		if(app==null)app=new App();
		return app;
	}
	private ExtendedDemo extendedDemo;
	private Stage primaryStage;
	App setController(ExtendedDemo extendedDemo,Stage primaryStage) {
		this.extendedDemo = extendedDemo;
		this.primaryStage = primaryStage;
		return this;
	}
	/**
	 * 
	 * @param clientType 重启后的客户端类型
	 * @param url 重启后的服务地址
	 */
	public void restart(String clientType,String url) {
		if(extendedDemo!=null) {
			extendedDemo.checkConfig(primaryStage, clientType, url);
		}
	}	
}

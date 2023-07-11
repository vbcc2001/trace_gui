package com.dlsc.workbenchfx.demo;

import java.io.InputStream;
import java.util.List;

import com.cg.core.util.Log;
import com.moandjiezana.toml.Toml;
/**
 * Toml配置
 * @author LiangGuanHao
 *
 */
public class TomlConfig {
	private String title;
	private Index index;
	private Version version;    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
	public static class Version{		
		private String number;//客户端版本号或者客户端最低版本号
		private String server;//服务器版本号或者服务器最低版本号
		/**
		 * 
		 * @return 客户端当前版本号
		 */
		public String getNumber() {
			return number;
		}
		
		public Version setNumber(String number) {
			this.number = number;
			return this;
		}
		/**
		 * 
		 * @return 服务器最低版本号
		 */
		public String getServer() {
			return server;
		}

		public Version setServer(String server) {
			this.server = server;
			return this;
		}

		@Override
		public String toString() {
			return "Version [number=" + number + ", server=" + server + "]";
		}
	}
	public static class Index{
		private List<String> moduleNames;
		/**
		 * 
		 * @return 可用模块
		 */
		public List<String> getModuleNames() {
			return moduleNames;
		}

		public void setModuleNames(List<String> moduleNames) {
			this.moduleNames = moduleNames;
		}

		@Override
		public String toString() {
			return "Index [moduleNames=" + moduleNames + "]";
		}
		
	}	

	@Override
	public String toString() {
		return "TomlConfig [title=" + title + ", index=" + index + ", version=" + version + "]";
	}
	private static TomlConfig tomlConfig;
	public static TomlConfig build() {
		if(tomlConfig==null) {
			try(InputStream in=TomlConfig.class.getResourceAsStream("/config.toml");){
				Toml t=new Toml().read(in);
				TomlConfig.tomlConfig=t.to(TomlConfig.class);
				Log.info(tomlConfig.toString());
			}catch (Throwable t) {
				Log.error("配置加载失败",t);
				TomlConfig.tomlConfig=new TomlConfig();
			}
		}
		return tomlConfig;
	}
}

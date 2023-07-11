package com.dlsc.workbenchfx.demo;



import java.util.HashMap;
import java.util.Map;

import com.cg.core.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;


public class LocalCache{        
    private static LocalCache instance = null;  
    private Map<String, Map<String, String>> cache;//dicType->{code->value}

    public static LocalCache getCache(){
        if (instance==null){
            instance = new LocalCache();
            instance.loadCache();
        }
        return instance;
    }

    private LocalCache(){
     
    }
    private File getUserDirectory() {
        return new File(System.getProperty("user.home"));
    }
    private File cacheFile;
    @SuppressWarnings("unchecked")
	private void loadCache() {    	
    	try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(getCacheFile()));){
    		cache=(Map<String, Map<String, String>>) in.readObject();
    		Log.info("Load cache:"+cache);
    	}catch (Throwable t) {
			cache=new HashMap<>();
		}
    }
    private File getCacheFile() {
    	if(cacheFile!=null)return cacheFile;
    	cacheFile=new File(getUserDirectory(), "cache.bin");
    	if(!cacheFile.exists()) {//之前V3测试的本地缓存文件建议手动删除
    		try {
    			cacheFile.createNewFile();				
			} catch (IOException e) {
				Log.warn("本地缓存读写受限", e);				
			}
    		hideCacheFile(true);
    	}
    	Log.debug("cache path:"+cacheFile);
    	return cacheFile;
    }

    public synchronized boolean addOrUpdateDict(String type, String key, String value){
    	Map<String, String> map=cache.get(type);
    	if(map==null) {
    		map=new HashMap<>();
    		cache.put(type, map);
    	}
    	map.put(key, value);//可能会覆盖旧的缓存
    	return saveCache();     
    }
    private boolean saveCache() {
    	hideCacheFile(false);
    	try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(getCacheFile()));){
    		out.writeObject(cache);
    		return true;
    	} catch (Throwable t) {
    		Log.warn("Fail to save cache:"+cache,t);
			return false;
		} finally {
			hideCacheFile(true);//隐藏缓存文件防止误删
		}
    }    
    private void hideCacheFile(boolean hide) {//是否隐藏缓存文件
    	try {
			Files.setAttribute(getCacheFile().toPath(), "dos:hidden", hide);
		} catch (IOException e) {			
			Log.warn("Fail to hide cache file:"+hide);
		}
    }
    
    public String getDict(String type, String key) {
    	Map<String, String> map=cache.get(type);
    	if(map==null)return null;
    	return map.get(key);     
    }
    /**
     * 
     * @return 客户端类型,例如:sushi,xiaomi
     */
    public String getClientType() {
    	return getDict("system", "version");
    }
    /**
     * 
     * @param clientType 客户端类型
     * @return 从缓存中获取服务器地址(不做校验)
     */
    public String getServer(String clientType) {    	
    	return getDict("serverUrl", clientType);
    }
    /**
     * 更新本地缓存的客户端服务地址
     * @param clientType 客户端类型
     * @param serverUrl 目标类型对应的服务地址
     */
    public void saveServerUrl(String clientType,String serverUrl) {
    	addOrUpdateDict("serverUrl", clientType, serverUrl);
    }
    /**
     * 保存客户端启动必需的初始配置
     * @param clientType 客户端类型,例如:sushi , xiaomi
     * @param server 客户端对应的服务器地址
     */
    public void saveInitConfig(String clientType,String server) {
    	if(addOrUpdateDict("system", "version", clientType)) {//保存clientType
    		Log.info("Cache client type:"+clientType);
    	}else {
    		Log.warn("Fail to cache client type:"+clientType);
    	}
    	if(addOrUpdateDict("serverUrl", clientType, server)){//保存server
    		Log.info("Cache server url:"+server);
    	}else {
    		Log.warn("Fail to cache server url:"+server);
    	}    	
    }
}

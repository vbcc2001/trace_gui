package com.dlsc.workbenchfx.demo.api;

import java.util.Collections;
import java.util.Map;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.demo.TomlConfig.Version;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;

public abstract class ServerAPI {
	public final static String getServer() {
		return GlobalConfig.getInstance().getServerUrl();
	}
	/**
	 * 检查服务地址和客户端类型是否配套,顺便检查服务地址是否有效
	 * @param serverUrl 服务地址
	 * @param clientType 客户端类型 
	 * @throws BizException 校验失败
	 */
	public final static void checkClientType(String serverUrl,String clientType) throws BizException {
		try {
			String sct=new RestUtil(serverUrl).setConnetTimeout(1000).post("/server/clientType").getDataString();	
			if(!clientType.equals(sct))
				throw new BizException("服务器不是"+GlobalConfig.getInstance().getClientName(clientType)+"服务器:"+serverUrl);
		} catch (RequestException e) {
			throw toBizException(serverUrl, e);
		}		
	}
	public final static BizException toBizException(String serverUrl,RequestException e) {
		if(serverUrl==null||serverUrl.isBlank())serverUrl=getServer();
		if(e.is404()) {
			return new BizException("服务器版本过低，请先升级服务器:"+serverUrl);
		}else if(e.isConnectRefuse()) {
			return  new BizException("服务器地址错误,拒绝连接:"+serverUrl);
		}else if(e.isConnectTimeout()) {
			return new BizException("服务器连接超时,请稍后再试:"+serverUrl);
		}else {
			Log.error(serverUrl, e);
			return new BizException("服务器地址无效:"+serverUrl);
		}
	}
	/**
	 * 
	 * 检查服务器版本和客户端版本是否配套
	 * @param serverUrl 服务器地址
	 * @param clientVersion 客户端版本号
	 * @return 记录服务器版本号和客户端最低版本号的Version
	 * @throws BizException 校验失败
	 */
	public final static Version getAndCheckServerVersion(String serverUrl,Version clientVersion) throws BizException {
		Map<String, String> info=null;
		try {
			info=new RestUtil(serverUrl).setConnetTimeout(1000).get("/server/ver").getDataMap(String.class);			
		} catch (BizException e) {
			Log.warn("/server/ver", e);
			info=Collections.emptyMap();
		} catch (RequestException e) {
			throw toBizException(serverUrl, e);
		}
		String sVer=info.get("server");//服务器当前版本号
		String cVer=info.get("client");//服务器要求客户端最低版本号
		if(info.isEmpty()||sVer==null||cVer==null)
			throw new BizException("服务器版本过低，请先升级服务器:"+serverUrl);
		if(sVer.compareTo(clientVersion.getServer())<0)
			throw new BizException("服务器版本低于"+clientVersion.getServer()+",请先升级服务器:"+serverUrl);		
		return new Version().setServer(sVer).setNumber(cVer);
	}
	public final static String getClientType(String serverUrl) throws RequestException {
		try {
			 return new RestUtil(serverUrl).setConnetTimeout(1000).post("/server/clientType").getDataString();		
		} catch (BizException e) {
			Log.warn("/server/clientType", e);
			return null;
		}
	}
}

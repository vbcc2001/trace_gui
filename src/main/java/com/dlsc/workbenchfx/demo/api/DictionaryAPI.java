package com.dlsc.workbenchfx.demo.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.demo.Dictionary;

public class DictionaryAPI extends ServerAPI{
	
	public static boolean addOrUpdate(String dicType,String code,String value) throws RequestException {
		final Map<String, String> body=Map.of("dicType",dicType,"code",code,"text",value);
		try {
			new RestUtil(getServer()).post("/dic/saveDic", body);
			return true;
		}catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
			Log.warn("/dic/saveDic "+body.toString(),e);
			return false;
		}catch (RequestException e) {
			if(e.isConnectTimeout()) {//自动重连一次
				return addOrUpdate(dicType, code, value);
			}
			throw e;
		}
	}
	/**
	 * 查询某个类型的所有字典项，超时自动重连1次
	 * @param dicType 字典类型
	 * @return 目标类型的所有字典项,永不为null
	 * @throws RequestException 网络异常
	 */
	public static List<Dictionary> getByType(String dicType) throws RequestException{
		return getByType(dicType, 1);
	}
	/**
	 * 查询某个类型的所有字典项，超时自动重连最多tryCount次
	 * @param dicType 字典类型
	 * @param tryCount 超时自动重连最大次数
	 * @return 目标类型的所有字典项,永不为null
	 * @throws RequestException 网络异常
	 */
	public static List<Dictionary> getByType(String dicType,int tryCount) throws RequestException{
		try {
			List<Dictionary> list=new RestUtil(getServer()).post("/dic/getByType?dicType="+dicType).getDataList(Dictionary.class);
			//由于历史原因,颜色编码字典在设计时code为中文,text为英文,因为颜色编码查询只有getByType,所以就不强制改回来了
			if(list==null)list=new ArrayList<>();
			return list;
		} catch (RequestException e) {
			if(e.isConnectTimeout()&&tryCount>0) {
				return getByType(dicType, tryCount-1);
			}
			throw e;
		} catch (BizException e) {
			Log.warn("/dic/getByType?dicType="+dicType,e);
			return new ArrayList<>();
		}		
	}
}

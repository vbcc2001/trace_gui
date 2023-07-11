package com.dlsc.workbenchfx.demo.modules.productInfo.model;

import java.util.ArrayList;
import java.util.List;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.demo.api.ServerAPI;

/**
 * 产品信息接口类，用于对接后端接口
 * @author LiangGuanHao
 *
 */
public abstract class ProductInfoAPI extends ServerAPI{
	/**
	 * 查询所有产品模板
	 * @return 所有产品信息
	 * @throws RequestException 网络异常
	 */
	public static List<ProductInfo> getAll() throws RequestException{
		return getAll(0);
	}
	/**
	 * 查询所有产品模板
	 * @param tryCount 连接超时自动重连最大次数
	 * @return 所有产品信息,永不为null;
	 * @throws RequestException 网络异常
	 */
	public static List<ProductInfo> getAll(int tryCount) throws RequestException{
		try {
			List<ProductInfo> list=new RestUtil(getServer()).post("/productinfo/getAll").getDataList(ProductInfo.class);
			return list==null?new ArrayList<>():list;
		}catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
			Log.warn("/productinfo/getAll", e);
			return new ArrayList<>();
		}catch (RequestException e) {
			if(e.isConnectTimeout()&&tryCount>0) {//连接超时自动重连
				return getAll(tryCount-1);
			}
			throw e;
		}	
	}
	/**
	 * /productinfo/count
	 * @return 产品信息总数,若业务异常则返回0
	 * @throws RequestException 
	 */
	public static Integer getCount() throws RequestException {
		try {
			return new RestUtil(getServer()).get("/productinfo/count").getData(Integer.class);
		} catch (BizException e) {
			Log.warn("/productinfo/count", e);
			return 0;
		}
	}
	/**
	 * 
	 * @param productInfo 更新对象
	 * @return 产品条件
	 * @throws RequestException 网络异常
	 * @throws BizException 插入异常
	 */
	public static ProductInfo update(ProductInfo productInfo) throws RequestException, BizException {
		return new RestUtil(getServer()).post("/productinfo/update",productInfo).getData(ProductInfo.class);
	}
	
	public static List<ProductInfo> addAll(List<ProductInfo> list) throws RequestException, BizException{
		return new RestUtil(getServer()).post("/productinfo/addAll",list).getDataList(ProductInfo.class);
	}	
	public static boolean delOne(Integer id) throws BizException {
		try {
			return new RestUtil(getServer()).post("/productinfo/delOne?id="+id).getData(Boolean.class);
		} catch (RequestException e) {
			throw toBizException(getServer(), e);
		} catch (BizException e) {
			throw e;
		}
	}
}

package com.dlsc.workbenchfx.demo.modules.productLaser.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.demo.api.ServerAPI;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;

/**
 * 产品信息接口类，用于对接后端接口
 * @author LiangGuanHao
 *
 */
public abstract class ProductLaserAPI extends ServerAPI{
	/**
	 * 
	 * @return 所有产品信息
	 * @throws RequestException 
	 */
	public static List<ProductInfo> getAll() throws RequestException{
		try {
			return new RestUtil(getServer()).post("/productLaser/getAll").getDataList(ProductInfo.class);
		}catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
			Log.warn("/productLaser/getAll",e);
			return null;
		}
	}


    public static List<ProductLaser> getByBoxCode(String cartoonBoxCode) throws RequestException{//兄弟记得传入nickName
        String path = "/productLaser/getByBoxCode?cartoonBoxCode="+cartoonBoxCode;
		try {
			return new RestUtil(getServer()).post(path).getDataList(ProductLaser.class);
		}catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
			Log.warn(path, e);
			return new ArrayList<>();
		}	
    }

    public static List<ProductLaser> getByPalletNickName(String palletNickName) throws RequestException{//兄弟记得传入nickName
        String path = "/productLaser/getByPalletNickName?palletNickName="+palletNickName;
		try {
			return new RestUtil(getServer()).post(path).getDataList(ProductLaser.class);
		}catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
			Log.warn(path, e);
			return new ArrayList<>();
		}	
    }

	
    public Integer getProductLaserCount(String partSNCode) throws RequestException{
    	try {
			return new RestUtil(getServer()).get("/productLaser/count?laserCode="+partSNCode).getData(Integer.class);
		} catch (BizException e) {
			Log.warn("/productLaser/count?laserCode="+partSNCode,e);
			return 0;
		}    
    }
    
    /**
     * 
     * @param snCode 唯一SN码
     * @return ProductLaser,若snCode为空/不存在/查询异常则返回null
     * @throws RequestException 
     */
    public static ProductLaser getProductLaser(String snCode) throws RequestException{
    	try {
    		if(snCode==null||snCode.isBlank())return null;
			return new RestUtil(getServer()).get("/productLaser/getBySN?SNCode="+snCode).getData(ProductLaser.class);
		} catch (BizException e) {
			Log.warn("/productLaser/getBySN?SNCode="+snCode,e);
			return null;
		}     
    }
    
    public static boolean addLasers(Collection<ProductLaser> laser) throws RequestException {
    	try {
    		if(laser==null||laser.isEmpty())return false;
    		new RestUtil(getServer()).post("/productLaser/addAll", laser);
    		return true;
		} catch (BizException e) {
			Log.warn("/productLaser/addAll",e);
			return false;
		}  
    }

	//@PostMapping("/clearBoxCode") // {{server}}/productLaser/clearBoxCode?boxCode=
    public static int clearBoxCode(String boxCode) throws RequestException{
        if(boxCode==null||boxCode.isEmpty())return 0;
        String path = "/productLaser/clearBoxCode?boxCode="+boxCode;
        try {
            int num = new RestUtil(getServer()).post(path).getData(int.class);
            return num;
        } catch (BizException e) {
            Log.warn(path,e);
            return 0;
        }  
    }

	//@PostMapping("/clearShipCode") // {{server}}/productLaser/clearBoxCode?boxCode=
    public static int clearShipCode(String shipCode) throws RequestException{
        if(shipCode==null||shipCode.isEmpty())return 0;
        String path = "/productLaser/clearShipCode?shipCode="+shipCode;
        try {
            int num = new RestUtil(getServer()).post(path).getData(int.class);
            return num;
        } catch (BizException e) {
            Log.warn(path,e);
            return 0;
        }  
    }

	//@PostMapping("/clearShipCodeByBoxNickName")
    public static int clearShipCodeByBoxNickName(String boxNickName) throws RequestException{
        if(boxNickName==null||boxNickName.isEmpty())return 0;
        String path = "/productLaser/clearShipCodeByBoxNickName?boxNickName="+boxNickName;
        try {
            int num = new RestUtil(getServer()).post(path).getData(int.class);
            return num;
        } catch (BizException e) {
            Log.warn(path,e);
            return 0;
        }  
    }

	//@PostMapping("/clearShipCodeByBoxNickName")
    public static int clearShipCodeByPalletNickName(String palletNickName) throws RequestException{
        if(palletNickName==null||palletNickName.isEmpty())return 0;
        String path = "/productLaser/clearShipCodeByPalletNickName?palletNickName="+palletNickName;
        try {
            int num = new RestUtil(getServer()).post(path).getData(int.class);
            return num;
        } catch (BizException e) {
            Log.warn(path,e);
            return 0;
        }  
    }


	//@PostMapping("/get") // {{server}}/productLaser/get
	public static List<ProductLaser> getProductLasers(ProductLaser probe) throws RequestException{
        String path = "/productLaser/get";
		try {
			return new RestUtil(getServer()).post(path,probe).getDataList(ProductLaser.class);
		}catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
			Log.warn(path, e);
			return new ArrayList<>();
		}	

	}
	//@GetMapping("/shippingCount") // {{server}}/productLaser/shippingCount
	public static Integer getShippingCount() throws RequestException{
        String path = "/productLaser/shippingCount";
		try {
			return new RestUtil(getServer()).get(path).getData(Integer.class);
		}catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
			Log.warn(path, e);
			return 0;
		}	

	}

	//@PostMapping("/getShippings") // {{server}}/productLaser/getShippings?pageIndex=&pageSize=
	//List<> getShippings(Integer pageIndex,Integer pageSize,boolean isBox){
    //}	
	/**
	 * 更新产品重量
	 * @param laserId 产品ID
	 * @param weight 产品重量(g)
	 * @return 产品更新结果,永不为null
	 * @throws BizException 产品重量更新失败
	 */
	public static ProductLaser updateWeight(Integer laserId,String weight) throws BizException {
		String api="/productLaser/setWeight?weight="+weight+"&id="+laserId;
		try {			
			ProductLaser laser=new RestUtil(getServer()).post(api).getData(ProductLaser.class);
			if(laser==null||laser.getWeight()==null||!laser.getWeight().equals(weight))
				throw new BizException("称重结果入库失败");
			return laser;
		} catch (RequestException e) {
			throw toBizException(getServer(), e);
		}
	}

}

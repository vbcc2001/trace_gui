package com.dlsc.workbenchfx.demo.api;

import java.util.List;

import java.util.ArrayList;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;


import com.dlsc.workbenchfx.demo.modules.cartoonBoxSN.model.CartoonBoxSN;


public abstract class CartoonBoxAPI extends ServerAPI{
	//@PostMapping("/addAll") // {{server}}/cartoonBox/addAll
	public static List<CartoonBoxSN> addAll(List<CartoonBoxSN> cartoonBoxList) throws RequestException{
        String path = "/cartoonBox/addAll";
		try {
			return new RestUtil(getServer()).post(path,cartoonBoxList).getDataList(CartoonBoxSN.class);
		}catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
			Log.warn(path, e);
			return new ArrayList<>();
		}	

	}
	
	//@PostMapping("/getCodeLike") // {{server}}/cartoonBox/getCodeLike?SNCode=&pageIndex=&pageSize=
	//PageData getBySNCode(String SNCode,Integer pageIndex,Integer pageSize){
	//	if(SNCode==null||SNCode.isEmpty())
	//		throw new RestException("缺少参数SNCode");	
	//	final Pageable pageable=PageRequest.of(pageIndex==null||pageIndex<0?0:pageIndex, pageSize==null||pageSize<0?30:pageSize, Sort.by("streamCode"));//默认升序排序
	//	Page<CartoonBox> page=cartoonBoxRepo.findBySNCode(SNCode,pageable);
	//	return PageData.fromPage(page);
	//}
	
	//@PostMapping("/getByCode") // {{server}}/cartoonBox/getByCode?SNCode=
    public static CartoonBoxSN getBySNCode(String SNCode) throws RequestException {
        String path = "/cartoonBox/getByCode?SNCode="+SNCode;
        try {
            return new RestUtil(getServer()).post(path).getData(CartoonBoxSN.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return null;
        }	
	}
		
	//@PostMapping("/delByCode") // {{server}}/cartoonBox/delByCode?SNCode=
	public static Integer delBySNCode(String SNCode) throws RequestException{
        String path = "/cartoonBox/delByCode?SNCode="+SNCode;
        try {
            return new RestUtil(getServer()).post(path).getData(Integer.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return 0;
        }	
	}

	//@PostMapping("/delById") // {{server}}/cartoonBox/delById
	public static Boolean delById(List<Long> ids) throws RequestException{
        String path = "/cartoonBox/delById";
        try {
            new RestUtil(getServer()).post(path,ids);
            return true;
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return false;
        }	
	}
	
	//@PostMapping("/updateById") // {{server}}/cartoonBox/updateById
	public static Boolean updateById(CartoonBoxSN cartoonBox) throws RequestException{
        String path = "/cartoonBox/updateById";
        try {
            new RestUtil(getServer()).post(path,cartoonBox);
            return true;
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return false;
        }	
	}
	
	//@PostMapping("/updateByCode") // {{server}}/cartoonBox/updateByCode
	public static Boolean updateBySNCode(CartoonBoxSN cartoonBox) throws RequestException{
        String path = "/cartoonBox/updateByCode";
        try {
            new RestUtil(getServer()).post(path,cartoonBox).getData(CartoonBoxSN.class);
            return true;
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return false;
        }	
	}

	//@GetMapping("/count") // {{server}}/cartoonBox/count?SNCode=
	public static Long getCount(String SNCode) throws RequestException{
        String path = "/cartoonBox/count?SNCode="+SNCode;
        try {
            return new RestUtil(getServer()).get(path).getData(Long.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return 0L;
        }	
	}
	//@PostMapping("/getSNByPalletCode") // {{server}}/cartoonBox/getSNByPalletCode?palletCode=
	public static List<CartoonBoxSN> getCartoonBoxByPallet(String palletCode)throws RequestException{//记得传入的是nickName
        String path = "/cartoonBox/getSNByPalletCode?palletCode="+palletCode;
        try {
            return new RestUtil(getServer()).post(path).getDataList(CartoonBoxSN.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return new ArrayList<>();
        }	
	}
	
	//@PostMapping("/clearPalletCode") // {{server}}/cartoonBox/clearPalletCode?palletCode=
	public static int clearPalletCode(String palletCode) throws RequestException {//记得传入的是nickName
        String path = "/cartoonBox/clearPalletCode?palletCode="+palletCode;
        try {
            return new RestUtil(getServer()).post(path).getData(int.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return 0;
        }	
	}


	//@PostMapping("/get") // {{server}}/cartoonBox/get
	public static List<CartoonBoxSN> getCartoonBox(CartoonBoxSN probe) throws RequestException{
        String path = "/cartoonBox/get";
        try {
            return new RestUtil(getServer()).post(path,probe).getDataList(CartoonBoxSN.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return new ArrayList<>();
        }	
	}

	//@PostMapping("/byNickName")
	public static CartoonBoxSN byNickName(String nickName) throws RequestException{// {{server}}/cartoonBox/byNickName?nickName=xx
        String path = "/cartoonBox/byNickName?nickName="+nickName;
        try {
            return new RestUtil(getServer()).post(path).getData(CartoonBoxSN.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return null;
        }	
	}
	public static boolean inputLaser(CartoonBoxSN box,List<String> laserSNCodes) throws BizException, RequestException {
		String path = "/cartoonBox/inputLaser";
		box.setLaserSNCodes(laserSNCodes);
		try {
			return new RestUtil(getServer()).post(path,box).getData(Boolean.class);
		}  catch (BizException e) {
			Log.warn(path, e);
			throw e;
		}		
	}

	//@PostMapping("/bindShip")// {{server}}/cartoonBox/bindShip?shippingCode=xx  [boxCode1,...]
	public static int bindShip(String shippingCode, String shippingDate, CartoonBoxSN box) throws RequestException{//出货单号批量绑定中箱
        String path = "/cartoonBox/bindShip?shippingCode="+shippingCode+"&shippingDate="+shippingDate;
        try {
            return new RestUtil(getServer()).post(path,box).getData(int.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return 0;
        }	

	}

}

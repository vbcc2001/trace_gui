package com.dlsc.workbenchfx.demo.api;

import java.util.List;

import java.util.ArrayList;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.demo.modules.palletSN.model.PalletSN;






public abstract class PalletAPI extends ServerAPI{
	//@PostMapping("/addAll") // {{server}}/pallet/addAll
    public static List<PalletSN> addAll(List<PalletSN> palletList) throws RequestException{
        String path = "/pallet/addAll";
        try {
            return new RestUtil(getServer()).post(path,palletList).getDataList(PalletSN.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return new ArrayList<>();
        }	
    }

	//@PostMapping("/getCodeLike") // {{server}}/pallet/getCodeLike?SNCode=&pageIndex=&pageSize=
	//PageData getBySNCode(String SNCode,Integer pageIndex,Integer pageSize){
	//	if(SNCode==null||SNCode.isEmpty())
	//		throw new RestException("缺少参数SNCode");	
	//	final Pageable pageable=PageRequest.of(pageIndex==null||pageIndex<0?0:pageIndex, pageSize==null||pageSize<0?30:pageSize, Sort.by("streamCode"));//默认升序排序
	//	Page<Pallet> page=palletRepo.findBySNCode(SNCode,pageable);
	//	return PageData.fromPage(page);
	//}
	
	//@PostMapping("/getByCode") // {{server}}/pallet/getByCode?SNCode=
	public static PalletSN getBySNCode(String SNCode) throws RequestException{
        String path = "/pallet/getByCode?SNCode="+SNCode;
        try {
            return new RestUtil(getServer()).post(path).getData(PalletSN.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return null;
        }	

	}
		
	//@PostMapping("/delByCode") // {{server}}/pallet/delByCode?SNCode=
	public static Integer delBySNCode(String SNCode) throws RequestException{
        String path = "/pallet/delByCode?SNCode="+SNCode;
        try {
            return new RestUtil(getServer()).post(path).getData(Integer.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return 0;
        }	

	}

	//@PostMapping("/delById") // {{server}}/pallet/delById
    public static Boolean delById(List<Long> ids) throws RequestException{
        String path = "/pallet/delById";
        try {
            new RestUtil(getServer()).post(path,ids);
            return true;
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return false;
        }	
    }	

	//@PostMapping("/updateById") // {{server}}/pallet/updateById
	public static Boolean updateById(PalletSN pallet) throws RequestException{
        String path = "/pallet/updateById";
        try {
            new RestUtil(getServer()).post(path,pallet);
            return true;
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return false;
        }	

	}
	
	//@PostMapping("/updateByCode") // {{server}}/pallet/updateById
	public static Boolean updateBySNCode(PalletSN pallet)throws RequestException {
        String path = "/pallet/updateByCode";
        try {
            new RestUtil(getServer()).post(path,pallet);
            return true;
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return false;
        }	
	}
	
	//@GetMapping("/count") // {{server}}/pallet/count?SNCode=
	public static Long getCount(String SNCode) throws RequestException{
        String path = "/pallet/count?SNCode="+SNCode;
        try {
            return new RestUtil(getServer()).get(path).getData(Long.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return 0L;
        }	

	}

	//@PostMapping("/get") // {{server}}/pallet/get
	public static List<PalletSN> getPallet(PalletSN probe) throws RequestException{
        String path = "/pallet/get";
        try {
            return new RestUtil(getServer()).post(path,probe).getDataList(PalletSN.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return new ArrayList<>();
        }	

	}
	public static List<PalletSN> getByNickName(String nickName) throws RequestException{
		if(nickName==null||nickName.isBlank()) {
			Log.warn("栈板SN码不能为空");
			return new ArrayList<>();
		}
		return getPallet(new PalletSN().setNickName(nickName));		
	}
	public static boolean inputBox(PalletSN pallet,List<String> boxNickNames) throws BizException {
		String path = "/pallet/inputBox";
		pallet.setBoxNickNames(boxNickNames);
		try {
			return new RestUtil(getServer()).post(path,pallet).getData(Boolean.class);
		} catch (RequestException e) {
			throw toBizException(getServer(), e);
		} catch (BizException e) {
			Log.warn(path, e);
			throw e;
		}		
	}

    /**
     * 经常要用到nickName查栈板..
     * */
    public static PalletSN getPalletByNickName(String nickName) throws RequestException {

        PalletSN probe = new PalletSN();
        probe.setNickName(nickName);
        List<PalletSN> list = PalletAPI.getPallet(probe);
        //注意nickName是唯一的,所以只能查出1个
        if (list.size()!=1){
            return null;
        }
        return list.get(0);
    }

	//@PostMapping("/bindShip")// {{server}}/pallet/bindShip?shippingCode=xx  [boxCode1,...]
	public static int bindShip(String shippingCode, String shippingDate, PalletSN pallet) throws RequestException{//出货单号批量绑定中箱
        String path = "/pallet/bindShip?shippingCode="+shippingCode+"&shippingDate="+shippingDate;
        try {
            return new RestUtil(getServer()).post(path,pallet).getData(int.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return 0;
        }	

	}


}

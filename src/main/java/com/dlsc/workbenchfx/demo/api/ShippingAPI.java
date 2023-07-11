
package com.dlsc.workbenchfx.demo.api;


import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.demo.modules.shipping.model.Shipping;


public abstract class ShippingAPI extends ServerAPI{
    //	@PostMapping("/byBox")// {{server}}/shipping/byBox?SNCode=
    public static Shipping byBox(String SNCode) throws RequestException{//按中箱NickName生成出货信息
        String path = "/shipping/byBox?SNCode="+SNCode;
        try {
            return new RestUtil(getServer()).post(path).getData(Shipping.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return null;
        }	
    }
    //@PostMapping("/byPallet")// {{server}}/shipping/byPallet?SNCode=
    public static Shipping byPallet(String SNCode) throws RequestException{//按栈板NickName生成出货信息
        String path = "/shipping/byPallet?SNCode="+SNCode;
        try {
            return new RestUtil(getServer()).post(path).getData(Shipping.class);
        }catch (BizException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            Log.warn(path, e);
            return null;
        }	
    }
}

package com.dlsc.workbenchfx.demo.api;
import java.util.List;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;

/**
 * 统一流水计数器API
 * @author LiangGuanHao
 *
 */
public abstract class CounterAPI extends ServerAPI{
	public final static Integer COUNTER_LASER=0;
	public final static Integer COUNTER_BOX=0;
	public final static Integer COUNTER_PALLET=0;
	
	/**
	 * 
	 * @param prefix SN码除去流水号的部分
	 * @param type 流水类型,0表示laser,1表示box,2表示pallet
	 * @param isDay 是否日流水
	 * @param count 申请流水数量
	 * @return 流水取值区间 [start,end]
	 * @throws BizException 申请流水失败 
	 */
	public static List<Integer> getNum(final String prefix,final Integer type,boolean isDay,int count) throws BizException {        
        String api = "/counter/get?prefix="+prefix+"&type="+type +"&count="+count+"&isDay="+isDay;
        Log.debug("申请流水:"+api);
        return getNum(api, 3);//自动重连3次      
	}
	private static List<Integer> getNum(String api,int tryCount) throws BizException {    
        try {
        	List<Integer> list=new RestUtil(getServer()).get(api).getDataList(Integer.class);
        	if(list==null||list.isEmpty()) {//后端处理超时或者流水业务繁忙
        		if(tryCount>0) {
        			return getNum(api, tryCount-1);
        		}else {
        			throw new BizException("流水申请正忙,请稍后再试");
        		}    
        	}
        	return list;           
        }catch (RequestException e) {//此处可以自定义异常处理，也可以抛出异常由默认的异常处理器统一处理
            if(e.isConnectTimeout() &&tryCount>0) {
            	return getNum(api, tryCount-1);
            }else {
            	throw toBizException(getServer(), e);
            }
        }
	}
}

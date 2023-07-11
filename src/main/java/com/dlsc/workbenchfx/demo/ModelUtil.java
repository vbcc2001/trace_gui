package com.dlsc.workbenchfx.demo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;

public class ModelUtil {
	private static Map<PropertyDescriptor, PropertyDescriptor> laserAndInfo;	
	/**
	 * FQCN->props
	 */
	private final static Map<String, PropertyDescriptor[]> propMap=new HashMap<>();
//	public static void main(String[] args) {
//		ProductLaser laser=new ProductLaser();
//		laser.setProductName("测试");
//		ProductInfo info=new ProductInfo();
//		info.setBoxGrossWeight("5.0kg");
//		info.setFactoryCode("0A");
//		info.setProductPartNumber("WHO");
//		fillLaserValueFromInfo(laser, info);
//		System.out.println(com.alibaba.fastjson2.JSON.toJSONString(laser));
//		System.out.println(com.alibaba.fastjson2.JSON.toJSONString(info));
//	}
	/**
	 * 遍历产品中值为null的属性，并从产品模板中自动补值
	 * @param laser 产品
	 * @param info 产品模板
	 */
	public static void fillLaserValueFromInfo(ProductLaser laser,ProductInfo info) {
		if(laser==null||info==null)return;
		if(laserAndInfo==null) {
			laserAndInfo=new HashMap<>();
			PropertyDescriptor[] laserProps=getPropertyDescriptors(laser);
			PropertyDescriptor[] infoProps=getPropertyDescriptors(info);
			if(laserProps==null||infoProps==null)return;
			//属性比对并缓存同名属性
			for(PropertyDescriptor p1:laserProps) {
				inner:for(PropertyDescriptor p2:infoProps) {
					if(p1.getName().equals(p2.getName())&&p1.getWriteMethod()!=null) {//同名属性
						laserAndInfo.put(p1, p2);
						break inner;
					}
				}
			}
		}
		if(laserAndInfo.isEmpty())return;		
		laserAndInfo.forEach((laserPd,infoPd)->{
			Object value=readValue(laserPd, laser);
			if(value==null) {
				writeValue(laserPd, laser, readValue(infoPd, info));
			}			
		});
		
	}
	public static Object readValue(PropertyDescriptor pd,Object obj) {
		try {
			return pd.getReadMethod().invoke(obj);
		} catch (Throwable e) {
			Log.warn("Fail to read value from "+pd.getDisplayName(),e);
			return null;
		}
	}
	private static void writeValue(PropertyDescriptor pd,Object obj,Object value) {
		try {
			pd.getWriteMethod().invoke(obj, value);
		} catch (Throwable e) {
			Log.warn("Fail to write value from "+pd.getDisplayName(),e);
		}
	}
	private static  PropertyDescriptor[] getPropertyDescriptors(Object obj) {
		if(obj==null)return null;
		try {
			Class<?> targetClass=null;
			if(obj instanceof Class) {
				targetClass=(Class<?>) obj;
			}else {
				targetClass=obj.getClass();
			}
			BeanInfo info=Introspector.getBeanInfo(targetClass);
			return info.getPropertyDescriptors();
		} catch (IntrospectionException e) {
			return null;
		}
	}
	/**
	 * 将源model的可读写属性拷贝到目标model
	 * @param <T> model类型
	 * @param src 源model
	 * @param target 目标model
	 * @return 目标model
	 */
	public static <T> T copyModel(T src,T target) {
		if(src==null||target==null)return null;
		final Class<?> clz=src.getClass();
		PropertyDescriptor[] userInfoProps=loadProps(clz);
		for(PropertyDescriptor prop:userInfoProps) {
			if(prop.getWriteMethod()==null||prop.getReadMethod()==null)continue;
			writeValue(prop, target, readValue(prop, src));
		}
		return target;
	}
	private static PropertyDescriptor[] loadProps(Class<?> clz) {
		PropertyDescriptor[] props=propMap.get(clz.getName());
		if(props==null) {
			props=getPropertyDescriptors(clz);			
			propMap.put(clz.getName(), props);
		}
		return props;
	}
	public static Map<String, PropertyDescriptor> getPropMap(Class<?> clz){
		PropertyDescriptor[] props=loadProps(clz);
		Map<String, PropertyDescriptor> map=new HashMap<>();
		for (PropertyDescriptor prop : props) {
			map.put(prop.getName(), prop);
		}
		return map;
	}
}

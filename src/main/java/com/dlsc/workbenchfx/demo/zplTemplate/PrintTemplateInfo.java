package com.dlsc.workbenchfx.demo.zplTemplate;

import java.util.HashMap;
import java.util.Map;

import com.cg.core.module.BizException;
/**
 * 打印模板信息,多个模板可以共用一个实例,实例个数取决于所有模板实现类的个数
 * @author LiangGuanHao
 *
 */
public class PrintTemplateInfo {
	private final PrintTemplate anno;//模板注解
	private final TPInterface service;
	private final Map<String, Boolean> supportClientType=new HashMap<>();	
	public PrintTemplateInfo(TPInterface service, PrintTemplate anno) {
		super();
		this.service=service;
		this.anno = anno;
		for(String ct:anno.clientType()) {
			supportClientType.put(ct, true);
		}
	}	
	/**
	 * 
	 * @return PrintService 永不为null
	 * @throws BizException 找不到打印模板
	 */
	public TPInterface getService() throws BizException{
		return service;
	}

	public PrintTemplate getAnno() {
		return anno;
	}
	public boolean isSupport(String clientType) {
		return supportClientType.get(clientType)==Boolean.TRUE;
	}
}

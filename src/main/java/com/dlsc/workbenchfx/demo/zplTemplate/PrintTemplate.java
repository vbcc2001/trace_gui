package com.dlsc.workbenchfx.demo.zplTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记可用的打印模板,在Eclipse/STS4中可以通过快捷键Ctrl+Shift+G快速定位注解引用记录.<br>
 * @author LiangGuanHao
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrintTemplate {
	/**
	 * 
	 * @return 模板命名
	 */
	String[] value() default {};
	/**
	 * 
	 * @return 模板适用客户端类型
	 */
	String[] clientType() default {"sushi","xiaomi"};	
}

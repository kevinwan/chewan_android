package net.duohuo.dhroid.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) 
public @interface API {
	//对应方法
	public String url() default "";
	
	public String params();
	
}

package net.duohuo.dhroid.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanUtil {
	
	/**
	 * 对象fu
	 * @param from
	 * @param to
	 */
	public static void copyBeanWithOutNull(Object from,Object to){
		Class<?> beanClass = from.getClass();
		Method[] methodList = beanClass.getDeclaredMethods();
		for (int i = 0; i < methodList.length; i++) {
			Method method=methodList[i];
			if(method.toString().startsWith("public")){
				if(method.getName().startsWith("get")||method.getName().startsWith("is")){
					try {
						Object value=method.invoke(from);
						if(value!=null){
							String methodName="set"+method.getName().substring(3);							
							Method setMethod = beanClass.getDeclaredMethod(methodName,method.getReturnType());
							setMethod.invoke(to, value);
						}												
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}
		}
	}
	
	
	
	/**
	 * 获取属性
	 * @param o
	 * @param field
	 * @return
	 */
	public  static Object getProperty(Object o,String field){
		try {
			Field f =o.getClass().getDeclaredField(field);
			return	f.get(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 添加屬性
	 * @param o
	 * @param field
	 * @param value
	 */
	public static void setProperty(Object o,String field,Object value){
		try {
			Field	f = o.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(o, value);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
	}


}

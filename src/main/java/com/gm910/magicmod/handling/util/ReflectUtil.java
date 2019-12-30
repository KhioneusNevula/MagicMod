package com.gm910.magicmod.handling.util;

import java.lang.reflect.Field;

public class ReflectUtil {

	
	@SuppressWarnings("unchecked")
	public static <I, T> T getField(String name, I object, Class<? extends T> returnType, int superLevel) {
		Field field = null;
		Class<?> clazz = object.getClass();
		/*if (superLevel > 0) {
			for (int i = 0; i < superLevel; i++) {
				clazz = clazz.getSuperclass();
			}
		}*/
		try {
			
			field = clazz.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			
			e.printStackTrace();
		}
		if (field == null) return (T)null;
		field.setAccessible(true);
		Object obj = null;
		try {
			obj = field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			
			e.printStackTrace();
		}
		return (T)obj;
	}
}

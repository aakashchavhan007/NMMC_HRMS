package com.nmmc.hrms.Utils;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class TrimUtils {

    public static void trimObjectFields(Object obj) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            try {
                // Skip static and final fields
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);

                if (field.getType() == String.class) {
                    String value = (String) field.get(obj);
                    if (value != null) {
                        field.set(obj, value.trim());
                    }
                } else if (!field.getType().isPrimitive() && !field.getType().isEnum()) {
                    Object fieldValue = field.get(obj);
                    if (fieldValue != null) {
                        trimObjectFields(fieldValue);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.luxoft.bankapp.service;

import com.luxoft.bankapp.model.NoDB;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class TestService {

    private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + TestService.class.getName());

    public static boolean isEquals(Object o1, Object o2) {

        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.getClass() != o2.getClass()) {
            return false;
        }

        Map<String, Object> o1FieldsMap = getUnannotatedAndNotSetFields(o1);
        Map<String, Object> o2FieldsMap = getUnannotatedAndNotSetFields(o2);

        Map<String, Object> o1SetFieldsMap = getSetFields(o1);
        Map<String, Object> o2SetFieldsMap = getSetFields(o2);

        if (o1SetFieldsMap.size() != 0 || o2SetFieldsMap.size() != 0) {
            for (String fieldName : o1SetFieldsMap.keySet()) {
                Set<Object> set1 = (Set<Object>) o1SetFieldsMap.get(fieldName);
                Set<Object> set2 = (Set<Object>) o2SetFieldsMap.get(fieldName);

                if (set1.size() != set2.size()) {
                    return false;
                }

                Iterator iter1 = set1.iterator();
                Iterator iter2 = set2.iterator();

                while (iter1.hasNext() && iter2.hasNext()) {
                    isEquals(iter1.next(), iter2.next());
                }
            }
        }

        assertReflectionEquals(o1FieldsMap, o2FieldsMap);
        return true;
    }

    private static Map<String, Object> getUnannotatedAndNotSetFields(Object obj) {
        ArrayList<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredFields()));

        Map<String, Object> map = new HashMap<>();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getAnnotationsByType(NoDB.class).length == 0 && !Modifier.isStatic(f.getModifiers()) && !f.getType().isAssignableFrom(Set.class)) {
                try {
                    map.put(f.getName(), f.get(obj));
                } catch (IllegalAccessException e) {
                    EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return map;
    }

    private static Map<String, Object> getSetFields(Object obj) {
        ArrayList<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredFields()));
        Map<String, Object> map = new HashMap<>();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getAnnotationsByType(NoDB.class).length == 0 && f.getType().isAssignableFrom(Set.class)) {
                try {
                    map.put(f.getName(), f.get(obj));
                } catch (IllegalAccessException e) {
                    EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return map;
    }
}
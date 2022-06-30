package com.exceedit.auth.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class Utils {

    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static <T> T merge(T local, T remote) {
        try {
            Class<?> clazz = local.getClass();
            java.lang.Object merged = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                java.lang.Object localValue = field.get(local);
                java.lang.Object remoteValue = field.get(remote);
                if (localValue != null) {
                    switch (localValue.getClass().getSimpleName()) {
                        case "Default":
                        case "Detail":
                            field.set(merged, merge(localValue, remoteValue));
                            break;
                        default:
                            field.set(merged, (remoteValue != null) ? remoteValue : localValue);
                    }
                }
            }
            return (T) merged;
        } catch (Exception err) {
            logger.error("cannot merger, error - " + err);
            return null;
        }
    }

    public static <T, K> T mergeDiff(T local, K remote) {
        try {
            Class<?> clazz = local.getClass();
            Class<?> classRemote = remote.getClass();
            java.lang.Object merged = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                for (Field fieldRem : classRemote.getDeclaredFields()) {
                    fieldRem.setAccessible(true);
                    System.out.println(fieldRem + "    " + field);
                    if (field.getName().equals(fieldRem.getName())) {
                        java.lang.Object localValue = field.get(local);
                        java.lang.Object remoteValue = fieldRem.get(remote);

                        field.set(merged, (remoteValue != null) ? remoteValue : localValue);
                    }
                }
            }
            return (T) merged;
        } catch (Exception err) {
            logger.error("cannot merge, error - " + err.getMessage());
            return null;
        }
    }
}

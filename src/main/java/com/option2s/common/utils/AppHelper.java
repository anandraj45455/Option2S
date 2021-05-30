package com.option2s.common.utils;

public class AppHelper {

    public static boolean isNullOrEmpty(Object object) {
        return object == null || object.toString().trim().isEmpty();
    }

    public static boolean isPresent(Object object) {
        return !isNullOrEmpty(object);
    }

}

package com.erick.util;

public final class StringUtil {
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }
}

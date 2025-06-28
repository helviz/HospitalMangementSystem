package org.example.utilities;

public class CapitaliseUtil {
    public static String Capitalise(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}

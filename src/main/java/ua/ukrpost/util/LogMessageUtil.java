package ua.ukrpost.util;

import static java.lang.String.format;

public class LogMessageUtil {

    public static String getAllLogEndpoint(Class clazz) {
        return format("Getting all %s", clazz.getSimpleName());
    }
}

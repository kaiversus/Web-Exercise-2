package com.kai.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class AppConfig {

    private static final Properties PROPS = new Properties();

    static { 
        try (InputStream in = AppConfig.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "Khong tim thay app.properties trong classpath");
            }
            PROPS.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static String get(String key) {
        String envKey = key.toUpperCase().replace('.', '_');
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return PROPS.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        String v = get(key);
        return (v == null || v.isBlank()) ? defaultValue : v;
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key).trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
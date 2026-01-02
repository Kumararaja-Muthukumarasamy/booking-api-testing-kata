package com.booking.config;

import com.booking.constants.config.ConfigConstants;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(ConfigConstants.CONFIG_FILE_PATH)) {
            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getProperty(ConfigKey key) {

        return properties.getProperty(key.getKey());
    }
}
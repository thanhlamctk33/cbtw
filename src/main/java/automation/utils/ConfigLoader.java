package automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";
    private static boolean isInitialized = false;

    public static synchronized void init() {
        if (isInitialized) {
            return;
        }

        try {
            loadProperties(CONFIG_FILE);

            properties.putAll(System.getProperties());

            isInitialized = true;
        } catch (IOException e) {
            LogUtil.error("Failed to initialize configuration: " + e.getMessage());
            throw new RuntimeException("Failed to initialize configuration", e);
        }
    }

    private static void loadProperties(String fileName) throws IOException {
        File parametersFile = new File("./Parameters/" + fileName);
        if (parametersFile.exists()) {
            try (InputStream input = new FileInputStream(parametersFile)) {
                properties.load(input);
                LogUtil.info("Loaded properties from: " + parametersFile.getAbsolutePath());
                return;
            }
        }

        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input != null) {
                properties.load(input);
                LogUtil.info("Loaded properties from classpath: " + fileName);
                return;
            }
        }
        throw new IOException("Property file '" + fileName + "' not found in Parameters directory or classpath");
    }

    public static String getProperty(String key) {
        if (!isInitialized) {
            init();
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (!isInitialized) {
            init();
        }
        return properties.getProperty(key, defaultValue);
    }

    public static int getPropertyAsInt(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LogUtil.error("Property '" + key + "' value '" + value + "' is not a valid integer, using default: " + defaultValue);
            return defaultValue;
        }
    }

    public static boolean getPropertyAsBoolean(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
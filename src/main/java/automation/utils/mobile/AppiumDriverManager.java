package automation.utils.mobile;

import automation.utils.ConfigLoader;
import automation.utils.LogUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages Appium driver instances for mobile testing
 */
public class AppiumDriverManager {
    private static Map<String, AppiumDriver> driverMap = new HashMap<>();
    private static AppiumDriverLocalService appiumServer;

    public static AppiumDriver getDriver(String platform) {
        if (driverMap.containsKey(platform)) {
            return driverMap.get(platform);
        }

        AppiumDriver driver = createDriver(platform);
        driverMap.put(platform, driver);
        return driver;
    }

    private static AppiumDriver createDriver(String platform) {
        boolean isRemote = ConfigLoader.getPropertyAsBoolean("appium.remote", false);

        if (isRemote) {
            return createRemoteDriver(platform);
        } else {
            startAppiumServerIfNeeded();
            return createLocalDriver(platform);
        }
    }

    private static AppiumDriver createLocalDriver(String platform) {
        LogUtil.info("Creating local Appium driver for platform: " + platform);

        try {
            switch (platform.toLowerCase()) {
                case "android":
                    return new AndroidDriver(appiumServer.getUrl(), getAndroidCapabilities());
                case "ios":
                    return new IOSDriver(appiumServer.getUrl(), getIOSCapabilities());
                default:
                    LogUtil.warn("Platform '" + platform + "' not supported. Defaulting to Android");
                    return new AndroidDriver(appiumServer.getUrl(), getAndroidCapabilities());
            }
        } catch (Exception e) {
            LogUtil.error("Error creating Appium driver: " + e.getMessage());
            throw new RuntimeException("Error creating Appium driver", e);
        }
    }

    private static AppiumDriver createRemoteDriver(String platform) {
        LogUtil.info("Creating remote Appium driver for platform: " + platform);

        String remoteUrl = ConfigLoader.getProperty("appium.remote.url", "http://localhost:4723");

        if (!remoteUrl.endsWith("/wd/hub")) {
            remoteUrl += "/wd/hub";
        }

        try {
            URL url = new URL(remoteUrl);

            switch (platform.toLowerCase()) {
                case "android":
                    return new AndroidDriver(url, getAndroidCapabilities());
                case "ios":
                    return new IOSDriver(url, getIOSCapabilities());
                default:
                    LogUtil.warn("Platform '" + platform + "' not supported for remote execution. Defaulting to Android");
                    return new AndroidDriver(url, getAndroidCapabilities());
            }
        } catch (Exception e) {
            LogUtil.error("Error creating remote Appium driver: " + e.getMessage());
            throw new RuntimeException("Error creating remote Appium driver", e);
        }
    }

    private static DesiredCapabilities getAndroidCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName",
                ConfigLoader.getProperty("appium.android.device", "Android Device"));
        capabilities.setCapability("automationName", "UiAutomator2");

        // Optional platform version
        String platformVersion = ConfigLoader.getProperty("appium.android.version");
        if (platformVersion != null && !platformVersion.isEmpty()) {
            capabilities.setCapability("platformVersion", platformVersion);
        }

        String appPath = ConfigLoader.getProperty("appium.android.app.path");
        if (appPath != null && !appPath.isEmpty()) {
            File app = new File(appPath);
            if (app.exists()) {
                capabilities.setCapability("app", app.getAbsolutePath());
            } else {
                LogUtil.warn("Android app file not found at path: " + appPath);
            }
        }

        String appPackage = ConfigLoader.getProperty("appium.android.app.package");
        String appActivity = ConfigLoader.getProperty("appium.android.app.activity");

        if (appPackage != null && !appPackage.isEmpty()) {
            capabilities.setCapability("appPackage", appPackage);
        }

        if (appActivity != null && !appActivity.isEmpty()) {
            capabilities.setCapability("appActivity", appActivity);
        }

        // Other capabilities
        capabilities.setCapability("newCommandTimeout",
                ConfigLoader.getPropertyAsInt("appium.command.timeout", 60));
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability("noReset",
                ConfigLoader.getPropertyAsBoolean("appium.no.reset", false));
        capabilities.setCapability("fullReset",
                ConfigLoader.getPropertyAsBoolean("appium.full.reset", false));

        return capabilities;
    }

    private static DesiredCapabilities getIOSCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("deviceName",
                ConfigLoader.getProperty("appium.ios.device", "iPhone Simulator"));
        capabilities.setCapability("automationName", "XCUITest");

        String platformVersion = ConfigLoader.getProperty("appium.ios.version");
        if (platformVersion != null && !platformVersion.isEmpty()) {
            capabilities.setCapability("platformVersion", platformVersion);
        }

        String appPath = ConfigLoader.getProperty("appium.ios.app.path");
        if (appPath != null && !appPath.isEmpty()) {
            File app = new File(appPath);
            if (app.exists()) {
                capabilities.setCapability("app", app.getAbsolutePath());
            } else {
                LogUtil.warn("iOS app file not found at path: " + appPath);
            }
        }

        String bundleId = ConfigLoader.getProperty("appium.ios.bundle.id");
        if (bundleId != null && !bundleId.isEmpty()) {
            capabilities.setCapability("bundleId", bundleId);
        }

        String udid = ConfigLoader.getProperty("appium.ios.udid");
        if (udid != null && !udid.isEmpty()) {
            capabilities.setCapability("udid", udid);
        }

        capabilities.setCapability("newCommandTimeout",
                ConfigLoader.getPropertyAsInt("appium.command.timeout", 60));
        capabilities.setCapability("autoAcceptAlerts", true);
        capabilities.setCapability("noReset",
                ConfigLoader.getPropertyAsBoolean("appium.no.reset", false));
        capabilities.setCapability("fullReset",
                ConfigLoader.getPropertyAsBoolean("appium.full.reset", false));

        return capabilities;
    }

    public static synchronized void startAppiumServerIfNeeded() {
        if (appiumServer != null && appiumServer.isRunning()) {
            return;
        }

        LogUtil.info("Starting Appium server");

        try {
            AppiumServiceBuilder builder = new AppiumServiceBuilder();

            // Set Appium JS path if provided
            String appiumJsPath = ConfigLoader.getProperty("appium.js.path");
            if (appiumJsPath != null && !appiumJsPath.isEmpty()) {
                builder.withAppiumJS(new File(appiumJsPath));
            } else {
                // Try to use default location based on user's nvm installation
                String userHome = System.getProperty("user.home");
                File defaultAppiumJs = new File(userHome + "/.nvm/versions/node/v20.15.1/lib/node_modules/appium/build/lib/main.js");
                if (defaultAppiumJs.exists()) {
                    builder.withAppiumJS(defaultAppiumJs);
                    LogUtil.info("Using default Appium JS path: " + defaultAppiumJs.getAbsolutePath());
                } else {
                    LogUtil.warn("No Appium JS path provided and default path not found. Using system PATH");
                }
            }

            // Set Appium server address and port
            String appiumAddress = ConfigLoader.getProperty("appium.address", "127.0.0.1");
            int appiumPort = ConfigLoader.getPropertyAsInt("appium.port", 4723);

            builder.withIPAddress(appiumAddress)
                    .usingPort(appiumPort)
                    .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                    .withArgument(() -> "--base-path", "/wd/hub"); // Add base path for Appium 2.x

            appiumServer = AppiumDriverLocalService.buildService(builder);
            appiumServer.start();

            LogUtil.info("Appium server started successfully on " +
                    appiumServer.getUrl().toString());
        } catch (Exception e) {
            LogUtil.error("Error starting Appium server: " + e.getMessage());
            throw new RuntimeException("Error starting Appium server", e);
        }
    }

    public static synchronized void stopAppiumServer() {
        if (appiumServer != null && appiumServer.isRunning()) {
            LogUtil.info("Stopping Appium server");
            appiumServer.stop();
            appiumServer = null;
        }
    }

    public static void quitDriver(String platform) {
        AppiumDriver driver = driverMap.get(platform);
        if (driver != null) {
            try {
                driver.quit();
                LogUtil.info("Appium driver instance for platform '" + platform + "' quit successfully");
            } catch (Exception e) {
                LogUtil.error("Error quitting Appium driver instance for platform '" + platform + "': " + e.getMessage());
            } finally {
                driverMap.remove(platform);
            }
        }
    }

    public static void quitAllDrivers() {
        for (String platform : driverMap.keySet()) {
            AppiumDriver driver = driverMap.get(platform);
            if (driver != null) {
                try {
                    driver.quit();
                    LogUtil.info("Appium driver instance for platform '" + platform + "' quit successfully");
                } catch (Exception e) {
                    LogUtil.error("Error quitting Appium driver instance for platform '" + platform + "': " + e.getMessage());
                }
            }
        }
        driverMap.clear();
    }

    public static void cleanup() {
        quitAllDrivers();
        stopAppiumServer();
    }
}
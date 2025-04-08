package automation.utils.mobile;

import automation.utils.ConfigLoader;
import automation.utils.LogUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.URL;

public class AppLauncher {

    public static AppiumDriver launchExnessApp() {
        try {
            LogUtil.info("Connecting to mobile device and launching Exness app");

            UiAutomator2Options options = new UiAutomator2Options();

            options.setDeviceName(ConfigLoader.getProperty("appium.android.device", "Android Device"));
            options.setPlatformName("Android");
            options.setPlatformVersion(ConfigLoader.getProperty("appium.android.version", "13"));
            options.setAutomationName("UiAutomator2");

            // App package from config
            String appPackage = ConfigLoader.getProperty("appium.android.app.package", "com.exness.android.pa");
            options.setAppPackage(appPackage);
            boolean noReset = ConfigLoader.getPropertyAsBoolean("appium.no.reset", true);
            boolean fullReset = ConfigLoader.getPropertyAsBoolean("appium.full.reset", false);
            boolean autoGrantPermissions = ConfigLoader.getPropertyAsBoolean("appium.autoGrantPermissions", true);

            options.setNoReset(noReset);
            options.setFullReset(fullReset);
            options.setAutoGrantPermissions(autoGrantPermissions);

            String configuredActivity = ConfigLoader.getProperty("appium.android.app.activity");

            if (configuredActivity != null && !configuredActivity.isEmpty()) {
                LogUtil.info("Using app activity from config: " + configuredActivity);
                options.setAppActivity(configuredActivity);

                try {
                    // Get Appium server URL from config
                    String appiumUrl = ConfigLoader.getProperty("appium.remote.url", "http://localhost:4723/wd/hub");
                    URL appiumServerURL = new URL(appiumUrl);
                    AppiumDriver driver = new AndroidDriver(appiumServerURL, options);

                    LogUtil.info("Successfully connected to device and launched Exness app with activity: " + configuredActivity);
                    LogUtil.info("Using config values: appPackage=" + appPackage + ", noReset=" + noReset +
                            ", fullReset=" + fullReset + ", autoGrantPermissions=" + autoGrantPermissions);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    return driver;
                } catch (Exception e) {
                    LogUtil.warn("Failed to launch with configured activity: " + configuredActivity);
                    LogUtil.warn("Will try fallback activities. Error: " + e.getMessage());
                }
            }

            String[] possibleActivities = {
                    "com.exness.features.entry.impl.presentation.EntryActivity",
                    "com.exness.features.entry.impl.presentation.PremierEntryActivity"
            };
            if (configuredActivity != null) {
                for (int i = 0; i < possibleActivities.length; i++) {
                    if (possibleActivities[i].equals(configuredActivity)) {
                        continue;
                    }

                    try {
                        options.setAppActivity(possibleActivities[i]);
                        String appiumUrl = ConfigLoader.getProperty("appium.remote.url", "http://localhost:4723/wd/hub");
                        URL appiumServerURL = new URL(appiumUrl);
                        AppiumDriver driver = new AndroidDriver(appiumServerURL, options);

                        LogUtil.info("Successfully connected to device and launched Exness app with fallback activity: " + possibleActivities[i]);
                        LogUtil.info("Using config values: appPackage=" + appPackage + ", noReset=" + noReset +
                                ", fullReset=" + fullReset + ", autoGrantPermissions=" + autoGrantPermissions);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                        return driver;
                    } catch (Exception e) {
                        LogUtil.warn("Failed to launch with activity: " + possibleActivities[i]);
                        LogUtil.warn("Error details: " + e.getMessage());
                    }
                }
            }

            throw new RuntimeException("Could not launch Exness app with any known activity.");

        } catch (Exception e) {
            LogUtil.error("Comprehensive failure in launching Exness app: " + e.getMessage());
            LogUtil.info("Troubleshooting steps:");
            LogUtil.info("1. Verify app is installed on the device");
            LogUtil.info("2. Check Appium server is running at " +
                    ConfigLoader.getProperty("appium.remote.url", "http://localhost:4723/wd/hub"));
            LogUtil.info("3. Confirm device " +
                    ConfigLoader.getProperty("appium.android.device", "Android Device") + " is connected and recognized");
            LogUtil.info("4. Verify correct device ID and Android version (" +
                    ConfigLoader.getProperty("appium.android.version", "13") + ")");
            LogUtil.info("5. Check app package (" +
                    ConfigLoader.getProperty("appium.android.app.package", "com.exness.android.pa") + ") and activity");
            LogUtil.info("6. Review config.properties for accurate settings");
            LogUtil.info("7. Make sure the app is not already running or in a locked state");

            throw new RuntimeException("Failed to launch Exness app with comprehensive diagnostics", e);
        }
    }

    public static void main(String[] args) {
        ConfigLoader.init();
        LogUtil.info("Starting Exness app launcher test");

        try {
            AppiumDriver driver = launchExnessApp();

            LogUtil.info("App launched successfully. Current activity: " +
                    ((AndroidDriver)driver).currentActivity());

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            driver.quit();
            LogUtil.info("Test completed successfully");
        } catch (Exception e) {
            LogUtil.error("App launch test failed: " + e.getMessage());
        }
    }
}
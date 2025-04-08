package automation.utils.web;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import automation.utils.ConfigLoader;
import automation.utils.LogUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Factory class for creating WebDriver instances
 */
public class WebDriverFactory {
    private static Map<String, WebDriver> drivers = new HashMap<>();

    public static WebDriver getDriver() {
        String browser = ConfigLoader.getProperty("web.browser", "chrome").toLowerCase();
        if (drivers.containsKey(browser)) {
            return drivers.get(browser);
        }

        WebDriver driver = createDriver(browser);
        drivers.put(browser, driver);
        configureDriver(driver);

        return driver;
    }

    private static WebDriver createDriver(String browser) {
        LogUtil.info("Creating WebDriver for browser: " + browser);

        boolean headless = ConfigLoader.getPropertyAsBoolean("web.headless", false);

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--remote-allow-origins=*");
                return new ChromeDriver(chromeOptions);

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                return new EdgeDriver(edgeOptions);

            case "safari":
                WebDriverManager.safaridriver().setup();
                SafariOptions safariOptions = new SafariOptions();
                return new SafariDriver(safariOptions);

            default:
                LogUtil.warn("Browser '" + browser + "' not supported. Defaulting to Chrome");
                WebDriverManager.chromedriver().setup();
                ChromeOptions defaultOptions = new ChromeOptions();
                if (headless) {
                    defaultOptions.addArguments("--headless=new");
                }
                defaultOptions.addArguments("--start-maximized");
                defaultOptions.addArguments("--remote-allow-origins=*");
                return new ChromeDriver(defaultOptions);
        }
    }

    private static void configureDriver(WebDriver driver) {
        // Set timeouts
        int pageLoadTimeout = ConfigLoader.getPropertyAsInt("web.page.load.timeout", 60);
        int implicitWait = ConfigLoader.getPropertyAsInt("web.implicit.wait", 10);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().window().maximize();
    }

    public static void quitAllDrivers() {
        for (WebDriver driver : drivers.values()) {
            try {
                if (driver != null) {
                    driver.quit();
                }
            } catch (Exception e) {
                LogUtil.error("Error quitting WebDriver: " + e.getMessage());
            }
        }
        drivers.clear();
    }
}
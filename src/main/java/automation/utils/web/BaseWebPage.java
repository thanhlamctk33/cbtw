package automation.utils.web;

import automation.utils.ConfigLoader;
import automation.utils.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base Page class that provides common functionality for all web page objects
 */
public class BaseWebPage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected Actions actions;
    protected static final Logger LOGGER = LogManager.getLogger(BaseWebPage.class);
    protected static final String LOADING_BAR_XPATH = "//div[(contains(@class, 'loading-bar') and @role='progressbar') or contains(@class, 'spinner')]";
    protected static final String LOADING_SPINNER_XPATH = "//div[(contains(@class, 'spinner') or contains(@class, 'loader'))]";

    public BaseWebPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
                Integer.parseInt(ConfigLoader.getProperty("web.timeout.explicit"))));
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    public void waitForPageToLoad() {
        LogUtil.info("Waiting for page to load completely");
        wait.until(driver -> js.executeScript("return document.readyState").equals("complete"));
    }
}
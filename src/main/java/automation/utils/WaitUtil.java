package automation.utils;

import java.time.Duration;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtil {

    public static void waitForElementToBeVisible(WebDriver driver, WebElement element, String description) {
        LogUtil.info("Waiting for element to be visible: " + description);
        int timeoutInSeconds;
        try {
            String timeoutStr = ConfigLoader.getProperty("web.timeout.explicit");
            timeoutInSeconds = timeoutStr != null ? Integer.parseInt(timeoutStr) : 10;
        } catch (NumberFormatException e) {
            LogUtil.warn("Invalid timeout value. Using default of 10 seconds.");
            timeoutInSeconds = 10;
        }
        waitForElementToBeVisible(driver, element, description, timeoutInSeconds);
    }

    public static void waitForElementToBeVisible(WebDriver driver, WebElement element, String description, int timeoutInSeconds) {
        try {
            LogUtil.info("Waiting " + timeoutInSeconds + " seconds for element to be visible: " + description);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.visibilityOf(element));

            LogUtil.info("Element is now visible: " + description);
        } catch (TimeoutException e) {
            LogUtil.error("Timeout waiting for element to be visible: " + description);
            throw new AssertionError("Element not visible within " + timeoutInSeconds + " seconds: " + description, e);
        } catch (Exception e) {
            LogUtil.error("Error waiting for element: " + description + ". Error: " + e.getMessage());
            throw new AssertionError("Error waiting for element: " + description, e);
        }
    }

    public static void waitForElementToBeClickable(WebDriver driver, WebElement element, String description) {
        LogUtil.info("Waiting for element to be clickable: " + description);
        int timeoutInSeconds;
        try {
            String timeoutStr = ConfigLoader.getProperty("web.timeout.explicit");
            timeoutInSeconds = timeoutStr != null ? Integer.parseInt(timeoutStr) : 10; // Giá trị mặc định là 10 giây
        } catch (NumberFormatException e) {
            LogUtil.warn("Invalid timeout value. Using default of 10 seconds.");
            timeoutInSeconds = 10;
        }

        waitForElementToBeClickable(driver, element, description, timeoutInSeconds);
    }

    public static void waitForElementToBeClickable(WebDriver driver, WebElement element, String description, int timeoutInSeconds) {
        try {
            LogUtil.info("Waiting " + timeoutInSeconds + " seconds for element to be clickable: " + description);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.elementToBeClickable(element));

            LogUtil.info("Element is now clickable: " + description);
        } catch (TimeoutException e) {
            LogUtil.error("Timeout waiting for element to be clickable: " + description);
            throw new AssertionError("Element not clickable within " + timeoutInSeconds + " seconds: " + description, e);
        } catch (Exception e) {
            LogUtil.error("Error waiting for element to be clickable: " + description + ". Error: " + e.getMessage());
            throw new AssertionError("Error waiting for element to be clickable: " + description, e);
        }
    }
}
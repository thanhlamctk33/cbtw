package automation.utils;

import java.io.File;
import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class Element {

    public static WebElement findElementByXPath(WebDriver driver, String xpath, String description) {
        LogUtil.info("Finding element by XPath: " + description + " [" + xpath + "]");
        try {
            return driver.findElement(By.xpath(xpath));
        } catch (NoSuchElementException e) {
            LogUtil.error("Element not found by XPath: " + description + " [" + xpath + "]");
            throw e;
        }
    }

    public static String getText(WebElement element, String description) {
        LogUtil.info("Getting text from element: " + description);
        if (element == null) {
            LogUtil.warn("Element is null: " + description);
            return "";
        }
        try {
            String text = element.getText();
            return text != null ? text.trim() : "";
        } catch (StaleElementReferenceException e) {
            LogUtil.warn("Stale element reference while getting text from: " + description);
            return "";
        } catch (Exception e) {
            LogUtil.error("Exception while getting text from: " + description + ". Error: " + e.getMessage());
            return "";
        }
    }

    public static void click(WebDriver driver, WebElement elementToBeClicked, String description, boolean... scroll) {
        if (elementToBeClicked != null) {
            LogUtil.info("Click on '" + description + "'");
            try {
                JavascriptExecutor jse = (JavascriptExecutor) driver;
                if (scroll.length > 0 && scroll[0]) {
                    jse.executeScript("arguments[0].scrollIntoView(true)", elementToBeClicked);
                } else {
                    jse.executeScript("arguments[0].scrollIntoView(false)", elementToBeClicked);
                }
            } catch (WebDriverException wde) {
                LogUtil.error("Unable to scroll: " + wde.getMessage());
            }
            elementToBeClicked.click();
        } else {
            LogUtil.fail("Element '" + description + "' not found on the page");
            throw new AssertionError("Element '" + description + "' not found on the page");
        }
    }

    public static void sendKeys(WebDriver driver, WebElement element, String text, String description) {
        LogUtil.info("Entering text: '" + text + "' into element: " + description);
        try {
            WaitUtil.waitForElementToBeVisible(driver, element, description);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            LogUtil.error("Failed to sendKeys " + e.getMessage());
            throw new RuntimeException("Failed to sendKeys", e);
        }
    }

    public static void select(WebElement element, String text) {
        LogUtil.info("Selecting: '" + text + "' into element: " + element);
        try {
            Select categorySelect = new Select(element);
            categorySelect.selectByVisibleText(text);
            LogUtil.info("Selected category: " + text);
        } catch (Exception e) {
            LogUtil.error("Failed to select category: " + e.getMessage());
            throw new RuntimeException("Failed to select category", e);
        }
    }

    public static void enterData(WebDriver driver, WebElement element, String value, String description, boolean useTyping) {
        LogUtil.info("Enter the " + description + " as '" + value + "'");
        try {
            element.click();
        } catch (InvalidArgumentException e) {
            LogUtil.warn("InvalidArgumentException, skipping it...: " + e.getMessage());
        } catch (ElementClickInterceptedException e) {
            LogUtil.info("Trying to click element again...");
            new Actions(driver).moveToElement(element).click().perform();
        }
        element.clear();
        String currentText = "";
        try {
            currentText = element.getAttribute("text");
        } catch (Exception e) {
            LogUtil.info("Could not get text attribute: " + e.getMessage());
        }
        if (currentText != null && !StringUtils.isEmpty(currentText)) {
            new Actions(driver).click(element).sendKeys(Keys.END).keyDown(Keys.SHIFT)
                    .sendKeys(Keys.HOME).keyUp(Keys.SHIFT).sendKeys(Keys.BACK_SPACE).perform();
        }
        if (useTyping) {
            typing(driver, value, description);
            return;
        }
        element.sendKeys(value);
    }

    public static void enterData(WebDriver driver, WebElement element, String value, String description) {
        enterData(driver, element, value, description, false);
    }

    public static void typing(WebDriver driver, String value, String description) {
        LogUtil.info("Type the " + description + " as '" + value + "'");
        Actions actions = new Actions(driver);

        for (char c : value.toCharArray()) {
            actions.sendKeys(String.valueOf(c)).pause(Duration.ofMillis(100));
        }
        actions.pause(Duration.ofMillis(500));
        actions.perform();
    }

    public static Boolean isElementDisplayed(WebElement element) {
        Boolean visible = true;
        if (element == null) {
            return false;
        }
        try {
            visible = element.isDisplayed();
        } catch (StaleElementReferenceException e) {
            LogUtil.info("Stale element reference exception. Trying again...");
            try {
                visible = element.isDisplayed();
            } catch (Exception exc) {
                visible = false;
            }
        } catch (NoSuchElementException | ElementNotInteractableException e) {
            visible = false;
        }
        return visible;
    }

    public static void uploadFile(WebElement element, String filePath) {
        LogUtil.info("Upload File " + filePath);
        try {
            if (filePath != null && !filePath.isEmpty()) {
                File file = new File(filePath);
                if (file.exists()) {
                    LogUtil.info("Uploading challenge file: " + filePath);
                    element.sendKeys(file.getAbsolutePath());
                    LogUtil.info("File uploaded successfully");
                } else {
                    LogUtil.warn("File does not exist: " + filePath);
                }
            } else {
                LogUtil.info("No File specified for upload");
            }
        } catch (Exception e) {
            LogUtil.warn("Failed to upload challenge file: " + e.getMessage());
        }
    }
}
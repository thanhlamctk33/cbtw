package automation.utils.mobile;

import automation.utils.ConfigLoader;
import automation.utils.Element;
import automation.utils.LogUtil;
import automation.utils.WaitUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class BaseMobilePage {
    protected AppiumDriver driver;
    protected AppiumDriverLocalService appiumServer;
    protected boolean isAndroid;
    protected boolean isIOS;

    public BaseMobilePage(AppiumDriver driver) {
        this.driver = driver;
        isAndroid = driver instanceof AndroidDriver;
        isIOS = driver instanceof IOSDriver;
        int timeoutInSeconds = 15;
        try {
            String timeoutStr = ConfigLoader.getProperty("mobile.timeout.explicit");
            if (timeoutStr != null && !timeoutStr.isEmpty()) {
                timeoutInSeconds = Integer.parseInt(timeoutStr);
            }
        } catch (NumberFormatException e) {
            LogUtil.warn("Invalid mobile.timeout.explicit value. Using default of 15 seconds.");
        }
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(timeoutInSeconds)), this);
    }

    public void tap(WebElement element, String description) {
        WaitUtil.waitForElementToBeVisible(driver, element, description);
        Element.click(driver, element, description);
    }

    public void enterText(WebElement element, String text, String description) {
        WaitUtil.waitForElementToBeVisible(driver, element, description);
        Element.enterData(driver, element, text, description);
    }
}
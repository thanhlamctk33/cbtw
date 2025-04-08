package automation.ctflearn.mobile;

import automation.utils.AssertHelper;
import automation.utils.Element;
import automation.utils.mobile.BaseMobilePage;
import automation.utils.WaitUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import automation.utils.LogUtil;
import org.openqa.selenium.WebElement;

import java.util.Map;
public class ProfileScreen extends BaseMobilePage {

    @AndroidFindBy(id = "com.exness.android.pa:id/logoutTextView")
    private WebElement logoutButton;

    @AndroidFindBy(id = "com.exness.android.pa:id/shareStrategiesButton")
    private WebElement shareStrategiesButton;

    @AndroidFindBy(id = "com.exness.android.pa:id/userVoiceButton")
    private WebElement userVoiceButton;

    @AndroidFindBy(id = "com.exness.android.pa:id/profileContent")
    private WebElement profileContent;

    public ProfileScreen(AppiumDriver driver) {
        super(driver);
        AssertHelper.assertElementIsDisplayed("profile", profileContent);
    }

    public void logout() {
        LogUtil.info("Scrolling to Logout button and logging out");
        WaitUtil.waitForElementToBeVisible(driver, profileContent, "Profile content");
        scrollDownToFindElement(shareStrategiesButton, "shareStrategies button");
        scrollDownToFindElement(userVoiceButton, "userVoice button");
        scrollDownToFindElement(logoutButton, "Logout button");

        tap(logoutButton, "Logout button");
    }

    private void scrollDownToFindElement(WebElement element, String description) {
        LogUtil.info("Scrolling down to find: " + description);
        int maxScrollAttempts = 10;
        try {
            for (int attempt = 0; attempt < maxScrollAttempts; attempt++) {
                if (Element.isElementDisplayed(element)) {
                    LogUtil.info("Logout button is now visible");
                    return;
                }
                int screenHeight = driver.manage().window().getSize().height;
                int screenWidth = driver.manage().window().getSize().width;

                driver.executeScript("mobile: scrollGesture", Map.of(
                        "left", screenWidth / 2,
                        "top", (int) (screenHeight * 0.7),
                        "width", 10,
                        "height", screenHeight,
                        "direction", "up",
                        "percent", 0.6
                ));
                Thread.sleep(500);
            }
            LogUtil.warn("Could not find logout button after " + maxScrollAttempts + " scroll attempts");
        } catch (Exception e) {
            LogUtil.error("Error while scrolling to logout button: " + e.getMessage());
            throw new RuntimeException("Scroll to logout button failed", e);
        }
    }
}
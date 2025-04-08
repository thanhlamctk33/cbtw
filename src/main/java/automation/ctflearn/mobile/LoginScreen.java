package automation.ctflearn.mobile;

import automation.utils.AssertHelper;
import automation.utils.Element;
import automation.utils.mobile.BaseMobilePage;
import automation.utils.WaitUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import automation.utils.LogUtil;
import org.openqa.selenium.WebElement;

public class LoginScreen extends BaseMobilePage {

    final String digitButtonXpath = "//*[@text='%s']";

    @AndroidFindBy(id = "com.exness.android.pa:id/signInView")
    private WebElement signInButton;

    @AndroidFindBy(xpath = "(//*[@resource-id='com.exness.android.pa:id/editText'])[1]")
    private WebElement emailInput;

    @AndroidFindBy(xpath = "(//*[@resource-id='com.exness.android.pa:id/editText'])[2]")
    private WebElement passwordInput;

    @AndroidFindBy(id = "com.exness.android.pa:id/signInButton")
    private WebElement loginButton;

    @AndroidFindBy(id = "com.exness.android.pa:id/passcode")
    private WebElement passcodeField;

    @AndroidFindBy(xpath = "//*[@text='Not now']")
    private WebElement notNowButton;

    @AndroidFindBy(id = "com.exness.android.pa:id/bottom_navigation_item_icon")
    private WebElement accountsNavButton;

    @AndroidFindBy(xpath = "//*[@resource-id='com.exness.android.pa:id/bottom_navigation_item_title' and @text='Trade']")
    private WebElement tradeNavButton;

    @AndroidFindBy(xpath = "//*[@resource-id='com.exness.android.pa:id/bottom_navigation_item_title' and @text='Profile']")
    private WebElement profileNavButton;

    public LoginScreen(AppiumDriver driver) {
        super(driver);
        AssertHelper.assertElementIsDisplayed("account" , accountsNavButton);
    }

    public void clickSignIn() {
        LogUtil.info("Clicking on Sign In button");
        WaitUtil.waitForElementToBeVisible(driver, signInButton, "Wait Sign In Button");
        tap(signInButton, "Sign In Button");
    }

    public void login(String email, String password) {
        LogUtil.info("Logging in with email: " + email);

        WaitUtil.waitForElementToBeVisible(driver, emailInput, "Email input field");
        enterText(emailInput, email, "Email");

        WaitUtil.waitForElementToBeVisible(driver, passwordInput, "Password input field");
        enterText(passwordInput, password, "Password");

        WaitUtil.waitForElementToBeClickable(driver, loginButton, "Login button");
        tap(loginButton, "Login button");
    }

    public void enterPasscode(String passcode) {
        LogUtil.info("Entering passcode");
        WaitUtil.waitForElementToBeVisible(driver, passcodeField, "Passcode field");

        for (char digit : passcode.toCharArray()) {
            WebElement button = Element.findElementByXPath(driver, String.format(digitButtonXpath, digit), "Passcode");
            tap(button, "Passcode digit " + digit);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void handleSecurityPopup() {
        LogUtil.info("Handling security popup: Clicking 'Not now'");
        if (Element.isElementDisplayed(notNowButton)) {
            tap(notNowButton, "Not now button");
        }
    }

    public void reEnterPasscode(String passcode) {
        LogUtil.info("Re-entering passcode for confirmation");
        enterPasscode(passcode);
        handleSecurityPopup();
    }

    public TradeScreen navigateToTrade() {
        LogUtil.info("Navigating to Trade screen");
        WaitUtil.waitForElementToBeVisible(driver, tradeNavButton, "Trade navigation button");
        tap(tradeNavButton, "Trade navigation button");

        return new TradeScreen(driver);
    }

    public ProfileScreen navigateToProfile() {
        LogUtil.info("Navigating to Profile screen");

        WaitUtil.waitForElementToBeVisible(driver,profileNavButton, "Profile navigation button");
        tap(profileNavButton, "Profile navigation button");

        return new ProfileScreen(driver);
    }
}
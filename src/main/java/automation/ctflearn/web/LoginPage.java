package automation.ctflearn.web;

import automation.utils.*;
import automation.utils.web.BaseWebPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BaseWebPage {
    @FindBy(xpath = "//input[@class='form-control' and @name='identifier']")
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@class='form-control' and @type='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit' and contains(@class, 'btn-primary')]")
    private WebElement loginButton;

    @FindBy(xpath = "//h1[contains(text(), 'Login')]")
    private WebElement loginHeader;

    public LoginPage(WebDriver driver) {
        super(driver);
        String loginUrl = ConfigLoader.getProperty("web.url") + "/user/login";
        driver.get(loginUrl);
        waitForPageToLoad();
        try {
            WaitUtil.waitForElementToBeVisible(driver, usernameInput, "Username or email");
            if (!isLoginPageLoaded()) {
                LogUtil.warn("Login page might not be fully loaded");
            }
        } catch (Exception e) {
            LogUtil.error("Failed to load login page: " + e.getMessage());
            throw new IllegalStateException("Login page did not load properly: " + e.getMessage());
        }
    }

    public boolean isLoginPageLoaded() {
        try {
            return Element.isElementDisplayed(loginHeader) &&
                    Element.isElementDisplayed(usernameInput) &&
                    Element.isElementDisplayed(passwordInput) &&
                    Element.isElementDisplayed(loginButton);
        } catch (Exception e) {
            LogUtil.warn("Error checking login page elements: " + e.getMessage());
            return false;
        }
    }

    public static LoginPage navigateToLoginPage(WebDriver driver) {
        return new LoginPage(driver);
    }

    public HomePage login(String email, String password) {
        LogUtil.info("Logging in with email: " + email);
        Element.sendKeys(driver, usernameInput, email, "email input");
        Element.sendKeys(driver, passwordInput, password, "Password input");
        Element.click(driver, loginButton, "Login button");
        return new HomePage(driver);
    }
}
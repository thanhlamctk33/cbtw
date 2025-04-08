package automation.ctflearn.web;

import automation.utils.*;
import automation.utils.web.BaseWebPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BaseWebPage {
    @FindBy(xpath = "//a[@href='/dashboard']")
    private WebElement dashboardLink;

    @FindBy(xpath = "//a[@href='/challenge/1/browse' and @id='navbarDropdownMenuLink']")
    private WebElement challengesLink;

    @FindBy(xpath = "//a[contains(text(), 'Scoreboard')]")
    private WebElement scoreboardLink;

    @FindBy(xpath = "//div[contains(text(), 'Get Started')]")
    private WebElement getStartedSection;

    @FindBy(xpath = "//button[contains(text(), 'resend()')]")
    private WebElement resendEmailButton;

    @FindBy(xpath = "//a[@id='profileDropdown']")
    private WebElement userProfileIcon;

    @FindBy(xpath = "//a[@href='/user/logout']")
    private WebElement logoutButton;

    public HomePage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
        AssertHelper.assertElementIsDisplayed("challengesLink", challengesLink);
        AssertHelper.assertElementIsDisplayed("dashboardLink", dashboardLink);
        AssertHelper.assertElementIsDisplayed("scoreboardLink", scoreboardLink);
    }

    public ChallengesPage navigateToChallengesPage() {
        LogUtil.info("Navigating to Challenges page");
        try {
            Element.click(driver, challengesLink, "Challenges link");
            LogUtil.info("Clicked on Challenges link");
            return new ChallengesPage(driver);
        } catch (Exception e) {
            LogUtil.error("Failed to navigate to Challenges page: " + e.getMessage());
            throw new RuntimeException("Failed to navigate to Challenges page", e);
        }
    }

    public void logout() {
        LogUtil.startStep("Logout Process");
        try {
            Element.click(driver, userProfileIcon, "User Profile Icon");
            Element.click(driver, logoutButton, "Logout Button");
            LogUtil.pass("Logout successful");
        } catch (Exception e) {
            LogUtil.fail("Logout failed: " + e.getMessage());
            LogUtil.endStep("Logout Process");
            throw new RuntimeException("Failed to logout", e);
        }
    }
}
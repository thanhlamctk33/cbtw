package automation.ctflearn.web;

import automation.utils.*;
import automation.utils.web.BaseWebPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ChallengesPage extends BaseWebPage {
    @FindBy(xpath = "//h1[contains(text(), 'Challenges')]")
    private WebElement challengesHeader;

    @FindBy(xpath = "//input[@placeholder='Search by author, title, or ID']")
    private WebElement searchByID;

    @FindBy(xpath = " //select[@id='category']")
    private WebElement searchByCategory;

    @FindBy(xpath = " //select[@id='difficulty']")
    private WebElement searchByDifficulty;

    @FindBy(xpath = " //select[@id='solved']")
    private WebElement searchBySolved;

    @FindBy(xpath = " //select[@id='order']")
    private WebElement searchByOrder;

    @FindBy(xpath = "//a[contains(@href, '/challenge/create')]")
    private WebElement createChallengeLink;

    @FindBy(xpath = "//a[contains(@class, 'dropdown-item') and contains(text(), 'Create Challenge')]")
    private WebElement createChallengeMenuItem;

    public ChallengesPage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
        AssertHelper.assertElementIsDisplayed("searchByID", searchByID);
        AssertHelper.assertElementIsDisplayed("searchByCategory", searchByCategory);
        AssertHelper.assertElementIsDisplayed("searchByDifficulty", searchByDifficulty);
        AssertHelper.assertElementIsDisplayed("searchBySolved", searchBySolved);
        AssertHelper.assertElementIsDisplayed("searchByOrder", searchByOrder);
        LogUtil.info("Challenges page loaded successfully");
    }

    public CreateChallengePage clickCreateChallenge() {
        LogUtil.info("Clicking Create Challenge link");
        try {
            if (Element.isElementDisplayed(createChallengeLink)) {
                Element.click(driver, createChallengeLink, "Create Challenge link");
            } else {
                if (Element.isElementDisplayed(createChallengeMenuItem)) {
                    Element.click(driver,
                            createChallengeMenuItem, "Create Challenge menu item");
                } else {
                    LogUtil.info("Menu item not found, trying to navigate directly");
                    driver.get(ConfigLoader.getProperty("web.url") + "/challenge/create");
                }
            }
            waitForPageToLoad();
            return new CreateChallengePage(driver);
        } catch (Exception e) {
            LogUtil.error("Failed to click Create Challenge link: " + e.getMessage());
            throw new RuntimeException("Failed to click Create Challenge link", e);
        }
    }
}
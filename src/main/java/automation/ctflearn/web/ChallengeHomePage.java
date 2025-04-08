package automation.ctflearn.web;

import automation.utils.*;
import automation.utils.web.BaseWebPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ChallengeHomePage extends BaseWebPage {
    @FindBy(xpath = "//h1[contains(text(), 'Challenges')]")
    private WebElement challengeTitleLabel;

    @FindBy(xpath = "//div[@id='description-display']")
    private WebElement challengeDescriptionLabel;

    @FindBy(xpath = "//span[@id='points-display']")
    private WebElement challengePointLabel;

    @FindBy(xpath = "//span[@id='category-display']")
    private WebElement challengeCategoryLabel;

    @FindBy(xpath = "//a[@id='fileName']")
    private WebElement challengeFileName;

    public ChallengeHomePage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
        AssertHelper.assertElementIsDisplayed("challengeDescriptionLabel", challengeDescriptionLabel);
        AssertHelper.assertElementIsDisplayed("challengePointLabel", challengePointLabel);
        AssertHelper.assertElementIsDisplayed("challengeCategoryLabel", challengeCategoryLabel);
        LogUtil.info("Challenge page loaded successfully");
    }

    public void verifyChallengeDetails(String expectedTitle, String expectedDescription,
                                       String expectedCategory, String expectedPoints, String fileName) {
        AssertHelper.compareEquals("Challenge Title", expectedTitle, Element.getText(challengeTitleLabel, "Challenge Title Label"));
        AssertHelper.compareEquals("Challenge Description", expectedDescription, Element.getText(challengeDescriptionLabel, "Challenge Description Label"));
        AssertHelper.compareEquals("Challenge Points", expectedPoints, Element.getText(challengePointLabel, "Challenge Points Label"));
        AssertHelper.compareEquals("Challenge Category", expectedCategory, Element.getText(challengeCategoryLabel, "Challenge Category Label"));
        AssertHelper.compareEquals("Challenge File Name", expectedCategory, Element.getText(challengeFileName, "Challenge FileName Label"));
    }
}
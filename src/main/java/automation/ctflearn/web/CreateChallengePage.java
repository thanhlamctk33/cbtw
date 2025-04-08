package automation.ctflearn.web;

import automation.utils.*;
import automation.utils.web.BaseWebPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CreateChallengePage extends BaseWebPage {
    @FindBy(xpath = "//select[@id='event_id']")
    private WebElement evenTitleLabel;

    @FindBy(xpath = "//input[@id='title']")
    private WebElement titleInput;

    @FindBy(xpath = "//input[@id='flag']")
    private WebElement flagInput;

    @FindBy(xpath = "//textarea[@id='flask-pagedown-description']")
    private WebElement descriptionTextarea;

    @FindBy(xpath = "//input[@id='file-upload']")
    private WebElement fileUpload;

    @FindBy(xpath = "//select[@id='category']")
    private WebElement categoryDropdown;

    @FindBy(xpath = "//select[@id='points']")
    private WebElement pointsDropdown;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileUploadInput;

    @FindBy(xpath = "//textarea[@id='howtosolve']")
    private WebElement howToSolveTextarea;

    @FindBy(xpath = "//button[@class ='btn btn-success form-control']")
    private WebElement submitButton;

    @FindBy(xpath = "//span[text()='Create A Challenge']")
    private WebElement pageHeader;

    public CreateChallengePage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
        LogUtil.info("Create Challenge Page loaded");
        verifyCreateChallengePageLoaded();
    }

    public void verifyCreateChallengePageLoaded() {
        AssertHelper.assertElementIsDisplayed("Create Challenge header", pageHeader);
        AssertHelper.assertElementIsDisplayed("Even Title", evenTitleLabel);
        AssertHelper.assertElementIsDisplayed("Title Input", titleInput);
        AssertHelper.assertElementIsDisplayed("Flag Input", flagInput);
        AssertHelper.assertElementIsDisplayed("SubmitButton", submitButton);
        AssertHelper.assertElementIsDisplayed("Description Textarea", descriptionTextarea);
    }

    public void fillChallengeTitle(String title) {
        LogUtil.info("Filling challenge title: " + title);
        Element.sendKeys(driver, titleInput, title, "Challenge flag");
    }

    public void fillChallengeFlag(String flag) {
        LogUtil.info("Filling challenge flag: " + flag);
        Element.sendKeys(driver, flagInput, flag, "Challenge flag");
    }

    public void fillChallengeDescription(String description) {
        LogUtil.info("Filling challenge description");
        Element.sendKeys(driver, descriptionTextarea, description, "Challenge description");
    }

    public void fillHowToSolve(String description) {
        LogUtil.info("Filling challenge description");
        Element.sendKeys(driver, howToSolveTextarea, description, "How to solve description");
    }

    public void selectChallengeCategory(String category) {
        LogUtil.info("Selecting challenge category: " + category);
        Element.select(categoryDropdown, category);
    }

    public void setPoints(String points) {
        LogUtil.info("Setting challenge points: " + points);
        Element.select(pointsDropdown, points);
    }

    public void uploadChallengeFile(String filePath) {
        LogUtil.info("Upload Challenge File");
        Element.uploadFile(fileUploadInput, filePath);
    }

    public ChallengeHomePage submitChallenge() {
        LogUtil.info("Submitting new challenge");
        try {
            Element.click(driver, submitButton, "Submit challenge button");
            waitForPageToLoad();
            return new ChallengeHomePage(driver);
        } catch (Exception e) {
            LogUtil.error("Failed to submit challenge: " + e.getMessage());
            throw new RuntimeException("Failed to submit challenge", e);
        }
    }

    public ChallengeHomePage createChallenge(String title, String flag, String description,
                                             String category, String points, String howToSolve, String filePath) {
        LogUtil.info("Creating new challenge with title: " + title);
        fillChallengeTitle(title);
        fillChallengeFlag(flag);
        fillChallengeDescription(description);
        selectChallengeCategory(category);
        setPoints(points);
        fillHowToSolve(howToSolve);
        if (filePath != null && !filePath.isEmpty()) {
            uploadChallengeFile(filePath);
        }
        return submitChallenge();
    }
}
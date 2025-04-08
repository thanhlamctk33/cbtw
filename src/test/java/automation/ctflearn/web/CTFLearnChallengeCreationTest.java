package automation.ctflearn.web;

import automation.ctflearn.dataObject.web.CTFLearnChallenge;
import automation.utils.PathManager;
import automation.utils.TestBase;
import automation.utils.LogUtil;
import org.testng.annotations.Test;

public class CTFLearnChallengeCreationTest extends TestBase {

    static String fileName =  "ex.pdf";
    private static final String uploadFilePath = PathManager.getPdfFilePath(fileName);
    CTFLearnChallenge challenge = new CTFLearnChallenge("CTFLearn.json");

    @Test(description = "Web UI E2E Test Flow for Challenge Creation")
    public void testChallengeCreation() {
        LogUtil.startStep("Login to CTFLearn");
        LoginPage loginPage = LoginPage.navigateToLoginPage(driver);
        HomePage homePage = loginPage.login(webUsername, webPassword);

        LogUtil.startStep(" Navigate to Challenges page and click create challenge");
        ChallengesPage challengesPage = homePage.navigateToChallengesPage();
        CreateChallengePage createChallengePage = challengesPage.clickCreateChallenge();

        LogUtil.startStep("Create a challenge");
        ChallengeHomePage newChallengePage = createChallengePage.createChallenge(
                challenge.getTitle(),
                challenge.getFlag(),
                challenge.getDescription(),
                challenge.getCategory(),
                challenge.getPoints(),
                challenge.getHowToSolve(),
                uploadFilePath
        );

        LogUtil.startStep("verify the created challenge is displayed");
        newChallengePage.verifyChallengeDetails(challenge.getTitle(),
                challenge.getDescription(), challenge.getCategory(), challenge.getPoints(), fileName
        );

        LogUtil.startStep("Logout from the application");
        homePage.logout();
    }
}
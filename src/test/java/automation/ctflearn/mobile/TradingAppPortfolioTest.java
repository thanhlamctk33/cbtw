package automation.ctflearn.mobile;

import automation.utils.mobile.Mobile;
import automation.utils.LogUtil;
import automation.utils.TestBase;
import org.testng.annotations.Test;

@Mobile
public class TradingAppPortfolioTest extends TestBase {

    private LoginScreen loginScreen;

    @Test
    public void tradingAppE2ETest() {
        LogUtil.startStep("Initialize LoginScreen for Exness trading app");
        loginScreen = new LoginScreen(appiumDriver);

        LogUtil.startStep("Click on Sign In button");
        loginScreen.clickSignIn();

        LogUtil.startStep("Login with valid credentials");
        loginScreen.login(email, password);

        LogUtil.startStep("Enter passcode");
        loginScreen.enterPasscode(passcode);

        LogUtil.startStep("Re-enter passcode for confirmation");
        loginScreen.reEnterPasscode(passcode);

        LogUtil.startStep("Navigate to Trade screen and validate investment data");
        TradeScreen tradeScreen = loginScreen.navigateToTrade();

        LogUtil.startStep("Validate trading data");
        tradeScreen.verifyTradeScreenValueDisplayed();

        LogUtil.startStep("Navigate to Profile");
        ProfileScreen profileScreen = loginScreen.navigateToProfile();

        LogUtil.startStep("Logout");
        profileScreen.logout();

        LogUtil.endTest("Exness Trading App E2E Test", true);
    }
}
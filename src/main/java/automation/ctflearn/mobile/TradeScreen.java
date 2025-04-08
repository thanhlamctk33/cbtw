package automation.ctflearn.mobile;

import automation.utils.*;
import automation.utils.mobile.BaseMobilePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;
public class TradeScreen extends BaseMobilePage {

    @AndroidFindBy(id = "com.exness.android.pa:id/instrumentView")
    private WebElement instrumentView;

    @AndroidFindBy(id = "com.exness.android.pa:id/priceView")
    private WebElement priceView;

    @AndroidFindBy(id = "com.exness.android.pa:id/nameView")
    private WebElement nameView;

    @AndroidFindBy(id = "com.exness.android.pa:id/percentView")
    private WebElement percentView;

    @AndroidFindBy(id = "com.exness.android.pa:id/sparkLine")
    private WebElement sparkLineGraph;

    @AndroidFindBy(xpath = "//*[@resource-id='com.exness.android.pa:id/instrumentView']")
    private List<WebElement> instrumentList;

    public TradeScreen(AppiumDriver driver) {
        super(driver);
    }

    public void verifyTradeScreenValueDisplayed() {
        LogUtil.info("Checking if Trade screen is properly displayed");
        WaitUtil.waitForElementToBeVisible(driver, instrumentView, "Instrument view");
        AssertHelper.assertElementIsDisplayed("instrumentView ", instrumentView);
        AssertHelper.assertElementIsDisplayed("priceView ", priceView);
        AssertHelper.assertElementIsDisplayed("nameView ", nameView);
        AssertHelper.assertElementIsDisplayed("percentView ", percentView);
        AssertHelper.assertElementIsDisplayed("instrumentView ", instrumentView);
    }
}
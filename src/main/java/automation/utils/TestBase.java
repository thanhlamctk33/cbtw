package automation.utils;

import automation.utils.mobile.AppLauncher;
import automation.utils.mobile.AppiumDriverManager;
import automation.utils.mobile.Mobile;
import automation.utils.reporting.AllureReportManager;
import automation.utils.reporting.HtmlReportManager;
import automation.utils.web.WebDriverFactory;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Base class for all test classes
 */
public class TestBase {
    protected WebDriver driver;
    protected AppiumDriver appiumDriver;
    protected SoftAssert softAssert;
    protected boolean isMobile;

    // Common credentials for mobile tests
    protected String email;
    protected String password;
    protected String passcode;

    // Web credentials
    protected String webUsername;
    protected String webPassword;

    /**
     * Setup method run before suite
     *
     * @param context TestNG test context
     */
    @BeforeSuite
    public void beforeSuite(ITestContext context) {
        ConfigLoader.init();
        LogUtil.startTest("Test Suite: " + context.getSuite().getName());
    }

    /**
     * Setup method run before each test class
     */
    @BeforeClass
    public void beforeClass() {
        LogUtil.info("Starting test class: " + this.getClass().getSimpleName());
        softAssert = new SoftAssert();
        webUsername = ConfigLoader.getProperty("web.username");
        webPassword = ConfigLoader.getProperty("web.password");
        LogUtil.info("Loaded web credentials for username: " + webUsername);
    }

    /**
     * Setup method run before each test method
     *
     * @param method      Test method
     * @param testContext TestNG test context
     */
    @BeforeMethod
    public void beforeMethod(Method method, ITestContext testContext) {
        String testName = method.getName();
        LogUtil.startTest("Test Method: " + testName);

        isMobile = method.isAnnotationPresent(Mobile.class) ||
                this.getClass().isAnnotationPresent(Mobile.class);

        if (isMobile) {
            setupMobileTest();
        } else {
            setupWebTest();
        }
    }

    private void setupWebTest() {
        LogUtil.info("Setting up web test");
        driver = WebDriverFactory.getDriver();
    }

    private void setupMobileTest() {
        LogUtil.info("Setting up mobile test");
        try {
            email = ConfigLoader.getProperty("mobile.username");
            password = ConfigLoader.getProperty("mobile.password");
            passcode = ConfigLoader.getProperty("mobile.passcode");
            LogUtil.info("Using credentials - Email: " + email + ", Passcode: " + passcode);

            String platform = ConfigLoader.getProperty("appium.platform", "android");
            appiumDriver = AppiumDriverManager.getDriver(platform);
            driver = appiumDriver;
        } catch (Exception e) {
            LogUtil.error("Failed to set up mobile test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @BeforeMethod(dependsOnMethods = "beforeMethod")
    public void launchApp() {
        if (isMobile) {
            try {
                LogUtil.info("Launching mobile app using AppLauncher");
                appiumDriver = AppLauncher.launchExnessApp();
                driver = appiumDriver; // Update driver reference
                LogUtil.info("Mobile app launched successfully");
            } catch (Exception e) {
                LogUtil.error("Failed to launch mobile app: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        // Take screenshot if test failed and driver is available
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            try {
                takeScreenshot(result.getName());
            } catch (Exception e) {
                LogUtil.error("Failed to take screenshot: " + e.getMessage());
            }
        }

        if (result.getStatus() == ITestResult.FAILURE) {
            LogUtil.error("Test Failed: " + result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            LogUtil.pass("Test Passed: " + result.getName());
        } else {
            LogUtil.warn("Test Skipped: " + result.getName());
        }

        LogUtil.endTest(result.getName(), result.isSuccess());
    }

    @AfterMethod(dependsOnMethods = "afterMethod")
    public void closeMobileDriver() {
        if (isMobile && appiumDriver != null) {
            LogUtil.info("Closing AppiumDriver");
            appiumDriver.quit();
        }
    }

    protected void takeScreenshot(String testName) {
        if (driver instanceof TakesScreenshot) {
            try {
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                saveScreenshotToFile(FileUtils.readFileToByteArray(screenshotFile), testName);
            } catch (Exception e) {
                LogUtil.error("Failed to take screenshot: " + e.getMessage());
            }
        }
    }

    @AfterClass
    public void afterClass() {
        LogUtil.info("Finishing test class: " + this.getClass().getSimpleName());
    }

    @AfterSuite
    public void afterSuite(ITestContext context) {
        LogUtil.info("Cleaning up resources");
        WebDriverFactory.quitAllDrivers();
        AppiumDriverManager.cleanup();
        LogUtil.endTest("Test Suite: " + context.getSuite().getName(), true);
    }

    private void saveScreenshotToFile(byte[] screenshot, String testName) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String filename = "screenshot_" + testName + "_" + timestamp + ".png";
            String directory = "test-output/screenshots/";
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileUtils.writeByteArrayToFile(new File(directory + filename), screenshot);
            LogUtil.info("Screenshot saved to: " + directory + filename);
        } catch (IOException e) {
            LogUtil.error("Error saving screenshot to file: " + e.getMessage());
        }
    }
    // Add these methods to your TestBase class to integrate with HTML reporting

    /**
     * Get the current test method name
     *
     * @return Current test method name
     */
    protected String getTestMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /**
     * Log a step in the HTML report
     *
     * @param stepName Name of the step
     */
    protected void logStepInReport(String stepName) {
        LogUtil.startStep(stepName);
        ExtentReportManager.logInfo(stepName);
        HtmlReportManager.addTestLog(getTestMethodName(), "INFO", stepName);
        Allure.step(stepName);
    }

    /**
     * Log a passed step in the HTML report
     *
     * @param message Pass message
     */
    protected void logPassInReport(String message) {
        LogUtil.pass(message);
        ExtentReportManager.logPass(message);
        HtmlReportManager.addTestLog(getTestMethodName(), "PASS", message);
        Allure.step(message, io.qameta.allure.model.Status.PASSED);
    }

    /**
     * Log a failed step in the HTML report
     *
     * @param message Failure message
     */
    protected void logFailInReport(String message) {
        LogUtil.fail(message);
        ExtentReportManager.logFail(message);
        HtmlReportManager.addTestLog(getTestMethodName(), "FAIL", message);
        Allure.step(message, io.qameta.allure.model.Status.FAILED);

        // Take screenshot on failure
        takeScreenshot(getTestMethodName() + "_failure");
    }

    /**
     * Log a warning in the HTML report
     *
     * @param message Warning message
     */
    protected void logWarningInReport(String message) {
        LogUtil.warn(message);
        ExtentReportManager.logWarning(message);
        HtmlReportManager.addTestLog(getTestMethodName(), "WARNING", message);
        Allure.step(message, io.qameta.allure.model.Status.BROKEN);
    }

    /**
     * Add a screenshot to the HTML report
     *
     * @param testName Test name
     * @param screenshotPath Path to the screenshot
     * @param description Screenshot description
     */
    protected void addScreenshotToReport(String testName, String screenshotPath, String description) {
        HtmlReportManager.addScreenshot(testName, screenshotPath, description);
        ExtentReportManager.addScreenshot(screenshotPath, description);

        try {
            AllureReportManager.attachScreenshot(description, new File(screenshotPath));
        } catch (Exception e) {
            LogUtil.error("Failed to attach screenshot to Allure report: " + e.getMessage());
        }
    }

    /**
     * Assert condition with detailed reporting
     *
     * @param condition Condition to assert
     * @param message Message to log
     */
    protected void assertWithReporting(boolean condition, String message) {
        if (condition) {
            logPassInReport("PASS: " + message);
        } else {
            logFailInReport("FAIL: " + message);
            throw new AssertionError(message);
        }
    }

    /**
     * Add test result to HTML report
     *
     * @param testName Test name
     * @param className Class name
     * @param status Test status (PASS, FAIL, SKIP)
     * @param startTime Test start time
     * @param endTime Test end time
     * @param throwable Exception if test failed
     */
    protected void addTestResultToReport(String testName, String className, String status,
                                         long startTime, long endTime, Throwable throwable) {
        HtmlReportManager.addTestResult(testName, className, status, startTime, endTime, throwable);
    }
}
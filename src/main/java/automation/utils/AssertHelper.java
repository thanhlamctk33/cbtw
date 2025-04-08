package automation.utils;

import org.openqa.selenium.WebElement;

public class AssertHelper {

    public static void assertElementIsDisplayed(String description, WebElement element) {
        boolean result = isElementDisplayed(element);
        if (result) {
            LogUtil.pass("Element '" + description + "' is displayed on the page");
        } else {
            LogUtil.fail("Element '" + description + "' is NOT displayed on the page");
        }
    }

    public static <T> void compareEquals(String what, T expected, T actual) {
        compareEquals(what, expected, actual, false);
    }

    public static <T> void compareEquals(String what, T expected, T actual, Boolean strict) {
        if (expected == null && actual == null) {
            LogUtil.pass(what + ": " + actual);
            return;
        }
        if (actual != null) {
            if (!actual.equals(expected)) {
                if (strict) {
                    LogUtil.error("Expected '" + what + "' was :-'" + expected + "'. But actual is '" + actual + "'");
                    throw new AssertionError("Expected '" + what + "' was :-'" + expected + "'. But actual is '" + actual + "'");
                } else {
                    LogUtil.fail(what + "Failed : " + expected + "...");
                }
            } else {
                LogUtil.pass(what + ": " + actual);
            }
        } else {
            if (strict) {
                LogUtil.error("Expected '" + what + "' was :-'" + expected + "'. But actual is '" + actual + "'");
                throw new AssertionError("Expected '" + what + "' was :-'" + expected + "'. But actual is '" + actual + "'");
            } else {
                LogUtil.fail(what + "Failed : " + expected + "...");
            }
        }
    }

    private static boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
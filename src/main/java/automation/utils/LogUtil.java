package automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;

/**
 * Utility class for logging
 */
public class LogUtil {
    private static final Logger logger = LogManager.getLogger(LogUtil.class);
    private static boolean isReportingEnabled = true;
    private static boolean isConsoleLoggingEnabled = true;
    private static boolean isFileLoggingEnabled = true;

    public static void init(boolean reportingEnabled, boolean consoleLoggingEnabled, boolean fileLoggingEnabled) {
        isReportingEnabled = reportingEnabled;
        isConsoleLoggingEnabled = consoleLoggingEnabled;
        isFileLoggingEnabled = fileLoggingEnabled;
    }

    public static void info(String message) {
        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.info(message);
        }
        if (isReportingEnabled) {
            Reporter.log(message, true);
        }
    }

    public static void warn(String message) {
        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.warn(message);
        }
        if (isReportingEnabled) {
            Reporter.log("<font color='orange'><b>WARNING: </b>" + message + "</font>", true);
        }
    }

    public static void error(String message) {
        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.error(message);
        }
        if (isReportingEnabled) {
            Reporter.log("<font color='red'><b>ERROR: </b>" + message + "</font>", true);
        }
    }

    public static void debug(String message) {
        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.debug(message);
        }
        if (isReportingEnabled && ConfigLoader.getPropertyAsBoolean("debug.mode", false)) {
            Reporter.log("<font color='gray'><b>DEBUG: </b>" + message + "</font>", true);
        }
    }

    public static void startStep(String stepName) {
        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.info("STEP: Starting " + stepName);
        }
        if (isReportingEnabled) {
            Reporter.log("<br/><font color='blue'><b>STEP: Starting " + stepName + "</b></font>", true);
        }
    }

    public static void endStep(String stepName) {
        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.info("STEP: Completed " + stepName);
        }
        if (isReportingEnabled) {
            Reporter.log("<font color='green'><b>STEP: Completed " + stepName + "</b></font><br/>", true);
        }
    }

    public static void startTest(String testName) {
        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.info("TEST: Starting " + testName);
        }
        if (isReportingEnabled) {
            Reporter.log("<br/><font color='blue' size='3'><b>TEST: Starting " + testName + "</b></font><br/>", true);
        }
    }

    public static void endTest(String testName, boolean result) {
        String resultText = result ? "PASSED" : "FAILED";
        String color = result ? "green" : "red";

        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.info("TEST: " + testName + " - " + resultText);
        }
        if (isReportingEnabled) {
            Reporter.log("<br/><font color='" + color + "' size='3'><b>TEST: " + testName + " - " + resultText + "</b></font><br/>", true);
        }
    }

    public static void pass(String message) {
        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.info("PASS: " + message);
        }
        if (isReportingEnabled) {
            Reporter.log("<font color='green'><b>PASS: </b>" + message + "</font>", true);
        }
    }

    public static void fail(String message) {
        if (isConsoleLoggingEnabled || isFileLoggingEnabled) {
            logger.error("FAIL: " + message);
        }
        if (isReportingEnabled) {
            Reporter.log("<font color='red'><b>FAIL: </b>" + message + "</font>", true);
        }
    }
}
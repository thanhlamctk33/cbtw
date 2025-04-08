package automation.utils;

import java.io.File;

public class PathManager {
    public static final String TEST_DATA_DIR = "testdata";
    public static final String JSON_DIR = TEST_DATA_DIR + File.separator + "jsonfile";
    public static final String PDF_DIR = TEST_DATA_DIR + File.separator + "pdffile";

    public static String getJsonFilePath(String fileName) {
        return JSON_DIR + File.separator + fileName;
    }

    public static String getPdfFilePath(String fileName) {
        return PDF_DIR + File.separator + fileName;
    }

    public static String getTestDataFilePath(String subDir, String fileName) {
        return TEST_DATA_DIR + File.separator + subDir + File.separator + fileName;
    }
}
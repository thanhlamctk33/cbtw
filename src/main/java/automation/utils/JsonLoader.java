package automation.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonLoader {
    private static Map<String, JSONObject> jsonDataCache = new HashMap<>();

    public static JSONObject loadJsonFromPath(String filePath) {
        if (jsonDataCache.containsKey(filePath)) {
            return jsonDataCache.get(filePath);
        }

        try {
            LogUtil.info("Loading JSON data from: " + filePath);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(filePath));
            jsonDataCache.put(filePath, jsonObject);

            LogUtil.info("Successfully loaded JSON data from: " + filePath);
            return jsonObject;
        } catch (IOException | ParseException e) {
            LogUtil.error("Failed to load JSON data from " + filePath + ": " + e.getMessage());
            return null;
        }
    }
}
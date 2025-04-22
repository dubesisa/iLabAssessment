package com.automation.assessment.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonDataReader {
    private static final Logger logger = LogManager.getLogger(JsonDataReader.class);
    private final ObjectMapper objectMapper;

    public JsonDataReader() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Read data from a JSON file
     * 
     * @param filePath Path to the JSON file
     * @param arrayNodeName Name of the array node to read data from
     * @return List of maps, where each map represents an object in the array
     */
    public List<Map<String, String>> readJsonFile(String filePath, String arrayNodeName) {
        List<Map<String, String>> jsonData = new ArrayList<>();
        
        try {
            // Read JSON file
            byte[] jsonBytes = Files.readAllBytes(Paths.get(filePath));
            JsonNode rootNode = objectMapper.readTree(jsonBytes);
            
            // Get the array node
            JsonNode arrayNode = rootNode.get(arrayNodeName);
            if (arrayNode == null || !arrayNode.isArray()) {
                logger.error("Array node not found or not an array: " + arrayNodeName);
                return jsonData;
            }
            
            // Iterate through array elements
            for (JsonNode elementNode : arrayNode) {
                Map<String, String> element = new HashMap<>();
                
                // Convert each field to a string in the map
                elementNode.fields().forEachRemaining(field -> {
                    String fieldName = field.getKey();
                    String fieldValue = field.getValue().asText();
                    element.put(fieldName, fieldValue);
                });
                
                jsonData.add(element);
            }
            
            logger.info("Read " + jsonData.size() + " items from JSON file: " + filePath);
            
        } catch (IOException e) {
            logger.error("Error reading JSON file: " + e.getMessage());
        }
        
        return jsonData;
    }

    /**
     * Read a specific value from a JSON file
     * 
     * @param filePath Path to the JSON file
     * @param jsonPath Path to the value in the JSON structure (e.g., "user.address.city")
     * @return String value, or null if not found
     */
    public String readJsonValue(String filePath, String jsonPath) {
        try {
            // Read JSON file
            JsonNode rootNode = objectMapper.readTree(new File(filePath));
            
            // Navigate through the JSON path
            String[] pathSegments = jsonPath.split("\\.");
            JsonNode currentNode = rootNode;
            
            for (String segment : pathSegments) {
                currentNode = currentNode.get(segment);
                if (currentNode == null) {
                    logger.error("JSON path segment not found: " + segment);
                    return null;
                }
            }
            
            return currentNode.asText();
            
        } catch (IOException e) {
            logger.error("Error reading JSON file: " + e.getMessage());
            return null;
        }
    }
}

package com.automation.assessment.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVDataReader {
    private static final Logger logger = LogManager.getLogger(CSVDataReader.class);

    /**
     * Read data from a CSV file
     * 
     * @param filePath Path to the CSV file
     * @return List of maps, where each map represents a row with column names as keys
     */
    public List<Map<String, String>> readCSVFile(String filePath) {
        List<Map<String, String>> csvData = new ArrayList<>();
        
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).build()) {
            // Read all lines at once
            List<String[]> allRows = reader.readAll();
            
            if (allRows.isEmpty()) {
                logger.warn("CSV file is empty: " + filePath);
                return csvData;
            }
            
            // Extract header row
            String[] headers = allRows.get(0);
            
            // Process data rows
            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                Map<String, String> rowData = new HashMap<>();
                
                // Map column values to column names
                for (int j = 0; j < headers.length && j < row.length; j++) {
                    rowData.put(headers[j], row[j]);
                }
                
                csvData.add(rowData);
            }
            
            logger.info("Read " + csvData.size() + " rows from CSV file: " + filePath);
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV file: " + e.getMessage());
        }
        
        return csvData;
    }

    /**
     * Read a specific column from a CSV file
     * 
     * @param filePath Path to the CSV file
     * @param columnName Name of the column to read
     * @return List of values in the specified column
     */
    public List<String> readCSVColumn(String filePath, String columnName) {
        List<String> columnValues = new ArrayList<>();
        
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).build()) {
            // Read all lines at once
            List<String[]> allRows = reader.readAll();
            
            if (allRows.isEmpty()) {
                logger.warn("CSV file is empty: " + filePath);
                return columnValues;
            }
            
            // Extract header row and find column index
            String[] headers = allRows.get(0);
            int columnIndex = -1;
            
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equals(columnName)) {
                    columnIndex = i;
                    break;
                }
            }
            
            if (columnIndex == -1) {
                logger.error("Column not found in CSV: " + columnName);
                return columnValues;
            }
            
            // Extract values from the column
            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                if (columnIndex < row.length) {
                    columnValues.add(row[columnIndex]);
                }
            }
            
            logger.info("Read " + columnValues.size() + " values from column '" + columnName + "' in CSV file: " + filePath);
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV file: " + e.getMessage());
        }
        
        return columnValues;
    }
}

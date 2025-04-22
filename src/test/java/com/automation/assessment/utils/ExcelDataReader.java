package com.automation.assessment.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelDataReader {
    private static final Logger logger = LogManager.getLogger(ExcelDataReader.class);

    /**
     * Read data from an Excel file
     * 
     * @param filePath Path to the Excel file
     * @param sheetName Name of the sheet to read data from
     * @return List of maps, where each map represents a row with column names as keys
     */
    public List<Map<String, String>> readExcelFile(String filePath, String sheetName) {
        List<Map<String, String>> excelData = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet not found: " + sheetName);
                return excelData;
            }
            
            // Get header row
            Row headerRow = sheet.getRow(0);
            int columnCount = headerRow.getLastCellNum();
            
            // Iterate through data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                if (dataRow != null) {
                    Map<String, String> rowData = new HashMap<>();
                    
                    // Iterate through columns
                    for (int j = 0; j < columnCount; j++) {
                        Cell headerCell = headerRow.getCell(j);
                        Cell dataCell = dataRow.getCell(j);
                        
                        if (headerCell != null) {
                            String columnName = headerCell.getStringCellValue();
                            String cellValue = getCellValueAsString(dataCell);
                            rowData.put(columnName, cellValue);
                        }
                    }
                    
                    excelData.add(rowData);
                }
            }
            
            logger.info("Read " + excelData.size() + " rows from Excel file: " + filePath);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: " + e.getMessage());
        }
        
        return excelData;
    }
    
    /**
     * Get cell value as string, regardless of cell type
     * 
     * @param cell Excel cell
     * @return String representation of cell value
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}

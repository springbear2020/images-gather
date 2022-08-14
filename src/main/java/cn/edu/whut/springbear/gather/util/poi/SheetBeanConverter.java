package cn.edu.whut.springbear.gather.util.poi;


import cn.edu.whut.springbear.gather.util.poi.exception.ConverterException;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 15:55 Tuesday
 */
public class SheetBeanConverter extends AbstractConverter implements Converter {
    private final String fileAbsolutePath;

    public SheetBeanConverter(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
    }

    /**
     * Converter the sheet row data into java bean list
     */
    @Override
    public <T> List<T> sheetConvertBean(Class<T> clazz) {
        String sheetName = this.getSheetName(clazz);

        try {
            Sheet sheet = this.getWorkSheet(sheetName);
            // Data row size in the work sheet, index start with 0 in the excel file
            int validDataRowSize = sheet.getLastRowNum() + 1;
            if (validDataRowSize < 2) {
                // We assume that the first line is always save the table header info of the work sheet
                throw new ConverterException("There is no valid data in the \"" + sheetName + "\" work sheet");
            }

            // validFieldMap is <sheetColumnName, fieldOfBean> key value set
            Map<String, Field> validFieldsMap = this.validFieldsScan(clazz);
            // Header info of the work sheet (save at the first line in the work sheet)
            List<String> sheetHeaderList = this.getSheetHeader(sheet);
            // Get the index list to traverse the specified columns instead of traversing all columns
            List<Integer> validColumnsIndex = this.getValidSheetColumnIndex(sheetHeaderList, validFieldsMap);
            // Traverse the work sheet rows then generate bean though the row data
            List<T> beanList = this.traverseSheet(sheet, validDataRowSize, clazz, validColumnsIndex, sheetHeaderList, validFieldsMap);

            System.out.println("********************************************************");
            // The first line in the excel file is always save the header info, so valid row data size is (rowSize - 1)
            System.out.println("* [ExcelBeanConverter] Work sheet row data size is " + (validDataRowSize - 1) + " *");
            System.out.println("* [ExcelBeanConverter] Generate number of beans is " + beanList.size() + " *");
            System.out.println("********************************************************");
            return beanList;
        } catch (IOException | ReflectiveOperationException | ConverterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the work sheet object from excel file input stream
     */
    private Sheet getWorkSheet(String sheetName) throws ConverterException, IOException {
        String fileSuffix = fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf('.'));
        // Whether the file is in excel format
        if (!(".xls".equals(fileSuffix) || ".xlsx".equals(fileSuffix))) {
            throw new ConverterException(fileAbsolutePath + " (The file you chosen is not a excel file)");
        }
        Workbook workbook = HSSFWorkbookFactory.create(new File(fileAbsolutePath));
        if (workbook == null) {
            throw new ConverterException("The file you chosen is not a excel file");
        }

        // Specified sheet including row data
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new ConverterException("@ExcelSheetName(\"" + sheetName + "\") (The sheet name you given is not exists)");
        }

        return sheet;
    }

    /**
     * Get the table header info of the work sheet, we assume the first line is always save the header info
     */
    private List<String> getSheetHeader(Sheet sheet) {
        List<String> sheetHeaderList = new ArrayList<>();
        // First line is always save the header info
        Row row = sheet.getRow(0);
        for (Cell cell : row) {
            sheetHeaderList.add(cell.getStringCellValue());
        }
        return sheetHeaderList;
    }

    /**
     * Get the valid index list of the work sheet,
     * the valid index list is meaning you use @SheetColumnName to specify in the bean class field,
     * get the index list to traverse the specified columns instead of traversing all columns in order to save time
     *
     * @param sheetHeaderList Header info of the work sheet (first line strings)
     * @param validFieldMap   <columnName, beanField>
     * @return Valid column index list
     */
    private List<Integer> getValidSheetColumnIndex(List<String> sheetHeaderList, Map<String, Field> validFieldMap) {
        List<Integer> validSheetColumnIndex = new ArrayList<>();
        // All valid column names in the valid fields map
        Set<String> validColumnNames = validFieldMap.keySet();
        int sheetCols = sheetHeaderList.size();

        for (String validColumnName : validColumnNames) {
            for (int i = 0; i < sheetCols; ++i) {
                if (validColumnName.equals(sheetHeaderList.get(i))) {
                    // Add the relevant column index of the work sheet
                    validSheetColumnIndex.add(i);
                }
            }
        }

        return validSheetColumnIndex;
    }

    /**
     * Traverse the work sheet rows then generate bean though the row data
     */
    private <T> List<T> traverseSheet(Sheet sheet, int rowSize, Class<T> clazz,
                                      List<Integer> validSheetColumnIndex, List<String> sheetHeaderList, Map<String, Field> validFieldMap) throws ReflectiveOperationException {
        List<T> beanList = new ArrayList<>();

        // Traverse the rows and generate bean, begin with the second line because we assume the first line save the header info
        for (int rowIndex = 1; rowIndex < rowSize; ++rowIndex) {
            Row row = sheet.getRow(rowIndex);
            T instance = clazz.newInstance();

            // Just go though the specified columns instead of traversing the all columns in order to save time
            for (Integer sheetColumnIndex : validSheetColumnIndex) {
                // Current cell data
                Cell cell = row.getCell(sheetColumnIndex);
                // The column name of the current column of work sheet
                String sheetColumnName = sheetHeaderList.get(sheetColumnIndex);
                // The relevant field of current column name
                Field field = validFieldMap.get(sheetColumnName);
                // Assign the cell data to the relevant field
                setFieldValue(cell, field, instance);
            }

            beanList.add(instance);
        }

        return beanList;
    }

    /**
     * Assign the value of the instance's field according to the current cell data
     */
    private <T> void setFieldValue(Cell cell, Field field, T instance) throws IllegalAccessException {
        if (cell != null) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            // Assign value for current field according it's java type and the cell type
            if (Date.class.equals(fieldType)) {
                field.set(instance, cell.getDateCellValue());
            } else if (String.class.equals(fieldType)) {
                cell.setCellType(CellType.STRING);
                String cellValue = cell.getStringCellValue();
                cellValue = cellValue.trim();
                field.set(instance, cellValue);
            } else if (Boolean.class.equals(fieldType)) {
                cell.setCellType(CellType.BOOLEAN);
                field.set(instance, cell.getBooleanCellValue());
            } else if (Character.class.equals(fieldType)) {
                cell.setCellType(CellType.STRING);
                String cellValue = cell.getStringCellValue();
                cellValue = cellValue.trim();
                field.set(instance, cellValue);
            } else if (Byte.class.equals(fieldType)) {
                field.set(instance, (byte) ((int) cell.getNumericCellValue()));
            } else if (Short.class.equals(fieldType)) {
                field.set(instance, (short) ((int) cell.getNumericCellValue()));
            } else if (Integer.class.equals(fieldType)) {
                field.set(instance, (int) cell.getNumericCellValue());
            } else if (Float.class.equals(fieldType)) {
                field.set(instance, (float) cell.getNumericCellValue());
            } else if (Long.class.equals(fieldType)) {
                field.set(instance, (long) cell.getNumericCellValue());
            } else if (Double.class.equals(fieldType)) {
                field.set(instance, cell.getNumericCellValue());
            }
        }
    }
}

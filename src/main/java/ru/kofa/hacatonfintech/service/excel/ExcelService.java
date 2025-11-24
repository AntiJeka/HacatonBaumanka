package ru.kofa.hacatonfintech.service.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.kofa.hacatonfintech.model.StoreObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelService implements IExcelService {
    private static final String FILE_PATH = "src/main/resources/allCell.xlsx";

    public String addObject(String name, String cell) {
        Workbook workbook = null;
        File file = new File(FILE_PATH);
        String freeCell = "";

        try {
            if (file.exists()) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            } else {
                workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Sheet1");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Ячейка");
                headerRow.createCell(1).setCellValue("Объект");
            }

            Sheet sheet = workbook.getSheetAt(0);

            if (cell.isEmpty()) {
                Iterator<Row> rowIterator = sheet.rowIterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Cell objectCell = row.getCell(1);

                    if (objectCell == null ||
                            objectCell.getCellType() == CellType.BLANK ||
                            (objectCell.getCellType() == CellType.STRING &&
                                    objectCell.getStringCellValue().isEmpty())) {

                        if (objectCell == null) {
                            objectCell = row.createCell(1);
                        }
                        objectCell.setCellValue(name);
                        freeCell = row.getCell(0).getStringCellValue();
                        break;
                    }
                }
            } else {
                Iterator<Row> rowIterator = sheet.rowIterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }

                boolean cellFound = false;
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Cell cellCell = row.getCell(0);

                    if (cellCell != null && cellCell.getCellType() == CellType.STRING &&
                            cellCell.getStringCellValue().equals(cell)) {

                        Cell objectCell = row.getCell(1);
                        if (objectCell == null) {
                            objectCell = row.createCell(1);
                        }
                        objectCell.setCellValue(name);
                        cellFound = true;

                        freeCell = row.getCell(0).getStringCellValue();
                        break;
                    }
                }

                if (!cellFound) {
                    throw new RuntimeException("Ячейка " + cell + " не найдена в файле");
                }

                return freeCell;
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при работе с Excel файлом", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return name;
    }

    public List<StoreObject> getAllStoreObjects() {
        List<StoreObject> list = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return list;
        }

        try (FileInputStream fileStream = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fileStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cellCell = row.getCell(0);
                Cell nameCell = row.getCell(1);

                if (cellCell != null && cellCell.getCellType() == CellType.STRING &&
                        nameCell != null && nameCell.getCellType() == CellType.STRING &&
                        !nameCell.getStringCellValue().trim().isEmpty()) {

                    String cellNumber = cellCell.getStringCellValue();
                    String objectName = nameCell.getStringCellValue();

                    StoreObject object = new StoreObject(objectName, cellNumber);
                    list.add(object);
                }
            }
            return list;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении Excel файла", e);
        }
    }

    public boolean isAvailable(String cellToSave) {
        boolean flag = false;
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return flag;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(0);

                if (cell != null && cell.getStringCellValue().equals(cellToSave)) {
                    Cell nameCell = row.getCell(1);
                    flag = nameCell == null ||
                            nameCell.getCellType() == CellType.BLANK ||
                            (nameCell.getCellType() == CellType.STRING &&
                                    nameCell.getStringCellValue().isEmpty());
                }
            }
            return flag;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public List<String> getAvailableCells() {
        List<String> availableCells = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return availableCells;
        }

        try (FileInputStream fileStream = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fileStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cellCell = row.getCell(0);
                Cell nameCell = row.getCell(1);

                if (cellCell != null && cellCell.getCellType() == CellType.STRING) {
                    String cellNumber = cellCell.getStringCellValue();

                    boolean isOccupied = nameCell != null &&
                            nameCell.getCellType() == CellType.STRING &&
                            !nameCell.getStringCellValue().trim().isEmpty();

                    if (!isOccupied) {
                        availableCells.add(cellNumber);
                    }
                }
            }
            return availableCells;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении Excel файла", e);
        }
    }

    @Override
    public void deleteCell(String cell) {
        Workbook workbook = null;
        File file = new File(FILE_PATH);

        try {
            if (!file.exists()) {
                return;
            }

            workbook = new XSSFWorkbook(new FileInputStream(file));
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            boolean cellFound = false;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cellCell = row.getCell(0);

                if (cellCell != null && cellCell.getCellType() == CellType.STRING &&
                        cellCell.getStringCellValue().equals(cell)) {

                    Cell objectCell = row.getCell(1);
                    if (objectCell == null) {
                        objectCell = row.createCell(1);
                    }
                    objectCell.setCellValue("");
                    cellFound = true;
                    break;
                }
            }

            if (!cellFound) {
                throw new RuntimeException("Ячейка " + cell + " не найдена в файле");
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при работе с Excel файлом", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
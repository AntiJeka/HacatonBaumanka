package ru.kofa.hacatonfintech.service.excel;

import ru.kofa.hacatonfintech.model.StoreObject;

import java.util.List;

public interface IExcelService {
    String addObject(String name, String cell);
    List<StoreObject> getAllStoreObjects();
    boolean isAvailable(String cellToSave);
    List<String> getAvailableCells();
    void deleteCell(String cell);
}

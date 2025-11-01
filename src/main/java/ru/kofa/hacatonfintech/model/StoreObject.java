package ru.kofa.hacatonfintech.model;

import lombok.Data;

@Data
public class StoreObject {
    private String name;
    private String cellNumber;

    public StoreObject(String name, String cellNumber) {
        this.name = name;
        this.cellNumber = cellNumber;
    }

    public String getName() { return name; }
    public String getCellNumber() { return cellNumber; }
}
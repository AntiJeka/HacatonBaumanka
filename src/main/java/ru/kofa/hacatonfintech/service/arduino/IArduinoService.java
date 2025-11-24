package ru.kofa.hacatonfintech.service.arduino;

import java.io.IOException;

public interface IArduinoService {
    boolean setStatusCell(String statusCell) throws IOException;
}

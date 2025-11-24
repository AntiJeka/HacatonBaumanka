package ru.kofa.hacatonfintech.service.arduino;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class ArduinoService implements IArduinoService{



    @Override
    public boolean setStatusCell(String statusCell) throws IOException {
        SerialPort sp = SerialPort.getCommPort("/dev/ttyACM1");

        // default connection settings for Arduino
        sp.setComPortParameters(9600, 8, 1, 0);

        // block until bytes can be written
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (sp.openPort()) {
            System.out.println("Port is open");
        } else {
            System.out.println("Failed to open port");
            return false;
        }

        sp.getOutputStream().write(statusCell.getBytes());
        sp.getOutputStream().flush();

        if (sp.closePort()) {
            System.out.println("Port is closed");
        } else {
            System.out.println("Failed to close port");
            return false;
        }

        return true;
    }


}

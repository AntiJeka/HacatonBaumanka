package ru.kofa.hacatonfintech.service.arduino;

import com.fazecast.jSerialComm.SerialPort;
import ru.kofa.hacatonfintech.service.excel.ExcelService;

import java.io.IOException;
import java.util.Arrays;

public class ArduinoService implements IArduinoService {

    private SerialPort serialPort;
    private static final int BAUD_RATE = 115200;

    @Override
    public boolean setStatusCell(String statusCell) throws IOException {
        // Закрываем предыдущее соединение, если оно есть
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }

        // Получаем список доступных портов
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Доступные COM порты:");
        for (SerialPort port : ports) {
            System.out.println(" - " + port.getSystemPortName() + ": " + port.getDescriptivePortName());
        }

        // Автоматически находим Arduino порт
        SerialPort arduinoPort = findArduinoPort(ports);

        if (arduinoPort == null) {
            System.err.println("Arduino не найден! Доступные порты: " + Arrays.toString(ports));
            return false;
        }

        this.serialPort = arduinoPort;

        serialPort.setComPortParameters(BAUD_RATE, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 0);

        if (serialPort.openPort()) {
            System.out.println("Порт " + serialPort.getSystemPortName() + " открыт успешно");
        } else {
            System.err.println("Не удалось открыть порт " + serialPort.getSystemPortName());
            return false;
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String command = statusCell + "\n";
        byte[] data = command.getBytes();

        int bytesWritten = serialPort.writeBytes(data, data.length);
        System.out.println("Отправлено " + bytesWritten + " байт: " + command.trim());



        if (serialPort.closePort()) {
            System.out.println("Порт закрыт");
        } else {
            System.err.println("Не удалось закрыть порт");
            return false;
        }

        return bytesWritten == data.length;
    }

    private SerialPort findArduinoPort(SerialPort[] ports) {
        for (SerialPort port : ports) {
            String portDescription = port.getDescriptivePortName().toLowerCase();
            if (portDescription.contains("arduino") ||
                    portDescription.contains("ch340") ||
                    portDescription.contains("usb serial")) {
                return port;
            }
        }

        for (SerialPort port : ports) {
            String portName = port.getSystemPortName().toLowerCase();
            if (portName.startsWith("com") || portName.startsWith("/dev/tty")) {
                return port;
            }
        }

        return null;
    }

    // Метод для ручного указания порта
    public boolean setStatusCell(String statusCell, String portName) throws IOException {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }

        this.serialPort = SerialPort.getCommPort(portName);
        serialPort.setComPortParameters(BAUD_RATE, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 0);

        if (!serialPort.openPort()) {
            System.err.println("Не удалось открыть порт: " + portName);
            return false;
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String command = statusCell + "\n";
        byte[] data = command.getBytes();
        int bytesWritten = serialPort.writeBytes(data, data.length);

        serialPort.closePort();
        return bytesWritten == data.length;
    }
}
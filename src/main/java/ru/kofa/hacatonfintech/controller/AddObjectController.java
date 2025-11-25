package ru.kofa.hacatonfintech.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Setter;
import ru.kofa.hacatonfintech.HelloApplication;
import ru.kofa.hacatonfintech.service.arduino.ArduinoService;
import ru.kofa.hacatonfintech.service.excel.ExcelService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddObjectController implements Initializable {

    private String lastCell = "";
    private final ArduinoService arduinoService = new ArduinoService();

    @Setter
    private ExcelService excelService;

    @Setter
    private HelloApplication helloApp;

    @FXML
    private TextField nameField;

    @FXML
    private TextField cellField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button addButton;

    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> clearError());
        cellField.textProperty().addListener((observable, oldValue, newValue) -> clearError());

        closeButton.setVisible(false);
    }

    @FXML
    private void handleAddObject() {
        String name = nameField.getText().trim();
        String cell = cellField.getText().trim();

        if (name.isEmpty()) {
            showError("Название объекта не может быть пустым");
            return;
        }

        if (name.length() > 100) {
            showError("Название объекта слишком длинное (максимум 100 символов)");
            return;
        }

        try {
            if (excelService != null) {
                boolean flag;
                String command = "OPEN";
                if (!cell.isEmpty()) {
                    flag = excelService.isAvailable(cell);
                    if (flag) {
                        lastCell = excelService.addObject(name, cell);
//                        try {
//                            boolean success = arduinoService.setStatusCell(command);
//                            if (success) {
//                                System.out.println("Команда отправлена: " + command);
//                            } else {
//                                System.err.println("Не удалось отправить команду: " + command);
//                            }
//                        } catch (Exception e) {
//                            System.err.println("Ошибка при отправке команды на Arduino: " + e.getMessage());
//                        }
                        showSuccessAndSwitchButtons("Объект успешно добавлен!");
                    } else {
                        cellField.clear();
                        List<String> list = excelService.getAvailableCells();
                        showError("Данная ячейка занята. Список доступных ячеек: " + list.toString());
                    }
                } else {
                    lastCell = excelService.addObject(name, cell);

//                    try {
//                        boolean success = arduinoService.setStatusCell(command);
//                        if (success) {
//                            System.out.println("Команда отправлена: " + command);
//
//                        } else {
//                            System.err.println("Не удалось отправить команду: " + command);
//                        }
//                    } catch (Exception e) {
//                        System.err.println("Ошибка при отправке команды на Arduino: " + e.getMessage());
//                    }

                    showSuccessAndSwitchButtons("Объект успешно добавлен!");
                }
            } else {
                showError("Сервис не доступен");
            }

        } catch (Exception e) {
            showError("Ошибка при добавлении объекта: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCloseButton() {
        nameField.clear();
        cellField.clear();
        clearError();

        String command = "CLOSE";
//        try {
//            boolean success = arduinoService.setStatusCell(command);
//            if (success) {
//                System.out.println("Команда отправлена: " + command);
//            } else {
//                System.err.println("Не удалось отправить команду: " + command);
//            }
//        } catch (Exception e) {
//            System.err.println("Ошибка при отправке команды на Arduino: " + e.getMessage());
//        }

        lastCell = "";

        addButton.setVisible(true);
        closeButton.setVisible(false);
    }

    @FXML
    private void handleBackButton() {
        try {
            helloApp.showMainMenu();
            if (!lastCell.isEmpty()) {
                System.out.println(lastCell + " CLOSE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: #e74c3c;");
    }

    private void showSuccessAndSwitchButtons(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: #27ae60;");

        nameField.clear();
        cellField.clear();

        addButton.setVisible(false);
        closeButton.setVisible(true);
    }

    private void clearError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }
}
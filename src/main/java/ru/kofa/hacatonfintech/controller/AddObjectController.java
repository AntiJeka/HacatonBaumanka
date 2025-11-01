package ru.kofa.hacatonfintech.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Setter;
import ru.kofa.hacatonfintech.HelloApplication;
import ru.kofa.hacatonfintech.service.ExcelService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddObjectController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> clearError());
        cellField.textProperty().addListener((observable, oldValue, newValue) -> clearError());
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

        String cellToSave = cell.isEmpty() ? "" : cell;

        try {
            if (excelService != null) {
                boolean flag = false;
                if (!cellToSave.isEmpty()) {
                    flag = excelService.isAvailable(cellToSave);
                }

                if (flag) {
                    excelService.addObject(name, cellToSave);
                    nameField.clear();
                    cellField.clear();
                    clearError();

                    showSuccess("Объект успешно добавлен!");
                } else {
                    cellField.clear();
                    List<String> list = excelService.getAvailableCells();
                    showError("Данная ячейка занята. Список доступных ячеек: " + list.toString());
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
    private void handleBackButton() {
        try {
            helloApp.showMainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: #e74c3c;");
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    private void clearError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }
}
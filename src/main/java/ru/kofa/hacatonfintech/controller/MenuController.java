package ru.kofa.hacatonfintech.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import lombok.Setter;
import ru.kofa.hacatonfintech.HelloApplication;

public class MenuController {

    @Setter
    private HelloApplication helloApp;

    @FXML
    private void handleSelectObject() {
        try {
            helloApp.showSelectScene();
        } catch (Exception e) {
            showError("Не удалось загрузить сцену выбора объектов");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddObject() {
        try {
            helloApp.showAddScene();
        } catch (Exception e) {
            showError("Не удалось загрузить сцену добавления объекта");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
package ru.kofa.hacatonfintech.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
import lombok.Setter;
import ru.kofa.hacatonfintech.HelloApplication;
import ru.kofa.hacatonfintech.model.StoreObject;
import ru.kofa.hacatonfintech.service.arduino.ArduinoService;
import ru.kofa.hacatonfintech.service.excel.ExcelService;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SelectController implements Initializable {

    private final ArduinoService arduinoService = new ArduinoService();

    @Setter
    private ExcelService excelService;

    @Setter
    private HelloApplication helloApp;

    @FXML
    private ListView<StoreObject> objectsListView;

    @FXML
    private Label countLabel;

    private Map<StoreObject, Boolean> objectStates;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        objectStates = new HashMap<>();
        objectsListView.setCellFactory(new Callback<ListView<StoreObject>, ListCell<StoreObject>>() {
            @Override
            public ListCell<StoreObject> call(ListView<StoreObject> listView) {
                return new ListCell<StoreObject>() {
                    @Override
                    protected void updateItem(StoreObject item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox();
                            hbox.setStyle("-fx-alignment: center-left; -fx-padding: 15; -fx-background-color: #2c3e50; -fx-background-radius: 8;");
                            hbox.setPrefHeight(70);

                            Label nameLabel = new Label(item.getName());
                            nameLabel.setPrefWidth(600);
                            nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ecf0f1; -fx-font-family: 'Arial';");

                            Label cellLabel = new Label(item.getCellNumber());
                            cellLabel.setPrefWidth(300);
                            cellLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #bdc3c7; -fx-font-family: 'Arial';");

                            HBox buttonBox = new HBox();
                            buttonBox.setSpacing(10);
                            buttonBox.setPrefWidth(320);

                            Button actionButton = new Button();
                            Button deleteButton = new Button("УДАЛИТЬ");

                            boolean isOpen = objectStates.getOrDefault(item, false);
                            updateButtonText(actionButton, isOpen);

                            actionButton.setOnAction(e -> handleObjectAction(item, actionButton, deleteButton));
                            deleteButton.setOnAction(e -> handleDeleteObject(item, actionButton, deleteButton));

                            actionButton.setStyle("-fx-pref-width: 150px; -fx-pref-height: 45px; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-family: 'Arial';");
                            deleteButton.setStyle("-fx-pref-width: 150px; -fx-pref-height: 45px; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-family: 'Arial';");

                            updateButtonStyle(actionButton, isOpen);
                            updateDeleteButtonStyle(deleteButton, isOpen);

                            deleteButton.setVisible(isOpen);

                            buttonBox.getChildren().addAll(actionButton, deleteButton);

                            HBox.setHgrow(nameLabel, Priority.ALWAYS);
                            hbox.getChildren().addAll(nameLabel, cellLabel, buttonBox);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    private void handleObjectAction(StoreObject object, Button actionButton, Button deleteButton) {
        boolean isCurrentlyOpen = objectStates.getOrDefault(object, false);

        boolean newState = !isCurrentlyOpen;
        objectStates.put(object, newState);

        updateButtonText(actionButton, newState);
        updateButtonStyle(actionButton, newState);
        updateDeleteButtonStyle(deleteButton, newState);

        deleteButton.setVisible(newState);

        if (newState) {
            System.out.println(object.getCellNumber() + " OPEN");
        } else {
            System.out.println(object.getCellNumber() + " CLOSE");
        }
    }

    private void handleDeleteObject(StoreObject object, Button actionButton, Button deleteButton) {
        System.out.println(object.getName() + " " + object.getCellNumber() + " CLOSE");

        if (excelService != null) {
            excelService.deleteCell(object.getCellNumber());
        }

        // Удаляем объект из списка
        objectsListView.getItems().remove(object);
        objectStates.remove(object);

        // Обновляем счетчик
        if (countLabel != null) {
            countLabel.setText("Найдено объектов: " + objectsListView.getItems().size());
        }

        // Отправляем команду закрытия на Arduino
//        try {
//            arduinoService.setStatusCell(object.getCellNumber() + " CLOSE");
//        } catch (Exception e) {
//            System.out.println("Ошибка при отправке команды на Arduino: " + e.getMessage());
//        }
    }

    private void updateButtonText(Button button, boolean isOpen) {
        if (isOpen) {
            button.setText("ЗАКРЫТЬ");
        } else {
            button.setText("ОТКРЫТЬ");
        }
    }

    private void updateButtonStyle(Button button, boolean isOpen) {
        if (isOpen) {
            button.setStyle("-fx-pref-width: 150px; -fx-pref-height: 45px; -fx-font-size: 16px; " +
                    "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-family: 'Arial';");
        } else {
            button.setStyle("-fx-pref-width: 150px; -fx-pref-height: 45px; -fx-font-size: 16px; " +
                    "-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-family: 'Arial';");
        }
    }

    private void updateDeleteButtonStyle(Button button, boolean isOpen) {
        if (isOpen) {
            button.setStyle("-fx-pref-width: 150px; -fx-pref-height: 45px; -fx-font-size: 16px; " +
                    "-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-family: 'Arial';");
        } else {
            button.setStyle("-fx-pref-width: 150px; -fx-pref-height: 45px; -fx-font-size: 16px; " +
                    "-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-family: 'Arial';");
        }
    }

    public void loadObjects() {
        if (excelService != null && objectsListView != null) {
            objectsListView.getItems().clear();
            objectStates.clear();

            java.util.List<StoreObject> objects = excelService.getAllStoreObjects();
            objectsListView.getItems().addAll(objects);

            if (countLabel != null) {
                countLabel.setText("Найдено объектов: " + objects.size());
            }
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            helloApp.showMainMenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleRefreshButton() {
        loadObjects();
    }
}
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
import ru.kofa.hacatonfintech.service.ExcelService;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectController implements Initializable {

    @Setter
    private ExcelService excelService;

    @Setter
    private HelloApplication helloApp;

    @FXML
    private ListView<StoreObject> objectsListView;

    @FXML
    private Label countLabel;

    // В методе initialize обновите cellFactory для полноэкранного вида:
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

                            // Название объекта
                            Label nameLabel = new Label(item.getName());
                            nameLabel.setPrefWidth(600);
                            nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ecf0f1; -fx-font-family: 'Arial';");

                            // Номер ячейки
                            Label cellLabel = new Label(item.getCellNumber());
                            cellLabel.setPrefWidth(300);
                            cellLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #bdc3c7; -fx-font-family: 'Arial';");

                            // Кнопка выбора
                            Button selectButton = new Button("ВЫБРАТЬ");
                            selectButton.setOnAction(e -> handleSelectObject(item));
                            selectButton.setStyle("-fx-pref-width: 150px; -fx-pref-height: 45px; -fx-font-size: 16px; -fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-family: 'Arial';");

                            HBox.setHgrow(nameLabel, Priority.ALWAYS);
                            hbox.getChildren().addAll(nameLabel, cellLabel, selectButton);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    public void loadObjects() {
        if (excelService != null && objectsListView != null) {
            objectsListView.getItems().clear();
            java.util.List<StoreObject> objects = excelService.getAllStoreObjects();
            objectsListView.getItems().addAll(objects);

            if (countLabel != null) {
                countLabel.setText("Найдено объектов: " + objects.size());
            }
        }
    }

    private void handleSelectObject(StoreObject object) {
        System.out.println("Выбран объект: " + object.getName() + " из ячейки " + object.getCellNumber());
    }

    @FXML
    private void handleBackButton() {
        try {
            helloApp.showMainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefreshButton() {
        loadObjects();
    }
}
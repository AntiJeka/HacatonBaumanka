package ru.kofa.hacatonfintech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.kofa.hacatonfintech.controller.AddObjectController;
import ru.kofa.hacatonfintech.controller.MenuController;
import ru.kofa.hacatonfintech.controller.SelectController;
import ru.kofa.hacatonfintech.service.ExcelService;

import java.io.IOException;

public class HelloApplication extends Application {
    private Stage primaryStage;
    private Scene mainScene;
    private ExcelService excelService;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.excelService = new ExcelService();

        // Настройка полноэкранного режима
        stage.setMaximized(true);
        stage.setTitle("СКЛАД - Система управления объектами");

        // Создаем основную сцену
        showMainMenu();
        stage.show();
    }

    public void showMainMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        MenuController controller = fxmlLoader.getController();
        controller.setHelloApp(this);

        if (mainScene == null) {
            mainScene = new Scene(root);
            primaryStage.setScene(mainScene);
        } else {
            mainScene.setRoot(root);
        }
    }

    public void showSelectScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("select-object.fxml"));
        Parent root = fxmlLoader.load();

        SelectController controller = fxmlLoader.getController();
        controller.setHelloApp(this);
        controller.setExcelService(excelService);
        controller.loadObjects();

        mainScene.setRoot(root);
    }

    public void showAddScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-object.fxml"));
        Parent root = fxmlLoader.load();

        AddObjectController controller = fxmlLoader.getController();
        controller.setHelloApp(this);
        controller.setExcelService(excelService);

        mainScene.setRoot(root);
    }
}
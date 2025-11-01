module ru.kofa.hacatonfintech {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires static lombok;

    opens ru.kofa.hacatonfintech to javafx.fxml;
    exports ru.kofa.hacatonfintech;
    exports ru.kofa.hacatonfintech.controller to javafx.fxml;
    opens ru.kofa.hacatonfintech.controller to javafx.fxml;
    exports ru.kofa.hacatonfintech.service;
    opens ru.kofa.hacatonfintech.service to javafx.fxml;
}
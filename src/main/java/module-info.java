module ru.kofa.hacatonfintech {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires static lombok;
    requires com.fazecast.jSerialComm;

    opens ru.kofa.hacatonfintech to javafx.fxml;
    exports ru.kofa.hacatonfintech;
    exports ru.kofa.hacatonfintech.controller to javafx.fxml;
    opens ru.kofa.hacatonfintech.controller to javafx.fxml;
    exports ru.kofa.hacatonfintech.service.excel;
    opens ru.kofa.hacatonfintech.service.excel to javafx.fxml;
    exports ru.kofa.hacatonfintech.service.arduino;
    opens ru.kofa.hacatonfintech.service.arduino to javafx.fxml;
}
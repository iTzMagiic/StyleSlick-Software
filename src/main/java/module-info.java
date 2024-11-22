module com.example.styleslick {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.example.styleslick to javafx.fxml;
    exports com.example.styleslick;
    exports com.example.styleslick.controller;
    opens com.example.styleslick.controller to javafx.fxml;
    exports com.example.styleslick.controller.customerManagement;
    opens com.example.styleslick.controller.customerManagement to javafx.fxml;
    opens com.example.styleslick.model to javafx.base;
}
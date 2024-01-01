module com.example.socialnetwork.java.ir.map {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.socialnetwork.java.ir.map.controllers to javafx.fxml;
    exports com.example.socialnetwork.java.ir.map.controllers;

    opens com.example.socialnetwork.java.ir.map.domain to javafx.fxml;
    exports com.example.socialnetwork.java.ir.map.domain;

    opens com.example.socialnetwork.java.ir.map to javafx.fxml;
    exports com.example.socialnetwork.java.ir.map;

}
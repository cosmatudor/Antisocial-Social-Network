module com.example.socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.socialnetwork.java.ir.map.controller to javafx.fxml;
    exports com.example.socialnetwork.java.ir.map.controller;

    opens com.example.socialnetwork to javafx.fxml;
    exports com.example.socialnetwork;

    opens com.example.socialnetwork.java.ir.map.domain to javafx.fxml;
    exports com.example.socialnetwork.java.ir.map.domain;


}
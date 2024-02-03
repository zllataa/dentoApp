module com.example.project {
    requires jbcrypt;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.project to javafx.fxml;
    exports com.example.project;
}
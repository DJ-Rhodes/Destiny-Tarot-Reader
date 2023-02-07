module com.example.tarot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.tarot to javafx.fxml;
    exports com.example.tarot;
    exports dbManagers;
    opens dbManagers to javafx.fxml;
}
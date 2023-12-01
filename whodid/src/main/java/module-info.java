module com.example.whodid {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql; // Add this line to include the java.sql module





    opens com.example.whodid to javafx.fxml;
    exports com.example.whodid;
}
module com.example.whodid {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.whodid to javafx.fxml;
    exports com.example.whodid;
}
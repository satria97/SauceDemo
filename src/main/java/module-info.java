module com.example.saucedemo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.saucedemo to javafx.fxml;
    exports com.example.saucedemo;
}
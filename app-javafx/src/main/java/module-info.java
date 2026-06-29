module com.gestionreposteria {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.gestionreposteria to javafx.fxml;
    opens com.gestionreposteria.controlador to javafx.fxml;

    opens com.gestionreposteria.modelo to javafx.base;

    exports com.gestionreposteria;
    exports com.gestionreposteria.controlador;
    exports com.gestionreposteria.modelo;
    exports com.gestionreposteria.dao;
}
package com.gestionreposteria.controlador;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void mostrarNuevoPedido() {
        cargarVista("nuevo-pedido.fxml");
    }

    @FXML
    private void mostrarAgenda() {
        cargarVista("agenda.fxml");
    }

    @FXML
    private void mostrarCatalogo() {
        cargarVista("catalogo.fxml");
    }

    @FXML
    private void mostrarHistorial() {
        cargarVista("historial.fxml");
    }

    // Reemplaza el contenido del panel central de manera dinámica
    private void cargarVista(String fxmlFile) {
        try {
            // Carga el recurso FXML especificado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestionreposteria/vista/" + fxmlFile));
            Parent vista = loader.load();

            // Limpia el contenedor y añade la nueva vista cargada
            contentArea.getChildren().setAll(vista);
        } catch (IOException e) {
            System.err.println("Error al cargar la vista modular: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
package com.gestionreposteria.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import com.gestionreposteria.modelo.PedidoAgenda;

public class AgendaController {

    @FXML
    private TableView<PedidoAgenda> tablaAgenda;
    @FXML
    private ComboBox<String> cbEstado;

    private ObservableList<PedidoAgenda> listaPedidos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        tablaAgenda.setItems(listaPedidos);
        cbEstado.getItems().addAll("Pendiente", "En producción", "Entregado");

        // Datos para poblar la tabla
        listaPedidos.addAll(
                new PedidoAgenda(1, LocalDate.now().toString(), "10:00", "Escribir 'Feliz Cumple'.", 37000.0,
                        "Pendiente"),
                new PedidoAgenda(2, LocalDate.now().plusDays(1).toString(), "15:30", "Sin TACC", 18000.0, "Pendiente"),
                new PedidoAgenda(3, LocalDate.now().plusDays(5).toString(), "12:00", "Detalle de flores", 30000.0,
                        "Pendiente"));

        // Resaltar pedidos urgentes
        tablaAgenda.setRowFactory(tv -> new TableRow<PedidoAgenda>() {
            @Override
            protected void updateItem(PedidoAgenda item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    LocalDate fechaPedido = LocalDate.parse(item.getFecha());
                    LocalDate hoy = LocalDate.now();

                    // Lógica de colores
                    if (fechaPedido.isEqual(hoy)) {
                        setStyle("-fx-background-color: #ffcccc;"); // Rojo claro para HOY
                    } else if (fechaPedido.isEqual(hoy.plusDays(1))) {
                        setStyle("-fx-background-color: #fff3cd;"); // Amarillo claro para MAÑANA
                    } else {
                        setStyle(""); // Blanco/Normal para el resto
                    }
                }
            }
        });
    }

    @FXML
    private void actualizarEstado() {
        PedidoAgenda seleccionado = tablaAgenda.getSelectionModel().getSelectedItem();
        String nuevoEstado = cbEstado.getValue();

        if (seleccionado != null && nuevoEstado != null) {
            // Actualizamos el objeto en memoria
            seleccionado.setEstado(nuevoEstado);
            tablaAgenda.refresh(); // Fuerza a la tabla a mostrar el nuevo texto

            // Cumplimiento parcial del caso de uso CP-005
            if (nuevoEstado.equals("Entregado")) {
                mostrarAlerta("Cobro y Entrega", "El pedido #" + seleccionado.getIdPedido()
                        + " está listo para ser entregado. Asegúrese de cobrar el saldo final.");
            }
        } else {
            mostrarAlerta("Atención", "Debe seleccionar un pedido de la tabla y elegir un nuevo estado del menú.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
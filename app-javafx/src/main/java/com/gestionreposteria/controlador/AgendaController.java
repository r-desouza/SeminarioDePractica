package com.gestionreposteria.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

import com.gestionreposteria.dao.AgendaDAO;
import com.gestionreposteria.modelo.PedidoAgenda;

public class AgendaController {

    @FXML
    private TableView<PedidoAgenda> tablaAgenda;
    @FXML
    private ComboBox<String> cbEstado;

    private ObservableList<PedidoAgenda> listaPedidos = FXCollections.observableArrayList();

    private AgendaDAO agendaDAO = new AgendaDAO();

    @FXML
    public void initialize() {
        tablaAgenda.setItems(listaPedidos);
        cbEstado.getItems().addAll("Pendiente", "En producción", "Entregado");

        cargarPedidosDeBD();

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

    private void cargarPedidosDeBD() {
        listaPedidos.clear();
        listaPedidos.addAll(agendaDAO.listarPedidos());
    }

    @FXML
    private void actualizarEstado() {
        PedidoAgenda seleccionado = tablaAgenda.getSelectionModel().getSelectedItem();
        String nuevoEstado = cbEstado.getValue();

        if (seleccionado != null && nuevoEstado != null) {
            // Guardar en Base de Datos
            if (agendaDAO.actualizarEstado(seleccionado.getIdPedido(), nuevoEstado)) {
                seleccionado.setEstado(nuevoEstado);
                tablaAgenda.refresh();

                if (nuevoEstado.equals("Entregado")) {
                    mostrarAlerta("Cobro y Entrega", "Pedido #" + seleccionado.getIdPedido() + " entregado.");
                }
            } else {
                mostrarAlerta("Error", "No se pudo actualizar la BD.");
            }
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
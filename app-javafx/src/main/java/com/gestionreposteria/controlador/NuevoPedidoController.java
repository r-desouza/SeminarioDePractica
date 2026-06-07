package com.gestionreposteria.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.gestionreposteria.modelo.DetallePedido;

public class NuevoPedidoController {

    @FXML
    private TextField txtNombreCliente;
    @FXML
    private TextField txtTelefonoCliente;
    @FXML
    private ComboBox<ProductoDemo> cbProductos;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TableView<DetallePedido> tablaPedido;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private Label lblTotal;

    // Lista observable para que la tabla se actualice sola al agregar items
    private ObservableList<DetallePedido> itemsPedido = FXCollections.observableArrayList();
    private double totalPresupuesto = 0.0;

    @FXML
    public void initialize() {
        // Inicializar la tabla
        tablaPedido.setItems(itemsPedido);

        // Simular productos traídos de la base de datos
        cbProductos.getItems().addAll(
                new ProductoDemo(1, "Tarta Cabsha", 15000.00),
                new ProductoDemo(2, "Torta Selva Negra", 22000.00));
    }

    @FXML
    private void agregarItem() {
        ProductoDemo prodSeleccionado = cbProductos.getValue();
        String cantStr = txtCantidad.getText();

        if (prodSeleccionado != null && !cantStr.isEmpty()) {
            try {
                int cantidad = Integer.parseInt(cantStr);
                double subtotal = prodSeleccionado.getPrecio() * cantidad;

                // Crear el detalle y agregarlo a la tabla
                DetallePedido nuevoDetalle = new DetallePedido(prodSeleccionado.getNombre(), cantidad, subtotal);
                itemsPedido.add(nuevoDetalle);

                // Actualizar el presupuesto total
                totalPresupuesto += subtotal;
                lblTotal.setText(String.format("$ %.2f", totalPresupuesto));

                // Limpiar campos para el siguiente ítem
                txtCantidad.clear();
                cbProductos.getSelectionModel().clearSelection();

            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "La cantidad debe ser un número entero.");
            }
        } else {
            mostrarAlerta("Atención", "Seleccione un producto e ingrese una cantidad.");
        }
    }

    @FXML
    private void guardarPedido() {
        // Aca irá la conexión a MySQL para guardar el pedido
        System.out.println("Guardando pedido para: " + txtNombreCliente.getText());
        System.out.println("Total a cobrar: $" + totalPresupuesto);
        System.out.println("Observaciones: " + txtObservaciones.getText());

        mostrarAlerta("Éxito", "Pedido registrado exitosamente (Simulación)");

        // Limpiar todo después de guardar
        itemsPedido.clear();
        totalPresupuesto = 0.0;
        lblTotal.setText("$ 0.00");
        txtNombreCliente.clear();
        txtTelefonoCliente.clear();
        txtObservaciones.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Clases temporal

    public static class ProductoDemo {
        private int id;
        private String nombre;
        private double precio;

        public ProductoDemo(int id, String nombre, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
        }

        public String getNombre() {
            return nombre;
        }

        public double getPrecio() {
            return precio;
        }

        @Override
        public String toString() {
            return nombre + " ($" + precio + ")";
        }
    }

}
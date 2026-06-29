package com.gestionreposteria.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.gestionreposteria.modelo.DetallePedido;
import com.gestionreposteria.modelo.ProductoCatalogo;
import com.gestionreposteria.modelo.Cliente;
import com.gestionreposteria.dao.ProductoDAO;
import com.gestionreposteria.dao.PedidoDAO;
import java.util.List;

public class NuevoPedidoController {

    @FXML
    private TextField txtNombreCliente;
    @FXML
    private TextField txtTelefonoCliente;
    @FXML
    private ComboBox<ProductoCatalogo> cbProductos; // Ahora usa el modelo real
    @FXML
    private TextField txtCantidad;
    @FXML
    private TableView<DetallePedido> tablaPedido;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private Label lblTotal;

    // Lista observable para que la tabla se actualice sola
    private ObservableList<DetallePedido> itemsPedido = FXCollections.observableArrayList();
    private double totalPresupuesto = 0.0;

    // Instancia los motores de base de datos
    private ProductoDAO productoDAO = new ProductoDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    @FXML
    public void initialize() {
        // Inicializa la tabla
        tablaPedido.setItems(itemsPedido);

        // Carga los productos directamente desde MySQL
        List<ProductoCatalogo> productosBD = productoDAO.listarActivos();
        cbProductos.getItems().addAll(productosBD);
    }

    @FXML
    private void agregarItem() {
        ProductoCatalogo prodSeleccionado = cbProductos.getValue();
        String cantStr = txtCantidad.getText();

        if (prodSeleccionado != null && !cantStr.isEmpty()) {
            try {
                int cantidad = Integer.parseInt(cantStr);
                double subtotal = prodSeleccionado.getPrecio() * cantidad;

                // Crea el detalle enviando el ID a MySQL y el Nombre a la pantalla
                DetallePedido nuevoDetalle = new DetallePedido(
                        prodSeleccionado.getId(),
                        prodSeleccionado.getNombre(),
                        cantidad,
                        subtotal);
                itemsPedido.add(nuevoDetalle);

                // Actualizar el presupuesto total
                totalPresupuesto += subtotal;
                lblTotal.setText(String.format("$ %.2f", totalPresupuesto));

                // Limpiar campos para el siguiente ítem
                txtCantidad.clear();
                cbProductos.getSelectionModel().clearSelection();

            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "La cantidad debe ser un número entero.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Atención", "Seleccione un producto e ingrese una cantidad.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void guardarPedido() {
        // Validaciones básicas
        if (txtNombreCliente.getText().isEmpty()) {
            mostrarAlerta("Datos incompletos", "Por favor, ingrese el nombre del cliente.", Alert.AlertType.WARNING);
            return;
        }
        if (itemsPedido.isEmpty()) {
            mostrarAlerta("Error", "Debe agregar al menos un producto al pedido.", Alert.AlertType.ERROR);
            return;
        }

        // Arma el objeto Cliente
        Cliente nuevoCliente = new Cliente(
                0, // MySQL generará el ID automáticamente
                txtNombreCliente.getText(),
                txtTelefonoCliente.getText(),
                "");

        // Ejecuta la Transacción SQL a través del DAO
        boolean guardadoExitoso = pedidoDAO.registrarPedidoCompleto(nuevoCliente, totalPresupuesto, itemsPedido);

        if (guardadoExitoso) {
            mostrarAlerta("Éxito", "El pedido ha sido registrado correctamente en la base de datos.",
                    Alert.AlertType.INFORMATION);

            // Limpiar todo después de guardar
            itemsPedido.clear();
            totalPresupuesto = 0.0;
            lblTotal.setText("$ 0.00");
            txtNombreCliente.clear();
            txtTelefonoCliente.clear();
            txtObservaciones.clear();
        } else {
            mostrarAlerta("Error de BD", "Ocurrió un problema al guardar el pedido en MySQL.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
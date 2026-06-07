package com.gestionreposteria.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.gestionreposteria.modelo.ProductoCatalogo;

public class CatalogoController {

    @FXML
    private TableView<ProductoCatalogo> tablaCatalogo;
    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<String> cbCategoria;
    @FXML
    private TextField txtPrecio;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private TextField txtDescripcion;

    private ObservableList<ProductoCatalogo> listaProductos = FXCollections.observableArrayList();
    private ProductoCatalogo productoSeleccionado = null; // Guarda el producto que estamos editando

    @FXML
    public void initialize() {
        // Inicializa opciones de los desplegables
        cbCategoria.getItems().addAll("Tartas Dulces", "Tortas Clásicas", "Tortas Temáticas", "Postres");
        cbEstado.getItems().addAll("Activo", "Pausado");

        // Llenar tabla con datos hardcodeados
        listaProductos.addAll(
                new ProductoCatalogo(1, "Tarta Cabsha", "Tartas Dulces",
                        "Masa sablée con dulce de leche y baño de chocolate", 15000.0),
                new ProductoCatalogo(2, "Torta Selva Negra", "Tortas Clásicas",
                        "Bizcochuelo de chocolate, crema y cerezas", 22000.0));
        tablaCatalogo.setItems(listaProductos);

        // Detectar cuando el usuario hace clic en una fila de la tabla
        tablaCatalogo.getSelectionModel().selectedItemProperty()
                .addListener((obs, viejoSeleccionado, nuevoSeleccionado) -> {
                    if (nuevoSeleccionado != null) {
                        productoSeleccionado = nuevoSeleccionado;
                        cargarDatosEnFormulario(nuevoSeleccionado);
                    }
                });
    }

    private void cargarDatosEnFormulario(ProductoCatalogo p) {
        txtNombre.setText(p.getNombre());
        cbCategoria.setValue(p.getCategoria());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        cbEstado.setValue(p.getEstado());
    }

    @FXML
    private void limpiarFormulario() {
        productoSeleccionado = null;
        tablaCatalogo.getSelectionModel().clearSelection();
        txtNombre.clear();
        cbCategoria.getSelectionModel().clearSelection();
        txtDescripcion.clear();
        txtPrecio.clear();
        cbEstado.getSelectionModel().clearSelection();
    }

    @FXML
    private void guardarProducto() {
        // Validaciones
        if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
            mostrarAlerta("Error", "Los campos Nombre y Precio son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        try {
            double precioNumerico = Double.parseDouble(txtPrecio.getText());

            if (productoSeleccionado == null) {
                // Es un producto NUEVO
                int nuevoId = listaProductos.size() + 1; // ID ficticio autoincremental
                ProductoCatalogo nuevoProd = new ProductoCatalogo(
                        nuevoId, txtNombre.getText(), cbCategoria.getValue(),
                        txtDescripcion.getText(), precioNumerico);
                if (cbEstado.getValue() != null)
                    nuevoProd.setEstado(cbEstado.getValue());

                listaProductos.add(nuevoProd);
                mostrarAlerta("Éxito", "Nuevo producto agregado al catálogo.", Alert.AlertType.INFORMATION);
            } else {
                // Es un producto existente
                productoSeleccionado.setNombre(txtNombre.getText());
                productoSeleccionado.setCategoria(cbCategoria.getValue());
                productoSeleccionado.setDescripcion(txtDescripcion.getText());
                productoSeleccionado.setPrecio(precioNumerico);
                if (cbEstado.getValue() != null)
                    productoSeleccionado.setEstado(cbEstado.getValue());

                tablaCatalogo.refresh(); // Actualiza la vista de la tabla
                mostrarAlerta("Éxito", "Producto actualizado correctamente.", Alert.AlertType.INFORMATION);
            }
            limpiarFormulario();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio debe ser un número válido (Ej: 15000.50).", Alert.AlertType.ERROR);
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
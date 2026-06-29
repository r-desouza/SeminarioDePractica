package com.gestionreposteria.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.gestionreposteria.modelo.ProductoCatalogo;
import com.gestionreposteria.util.GeneradorReporte;
import com.gestionreposteria.dao.ProductoDAO; // Importamos el DAO
import java.util.List;
import com.gestionreposteria.util.GeneradorReporte;

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
    private ProductoCatalogo productoSeleccionado = null;

    // Instanciamos el motor de base de datos
    private ProductoDAO productoDAO = new ProductoDAO();

    @FXML
    public void initialize() {
        cbCategoria.getItems().addAll(ProductoDAO.CATEGORIAS_PERMITIDAS);
        cbEstado.getItems().addAll("Activo", "Pausado");

        // Cargamos los datos reales desde la base de datos
        cargarDatosDesdeMySQL();

        // Detecta cuando el usuario hace clic en una fila de la tabla
        tablaCatalogo.getSelectionModel().selectedItemProperty()
                .addListener((obs, viejoSeleccionado, nuevoSeleccionado) -> {
                    if (nuevoSeleccionado != null) {
                        productoSeleccionado = nuevoSeleccionado;
                        cargarDatosEnFormulario(nuevoSeleccionado);
                    }
                });
    }

    // Trae los datos de MySQL y los inyecta en la tabla
    private void cargarDatosDesdeMySQL() {
        listaProductos.clear();
        List<ProductoCatalogo> productosBD = productoDAO.listarTodos(); // Consultamos a la BD
        listaProductos.addAll(productosBD); // Llenamos la lista observable
        tablaCatalogo.setItems(listaProductos); // Actualizamos la vista
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
            String categoria = cbCategoria.getValue() != null ? cbCategoria.getValue() : "Tortas";
            String estado = cbEstado.getValue() != null ? cbEstado.getValue() : "Activo";

            if (productoSeleccionado == null) {
                // Es un producto NUEVO
                // Pasamos 0 como ID porque MySQL se encarga del AUTO_INCREMENT
                ProductoCatalogo nuevoProd = new ProductoCatalogo(
                        0, txtNombre.getText(), categoria, txtDescripcion.getText(), precioNumerico);
                nuevoProd.setEstado(estado);

                // Guardamos en la Base de Datos
                if (productoDAO.registrar(nuevoProd)) {
                    mostrarAlerta("Éxito", "Nuevo producto guardado en la base de datos.", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "No se pudo guardar en la base de datos.", Alert.AlertType.ERROR);
                }

            } else {
                // Es un producto existente
                productoSeleccionado.setNombre(txtNombre.getText());
                productoSeleccionado.setCategoria(categoria);
                productoSeleccionado.setDescripcion(txtDescripcion.getText());
                productoSeleccionado.setPrecio(precioNumerico);
                productoSeleccionado.setEstado(estado);

                // Actualizamos en la Base de Datos
                if (productoDAO.actualizar(productoSeleccionado)) {
                    mostrarAlerta("Éxito", "Producto actualizado en la base de datos.", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar en la base de datos.", Alert.AlertType.ERROR);
                }
            }

            // Recargamos la tabla para que refleje lo que hay en la DB
            cargarDatosDesdeMySQL();
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

    @FXML
    private void exportarPrecios() {
        // Reutilizamos el DAO para obtener la lista más reciente de MySQL
        List<ProductoCatalogo> productosBD = productoDAO.listarTodos();

        // Llamamos al generador de archivos
        boolean exito = GeneradorReporte.exportarCatalogoTxt(productosBD);

        if (exito) {
            mostrarAlerta("Exportación Exitosa",
                    "El archivo 'Lista_Precios_Reposteria.txt' se ha generado correctamente en la carpeta del proyecto.",
                    Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo generar el archivo de reporte.", Alert.AlertType.ERROR);
        }
    }
}
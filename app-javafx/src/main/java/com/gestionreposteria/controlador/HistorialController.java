package com.gestionreposteria.controlador;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.util.Map;

import com.gestionreposteria.dao.HistorialDAO;
import com.gestionreposteria.util.GeneradorReporte;

public class HistorialController {

    @FXML
    private DatePicker dpDesde;
    @FXML
    private DatePicker dpHasta;
    @FXML
    private BarChart<String, Number> graficoVentas;
    @FXML
    private CategoryAxis ejeMeses;
    @FXML
    private NumberAxis ejeIngresos;

    private XYChart.Series<String, Number> serieVentas;

    // Instanciamos nuestro nuevo DAO
    private HistorialDAO historialDAO = new HistorialDAO();

    @FXML
    public void initialize() {
        // Por defecto, mostramos los últimos 3 meses hasta hoy
        dpDesde.setValue(LocalDate.now().withDayOfMonth(1).minusMonths(3));
        dpHasta.setValue(LocalDate.now());

        cargarGraficoReal();
    }

    // NUEVO MÉTODO: Carga los datos reales desde MySQL
    private void cargarGraficoReal() {
        if (dpDesde.getValue() == null || dpHasta.getValue() == null)
            return;

        graficoVentas.getData().clear();
        serieVentas = new XYChart.Series<>();
        serieVentas.setName("Ingresos Mensuales");

        // 1. Obtener datos reales
        Map<String, Double> datosBD = historialDAO.obtenerVentasPorMes(dpDesde.getValue(), dpHasta.getValue());

        // 2. Poblar el gráfico
        for (Map.Entry<String, Double> entry : datosBD.entrySet()) {
            serieVentas.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // 3. Añadir la serie al gráfico
        graficoVentas.getData().add(serieVentas);
    }

    @FXML
    private void filtrarDatos() {
        if (dpDesde.getValue() == null || dpHasta.getValue() == null) {
            mostrarAlerta("Atención", "Debe seleccionar ambas fechas para filtrar.");
            return;
        }

        // Al darle al botón "Filtrar", simplemente recargamos el gráfico
        cargarGraficoReal();
    }

    @FXML
    private void exportarReporte() {
        if (dpDesde.getValue() == null || dpHasta.getValue() == null) {
            mostrarAlerta("Atención", "Debe seleccionar ambas fechas antes de exportar.");
            return;
        }

        String fechaInicio = dpDesde.getValue().toString();
        String fechaFin = dpHasta.getValue().toString();

        String nombreArchivo = "Reporte_Ventas_" + fechaInicio + "_a_" + fechaFin + ".txt";

        GeneradorReporte generador = new GeneradorReporte();
        generador.exportarHistorialATxt(nombreArchivo, fechaInicio, fechaFin, serieVentas);

        mostrarAlerta("Reporte Generado",
                "El historial se ha exportado exitosamente como '" + nombreArchivo + "'.");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
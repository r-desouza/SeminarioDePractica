package com.gestionreposteria.controlador;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;

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

    @FXML
    public void initialize() {
        // Inicializar los DatePickers con el mes actual por defecto
        dpDesde.setValue(LocalDate.now().withDayOfMonth(1));
        dpHasta.setValue(LocalDate.now());

        cargarGraficoFicticio();
    }

    private void cargarGraficoFicticio() {
        // Limpiamos el gráfico por si hay datos previos
        graficoVentas.getData().clear();

        // Creamos una "Serie" de datos para el gráfico de barras
        XYChart.Series<String, Number> serieVentas = new XYChart.Series<>();
        serieVentas.setName("Ingresos Mensuales");

        // Agregamos puntos de datos ficticios para poblar el gráfico
        serieVentas.getData().add(new XYChart.Data<>("Marzo", 185000));
        serieVentas.getData().add(new XYChart.Data<>("Abril", 240000));
        serieVentas.getData().add(new XYChart.Data<>("Mayo", 310500));
        serieVentas.getData().add(new XYChart.Data<>("Junio", 125000));
        // Inyectamos la serie al gráfico
        graficoVentas.getData().add(serieVentas);
    }

    @FXML
    private void filtrarDatos() {
        if (dpDesde.getValue() == null || dpHasta.getValue() == null) {
            mostrarAlerta("Atención", "Debe seleccionar ambas fechas para filtrar.");
            return;
        }

        String fechaInicio = dpDesde.getValue().toString();
        String fechaFin = dpHasta.getValue().toString();

        mostrarAlerta("Filtro Aplicado",
                "Simulando carga de base de datos desde " + fechaInicio + " hasta " + fechaFin + ".");
        // Usar queries en un futuro real para obtener datos filtrados y actualizar el
        // gráfico dinámicamente
    }

    @FXML
    private void exportarReporte() {
        // Simulación para Exportar historial a un formato de lectura (.txt)
        System.out.println("--- GENERANDO REPORTE DE VENTAS ---");
        System.out.println("Procesando datos para exportación...");

        mostrarAlerta("Reporte Generado",
                "El historial de ventas se ha exportado exitosamente como 'Reporte_Ventas_Mensual.txt' en la carpeta Documentos.");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
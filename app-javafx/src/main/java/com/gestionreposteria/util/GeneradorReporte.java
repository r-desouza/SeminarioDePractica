package com.gestionreposteria.util;

import com.gestionreposteria.modelo.ProductoCatalogo;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneradorReporte {

    public static boolean exportarCatalogoTxt(List<ProductoCatalogo> listaProductos) {
        String rutaArchivo = "Lista_Precios_Reposteria.txt";

        // Manejo de Archivos y Excepciones de I/O
        try (FileWriter fw = new FileWriter(rutaArchivo);
                PrintWriter pw = new PrintWriter(fw)) {

            // Encabezado del archivo
            pw.println("==============================================================");
            pw.println("   LISTA DE PRECIOS - REPOSTERÍA ARTESANAL ROSSANA BRITOS");
            pw.println("==============================================================");

            // fecha y hora de generación
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            pw.println("Generado el: " + fecha);
            pw.println("");

            // Iteramos sobre el ArrayList
            for (ProductoCatalogo p : listaProductos) {
                // Solo los que están activos
                if ("Activo".equals(p.getEstado())) {
                    pw.println("- " + p.getNombre() + " (" + p.getCategoria() + ") ............ $" + p.getPrecio());
                }
            }

            pw.println("\n==============================================================");
            pw.println("Fin del reporte.");

            return true;

        } catch (IOException e) {
            System.err.println("Error al intentar escribir el archivo de texto.");
            e.printStackTrace();
            return false;
        }
    }

    public void exportarHistorialATxt(String nombreArchivo, String fechaDesde, String fechaHasta,
            javafx.scene.chart.XYChart.Series<String, Number> serieVentas) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(nombreArchivo))) {

            writer.write("--- REPORTE DE HISTORIAL DE VENTAS ---");
            writer.newLine();
            writer.write("Período: " + fechaDesde + " al " + fechaHasta);
            writer.newLine();
            writer.write("--------------------------------------");
            writer.newLine();

            double totalIngresos = 0.0;

            // Recorremos los datos que están actualmente en el gráfico
            for (javafx.scene.chart.XYChart.Data<String, Number> dato : serieVentas.getData()) {
                writer.write("Período: " + dato.getXValue() + " | Ingresos: $" + dato.getYValue());
                writer.newLine();
                totalIngresos += dato.getYValue().doubleValue();
            }

            writer.write("--------------------------------------");
            writer.newLine();
            writer.write("TOTAL ACUMULADO DEL PERÍODO: $" + totalIngresos);

            System.out.println("Reporte de historial generado exitosamente: " + nombreArchivo);

        } catch (java.io.IOException e) {
            System.err.println("Error al escribir el archivo de historial: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
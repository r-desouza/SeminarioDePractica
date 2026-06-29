package com.gestionreposteria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class HistorialDAO {

    // Devuelve un Map donde la Clave es el mes y el Valor es el total
    public Map<String, Double> obtenerVentasPorMes(LocalDate fechaInicio, LocalDate fechaFin) {

        // Usamos LinkedHashMap para mantener el orden cronológico
        Map<String, Double> ventasPorMes = new LinkedHashMap<>();

        // Agrupa por Año-Mes y suma el total del presupuesto
        String sql = "SELECT DATE_FORMAT(fecha_entrega, '%Y-%m') AS mes, SUM(total_presupuesto) AS total " +
                "FROM Pedido " +
                "WHERE fecha_entrega BETWEEN ? AND ? " +
                "GROUP BY DATE_FORMAT(fecha_entrega, '%Y-%m') " +
                "ORDER BY mes ASC";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            // Convertimos LocalDate de Java a Date de SQL
            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String mes = rs.getString("mes");
                    double total = rs.getDouble("total");
                    ventasPorMes.put(mes, total);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar el historial de ventas.");
            e.printStackTrace();
        }

        return ventasPorMes;
    }
}
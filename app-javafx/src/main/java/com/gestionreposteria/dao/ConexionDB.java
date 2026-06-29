package com.gestionreposteria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    // Configuración estándar para un servidor XAMPP local
    private static final String URL = "jdbc:mysql://localhost:3306/reposteriabd";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public static Connection getConexion() {
        Connection conexion = null;
        try {
            // Intenta establecer el puente con MySQL
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión exitosa a reposteriabd.");
        } catch (SQLException e) {
            System.err.println("Error: No se pudo conectar a la base de datos.");
            System.err.println("Asegurate de que XAMPP esté encendido y MySQL corriendo.");
            e.printStackTrace();
        }
        return conexion;
    }
}

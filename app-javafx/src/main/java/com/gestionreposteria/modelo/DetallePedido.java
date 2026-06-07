package com.gestionreposteria.modelo;

public class DetallePedido {
    private String nombreProducto;
    private int cantidad;
    private double subtotal;

    public DetallePedido(String nombreProducto, int cantidad, double subtotal) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    // --- Getters (Requeridos por el PropertyValueFactory de la Tabla) ---
    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
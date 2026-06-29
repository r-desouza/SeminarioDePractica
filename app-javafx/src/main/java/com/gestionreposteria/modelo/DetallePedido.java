package com.gestionreposteria.modelo;

public class DetallePedido {
    private int idProducto;
    private String nombreProducto;
    private int cantidad;
    private double subtotal;

    public DetallePedido(int idProducto, String nombreProducto, int cantidad, double subtotal) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getIdProducto() {
        return idProducto;
    }

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
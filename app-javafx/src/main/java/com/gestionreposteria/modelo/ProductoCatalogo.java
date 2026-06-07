package com.gestionreposteria.modelo;

public class ProductoCatalogo {
    private int id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private double precio;
    private String estado;

    public ProductoCatalogo(int id, String nombre, String categoria, String descripcion, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precio = precio;
        this.estado = "Activo";
    }

    // --- Getters (Requeridos por la Tabla) ---
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }

    // --- Setters (Requeridos para editar) ---
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
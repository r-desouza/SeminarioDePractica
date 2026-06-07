package com.gestionreposteria.modelo;

public class PedidoAgenda {
    private int idPedido;
    private String fecha;
    private String hora;
    private String observaciones;
    private double total;
    private String estado;

    public PedidoAgenda(int idPedido, String fecha, String hora, String observaciones, double total, String estado) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.hora = hora;
        this.observaciones = observaciones;
        this.total = total;
        this.estado = estado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
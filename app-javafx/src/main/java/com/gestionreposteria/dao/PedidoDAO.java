package com.gestionreposteria.dao;

import com.gestionreposteria.modelo.Cliente;
import com.gestionreposteria.modelo.DetallePedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PedidoDAO {

    public boolean registrarPedidoCompleto(Cliente cliente, double totalPresupuesto, List<DetallePedido> detalles) {
        Connection con = null;

        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            // 1. GUARDAR CLIENTE
            String sqlCliente = "INSERT INTO Cliente (nombre, telefono) VALUES (?, ?)";
            PreparedStatement psCliente = con.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS);
            psCliente.setString(1, cliente.getNombre());
            psCliente.setString(2, cliente.getTelefono());
            psCliente.executeUpdate();

            ResultSet rsCliente = psCliente.getGeneratedKeys();
            int idCliente = 0;
            if (rsCliente.next()) {
                idCliente = rsCliente.getInt(1);
            }

            // 2. GUARDAR PEDIDO
            String sqlPedido = "INSERT INTO Pedido (id_cliente, fecha_entrega, total_presupuesto, estado) VALUES (?, CURDATE(), ?, 'Pendiente')";
            PreparedStatement psPedido = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, idCliente);
            psPedido.setDouble(2, totalPresupuesto);
            psPedido.executeUpdate();

            ResultSet rsPedido = psPedido.getGeneratedKeys();
            int idPedido = 0;
            if (rsPedido.next()) {
                idPedido = rsPedido.getInt(1);
            }

            // 3. GUARDAR DETALLES DEL PEDIDO
            String sqlDetalle = "INSERT INTO Detalle_Pedido (id_pedido, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);

            for (DetallePedido detalle : detalles) {
                psDetalle.setInt(1, idPedido);
                psDetalle.setInt(2, detalle.getIdProducto());
                psDetalle.setInt(3, detalle.getCantidad());
                psDetalle.setDouble(4, detalle.getSubtotal());
                psDetalle.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al guardar el pedido. Revirtiendo cambios...");
            e.printStackTrace();
            try {
                if (con != null)
                    con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (con != null)
                    con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
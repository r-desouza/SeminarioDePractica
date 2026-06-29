package com.gestionreposteria.dao;

import com.gestionreposteria.modelo.PedidoAgenda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgendaDAO {

    public List<PedidoAgenda> listarPedidos() {
        List<PedidoAgenda> lista = new ArrayList<>();
        // Unimos Pedido con Cliente para obtener datos si fuera necesario
        String sql = "SELECT id_pedido, fecha_entrega, total_presupuesto, estado FROM Pedido ORDER BY fecha_entrega ASC";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new PedidoAgenda(
                        rs.getInt("id_pedido"),
                        rs.getString("fecha_entrega"),
                        "12:00", // Valor fijo por ahora ya que no hay columna hora
                        "Observaciones", // Placeholder
                        rs.getDouble("total_presupuesto"),
                        rs.getString("estado")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        String sql = "UPDATE Pedido SET estado = ? WHERE id_pedido = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
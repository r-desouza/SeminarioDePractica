package com.gestionreposteria.dao;

import com.gestionreposteria.modelo.ProductoCatalogo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements IDAO<ProductoCatalogo> {

    // Uso de Arreglo (Array estático)
    public static final String[] CATEGORIAS_PERMITIDAS = { "Tortas", "Tartas", "Postres", "Especiales" };

    @Override
    public List<ProductoCatalogo> listarTodos() {
        // Uso de ArrayList (Estructura dinámica)
        List<ProductoCatalogo> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM Producto";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductoCatalogo producto = new ProductoCatalogo(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"));
                producto.setEstado(rs.getString("estado"));

                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar el catálogo de productos.");
            e.printStackTrace();
        }

        return listaProductos;
    }

    public List<ProductoCatalogo> listarActivos() {
        List<ProductoCatalogo> listaActivos = new ArrayList<>();
        String sql = "SELECT * FROM Producto WHERE estado = 'Activo'";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductoCatalogo producto = new ProductoCatalogo(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"));
                producto.setEstado(rs.getString("estado"));
                listaActivos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar productos activos.");
            e.printStackTrace();
        }
        return listaActivos;
    }

    @Override
    public boolean registrar(ProductoCatalogo producto) {
        String sql = "INSERT INTO Producto (nombre, categoria, descripcion, precio, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getCategoria());
            ps.setString(3, producto.getDescripcion());
            ps.setDouble(4, producto.getPrecio());
            ps.setString(5, producto.getEstado());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar el producto en la base de datos.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(ProductoCatalogo producto) {
        String sql = "UPDATE Producto SET precio = ?, categoria = ?, descripcion = ?, estado = ? WHERE id_producto = ?";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, producto.getPrecio());
            ps.setString(2, producto.getCategoria());
            ps.setString(3, producto.getDescripcion());

            // 2. Agregamos el estado en la posición 4
            ps.setString(4, producto.getEstado());

            // 3. Ajustamos el ID a la posición 5
            ps.setInt(5, producto.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "UPDATE Producto SET estado = 'Pausado' WHERE id_producto = ?";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al intentar pausar el producto.");
            e.printStackTrace();
            return false;
        }
    }
}
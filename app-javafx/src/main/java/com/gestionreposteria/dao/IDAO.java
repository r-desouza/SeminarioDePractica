package com.gestionreposteria.dao;

import java.util.List;

/**
 * Interface genérica para estandarizar las operaciones CRUD.
 * La 'T' representa cualquier clase del Modelo (ProductoCatalogo, Cliente,
 * etc.)
 */
public interface IDAO<T> {

    boolean registrar(T objeto);

    List<T> listarTodos();

    boolean actualizar(T objeto);

    boolean eliminar(int id);

}

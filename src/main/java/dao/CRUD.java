package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CRUD<T> {
    T save(T entity) throws SQLException;

    List<T> findAll() throws SQLException;

    Optional<T> findById(Long id) throws SQLException;

    T update(T entity) throws SQLException;

    boolean delete(Long id) throws SQLException;
}

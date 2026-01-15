package jdbc.introduction.repository;

import jdbc.introduction.model.Employee;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    List<T> getAll() throws SQLException;

    Optional<T> getById(Object id) throws SQLException;

    void save(T data);

    void delete(Object id);
}

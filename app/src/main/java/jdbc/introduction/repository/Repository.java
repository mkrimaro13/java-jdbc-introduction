package jdbc.introduction.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    List<T> getAll() throws Exception;

    Optional<T> getById(Object id) throws Exception;

    void save(T data) throws Exception;

    void delete(int id) throws Exception;
}

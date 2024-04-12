package dubovikLera.dao;


import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    boolean update(E entity);
    List<E> findAll();
    Optional<E> findById(K id);
    E save(E entity);
    boolean delete(K id);

}

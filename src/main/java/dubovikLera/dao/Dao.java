package dubovikLera.dao;


import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    boolean update(E ticket);
    List<E> findAll();
    Optional<E> findById(K id);
    E save(E ticket);
    boolean delete(K id);

}

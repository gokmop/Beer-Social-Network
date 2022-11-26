package beertag.services.contracts.generic;


import java.util.List;
import java.util.Optional;

public interface GetService<T> {

    T getById(int id);

    List<T> getAll(Optional<Integer> desc, Optional<String> sortParameter);

    List<T> getAll();
}

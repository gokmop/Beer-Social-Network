package beertag.repositories.contracts.generic;

import java.util.List;

public interface GetRepo<T> {

    List<T> getAll(int desc, String sortParameter);

    List<T> getAll();

    T getById(int id);
}

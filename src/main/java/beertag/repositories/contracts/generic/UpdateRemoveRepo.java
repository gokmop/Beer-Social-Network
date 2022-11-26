package beertag.repositories.contracts.generic;

public interface UpdateRemoveRepo<T> {

    void remove(T t);

    void update(T t);
}

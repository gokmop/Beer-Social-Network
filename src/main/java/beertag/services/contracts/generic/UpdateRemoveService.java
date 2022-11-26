package beertag.services.contracts.generic;

import beertag.models.User;

public interface UpdateRemoveService<T> {

    void remove(int id, User authorise);

    void update(T t, User authorise);
}

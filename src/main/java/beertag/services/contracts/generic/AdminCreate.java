package beertag.services.contracts.generic;

import beertag.models.User;

public interface AdminCreate<T> {

    void create(T t, User isAdmin);

}

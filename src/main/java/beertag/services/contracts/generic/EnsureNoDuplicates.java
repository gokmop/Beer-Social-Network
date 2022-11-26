package beertag.services.contracts.generic;

import beertag.exception.DuplicateEntityException;

public interface EnsureNoDuplicates<T> {

    void ensureNoDuplicates(T t, String message) throws DuplicateEntityException;
}

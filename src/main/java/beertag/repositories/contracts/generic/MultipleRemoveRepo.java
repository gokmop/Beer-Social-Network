package beertag.repositories.contracts.generic;

import java.util.Collection;

public interface MultipleRemoveRepo<T> {

    void multipleRemove(Collection<T> entities);

}

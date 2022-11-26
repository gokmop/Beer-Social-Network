package beertag.services.contracts.generic;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SearchService<T> {

    List<T> search(Map<String, String> searchParams);

}

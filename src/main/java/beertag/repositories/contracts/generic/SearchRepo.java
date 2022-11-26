package beertag.repositories.contracts.generic;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SearchRepo<T> {

    List<T> search(Map<String, String> searchParams);
}

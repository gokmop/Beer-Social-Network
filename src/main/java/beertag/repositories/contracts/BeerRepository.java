package beertag.repositories.contracts;

import beertag.models.*;
import beertag.repositories.contracts.generic.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface BeerRepository extends GetRepo<Beer>, CreateRepo<Beer>, SearchRepo<Beer>,
        CheckExists<Beer>, MultipleRemoveRepo<Beer>, UpdateRemoveRepo<Beer> {

    List<Beer> getUserBeerList(UserDetails userDetails, String wishOrDrankList,
                               int desc, String sortParameter, int limit);

    List<Beer> getUserBeerListSearch(Map<String, String> searchParams, Collection<Beer> userBeers);

    List<Beer> ListAllBeers();

    List<Beer> getTop5Beers();

}

package beertag.services.contracts;


import beertag.models.Beer;
import beertag.models.Tag;
import beertag.models.UserDetails;
import beertag.repositories.contracts.generic.CheckExists;
import beertag.services.contracts.generic.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BeerService extends GetService<Beer>, UpdateRemoveService<Beer>, SearchService<Beer>,
        UserCreate<Beer>, EnsureNoDuplicates<Beer>, CheckExists<Beer> {

    void tagBeer(Beer beer, Tag tag);

    void unTagBeer(Beer beer, Tag tag);

    List<Beer> ListAllBeers();

    List<Beer> getTop5Beers();

    List<Beer> getUserBeerListSearch(Map<String, String> searchParams,
                                     Collection<Beer> userBeers);

    List<Beer> getList(UserDetails userDetails, String wishOrDrankList,
                       Optional<Integer> desc, Optional<String> sortParameter, int limit);
}

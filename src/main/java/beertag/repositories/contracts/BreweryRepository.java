package beertag.repositories.contracts;

import beertag.models.Beer;
import beertag.models.Brewery;
import beertag.repositories.contracts.generic.*;

import java.util.Collection;
import java.util.List;

public interface BreweryRepository extends GetRepo<Brewery>, CreateRepo<Brewery>,
        SearchRepo<Brewery>, CheckExists<Brewery>, UpdateRemoveRepo<Brewery> {

    boolean checkBreweryEmailExists(Brewery brewery, int currentId);

    List<Brewery> getTop5Breweries();

    List<Brewery> getBeerListBreweries(Collection<Beer> userBeers);

}

package beertag.services.contracts;

import beertag.models.Beer;
import beertag.models.Brewery;
import beertag.services.contracts.generic.*;

import java.util.Collection;
import java.util.List;

public interface BreweryService extends GetService<Brewery>, UpdateRemoveService<Brewery>,
        SearchService<Brewery>, EnsureNoDuplicates<Brewery>, AdminCreate<Brewery> {

    List<Brewery> getTop5Breweries();

    List<Brewery> getBeerListBreweries(Collection<Beer> userBeers);
}

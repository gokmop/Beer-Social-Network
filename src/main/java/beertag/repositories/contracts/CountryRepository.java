package beertag.repositories.contracts;

import beertag.models.Beer;
import beertag.models.Country;
import beertag.repositories.contracts.generic.GetRepo;

import java.util.Collection;
import java.util.List;

public interface CountryRepository extends GetRepo<Country> {
    List<Country> getBeerListCountries(Collection<Beer> userBeers);
}

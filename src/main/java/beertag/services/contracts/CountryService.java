package beertag.services.contracts;

import beertag.models.Beer;
import beertag.models.Country;
import beertag.services.contracts.generic.GetService;

import java.util.Collection;
import java.util.List;

public interface CountryService extends GetService<Country> {
    List<Country> getBeerListCountries(Collection<Beer> userBeers);
}

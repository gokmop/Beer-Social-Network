package beertag.services;

import beertag.models.Beer;
import beertag.models.Country;
import beertag.repositories.contracts.CountryRepository;
import beertag.services.contracts.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static beertag.Constants.SORT_DEFAULT;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAll(Optional<Integer> desc, Optional<String> sortParameter) {
        return countryRepository.getAll(desc.orElse(Integer.SIZE), sortParameter.orElse(SORT_DEFAULT));
    }

    @Override
    public List<Country> getBeerListCountries(Collection<Beer> userBeers) {
        return countryRepository.getBeerListCountries(userBeers);
    }

    @Override
    public Country getById(int id) {
        return countryRepository.getById(id);
    }

    @Override
    public List<Country> getAll() {
        return countryRepository.getAll();
    }
}

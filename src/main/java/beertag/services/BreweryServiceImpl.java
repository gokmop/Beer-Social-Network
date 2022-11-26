package beertag.services;

import beertag.exception.DuplicateEntityException;
import beertag.models.Beer;
import beertag.models.Brewery;
import beertag.models.User;
import beertag.repositories.contracts.BeerRepository;
import beertag.repositories.contracts.BreweryRepository;
import beertag.services.contracts.BreweryService;
import beertag.services.contracts.AuthorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static beertag.Constants.*;
import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.BreweryConstants.*;

@Service
public class BreweryServiceImpl implements BreweryService {

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final AuthorisationService authorisationService;

    @Autowired
    public BreweryServiceImpl(BreweryRepository breweryRepository, BeerRepository beerRepository,
                              AuthorisationService authorisationService) {
        this.breweryRepository = breweryRepository;
        this.beerRepository = beerRepository;
        this.authorisationService = authorisationService;
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void create(Brewery brewery, User authorise) {
        authorisationService.authorise(authorise, String.format(ONLY_ADMINS_CAN_S_S, CREATE, BREWERIES));
        ensureEmailNotTaken(brewery, CREATE_ERROR);
        ensureNoDuplicates(brewery, BREWERY_DUPLICATE_CREATE_ERROR);
        breweryRepository.create(brewery);
    }

    @Override
    public Brewery getById(int id) {
        return breweryRepository.getById(id);
    }

    @Override
    public void remove(int breweryId, User authorise) {
        authorisationService.authorise(authorise, String.format(ONLY_ADMINS_CAN_S_S, REMOVE, BREWERIES));
        Brewery breweryToRemove = getById(breweryId);
        beerRepository.multipleRemove(breweryToRemove.getMadeByThisBeers());
        breweryRepository.remove(breweryToRemove);
    }

    @Override
    public void update(Brewery brewery, User authorise) {
        authorisationService.authorise(authorise, String.format(ONLY_ADMINS_CAN_S_S, UPDATE, BREWERIES));
        ensureEmailNotTaken(brewery, UPDATE_ERROR);
        ensureNoDuplicates(brewery, BREWERY_DUPLICATE_UPDATE_ERROR);
        breweryRepository.update(brewery);
    }

    @Override
    public List<Brewery> getAll(Optional<Integer> desc, Optional<String> sortParameter) {
        return breweryRepository.getAll(desc.orElse(Integer.SIZE), sortParameter.orElse(SORT_DEFAULT));
    }

    @Override
    public List<Brewery> search(Map<String, String> searchParams) {
        return breweryRepository.search(searchParams);
    }

    @Override
    public void ensureNoDuplicates(Brewery brewery, String message) throws DuplicateEntityException {
        if (breweryRepository.checkExists(brewery)) {
            throw new DuplicateEntityException(message);
        }
    }

    private void ensureEmailNotTaken(Brewery brewery, String message) {
        if (breweryRepository.checkBreweryEmailExists(brewery, brewery.getId())) {
            throw new DuplicateEntityException(message.concat(DUPLICATE_BREWERY_EMAIL_ERROR));
        }
    }

    @Override
    public List<Brewery> getAll() {
        return breweryRepository.getAll();
    }

    @Override
    public List<Brewery> getTop5Breweries() {
        return breweryRepository.getTop5Breweries();

    }

    @Override
    public List<Brewery> getBeerListBreweries(Collection<Beer> userBeers) {
        return breweryRepository.getBeerListBreweries(userBeers);
    }
}

package beertag.services;

import beertag.exception.DuplicateEntityException;
import beertag.models.Beer;
import beertag.models.Tag;
import beertag.models.User;
import beertag.models.UserDetails;
import beertag.repositories.contracts.BeerRepository;
import beertag.services.contracts.BeerService;
import beertag.services.contracts.AuthorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.BeerConstants.BEER_DUPLICATE_CREATE_ERROR;
import static beertag.Constants.BeerConstants.BEER_DUPLICATE_UPDATE_ERROR;
import static beertag.Constants.HqlQueriesConstants.ID;
import static beertag.Constants.SORT_DEFAULT;

@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    //    private final BeerRepositoryJPA beerRepositoryJpa;
    private final AuthorisationService authorisationService;

    @Autowired
    public BeerServiceImpl(BeerRepository beerRepository, AuthorisationService authorisationService) {
        this.beerRepository = beerRepository;
//        this.beerRepositoryJpa = beerRepositoryJpa;
        this.authorisationService = authorisationService;
    }

    @Override
    public void create(Beer beer) {
        ensureNoDuplicates(beer, BEER_DUPLICATE_CREATE_ERROR);
        beerRepository.create(beer);
    }

    @Override
    public Beer getById(int id) {
        return beerRepository.getById(id);
    }

    @Override
    public void remove(int beerId, User authorise) {
        Beer beerToRemove = beerRepository.getById(beerId);
        authorisationService.authorise(beerToRemove, authorise, String
                .format(ONLY_AUTHORS_AND_ADMINS_CAN_S_ERROR, REMOVE));
        beerRepository.remove(beerToRemove);
    }

    @Override
    public void update(Beer beer, User authorise) {
        authorisationService.authorise(beer, authorise, String
                .format(ONLY_AUTHORS_AND_ADMINS_CAN_S_ERROR, UPDATE));
        ensureNoDuplicates(beer, BEER_DUPLICATE_UPDATE_ERROR);
        beerRepository.update(beer);
//        beerRepositoryJpa.save(beer);
    }

    @Override
    public List<Beer> getAll(Optional<Integer> desc, Optional<String> sortParameter) {
        List<Beer> beers = beerRepository.getAll(desc.orElse(Integer.SIZE),
                sortParameter.orElse(SORT_DEFAULT));
        //Sort sort = new Sort();
//        List<Beer> beers = beerRepositoryJpa.findAll();
        return beers;
    }

    @Override
    public List<Beer> ListAllBeers() {
        return beerRepository.ListAllBeers();
    }

    @Override
    public List<Beer> getTop5Beers() {
        return beerRepository.getTop5Beers();
    }

    @Override
    public boolean checkExists(Beer beer) {
        return beerRepository.checkExists(beer);
    }

    @Override
    public List<Beer> search(Map<String, String> searchParams) {
        return beerRepository.search(searchParams);
    }

    @Override
    public void ensureNoDuplicates(Beer beer, String message) throws DuplicateEntityException {
        if (beerRepository.checkExists(beer)) {
            throw new DuplicateEntityException(message);
        }
    }

    @Override
    public void tagBeer(Beer beer, Tag tag) {
        beer.addTag(tag);
        beerRepository.update(beer);
    }

    @Override
    public void unTagBeer(Beer beer, Tag tag) {
        beer.removeTag(tag);
        beerRepository.update(beer);
    }

    @Override
    public List<Beer> getList(UserDetails userDetails, String wishOrDrankList, Optional<Integer> desc,
                              Optional<String> sortParameter, int limit) {
        return beerRepository.getUserBeerList(userDetails, wishOrDrankList, desc.orElse(Integer.SIZE), sortParameter.orElse(ID), limit);
    }

    @Override
    public List<Beer> getAll() {
        return beerRepository.getAll();
    }

    @Override
    public List<Beer> getUserBeerListSearch(Map<String, String> searchParams,
                                            Collection<Beer> userBeers) {
        return beerRepository.getUserBeerListSearch(searchParams, userBeers);
    }
}

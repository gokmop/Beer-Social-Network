package beertag.repositories;

import beertag.models.Beer;
import beertag.models.Brewery;
import beertag.models.Country;
import beertag.repositories.contracts.CountryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static beertag.Constants.HqlQueriesConstants.ID;
import static beertag.Constants.HqlQueriesConstants.NAME;
import static beertag.Utility.*;

@Repository
public class CountryRepositoryHibernate implements CountryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CountryRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Country> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return getAllEntities(Country.class, session);
        }
    }

    @Override
    public List<Country> getAll(int desc, String sortParameter) {
        if (sortParameter.equals(NAME)) {
            sortParameter = NAME;
        } else {
            sortParameter = ID;
        }
        try (Session session = sessionFactory.openSession()) {
            return getAllEntitiesSorted(desc, sortParameter, session, Country.class);
        }
    }

    @Override
    public List<Country> getBeerListCountries(Collection<Beer> userBeers) {
        return userBeers.stream().map(Beer::getBrewery)
                .map(Brewery::getCountry).distinct().collect(Collectors.toList());
    }

    @Override
    public Country getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return getEntity(id, session, Country.class);
        }
    }
}

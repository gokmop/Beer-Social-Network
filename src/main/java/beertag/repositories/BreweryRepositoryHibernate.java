package beertag.repositories;

import beertag.models.Beer;
import beertag.models.Brewery;
import beertag.repositories.contracts.BreweryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Utility.*;
import static java.util.Arrays.asList;

@Repository
public class BreweryRepositoryHibernate implements BreweryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BreweryRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Brewery> search(Map<String, String> searchParams) {
        boolean descending = searchParams.get(DESC).equals(DESC);
        String sortParameterSql = getSortParamSql(searchParams.get(SORT_PARAMETER));
        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Brewery> criteriaQuery = criteriaBuilder.createQuery(Brewery.class);
            Root<Brewery> root = criteriaQuery.from(Brewery.class);

            List<Predicate> predicates = getPredicates(searchParams, criteriaBuilder, root);

            sortQuery(descending, sortParameterSql, criteriaBuilder, criteriaQuery,
                    root, predicates);

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public void create(Brewery brewery) {
        try (Session session = sessionFactory.openSession()) {
            session.save(brewery);
        }
    }

    @Override
    public Brewery getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return getEntity(id, session, Brewery.class);
        }
    }

    @Override
    public void remove(Brewery brewery) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(brewery);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Brewery brewery) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(brewery);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Brewery> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return getAllEntities(Brewery.class, session);
        }
    }

    @Override
    public List<Brewery> getAll(int desc, String sortParameter) {
        String sortParameterSql = getSortParamSql(sortParameter);
        try (Session session = sessionFactory.openSession()) {
            return getAllEntitiesSorted(desc, sortParameterSql, session, Brewery.class);

        }
    }

    @Override
    public boolean checkExists(Brewery brewery) {
        Map<Integer, Set<String>> fromCurrentBreweries = getBreweryDistinctions(brewery.getId());
        List<String> currentStats = asList(brewery.getName(), brewery.getAddress(), brewery.getCountryName());
        for (Map.Entry<Integer, Set<String>> entry : fromCurrentBreweries.entrySet()) {
            if (entry.getValue().containsAll(currentStats)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkBreweryEmailExists(Brewery brewery, int currentId) {
        try (Session session = sessionFactory.openSession()) {

            Set<String> currentEmails = getSingleColumnAsStringsQuery(
                    session, brewery.getClass(), currentId, EMAIL);

            return currentEmails.contains(brewery.getEmail());
        }
    }

    private Map<Integer, Set<String>> getBreweryDistinctions(int breweryId) {
        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
            Root<Brewery> beerRoot = criteriaQuery.from(Brewery.class);

            List<Path<Object[]>> paths = new LinkedList<>();

            paths.add(beerRoot.get(ID));
            paths.add(beerRoot.get(NAME));
            paths.add(beerRoot.get(ADDRESS));
            paths.add(beerRoot.get(COUNTRY).get(NAME));

            Predicate predicate = criteriaBuilder.notEqual(paths.get(0), breweryId);
            criteriaQuery.multiselect(paths.toArray(Path[]::new)).where(predicate);

            List<Object[]> multipleColumns = session.createQuery(criteriaQuery).getResultList();

            return getMapForDistinctions(multipleColumns);
        }
    }

    private List<Predicate> getPredicates(Map<String, String> searchParams, CriteriaBuilder criteriaBuilder
            , Root<Brewery> root) {

        List<Predicate> predicates = new LinkedList<>();
        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            switch (entry.getKey()) {
                case ID: {
                    predicates.add(criteriaBuilder.equal(root
                            .get(entry.getKey()), entry.getValue()));
                    break;
                }
                case NAME: {
                    predicates.add(criteriaBuilder.like(root
                            .get(entry.getKey()), "%" + entry.getValue() + "%"));
                    break;
                }
                case COUNTRY: {
                    predicates.add(criteriaBuilder.like(root
                            .get(entry.getKey()).get(NAME), "%" + entry.getValue() + "%"));
                    break;
                }
            }
        }
        return predicates;
    }

    private String getSortParamSql(String sortParameter) {
        String sortParameterSql;
        switch (sortParameter) {
            case NAME: {
                sortParameterSql = NAME;
                break;
            }
            case BEERS_MADE: {
                sortParameterSql = BEERS_MADE;
                break;
            }
            case RATING: {
                sortParameterSql = RATING;
                break;
            }
            default: {
                sortParameterSql = ID;
            }
        }
        return sortParameterSql;
    }

    private Map<Integer, Set<String>> getMapForDistinctions(List<Object[]> multipleColumns) {
        Map<Integer, Set<String>> breweryDistinctions = new HashMap<>();

        for (Object[] arr : multipleColumns) {

            Integer id = (Integer) arr[0];
            String breweryName = (String) arr[1];
            String breweryAddress = (String) arr[2];
            String breweryCountry = (String) arr[3];

            breweryDistinctions.put(
                    id,
                    new HashSet<>(asList(breweryName, breweryAddress, breweryCountry))
            );
        }
        return breweryDistinctions;
    }

    @Override
    public List<Brewery> getTop5Breweries() {
        try (Session session = sessionFactory.openSession()) {
            return getTopFiveEntities(session, Brewery.class);
        }
    }

    @Override
    public List<Brewery> getBeerListBreweries(Collection<Beer> userBeers) {
        return userBeers.stream().map(Beer::getBrewery).distinct().collect(Collectors.toList());
    }
}


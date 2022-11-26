package beertag.repositories;

import beertag.models.*;
import beertag.repositories.contracts.BeerRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.*;

import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Constants.HqlQueriesConstants.USER;
import static beertag.Constants.UserConstants.WISH_LIST;
import static beertag.Utility.*;
import static java.util.Arrays.asList;

@Repository
public class BeerRepositoryHibernate implements BeerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BeerRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.save(beer);
        }
    }

    @Override
    public Beer getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return getEntity(id, session, Beer.class);
        }
    }

    @Override
    public void remove(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(beer);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Beer> getAll(int desc, String sortParameter) {
        String sortParameterSql = getSortParamSql(sortParameter);
        try (Session session = sessionFactory.openSession()) {
            return getAllEntitiesSorted(desc, sortParameterSql, session, Beer.class);
        }
    }

    @Override
    public List<Beer> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return getAllEntities(Beer.class, session);
        }
    }

    @Override
    public boolean checkExists(Beer beer) {
        Map<Integer, Set<String>> fromCurrentBeers = getBeerDistinctions(beer.getId());

        List<String> currentStats = asList(beer.getName(), beer.getStyleName(),
                beer.getBreweryName(), beer.getCountryName());

        for (Map.Entry<Integer, Set<String>> entry : fromCurrentBeers.entrySet()) {
            if (entry.getValue().containsAll(currentStats)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Beer> search(Map<String, String> searchParams) {

        boolean descending = searchParams.get(DESC).equals(DESC);
        String sortParameterSql = getSortParamSql(searchParams.get(SORT_PARAMETER));
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Beer> criteriaQuery = criteriaBuilder.createQuery(Beer.class);
            Root<Beer> beerRoot = criteriaQuery.from(Beer.class);

            List<Predicate> predicates = getPredicates(searchParams, criteriaBuilder, beerRoot, session);

            sortQuery(descending, sortParameterSql, criteriaBuilder, criteriaQuery,
                    beerRoot, predicates);

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public List<Beer> getUserBeerListSearch(Map<String, String> searchParams,
                                            Collection<Beer> userBeers) {

        boolean descending = searchParams.get(DESC).equals(DESC);
        String sortParameterSql = getSortParamSql(searchParams.get(SORT_PARAMETER));

        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Beer> criteriaQuery = criteriaBuilder.createQuery(Beer.class);

            Root<Beer> beerRoot = criteriaQuery.from(Beer.class);

            List<Predicate> searchPredicates = getPredicates(searchParams, criteriaBuilder,
                    beerRoot, session);

            searchPredicates.add((beerRoot.in(userBeers)));

            sortQuery(descending, sortParameterSql, criteriaBuilder, criteriaQuery,
                    beerRoot, searchPredicates);

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public List<Beer> getUserBeerList(UserDetails userDetails, String wishOrDrankList, int desc, String sortParameter, int limit) {

        boolean descending = desc == 1;
        String sortParameterSql = getSortParamSql(sortParameter);
        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Beer> criteriaQuery = criteriaBuilder.createQuery(Beer.class);

            Root<Beer> beerRoot = criteriaQuery.from(Beer.class);
            Collection<Beer> userBeers = getUserBeers(userDetails, wishOrDrankList);
            List<Predicate> predicates = Collections.singletonList((beerRoot.in(userBeers)));

            sortQuery(descending, sortParameterSql, criteriaBuilder,
                    criteriaQuery, beerRoot, predicates);
            if (limit < 0) limit = 0;
            return session.createQuery(criteriaQuery).setMaxResults(limit).getResultList();
        }
    }

    @Override
    public List<Beer> ListAllBeers() {
        return getAll();
    }

    @Override
    public List<Beer> getTop5Beers() {
        try (Session session = sessionFactory.openSession()) {

            return getTopFiveEntities(session, Beer.class);
        }

    }

    private String getSortParamSql(String sortParameter) {
        String sortParameterSql;
        switch (sortParameter) {
            case NAME: {
                sortParameterSql = NAME;
                break;
            }
            case ABV: {
                sortParameterSql = ABV;
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

    private Map<Integer, Set<String>> getBeerDistinctions(int beerId) {
        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
            Root<Beer> beerRoot = criteriaQuery.from(Beer.class);

            List<Path<Tuple>> paths = new LinkedList<>();

            paths.add(beerRoot.get(ID));
            paths.add(beerRoot.get(NAME));
            paths.add(beerRoot.get(STYLE).get(NAME));
            paths.add(beerRoot.get(BREWERY).get(NAME));
            paths.add(beerRoot.get(BREWERY).get(COUNTRY).get(NAME));

            Predicate excludeThisId = criteriaBuilder.notEqual(paths.get(0), beerId);
            criteriaQuery.multiselect(paths.toArray(Path[]::new)).where(excludeThisId);

            List<Tuple> multipleColumns = session.createQuery(criteriaQuery).getResultList();

            return getIntegerSetMap(multipleColumns);
        }
    }

    private Map<Integer, Set<String>> getIntegerSetMap(List<Tuple> multipleColumns) {
        Map<Integer, Set<String>> beerDistinctions = new HashMap<>();
        for (Tuple tuple : multipleColumns) {
            Integer id = (Integer) tuple.get(0);
            String beerName = (String) tuple.get(1);
            String beerStyle = (String) tuple.get(2);
            String beerBrewery = (String) tuple.get(3);
            String beerCountry = (String) tuple.get(4);
            beerDistinctions.put(id,
                    new HashSet<>(asList(beerName, beerStyle,
                            beerBrewery, beerCountry)));
        }
        return beerDistinctions;
    }

    private List<Predicate> getPredicates(Map<String, String> searchParams, CriteriaBuilder criteriaBuilder,
                                          Root<Beer> beerRoot, Session session) {
        List<Predicate> predicates = new LinkedList<>();

        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            switch (entry.getKey()) {
                case ABV: {
                    predicates.add(criteriaBuilder.equal(beerRoot.get(ABV), entry.getValue()));
                    break;
                }
                case ID: {
                    predicates.add(criteriaBuilder.equal(beerRoot.get(ID), entry.getValue()));
                    break;
                }
                case MORE + ABV: {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(beerRoot.get(ABV), entry.getValue()));
                    break;
                }
                case LESS + ABV: {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(beerRoot.get(ABV), entry.getValue()));
                    break;
                }
                case NAME: {
                    predicates.add(criteriaBuilder.like(beerRoot.get(NAME), "%" + entry.getValue() + "%"));
                    break;
                }
                case STYLE: {
                    predicates.add(criteriaBuilder.like(beerRoot.get(STYLE)
                            .get(NAME), "%" + entry.getValue() + "%"));
                    break;
                }
                case BREWERY: {
                    predicates.add(criteriaBuilder.like(beerRoot.get(BREWERY)
                            .get(NAME), "%" + entry.getValue() + "%"));
                    break;
                }
                case COUNTRY: {
                    predicates.add(criteriaBuilder.like(beerRoot.get(BREWERY)
                            .get(COUNTRY).get(NAME), "%" + entry.getValue() + "%"));
                    break;
                }
                case AUTHOR: {
                    predicates.add(criteriaBuilder.equal(beerRoot.get(USER).get(USER_NAME), entry.getValue()));
                    break;
                }
                case TAGS: {
                    /* single tag by id
                    Optional<Tag> tag = getOptionalEntity(Integer.parseInt(entry.getValue()), session, Tag.class);
                    tag.ifPresent(value -> predicates.add(criteriaBuilder.isMember(value, beerRoot.get(TAGS))));
                    */

                    /* many tags by ids
                    List<Integer> tagIds = Arrays.stream(entry.getValue().split(" "))
                            .map(Integer::parseInt).collect(Collectors.toCollection(LinkedList::new));
                    List<Tag> beerTags = new LinkedList<>();
                    tagIds.forEach(i -> getOptionalEntity(i, session, Tag.class).ifPresent(beerTags::add));
                    beerTags.forEach(t -> predicates.add(criteriaBuilder.isMember(t, beerRoot.get(TAGS))));
                    */

                    //many tags by name
                    List<Tag> tags = new LinkedList<>();
                    Arrays.stream(entry.getValue().split(" +"))
                            .map(str -> getEntityByAttribute(str, LIKE, NAME, Tag.class, session))
                            .forEach(tags::addAll);
                    if (tags.isEmpty()) {
                        final int nonExistentTagId = Short.MIN_VALUE;
                        final Tag dummyTag = new Tag(nonExistentTagId, "");
                        tags.add(dummyTag);
                    }
                    tags.forEach(t -> predicates.add(criteriaBuilder.isMember(t, beerRoot.get(TAGS))));
                    break;
                }
                case LESS + RATING: {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(beerRoot
                            .get(RATING), entry.getValue()));
                    break;
                }
                case MORE + RATING: {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(beerRoot
                            .get(RATING), entry.getValue()));
                    break;
                }
                case RATING: {
                    predicates.add(criteriaBuilder.equal(beerRoot.get(RATING), entry.getValue()));
                }
                default: {
                    break;
                }
            }
        }
        return predicates;
    }

    private Collection<Beer> getUserBeers(UserDetails userDetails, String wishOrDrankList) {
        if (wishOrDrankList.equals(WISH_LIST)) {
            return userDetails.getWishList();
        } else {
            return userDetails.getDrankList();
        }
    }

    @Override
    public void multipleRemove(Collection<Beer> beersToRemove) {
        for (Beer beer : beersToRemove) {
            remove(beer);
        }
    }

}

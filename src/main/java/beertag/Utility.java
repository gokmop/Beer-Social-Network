package beertag;

import beertag.exception.EntityNotFoundException;
import beertag.models.*;
import beertag.models.contracts.HasRating;
import beertag.models.dto.beer.BeerSearchDTO;
import beertag.models.dto.beer.DisplayBeerDTO;
import beertag.models.dto.beer.RateDTO;
import beertag.models.mappers.BeerMapper;
import beertag.services.contracts.*;
import org.hibernate.Session;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.ENTITY_WITH_ID_D_NOT_FOUND;
import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Constants.ModelViewConstants.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class Utility {

    /**
     * @param session     standard hibernate session from sessionFactory
     * @param entityClass class of the entity which table is to be queried
     *                    beer.getClass()
     * @param idToExclude if the method is used in an update context it should
     *                    exclude the current entity's id
     * @param attribute   the name of the field to be collected in a string format
     * @param <E>         type of entity <Beer>
     * @return a set of strings from column with name @attribute from table @entityClass
     * Basically this method works like so :
     * <p>
     * select @attribute from @entityClass where @entityClass.id != @idToExclude
     * </p>
     */

    public static <E> Set<String> getSingleColumnAsStringsQuery(
            Session session, Class<E> entityClass, int idToExclude, String attribute) {

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<String> stringCriteriaQuery = criteriaBuilder.createQuery(String.class);

        Root<E> root = stringCriteriaQuery.from(entityClass);

        Predicate restriction = criteriaBuilder.notEqual(root.get(ID), idToExclude);

        Selection<String> selection = stringCriteriaQuery.select(root.get(attribute)).where(restriction).getSelection();

        CompoundSelection<String> projection = criteriaBuilder.construct(String.class, selection);
        stringCriteriaQuery.select(projection);
        return new HashSet<>(session.createQuery(stringCriteriaQuery).getResultList());
    }

    public static <E> E getEntity(int entityId, Session session, Class<E> entityClass) {
        E entity = session.get(entityClass, entityId);
        if (entity == null) {
            throw new EntityNotFoundException(String.format(ENTITY_WITH_ID_D_NOT_FOUND,
                    entityClass.getSimpleName(), entityId));
        }
        return entity;
    }

    /**
     * @param entityId    id we're looking for
     * @param session     hibernate session
     * @param entityClass class of the entity you're looking for
     * @param <E>         type of the entity
     * @return returns an Optional<E> entity
     */
    public static <E> Optional<E> getOptionalEntity(int entityId, Session session, Class<E> entityClass) {
        return Optional.ofNullable(session.get(entityClass, entityId));
    }

    /**
     * @param attr        value you want to compare to
     * @param likeOrEqual like -> jo finds John, Johnny, Joe; equal -> John finds only John
     * @param attrName    name of the field which holds the attribute
     * @param entityClass class of the object we are querying
     * @param session     hibernate session
     * @param <E>         type of the object we are querying
     * @return a list of entities which fit the criteria
     * if attr is not a valid entity field return empty list
     */
    public static <E, T> List<E> getEntityByAttribute(T attr, String likeOrEqual, String attrName,
                                                      Class<E> entityClass, Session session) {

        Set<String> entityFields = Arrays.stream(entityClass.getDeclaredFields())
                .map(Field::getName).collect(Collectors.toSet());

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);

        if (!entityFields.contains(attrName)) {
            return new ArrayList<>();//TODO how to handle
        }
        Predicate likeOrEqualPrt;
        if (attr instanceof String && likeOrEqual.equals(LIKE)) {
            likeOrEqualPrt = criteriaBuilder.like(root.get(attrName), "%" + attr + "%");
        } else {
            likeOrEqualPrt = criteriaBuilder.equal(root.get(attrName), attr);
        }

        return session.createQuery(criteriaQuery.select(root).where(likeOrEqualPrt)).getResultList();
    }

    /**
     * @param session hibernate session
     * @param eClass  class of the object we are querying
     * @param <E>     type of the object we are querying
     * @return a List<E> entities
     */
    public static <E> List<E> getAllEntities(Class<E> eClass, Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(eClass);
        Root<E> root = criteriaQuery.from(eClass);
        criteriaQuery.select(root);
        return session.createQuery(criteriaQuery.select(root)).getResultList();

    }

    /**
     * @param desc          1 for descending anything else is descending
     * @param sortParameter parameter by which we sort
     * @param session       hibernate session
     * @param entityClass   class of the object we are querying
     * @param <E>           type of the object we are querying
     * @return a List<E> of sorted entities
     */
    public static <E> List<E> getAllEntitiesSorted(int desc, String sortParameter, Session session, Class<E> entityClass) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        boolean descending = desc == 1;
        sortQuery(descending, sortParameter, criteriaBuilder, criteriaQuery,
                root, new ArrayList<>());

        return session.createQuery(criteriaQuery).getResultList();
    }

    /**
     * @param descending       true for ascending false for descending
     * @param sortParameterSql some field(the field can only be String or Number)
     *                         of the entity to be queried
     * @param criteriaBuilder  a tool to construct query criteria
     * @param criteriaQuery    an already constructed CriteriaQuery with a specified type
     * @param root             table of origin for the query should be the same type as above
     * @param predicates       restrictions can be empty
     * @param <E>              type of the already constructed objects
     *                         This method works like this:
     *                         *some query expression* order by @sortParameterSql @descending ? desc : asc
     */
    public static <E> void sortQuery(boolean descending, String sortParameterSql, CriteriaBuilder criteriaBuilder,
                                     CriteriaQuery<E> criteriaQuery, Root<E> root, List<Predicate> predicates) {

        criteriaQuery.distinct(true).select(root).where(predicates.toArray(Predicate[]::new));

        if (descending) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortParameterSql)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortParameterSql)));
        }

    }

    /**
     * @param searchParams map from searchDto interface containing search parameters
     * @param thisOrThat   an additional parameter [less] or [more]; [exact] or [not exact]
     * @param value        the value to be saved in the map
     * @param attribute    the key for the value concatenated with @thisOrThat
     *                     if present
     * @param <T>          the type of value
     */
    public static <T> void thisOrThat(Map<String, String> searchParams, Optional<String> thisOrThat,
                                      Optional<T> value, String attribute, String dis, String dat) {
        if (thisOrThat.isPresent() && thisOrThat.get().equals(dis)) {
            value.ifPresent(d -> searchParams.put(dis + attribute, String.valueOf(d)));
        } else if (thisOrThat.isPresent() && thisOrThat.get().equals(dat)) {
            value.ifPresent(d -> searchParams.put(dat + attribute, String.valueOf(d)));
        } else {
            value.ifPresent(d -> searchParams.put(attribute, String.valueOf(d)));
        }
    }

    /**
     * @param userService     userService
     * @param beerMapper      beerMapper
     * @param beerService     beerService
     * @param userName        user to get list for
     * @param desc            sort in asc or desc
     * @param sortParam       sort by id, abv, rating, name
     * @param wishOrDrankList specify what kind of list
     * @return a list of ready to display beers from usersDrankList or Wishlist
     */
    public static List<DisplayBeerDTO> getDisplayBeerDTOS(UserService userService, BeerMapper beerMapper,
                                                          BeerService beerService, String userName,
                                                          Integer desc, String sortParam,
                                                          String wishOrDrankList, int limit) {
        try {
            UserDetails userDetails = userService.getUserDetailsByUsername(userName);
            List<Beer> sortedWishList = beerService.getList(userDetails, wishOrDrankList, Optional.ofNullable(desc),
                    Optional.ofNullable(sortParam), limit);
            return beerMapper.toDTO(sortedWishList);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }


    /**
     * @param dto               dto with rating
     * @param username          valid username
     * @param userService       userService
     * @param beerService       beerService
     * @param beerRatingService beerRatingService
     * @return returns a BeerRating object
     */
    public static BeerRating getRating(RateDTO dto, String username, UserService userService,
                                       BeerService beerService, BeerRatingService beerRatingService) {
        UserDetails rater = userService.getUserDetailsByUsername(username);
        Beer beerToRate = beerService.getById(dto.getBeerId());

        BeerRating beerRating = beerRatingService.getByBeerIdAndUserID(dto.getBeerId(), rater.getId());
        beerRating.setRating(dto.getRating());
        beerRating.setUser(rater);
        beerRating.setBeer(beerToRate);
        return beerRating;
    }


    /**
     * @param session     hibernate session
     * @param entityClass Entity which has implemented HasRating interface
     * @param <T>         T type must have a field [Double Rating]
     * @return returns a maximum of five top rated entities
     */
    public static <T extends HasRating> List<T> getTopFiveEntities(Session session, Class<T> entityClass) {

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);

        Root<T> root = criteriaQuery.from(entityClass);
        Order desc = criteriaBuilder.desc(root.get(RATING));
        criteriaQuery.select(root).orderBy(desc);

        return session.createQuery(criteriaQuery).setMaxResults(5).getResultList();

    }
}

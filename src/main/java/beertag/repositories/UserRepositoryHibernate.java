package beertag.repositories;

import beertag.exception.EntityNotFoundException;
import beertag.models.User;
import beertag.models.UserDetails;
import beertag.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Constants.UserConstants.USER_WITH_USERNAME_S_DOESN_T_EXIST;
import static beertag.Utility.*;

@Repository
public class UserRepositoryHibernate implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<UserDetails> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return getAllEntities(UserDetails.class, session);
        }
    }

    @Override
    public List<UserDetails> getAll(int desc, String sortParameter) {
        String sortParameterSql = getSortParamSql(sortParameter);
        try (Session session = sessionFactory.openSession()) {
            return getAllEntitiesSorted(desc, sortParameterSql, session, UserDetails.class);
        }
    }

    @Override
    public UserDetails getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return getEntity(id, session, UserDetails.class);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            List<User> resultList = getEntityByAttribute(username, EQUAL, USER_NAME, User.class, session);
            if (resultList.isEmpty()) {
                throw new EntityNotFoundException(String.format(USER_WITH_USERNAME_S_DOESN_T_EXIST, username));
            }
            return resultList.get(0);
        }
    }

    @Override
    public UserDetails getUserDetailsByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            List<UserDetails> resultList = getEntityByAttribute(username, EQUAL, USER_NAME, UserDetails.class, session);
            if (resultList.isEmpty()) {
                throw new EntityNotFoundException(String.format(USER_WITH_USERNAME_S_DOESN_T_EXIST, username));
            }
            return resultList.get(0);
        }
    }

    @Override
    public List<UserDetails> search(Map<String, String> searchParams) {

        boolean descending = searchParams.get(DESC).equals(DESC);
        String sortParameterSql = getSortParamSql(searchParams.get(SORT_PARAMETER));

        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserDetails> criteriaQuery = criteriaBuilder.createQuery(UserDetails.class);
            Root<UserDetails> userRoot = criteriaQuery.from(UserDetails.class);

            List<Predicate> predicates = getPredicates(searchParams, criteriaBuilder, userRoot);

            sortQuery(descending, sortParameterSql, criteriaBuilder, criteriaQuery,
                    userRoot, predicates);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }


    @Override
    public void update(UserDetails userDetails) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(userDetails);
            session.getTransaction().commit();
        }
    }

    @Transactional
    @Override
    public void create(UserDetails userDetails) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(userDetails);
            session.getTransaction().commit();
        }
    }

    @Override
    public void remove(UserDetails userDetails) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(userDetails);
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeUser(User userToRemove) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(userToRemove);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateUser(User userToRemove) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(userToRemove);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean checkIfUsernameTaken(UserDetails userDetails, int currentId) {
        try (Session session = sessionFactory.openSession()) {
            Set<String> userCredentials = getSingleColumnAsStringsQuery(
                    session, userDetails.getClass(), currentId, USER_NAME);

            return userCredentials.contains(userDetails.getUserName());
        }
    }

    @Override
    public boolean checkIfEmailTaken(UserDetails userDetails, int currentId) {
        try (Session session = sessionFactory.openSession()) {

            Set<String> userCredentials = getSingleColumnAsStringsQuery(
                    session, userDetails.getClass(), currentId, EMAIL);

            return userCredentials.contains(userDetails.getEmail());
        }
    }

    private String getSortParamSql(String sortParameter) {
        switch (sortParameter) {
            case FIRST_NAME: {
                sortParameter = FIRST_NAME;
                break;
            }
            case LAST_NAME: {
                sortParameter = LAST_NAME;
                break;
            }
            case USER_NAME: {
                sortParameter = USER_NAME;
                break;
            }
            case "age": {
                sortParameter = BIRTH_DATE;
                break;
            }
            default: {
                sortParameter = ID;
            }
        }
        return sortParameter;
    }

    private List<Predicate> getPredicates(Map<String, String> searchParams, CriteriaBuilder criteriaBuilder, Root<UserDetails> userRoot) {
        List<Predicate> predicates = new LinkedList<>();

        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            switch (entry.getKey()) {
                case LESS + BIRTH_DATE: {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(userRoot.get(BIRTH_DATE), entry.getValue()));
                    break;
                }
                case MORE + BIRTH_DATE: {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(userRoot.get(BIRTH_DATE), entry.getValue()));
                    break;
                }
                case BIRTH_DATE: {
                    predicates.add(criteriaBuilder.equal(userRoot.get(entry.getKey()), entry.getValue()));
                    break;
                }
                case EXACT + LAST_NAME:
                case EXACT + FIRST_NAME:
                case EXACT + USER_NAME: {
                    String key = entry.getKey().replace(EXACT, "");
                    predicates.add(criteriaBuilder.equal(userRoot.get(key), entry.getValue()));
                    break;
                }
                case LAST_NAME:
                case FIRST_NAME:
                case USER_NAME:
                case NOT_EXACT + LAST_NAME:
                case NOT_EXACT + FIRST_NAME:
                case NOT_EXACT + USER_NAME: {
                    String key = entry.getKey().replace(NOT_EXACT, "");
                    predicates.add(criteriaBuilder.like(userRoot.get(key), "%" + entry.getValue() + "%"));
                    break;
                }
            }
        }
        return predicates;
    }
}
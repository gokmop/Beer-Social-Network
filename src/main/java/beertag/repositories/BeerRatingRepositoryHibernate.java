package beertag.repositories;

import beertag.models.BeerRating;
import beertag.repositories.contracts.BeerRatingRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static beertag.Constants.AuthorisationConstants.BEER;
import static beertag.Constants.HqlQueriesConstants.ID;
import static beertag.Constants.HqlQueriesConstants.USER;

@Repository
public class BeerRatingRepositoryHibernate implements BeerRatingRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BeerRatingRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(BeerRating beerRating) {
        try (Session session = sessionFactory.openSession()) {
            session.save(beerRating);
        }
    }

    @Override
    public void remove(BeerRating beerRating) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(beerRating);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(BeerRating beerRating) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(beerRating);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean checkExists(BeerRating beerRating) {
        BeerRating possibleMatch = getBeerRatingByUserAndBeer(beerRating.getBeerId(), beerRating.getUserId());
        return beerRating.equals(possibleMatch);
    }

    @Override
    public BeerRating getBeerRatingByUserAndBeer(int beerId, int userId) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
            CriteriaQuery<BeerRating> criteriaQuery = criteriaBuilder.createQuery(BeerRating.class);
            Root<BeerRating> beerRatingRoot = criteriaQuery.from(BeerRating.class);

            Predicate equalUserId = criteriaBuilder.equal(beerRatingRoot.get(USER).get(ID), userId);
            Predicate equalBeerId = criteriaBuilder.equal(beerRatingRoot.get(BEER).get(ID), beerId);

            criteriaQuery.select(beerRatingRoot).where(equalUserId, equalBeerId);

            List<BeerRating> resultList = session.createQuery(criteriaQuery).getResultList();
            if (resultList.isEmpty()) {
                return new BeerRating();
            }
            return resultList.get(0);
        }
    }
}

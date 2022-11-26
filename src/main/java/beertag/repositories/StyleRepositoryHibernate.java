package beertag.repositories;

import beertag.models.Beer;
import beertag.models.Style;
import beertag.repositories.contracts.StyleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static beertag.Constants.HqlQueriesConstants.ID;
import static beertag.Constants.HqlQueriesConstants.NAME;
import static beertag.Utility.*;

@Repository
public class StyleRepositoryHibernate implements StyleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public StyleRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Style> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return getAllEntities(Style.class, session);
        }
    }

    @Override
    public List<Style> getAll(int desc, String sortParameter) {
        if (sortParameter.equals(NAME)) {
            sortParameter = NAME;
        } else {
            sortParameter = ID;
        }
        try (Session session = sessionFactory.openSession()) {
            return getAllEntitiesSorted(desc, sortParameter, session, Style.class);

        }
    }

    public List<Style> getAllStyles() {
        try (Session session = sessionFactory.openSession()) {
            Query<Style> query = session.createQuery("from Style", Style.class);
            return query.list();
        }
    }

    @Override
    public Style getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return getEntity(id, session, Style.class);
        }
    }

    @Override
    public void create(Style style) {
        try (Session session = sessionFactory.openSession()) {
            session.save(style);
        }
    }

    @Override
    public void remove(Style style) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(style);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Style style) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(style);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean checkExists(Style style) {
        try (Session session = sessionFactory.openSession()) {

            Set<String> currentStyles = getSingleColumnAsStringsQuery(
                    session, style.getClass(), style.getId(), NAME);

            return currentStyles.contains(style.getName());
        }
    }

    @Override
    public List<Style> getBeerListStyles(Collection<Beer> userBeers) {
        return userBeers.stream().map(Beer::getStyle).distinct().collect(Collectors.toCollection(LinkedList::new));
    }
}

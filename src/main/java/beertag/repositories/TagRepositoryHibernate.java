package beertag.repositories;

import beertag.models.Beer;
import beertag.models.Tag;
import beertag.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import static beertag.Constants.HqlQueriesConstants.ID;
import static beertag.Constants.HqlQueriesConstants.NAME;
import static beertag.Utility.*;

@Repository
public class TagRepositoryHibernate implements TagRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public TagRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Tag> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return getAllEntities(Tag.class, session);
        }
    }

    @Override
    public List<Tag> getAll(int desc, String sortParameter) {
        if (sortParameter.equals(NAME)) {
            sortParameter = NAME;
        } else {
            sortParameter = ID;
        }
        try (Session session = sessionFactory.openSession()) {
            return getAllEntitiesSorted(desc, sortParameter, session, Tag.class);
        }
    }


    @Override
    public Tag getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return getEntity(id, session, Tag.class);
        }
    }

    @Override
    public void create(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.save(tag);
        }
    }

    @Override
    public void remove(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(tag);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(tag);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean checkExists(Tag tag) {
        try (Session session = sessionFactory.openSession()) {

            Set<String> currentStyles = getSingleColumnAsStringsQuery(
                    session, tag.getClass(), tag.getId(), NAME);

            return currentStyles.contains(tag.getName());
        }
    }

    @Override
    public List<Tag> getBeerListTags(Collection<Beer> userBeers) {
        Set<Tag> result = new HashSet<>();
        userBeers.stream().map(Beer::getTags).forEach(result::addAll);
        return new LinkedList<>(result);
    }
}

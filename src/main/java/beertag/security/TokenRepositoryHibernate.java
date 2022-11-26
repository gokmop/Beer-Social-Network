package beertag.security;

import beertag.exception.EntityNotFoundException;
import beertag.models.User;
import beertag.models.VerificationToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static beertag.Constants.HqlQueriesConstants.EQUAL;
import static beertag.Utility.*;

@Repository
public class TokenRepositoryHibernate implements TokenRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TokenRepositoryHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public VerificationToken findByToken(String token) {
        return getVerificationToken(token);
    }

    @Override
    public VerificationToken findByUser(User user) {
        return getVerificationToken(user);
    }

    @Override
    public void create(VerificationToken verificationToken) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(verificationToken);
            session.getTransaction().commit();
        }
    }

    @Override
    public VerificationToken getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return getEntity(id, session, VerificationToken.class);
        }
    }

    @Override
    public void remove(VerificationToken verificationToken) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(verificationToken);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(VerificationToken verificationToken) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(verificationToken);
            session.getTransaction().commit();
        }
    }

    private <T> VerificationToken getVerificationToken(T t) {
        try (Session session = sessionFactory.openSession()) {
            List<VerificationToken> result = getEntityByAttribute(t, EQUAL,
                    "token", VerificationToken.class, session);
            if (result.isEmpty()) {
                throw new EntityNotFoundException("No token like this!");
            }
            return result.get(0);
        }
    }
}

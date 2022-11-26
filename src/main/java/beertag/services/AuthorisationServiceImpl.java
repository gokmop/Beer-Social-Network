package beertag.services;

import beertag.exception.AccountNotActivatedException;
import beertag.models.Beer;
import beertag.models.User;
import beertag.models.UserDetails;
import beertag.services.contracts.AuthorisationService;
import org.springframework.stereotype.Service;

@Service
public class AuthorisationServiceImpl implements AuthorisationService {

    public AuthorisationServiceImpl() {
    }

    @Override
    public void authorise(User userToAuthorise, String message) throws UnsupportedOperationException {
        if (userToAuthorise.notAdmin()) {
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void authorise(Beer beer, User userToAuthorise, String message) throws UnsupportedOperationException {
        UserDetails toAuthoriseDetails = userToAuthorise.getUserDetails();
        if (beer.getAuthorId() != toAuthoriseDetails.getId() && userToAuthorise.notAdmin()) {
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void authorise(User user, User userToAuthorise, String message) throws UnsupportedOperationException {
        if (!user.equals(userToAuthorise) && userToAuthorise.notAdmin()) {
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void checkAccountActive(User user) throws AccountNotActivatedException {
        if (!user.isEnabled()) {
            throw new AccountNotActivatedException();
        }
    }
}

package beertag.services.contracts;

import beertag.exception.AccountNotActivatedException;
import beertag.models.Beer;
import beertag.models.User;
import beertag.models.UserDetails;

public interface AuthorisationService {

    void authorise(User userToAuthorise, String message) throws UnsupportedOperationException;

    void authorise(Beer beer, User userToAuthorise, String message) throws UnsupportedOperationException;

    void authorise(User user, User userToAuthorise, String message) throws UnsupportedOperationException;

    void checkAccountActive(User user) throws AccountNotActivatedException;

}

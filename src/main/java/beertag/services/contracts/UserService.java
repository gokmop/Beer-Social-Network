package beertag.services.contracts;

import beertag.models.User;
import beertag.models.UserDetails;
import beertag.services.contracts.generic.*;
import beertag.models.VerificationToken;

public interface UserService extends GetService<UserDetails>, UpdateRemoveService<UserDetails>, SearchService<UserDetails>, UserCreate<UserDetails> {

    UserDetails getUserDetailsByUsername(String username);

    User getUserByUsername(String username);

    void ensureUsernameNotTaken(UserDetails userDetails, String message);

    void ensureEmailNotTaken(UserDetails userDetails, String message);

    void updateLists(UserDetails userDetails);

    void updateUser(User userToUpdate);
    /*
    token stuff below v - v - v
     */

    User getUser(String verificationToken);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

}

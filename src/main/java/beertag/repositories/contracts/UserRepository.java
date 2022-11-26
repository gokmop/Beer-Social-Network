package beertag.repositories.contracts;

import beertag.models.User;
import beertag.models.UserDetails;
import beertag.repositories.contracts.generic.CreateRepo;
import beertag.repositories.contracts.generic.GetRepo;
import beertag.repositories.contracts.generic.SearchRepo;
import beertag.repositories.contracts.generic.UpdateRemoveRepo;

public interface UserRepository extends GetRepo<UserDetails>,
        CreateRepo<UserDetails>, UpdateRemoveRepo<UserDetails>, SearchRepo<UserDetails> {

    User getUserByUsername(String username);

    void removeUser(User userToRemove);

    void updateUser(User userToRemove);

    UserDetails getUserDetailsByUsername(String username);

    boolean checkIfUsernameTaken(UserDetails userDetails, int currentId);

    boolean checkIfEmailTaken(UserDetails userDetails, int currentId);

}

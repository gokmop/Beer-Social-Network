package beertag.security;

import beertag.models.User;
import beertag.models.VerificationToken;
import beertag.repositories.contracts.generic.CreateRepo;
import beertag.repositories.contracts.generic.UpdateRemoveRepo;


public interface TokenRepository
        extends CreateRepo<VerificationToken>, UpdateRemoveRepo<VerificationToken> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    VerificationToken getById(int id);

}

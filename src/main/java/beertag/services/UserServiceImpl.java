package beertag.services;

import beertag.exception.DuplicateEntityException;
import beertag.models.User;
import beertag.models.UserDetails;
import beertag.repositories.contracts.BeerRepository;
import beertag.repositories.contracts.UserRepository;
import beertag.services.contracts.UserService;
import beertag.services.contracts.AuthorisationService;
import beertag.security.TokenRepository;
import beertag.models.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import static beertag.Constants.*;
import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.UserConstants.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorisationService authorisationService;
    private final BeerRepository beerRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BeerRepository beerRepository,
                           AuthorisationService authorisationService, TokenRepository tokenRepository) {
        this.beerRepository = beerRepository;
        this.userRepository = userRepository;
        this.authorisationService = authorisationService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<UserDetails> getAll(Optional<Integer> desc, Optional<String> sortParameter) {
        return userRepository.getAll(desc.orElse(Integer.SIZE), sortParameter.orElse(SORT_DEFAULT));
    }

    @Override
    public UserDetails getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public List<UserDetails> search(Map<String, String> searchParams) {
        return userRepository.search(searchParams);
    }

    @Override
    public void update(UserDetails userDetailsToUpdate, User userToAuthorise) {
        //TODO test changing of username since it is an ID
        authorisationService.authorise(userToAuthorise, userToAuthorise, String
                .format(ONLY_AUTHORS_AND_ADMINS_CAN_S_ERROR, UPDATE));

        ensureUsernameNotTaken(userDetailsToUpdate, UPDATE_ERROR);
        ensureEmailNotTaken(userDetailsToUpdate, UPDATE_ERROR);
        userRepository.update(userDetailsToUpdate);
    }

    @Override
    public void create(UserDetails userDetails) {
        ensureUsernameNotTaken(userDetails, CREATE_ERROR);
        userRepository.create(userDetails);
    }


    @Override
    public void remove(int id, User authorise) {
        UserDetails detailsToRemove = getById(id);
        User userToRemove = getUserByUsername(detailsToRemove.getUserName());
        authorisationService.authorise(userToRemove, authorise, String
                .format(ONLY_AUTHORS_AND_ADMINS_CAN_S_ERROR, REMOVE));
        beerRepository.multipleRemove(detailsToRemove.getCreatedBeers());
        userRepository.remove(detailsToRemove);
        userRepository.removeUser(userToRemove);
    }

    @Override
    public UserDetails getUserDetailsByUsername(String username) {
        return getUserByUsername(username).getUserDetails();
    }

    public void ensureUsernameNotTaken(UserDetails userDetails, String message) {
        if (userRepository.checkIfUsernameTaken(userDetails, userDetails.getId())) {
            throw new DuplicateEntityException(message.concat(DUPLICATE_USERNAME_ERROR));
        }
    }

    public void ensureEmailNotTaken(UserDetails userDetails, String message) {
        if (userRepository.checkIfEmailTaken(userDetails, userDetails.getId())) {
            throw new DuplicateEntityException(message.concat(DUPLICATE_USER_EMAIL_ERROR));
        }
    }

    @Override
    public void updateLists(UserDetails userDetails) {
        userRepository.update(userDetails);
    }

    @Override
    public void updateUser(User userToUpdate) {
        userRepository.updateUser(userToUpdate);
        userRepository.update(userToUpdate.getUserDetails());
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        authorisationService.checkAccountActive(user);
        return user;
    }

    @Override
    public List<UserDetails> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getUser(String token) {
        VerificationToken takenToken = getVerificationToken(token);
        return takenToken.getUser();
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken newToken = new VerificationToken();
        newToken.setToken(token);
        newToken.setUser(user);
        tokenRepository.create(newToken);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

}

package beertag.services;

import beertag.exception.DuplicateEntityException;
import beertag.models.Beer;
import beertag.models.Style;
import beertag.models.User;
import beertag.repositories.contracts.BeerRepository;
import beertag.repositories.contracts.StyleRepository;
import beertag.services.contracts.StyleService;
import beertag.services.contracts.AuthorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.SORT_DEFAULT;
import static beertag.Constants.StyleConstants.STYLE_DUPLICATE_CREATE_ERROR;
import static beertag.Constants.StyleConstants.STYLE_DUPLICATE_UPDATE_ERROR;

@Service
public class StyleServiceImpl implements StyleService {

    private final StyleRepository styleRepository;
    private final BeerRepository beerRepository;
    private final AuthorisationService authorisationService;

    @Autowired
    public StyleServiceImpl(StyleRepository styleRepository, BeerRepository beerRepository,
                            AuthorisationService authorisationService) {
        this.styleRepository = styleRepository;
        this.beerRepository = beerRepository;
        this.authorisationService = authorisationService;
    }

    @Override
    public List<Style> getBeerListStyles(Collection<Beer> userBeers) {
        return styleRepository.getBeerListStyles(userBeers);
    }

    @Override
    public List<Style> getAll(Optional<Integer> desc, Optional<String> sortParameter) {
        return styleRepository.getAll(desc.orElse(Integer.SIZE), sortParameter.orElse(SORT_DEFAULT));
    }

    public List<Style> getAllStyles() {
        return styleRepository.getAllStyles();
    }

    @Override
    public Style getById(int id) {
        return styleRepository.getById(id);
    }

    @Override
    public void create(Style style, User isAdmin) {
        authorisationService.authorise(isAdmin, String.format(ONLY_ADMINS_CAN_S_S, CREATE, STYLES));
        ensureNoDuplicates(style, STYLE_DUPLICATE_CREATE_ERROR);
        styleRepository.create(style);
    }

    @Override
    public void update(Style style, User authorise) {
        authorisationService.authorise(authorise, String.format(ONLY_ADMINS_CAN_S_S, UPDATE, STYLES));
        ensureNoDuplicates(style, STYLE_DUPLICATE_UPDATE_ERROR);
        styleRepository.update(style);
    }

    @Override
    public void remove(int id, User authorise) {
        authorisationService.authorise(authorise, String.format(ONLY_ADMINS_CAN_S_S, REMOVE, STYLES));
        Style styleToRemove = getById(id);
        beerRepository.multipleRemove(styleToRemove.getThisStyleBeers());
        styleRepository.remove(styleToRemove);
    }

    @Override
    public void ensureNoDuplicates(Style style, String message) {
        if (styleRepository.checkExists(style)) {
            throw new DuplicateEntityException(message);
        }
    }

    @Override
    public List<Style> getAll() {
        return styleRepository.getAll();
    }
}

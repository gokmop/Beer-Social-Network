package beertag.controllers.mvc;

import beertag.exception.DuplicateEntityException;
import beertag.exception.EntityNotFoundException;
import beertag.models.Beer;
import beertag.models.BeerRating;
import beertag.models.User;
import beertag.models.dto.beer.BeerSearchDTO;
import beertag.models.dto.beer.CreateBeerDTO;
import beertag.models.dto.beer.DisplayBeerDTO;
import beertag.models.dto.beer.RateDTO;
import beertag.models.mappers.BeerMapper;
import beertag.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Constants.ModelViewConstants.*;
import static beertag.Utility.*;
import static org.springframework.http.HttpStatus.*;

@Controller
@RequestMapping("/beers")
public class MvcBeerController {

    private final AuthorisationService authorisationService;
    private final BeerService beerService;
    private final StyleService styleService;
    private final BreweryService breweryService;
    private final BeerMapper beerMapper;
    private final UserService userService;
    private final CountryService countryService;
    private final TagService tagService;
    private final BeerRatingService beerRatingService;

    @Autowired
    public MvcBeerController(BeerService beerService, StyleService styleService, BreweryService breweryService,
                             BeerMapper beerMapper, UserService userService, CountryService countryService,
                             TagService tagService, BeerRatingService beerRatingService,
                             AuthorisationService authorisationService) {
        this.beerService = beerService;
        this.styleService = styleService;
        this.breweryService = breweryService;
        this.beerMapper = beerMapper;
        this.userService = userService;
        this.tagService = tagService;
        this.countryService = countryService;
        this.beerRatingService = beerRatingService;
        this.authorisationService = authorisationService;
    }

    @GetMapping
    public String showAllBeersPage(Model model) {

        // for displaying information
        List<Beer> beers = beerService.getAll(Optional.of(1), Optional.of(RATING));
        newSearchForBeersResultsView(new BeerSearchDTO(), model, beerMapper.toDTO(beers), styleService,
                breweryService, countryService);
        return "beer-results";
    }

    @PostMapping
    public String searchForBeers(BeerSearchDTO searchDTO, Model model) {

        List<Beer> beers = beerService.search(searchDTO.getSearchParameters());
        List<DisplayBeerDTO> beersToDisplay = beerMapper.toDTO(beers);
        newSearchForBeersResultsView(searchDTO, model, beersToDisplay, styleService,
                breweryService, countryService);

        return "beer-results";
    }

    @GetMapping("/create-beer")
    public String getCreateBeerPage(Model model) {
        newCreateBeerView(new CreateBeerDTO(), model);
        return "beer-create-form";
    }

    @PostMapping("/create-beer")
    public String createNewBeerPage(Model model, Principal principal, @Valid @ModelAttribute(CREATE_BEER_DTO) CreateBeerDTO dto,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            newCreateBeerView(dto, model);
            return "beer-create-form";
        }

        try {
            User user = userService.getUserByUsername(principal.getName());
            Beer beer = beerMapper.fromDto(dto, user.getUserDetails());

            //beer.setAuthor(user.getUserDetails());
            beer.setEnabled(true);
            beerService.create(beer);

            showThisBeerResultsVew(model, beer);
            return "beer-results";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue(NAME, "error." + CREATE_BEER_DTO, e.getMessage());
            newCreateBeerView(dto, model);
            return "beer-create-form";
        }
    }

    @GetMapping("/update/{id}")
    public String getEditBeerPage(@PathVariable Integer id, Model model, Principal principal) {

        User user = userService.getUserByUsername(principal.getName());
        Beer beer = beerService.getById(id);

        authorisationService.authorise(beer, user, String
                .format(ONLY_AUTHORS_AND_ADMINS_CAN_S_ERROR, UPDATE));

        CreateBeerDTO dto = beerMapper.toDTO(beer, new CreateBeerDTO());

        newCreateBeerView(dto, model);
        model.addAttribute("id", id);

        return "beer-create-form";
    }

    @GetMapping("/profile/{id}")
    public String getProfileBeerPage(@PathVariable Integer id, Model model) {
        Beer beer = beerService.getById(id);
        model.addAttribute(RATE_DTO, new RateDTO());
        model.addAttribute(BEER, beer);
        return "beer-profile";
    }

    @PostMapping("/profile/rate/{id}")
    public String rateBeerProfile(@PathVariable Integer id, RateDTO dto, Principal principal, Model model) {
        dto.setBeerId(id);
        BeerRating beerRating = getRating(dto, principal.getName(), userService,
                beerService, beerRatingService);
        beerRatingService.rateBeer(beerRating);

        Beer beer = beerService.getById(dto.getBeerId());
        model.addAttribute(BEER, beer);
        model.addAttribute(RATE_DTO, dto);
        return "redirect:/beers/profile/{id}";
    }

    @PostMapping("/rate/{id}")
    public String rateBeer(@PathVariable Integer id, RateDTO dto, Principal principal, Model model) {
        dto.setBeerId(id);
        BeerRating beerRating = getRating(dto, principal.getName(), userService,
                beerService, beerRatingService);
        beerRatingService.rateBeer(beerRating);

        Beer beer = beerService.getById(dto.getBeerId());
        showThisBeerResultsVew(model, beer);
        return "beer-results";
    }

    @PostMapping(value = "/update/{id}")
    public String handleEditBeer(@PathVariable int id,
                                 @Valid @ModelAttribute(CREATE_BEER_DTO) CreateBeerDTO dto,
                                 BindingResult bindingResult,
                                 Model model, Principal principal) {

        if (bindingResult.hasErrors()) {
            newCreateBeerView(dto, model);
            return "beer-create-form";
        }

        try {
            User user = userService.getUserByUsername(principal.getName());

            Beer beer = beerService.getById(id);
            beer = beerMapper.updateFromDTO(dto, beer);

            beerService.update(beer, user);
            showThisBeerResultsVew(model, beer);

            return "beer-results";
        } catch (UnsupportedOperationException | EntityNotFoundException | DuplicateEntityException e) {
            bindingResult.rejectValue(NAME, "error." + CREATE_BEER_DTO, e.getMessage());
            newCreateBeerView(dto, model);
            return "beer-create-form";
        }
    }


    @RequestMapping("/delete/{id}")
    public String handleDeleteBeer(@PathVariable int id, Principal principal, Model model) {
//            Beer beer = beerService.getById(id);
//            showThisBeer(model, beer);
        User user = userService.getUserByUsername(principal.getName());
        beerService.remove(id, user);
        throw new ResponseStatusException(MOVED_PERMANENTLY, "Beer Successfully deleted!");
    }

    private void showThisBeerResultsVew(Model model, Beer beer) {
        model.addAttribute(STYLES, styleService.getAll(Optional.empty(), Optional.empty()));
        model.addAttribute(BREWERIES, breweryService.getAll(Optional.empty(), Optional.empty()));
        model.addAttribute(COUNTRIES, countryService.getAll(Optional.empty(), Optional.empty()));
        BeerSearchDTO searchDTO = new BeerSearchDTO();
        searchDTO.setId(beer.getId());
        List<Beer> beerList = beerService.search(searchDTO.getSearchParameters());
        model.addAttribute(BEERS, beerMapper.toDTO(beerList));
        model.addAttribute(SEARCH_BEER_DTO, searchDTO);
        model.addAttribute(RATE_DTO, new RateDTO());

    }

    private void newCreateBeerView(CreateBeerDTO dto, Model model) {
        model.addAttribute(STYLES, styleService.getAll(Optional.empty(), Optional.empty()));
        model.addAttribute(BREWERIES, breweryService.getAll(Optional.empty(), Optional.empty()));
        model.addAttribute(COUNTRIES, countryService.getAll(Optional.empty(), Optional.empty()));
        model.addAttribute(CREATE_BEER_DTO, dto);
        model.addAttribute(RATE_DTO, new RateDTO());
    }

    private void newSearchForBeersResultsView(BeerSearchDTO searchDTO, Model model, List<DisplayBeerDTO> beers,
                                              StyleService styleService, BreweryService breweryService,
                                              CountryService countryService) {
        model.addAttribute(STYLES, styleService.getAll(Optional.of(ASC_NUMERIC), Optional.of(NAME)));
        model.addAttribute(BREWERIES, breweryService.getAll(Optional.of(DESC_NUMERIC), Optional.of(RATING)));
        model.addAttribute(COUNTRIES, countryService.getAll(Optional.of(ASC_NUMERIC), Optional.of(NAME)));
        model.addAttribute(BEERS, beers);
        model.addAttribute(SEARCH_BEER_DTO, searchDTO);
        model.addAttribute(RATE_DTO, new RateDTO());
    }
}
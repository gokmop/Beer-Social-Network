package beertag.controllers.mvc;

import beertag.models.dto.beer.DisplayBeerDTO;
import beertag.models.dto.beer.RateDTO;
import beertag.models.dto.brewery.DisplayBreweryDTO;
import beertag.models.mappers.BeerMapper;
import beertag.models.mappers.BreweryMapper;
import beertag.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static beertag.Constants.AuthorisationConstants.BEERS;
import static beertag.Constants.AuthorisationConstants.BREWERIES;
import static beertag.Constants.ModelViewConstants.RATE_DTO;

@Controller
public class MvcHome {

    private final BeerService beerService;
    private final StyleService styleService;
    private final CountryService countryService;
    private final UserService userService;
    private final BreweryService breweryService;
    private final TagService tagService;
    private final BeerRatingService beerRatingService;
    private final AuthorisationService authorisationService;
    private final BeerMapper beerMapper;
    private final BreweryMapper breweryMapper;

    @Autowired
    public MvcHome(BeerService beerService, StyleService styleService, CountryService countryService,
                   UserService userService, BreweryService breweryService, TagService tagService,
                   BeerRatingService beerRatingService, AuthorisationService authorisationService,
                   BeerMapper beerMapper, BreweryMapper breweryMapper) {

        this.beerService = beerService;
        this.styleService = styleService;
        this.countryService = countryService;
        this.userService = userService;
        this.breweryService = breweryService;
        this.tagService = tagService;
        this.beerRatingService = beerRatingService;
        this.authorisationService = authorisationService;
        this.beerMapper = beerMapper;
        this.breweryMapper = breweryMapper;

    }


    @GetMapping("/")
    public String showHomePage(Model model) {
        List<DisplayBeerDTO> beerList = beerMapper.toDTO(beerService.getTop5Beers());
        List<DisplayBreweryDTO> breweryList = breweryMapper.toDTO(breweryService.getTop5Breweries());

        model.addAttribute(BEERS, beerList);
        model.addAttribute(BREWERIES, breweryList);
        model.addAttribute(RATE_DTO, new RateDTO());

        return "index";
    }

    @GetMapping("/test")
    public String showLoginPage() {
        return "test-page";
    }

    @GetMapping("/admin")
    public String showAdminPorta() {
        return "admin";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }

    @GetMapping("/register-confirmation")
    public String showRegisterConfirmation() {
        return "register-confirmation";
    }

}

package beertag.controllers.mvc;

import beertag.models.Beer;
import beertag.models.UserDetails;
import beertag.models.dto.beer.BeerSearchDTO;
import beertag.models.dto.beer.DisplayBeerDTO;
import beertag.models.dto.beer.RateDTO;
import beertag.models.mappers.BeerMapper;
import beertag.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.AuthorisationConstants.BEERS;
import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Constants.ModelViewConstants.RATE_DTO;
import static beertag.Constants.ModelViewConstants.SEARCH_BEER_DTO;
import static beertag.Constants.UserConstants.DRANK_LIST;
import static beertag.Constants.UserConstants.WISH_LIST;
import static beertag.Utility.getDisplayBeerDTOS;

@Controller
@RequestMapping("/list")
public class MvcUserListController {

    private final BeerService beerService;
    private final StyleService styleService;
    private final BreweryService breweryService;
    private final BeerMapper beerMapper;
    private final UserService userService;
    private final CountryService countryService;

    @Autowired
    public MvcUserListController(BeerService beerService, StyleService styleService, BreweryService breweryService,
                                 BeerMapper beerMapper, UserService userService, CountryService countryService) {
        this.beerService = beerService;
        this.styleService = styleService;
        this.breweryService = breweryService;
        this.beerMapper = beerMapper;
        this.userService = userService;
        this.countryService = countryService;
    }

    @GetMapping("/wishlist")
    public String getWishList(Principal principal, Model model) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());

        List<DisplayBeerDTO> beersToDisplay = getDisplayBeerDTOS(userService, beerMapper, beerService,
                userDetails.getUserName(), DESC_NUMERIC, ID, WISH_LIST, Integer.MAX_VALUE);

        model.addAttribute(BEERS, beersToDisplay);

        newSearchForUserBeersResultsPage(new BeerSearchDTO(), model, beersToDisplay,
                userDetails.getWishList());

        return "beer-wishlist-results";
    }

    @PostMapping("/wishlist/search")
    public String getWishListSearch(Principal principal, Model model,
                                    @ModelAttribute(SEARCH_BEER_DTO) BeerSearchDTO dto) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());

        List<Beer> listSearch = beerService.getUserBeerListSearch(dto.getSearchParameters(), userDetails.getWishList());
        List<DisplayBeerDTO> beersToDisplay = beerMapper.toDTO(listSearch);

        model.addAttribute(BEERS, beersToDisplay);

        newSearchForUserBeersResultsPage(new BeerSearchDTO(), model, beersToDisplay,
                userDetails.getWishList());

        return "beer-wishlist-results";
    }

    @PostMapping("/dranklist/search")
    public String getDrankListSearch(Principal principal, Model model,
                                     @ModelAttribute(SEARCH_BEER_DTO) BeerSearchDTO dto) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());

        List<Beer> listSearch = beerService.getUserBeerListSearch(dto.getSearchParameters(), userDetails.getDrankList());
        List<DisplayBeerDTO> beersToDisplay = beerMapper.toDTO(listSearch);

        model.addAttribute(BEERS, beersToDisplay);

        newSearchForUserBeersResultsPage(new BeerSearchDTO(), model, beersToDisplay,
                userDetails.getDrankList());

        return "beer-dranklist-results";
    }

    @GetMapping("/wishlist/add/{beerId}")
    public String addToWishList(@PathVariable int beerId,
                                Principal principal) {
            UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
            Beer beer = beerService.getById(beerId);
            userDetails.addToWishList(beer);
            userService.updateLists(userDetails);
            return "redirect:/list/wishlist";
    }

    @GetMapping("/profile/wishlist/add/{beerId}")
    public String addToWishListRedirectProfile(@PathVariable int beerId,
                                               Principal principal) {
            UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
            Beer beer = beerService.getById(beerId);

            userDetails.addToWishList(beer);
            userService.updateLists(userDetails);
            return "redirect:/beers/profile/{beerId}";
    }

    @GetMapping("/profile/dranklist/add/{beerId}")
    public String addToDrankListRedirectProfile(@PathVariable int beerId,
                                                Principal principal) {
            UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
            Beer beer = beerService.getById(beerId);
            userDetails.addToDrankList(beer);
            userService.updateLists(userDetails);
            return "redirect:/beers/profile/{beerId}";
    }

    @GetMapping("/dranklist")
    public String getDrankList(Principal principal, Model model) {

        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());

        List<DisplayBeerDTO> beersToDisplay = getDisplayBeerDTOS(userService, beerMapper, beerService,
                principal.getName(), DESC_NUMERIC, ID, DRANK_LIST, Integer.MAX_VALUE);

        model.addAttribute(BEERS, beersToDisplay);
        newSearchForUserBeersResultsPage(new BeerSearchDTO(), model, beersToDisplay,
                userDetails.getDrankList());
        return "beer-dranklist-results";
    }

    @GetMapping("/dranklist/add/{beerId}")
    public String addToDrankList(@PathVariable int beerId,
                                 Principal principal) {
            UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
            Beer beer = beerService.getById(beerId);
            userDetails.addToDrankList(beer);
            userService.updateLists(userDetails);
            return "redirect:/list/dranklist";

    }

    private void newSearchForUserBeersResultsPage(BeerSearchDTO searchDTO, Model model,
                                                  List<DisplayBeerDTO> beersToDisplay, Collection<Beer> userBeers) {
        model.addAttribute(STYLES, styleService.getBeerListStyles(userBeers));
        model.addAttribute(BREWERIES, breweryService.getBeerListBreweries(userBeers));
        model.addAttribute(COUNTRIES, countryService.getBeerListCountries(userBeers));
        model.addAttribute(BEERS, beersToDisplay);
        model.addAttribute(SEARCH_BEER_DTO, searchDTO);
        model.addAttribute(RATE_DTO, new RateDTO());

    }
}

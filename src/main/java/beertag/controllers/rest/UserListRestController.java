package beertag.controllers.rest;

import beertag.models.Beer;
import beertag.models.UserDetails;
import beertag.models.dto.beer.DisplayBeerDTO;
import beertag.models.mappers.BeerMapper;
import beertag.services.contracts.BeerService;
import beertag.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static beertag.Constants.UserConstants.DRANK_LIST;
import static beertag.Constants.UserConstants.WISH_LIST;
import static beertag.Utility.getDisplayBeerDTOS;

@RestController
@RequestMapping("/api/user")
public class UserListRestController {

    private final BeerService beerService;
    private final BeerMapper beerMapper;
    private final UserService userService;

    @Autowired
    public UserListRestController(BeerService beerService, BeerMapper beerMapper, UserService userService) {
        this.beerService = beerService;
        this.beerMapper = beerMapper;
        this.userService = userService;
    }

    @GetMapping("/wishlist")
    public List<DisplayBeerDTO> getWishList(Principal principal,
                                            @RequestParam(required = false) Integer desc,
                                            @RequestParam(required = false) String sortParam) {
        return getDisplayBeerDTOS(userService, beerMapper, beerService, principal.getName(),
                desc, sortParam, WISH_LIST, Integer.MAX_VALUE);
    }

    @PutMapping("/wishlist/add/{beerId}")
    public List<DisplayBeerDTO> addToWishList(@PathVariable int beerId,
                                              Principal principal) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
        Beer beer = beerService.getById(beerId);
        userDetails.addToWishList(beer);
        userService.updateLists(userDetails);
        return beerMapper.toDTO(userDetails.getWishList());
    }

    @PutMapping("/wishlist/remove/{beerId}")
    public List<DisplayBeerDTO> removeFromWishList(@PathVariable int beerId,
                                                   Principal principal) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
        Beer beer = beerService.getById(beerId);
        userDetails.removeFromWishList(beer);
        userService.updateLists(userDetails);
        return beerMapper.toDTO(userDetails.getWishList());
    }

    @GetMapping("/dranklist")
    public List<DisplayBeerDTO> getDrankList(Principal principal,
                                             @RequestParam(required = false) Integer desc,
                                             @RequestParam(required = false) String sortParam) {
        return getDisplayBeerDTOS(userService, beerMapper, beerService, principal.getName(),
                desc, sortParam, DRANK_LIST, Integer.MAX_VALUE);
    }

    @PutMapping("/dranklist/add/{beerId}")
    public List<DisplayBeerDTO> addToDrankList(@PathVariable int beerId,
                                               Principal principal) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
        Beer beer = beerService.getById(beerId);
        userDetails.addToDrankList(beer);
        userService.updateLists(userDetails);
        return beerMapper.toDTO(userDetails.getDrankList());
    }

    @PutMapping("/dranklist/remove/{beerId}")
    public List<DisplayBeerDTO> removeFromDrankList(@PathVariable int beerId,
                                                    Principal principal) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
        Beer beer = beerService.getById(beerId);
        userDetails.removeFromDrankList(beer);
        userService.updateLists(userDetails);
        return beerMapper.toDTO(userDetails.getDrankList());
    }
}

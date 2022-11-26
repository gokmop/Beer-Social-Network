package beertag.controllers.rest;

import beertag.exception.EntityNotFoundException;
import beertag.models.*;
import beertag.models.dto.beer.*;
import beertag.models.mappers.BeerMapper;
import beertag.services.contracts.BeerRatingService;
import beertag.services.contracts.BeerService;
import beertag.services.contracts.TagService;
import beertag.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

import static beertag.Utility.getRating;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {//extends Handler

    private final BeerService beerService;
    private final BeerMapper beerMapper;
    private final UserService userService;
    private final TagService tagService;
    private final BeerRatingService beerRatingService;

    @Autowired
    public BeerRestController(BeerService beerService, BeerMapper beerMapper,
                              UserService userService, TagService tagService,
                              BeerRatingService beerRatingService) {
        this.beerService = beerService;
        this.beerMapper = beerMapper;
        this.userService = userService;
        this.tagService = tagService;
        this.beerRatingService = beerRatingService;
    }

    @RequestMapping(value = "/rating", method = RequestMethod.POST)
    public BeerRating rateBeer(@RequestBody @Valid RateDTO dto,
                               Principal principal) {
            BeerRating beerRating = getRating(dto, principal.getName(), userService,
                    beerService, beerRatingService);
            beerRatingService.rateBeer(beerRating);
            return beerRating;
    }

    @PutMapping("/tag")
    public DisplayBeerDTO tagBeer(@RequestParam int beerId,
                                  @RequestParam int tagId,
                                  Principal principal) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
        Beer beer = beerService.getById(beerId);
        if (beer.getTags().size() == 5) {
            throw new ResponseStatusException(METHOD_NOT_ALLOWED, "Beer can have only 5 tags!");
        }
        Tag tag = tagService.getById(tagId);
        beerService.tagBeer(beer, tag);
        return beerMapper.toDTO(beer);
    }

    @PutMapping("/untag")
    public DisplayBeerDTO unTagBeer(@RequestParam int beerId,
                                    @RequestParam int tagId,
                                    Principal principal) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
        Beer beer = beerService.getById(beerId);
        Tag tag = tagService.getById(tagId);
        beerService.unTagBeer(beer, tag);
        return beerMapper.toDTO(beer);
    }

    @GetMapping
    public List<DisplayBeerDTO> getAll(@RequestParam(required = false) Integer desc,
                                       @RequestParam(required = false) String sortParameter) {
        List<Beer> beers = beerService.getAll(Optional.ofNullable(desc), Optional.ofNullable(sortParameter));
        return beerMapper.toDTO(beers);
    }

    @GetMapping("/{id}")
    public DisplayBeerDTO getBeerById(@PathVariable int id) {
        Beer beer = beerService.getById(id);
        return beerMapper.toDTO(beer);
    }

    @GetMapping("/search")
    public List<DisplayBeerDTO> search(@RequestBody BeerSearchDTO dto) {
        List<Beer> beers = beerService.search(dto.getSearchParameters());
        return beerMapper.toDTO(beers);
    }

    @PutMapping
    public ResponseEntity<DisplayBeerDTO> update(@Valid @RequestBody UpdateBeerDTO dto,
                                                 Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Beer beerToUpdate = beerMapper.fromDto(dto);
        beerService.update(beerToUpdate, user);
        return ResponseEntity.ok(beerMapper.toDTO(beerToUpdate));
    }

    @PostMapping
    public ResponseEntity<DisplayBeerDTO> createBeer(@Valid @RequestBody CreateBeerDTO dto,
                                                     Principal principal) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
        Beer beer = beerMapper.fromDto(dto, userDetails);
        beer.setEnabled(true);
        beerService.create(beer);
        return ResponseEntity.ok(beerMapper.getLastCreated());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeBeer(@PathVariable int id,
                                             Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        beerService.remove(id, user);
        return ResponseEntity.ok("Beer successfully removed from repository!");
    }

}

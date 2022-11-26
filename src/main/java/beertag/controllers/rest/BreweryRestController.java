package beertag.controllers.rest;

import beertag.models.Brewery;
import beertag.models.User;
import beertag.models.dto.brewery.BrewerySearchDTO;
import beertag.models.dto.brewery.CreateBreweryDTO;
import beertag.models.dto.brewery.DisplayBreweryDTO;
import beertag.models.dto.brewery.UpdateBreweryDTO;
import beertag.models.mappers.BreweryMapper;
import beertag.services.contracts.BreweryService;
import beertag.services.contracts.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/breweries")
public class BreweryRestController {

    private final BreweryService breweryService;
    private final BreweryMapper breweryMapper;
    private final UserService userService;

    public BreweryRestController(BreweryService breweryService, BreweryMapper breweryMapper, UserService userService) {
        this.breweryService = breweryService;
        this.breweryMapper = breweryMapper;
        this.userService = userService;
    }

    @GetMapping
    private List<DisplayBreweryDTO> getAll(@RequestParam(required = false) Integer desc,
                                           @RequestParam(required = false) String sortParameter) {
        return breweryMapper.toDTO(breweryService.getAll(Optional.ofNullable(desc), Optional.ofNullable(sortParameter)));
    }

    @GetMapping("/{id}")
    private DisplayBreweryDTO getBreweryById(@PathVariable int id) {
        breweryService.getById(id);
        return breweryMapper.toDTO(breweryService.getById(id));
    }

    @GetMapping("/search")
    public List<DisplayBreweryDTO> search(@RequestBody BrewerySearchDTO dto) {
        return breweryMapper.toDTO(breweryService.search(dto.getSearchParameters()));
    }

    @PutMapping
    public DisplayBreweryDTO updateBrewery(@Valid @RequestBody UpdateBreweryDTO dto,
                                           Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Brewery breweryToUpdate = breweryMapper.fromDTO(dto);
        breweryService.update(breweryToUpdate, user);
        return breweryMapper.toDTO(breweryToUpdate);
    }

    @PostMapping
    public DisplayBreweryDTO createBrewery(@Valid @RequestBody CreateBreweryDTO dto,
                                           Principal principal) {
        Brewery brewery = breweryMapper.fromDTO(dto);
        User user = userService.getUserByUsername(principal.getName());
        breweryService.create(brewery, user);
        return breweryMapper.toDTO(brewery);
    }

    //   @DeleteMapping("/{id}")
    public DisplayBreweryDTO removeBrewery(@PathVariable int id,
                                           Principal principal) {
        Brewery brewery = breweryService.getById(id);
        User user = userService.getUserByUsername(principal.getName());
        breweryService.remove(id, user);
        return breweryMapper.toDTO(brewery);
    }

}

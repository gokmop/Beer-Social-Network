package beertag.controllers.mvc;

import beertag.exception.DuplicateEntityException;
import beertag.exception.EntityNotFoundException;
import beertag.models.Brewery;
import beertag.models.User;
import beertag.models.dto.brewery.BrewerySearchDTO;
import beertag.models.dto.brewery.CreateBreweryDTO;
import beertag.models.dto.brewery.DisplayBreweryDTO;
import beertag.models.mappers.BreweryMapper;
import beertag.services.contracts.AuthorisationService;
import beertag.services.contracts.BreweryService;
import beertag.services.contracts.CountryService;
import beertag.services.contracts.UserService;
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
import static beertag.Constants.AuthorisationConstants.UPDATE;
import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Constants.ModelViewConstants.*;
import static org.springframework.http.HttpStatus.*;

@Controller
@RequestMapping("/brewery")
public class MvcBreweryController {

    private final BreweryService breweryService;
    private final BreweryMapper breweryMapper;
    private final CountryService countryService;
    private final UserService userService;
    private final AuthorisationService authorisationService;

    @Autowired
    public MvcBreweryController(BreweryService breweryService, UserService userService,
                                BreweryMapper breweryMapper, CountryService countryService,
                                AuthorisationService authorisationService) {
        this.breweryService = breweryService;
        this.countryService = countryService;
        this.breweryMapper = breweryMapper;
        this.userService = userService;
        this.authorisationService = authorisationService;
    }

    @GetMapping
    public String showAllBreweriesPage(Model model) {
        List<Brewery> breweries = breweryService.getAll(Optional.of(ASC_NUMERIC), Optional.of(RATING));
        model.addAttribute(BREWERIES, breweryMapper.toDTO(breweries));
        model.addAttribute(COUNTRIES, countryService.getAll(Optional.of(ASC_NUMERIC), Optional.of(NAME)));
        model.addAttribute(SEARCH_BREWERY_DTO, new BrewerySearchDTO());
        return "brewery-results";
    }

    @PostMapping
    public String searchForBrewery(BrewerySearchDTO searchDTO, Model model) {

        List<Brewery> breweries = breweryService.search(searchDTO.getSearchParameters());
        List<DisplayBreweryDTO> breweryDTOS = breweryMapper.toDTO(breweries);

        newSearchForBreweriesResultsPage(searchDTO, model, breweryDTOS);
        return "brewery-results";
    }

    @GetMapping("/create-brewery")
    public String getCreateBeerPage(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        authorisationService.authorise(user, String.format(ONLY_ADMINS_CAN_S_S, CREATE, BREWERIES));
        newCreateBrewery(new CreateBreweryDTO(), model);
        return "brewery-create-form";
    }

    @PostMapping("/create-brewery")
    public String handleNewCreate(Model model, Principal principal,
                                  @Valid @ModelAttribute(CREATE_BREWERY_DTO) CreateBreweryDTO dto,
                                  BindingResult bindingResult) {
        User user = userService.getUserByUsername(principal.getName());
        Brewery brewery = breweryMapper.fromDTO(dto);
        brewery.setEnabled(true);

        if (bindingResult.hasErrors()) {
            newCreateBrewery(dto, model);
            return "brewery-create-form";
        }

        try {
            breweryService.create(brewery, user);
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue(NAME, "error." + CREATE_BREWERY_DTO, e.getMessage());
            newCreateBrewery(dto, model);
            return "brewery-create-form";
        }

        showThisBrewery(model, brewery);
        return "brewery-results";
    }

    @GetMapping("/update/{id}")
    public String getEditBreweryPage(@PathVariable Integer id, Model model, Principal principal) {

        User user = userService.getUserByUsername(principal.getName());
        Brewery brewery = breweryService.getById(id);
        authorisationService.authorise(user, String.format(ONLY_ADMINS_CAN_S_S, UPDATE, BREWERIES));

        CreateBreweryDTO dto = breweryMapper.toDTO(brewery, new CreateBreweryDTO());

        newCreateBrewery(dto, model);
        model.addAttribute("id", id);

        return "brewery-create-form";
    }

    @GetMapping("/profile/{id}")
    public String getProfileBreweryPage(@PathVariable Integer id, Model model) {
        Brewery brewery = breweryService.getById(id);
        model.addAttribute(BREWERY, breweryMapper.toDTO(brewery));
        return "brewery-profile";

    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.POST})
    public String handleEditBrewery(@PathVariable int id,
                                    @Valid @ModelAttribute(CREATE_BREWERY_DTO) CreateBreweryDTO dto,
                                    BindingResult bindingResult,
                                    Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            newCreateBrewery(dto, model);
            return "brewery-create-form";
        }
        try {
            User user = userService.getUserByUsername(principal.getName());

            Brewery brewery = breweryService.getById(id);
            brewery = breweryMapper.updateFromDTO(dto, brewery);

            breweryService.update(brewery, user);
            showThisBrewery(model, brewery);

            return "brewery-results";
        } catch (UnsupportedOperationException | EntityNotFoundException | DuplicateEntityException e) {
            bindingResult.rejectValue(NAME, "error." + CREATE_BEER_DTO, e.getMessage());
            newCreateBrewery(dto, model);
            return "brewery-create-form";
        }
    }

    //   @RequestMapping("/delete/{id}")
    public String handleDeleteBrewery(@PathVariable int id, Principal principal) {
//            Brewery brewery = breweryService.getById(id);
//            showThisBrewery(model, brewery);
        User user = userService.getUserByUsername(principal.getName());
        breweryService.remove(id, user);
//            return "brewery-results";
        throw new ResponseStatusException(MOVED_PERMANENTLY, "Brewery Successfully deleted!");
    }

    private void showThisBrewery(Model model, Brewery brewery) {
        model.addAttribute(COUNTRIES, countryService.getAll(Optional.empty(), Optional.empty()));
        BrewerySearchDTO searchDTO = new BrewerySearchDTO();
        searchDTO.setId(brewery.getId());
        List<Brewery> breweries = breweryService.search(searchDTO.getSearchParameters());
        model.addAttribute(BREWERIES, breweryMapper.toDTO(breweries));
        model.addAttribute(SEARCH_BREWERY_DTO, searchDTO);
    }

    private void newCreateBrewery(CreateBreweryDTO dto, Model model) {
        model.addAttribute(COUNTRIES, countryService.getAll(Optional.empty(), Optional.empty()));
        model.addAttribute(CREATE_BREWERY_DTO, dto);
    }

    private void newSearchForBreweriesResultsPage(BrewerySearchDTO searchDTO, Model model, List<DisplayBreweryDTO> breweries) {
        model.addAttribute(COUNTRIES, countryService.getAll(Optional.of(ASC_NUMERIC), Optional.of(NAME)));
        model.addAttribute(BREWERIES, breweries);
        model.addAttribute(SEARCH_BREWERY_DTO, searchDTO);
    }

}

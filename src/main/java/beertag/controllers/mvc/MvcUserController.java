package beertag.controllers.mvc;

import beertag.exception.DuplicateEntityException;
import beertag.models.Beer;
import beertag.models.User;
import beertag.models.UserDetails;
import beertag.models.dto.beer.RateDTO;
import beertag.models.dto.user.DisplayUserDetailsDTO;
import beertag.models.dto.user.UpdateMvcUserDTO;
import beertag.models.mappers.BeerMapper;
import beertag.models.mappers.UserMapper;
import beertag.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static beertag.Constants.AuthorisationConstants.BEERS;
import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Constants.ModelViewConstants.RATE_DTO;
import static beertag.Constants.UserConstants.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/users")
public class MvcUserController {

    private final AuthorisationService authorisationService;
    private final BeerService beerService;
    private final StyleService styleService;
    private final BreweryService breweryService;
    private final BeerMapper beerMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CountryService countryService;
    private final TagService tagService;
    private final BeerRatingService beerRatingService;

    @Autowired
    public MvcUserController(AuthorisationService authorisationService, BeerService beerService,
                             StyleService styleService, BreweryService breweryService,
                             BeerMapper beerMapper, UserService userService, UserMapper userMapper, CountryService countryService,
                             TagService tagService, BeerRatingService beerRatingService) {
        this.authorisationService = authorisationService;
        this.beerService = beerService;
        this.styleService = styleService;
        this.breweryService = breweryService;
        this.beerMapper = beerMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.countryService = countryService;
        this.tagService = tagService;
        this.beerRatingService = beerRatingService;
    }

    @GetMapping("/profile")
    public String getProfileUserPage(Model model, Principal principal) {
            newProfileView(model, principal);
            return "user-profile";
    }

    @GetMapping("/profile/edit")
    public String getUserProfileEditPage(Model model, Principal principal) {

        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
        User user = userService.getUserByUsername(principal.getName());
        UpdateMvcUserDTO createUserDTO = new UpdateMvcUserDTO();
        newEditUserView(model, createUserDTO, userDetails, user);

        return "user-update-form";
    }

    @PostMapping("/profile/edit")
    public String handleEditUserPage(@Valid @ModelAttribute(UPDATE_USER_DTO) UpdateMvcUserDTO dto,
                                     BindingResult bindingResult, Model model,
                                     Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(UPDATE_USER_DTO, dto);
            return "user-update-form";
        }


        if (!dto.getPassword().equals(dto.getConfirmationPassword())) {
            bindingResult.rejectValue(CONFIRMATION_PASSWORD, "error." + UPDATE_USER_DTO, MATCH_PASSWORD);
            model.addAttribute(UPDATE_USER_DTO, dto);
            return "user-update-form";
        }

        try {
            UserDetails userToUpdate = userService.getUserDetailsByUsername(principal.getName());
            userToUpdate = userMapper.updateFromDto(userToUpdate, dto);
            User user = userService.getUserByUsername(principal.getName());
            user.setPassword(SPRING_SECURITY_TEXT_PASS + dto.getPassword());
            userService.update(userToUpdate, user);
            userService.updateUser(user);
            newProfileView(model, principal);
            return "user-profile";
        } catch (DuplicateEntityException e) {
            if (!e.getMessage().contains(EMAIL)) {
                bindingResult.rejectValue(USER_NAME, "error." + UPDATE_USER_DTO, e.getMessage());
            }
            bindingResult.rejectValue(EMAIL, "error." + UPDATE_USER_DTO, e.getMessage());
            model.addAttribute(UPDATE_USER_DTO, dto);
            return "user-update-form";
        }
    }


    private void newEditUserView(Model model, UpdateMvcUserDTO updateMvcUserDTO,
                                 UserDetails userDetails, User user) {
        updateMvcUserDTO.setFirstName(userDetails.getFirstName());
        updateMvcUserDTO.setLastName(userDetails.getLastName());
        updateMvcUserDTO.setEmail(userDetails.getEmail());
        updateMvcUserDTO.setPassword(user.getPassword());
        updateMvcUserDTO.setBirthDate(LocalDate.parse(userDetails.getBirthDate()));

        model.addAttribute(UPDATE_USER_DTO, updateMvcUserDTO);
    }

    private void newProfileView(Model model, Principal principal) {
        UserDetails userDetails = userService.getUserDetailsByUsername(principal.getName());
        DisplayUserDetailsDTO displayUserDetailsDTO = userMapper.toDto(userDetails);
        List<Beer> list = beerService.getList(userDetails, DRANK_LIST, Optional.of(DESC_NUMERIC), Optional.of(RATING), 3);
        model.addAttribute(RATE_DTO, new RateDTO());
        model.addAttribute(DISPLAY_USER_DETAILS_DTO, displayUserDetailsDTO);
        model.addAttribute(BEERS, beerMapper.toDTO(list));
    }
}

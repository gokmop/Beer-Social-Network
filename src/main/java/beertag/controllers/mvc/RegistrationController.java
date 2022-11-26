package beertag.controllers.mvc;

import beertag.exception.DuplicateEntityException;
import beertag.models.User;
import beertag.models.UserDetails;
import beertag.models.dto.user.CreateUserDTO;
import beertag.models.mappers.UserMapper;
import beertag.services.contracts.UserService;
import beertag.security.OnRegistrationCompleteEvent;
import beertag.models.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static beertag.Constants.HqlQueriesConstants.USER_NAME;
import static beertag.Constants.UserConstants.*;


@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserDetailsManager userDetailsManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final MessageSource messages;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegistrationController(UserDetailsManager userDetailsManager,
                                  UserService userService,
                                  UserMapper userMapper,
                                  @Qualifier("messageSource") MessageSource messages,
                                  ApplicationEventPublisher eventPublisher) {
        this.userDetailsManager = userDetailsManager;
        this.userService = userService;
        this.userMapper = userMapper;
        this.messages = messages;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public String showRegisterPage(Model model) {
        model.addAttribute(CREATE_USER_DTO, new CreateUserDTO());
        return "register";
    }

    @PostMapping
    public String registerUser(
            @Valid @ModelAttribute(CREATE_USER_DTO) CreateUserDTO user,
            HttpServletRequest request,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(CREATE_USER_DTO, user);
            return "register";
        }

        if (userDetailsManager.userExists(user.getUserName())) {
            model.addAttribute(CREATE_USER_DTO, user);
            bindingResult.rejectValue(USER_NAME, "error." + CREATE_USER_DTO, DUPLICATE_USERNAME_ERROR);
            return "register";
        }

        if (!user.getPassword().equals(user.getConfirmationPassword())) {
            model.addAttribute(CREATE_USER_DTO, user);
            bindingResult.rejectValue(CONFIRMATION_PASSWORD, "error." + CREATE_USER_DTO, MATCH_PASSWORD);
            return "register";
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        org.springframework.security.core.userdetails.User inactiveUser =
                new org.springframework.security.core.userdetails.User(user.getUserName(),
                        SPRING_SECURITY_TEXT_PASS + user.getPassword(),
                        true,
                        true,
                        true,
                        true,
                        authorities);
        try {
            UserDetails newDetails = userMapper.fromDto(user);
            userService.ensureEmailNotTaken(newDetails, "");
            userDetailsManager.createUser(inactiveUser);
            userService.create(newDetails);

            User beertagUser = userService.getUserByUsername(inactiveUser.getUsername());
            beertagUser.setEnabled(false);
            userService.updateUser(beertagUser);

            String appUrl = request.getContextPath();
            OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(beertagUser,
                    request.getLocale(), appUrl);
            eventPublisher.publishEvent(event);
            return "email-confirmation";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue(USER_NAME, "error." + CREATE_USER_DTO, e.getMessage());
            model.addAttribute(CREATE_USER_DTO, user);
            model.addAttribute("errors", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/confirm")
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "badUser";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "badUser";
        }
        user.setEnabled(true);
        userService.updateUser(user);
        return "register-confirmation";
    }

}

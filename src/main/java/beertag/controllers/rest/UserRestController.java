package beertag.controllers.rest;

import beertag.models.User;
import beertag.models.UserDetails;
import beertag.models.dto.user.*;
import beertag.models.mappers.UserMapper;
import beertag.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserDetailsManager userDetailsManager;
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper, UserDetailsManager userDetailsManager) {
        this.userService = userService;
        this.userDetailsManager = userDetailsManager;
        this.userMapper = userMapper;
    }

    @GetMapping
    private List<DisplayUserDetailsDTO> getAll(@RequestParam(required = false) Integer desc,
                                               @RequestParam(required = false) String sortParameter) {
        List<UserDetails> userDetailsList = userService.getAll(Optional.ofNullable(desc), Optional.ofNullable(sortParameter));
        return userMapper.toDto(userDetailsList);
    }

    @GetMapping("/{id}")
    private DisplayUserDetailsDTO getUserById(@PathVariable int id) {
            return userMapper.toDto(userService.getById(id));
    }

    @GetMapping("/search")
    public List<DisplayUserDetailsDTO> search(@RequestBody @Valid UserSearchDetailsDTO dto) {
        return userMapper.toDto(userService.search(dto.getSearchParameters()));

    }

    @PutMapping
    public DisplayUserDetailsDTO update(@Valid @RequestBody UpdateUserDetailsDTO dto,
                                        Principal principal) {
            dto.setUserName(principal.getName());
            UserDetails userDetailsToUpdate = userMapper.fromDto(dto);
            User user = userService.getUserByUsername(principal.getName());

            userService.update(validUser(userDetailsToUpdate), user);
            return userMapper.toDto(userDetailsToUpdate);
    }

    @PostMapping("/register")
    public DisplayUserDetailsDTO createUser(@Valid @RequestBody CreateUserDTO regDto) {

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        org.springframework.security.core.userdetails.User newUser =
                new org.springframework.security.core.userdetails.User(
                        regDto.getUserName(), "{noop}" + regDto.getPassword(), authorities);

            UserDetails newDetails = userMapper.fromDto(regDto);
            newDetails.setEnabled(true);

            userService.ensureEmailNotTaken(newDetails, "");

            userDetailsManager.createUser(newUser);

            userService.create(newDetails);
            return userMapper.toDto(newDetails);
    }

    //  @DeleteMapping("/{id}")
    public DisplayUserDetailsDTO deleteUserById(@PathVariable int id,
                                                Principal principal) {
            UserDetails userDetailsToRemove = userService.getById(id);
            User user = userService.getUserByUsername(principal.getName());
            userService.remove(id, user);
            return userMapper.toDto(userDetailsToRemove);
    }

    private UserDetails validUser(@Valid UserDetails userDetails) {
        return userDetails;
    }
}

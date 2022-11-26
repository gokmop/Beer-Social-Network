package beertag.models.mappers;

import beertag.models.User;
import beertag.models.UserDetails;
import beertag.models.dto.user.CreateUserDTO;
import beertag.models.dto.user.DisplayUserDetailsDTO;
import beertag.models.dto.user.UpdateMvcUserDTO;
import beertag.models.dto.user.UpdateUserDetailsDTO;
import beertag.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public UserDetails fromDto(CreateUserDTO dto) {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(dto.getFirstName());
        userDetails.setLastName(dto.getLastName());
        userDetails.setUserName(dto.getUserName());
        userDetails.setEmail(dto.getEmail());
        userDetails.setBirthDate(dto.getBirthDate());
        userDetails.setProfilePic(dto.getProfilePic());
        return userDetails;
    }

    public UserDetails fromDto(UpdateUserDetailsDTO dto) {
        UserDetails userDetailsToUpdate = userService.getUserDetailsByUsername(dto.getUserName());
        dto.getFirstName().ifPresentOrElse(userDetailsToUpdate::setFirstName, () -> userDetailsToUpdate.setFirstName(userDetailsToUpdate.getFirstName()));
        dto.getLastName().ifPresentOrElse(userDetailsToUpdate::setLastName, () -> userDetailsToUpdate.setLastName(userDetailsToUpdate.getLastName()));
        dto.getEmail().ifPresentOrElse(userDetailsToUpdate::setEmail, () -> userDetailsToUpdate.setEmail(userDetailsToUpdate.getEmail()));
        dto.getBirthDate().ifPresentOrElse(userDetailsToUpdate::setBirthDate, () -> userDetailsToUpdate.setBirthDate(LocalDate.parse(userDetailsToUpdate.getBirthDate())));
        dto.getProfilePic().ifPresentOrElse(userDetailsToUpdate::setProfilePic, () -> userDetailsToUpdate.setProfilePic(userDetailsToUpdate.getProfilePic()));
        return userDetailsToUpdate;
    }

    public DisplayUserDetailsDTO toDto(UserDetails userDetails) {
        DisplayUserDetailsDTO dto = new DisplayUserDetailsDTO();
        dto.setBirthDate(userDetails.getBirthDate());
        dto.setFirstName(userDetails.getFirstName());
        dto.setLastName(userDetails.getLastName());
        dto.setUserName(userDetails.getUserName());
        dto.setEmail(userDetails.getEmail());
        User user = userService.getUserByUsername(userDetails.getUserName());
        dto.setUserRoles(user.getAuthorities());
        return dto;
    }

    public List<DisplayUserDetailsDTO> toDto(Collection<UserDetails> userDetails) {
        return userDetails.stream().map(this::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public UserDetails updateFromDto(UserDetails userDetails, UpdateMvcUserDTO dto) {
        userDetails.setFirstName(dto.getFirstName());
        userDetails.setLastName(dto.getLastName());
        userDetails.setEmail(dto.getEmail());
        userDetails.setBirthDate(dto.getBirthDate());
        return userDetails;
    }
}

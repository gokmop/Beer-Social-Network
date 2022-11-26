package beertag.models.dto.user;

import beertag.annotations.MaxAge;
import beertag.annotations.MinAge;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Optional;

import static beertag.Constants.UserConstants.*;
import static beertag.Constants.UserConstants.USER_OLDER_THAN_LEGAL_YEARS_ERROR;

@Scope("prototype")
public class UpdateUserDetailsDTO {
    //TODO deprecate

    private int id;
    @Size(min = 5, max = 20, message = "First name should be between 5 & 20 symbols")
    private String firstName;
    @Size(min = 3, max = 20, message = "Last name should be between 3 & 20 symbols")
    private String lastName;
    @Size(min = 5, max = 20, message = "Username should be between 5 & 20 symbols")
    private String userName;
    @Email(regexp = "^(.+)@(.+)$")
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @MinAge(value = LEGAL_YEARS_MIN, message = USER_YOUNGER_THAN_LEGAL_YEARS_ERROR)
    @MaxAge(value = LEGAL_YEARS_MAX, message = USER_OLDER_THAN_LEGAL_YEARS_ERROR)
    private LocalDate birthDate;
    private String profilePic;

    public UpdateUserDetailsDTO() {
    }

    public int getId() {
        return id;
    }

    public Optional<String> getFirstName() {
        return Optional.ofNullable(firstName);
    }

    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }

    public String getUserName() {
        return userName;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<LocalDate> getBirthDate() {
        return Optional.of(birthDate);
    }

    public Optional<String> getProfilePic() {
        return Optional.ofNullable(profilePic);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

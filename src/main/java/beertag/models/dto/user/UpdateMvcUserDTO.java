package beertag.models.dto.user;


import beertag.annotations.MaxAge;
import beertag.annotations.MinAge;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static beertag.Constants.UserConstants.*;

@Scope("prototype")
public class UpdateMvcUserDTO {

    private int id;

    @NotBlank
    @Size(min = 5, max = 20, message = "First name should be between 5 & 20 symbols")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20, message = "Last name should be between 3 & 20 symbols")
    private String lastName;

    @NotBlank
    @Size(min = 6, message = "Password too short!")
    private String password;

    @NotBlank
    @Size(min = 6, message = "Password too short!")
    private String confirmationPassword;

    @NotBlank
    @Email(regexp = "^(.+)@(.+)$")
    private String email;

    @NotNull(message = "Birthdate cannot be blank!")
    @MinAge(value = LEGAL_YEARS_MIN, message = USER_YOUNGER_THAN_LEGAL_YEARS_ERROR)
    @MaxAge(value = LEGAL_YEARS_MAX, message = USER_OLDER_THAN_LEGAL_YEARS_ERROR)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String profilePic;

    public UpdateMvcUserDTO() {
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }
}

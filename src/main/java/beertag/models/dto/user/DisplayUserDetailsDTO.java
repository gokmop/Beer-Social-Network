package beertag.models.dto.user;

import beertag.models.UserAuthority;

import java.util.Set;

public class DisplayUserDetailsDTO {

    private String firstName;
    private String lastName;
    private String userName;
    private String birthDate;
    private String email;
    private Set<UserAuthority> userRoles;

    public DisplayUserDetailsDTO() {
    }

    public Set<UserAuthority> getUserRoles() {
        return userRoles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setUserRoles(Set<UserAuthority> userRoles) {
        this.userRoles = userRoles;
    }
}

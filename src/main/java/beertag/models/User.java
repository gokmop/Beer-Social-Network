package beertag.models;

import beertag.enums.UserRoles;
import beertag.models.contracts.SoftDeletable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

import static beertag.Constants.UserConstants.USER_SOFT_DELETE;

@Entity(name = "User")
@Table(name = "users")
public class User extends SoftDeletable {

    @Id
    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinTable(
            name = "user_details",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private UserDetails userDetails;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private Set<UserAuthority> authorities;

    public User() {
    }

    @JsonIgnore
    public boolean notAdmin() {
        UserAuthority adminAuthority = new UserAuthority();
        adminAuthority.setAuthority(UserRoles.ROLE_ADMIN.name());
        adminAuthority.setUserName(this.userName);
        return !authorities.contains(adminAuthority);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public Set<UserAuthority> getAuthorities() {
        return authorities;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.userDetails.setEnabled(enabled);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public void setAuthorities(Set<UserAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) &&
                Objects.equals(password, user.password) &&
                Objects.equals(userDetails, user.userDetails) &&
                Objects.equals(authorities, user.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, userDetails, authorities);
    }

    @Override
    public String toString() {
        return userName;
    }
}

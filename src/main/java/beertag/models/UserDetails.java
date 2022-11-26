package beertag.models;


import beertag.annotations.MaxAge;
import beertag.annotations.MinAge;
import beertag.models.contracts.Searchable;
import beertag.models.contracts.SoftDeletable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static beertag.Constants.UserConstants.*;
import static beertag.Constants.UserConstants.USER_OLDER_THAN_LEGAL_YEARS_ERROR;

@Entity(name = "UserDetails")
@Table(name = "user_details")
//@Loader(namedQuery = "findUserDetailsById")
//@NamedQuery(name = "findUserDetailsById", query = USER_DETAILS_LOAD)
//@SQLDelete(sql = USER_DETAILS_SOFT_DELETE, check = ResultCheckStyle.COUNT)
//@Where(clause = "enabled = 1")
public class UserDetails extends SoftDeletable implements Searchable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    @NotBlank
    @Size(min = 5, max = 20, message = "First name should be between 5 & 20 symbols")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20, message = "Last name should be between 3 & 20 symbols")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Size(min = 5, max = 20, message = "Username should be between 5 & 20 symbols")
    @Column(name = "username")
    private String userName;

    @NotBlank
    @Email(regexp = "^(.+)@(.+)$")
    @Column(name = "email")
    private String email;

    @NotNull
    @MinAge(value = LEGAL_YEARS_MIN, message = USER_YOUNGER_THAN_LEGAL_YEARS_ERROR)
    @MaxAge(value = LEGAL_YEARS_MAX, message = USER_OLDER_THAN_LEGAL_YEARS_ERROR)
    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "profile_picture")
    private String profilePic;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "beer_wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id")
    )
    private Set<Beer> wishList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "drank_list",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id")
    )
    private Set<Beer> drankList;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "beer_user_relation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id")
    )
    private Set<Beer> createdBeers;

    public UserDetails() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void addToWishList(Beer beer) {
        if (drankList.contains(beer)) return;
        wishList.add(beer);
    }

    public void removeFromWishList(Beer beer) {
        wishList.remove(beer);
    }

    public void addToDrankList(Beer beer) {
        wishList.remove(beer);
        drankList.add(beer);
    }

    public void removeFromDrankList(Beer beer) {
        drankList.remove(beer);
    }

    public Set<Beer> getCreatedBeers() {
        return new HashSet<>(createdBeers);
    }

    @JsonIgnore
    public Set<Beer> getWishList() {
        return new HashSet<>(wishList);
    }

    @JsonIgnore
    public Set<Beer> getDrankList() {
        return new HashSet<>(drankList);
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

    public String getProfilePic() {
        return profilePic;
    }

    public int getId() {
        return id;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) return;
        this.birthDate = birthDate.toString();
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setCreatedBeers(Set<Beer> createdBeers) {
        this.createdBeers = createdBeers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetails userDetails = (UserDetails) o;
        return id == userDetails.id &&
                Objects.equals(firstName, userDetails.firstName) &&
                Objects.equals(lastName, userDetails.lastName) &&
                Objects.equals(userName, userDetails.userName) &&
                Objects.equals(email, userDetails.email) &&
                Objects.equals(birthDate, userDetails.birthDate) &&
                Objects.equals(profilePic, userDetails.profilePic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, userName, email, birthDate, profilePic);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }
}

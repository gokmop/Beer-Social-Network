package beertag.models;

import beertag.models.contracts.HasRating;
import beertag.models.contracts.Searchable;
import beertag.models.contracts.SoftDeletable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

import static beertag.Constants.BreweryConstants.AVG_BEERS_MADE_RATING;
import static beertag.Constants.BreweryConstants.COUNT_BEERS_MADE;

@Entity(name = "Brewery")
@Table(name = "breweries")
@Loader(namedQuery = "findBreweryById")
@NamedQuery(name = "findBreweryById", query =
        "SELECT b FROM Brewery b " +
                "WHERE " +
                "    b.id = ?1 AND " +
                "    b.enabled = true")
@SQLDelete(sql = "UPDATE breweries SET enabled = '0' WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "enabled = 1")
public class Brewery extends SoftDeletable implements HasRating, Searchable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 5, max = 30, message = "Brewery name should be between 5 and 30 characters.")
    @Column(name = "name")
    private String name;

    @NotBlank
    @Email(regexp = "^(.+)@(.+)$")
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(min = 20, max = 100, message = "Address should be between 20 and 100 characters.")
    @Column(name = "address_text")
    private String address;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "brewery_beer_relation",
            joinColumns = @JoinColumn(name = "brewery_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id")
    )
    private Set<Beer> madeByThisBeers;

    @Formula(AVG_BEERS_MADE_RATING)
    private Double rating;

    @Formula(COUNT_BEERS_MADE)
    private int beersMade;

    public Brewery() {
    }

    @JsonIgnore
    public String getCountryName() {
        return country.getName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public Country getCountry() {
        return country;
    }

    public Set<Beer> getMadeByThisBeers() {
        return new HashSet<>(madeByThisBeers);
    }

    public Double getRating() {
        return rating;
    }

    public int getBeersMade() {
        return beersMade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Brewery{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", country=" + country +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brewery brewery = (Brewery) o;
        return name.equals(brewery.name) &&
                address.equals(brewery.address) &&
                country.equals(brewery.country) &&
                email.equals(brewery.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, country, email);
    }
}

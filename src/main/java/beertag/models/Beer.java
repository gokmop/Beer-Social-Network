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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static beertag.Constants.BeerConstants.AVG_BEER_RATING;
import static beertag.Constants.BeerConstants.TIMES_BEER_RATED;

@Entity(name = "Beer")
@Table(name = "beers")
@Loader(namedQuery = "findBeerById")
@NamedQuery(name = "findBeerById", query =
        "SELECT b " +
                "FROM Beer b " +
                "WHERE " +
                "    b.id = ?1 AND " +
                "    b.enabled = true")
@SQLDelete(sql = "UPDATE beers SET enabled = '0' WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "enabled = 1")
public class Beer extends SoftDeletable implements Comparable<Beer>, HasRating, Searchable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "alcohol_content")
    private double abv;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String picture;

    @ManyToOne
    @JoinColumn(name = "style_id")
    private Style style;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_user_id")
    private UserDetails userDetails;

    @ManyToOne
    @JoinColumn(name = "brewery_id")
    private Brewery brewery;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tag_beer_relation",
            joinColumns = @JoinColumn(name = "beer_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @Basic(fetch = FetchType.EAGER)
    @Formula(AVG_BEER_RATING)
    private Double rating;

    @Basic(fetch = FetchType.EAGER)
    @Formula(TIMES_BEER_RATED)
    private Integer timesRated;

    public Beer() {

    }

    public Integer getTimesRated() {
        return timesRated;
    }

    public Double getRating() {
        return rating;
    }

    @JsonIgnore
    public String getStyleName() {
        return style.getName();
    }

    @JsonIgnore
    public int getCountryId() {
        return brewery.getCountry().getId();
    }

    @JsonIgnore
    public int getAuthorId() {
        return userDetails.getId();
    }

    @JsonIgnore
    public String getAuthorUsername() {
        return userDetails.getUserName();
    }

    @JsonIgnore
    public String getBreweryName() {
        return brewery.getName();
    }

    @JsonIgnore
    public String getCountryName() {
        return brewery.getCountryName();
    }

    public Set<Tag> getTags() {
        if (tags == null) return new HashSet<>();
        return new HashSet<>(tags);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    @JsonIgnore
    public UserDetails getAuthor() {
        return userDetails;
    }

    public Brewery getBrewery() {
        return brewery;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public Style getStyle() {
        return style;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAbv() {
        return abv;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public void setAuthor(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public void setBrewery(Brewery brewery) {
        this.brewery = brewery;
    }

    @Override
    public String toString() {
        return "Beer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abv=" + abv +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", style=" + style +
                ", createdBy=" + userDetails +
                ", brewery=" + brewery +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beer beer = (Beer) o;
        return id == beer.id &&
                Double.compare(beer.abv, abv) == 0 &&
                Objects.equals(name, beer.name) &&
                Objects.equals(description, beer.description) &&
                Objects.equals(style, beer.style) &&
                Objects.equals(brewery, beer.brewery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, abv, description,
                style, brewery);
    }

    @Override
    public int compareTo(Beer o) {
        return Double.compare(this.getRating(), o.getRating());
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @JsonIgnore
    public String getTagsAsString() {
        return tags.toString();
    }

    @JsonIgnore
    public String getBreweryEmail() {
        return brewery.getEmail();
    }

    @JsonIgnore
    public String getBreweryAddress() {
        return brewery.getAddress();
    }
}


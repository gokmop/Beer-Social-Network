package beertag.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "beer_ratings")
public class BeerRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "beer_id")
    private Beer beer;

    @Column(name = "rating")
    private int rating;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "rated_by_user_id")
    private UserDetails userDetails;

    public BeerRating() {
    }

    public BeerRating(int id, Beer beer, int rating, UserDetails userDetails) {
        this.id = id;
        this.beer = beer;
        this.rating = rating;
        this.userDetails = userDetails;
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public int getBeerId() {
        return beer.getId();
    }

    @JsonIgnore
    public int getUserId() {
        return userDetails.getId();
    }

    @JsonIgnore
    public Beer getBeer() {
        return beer;
    }

    public int getRating() {
        return rating;
    }

    @JsonIgnore
    public UserDetails getUser() {
        return userDetails;
    }

    public String getUserName() {
        return userDetails.getUserName();
    }

    public String getSparseBeerDetails() {
        StringBuilder strB = new StringBuilder(getBeer().getName());
        strB.append(' ').append(getBeer().getAbv()).append(' ').append(getBeer().getStyleName());
        return new String(strB);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setUser(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeerRating that = (BeerRating) o;
        return beer.equals(that.beer) &&
                userDetails.equals(that.userDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beer, userDetails);
    }
}

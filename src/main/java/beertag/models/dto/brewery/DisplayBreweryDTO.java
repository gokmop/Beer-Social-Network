package beertag.models.dto.brewery;

import beertag.models.dto.beer.DisplayBeerDTO;
import java.util.Optional;

public class DisplayBreweryDTO {

    private int id;
    private String name;
    private String address;
    private String description;
    private String country;
    private String beersMade;
    private String email;
    private String avgBeerRating;
    private DisplayBeerDTO topBeer;

    public DisplayBreweryDTO() {
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getBeersMade() {
        return beersMade;
    }


    public String getAvgBeerRating() {
        return avgBeerRating;
    }

    public DisplayBeerDTO getTopBeer() {
        return topBeer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBeersMade(Optional<Integer> beersMade) {
        if (beersMade.isPresent()) {
            this.beersMade = String.valueOf(beersMade.get());
        } else {
            this.beersMade = "no beers made";
        }
    }

    public void setAvgBeerRating(Optional<Double> avgBeerRating) {
        if (avgBeerRating.isPresent()) {
            this.avgBeerRating = String.format("%.2f", avgBeerRating.get());
        } else {
            this.avgBeerRating = "no rated beers";
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTopBeer(DisplayBeerDTO topBeer) {
        this.topBeer = topBeer;
    }
}

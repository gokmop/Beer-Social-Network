package beertag.models.dto.beer;


import beertag.models.Tag;

import java.util.Optional;
import java.util.Set;

import static beertag.Constants.BeerConstants.NOT_RATED;
import static beertag.Constants.BeerConstants.NO_TAGS;

public class DisplayBeerDTO {

    private int id;
    private String name;
    private String styleName;
    private String breweryName;
    private String description;
    private String abv;
    private String rating;
    private String timesRated;
    private String author;
    private String countryName;
    private String tags;


    public DisplayBeerDTO() {
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getStyleName() {
        return styleName;
    }

    public String getBreweryName() {
        return breweryName;
    }

    public String getAbv() {
        return abv;
    }

    public String getRating() {
        return rating;
    }

    public String getTimesRated() {
        return timesRated;
    }

    public String getCountryName() {
        return countryName;
    }



    public String getTags() {
        return tags;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setBreweryName(String breweryName) {
        this.breweryName = breweryName;
    }

    public void setAbv(double abv) {
        this.abv = String.format("%.1f", abv);
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(Optional<Double> rating) {
        if (rating.isPresent()) {
            this.rating = String.format("%.1f", rating.get());
        } else {
            this.rating = NOT_RATED;
        }
    }

    public void setTimesRated(Optional<Integer> timesRated) {
        if (timesRated.isPresent() && timesRated.get() != 0) {
            this.timesRated = String.format("%d", timesRated.get());
        } else {
            this.timesRated = NOT_RATED;
        }
    }

    public void setTags(Set<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuilder strB = new StringBuilder();
            for (Tag tag : tags) {
                strB.append(tag.getName()).append(", ");
            }
            this.tags = strB.substring(0, strB.length() - 2);
        } else {
            this.tags = NO_TAGS;
        }
    }
}

package beertag.models.dto.beer;


import org.springframework.context.annotation.Scope;

import javax.validation.constraints.*;

@Scope("prototype")
public class CreateBeerDTO {

    @NotBlank
    @Size(min = 2, max = 20, message = "Name should be between 2 & 20 symbols")
    private String name;

    @PositiveOrZero(message = "ABV cannot be a negative number")
    @Max(value = 50, message = "ABV should be below 50")
    private double abv;

    private String description;

    private String picture;

    @Positive
    private int styleId;

    @Positive
    private int breweryId;

    public CreateBeerDTO() {
    }

    public String getName() {
        return name;
    }

    public double getAbv() {
        return abv;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public int getStyleId() {
        return styleId;
    }


    public int getBreweryId() {
        return breweryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public void setBreweryId(int breweryId) {
        this.breweryId = breweryId;
    }
}

package beertag.models.dto.beer;

import beertag.annotations.ValidRating;

public class RateDTO {

    private int beerId;
    private String redirect = "index";

    @ValidRating(max = 5, message = "Rating must be between 0 and 5!")
    private int rating;

    public RateDTO() {
    }

    public int getBeerId() {
        return beerId;
    }

    public int getRating() {
        return rating;
    }

    public void setBeerId(int beerId) {
        this.beerId = beerId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getRedirect() {
        return redirect;
    }
}

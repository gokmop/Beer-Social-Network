package beertag.services.contracts;

import beertag.models.BeerRating;

public interface BeerRatingService {

    void rateBeer(BeerRating beerRating);

    BeerRating getByBeerIdAndUserID(int beerId, int userId);
}

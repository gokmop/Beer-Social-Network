package beertag.repositories.contracts;

import beertag.models.BeerRating;
import beertag.repositories.contracts.generic.CreateRepo;
import beertag.repositories.contracts.generic.CheckExists;
import beertag.repositories.contracts.generic.UpdateRemoveRepo;

public interface BeerRatingRepository extends CreateRepo<BeerRating>,
        CheckExists<BeerRating>, UpdateRemoveRepo<BeerRating> {

    BeerRating getBeerRatingByUserAndBeer(int beerId, int userId);
}

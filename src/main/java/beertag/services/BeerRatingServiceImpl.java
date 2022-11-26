package beertag.services;

import beertag.models.BeerRating;
import beertag.repositories.contracts.BeerRatingRepository;
import beertag.services.contracts.AuthorisationService;
import beertag.services.contracts.BeerRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeerRatingServiceImpl implements BeerRatingService {

    private final BeerRatingRepository beerRatingRepository;
    private final AuthorisationService authorisationService;

    @Autowired
    public BeerRatingServiceImpl(BeerRatingRepository beerRatingRepository,
                                 AuthorisationService authorisationService) {
        this.beerRatingRepository = beerRatingRepository;
        this.authorisationService = authorisationService;
    }

    @Override
    public void rateBeer(BeerRating beerRating) {
        if (beerRatingRepository.checkExists(beerRating)) {
            beerRatingRepository.update(beerRating);
        } else {
            beerRatingRepository.create(beerRating);
        }
    }

    @Override
    public BeerRating getByBeerIdAndUserID(int beerId, int userId) {
        return beerRatingRepository.getBeerRatingByUserAndBeer(beerId, userId);
    }
}

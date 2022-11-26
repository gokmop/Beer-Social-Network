package beertag.models.mappers;

import beertag.models.Beer;
import beertag.models.UserDetails;
import beertag.models.dto.beer.CreateBeerDTO;
import beertag.models.dto.beer.DisplayBeerDTO;
import beertag.models.dto.beer.UpdateBeerDTO;
import beertag.services.contracts.BeerService;
import beertag.services.contracts.BreweryService;
import beertag.services.contracts.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BeerMapper {

    private final StyleService styleService;
    private final BeerService beerService;
    private final BreweryService breweryService;
    private Beer lastCreated;

    @Autowired
    public BeerMapper(StyleService styleService, BeerService beerService, BreweryService breweryService) {
        this.styleService = styleService;
        this.beerService = beerService;
        this.breweryService = breweryService;
    }

    public Beer fromDto(CreateBeerDTO dto, UserDetails userDetails) {
        Beer beer = new Beer();
        beer.setName(dto.getName());
        beer.setAbv(dto.getAbv());
        beer.setDescription(dto.getDescription());
        beer.setPicture(dto.getPicture());
        beer.setStyle(styleService.getById(dto.getStyleId()));
        beer.setBrewery(breweryService.getById(dto.getBreweryId()));
        beer.setAuthor(userDetails);
        lastCreated = beer;
        return beer;
    }

    public Beer fromDto(UpdateBeerDTO dto) {
        Beer beerToUpdate = beerService.getById(dto.getId());
        dto.getName().ifPresent(beerToUpdate::setName);
        dto.getAbv().ifPresent(beerToUpdate::setAbv);
        dto.getDescription().ifPresent(beerToUpdate::setDescription);
        dto.getStyleId().ifPresent(i -> beerToUpdate.setStyle(styleService.getById(i)));
        dto.getBreweryId().ifPresent(i -> beerToUpdate.setBrewery(breweryService.getById(i)));
        dto.getPicture().ifPresent(beerToUpdate::setPicture);
        return beerToUpdate;
    }

    public CreateBeerDTO toDTO(Beer beer, CreateBeerDTO dto) {
        dto.setAbv(beer.getAbv());
        dto.setName(beer.getName());
        dto.setStyleId(beer.getStyle().getId());
        dto.setBreweryId(beer.getBrewery().getId());
        dto.setDescription(beer.getDescription());
        dto.setPicture(beer.getPicture());
        return dto;
    }

    public DisplayBeerDTO toDTO(Beer beer) {
        DisplayBeerDTO dto = new DisplayBeerDTO();
        dto.setId(beer.getId());
        dto.setName(beer.getName());
        dto.setStyleName(beer.getStyleName());
        dto.setBreweryName(beer.getBreweryName());
        dto.setAbv(beer.getAbv());
        dto.setRating(Optional.ofNullable(beer.getRating()));
        dto.setTimesRated(Optional.ofNullable(beer.getTimesRated()));
        dto.setAuthor(beer.getAuthorUsername());
        dto.setCountryName(beer.getCountryName());
        dto.setTags(beer.getTags());
        dto.setDescription(beer.getDescription());
        return dto;
    }

    public DisplayBeerDTO getLastCreated() {
        return toDTO(lastCreated);
    }

    public List<DisplayBeerDTO> toDTO(Collection<Beer> beers) {
        return beers.stream().map(this::toDTO).collect(Collectors.toCollection(LinkedList::new));
    }

    public Beer updateFromDTO(CreateBeerDTO dto, Beer beer) {

        beer.setName(dto.getName());
        beer.setAbv(dto.getAbv());
        beer.setStyle(styleService.getById(dto.getStyleId()));
        beer.setBrewery(breweryService.getById(dto.getBreweryId()));

        beer.setDescription(dto.getDescription());
        beer.setPicture(dto.getPicture());
        return beer;
    }
}

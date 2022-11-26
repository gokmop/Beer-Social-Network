package beertag.models.mappers;

import beertag.models.Brewery;
import beertag.models.dto.brewery.CreateBreweryDTO;
import beertag.models.dto.brewery.DisplayBreweryDTO;
import beertag.models.dto.brewery.UpdateBreweryDTO;
import beertag.services.contracts.BreweryService;
import beertag.services.contracts.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BreweryMapper {

    private final CountryService countryService;
    private final BreweryService breweryService;
    private final BeerMapper beerMapper;

    @Autowired
    public BreweryMapper(CountryService countryService, BeerMapper beerMapper, BreweryService breweryService) {
        this.countryService = countryService;
        this.breweryService = breweryService;
        this.beerMapper = beerMapper;
    }


    public Brewery fromDTO(CreateBreweryDTO dto) {
        Brewery brewery = new Brewery();
        brewery.setName(dto.getName());
        brewery.setEmail(dto.getEmail());
        brewery.setDescription(dto.getDescription());
        brewery.setAddress(dto.getAddress());
        brewery.setCountry(countryService.getById(dto.getCountryId()));
        return brewery;
    }

    public Brewery fromDTO(UpdateBreweryDTO dto) {
        Brewery breweryToUpdate = breweryService.getById(dto.getId());
        dto.getName().ifPresent(breweryToUpdate::setName);
        dto.getEmail().ifPresent(breweryToUpdate::setEmail);
        dto.getAddress().ifPresent(breweryToUpdate::setAddress);
        dto.getDescription().ifPresent(breweryToUpdate::setDescription);
        dto.getCountryId().ifPresent(i -> breweryToUpdate.setCountry(countryService.getById(i)));
        return breweryToUpdate;
    }

    public DisplayBreweryDTO toDTO(Brewery brewery) {
        DisplayBreweryDTO dto = new DisplayBreweryDTO();
        dto.setId(brewery.getId());
        dto.setName(brewery.getName());
        dto.setEmail(brewery.getEmail());
        dto.setDescription(brewery.getDescription());
        dto.setAddress(brewery.getAddress());
        dto.setCountry(brewery.getCountryName());
        dto.setAvgBeerRating(Optional.ofNullable(brewery.getRating()));
        dto.setBeersMade(Optional.of(brewery.getBeersMade()));
        //TODO brewery.getTopBeer().ifPresent(b -> dto.setTopBeer(beerMapper.toDTO(b)));
        return dto;
    }

    public List<DisplayBreweryDTO> toDTO(Collection<Brewery> breweries) {
        return breweries.stream().map(this::toDTO).collect(Collectors.toCollection(LinkedList::new));
    }

    public Brewery updateFromDTO(CreateBreweryDTO dto, Brewery brewery) {

        brewery.setName(dto.getName());
        brewery.setAddress(dto.getAddress());
        brewery.setCountry(countryService.getById(dto.getCountryId()));
        brewery.setEmail(dto.getEmail());
        brewery.setDescription(dto.getDescription());
        return brewery;
    }

    public CreateBreweryDTO toDTO(Brewery brewery, CreateBreweryDTO dto) {
        dto.setId(brewery.getId());
        dto.setName(brewery.getName());
        dto.setEmail(brewery.getEmail());
        dto.setAddress(brewery.getAddress());
        dto.setDescription(brewery.getDescription());
        dto.setCountryId(brewery.getCountry().getId());
        return dto;
    }
}

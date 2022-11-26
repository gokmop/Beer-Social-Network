package beertag.controllers.rest;

import beertag.models.Country;
import beertag.services.contracts.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/country")
public class CountryRestController {

    private final CountryService countryService;

    @Autowired
    public CountryRestController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getAll(@RequestParam(required = false) Integer desc,
                                @RequestParam(required = false) String sortParameter) {
        return countryService.getAll(Optional.ofNullable(desc), Optional.ofNullable(sortParameter));
    }

    @GetMapping("/{id}")
    public List<Country> getById(@PathVariable int id) {
            return Collections.singletonList(countryService.getById(id));

    }

}

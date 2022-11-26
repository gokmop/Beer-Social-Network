package beertag.models.dto.brewery;


import org.springframework.context.annotation.Scope;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Optional;

public class UpdateBreweryDTO {

    @Positive
    private final int id;

    @NotBlank
    @Size(min = 5, max = 30, message = "Brewery name should be between 5 and 30 characters.")
    private final String name;

    @NotBlank
    @Email(regexp = "^(.+)@(.+)$")
    private final String email;

    @NotBlank
    @Size(min = 20, max = 100, message = "Address should be between 20 and 100 characters.")
    private final String address;

    private final String description;

    @Positive
    private final Integer countryId;

    public UpdateBreweryDTO(int id, String name, String email, String address,
                            String description, Integer countryId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.description = description;
        this.countryId = countryId;
    }

    public int getId() {
        return id;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<Integer> getCountryId() {
        return Optional.ofNullable(countryId);
    }
}

package beertag.models.dto.brewery;


import org.springframework.context.annotation.Scope;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Scope("prototype")
public class CreateBreweryDTO {

    private int id;

    @NotEmpty
    @Size(min = 5, max = 30, message = "Brewery name should be between 5 and 30 characters.")
    private String name;

    @NotEmpty
    @Email(regexp = "^(.+)@(.+)$")
    private String email;

    @NotEmpty
    @Size(min = 20, max = 100, message = "Address should be between 20 and 100 characters.")
    private String address;

    @NotEmpty
    private String description;
    private int countryId;

    public CreateBreweryDTO() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}

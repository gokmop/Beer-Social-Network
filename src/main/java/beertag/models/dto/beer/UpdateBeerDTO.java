package beertag.models.dto.beer;


import org.springframework.context.annotation.Scope;

import javax.validation.constraints.*;
import java.util.Optional;

@Scope("prototype")
public class UpdateBeerDTO {

    private final int id;

    @NotBlank
    @Size(min = 2, max = 20, message = "Name should be between 2 & 20 symbols")
    private final String name;

    @PositiveOrZero(message = "ABV cannot be a negative number")
    @Max(value = 50, message = "ABV should be below 50")
    private final Double abv;

    private final String description;
    private final String picture;

    @Positive
    private final Integer styleId;

    @Positive
    private final Integer createdById;

    @Positive
    private final Integer breweryId;

    public UpdateBeerDTO(int id, String name, Double abv, String description, String picture,
                         Integer styleId, Integer createdById, Integer breweryId) {
        this.id = id;
        this.name = name;
        this.abv = abv;
        this.description = description;
        this.picture = picture;
        this.styleId = styleId;
        this.createdById = createdById;
        this.breweryId = breweryId;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<Double> getAbv() {
        return Optional.ofNullable(abv);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<String> getPicture() {
        return Optional.ofNullable(picture);
    }

    public Optional<Integer> getStyleId() {
        return Optional.ofNullable(styleId);
    }

    public Optional<Integer> getCreatedById() {
        return Optional.ofNullable(createdById);
    }

    public Optional<Integer> getBreweryId() {
        return Optional.ofNullable(breweryId);
    }

    public int getId() {
        return id;
    }
}

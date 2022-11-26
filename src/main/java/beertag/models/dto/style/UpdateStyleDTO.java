package beertag.models.dto.style;

import org.springframework.context.annotation.Scope;

import javax.validation.constraints.Size;
import java.util.Optional;

@Scope("prototype")
public class UpdateStyleDTO {

    private final int id;

    @Size(min = 3, max = 20, message = "Style name must be between 3 and 20 characters.")
    private final String name;

    public UpdateStyleDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public int getId() {
        return id;
    }

}

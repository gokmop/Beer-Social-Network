package beertag.models.dto.tag;

import org.springframework.context.annotation.Scope;

import javax.validation.constraints.Size;
import java.util.Optional;

@Scope("prototype")
public class UpdateTagDTO {

    private final int id;
    @Size(min = 2, max = 20)
    private final String tagName;

    public UpdateTagDTO(int id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    public Optional<String> getTagName() {
        return Optional.ofNullable(tagName);
    }

    public int getId() {
        return id;
    }

}

package beertag.models.dto.tag;

import org.springframework.context.annotation.Scope;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Scope("prototype")
public class CreateTagDTO {

    private int id;

    @NotEmpty
    @Size(min = 3, max = 20, message = "Tag name must be between 3 and 20 characters.")
    private String tagName;

    public CreateTagDTO() {
    }

    public String getTagName() {
        return tagName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}


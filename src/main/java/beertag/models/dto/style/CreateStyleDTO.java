package beertag.models.dto.style;

import org.springframework.context.annotation.Scope;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Scope("prototype")
public class CreateStyleDTO {

    private int id;

    @NotEmpty
    @Size(min = 3, max = 20, message = "Style name must be between 3 and 20 characters.")
    private String styleName;


    public CreateStyleDTO() {
    }

    public String getStyleName() {
        return styleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
}

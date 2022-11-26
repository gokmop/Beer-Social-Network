package beertag.models.dto.user;

import org.springframework.context.annotation.Scope;

import javax.validation.constraints.*;


@Scope("prototype")
public class CreateUserDTO extends UpdateMvcUserDTO {

    @NotBlank
    @Size(min = 5, max = 20, message = "Username should be between 5 & 20 symbols")
    private String userName;

    public CreateUserDTO() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

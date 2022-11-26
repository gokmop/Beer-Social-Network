package beertag.models.dto.user;

import beertag.models.dto.SearchDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Utility.thisOrThat;

public class UserSearchDetailsDTO implements SearchDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String userName;
    private String exact;
    private String lessOrMoreThanBirthDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String desc;
    private String sortParameter;

    @Override
    public Map<String, String> getSearchParameters() {
        Optional<Integer> id = Optional.ofNullable(this.id);
        Optional<String> firstName = Optional.ofNullable(this.firstName);
        Optional<String> lastName = Optional.ofNullable(this.lastName);
        Optional<String> userName = Optional.ofNullable(this.userName);
        Optional<String> exact = Optional.ofNullable(this.exact);
        Optional<String> lessOrMoreThanBirthDate = Optional.ofNullable(this.lessOrMoreThanBirthDate);
        Optional<LocalDate> birthday = Optional.ofNullable(this.birthday);
        Optional<String> desc = Optional.ofNullable(this.desc);
        Optional<String> sortParameter = Optional.ofNullable(this.sortParameter);

        Map<String, String> searchParam = new HashMap<>();
        id.ifPresent(integer -> searchParam.put(ID, String.valueOf(integer)));
        if (exact.isPresent()) {
            thisOrThat(searchParam, exact, firstName, FIRST_NAME, EXACT, NOT_EXACT);
            thisOrThat(searchParam, exact, lastName, LAST_NAME, EXACT, NOT_EXACT);
            thisOrThat(searchParam, exact, userName, USER_NAME, EXACT, NOT_EXACT);
        } else {
            firstName.ifPresent(str -> searchParam.put(FIRST_NAME, str));
            lastName.ifPresent(str -> searchParam.put(LAST_NAME, str));
            userName.ifPresent(str -> searchParam.put(USER_NAME, str));
        }
        sortParameter.ifPresentOrElse(str -> searchParam.put(SORT_PARAMETER, str),
                () -> searchParam.put(SORT_PARAMETER, ID));
        desc.ifPresentOrElse(str -> searchParam.put(DESC, str), () -> searchParam.put(DESC, ASC));
        thisOrThat(searchParam, lessOrMoreThanBirthDate, birthday, BIRTH_DATE, LESS, MORE);
        return searchParam;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLessOrMoreThanBirthDate(String lessOrMoreThanBirthDate) {
        this.lessOrMoreThanBirthDate = lessOrMoreThanBirthDate;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setSortParameter(String sortParameter) {
        this.sortParameter = sortParameter;
    }

    public void setExact(String exact) {
        this.exact = exact;
    }
}

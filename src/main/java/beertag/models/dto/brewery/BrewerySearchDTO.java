package beertag.models.dto.brewery;

import beertag.models.dto.SearchDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static beertag.Constants.HqlQueriesConstants.*;

public class BrewerySearchDTO implements SearchDto {

    private Integer id;
    private String name;
    private String countryName;
    private String desc;
    private String sortParameter;

    public BrewerySearchDTO() {
    }

    @Override
    public Map<String, String> getSearchParameters() {
        Optional<Integer> id = Optional.ofNullable(this.id);
        Optional<String> name = Optional.ofNullable(this.name);
        Optional<String> desc = Optional.ofNullable(this.desc);
        Optional<String> countryName = Optional.ofNullable(this.countryName);
        Optional<String> sortParameter = Optional.ofNullable(this.sortParameter);

        Map<String, String> searchParams = new HashMap<>();
        id.ifPresent(i -> searchParams.put(ID, String.valueOf(i)));
        name.ifPresent(str -> searchParams.put(NAME, str));
        countryName.ifPresent(str -> searchParams.put(COUNTRY, str));
        sortParameter.ifPresentOrElse(str -> searchParams.put(SORT_PARAMETER, str),
                () -> searchParams.put(SORT_PARAMETER, ID));
        desc.ifPresentOrElse(str -> searchParams.put(DESC, str), () -> searchParams.put(DESC, ASC));
        return searchParams;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setSortParameter(String sortParameter) {
        this.sortParameter = sortParameter;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getDesc() {
        return desc;
    }

    public String getSortParameter() {
        return sortParameter;
    }
}

package beertag.models.dto.beer;

import beertag.models.dto.SearchDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.HqlQueriesConstants.*;
import static beertag.Utility.thisOrThat;

public class BeerSearchDTO implements SearchDto {

    private Integer id;
    private String name;
    private Double abv;
    private String lessOrMoreThanAbv;
    private String styleName;
    private String countryName;
    private String authorName;
    private String breweryName;
    private Double rating;
    private String lessOrMoreThanRating;
    private String tagId;
    private String desc;
    private String sortParameter;

    public BeerSearchDTO() {
    }

    public Map<String, String> getSearchParameters() {

        Optional<Integer> id = Optional.ofNullable(this.id);
        Optional<String> name = Optional.ofNullable(this.name);
        Optional<Double> abv = Optional.ofNullable(this.abv);
        Optional<String> lessOrMoreThanAbv = Optional.ofNullable(this.lessOrMoreThanAbv);
        Optional<String> styleName = Optional.ofNullable(this.styleName);
        Optional<String> countryName = Optional.ofNullable(this.countryName);
        Optional<String> authorName = Optional.ofNullable(this.authorName);
        Optional<String> breweryName = Optional.ofNullable(this.breweryName);
        Optional<Double> rating = Optional.ofNullable(this.rating);
        Optional<String> lessOrMoreThanRating = Optional.ofNullable(this.lessOrMoreThanRating);
        Optional<String> tagId = Optional.ofNullable(this.tagId);
        Optional<String> sortParameter = Optional.ofNullable(this.sortParameter);
        Optional<String> desc = Optional.ofNullable(this.desc);

        Map<String, String> searchParams = new HashMap<>();
        id.ifPresent(i -> searchParams.put(ID, String.valueOf(i)));
        name.ifPresent(str -> searchParams.put(NAME, str));
        tagId.ifPresent(str -> searchParams.put(TAGS, str));
        styleName.ifPresent(str -> searchParams.put(STYLE, str));
        countryName.ifPresent(str -> searchParams.put(COUNTRY, str));
        authorName.ifPresent(str -> searchParams.put(AUTHOR, str));
        breweryName.ifPresent(str -> searchParams.put(BREWERY, str));
        sortParameter.ifPresentOrElse(str -> searchParams.put(SORT_PARAMETER, str),
                () -> searchParams.put(SORT_PARAMETER, ID));
        desc.ifPresentOrElse(str -> searchParams.put(DESC, str), () -> searchParams.put(DESC, ASC));

        thisOrThat(searchParams, lessOrMoreThanAbv, abv, ABV, LESS, MORE);
        thisOrThat(searchParams, lessOrMoreThanRating, rating, RATING, LESS, MORE);

        return searchParams;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAbv() {
        return abv;
    }

    public String getLessOrMoreThanAbv() {
        return lessOrMoreThanAbv;
    }

    public String getStyleName() {
        return styleName;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBreweryName() {
        return breweryName;
    }

    public Double getRating() {
        return rating;
    }

    public String getLessOrMoreThanRating() {
        return lessOrMoreThanRating;
    }

    public String getTagId() {
        return tagId;
    }

    public String getDesc() {
        return desc;
    }

    public String getSortParameter() {
        return sortParameter;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbv(Double abv) {
        this.abv = abv;
    }

    public void setLessOrMoreThanAbv(String lessOrMoreThanAbv) {
        this.lessOrMoreThanAbv = lessOrMoreThanAbv;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setBreweryName(String breweryName) {
        this.breweryName = breweryName;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setLessOrMoreThanRating(String lessOrMoreThanRating) {
        this.lessOrMoreThanRating = lessOrMoreThanRating;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setSortParameter(String sortParameter) {
        this.sortParameter = sortParameter;
    }
}

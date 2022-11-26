package beertag;

public class Constants {

    public static final String ENTITY_WITH_ID_D_NOT_FOUND = "%s with id %d doesn't exist!";
    public static final String UPDATE_ERROR = "Update error : ";
    public static final String CREATE_ERROR = "Create error : ";
    public static final String SORT_DEFAULT = "id";

    public static class UserConstants {
        public static final String DISPLAY_USER_DETAILS_DTO = "DisplayUserDetailsDTO";
        public static final String USER_SOFT_DELETE = "UPDATE users SET enabled = '0' WHERE id = ?";
        public static final String USER_DETAILS_SOFT_DELETE = "UPDATE user_details SET enabled = '0' WHERE id = ?";
        public static final String USER_DETAILS_LOAD = "SELECT u FROM UserDetails u WHERE u.id = ?1 AND u.enabled = true";
        public static final String SPRING_SECURITY_TEXT_PASS = "{noop}";
        public static final String UPDATE_USER_DTO = "updateUserDto";
        public static final String MATCH_PASSWORD = "Password confirmation doesn't match password!";
        public static final String CONFIRMATION_PASSWORD = "confirmationPassword";
        public static final String CREATE_USER_DTO = "createUserDto";
        public static final int LEGAL_YEARS_MIN = 18;
        public static final int LEGAL_YEARS_MAX = 120;
        public static final String USER_YOUNGER_THAN_LEGAL_YEARS_ERROR = "User younger than legal [18] years!";
        public static final String USER_OLDER_THAN_LEGAL_YEARS_ERROR = "User older than 'legal' [120] years!";
        public static final String DUPLICATE_USER_EMAIL_ERROR = "User email already exists in the repository!";
        public static final String DUPLICATE_USERNAME_ERROR = "Username already in the repository!";
        public static final String USER_WITH_USERNAME_S_DOESN_T_EXIST = "User with username [%s] doesn't exist!";
        public static final String BIRTH_DAY_PATTERN = "yyyy-MM-dd";
        public static final String DRANK_LIST = "drankList";
        public static final String WISH_LIST = "wishList";
    }

    public static class BeerConstants {
        public static final String AVG_BEER_RATING = "(SELECT AVG(br.rating) FROM BEERS b " +
                "JOIN beer_ratings br ON b.id = br.beer_id " +
                "WHERE br.beer_id = id AND b.enabled = 1)";
        public static final String TIMES_BEER_RATED = "(SELECT count(br.rating) FROM BEERS b " +
                "JOIN beer_ratings br ON b.id = br.beer_id " +
                "WHERE br.beer_id = id AND b.enabled = 1)";
        public static final String NOT_RATED = "Not rated";
        public static final String NO_TAGS = "No tags";
        public static final String BEER_DUPLICATE_UPDATE_ERROR = "Updated beer already in the repository!";
        public static final String BEER_DUPLICATE_CREATE_ERROR = "Beer already exists in the repository!";
    }

    public static class BreweryConstants {
        public static final String COUNT_BEERS_MADE = "(SELECT COUNT(b.id) " +
                "FROM BEERS b " +
                "JOIN breweries br ON b.brewery_id = br.id " +
                "WHERE b.brewery_id = id " +
                "AND b.enabled = 1)";
        public static final String AVG_BEERS_MADE_RATING = "(SELECT AVG(br.rating)\n" +
                "FROM BEERS b\n" +
                "         JOIN beer_ratings br ON b.id = br.beer_id\n" +
                "         JOIN breweries b2 ON b2.id = b.brewery_id\n" +
                "WHERE b.brewery_id = id\n" +
                "  AND b.enabled = 1)";
        public static final String BREWERY_DUPLICATE_UPDATE_ERROR = "Updated brewery already in the repository!";
        public static final String BREWERY_DUPLICATE_CREATE_ERROR = "Brewery already exists in the repository!";
        public static final String DUPLICATE_BREWERY_EMAIL_ERROR = "Brewery email already exists in the repository!";
    }

    public static class StyleConstants {
        public static final String STYLE_LOAD = "SELECT s FROM Style s WHERE  s.id = ?1 AND s.enabled = true";
        public static final String STYLE_SOFT_DELETE = "UPDATE styles SET enabled = '0' WHERE id = ?";

        public static final String STYLE_DUPLICATE_UPDATE_ERROR = "Updated style already in the repository!";
        public static final String STYLE_DUPLICATE_CREATE_ERROR = "Style already exists in the repository!";
    }

    public static class TagConstants {
        public static final String TAG_DUPLICATE_UPDATE_ERROR = "Updated tag already in the repository!";
        public static final String TAG_DUPLICATE_CREATE_ERROR = "Tag already exists in the repository!";
    }

    public static class HqlQueriesConstants {
        public static final String EXACT = "exact";
        public static final String NOT_EXACT = "not exact";
        public static final String DESC = "desc";
        public static final String ASC = "asc";
        public static final int DESC_NUMERIC = 1;
        public static final int ASC_NUMERIC = -1;
        public static final String SORT_PARAMETER = "sortParameter";
        public static final String LIKE = "like";
        public static final String EQUAL = "equal";
        public static final String NAME = "name";
        public static final String ABV = "abv";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String USER_NAME = "userName";
        public static final String ID = "id";
        public static final String EMAIL = "email";
        public static final String PROFILE_PICTURE = "profilePic";
        public static final String BIRTH_DATE = "birthDate";
        public static final String STYLE = "style";
        public static final String COUNTRY = "country";
        public static final String BREWERY = "brewery";
        public static final String AUTHOR = "author";
        public static final String USER = "userDetails";
        public static final String BEERS_MADE = "beersMade";
        public static final String RATING = "rating";
        public static final String MORE = "more";
        public static final String LESS = "less";
    }

    public static class AuthorisationConstants {
        public static final String ONLY_ADMINS_CAN_S_S = "Only admins can %s %s";
        public static final String ONLY_AUTHORS_AND_ADMINS_CAN_S_ERROR = "Only authors and admins can %s!";
        public static final String TAGS = "tags";
        public static final String COUNTRIES = "countries";
        public static final String STYLES = "styles";
        public static final String BREWERIES = "breweries";
        public static final String BEERS = "beers";
        public static final String ADDRESS = "address";
        public static final String UPDATE = "update";
        public static final String CREATE = "create";
        public static final String REMOVE = "remove";
        public static final String BEER = "beer";
        public static final String USER = "user";
    }

    public static class ModelViewConstants {
        public static final String SEARCH_BEER_DTO = "searchBeerDto";
        public static final String RATE_DTO = "rateDto";
        public static final String CREATE_BEER_DTO = "createBeerDto";
        public static final String CREATE_BREWERY_DTO = "createBreweryDto";
        public static final String SEARCH_BREWERY_DTO = "searchBreweryDto";
    }

}

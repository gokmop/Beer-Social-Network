package beertag.models;

import java.io.Serializable;
import java.util.Objects;

public class UserAuthorityId implements Serializable {

    private String userName;
    private String authority;

    public UserAuthorityId() {
    }

    public UserAuthorityId(String userName, String authority) {
        this.userName = userName;
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthorityId that = (UserAuthorityId) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, authority);
    }
}

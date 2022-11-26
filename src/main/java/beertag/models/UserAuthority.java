package beertag.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "authorities")
@Entity(name = "UserAuthority")
@IdClass(value = UserAuthorityId.class)
public class UserAuthority implements GrantedAuthority {

    @Id
    @Column(name = "username")
    private String userName;

    @Id
    @Column(name = "authority")
    private String authority;

    public UserAuthority() {
    }

    public String getUserName() {
        return userName;
    }

    public String getAuthority() {
        return authority;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthority that = (UserAuthority) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, authority);
    }

    @Override
    public String toString() {
        return authority;
    }
}

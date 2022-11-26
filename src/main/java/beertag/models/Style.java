package beertag.models;

import beertag.models.contracts.SoftDeletable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static beertag.Constants.StyleConstants.STYLE_LOAD;
import static beertag.Constants.StyleConstants.STYLE_SOFT_DELETE;

@Entity(name = "Style")
@Table(name = "styles")
@Loader(namedQuery = "findStyleById")
@NamedQuery(name = "findStyleById", query = STYLE_LOAD)
@SQLDelete(sql = STYLE_SOFT_DELETE, check = ResultCheckStyle.COUNT)
@Where(clause = "enabled = 1")
public class Style extends SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 3, max = 20, message = "Style name must be between 3 and 20 characters.")
    @Column(name = "style")
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "style_beer_relation",
            joinColumns = @JoinColumn(name = "style_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id")
    )
    private Set<Beer> thisStyleBeers;

    public Style() {
    }

    public Style(int id, String name) {
        this.id = id;
        setName(name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Beer> getThisStyleBeers() {
        return new HashSet<>(thisStyleBeers);
    }

    public void update(Style style) {
        setName(style.getName());
    }

    private void setName(String name) {
        this.name = name;
    }

    public void setThisStyleBeers(Set<Beer> thisStyleBeers) {
        this.thisStyleBeers = thisStyleBeers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Style style = (Style) o;
        return id == style.id &&
                Objects.equals(name, style.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Style{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

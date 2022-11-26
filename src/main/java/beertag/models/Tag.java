package beertag.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 3, max = 20, message = "Tag name must be between 3 and 20 characters.")
    @Column(name = "tag")
    private String name;

    public Tag() {
    }

    public Tag(int id, String tagName) {
        this.id = id;
        setName(tagName);

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void update(Tag tag) {
        setName(tag.getName());
    }

    public void setName(String name) {
        this.name = "#" + name.replaceAll("#", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id &&
                Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return name;
    }
}

package beertag.repositories.contracts;

import beertag.models.Beer;
import beertag.models.Tag;
import beertag.repositories.contracts.generic.GetRepo;
import beertag.repositories.contracts.generic.CheckExists;
import beertag.repositories.contracts.generic.CreateRepo;
import beertag.repositories.contracts.generic.UpdateRemoveRepo;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends GetRepo<Tag>, CreateRepo<Tag>,
        UpdateRemoveRepo<Tag>, CheckExists<Tag> {
    List<Tag> getBeerListTags(Collection<Beer> userBeers);

}

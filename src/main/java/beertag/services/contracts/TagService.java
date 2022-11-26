package beertag.services.contracts;

import beertag.models.Beer;
import beertag.models.Tag;
import beertag.services.contracts.generic.UpdateRemoveService;
import beertag.services.contracts.generic.EnsureNoDuplicates;
import beertag.services.contracts.generic.GetService;
import beertag.services.contracts.generic.UserCreate;

import java.util.Collection;
import java.util.List;

public interface TagService extends GetService<Tag>, UpdateRemoveService<Tag>,
        EnsureNoDuplicates<Tag>, UserCreate<Tag> {
    List<Tag> getBeerListTags(Collection<Beer> userBeers);
}

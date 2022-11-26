package beertag.repositories.contracts;

import beertag.models.Beer;
import beertag.models.Style;
import beertag.repositories.contracts.generic.GetRepo;
import beertag.repositories.contracts.generic.CheckExists;
import beertag.repositories.contracts.generic.CreateRepo;
import beertag.repositories.contracts.generic.UpdateRemoveRepo;

import java.util.Collection;
import java.util.List;

public interface StyleRepository extends GetRepo<Style>, CreateRepo<Style>,
        CheckExists<Style>, UpdateRemoveRepo<Style> {
    List<Style> getAllStyles();

    List<Style> getBeerListStyles(Collection<Beer> userBeers);

}

package beertag.services.contracts;

import beertag.models.Beer;
import beertag.models.Style;
import beertag.services.contracts.generic.*;

import java.util.Collection;
import java.util.List;

public interface StyleService extends GetService<Style>, UpdateRemoveService<Style>,
        EnsureNoDuplicates<Style>, AdminCreate<Style> {
    public List<Style> getAllStyles();

    List<Style> getBeerListStyles(Collection<Beer> userBeers);
}

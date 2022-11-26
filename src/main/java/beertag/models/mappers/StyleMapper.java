package beertag.models.mappers;

import beertag.models.Style;
import beertag.models.dto.style.CreateStyleDTO;
import beertag.models.dto.style.UpdateStyleDTO;
import beertag.services.contracts.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StyleMapper {
    private final StyleService styleService;

    @Autowired
    public StyleMapper(StyleService styleService) {
        this.styleService = styleService;
    }

    public Style fromDto(CreateStyleDTO styleDTO) {
        return new Style(styleDTO.getId(), styleDTO.getStyleName());
    }

    public Style fromDto(UpdateStyleDTO styleDTO) {
        Style style = styleService.getById(styleDTO.getId());
        return new Style(styleDTO.getId(), styleDTO.getName().orElse(style.getName()));
    }
}

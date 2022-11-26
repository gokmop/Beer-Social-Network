package beertag.models.mappers;

import beertag.models.Tag;
import beertag.models.dto.tag.CreateTagDTO;
import beertag.models.dto.tag.UpdateTagDTO;
import beertag.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    private final TagService tagService;

    @Autowired
    public TagMapper(TagService tagService) {
        this.tagService = tagService;
    }

    public Tag fromDto(CreateTagDTO tagDTO) {
        return new Tag(tagDTO.getId(), tagDTO.getTagName());
    }

    public Tag fromDto(UpdateTagDTO tagDTO) {
        Tag tag = tagService.getById(tagDTO.getId());
        return new Tag(tagDTO.getId(), tagDTO.getTagName().orElse(tag.getName()));
    }
}

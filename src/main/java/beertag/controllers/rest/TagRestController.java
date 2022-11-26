package beertag.controllers.rest;

import beertag.models.Tag;
import beertag.models.User;
import beertag.models.dto.tag.CreateTagDTO;
import beertag.models.dto.tag.UpdateTagDTO;
import beertag.models.mappers.TagMapper;
import beertag.services.contracts.TagService;
import beertag.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagRestController {

    private final TagService tagService;
    private final TagMapper tagMapper;
    private final UserService userService;

    @Autowired
    public TagRestController(TagService tagService, TagMapper tagMapper, UserService userService) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
        this.userService = userService;
    }

    @GetMapping
    public List<Tag> getAll(
            @RequestParam(required = false) Integer desc,
            @RequestParam(required = false) String sortParameter) {
        return tagService.getAll(Optional.ofNullable(desc), Optional.ofNullable(sortParameter));
    }

    @GetMapping("/{id}")
    public List<Tag> getById(@PathVariable int id) {
            return Collections.singletonList(tagService.getById(id));
    }

    @PostMapping
    public Tag createTag(@Valid @RequestBody CreateTagDTO dto,
                         Principal principal) {
            User user = userService.getUserByUsername(principal.getName());
            Tag tag = tagMapper.fromDto(dto);
            tagService.create(tag);
            return tag;
    }

    @PutMapping
    public Tag updateTag(@Valid @RequestBody UpdateTagDTO dto,
                         Principal principal) {

            User user = userService.getUserByUsername(principal.getName());
            Tag tagToUpdate = tagMapper.fromDto(dto);
            tagService.update(tagToUpdate, user);
            return tagToUpdate;
    }

    @DeleteMapping("/{id}")
    public Tag removeTag(@PathVariable int id,
                         Principal principal) {
            Tag tag = tagService.getById(id);
            User user = userService.getUserByUsername(principal.getName());
            tagService.remove(id, user);
            return tag;
    }

}

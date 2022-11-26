package beertag.controllers.rest;

import beertag.models.Style;
import beertag.models.User;
import beertag.models.dto.style.CreateStyleDTO;
import beertag.models.dto.style.UpdateStyleDTO;
import beertag.models.mappers.StyleMapper;
import beertag.services.contracts.StyleService;
import beertag.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/styles")
public class StyleRestController {

    private final StyleService styleService;
    private final StyleMapper styleMapper;
    private final UserService userService;

    @Autowired
    public StyleRestController(StyleService styleService, StyleMapper styleMapper, UserService userService) {
        this.styleService = styleService;
        this.styleMapper = styleMapper;
        this.userService = userService;
    }

    @GetMapping
    public List<Style> getAll(@RequestParam(required = false) Integer desc,
                              @RequestParam(required = false) String sortParameter
    ) {
        return styleService.getAll(Optional.ofNullable(desc), Optional.ofNullable(sortParameter));
    }

    @GetMapping("/{id}")
    public List<Style> getById(@PathVariable int id) {
            return Collections.singletonList(styleService.getById(id));
    }

    @PostMapping
    public Style createStyle(@Valid @RequestBody CreateStyleDTO dto,
                             Principal principal) {
            Style style = styleMapper.fromDto(dto);
            User user = userService.getUserByUsername(principal.getName());
            styleService.create(style, user);
            return style;
    }

    @PutMapping
    public Style updateStyle(@Valid @RequestBody UpdateStyleDTO dto,
                             Principal principal) {
            User user = userService.getUserByUsername(principal.getName());
            Style styleToUpdate = styleMapper.fromDto(dto);
            styleService.update(styleToUpdate, user);
            return styleToUpdate;
    }

 //   @DeleteMapping("/{id}")
    public Style removeStyle(@PathVariable int id,
                             Principal principal) {
            Style style = styleService.getById(id);
            User user = userService.getUserByUsername(principal.getName());
            styleService.remove(id, user);
            return style;
    }

}

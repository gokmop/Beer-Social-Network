package beertag.services;


import beertag.exception.DuplicateEntityException;
import beertag.models.Beer;
import beertag.models.Tag;
import beertag.models.User;
import beertag.repositories.contracts.TagRepository;
import beertag.services.contracts.TagService;
import beertag.services.contracts.AuthorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static beertag.Constants.AuthorisationConstants.*;
import static beertag.Constants.SORT_DEFAULT;
import static beertag.Constants.TagConstants.TAG_DUPLICATE_CREATE_ERROR;
import static beertag.Constants.TagConstants.TAG_DUPLICATE_UPDATE_ERROR;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final AuthorisationService authorisationService;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, AuthorisationService authorisationService) {
        this.tagRepository = tagRepository;
        this.authorisationService = authorisationService;
    }

    @Override
    public List<Tag> getAll(Optional<Integer> desc, Optional<String> sortParameter) {
        return tagRepository.getAll(desc.orElse(Integer.SIZE), sortParameter.orElse(SORT_DEFAULT));
    }

    @Override
    public Tag getById(int id) {
        return tagRepository.getById(id);
    }

    @Override
    public void create(Tag tag) {
        ensureNoDuplicates(tag, TAG_DUPLICATE_CREATE_ERROR);
        tagRepository.create(tag);
    }

    @Override
    public void update(Tag tag, User authorise) {
        authorisationService.authorise(authorise, String.format(ONLY_ADMINS_CAN_S_S, UPDATE, TAGS));
        ensureNoDuplicates(tag, TAG_DUPLICATE_UPDATE_ERROR);
        tagRepository.update(tag);
    }

    @Override
    public void remove(int id, User authorise) {
        authorisationService.authorise(authorise, String.format(ONLY_ADMINS_CAN_S_S, REMOVE, TAGS));
        Tag tagToRemove = tagRepository.getById(id);
        tagRepository.remove(tagToRemove);
    }

    public void ensureNoDuplicates(Tag tag, String message) {
        if (tagRepository.checkExists(tag)) {
            throw new DuplicateEntityException(message);
        }
    }

    @Override
    public List<Tag> getBeerListTags(Collection<Beer> userBeers) {
        return tagRepository.getBeerListTags(userBeers);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.getAll();
    }
}

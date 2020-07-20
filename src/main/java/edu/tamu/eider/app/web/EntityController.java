package edu.tamu.eider.app.web;

import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;
import edu.tamu.eider.app.model.repo.NameRepository;

@RepositoryRestController
public class EntityController {

    @Autowired
    private NameRepository nameRepo;

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    @GetMapping("/entity/name")
    public @ResponseBody CollectionModel<PersistentEntityResource> findByAssociatedName(@RequestParam(required = true) String name, PersistentEntityResourceAssembler assembler) throws JsonProcessingException {
        Set<PersistentEntityResource> entities = nameRepo.findByName(name).stream()
            .map(n -> n.getEntity())
            .map(e -> assembler.toFullResource(e))
            .collect(Collectors.toSet());
        return CollectionModel.of(entities);
    }

    @GetMapping("/entity/url")
    public @ResponseBody PersistentEntityResource findByUrl(@RequestParam(required = true) URL url, PersistentEntityResourceAssembler assembler) {
        Optional<Entity> entityOption;
        Optional<Identifier> identifierOption = identifierRepo.findByIdentifier(url.toString());
        if (identifierOption.isPresent()) {
            entityOption = Optional.of(identifierOption.get().getEntity());
        } else if (!(entityOption = entityRepo.findByUrl(url)).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find any Entity or Identifier that matches the given url");
        }
        return assembler.toFullResource(entityOption.get());
    }

}

package edu.tamu.eider.app.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

import java.net.URL;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;
import edu.tamu.eider.app.model.repo.NameRepository;

@BasePathAwareController
public class EntityController {

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    @Autowired
    private NameRepository nameRepo;

    @RequestMapping(path = "/entity/{uuid}", method = HEAD)
    public RedirectView redirectHeadToEntity(@PathVariable("uuid") UUID uuid) {
        Optional<Entity> entityOption = entityRepo.findById(uuid);
        if (entityOption.isPresent()) {
            return new RedirectView(entityOption.get().getUrl().toString());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Entity for provided id");
        }
    }

    @ResponseBody
    @GetMapping("/entity/name")
    public CollectionModel<PersistentEntityResource> findByAssociatedName(@RequestParam("name") String nameValue, PersistentEntityResourceAssembler assembler)
            throws JsonProcessingException {
        
        Set<PersistentEntityResource> entities = nameRepo.findByName(nameValue).stream()
            .map(name -> name.getEntity())
            .map(ent -> assembler.toFullResource(ent))
            .collect(Collectors.toSet());
        return CollectionModel.of(entities);
    }

    @ResponseBody
    @GetMapping("/entity/url")
    public PersistentEntityResource findByUrl(@RequestParam(name = "url") URL url, PersistentEntityResourceAssembler assembler) {
        Optional<Entity> entityOption;
        Optional<Identifier> identifierOption = identifierRepo.findByIdentifier(url.toString());
        if (identifierOption.isPresent()) {
            entityOption = Optional.of(identifierOption.get().getEntity());
        } else if (!(entityOption = entityRepo.findByUrl(url)).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find any Entity or Identifier that matches the given url");
        }
        return assembler.toFullResource(entityOption.get());
    }

    @RequestMapping(path = "/entity/{uuid}/redirect", method = GET)
    public RedirectView redirectGetToEntity(@PathVariable("uuid") UUID uuid) {
        Optional<Entity> entityOption = entityRepo.findById(uuid);
        if (entityOption.isPresent()) {
            return new RedirectView(entityOption.get().getUrl().toString());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Entity for provided id");
        }
    }

}

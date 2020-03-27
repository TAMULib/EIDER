package edu.tamu.eider.app.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
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
import edu.tamu.eider.app.model.Name;
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
    public Optional<List<Entity>> findByAssociatedName(@RequestParam String nameValue) {
        List<Entity> entities = new ArrayList<Entity>();
        for (Name name : nameRepo.findByName(nameValue)) {
            entities.add(name.getEntity());
        }
        return Optional.of(entities);
    }

    @ResponseBody
    @GetMapping("/entity/url")
    public Optional<Entity> findByUrl(@RequestParam(name = "url") URL url) {
        Optional<Entity> entityOption = entityRepo.findByUrl(url);
        Optional<Identifier> identifierOption = identifierRepo.findByIdentifier(url.toString());
        if (identifierOption.isPresent()) {
            entityOption = Optional.of(identifierOption.get().getEntity());
        } else if (entityOption.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find any Entity or Identifier that matches the given url");
        }
        return entityOption;
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

package edu.tamu.eider.app.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.repo.EntityRepository;

@RestController
public class EntityController {

    @Autowired
    private EntityRepository entityRepo;

    @RequestMapping(path = "/entity/{uuid}/redirect", method = GET)
    public RedirectView redirectGetToEntity(@PathVariable("uuid") UUID uuid) {
        Optional<Entity> entityOption = entityRepo.findById(uuid);
        if (entityOption.isPresent()) {
            return new RedirectView(entityOption.get().getUrl().toString());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Entity for provided id");
        }
    }

    @RequestMapping(path = "/entity/{uuid}", method = GET)
    public Entity getEntity(@PathVariable("uuid") UUID uuid) {
        Optional<Entity> entityOption = entityRepo.findById(uuid);
        if (entityOption.isPresent()) {
            return entityOption.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Entity for provided id");
        }
    }

    @RequestMapping(path = "/entity/{uuid}", method = HEAD)
    public RedirectView redirectHeadToEntity(@PathVariable("uuid") UUID uuid) {
        Optional<Entity> entityOption = entityRepo.findById(uuid);
        if (entityOption.isPresent()) {
            return new RedirectView(entityOption.get().getUrl().toString());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Entity for provided id");
        }
    }
}
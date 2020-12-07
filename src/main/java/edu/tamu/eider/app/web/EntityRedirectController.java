package edu.tamu.eider.app.web;

import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.repo.EntityRepository;

@RepositoryRestController
public class EntityRedirectController {

    @Autowired
    private EntityRepository entityRepo;

    @RequestMapping(method = HEAD, path = "/entity/{uuid}")
    public RedirectView headRedirectEntity(@PathVariable(required = true) final UUID uuid) {
        return redirectEntity(uuid);
    }

    @GetMapping("/entity/{uuid}/redirect")
    public RedirectView getRedirectEntity(@PathVariable(required = true) final UUID uuid) {
        return redirectEntity(uuid);
    }

    private RedirectView redirectEntity(final UUID uuid) {
        Optional<Entity> entityOption = entityRepo.findById(uuid);
        if (entityOption.isPresent()) {
            return new RedirectView(entityOption.get().getUrl());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Entity for provided id");
        }
    }

}

package edu.tamu.eider.app.web;

import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.IdentifierRepository;

@RepositoryRestController
public class IdentifierRedirectController {

    @Autowired
    private IdentifierRepository identifierRepo;

    @RequestMapping(method = HEAD, path = "/identifier")
    public RedirectView headRedirectIdentifier(@RequestParam(required = true) final String url) {
        return redirectIdentifier(url);
    }

    @GetMapping("/identifier/redirect")
    public RedirectView getRedirectIdentifier(@RequestParam(required = true) final String url) {
        return redirectIdentifier(url);
    }

    private RedirectView redirectIdentifier(final String url) {
        final Optional<Identifier> identifierOption = identifierRepo.findByIdentifier(url);
        if (identifierOption.isPresent()) {
            return new RedirectView(identifierOption.get().getEntity().getUrl());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to resolve identifier for provided url");
        }
    }

}

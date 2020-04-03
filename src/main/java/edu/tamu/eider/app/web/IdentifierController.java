package edu.tamu.eider.app.web;

import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

import java.net.URL;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.IdentifierRepository;

@BasePathAwareController
public class IdentifierController {

    @Autowired
    private IdentifierRepository identifierRepo;

    @RequestMapping(path = "/identifier", method = HEAD)
    public RedirectView redirectHeadToEntity(@RequestParam final URL url) {
        final Optional<Identifier> identifierOption = identifierRepo.findByIdentifier(url.toString());
        if (identifierOption.isPresent()) {
            return new RedirectView(identifierOption.get().getEntity().getUrl().toString());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to resolve identifier for provided url");
        }
    }
}
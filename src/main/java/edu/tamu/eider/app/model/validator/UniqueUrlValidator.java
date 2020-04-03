package edu.tamu.eider.app.model.validator;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;

public abstract class UniqueUrlValidator implements Validator {

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    public void validate(Object obj, Errors e) {
        String url;
        if (obj instanceof Entity) {
            url = ((Entity) obj).getUrl().toString();
        } else if (obj instanceof Identifier) {
            url = ((Identifier) obj).getIdentifier();
        } else {
            throw new RuntimeException("Object is not an Entity or Identifier");
        }
        try {
            if (identifierRepo.existsByIdentifier(url)) {
                e.rejectValue("url", "url.not.unique", "There is already an Identifier with the given url");
            } else if (entityRepo.existsByUrl(new URL(url))) {
                e.rejectValue("url", "url.not.unique", "There is alreay an Entity with the given url");
            }
        } catch (MalformedURLException e2) {
            // Do nothing, non-url identifier is not already in use
        }
    }
}

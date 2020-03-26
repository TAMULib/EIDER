package edu.tamu.eider.app.model.validator;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;

public class BeforeSaveIdentifierValidator implements Validator {

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    public boolean supports(Class<?> clazz) {
        return Identifier.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        Identifier identifier = (Identifier) obj;
        try {
            if (identifierRepo.existsByIdentifier(identifier.getIdentifier())) {
                e.rejectValue("url", "url.not.unique", "There is already an Identifier with the given url");
            } else if (entityRepo.existsByUrl(new URL(identifier.getIdentifier()))) {
                e.rejectValue("url", "url.not.unique", "There is already an Entity with the given url");
            }
        } catch (MalformedURLException e1) {
            // Do nothing, non-url identifier is not already in use
        }
    }
}
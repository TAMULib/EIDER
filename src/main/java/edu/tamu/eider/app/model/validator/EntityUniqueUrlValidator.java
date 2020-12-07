package edu.tamu.eider.app.model.validator;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.repo.IdentifierRepository;

public abstract class EntityUniqueUrlValidator implements Validator {

    @Autowired
    private IdentifierRepository identifierRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return Entity.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        String url = ((Entity) obj).getUrl();
        if (Objects.nonNull(url) && identifierRepo.existsByIdentifier(url)) {
            e.rejectValue("url", "url.not.unique", "There is already an Identifier with the given url");
        }
    }
}

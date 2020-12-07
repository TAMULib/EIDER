package edu.tamu.eider.app.model.validator;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.EntityRepository;

public abstract class IdentifierUniqueUrlValidator implements Validator {

    @Autowired
    private EntityRepository entityRepo;

    @Override
    public boolean supports(Class<?> clazz) {
        return Identifier.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        String identifier = ((Identifier) obj).getIdentifier();
        if (Objects.nonNull(identifier) && entityRepo.existsByUrl(identifier)) {
            e.rejectValue("identifier", "identifier.not.unique", "There is already an Entity with the given url");
        }
    }
}

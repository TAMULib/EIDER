package edu.tamu.eider.app.model.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;

@Component
public class BeforeCreateEntityValidator implements Validator {

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    public boolean supports(Class<?> clazz) {
        return Entity.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        Entity entity = (Entity) obj;
        Optional<Entity> entityOption = entityRepo.findByUrl(entity.getUrl());
        Optional<Identifier> identifierOption = identifierRepo.findByIdentifier(entity.getUrl().toString());
        if (entityOption.isPresent() || identifierOption.isPresent()) {
            e.rejectValue("url", "url.not.unique", "There is already an Entity or Identifier with the given url");
        }
    }
}
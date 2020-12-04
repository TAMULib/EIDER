package edu.tamu.eider.app.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.annotation.ValidIdentifier;

public class IdentifierValidator implements ConstraintValidator<ValidIdentifier, Identifier> {

    @Override
    public boolean isValid(Identifier identifier, ConstraintValidatorContext context) {
        return identifier.getIdentifier().toLowerCase()
            .startsWith(identifier.getIdentifierType().getNamespace().toLowerCase());
    }

}

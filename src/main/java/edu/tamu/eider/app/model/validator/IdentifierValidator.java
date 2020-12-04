package edu.tamu.eider.app.model.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.annotation.ValidIdentifier;

public class IdentifierValidator implements ConstraintValidator<ValidIdentifier, Identifier> {

    @Override
    public boolean isValid(Identifier identifier, ConstraintValidatorContext context) {
        return Objects.isNull(identifier.getIdentifierType()) || identifier.getIdentifier().toLowerCase()
            .startsWith(identifier.getIdentifierType().getNamespace().toLowerCase());
    }

}

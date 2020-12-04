package edu.tamu.eider.app.model.validator;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.tamu.eider.app.model.annotation.ValidUri;

public class UriValidator implements ConstraintValidator<ValidUri, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            new URI(value);
        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }

}

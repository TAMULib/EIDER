package edu.tamu.eider.app.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.UrlValidator;

import edu.tamu.eider.app.model.annotation.ValidHttpUrl;

public class HttpUrlValidator implements ConstraintValidator<ValidHttpUrl, String> {

    private final static UrlValidator URL_VALIDATOR = new UrlValidator(new String[] { "http", "https" });

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return URL_VALIDATOR.isValid(value);
    }

}

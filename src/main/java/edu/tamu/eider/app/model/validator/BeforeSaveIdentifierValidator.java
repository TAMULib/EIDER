package edu.tamu.eider.app.model.validator;

import org.springframework.stereotype.Component;

import edu.tamu.eider.app.model.Identifier;

@Component
public class BeforeSaveIdentifierValidator extends UniqueUrlValidator {

    public boolean supports(Class<?> clazz) {
        return Identifier.class.equals(clazz);
    }

}
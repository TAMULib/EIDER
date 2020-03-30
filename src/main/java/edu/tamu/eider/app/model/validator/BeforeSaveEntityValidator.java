package edu.tamu.eider.app.model.validator;

import org.springframework.stereotype.Component;

import edu.tamu.eider.app.model.Entity;

@Component
public class BeforeSaveEntityValidator extends UniqueUrlValidator {

    public boolean supports(Class<?> clazz) {
        return Entity.class.equals(clazz);
    }

}
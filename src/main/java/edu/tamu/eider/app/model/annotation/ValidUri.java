package edu.tamu.eider.app.model.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.tamu.eider.app.model.validator.UriValidator;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = UriValidator.class)
public @interface ValidUri {

    String message() default "Inavalid URI";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

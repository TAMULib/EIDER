package edu.tamu.eider.app.model.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.tamu.eider.app.model.validator.HttpUrlValidator;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = HttpUrlValidator.class)
public @interface ValidHttpUrl {

    String message() default "Inavalid URL";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

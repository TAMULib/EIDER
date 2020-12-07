package edu.tamu.eider.app.model.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.tamu.eider.app.model.validator.IdentifierValidator;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = IdentifierValidator.class)
public @interface ValidIdentifier {

    String message() default "Identifier must start with the identifier type namespace";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

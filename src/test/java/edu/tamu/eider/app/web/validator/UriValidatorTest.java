package edu.tamu.eider.app.web.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.tamu.eider.app.model.validator.UriValidator;

@SpringBootTest
public class UriValidatorTest {

    @Test
    void testIsValid() {
        UriValidator uriValidator = new UriValidator();
        assertTrue(uriValidator.isValid("http://example.com", null));
    }

    @Test
    void testIsValidExpectingURISyntaxException() {
        UriValidator uriValidator = new UriValidator();
        assertFalse(uriValidator.isValid("file: {invalid uri}", null));
    }

}

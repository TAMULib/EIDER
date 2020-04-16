package edu.tamu.eider.resources;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import edu.tamu.eider.ApiDocumentation;
import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.IdentifierType;

public class IdentifierTestData extends ApiDocumentation {

    protected static final String TEST_ENTITY_1_CANONICAL_NAME = "Example 1";
    protected static final String TEST_ENTITY_1_NOTES = "Example 1 notes";
    protected static final String TEST_ENTITY_1_URL_STRING = "http://www.example.com/1";
    protected static final String TEST_IDENTIFIER_1_IDENTIFIER = "http://www.example.com/2";
    protected static final String TEST_IDENTIFIER_1_NOTES = "Identifier 1 Notes";
    protected static final String TEST_IDENTIFIER_2_IDENTIFIER = "http://www.example.com/3";
    protected static final String TEST_IDENTIFIER_2_NOTES = "Identifier 2 Notes";
    protected static final String TEST_IDENTIFIER_TYPE_NAME = "Test Identifier Type";
    protected static final String TEST_IDENTIFIER_TYPE_NAMESPACE_STRING = "http://www.example.com";

    protected static final LocalDate TEST_IDENTIFIER_1_START_DATE = LocalDate.of(2020, 1, 1);
    protected static final LocalDate TEST_IDENTIFIER_1_END_DATE = LocalDate.of(2020, 4, 15);
    protected static final LocalDate TEST_IDENTIFIER_2_START_DATE = LocalDate.of(1990, 2, 4);
    protected static final LocalDate TEST_IDENTIFIER_2_END_DATE = LocalDate.of(2010, 9, 1);


    protected static final IdentifierType TEST_IDENTIFIER_TYPE = new IdentifierType();
    protected static final Entity TEST_ENTITY_1 = new Entity();
    protected static final Identifier TEST_IDENTIFIER_1 = new Identifier();
    protected static final Identifier TEST_IDENTIFIER_2 = new Identifier();

    static {
        try {
            TEST_ENTITY_1.setUrl(new URL(TEST_ENTITY_1_URL_STRING));
            TEST_IDENTIFIER_TYPE.setNamespace(new URL(TEST_IDENTIFIER_TYPE_NAMESPACE_STRING));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TEST_ENTITY_1.setCanonicalName(TEST_ENTITY_1_CANONICAL_NAME);
        TEST_ENTITY_1.setNotes(TEST_ENTITY_1_NOTES);

        TEST_IDENTIFIER_TYPE.setName(TEST_IDENTIFIER_TYPE_NAME);

        TEST_IDENTIFIER_1.setEntity(TEST_ENTITY_1);
        TEST_IDENTIFIER_1.setIdentifier(TEST_IDENTIFIER_1_IDENTIFIER);
        TEST_IDENTIFIER_1.setNotes(TEST_IDENTIFIER_1_NOTES);
        TEST_IDENTIFIER_1.setStartDate(TEST_IDENTIFIER_1_START_DATE);
        TEST_IDENTIFIER_1.setEndDate(TEST_IDENTIFIER_1_END_DATE);
        TEST_IDENTIFIER_1.setIdentifierType(TEST_IDENTIFIER_TYPE);

        TEST_IDENTIFIER_2.setEntity(TEST_ENTITY_1);
        TEST_IDENTIFIER_2.setIdentifier(TEST_IDENTIFIER_2_IDENTIFIER);
        TEST_IDENTIFIER_2.setNotes(TEST_IDENTIFIER_2_NOTES);
        TEST_IDENTIFIER_2.setStartDate(TEST_IDENTIFIER_2_START_DATE);
        TEST_IDENTIFIER_2.setEndDate(TEST_IDENTIFIER_2_END_DATE);
        TEST_IDENTIFIER_2.setIdentifierType(TEST_IDENTIFIER_TYPE);
    }
}
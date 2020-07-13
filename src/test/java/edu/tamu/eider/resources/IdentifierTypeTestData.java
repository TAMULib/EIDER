package edu.tamu.eider.resources;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import edu.tamu.eider.ApiDocumentation;
import edu.tamu.eider.app.model.IdentifierType;

public class IdentifierTypeTestData extends ApiDocumentation {

    protected static final String TEST_IDENTIFIER_TYPE_1_NAME = "Test Identifier Type 1";
    protected static final String TEST_IDENTIFIER_TYPE_1_NAMESPACE_STRING = "http://www.example1.com";
    protected static final String TEST_IDENTIFIER_TYPE_2_NAME = "Test Identifier Type 1";
    protected static final String TEST_IDENTIFIER_TYPE_2_NAMESPACE_STRING = "http://www.example2.com";

    protected static final IdentifierType TEST_IDENTIFIER_TYPE_1 = new IdentifierType();
    protected static final IdentifierType TEST_IDENTIFIER_TYPE_2 = new IdentifierType();

    protected static final RequestFieldsSnippet TEST_IDENTIFIER_TYPE_REQUEST_FIELDS = requestFields(
        fieldWithPath("name").description("The name of the Identifier Type"),
        fieldWithPath("namespace").description("The namespace associated with the Identifier Type")
    );

    protected static final ResponseFieldsSnippet TEST_IDENTIFIER_TYPE_RESPONSE_FIELDS = responseFields(
        fieldWithPath("id").description("The UUID id of the Identifier Type"),
        fieldWithPath("name").description("The name of the Identifier Type"),
        fieldWithPath("namespace").description("The namespace associated with the Identifier Type"),
        subsectionWithPath("_links").description("A list of links associated with the Identifier Type")
    );

    protected static final LinksSnippet TEST_IDENTIFIER_TYPE_LINKS = links(
        halLinks(),
        linkWithRel("self").description("The canoncial url for this Identifier Type"),
        linkWithRel("identifierType").description("A link to the Identifier Type resource")
    );

    static {
        try {
            TEST_IDENTIFIER_TYPE_1.setNamespace(new URL(TEST_IDENTIFIER_TYPE_1_NAMESPACE_STRING));
            TEST_IDENTIFIER_TYPE_2.setNamespace(new URL(TEST_IDENTIFIER_TYPE_2_NAMESPACE_STRING));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TEST_IDENTIFIER_TYPE_1.setName(TEST_IDENTIFIER_TYPE_1_NAME);
        TEST_IDENTIFIER_TYPE_2.setName(TEST_IDENTIFIER_TYPE_2_NAME);
    }
}
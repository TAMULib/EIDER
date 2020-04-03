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
import edu.tamu.eider.app.model.Entity;

public class EntityTestData extends ApiDocumentation {

    protected static final String TEST_ENTITY_1_CANONICAL_NAME = "Example 1";
    protected static final String TEST_ENTITY_1_NOTES = "Example 1 notes";
    protected static final String TEST_ENTITY_1_URL_STRING = "http://www.example.com/1";
    protected static final String TEST_ENTITY_2_CANONICAL_NAME = "Example 2";
    protected static final String TEST_ENTITY_2_NOTES = "Example 2 notes";
    protected static final String TEST_ENTITY_2_URL_STRING = "http://www.example.com/2";

    protected static final Entity TEST_ENTITY_1 = new Entity();
    protected static final Entity TEST_ENTITY_2 = new Entity();

    protected static final RequestFieldsSnippet ENTITY_REQUEST_FIELDS_SNIPPET = requestFields(
        fieldWithPath("url").description("The URL of the Entity"),
        fieldWithPath("canonicalName").description("The canonical name for the Entity"),
        fieldWithPath("notes").description("Notes describing the Entity")
    );

    protected static final ResponseFieldsSnippet ENTITY_RESPONSE_FIELDS_SNIPPET = responseFields(
        fieldWithPath("id").description("The UUID id of the Entity"),
        fieldWithPath("url").description("The URL of the Entity"),
        fieldWithPath("canonicalName").description("The canonical name for the Entity"),
        fieldWithPath("notes").description("Notes describing the Entity"),
        subsectionWithPath("_links").description("A list of links associated with the Entity")
    );

    protected static final LinksSnippet ENTITY_COLLECTION_LINKS = links(
        halLinks(),
        linkWithRel("self").description("A link to the Entity collection"),
        linkWithRel("profile").description("A link to the profile for the Entity collection"),
        linkWithRel("findByUrl").description("Returns the Entity that either contains the given URL, or is associated with the Identifier that does"),
        linkWithRel("findByName").description("Returns a list of Entities associated with a Name that matches the given value")
    );

    protected static final LinksSnippet ENTITY_LINKS = links(
        halLinks(),
        linkWithRel("self").description("The canonical url for this Entity"),
        linkWithRel("entity").description("A link to the Entity resource"),
        linkWithRel("redirect").description("Redirects request to the URL associated with the Entity"),
        linkWithRel("redirectByHead").description("Redirects HEAD requests to the URL associated with the Entity")
    );

    static {
        try {
            TEST_ENTITY_1.setUrl(new URL(TEST_ENTITY_1_URL_STRING));
            TEST_ENTITY_2.setUrl(new URL(TEST_ENTITY_2_URL_STRING));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TEST_ENTITY_1.setCanonicalName(TEST_ENTITY_1_CANONICAL_NAME);
        TEST_ENTITY_1.setNotes(TEST_ENTITY_1_NOTES);
        TEST_ENTITY_2.setCanonicalName(TEST_ENTITY_2_CANONICAL_NAME);
        TEST_ENTITY_2.setNotes(TEST_ENTITY_2_NOTES);
    }
}

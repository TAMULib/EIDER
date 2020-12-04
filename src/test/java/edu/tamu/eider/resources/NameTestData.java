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
import java.time.LocalDate;

import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import edu.tamu.eider.ApiDocumentation;
import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Name;

public class NameTestData extends ApiDocumentation {

    protected static final String TEST_ENTITY_1_CANONICAL_NAME = "Example 1";
    protected static final String TEST_ENTITY_1_NOTES = "Example 1 notes";
    protected static final String TEST_ENTITY_1_URL_STRING = "http://www.example.com/1";
    protected static final String TEST_NAME_1_NAME = "Test Name 1";
    protected static final String TEST_NAME_1_NOTES = "Test Notes for Name 1";
    protected static final String TEST_NAME_2_NAME = "Test Name 2";
    protected static final String TEST_NAME_2_NOTES = "Test Notes for Name 2";

    protected static final LocalDate TEST_NAME_1_START_DATE = LocalDate.of(2020, 1, 1);
    protected static final LocalDate TEST_NAME_1_END_DATE = LocalDate.of(2020, 4, 15);
    protected static final LocalDate TEST_NAME_2_START_DATE = LocalDate.of(1990, 2, 4);
    protected static final LocalDate TEST_NAME_2_END_DATE = LocalDate.of(2010, 9, 1);

    protected static final Entity TEST_ENTITY_1 = new Entity();
    protected static final Name TEST_NAME_1 = new Name(TEST_NAME_1_NAME, TEST_NAME_1_NOTES, TEST_ENTITY_1);
    protected static final Name TEST_NAME_2 = new Name(TEST_NAME_2_NAME, TEST_NAME_2_NOTES, TEST_ENTITY_1);

    protected static final RequestFieldsSnippet NAME_REQUEST_FIELDS = requestFields(
        fieldWithPath("name").description("The name property of the Name"),
        fieldWithPath("notes").description("The notes used to describe the Name"),
        fieldWithPath("startDate").description("The date the Name became active"),
        fieldWithPath("endDate").description("The date the Name became inactive"),
        fieldWithPath("entity").description("The Entity associated with the Name")
    );

    protected static final ResponseFieldsSnippet NAME_RESPONSE_FIELDS = responseFields(
        fieldWithPath("id").description("The UUID id of the Name"),
        fieldWithPath("name").description("The name property of the Name"),
        fieldWithPath("notes").description("The notes used to describe the Name"),
        fieldWithPath("startDate").description("The date the Name became active"),
        fieldWithPath("endDate").description("The date the Name became inactive"),
        subsectionWithPath("_links").description("A list of links associated with the Name")
    );

    protected static final LinksSnippet NAME_LINKS = links(
        halLinks(),
        linkWithRel("self").description("The canoncial url for this Name"),
        linkWithRel("name").description("A link to the Name resource"),
        linkWithRel("entity").description("A link to the Entity associated with this Name")
    );

    static {
        try {
            TEST_ENTITY_1.setUrl(new URL(TEST_ENTITY_1_URL_STRING));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TEST_NAME_1.setStartDate(TEST_NAME_1_START_DATE);
        TEST_NAME_1.setEndDate(TEST_NAME_1_END_DATE);
        TEST_NAME_2.setStartDate(TEST_NAME_2_START_DATE);
        TEST_NAME_2.setEndDate(TEST_NAME_2_END_DATE);
    }

}

package edu.tamu.eider.repository;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import edu.tamu.eider.ApiDocumentation;
import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.repo.EntityRepository;

public class EntityCrudTest extends ApiDocumentation {

    private static final String TEST_ENTITY_1_CANONICAL_NAME = "Example 1";
    private static final String TEST_ENTITY_1_NOTES = "Example 1 notes";
    private static final String TEST_ENTITY_1_URL_STRING = "http://www.example.com/1";
    private static final String TEST_ENTITY_2_CANONICAL_NAME = "Example 2";
    private static final String TEST_ENTITY_2_NOTES = "Example 2 notes";
    private static final String TEST_ENTITY_2_URL_STRING = "http://www.example.com/2";

    private static final Entity TEST_ENTITY_1 = new Entity();
    private static final Entity TEST_ENTITY_2 = new Entity();

    private static final RequestFieldsSnippet ENTITY_REQUEST_FIELDS_SNIPPET = requestFields(
        fieldWithPath("url").description("The URL of the Entity"),
        fieldWithPath("canonicalName").description("The canonical name for the Entity"),
        fieldWithPath("notes").description("Notes describing the Entity")
    );

    private static final ResponseFieldsSnippet ENTITY_RESPONSE_FIELDS_SNIPPET = responseFields(
        fieldWithPath("id").description("The UUID id of the Entity"),
        fieldWithPath("url").description("The URL of the Entity"),
        fieldWithPath("canonicalName").description("The canonical name for the Entity"),
        fieldWithPath("notes").description("Notes describing the Entity"),
        subsectionWithPath("_links").description("A list of links associated with the Entity")
    );

    private static final LinksSnippet ENTITY_COLLECTION_LINKS = links(
        halLinks(),
        linkWithRel("self").description("A link to the Entity collection"),
        linkWithRel("profile").description("A link to the profile for the Entity collection"),
        linkWithRel("findByUrl").description("Returns the Entity that either contains the given URL, or is associated with the Identifier that does"),
        linkWithRel("findByName").description("Returns a list of Entities associated with a Name that matches the given value")
    );

    private static final LinksSnippet ENTITY_LINKS = links(
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

    @Autowired
    private EntityRepository entityRepo;

    @Test
    public void testGetAllEntities() throws Exception {
        entityRepo.save(TEST_ENTITY_1);
        entityRepo.save(TEST_ENTITY_2);
        this.mockMvc
            .perform(get("/entity")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "25")
                .param("sort", "desc"))
            .andExpect(status().isOk())
            .andDo(document("get-all-entities",
                requestParameters(
                    parameterWithName("page").description("The page of Entities to retrieve"),
                    parameterWithName("size").description("The number of Entities per page"),
                    parameterWithName("sort").description("The way the returned page should be sorted")
                ),
                responseFields(
                    subsectionWithPath("_embedded").description("A list of the Entities on the page"),
                    subsectionWithPath("_links").description("A list of links associated with the Enttiy collection"),
                    subsectionWithPath("page").description("A description of the attributes of the page")
                ),
                ENTITY_COLLECTION_LINKS
            ));
    }

    @Test
    public void testGetEntity() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc
            .perform(get("/entity/{id}", entity.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(entity.getId().toString()))
            .andExpect(jsonPath("url").value(TEST_ENTITY_1_URL_STRING))
            .andExpect(jsonPath("canonicalName").value(TEST_ENTITY_1_CANONICAL_NAME))
            .andExpect(jsonPath("notes").value(TEST_ENTITY_1_NOTES))
            .andDo(document("get-entity",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Entity to be retrieved")
                ),
                ENTITY_RESPONSE_FIELDS_SNIPPET,
                ENTITY_LINKS
            ));
    }

    @Test
    public void testCreateEntity() throws Exception {
        this.mockMvc
            .perform(post("/entity")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(TEST_ENTITY_1)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("url").value(TEST_ENTITY_1_URL_STRING))
            .andExpect(jsonPath("canonicalName").value(TEST_ENTITY_1_CANONICAL_NAME))
            .andExpect(jsonPath("notes").value(TEST_ENTITY_1_NOTES))
            .andDo(document("create-entity",
                ENTITY_REQUEST_FIELDS_SNIPPET,
                ENTITY_RESPONSE_FIELDS_SNIPPET,
                ENTITY_LINKS
            ));
    }

    @Test
    public void testReplaceEntity() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc.perform(put("/entity/{id}", entity.getId().toString())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(TEST_ENTITY_2))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(entity.getId().toString()))
        .andExpect(jsonPath("url").value(TEST_ENTITY_2_URL_STRING))
        .andExpect(jsonPath("canonicalName").value(TEST_ENTITY_2_CANONICAL_NAME))
        .andExpect(jsonPath("notes").value(TEST_ENTITY_2_NOTES))
        .andDo(document("replace-entity",
            pathParameters(
                parameterWithName("id").description("The UUID id of the Entity to be replaced")
            ),
            ENTITY_REQUEST_FIELDS_SNIPPET,
            ENTITY_RESPONSE_FIELDS_SNIPPET,
            ENTITY_LINKS
        ));
    }

    @Test
    public void testUpdateEntity() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc.perform(patch("/entity/{id}", entity.getId().toString())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(TEST_ENTITY_2))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(entity.getId().toString()))
        .andExpect(jsonPath("url").value(TEST_ENTITY_2_URL_STRING))
        .andExpect(jsonPath("canonicalName").value(TEST_ENTITY_2_CANONICAL_NAME))
        .andExpect(jsonPath("notes").value(TEST_ENTITY_2_NOTES))
        .andDo(document("update-entity",
            pathParameters(
                parameterWithName("id").description("The UUID id of the Entity to be replaced")
            ),
            ENTITY_REQUEST_FIELDS_SNIPPET,
            ENTITY_RESPONSE_FIELDS_SNIPPET,
            ENTITY_LINKS
        ));
    }

    @Test
    public void testDeleteEntity() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc.perform(delete("/entity/{id}", entity.getId().toString()))
            .andExpect(status().isNoContent())
            .andDo(document("delete-entity",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Entity to be replaced")
                )
            ));
    }

    @AfterEach
    public void cleanUp() {
        entityRepo.deleteAll();
    }
}
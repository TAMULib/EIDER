package edu.tamu.eider.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.head;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Name;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.NameRepository;
import edu.tamu.eider.resources.EntityTestData;

public class EntityControllerTest extends EntityTestData {

    private static String TEST_NAME_NAME = "Name";
    private static String TEST_NAME_1_NOTES = "First Name";
    private static String TEST_NAME_2_NOTES = "Second Name";

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private NameRepository nameRepo;

    @Test
    public void testRedirectHeadToEntity() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc
            .perform(head("/entity/{id}", entity.getId().toString()))
            .andExpect(status().isFound())
            .andDo(document("redirect-head-to-entity",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Entity whose URL the response will redirect to")
                )
            ));
    }

    @Test
    public void testFindByUrl() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc
            .perform(get("/entity/url")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("url", entity.getUrl().toString())
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(entity.getId().toString()))
            .andExpect(jsonPath("url").value(TEST_ENTITY_1_URL_STRING))
            .andExpect(jsonPath("canonicalName").value(TEST_ENTITY_1_CANONICAL_NAME))
            .andExpect(jsonPath("notes").value(TEST_ENTITY_1_NOTES))
            .andDo(document("find-by-url",
                requestParameters(
                    parameterWithName("url").description("The URL on the desired Entity, or on an associated Identifier")
                ),
                ENTITY_RESPONSE_FIELDS_SNIPPET,
                ENTITY_LINKS
            
            ));
    }

    @Test
    public void testfindByName() throws Exception {
        Entity entity1 = entityRepo.save(TEST_ENTITY_1);
        Entity entity2 = entityRepo.save(TEST_ENTITY_2);
        nameRepo.save(new Name(TEST_NAME_NAME, TEST_NAME_1_NOTES, entity1));
        nameRepo.save(new Name(TEST_NAME_NAME, TEST_NAME_2_NOTES, entity2));
        this.mockMvc
            .perform(get("/entity/name")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", TEST_NAME_NAME)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.entity", hasSize(2)))
            .andDo(document("find-by-name",
                requestParameters(
                    parameterWithName("name").description("Name value to find associated Entities by")
                ),
                responseFields(
                    fieldWithPath("_embedded").description("HAL wrapper indicating results are full enitities"),
                    subsectionWithPath("_embedded.entity").description("A set of all matching Entities")
                )
            ));
    }

    @Test
    public void testRedirectGetToEntity() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc
            .perform(get("/entity/{id}/redirect", entity.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isFound())
            .andDo(document("redirect-get-to-entity",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Entity whose URL the response will redirect to")
                )
            ));

    }

    @AfterEach
    public void cleanUp() {
        nameRepo.deleteAll();
        entityRepo.deleteAll();
        // Entity id is somehow being set before the create entity test
        TEST_ENTITY_1.setId(null);
        TEST_ENTITY_2.setId(null);
    }
}

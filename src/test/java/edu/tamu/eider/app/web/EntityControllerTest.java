package edu.tamu.eider.app.web;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.Name;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;
import edu.tamu.eider.app.model.repo.NameRepository;
import edu.tamu.eider.resources.EntityTestData;

public class EntityControllerTest extends EntityTestData {

    private static String TEST_NAME_NAME = "Name";
    private static String TEST_NAME_1_NOTES = "First Name";
    private static String TEST_NAME_2_NOTES = "Second Name";

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    @Autowired
    private NameRepository nameRepo;

    @Test
    public void testFindByUrl() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc
            .perform(get("/entity/url")
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("url", entity.getUrl().toString())
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
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
    public void testFindByUrlFromIdentifier() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        Identifier identifier = identifierRepo.save(TEST_IDENTIFIER);
        this.mockMvc
            .perform(get("/entity/url")
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("url", identifier.getIdentifier())
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("id").value(entity.getId().toString()))
            .andExpect(jsonPath("url").value(TEST_ENTITY_1_URL_STRING))
            .andExpect(jsonPath("canonicalName").value(TEST_ENTITY_1_CANONICAL_NAME))
            .andExpect(jsonPath("notes").value(TEST_ENTITY_1_NOTES));
    }

    @Test
    public void testFindByUrlWithoutMatch() throws Exception {
        this.mockMvc
            .perform(get("/entity/url").param("url", TEST_ENTITY_2_URL_STRING))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testfindByName() throws Exception {
        Entity entity1 = entityRepo.save(TEST_ENTITY_1);
        Entity entity2 = entityRepo.save(TEST_ENTITY_2);
        nameRepo.save(new Name(TEST_NAME_NAME, TEST_NAME_1_NOTES, entity1));
        nameRepo.save(new Name(TEST_NAME_NAME, TEST_NAME_2_NOTES, entity2));
        this.mockMvc
            .perform(get("/entity/name")
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", TEST_NAME_NAME)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
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

    @AfterEach
    public void cleanUp() {
        identifierRepo.deleteAll();
        nameRepo.deleteAll();
        entityRepo.deleteAll();
        // null out entity id as entity id is assigned after save
        TEST_ENTITY_1.setId(null);
        TEST_ENTITY_2.setId(null);
    }

}

package edu.tamu.eider.repository;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.resources.EntityTestData;

public class EntityCrudTest extends EntityTestData {

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
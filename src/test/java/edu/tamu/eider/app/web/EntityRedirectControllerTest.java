package edu.tamu.eider.app.web;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.head;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;
import edu.tamu.eider.resources.EntityTestData;

public class EntityRedirectControllerTest extends EntityTestData {

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    @Test
    public void testHeadRedirectEntity() throws Exception {
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
    public void testHeadRedirectEntityWithoutMatch() throws Exception {
        this.mockMvc
            .perform(head("/entity/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGetRedirectEntity() throws Exception {
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

    @Test
    public void testGetRedirectEntityWithoutMatch() throws Exception {
        this.mockMvc
            .perform(get("/entity/{id}/redirect", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @AfterEach
    public void cleanUp() {
        identifierRepo.deleteAll();
        entityRepo.deleteAll();
        TEST_ENTITY_1.setId(null);
        TEST_ENTITY_2.setId(null);
    }

}

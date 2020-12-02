package edu.tamu.eider.app.web;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.head;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;
import edu.tamu.eider.resources.EntityTestData;

public class IdentifierRedirectControllerTest extends EntityTestData {

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    @Test
    public void testHeadRedirectIdentifier() throws Exception {
        entityRepo.save(TEST_ENTITY_1);
        Identifier identifier = identifierRepo.save(TEST_IDENTIFIER);
        this.mockMvc
            .perform(head("/identifier").param("url", identifier.getIdentifier()))
            .andExpect(status().isFound())
            .andDo(document("redirect-head-to-identifier-entity",
                requestParameters(
                    parameterWithName("url").description("The URL identifier of the Identifier whose associated Entity URL should be used for the redirect")
                )
            ));
    }

    @Test
    public void testHeadRedirectIdentifierWithoutMatch() throws Exception {
        this.mockMvc
            .perform(head("/identifier").param("url", TEST_ENTITY_2_URL_STRING))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGetRedirectIdentifier() throws Exception {
        entityRepo.save(TEST_ENTITY_1);
        Identifier identifier = identifierRepo.save(TEST_IDENTIFIER);
        this.mockMvc
            .perform(get("/identifier/redirect")
                .param("url", identifier.getIdentifier())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isFound())
            .andDo(document("redirect-get-to-identifier-entity",
                requestParameters(
                    parameterWithName("url").description("The URL identifier of the Identifier whose associated Entity URL should be used for the redirect")
                )
            ));

    }

    @Test
    public void testGetRedirectIdentifierWithoutMatch() throws Exception {
        this.mockMvc
            .perform(get("/identifier/{id}/redirect", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @AfterEach
    public void cleanUp() {
        identifierRepo.deleteAll();
        entityRepo.deleteAll();
        // null out entity id as entity id is assigned after save
        TEST_ENTITY_1.setId(null);
    }

}

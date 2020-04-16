package edu.tamu.eider.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.head;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;
import edu.tamu.eider.resources.EntityTestData;

public class IdentifierControllerTest extends EntityTestData {

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    @Test
    public void testRedirectHeadToEntity() throws Exception {
        entityRepo.save(TEST_ENTITY_1);
        Identifier identifier = identifierRepo.save(TEST_IDENTIFIER);
        this.mockMvc
            .perform(head("/identifier").param("url", identifier.getIdentifier()))
            .andExpect(status().isFound())
            .andDo(document("redirect-head-to-entity",
                requestParameters(
                    parameterWithName("url").description("The URL identifier of the Identifier whose associated Entity should be used for the redirect")
                )
            ));
    }

    @Test
    public void testRedirectHeadToEntityWithoutMatch() throws Exception {
        this.mockMvc
            .perform(head("/identifier").param("url", TEST_ENTITY_2_URL_STRING))
            .andExpect(status().isNotFound());
    }

    @AfterEach
    public void cleanUp() {
        TEST_ENTITY_1.setId(null);
        identifierRepo.deleteAll();
        entityRepo.deleteAll();
    }
}
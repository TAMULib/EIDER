package edu.tamu.eider.repository;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.app.model.repo.IdentifierRepository;
import edu.tamu.eider.app.model.repo.IdentifierTypeRepository;
import edu.tamu.eider.resources.IdentifierTestData;

public class IdentifierCrudTest extends IdentifierTestData {

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private IdentifierRepository identifierRepo;

    @Autowired
    private IdentifierTypeRepository identifierTypeRepo;

    @Test
    public void testGetAllIdentifiers() throws Exception {
        identifierTypeRepo.save(TEST_IDENTIFIER_TYPE);
        entityRepo.save(TEST_ENTITY_1);
        identifierRepo.save(TEST_IDENTIFIER_1);
        identifierRepo.save(TEST_IDENTIFIER_2);
        this.mockMvc
            .perform(get("/identifier")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "25")
                .param("sort", "desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andDo(document("get-all-identifiers",
                requestParameters(
                    parameterWithName("page").description("The page of Identifiers to retrieve"),
                    parameterWithName("size").description("The number of Identifiers per page"),
                    parameterWithName("sort").description("The way the returned page should be sorted")
                ),
                responseFields(
                    subsectionWithPath("_embedded").description("A list of the Identifiers on the page"),
                    subsectionWithPath("_links").description("A list of links associated with the Identifier collection"),
                    subsectionWithPath("page").description("A description of the attributes of the page")
                )
            ));
    }

    @Test
    public void testGetIdentifier() throws Exception {
        identifierTypeRepo.save(TEST_IDENTIFIER_TYPE);
        entityRepo.save(TEST_ENTITY_1);
        Identifier identifier = identifierRepo.save(TEST_IDENTIFIER_1);
        this.mockMvc
            .perform(get("/identifier/{id}", identifier.getId().toString())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("id").value(identifier.getId().toString()))
            .andExpect(jsonPath("identifier").value(TEST_IDENTIFIER_1_IDENTIFIER))
            .andDo(document("get-identifier",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Identifer to be retrieved")
                )
            ));
    }

    @AfterEach
    public void cleanUp() {
        identifierRepo.deleteAll();
        entityRepo.deleteAll();
        identifierTypeRepo.deleteAll();
        TEST_ENTITY_1.setId(null);
        TEST_IDENTIFIER_TYPE.setId(null);
    }
}
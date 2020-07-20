package edu.tamu.eider.repository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

import edu.tamu.eider.app.model.IdentifierType;
import edu.tamu.eider.app.model.repo.IdentifierTypeRepository;
import edu.tamu.eider.resources.IdentifierTypeTestData;

public class IdentifierTypeCrudTest extends IdentifierTypeTestData {

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    @Autowired
    private IdentifierTypeRepository identifierTypeRepo;

    @Test
    public void testGetAllIdentifierTypes() throws Exception {
        identifierTypeRepo.save(TEST_IDENTIFIER_TYPE_1);
        identifierTypeRepo.save(TEST_IDENTIFIER_TYPE_2);
        this.mockMvc
            .perform(get("/identifierType")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "25")
                .param("sort", "desc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.identifierType", hasSize(2)))
            .andDo(document("get-all-identifier-types",
                requestParameters(
                    parameterWithName("page").description("The page of Identifier Types to retrieve"),
                    parameterWithName("size").description("The number of Identifier Types per page"),
                    parameterWithName("sort").description("The way the returned page should be sorted")
                ),
                responseFields(
                    subsectionWithPath("_embedded").description("A list of the Identifier Types on the page"),
                    subsectionWithPath("_links").description("A list of links associated with the Identifier Type collection"),
                    subsectionWithPath("page").description("A description of the attributes of the page")
                ),
                links(
                    halLinks(),
                    linkWithRel("self").description("The canonical URL for this Identifier Type"),
                    linkWithRel("profile").description("A link to the Identifier Type resource")
                )
            ));
    }

    @Test
    public void testGetIdentifierType() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE_1);
        this.mockMvc
            .perform(get("/identifierType/{id}", identifierType.getId().toString())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("name").value(TEST_IDENTIFIER_TYPE_1_NAME))
            .andExpect(jsonPath("namespace").value(TEST_IDENTIFIER_TYPE_1_NAMESPACE_STRING))
            .andDo(document("get-identifier-type",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Identifier Type to be retrieved")
                ),
                TEST_IDENTIFIER_TYPE_RESPONSE_FIELDS,
                TEST_IDENTIFIER_TYPE_LINKS
            ));
    }

    @Test
    public void testCreateIdentifierType() throws Exception {
        this.mockMvc
            .perform(post("/identifierType")
            .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(TEST_IDENTIFIER_TYPE_1))
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("name").value(TEST_IDENTIFIER_TYPE_1_NAME))
            .andExpect(jsonPath("namespace").value(TEST_IDENTIFIER_TYPE_1_NAMESPACE_STRING))
            .andDo(document("create-identifier-type",
                TEST_IDENTIFIER_TYPE_REQUEST_FIELDS,
                TEST_IDENTIFIER_TYPE_RESPONSE_FIELDS,
                TEST_IDENTIFIER_TYPE_LINKS
            ));
    }

    @Test
    public void testReplaceIdentifierType() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE_1);
        this.mockMvc
            .perform(put("/identifierType/{id}", identifierType.getId().toString())
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(TEST_IDENTIFIER_TYPE_2))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("name").value(TEST_IDENTIFIER_TYPE_2_NAME))
            .andExpect(jsonPath("namespace").value(TEST_IDENTIFIER_TYPE_2_NAMESPACE_STRING))
            .andDo(document("replace-identifier-type",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Identifier to be replaced")
                ),
                TEST_IDENTIFIER_TYPE_REQUEST_FIELDS,
                TEST_IDENTIFIER_TYPE_RESPONSE_FIELDS,
                TEST_IDENTIFIER_TYPE_LINKS
            ));
    }

    @Test
    public void testUpdateIdentifierType() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE_1);
        this.mockMvc
            .perform(patch("/identifierType/{id}", identifierType.getId().toString())
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(TEST_IDENTIFIER_TYPE_2))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("name").value(TEST_IDENTIFIER_TYPE_2_NAME))
            .andExpect(jsonPath("namespace").value(TEST_IDENTIFIER_TYPE_2_NAMESPACE_STRING))
            .andDo(document("update-identifier-type",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Identifier to be updated")
                ),
                TEST_IDENTIFIER_TYPE_REQUEST_FIELDS,
                TEST_IDENTIFIER_TYPE_RESPONSE_FIELDS,
                TEST_IDENTIFIER_TYPE_LINKS
            ));
    }
    
    @Test
    public void testDeleteIdentifierType() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE_1);
        this.mockMvc
            .perform(delete("/identifierType/{id}", identifierType.getId().toString())
                .with(httpBasic(username, password))
            )
            .andExpect(status().isNoContent())
            .andDo(document("delete-identifier-type",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Identifier Type to be deleted")
                )
            ));
    }

    @AfterEach
    public void cleanUp() {
        TEST_IDENTIFIER_TYPE_1.setId(null);
        TEST_IDENTIFIER_TYPE_2.setId(null);
        identifierTypeRepo.deleteAll();
    }
}
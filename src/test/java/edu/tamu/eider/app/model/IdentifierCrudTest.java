package edu.tamu.eider.app.model;

import static org.hamcrest.core.StringContains.containsStringIgnoringCase;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

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
                ),
                links(
                    halLinks(),
                    linkWithRel("self").description("The canonical URL for this Identifier"),
                    linkWithRel("profile").description("A link to the Identifier resource")
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
            .andExpect(jsonPath("startDate").value(TEST_IDENTIFIER_1_START_DATE.toString()))
            .andExpect(jsonPath("endDate").value(TEST_IDENTIFIER_1_END_DATE.toString()))
            .andDo(document("get-identifier",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Identifer to be retrieved")
                ),
                IDENTIFIER_RESPONSE_FIELDS,
                IDENTIFIER_LINKS
            ));
    }

    @Test
    public void testCreateIdentifier() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE);
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        Map<String, String> identifierMap = new HashMap<>();
        identifierMap.put("identifier", TEST_IDENTIFIER_1_IDENTIFIER);
        identifierMap.put("notes", TEST_IDENTIFIER_1_NOTES);
        identifierMap.put("startDate", TEST_IDENTIFIER_1_START_DATE.toString());
        identifierMap.put("endDate", TEST_IDENTIFIER_1_END_DATE.toString());
        identifierMap.put("entity", linkTo(EntityRepository.class).slash(entity.getId()).withSelfRel().getHref());
        identifierMap.put("identifierType", linkTo(IdentifierTypeRepository.class).slash(identifierType.getId()).withSelfRel().getHref());
        this.mockMvc
            .perform(post("/identifier")
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(identifierMap))
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("identifier").value(TEST_IDENTIFIER_1_IDENTIFIER))
            .andExpect(jsonPath("notes").value(TEST_IDENTIFIER_1_NOTES))
            .andExpect(jsonPath("startDate").value(TEST_IDENTIFIER_1_START_DATE.toString()))
            .andExpect(jsonPath("endDate").value(TEST_IDENTIFIER_1_END_DATE.toString()))
            .andDo(document("create-identifier",
                IDENTIFIER_REQUEST_FIELDS,
                IDENTIFIER_RESPONSE_FIELDS,
                IDENTIFIER_LINKS
            ));
    }

    @Test
    public void testCreateIdentifierWithDuplicateidentifier() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE);
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        Identifier identifier = identifierRepo.save(TEST_IDENTIFIER_1);
        Map<String, String> identifierMap = new HashMap<>();
        identifierMap.put("identifier", identifier.getIdentifier());
        identifierMap.put("notes", identifier.getNotes());
        identifierMap.put("startDate", identifier.getStartDate().toString());
        identifierMap.put("endDate", identifier.getEndDate().toString());
        identifierMap.put("entity", linkTo(EntityRepository.class).slash(entity.getId()).withSelfRel().getHref());
        identifierMap.put("identifierType", linkTo(IdentifierTypeRepository.class).slash(identifierType.getId()).withSelfRel().getHref());
        this.mockMvc
            .perform(post("/identifier")
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(identifierMap))
            )
            .andExpect(status().isConflict());
    }

    @Test
    public void testCreateIdentifierWithDuplicateEntityUrl() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE);
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        Map<String, String> identifierMap = new HashMap<>();
        identifierMap.put("identifier", TEST_ENTITY_1_URL_STRING);
        identifierMap.put("notes", TEST_IDENTIFIER_1_NOTES);
        identifierMap.put("startDate", TEST_IDENTIFIER_1_START_DATE.toString());
        identifierMap.put("endDate", TEST_IDENTIFIER_1_END_DATE.toString());
        identifierMap.put("entity", linkTo(EntityRepository.class).slash(entity.getId()).withSelfRel().getHref());
        identifierMap.put("identifierType", linkTo(IdentifierTypeRepository.class).slash(identifierType.getId()).withSelfRel().getHref());
        this.mockMvc
            .perform(post("/identifier")
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(identifierMap))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testReplaceIdentifier() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE);
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        TEST_IDENTIFIER_1.setEntity(entity);
        TEST_IDENTIFIER_1.setIdentifierType(identifierType);
        Identifier identifier = identifierRepo.save(TEST_IDENTIFIER_1);
        Map<String, String> identifierMap = new HashMap<>();
        identifierMap.put("identifier", TEST_IDENTIFIER_2_IDENTIFIER);
        identifierMap.put("notes", TEST_IDENTIFIER_2_NOTES);
        identifierMap.put("startDate", TEST_IDENTIFIER_2_START_DATE.toString());
        identifierMap.put("endDate", TEST_IDENTIFIER_2_END_DATE.toString());
        identifierMap.put("entity", linkTo(EntityRepository.class).slash(entity.getId()).withSelfRel().getHref());
        identifierMap.put("identifierType", linkTo(IdentifierTypeRepository.class).slash(identifierType.getId()).withSelfRel().getHref());
        this.mockMvc
            .perform(put("/identifier/{id}", identifier.getId())
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(identifierMap))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("id").value(identifier.getId().toString()))
            .andExpect(jsonPath("notes").value(TEST_IDENTIFIER_2_NOTES))
            .andExpect(jsonPath("identifier").value(TEST_IDENTIFIER_2_IDENTIFIER))
            .andExpect(jsonPath("startDate").value(TEST_IDENTIFIER_2_START_DATE.toString()))
            .andExpect(jsonPath("endDate").value(TEST_IDENTIFIER_2_END_DATE.toString()))
            .andExpect(jsonPath("_links.entity.href", containsStringIgnoringCase(identifier.getId().toString())))
            .andExpect(jsonPath("_links.identifierType.href", containsStringIgnoringCase(identifier.getId().toString())))
            .andDo(document("replace-identifier",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Identifier to be replaced")
                ),
                IDENTIFIER_REQUEST_FIELDS,
                IDENTIFIER_RESPONSE_FIELDS,
                IDENTIFIER_LINKS
            ));
    }

    @Test
    public void testUpdateIdentifier() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE);
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        TEST_IDENTIFIER_1.setEntity(entity);
        TEST_IDENTIFIER_1.setIdentifierType(identifierType);
        Identifier identifier = identifierRepo.save(TEST_IDENTIFIER_1);
        Map<String, String> identifierMap = new HashMap<>();
        identifierMap.put("identifier", TEST_IDENTIFIER_2_IDENTIFIER);
        identifierMap.put("notes", TEST_IDENTIFIER_2_NOTES);
        identifierMap.put("startDate", TEST_IDENTIFIER_2_START_DATE.toString());
        identifierMap.put("endDate", TEST_IDENTIFIER_2_END_DATE.toString());
        identifierMap.put("entity", linkTo(EntityRepository.class).slash(entity.getId()).withSelfRel().getHref());
        identifierMap.put("identifierType", linkTo(IdentifierTypeRepository.class).slash(identifierType.getId()).withSelfRel().getHref());
        this.mockMvc
            .perform(patch("/identifier/{id}", identifier.getId())
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(identifierMap))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("id").value(identifier.getId().toString()))
            .andExpect(jsonPath("notes").value(TEST_IDENTIFIER_2_NOTES))
            .andExpect(jsonPath("identifier").value(TEST_IDENTIFIER_2_IDENTIFIER))
            .andExpect(jsonPath("startDate").value(TEST_IDENTIFIER_2_START_DATE.toString()))
            .andExpect(jsonPath("endDate").value(TEST_IDENTIFIER_2_END_DATE.toString()))
            .andExpect(jsonPath("_links.entity.href", containsStringIgnoringCase(identifier.getId().toString())))
            .andExpect(jsonPath("_links.identifierType.href", containsStringIgnoringCase(identifier.getId().toString())))
            .andDo(document("update-identifier",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Identifier to be replaced")
                ),
                IDENTIFIER_REQUEST_FIELDS,
                IDENTIFIER_RESPONSE_FIELDS,
                IDENTIFIER_LINKS
            ));
    }

    @Test
    public void testDeleteIdentifier() throws Exception {
        IdentifierType identifierType = identifierTypeRepo.save(TEST_IDENTIFIER_TYPE);
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        TEST_IDENTIFIER_1.setEntity(entity);
        TEST_IDENTIFIER_1.setIdentifierType(identifierType);
        Identifier identifier = identifierRepo.save(TEST_IDENTIFIER_1);
        this.mockMvc
            .perform(delete("/identifier/{id}", identifier.getId().toString())
                .with(httpBasic(username, password))
            )
            .andExpect(status().isNoContent())
            .andDo(document("delete-identifier",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Identifier to delete")
                )
            ));
    }

    @AfterEach
    public void cleanUp() {
        identifierRepo.deleteAll();
        identifierTypeRepo.deleteAll();
        entityRepo.deleteAll();
        TEST_ENTITY_1.setId(null);
        TEST_IDENTIFIER_1.setId(null);
        TEST_IDENTIFIER_TYPE.setId(null);
    }
}

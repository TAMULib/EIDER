package edu.tamu.eider.app.model;

import static org.hamcrest.Matchers.hasSize;
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
import edu.tamu.eider.app.model.repo.NameRepository;
import edu.tamu.eider.resources.NameTestData;

public class NameCrudTest extends NameTestData {

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    @Autowired
    private EntityRepository entityRepo;

    @Autowired
    private NameRepository nameRepo;

    @Test
    public void testGetAllNames() throws Exception {
        entityRepo.save(TEST_ENTITY_1);
        nameRepo.save(TEST_NAME_1);
        nameRepo.save(TEST_NAME_2);
        this.mockMvc
            .perform(get("/name")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "25")
                .param("sort", "desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("_embedded.name", hasSize(2)))
            .andDo(document("get-all-names",
                requestParameters(
                    parameterWithName("page").description("The page of Names to retrieve"),
                    parameterWithName("size").description("The number of Names per page"),
                    parameterWithName("sort").description("The way the returned page should be sorted")
                ),
                responseFields(
                    subsectionWithPath("_embedded").description("A list of the Names on the page"),
                    subsectionWithPath("_links").description("A list of links associated with the Name collection"),
                    subsectionWithPath("page").description("A description of the attributes of the page")
                ),
                links(
                    halLinks(),
                    linkWithRel("self").description("The canonical URL for this Name"),
                    linkWithRel("profile").description("A link to the Name resource")
                )
            ));
    }

    @Test
    public void testGetName() throws Exception {
        entityRepo.save(TEST_ENTITY_1);
        Name name = nameRepo.save(TEST_NAME_1);
        this.mockMvc
            .perform(get("/name/{id}", name.getId().toString())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("name").value(TEST_NAME_1_NAME))
            .andExpect(jsonPath("notes").value(TEST_NAME_1_NOTES))
            .andExpect(jsonPath("startDate").value(TEST_NAME_1_START_DATE.toString()))
            .andExpect(jsonPath("endDate").value(TEST_NAME_1_END_DATE.toString()))
            .andDo(document("get-name",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Name to be retrieved")
                ),
                NAME_RESPONSE_FIELDS,
                NAME_LINKS
            ));
    }

    @Test
    public void testCreateName() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("name", TEST_NAME_1_NAME);
        nameMap.put("notes", TEST_NAME_1_NOTES);
        nameMap.put("startDate", TEST_NAME_1_START_DATE.toString());
        nameMap.put("endDate", TEST_NAME_1_END_DATE.toString());
        nameMap.put("entity", linkTo(NameRepository.class).slash(entity.getId()).withSelfRel().getHref());
        this.mockMvc
            .perform(post("/name")
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(nameMap))
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("name").value(TEST_NAME_1_NAME))
            .andExpect(jsonPath("notes").value(TEST_NAME_1_NOTES))
            .andExpect(jsonPath("startDate").value(TEST_NAME_1_START_DATE.toString()))
            .andExpect(jsonPath("endDate").value(TEST_NAME_1_END_DATE.toString()))
            .andDo(document("create-name",
                NAME_REQUEST_FIELDS,
                NAME_RESPONSE_FIELDS,
                NAME_LINKS
            ));
    }

    @Test
    public void testReplaceName() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        Name name = nameRepo.save(TEST_NAME_1);
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("name", TEST_NAME_2_NAME);
        nameMap.put("notes", TEST_NAME_2_NOTES);
        nameMap.put("startDate", TEST_NAME_2_START_DATE.toString());
        nameMap.put("endDate", TEST_NAME_2_END_DATE.toString());
        nameMap.put("entity", linkTo(NameRepository.class).slash(entity.getId()).withSelfRel().getHref());
        this.mockMvc
            .perform(put("/name/{id}", name.getId().toString())
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(nameMap))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("id").value(name.getId().toString()))
            .andExpect(jsonPath("notes").value(TEST_NAME_2_NOTES))
            .andExpect(jsonPath("name").value(TEST_NAME_2_NAME))
            .andExpect(jsonPath("startDate").value(TEST_NAME_2_START_DATE.toString()))
            .andExpect(jsonPath("endDate").value(TEST_NAME_2_END_DATE.toString()))
            .andExpect(jsonPath("_links.entity.href", containsStringIgnoringCase(name.getId().toString())))
            .andDo(document("replace-name",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Name to be replaced")
                ),
                NAME_REQUEST_FIELDS,
                NAME_RESPONSE_FIELDS,
                NAME_LINKS
            ));
    }

    @Test
    public void testUpdateName() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        Name name = nameRepo.save(TEST_NAME_1);
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("name", TEST_NAME_2_NAME);
        nameMap.put("notes", TEST_NAME_2_NOTES);
        nameMap.put("startDate", TEST_NAME_2_START_DATE.toString());
        nameMap.put("endDate", TEST_NAME_2_END_DATE.toString());
        nameMap.put("entity", linkTo(NameRepository.class).slash(entity.getId()).withSelfRel().getHref());
        this.mockMvc
            .perform(patch("/name/{id}", name.getId())
                .with(httpBasic(username, password))
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(nameMap))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("id").value(name.getId().toString()))
            .andExpect(jsonPath("notes").value(TEST_NAME_2_NOTES))
            .andExpect(jsonPath("name").value(TEST_NAME_2_NAME))
            .andExpect(jsonPath("startDate").value(TEST_NAME_2_START_DATE.toString()))
            .andExpect(jsonPath("endDate").value(TEST_NAME_2_END_DATE.toString()))
            .andExpect(jsonPath("_links.entity.href", containsStringIgnoringCase(name.getId().toString())))
            .andDo(document("update-name",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Name to be replaced")
                ),
                NAME_REQUEST_FIELDS,
                NAME_RESPONSE_FIELDS,
                NAME_LINKS
            ));
    }

    @Test
    public void testDeleteName() throws Exception {
        entityRepo.save(TEST_ENTITY_1);
        Name name = nameRepo.save(TEST_NAME_1);
        this.mockMvc
            .perform(delete("/name/{id}", name.getId().toString())
                .with(httpBasic(username, password))
            )
            .andExpect(status().isNoContent())
            .andDo(document("delete-name",
                pathParameters(
                    parameterWithName("id").description("The UUID id of the Name to delete")
                )
            ));
    }

    @AfterEach
    public void cleanUp() {
        nameRepo.deleteAll();
        entityRepo.deleteAll();
        TEST_ENTITY_1.setId(null);
        TEST_NAME_1.setId(null);
        TEST_NAME_2.setId(null);
    }
}

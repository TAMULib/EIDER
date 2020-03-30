package edu.tamu.eider.controller;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import edu.tamu.eider.ApiDocumentation;
import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.repo.EntityRepository;

public class EntityControllerTest extends ApiDocumentation {

    private static final String TEST_ENTITY_1_CANONICAL_NAME = "Example";
    private static final String TEST_ENTITY_1_NOTES = "Example notes";
    private static final String TEST_ENTITY_1_URL_STRING = "http://www.example.com";

    private static final Entity TEST_ENTITY_1 = new Entity();

    static {
        try {
            TEST_ENTITY_1.setUrl(new URL(TEST_ENTITY_1_URL_STRING));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TEST_ENTITY_1.setCanonicalName(TEST_ENTITY_1_CANONICAL_NAME);
        TEST_ENTITY_1.setNotes(TEST_ENTITY_1_NOTES);
    }

    @Autowired
    private EntityRepository entityRepo;

    @Test
    public void testGetAllEntities() throws Exception {
        this.mockMvc.perform(
            get("/entity")
        )
            .andExpect(status().isOk())
            .andDo(document("get-all-entities"));
    }

    @Test
    public void testGetEntity() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc.perform(
            get("/entity/{id}", entity.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", entity.getId().toString())
        )
            .andExpect(status().isOk())
            .andDo(document("get-entity",
                responseFields(
                    fieldWithPath("id").description("The UUID id of the Entity"),
                    fieldWithPath("url").description("The URL of the Entity"),
                    fieldWithPath("canonicalName").description("The canonical name for the Entity"),
                    fieldWithPath("notes").description("Notes describing the Entity"),
                    subsectionWithPath("_links").description("A list of links associated with the Entity")
                ), links(
                    halLinks(),
                    linkWithRel("self").description("The canonical url for this Entity"),
                    linkWithRel("entity").description("A link to the Entity resource"),
                    linkWithRel("redirect").description("Redirects request to the URL associated with the Entity"),
                    linkWithRel("redirectByHead").description("Redirects HEAD requests to the URL associated with the Entity")
                )
            ));
    }

    @Test
    public void testCreateEntity() throws Exception {
        this.mockMvc.perform(
            post("/entity")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(TEST_ENTITY_1))
            )
            .andExpect(status().isCreated())
            .andDo(document("create-entity",
                requestFields(
                    fieldWithPath("url").description("The URL of the Entity"),
                    fieldWithPath("canonicalName").description("The canonical name for the Entity"),
                    fieldWithPath("notes").description("Notes describing the Entity")
                )));
    }

    // @Test
    // public void testUpdateEntity() {

    // }

    @AfterEach
    public void cleanUp() {
        entityRepo.deleteAll();
    }
}
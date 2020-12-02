package edu.tamu.eider.app.web;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.repo.EntityRepository;
import edu.tamu.eider.resources.EntityTestData;

public class EntityResolverControllerTest extends EntityTestData {

    @Autowired
    private EntityRepository entityRepo;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testCreateWithUrl() throws Exception {
        this.mockMvc
            .perform(post("/entity")
                .param("url", "http://www.resolve.com")
                .accept(MediaType.TEXT_PLAIN)
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.TEXT_PLAIN))
            .andDo(document("create-with-url",
                requestParameters(
                    parameterWithName("url").description("The url of the desired Entity")
                )
            ));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testCreateWithUrlAlreadyExists() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc
            .perform(post("/entity")
                .param("url", entity.getUrl().toString())
                .accept(MediaType.TEXT_PLAIN)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.TEXT_PLAIN))
            .andExpect(content().string(entity.getId().toString()))
            .andDo(document("create-with-url",
                requestParameters(
                    parameterWithName("url").description("The url of the desired Entity")
                )
            ));
    }

    @Test
    public void testResolveId() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc
            .perform(get("/entity/{id}", entity.getId())
                .accept(MediaType.TEXT_PLAIN)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.TEXT_PLAIN))
            .andExpect(content().string(entity.getUrl().toString()))
            .andDo(document("resolve-id",
                pathParameters(
                    parameterWithName("id").description("The id on the desired Entity")
                )
            ));
    }

    @Test
    public void testResolveIdNotFound() throws Exception {
        this.mockMvc
            .perform(get("/entity/{id}", UUID.randomUUID())
                .accept(MediaType.TEXT_PLAIN)
            )
            .andExpect(status().isNotFound());
    }

    @Test
    public void testResolveUrl() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc
            .perform(get("/entity").param("url", entity.getUrl().toString())
                .accept(MediaType.TEXT_PLAIN)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.TEXT_PLAIN))
            .andExpect(content().string(entity.getId().toString()))
            .andDo(document("resolve-url",
                requestParameters(
                    parameterWithName("url").description("The url on the desired Entity")
                )
            ));
    }

    @Test
    public void testResolveUrlNotFound() throws Exception {
        this.mockMvc
            .perform(get("/entity").param("url", "http://www.not-found.com")
                .accept(MediaType.TEXT_PLAIN)
            )
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testDeleteById() throws Exception {
        Entity entity = entityRepo.save(TEST_ENTITY_1);
        this.mockMvc
            .perform(delete("/entity/{id}", entity.getId())
                .accept(MediaType.TEXT_PLAIN)
            )
            .andExpect(status().isNoContent())
            .andDo(document("delete-by-id",
                pathParameters(
                    parameterWithName("id").description("The id on the desired Entity")
                )
            ));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void testDeleteByIdNotFound() throws Exception {
        this.mockMvc
            .perform(delete("/entity/{id}", UUID.randomUUID().toString())
                .accept(MediaType.TEXT_PLAIN)
            )
            .andExpect(status().isNotFound());
    }

    @AfterEach
    public void cleanUp() {
        entityRepo.deleteAll();
        // null out entity id as entity id is assigned after save
        TEST_ENTITY_1.setId(null);
    }

}
